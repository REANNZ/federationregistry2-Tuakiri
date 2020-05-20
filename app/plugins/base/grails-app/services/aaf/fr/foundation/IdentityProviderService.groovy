package aaf.fr.foundation

import org.springframework.context.i18n.LocaleContextHolder as LCH
import org.springframework.transaction.interceptor.TransactionAspectSupport

import aaf.fr.workflow.ProcessPriority

/**
 * Provides methods for managing IDPSSODescriptor instances.
 *
 * @author Bradley Beddoes
 */
class IdentityProviderService {
  static transactional = true

  def cryptoService
  def workflowProcessService
  def entityDescriptorService
  def attributeAuthorityDescriptorService

  def create(def params) {

    /* There is a lot of moving parts when creating an IdP (+AA hybrid) so below is more complex then usual,
    you'll note validation calls on most larger objects, this is to get finer grained object error population */

    // Organization
    def organization = Organization.lock(params.organization?.id)

    // Contact
    def contact
    if(params.contact?.email)
        contact = Contact.findByEmail(params.contact?.email)       // We may already have them referenced by email

    if(!contact) {
      contact = new Contact(givenName: params.contact?.givenName, surname: params.contact?.surname, email: params.contact?.email, organization:organization)
      contact.save()
      if(contact.hasErrors()) {
          log.info "$subject attempted to create identity provider but contact details supplied were invalid"
          contact.errors.each { log.debug it }
      }
    }
    def ct = params.contact?.type ?: 'technical'

    // Entity Descriptor
    def entityDescriptor
    if(params.entity?.id && EntityDescriptor.exists(params.entity?.id)) {
      entityDescriptor = EntityDescriptor.lock(params.entity?.id)
    }

    if(!entityDescriptor) {
      def created
      (created, entityDescriptor) = entityDescriptorService.createNoSave(params)  // Odd issues with transactions cross services not rolling back
    }

    // IDP
    def saml2Namespace = SamlURI.findByUri('urn:oasis:names:tc:SAML:2.0:protocol')
    def identityProvider = new IDPSSODescriptor(approved:false, active:params.active, displayName: params.idp?.displayName, description: params.idp?.description, scope: params.idp?.scope, organization: organization)
    identityProvider.addToProtocolSupportEnumerations(saml2Namespace)

    def supportedAttributes = []
    params.idp.attributes.each { a ->
      if(a.value == "on") {
        def attr = AttributeBase.get(a.key)
        if(attr) {
          identityProvider.addToAttributes(new Attribute(base:attr))
          supportedAttributes.add(attr)
        }
      }
    }

    def nameID = SamlURI.findWhere(uri:'urn:oasis:names:tc:SAML:2.0:nameid-format:transient')
    if(nameID) {
      identityProvider.addToNameIDFormats(nameID)
    }

    def idpContactPerson = new ContactPerson(contact:contact, type:ContactType.findByName(ct))
    identityProvider.addToContacts(idpContactPerson)

    // Initial endpoints
    def postBinding = SamlURI.findByUri(SamlConstants.httpPost)
    def postLocation = params.idp?.post
    def httpPost = new SingleSignOnService(approved: true, binding: postBinding, location:postLocation, active:params.active)
    identityProvider.addToSingleSignOnServices(httpPost)
    httpPost.validate()

    def redirectBinding = SamlURI.findByUri(SamlConstants.httpRedirect)
    def redirectLocation = params.idp?.redirect
    def httpRedirect = new SingleSignOnService(approved: true, binding: redirectBinding, location:redirectLocation, active:params.active)
    identityProvider.addToSingleSignOnServices(httpRedirect)
    httpRedirect.validate()

    def ecp = null
    if(params.idp?.ecp) {
      def ecpBinding = SamlURI.findByUri(SamlConstants.soap)
      def ecpLocation = params.idp?.ecp
      ecp = new SingleSignOnService(approved: true, binding: ecpBinding, location:ecpLocation, active:params.active)
      identityProvider.addToSingleSignOnServices(ecp)
      ecp.validate()
    }

    // Cryptography
    // Signing
    def certSIG = cryptoService.createCertificate(params.sigcert)
    if(certSIG) {
      def keyInfo = new aaf.fr.foundation.KeyInfo(certificate: certSIG)
      def keyDescriptor = new aaf.fr.foundation.KeyDescriptor(keyInfo:keyInfo, keyType:KeyTypes.signing, encryptionMethod:null)
      keyDescriptor.roleDescriptor = identityProvider
      identityProvider.addToKeyDescriptors(keyDescriptor)
    }
    else {
      identityProvider.errors.rejectValue('keyDescriptors', 'aaf.fr.foundation.IDPSSODescriptor.crypto.signing.invalid')
    }

    // Encryption
    if(params.idp?.crypto?.enc && params.enccert) {
      def certEnc = cryptoService.createCertificate(params.enccert)
      if(certEnc) {
        def keyInfoEnc = new KeyInfo(certificate:certEnc)
        def keyDescriptorEnc = new aaf.fr.foundation.KeyDescriptor(keyInfo:keyInfoEnc, keyType:KeyTypes.encryption, encryptionMethod:null)
        keyDescriptorEnc.roleDescriptor = identityProvider
        identityProvider.addToKeyDescriptors(keyDescriptorEnc)
      }
      else {
        identityProvider.errors.rejectValue('keyDescriptors', 'aaf.fr.foundation.IDPSSODescriptor.crypto.encryption.invalid')
      }
    }

    // Attribute Authority - collaborates with created IDP
    def attributeAuthority
    if(params.aa?.create) {
      attributeAuthority = new AttributeAuthorityDescriptor(approved:false, active:params.active, displayName: params.idp?.displayName, description: params.idp?.description, scope: params.idp?.scope, organization:organization)
      attributeAuthority.addToProtocolSupportEnumerations(saml2Namespace)
    }

    // Generate return map
    def ret = [:]
    ret.organization = organization
    ret.entityDescriptor = entityDescriptor
    ret.identityProvider = identityProvider
    ret.attributeAuthority = attributeAuthority
    ret.hostname = params.hostname
    ret.scope = params.idp?.scope
    ret.httpPost = httpPost
    ret.httpRedirect = httpRedirect
    ret.ecp = ecp
    ret.contact = contact
    ret.sigcert = params.sigcert
    ret.bccert = params.bccert
    ret.enccert = params.enccert
    ret.supportedAttributes = supportedAttributes

    entityDescriptor.addToIdpDescriptors(identityProvider)
    if(params.aa?.create) {
      entityDescriptor.addToAttributeAuthorityDescriptors(attributeAuthority)
    }

    if(!identityProvider.hasErrors()) {
      identityProvider.validate()
      if(params.aa?.create) {
          attributeAuthority.validate()
      }
    }

    // Check contact explicitly to avoid TransientObjectException
    if(!entityDescriptor.validate() || contact.hasErrors() || identityProvider.hasErrors() ) {
            log.info "$subject attempted to create $identityProvider but failed input validation"
      entityDescriptor.errors.each {log.debug it}
      TransactionAspectSupport.currentTransactionStatus().setRollbackOnly()
            return [false, ret]
    }

    // Force flush as we need identifiers
    if(!entityDescriptor.save(flush:true)) {
            log.info "$subject attempted to create $identityProvider but failed save attempt"
      entityDescriptor.errors.each {log.debug it}
      TransactionAspectSupport.currentTransactionStatus().setRollbackOnly()
            return [false, ret]
    }

    if(params.aa?.create) {
      // We have to establish this relationship after the initial save so everything is setup
      identityProvider.collaborator = attributeAuthority
      attributeAuthority.collaborator = identityProvider

      if(!entityDescriptor.save()) {
        log.info "$subject attempted to create $identityProvider but failed collaborator establishment"
        entityDescriptor.errors.each {log.debug it}
        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly()
        return [false, ret]
      }
    }

    def workflowParams = [ creator:contact?.id?.toString(), identityProvider:identityProvider?.id?.toString(), attributeAuthority:attributeAuthority?.id?.toString(), organization:organization.id?.toString(), locale:LCH.getLocale().getLanguage() ]

    def (initiated, processInstance) = workflowProcessService.initiate( "idpssodescriptor_create", "Approval for creation of ${identityProvider}", ProcessPriority.MEDIUM, workflowParams)

    if(initiated)
      workflowProcessService.run(processInstance)
    else
      throw new ErronousStateException("Unable to execute workflow when creating ${identityProvider}")

    log.info "$subject created $identityProvider"
    return [true, ret]
  }

  def update(def params) {
    def identityProvider = IDPSSODescriptor.get(params.id)
    if(!identityProvider)
      return [false, null]

    identityProvider.displayName = params.idp.displayName
    identityProvider.description = params.idp.description
    identityProvider.scope = params.idp.scope

    if(identityProvider.collaborator) {
        identityProvider.collaborator.displayName = params.idp.displayName
        identityProvider.collaborator.description = params.idp.description
        identityProvider.collaborator.scope = params.idp.scope
    }

    if(params.idp.status == 'true') {
      identityProvider.active = true
      identityProvider.collaborator?.active = true
      identityProvider.entityDescriptor.active = true
    }
    else {
      identityProvider.active = false
      identityProvider.collaborator?.active = false
      def entityDescriptor = identityProvider.entityDescriptor
      if(entityDescriptor.holdsIDPOnly()) {
        entityDescriptor.active = false
      }
    }

    identityProvider.autoAcceptServices = params.idp.autoacceptservices == 'true'
    identityProvider.attributeAuthorityOnly = params.idp.attributeauthorityonly == 'true'

    log.debug "Updating $identityProvider active: ${identityProvider.active}, requestSigned: ${identityProvider.wantAuthnRequestsSigned}"

    if(!identityProvider.entityDescriptor.validate()) {
      log.info "$subject attempted to update $identityProvider but failed IDPSSODescriptor validation"
      identityProvider.entityDescriptor.errors.each {log.warn it; print it}
      return [false, identityProvider]
    }

    if(!identityProvider.entityDescriptor.save()) {
      identityProvider.entityDescriptor.errors.each {log.warn it}
      throw new ErronousStateException("Unable to save when updating ${identityProvider}")
    }

    log.info "$subject updated $identityProvider"
    return [true, identityProvider]
  }

  def delete(long id) {
    def identityProvider = IDPSSODescriptor.get(id)
    if(!identityProvider)
      throw new ErronousStateException("Unable to delete identity provider, no such instance")

    log.info "Deleting $identityProvider on request of $subject"

    def entityDescriptor = identityProvider.entityDescriptor
    def aa = identityProvider.collaborator

    if(aa) {
      // Untangle the IDPSSODescriptor <==> AAD collaborator linkage.
      // Otherwise, delete would break on failed foreign key constraints.
      identityProvider.collaborator = null
      aa.collaborator = null

      // Explicitly flush these changes - otherwise, Hibernate would skip the UPDATES
      // if it sees a subsequent DELETE - and the FK constraints would break.
      identityProvider.save(flush: true)
      aa.save(flush: true)

      // WORKAROUND: for uknown reasons, only the AA change gets flushed, not the IdP change.
      // So we can delete the IDP - and once it is deleted, we can delete the AA.
      // Delay AA delete until after IDP is deleted.
    }

    identityProvider.attributeProfiles?.each { it.delete() }
    identityProvider.attributes?.each { it.delete() }
    identityProvider.contacts?.each { it.delete() }
    identityProvider.keyDescriptors?.each { it.delete() }
    identityProvider.monitors?.each { it.delete() }

    entityDescriptor.idpDescriptors.remove(identityProvider)
    identityProvider.delete()

    log.info "$subject deleted $identityProvider"

    if(aa) {  // Delayed delete
      aa.attributeServices?.each { it.delete() }
      aa.assertionIDRequestServices?.each { it.delete() }
      aa.attributes?.each { it.delete() }
      aa.contacts?.each { it.delete() }
      aa.keyDescriptors?.each { it.delete() }
      aa.monitors?.each { it.delete() }

      entityDescriptor.attributeAuthorityDescriptors.remove(aa)

      log.info "$subject deleted $aa"
      aa.delete()
    }
  }

  def archive(long id) {
    def identityProvider = IDPSSODescriptor.get(id)
    if(!identityProvider)
      throw new ErronousStateException("Unable to find IDPSSODescriptor with id $id")

    identityProvider.archived = true
    identityProvider.active = false
    if(!identityProvider.save()) {
      log.error "Unable to archive $identityProvider"
      identityProvider.errors.each { log.error it }
      throw new ErronousStateException("Unable to archive IDPSSODescriptor with id $id")
    }

    log.info "$subject successfully archived $identityProvider"
  }

  def unarchive(long id) {
    def identityProvider = IDPSSODescriptor.get(id)
    if(!identityProvider)
      throw new ErronousStateException("Unable to find IDPSSODescriptor with id $id")

    identityProvider.archived = false
    if(!identityProvider.save()) {
      log.error "Unable to unarchive $identityProvider"
      identityProvider.errors.each { log.error it }
      throw new ErronousStateException("Unable to unarchive IDPSSODescriptor with id $id")
    }

    log.info "$subject successfully unarchived $identityProvider"
  }

  def activate(long id) {
    def identityProvider = IDPSSODescriptor.get(id)
    if(!identityProvider)
      throw new ErronousStateException("Unable to find IDPSSODescriptor with id $id")

    identityProvider.active = true
    if(!identityProvider.save()) {
      log.error "Unable to activate $identityProvider"
      identityProvider.errors.each { log.error it }
      throw new ErronousStateException("Unable to activate IDPSSODescriptor with id $id")
    }

    log.info "$subject successfully activate $identityProvider"
  }

}
