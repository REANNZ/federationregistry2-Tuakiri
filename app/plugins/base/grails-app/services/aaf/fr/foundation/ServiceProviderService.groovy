package aaf.fr.foundation

import org.springframework.context.i18n.LocaleContextHolder as LCH
import org.springframework.transaction.interceptor.TransactionAspectSupport

import aaf.fr.workflow.ProcessPriority

/**
 * Provides methods for managing SPSSODescriptor instances.
 *
 * @author Bradley Beddoes
 */
class ServiceProviderService {
  static transactional = true

  def cryptoService
  def entityDescriptorService
  def workflowProcessService

  def create(def params) {
    def organization = Organization.lock(params.organization?.id)

    def contact
    if(params.contact?.email)
        contact = Contact.findByEmail(params.contact?.email)       // We may already have them referenced by email

    if(!contact) {
      contact = new Contact(givenName: params.contact?.givenName, surname: params.contact?.surname, email: params.contact?.email, organization:organization)
      contact.save()
      if(contact.hasErrors()) {
          log.info "$subject attempted to create service provider but contact details supplied were invalid"
          contact.errors.each { log.debug it }
      }
    }

    def ct = params.contact?.type ?: 'technical'

    def entityDescriptor
    if(params.entity?.id) {
      entityDescriptor = EntityDescriptor.lock(params.entity?.id)
    }

    if(!entityDescriptor) {
      def created
      (created, entityDescriptor) = entityDescriptorService.createNoSave(params)  // Odd issues with transactions cross services not rolling back
    }

    def serviceProvider = new SPSSODescriptor(approved:false, active:params.active, displayName: params.sp?.displayName, description: params.sp?.description, organization: organization, authnRequestsSigned:false, wantAssertionsSigned:false)

    def saml2Namespace = SamlURI.findByUri('urn:oasis:names:tc:SAML:2.0:protocol')
    serviceProvider.addToProtocolSupportEnumerations(saml2Namespace)

    def nameID = SamlURI.findWhere(uri:'urn:oasis:names:tc:SAML:2.0:nameid-format:transient')
    if(nameID) {
      serviceProvider.addToNameIDFormats(nameID)
    }

    def acs = new AttributeConsumingService(approved:true, lang:params.lang ?:'en')
    if(params.sp?.displayName)
            acs.addToServiceNames(params.sp.displayName)

    def supportedAttributes = []
    params.sp.attributes.each { a ->
      def attrID = a.key
      def val = a.value

      if(!( val instanceof String )) {
        if(val.requested && val.requested == "on") {
          def attr = AttributeBase.get(attrID)
          if(attr) {
            def ra = new RequestedAttribute(approved:true, base:attr, reasoning: val.reasoning, isRequired: (val.required == 'on') )
            supportedAttributes.add(ra)
            acs.addToRequestedAttributes(ra)

            if(val.requestedvalues) {
              val.requestedvalues.each {
                if(it.value && it.value.size() > 0)
                  ra.addToValues( new AttributeValue(value: it.value) )
              }
            }
          }
        }
      }
    }
    serviceProvider.addToAttributeConsumingServices(acs)

    def spContactPerson = new ContactPerson(contact:contact, type:ContactType.findByName(ct))
    serviceProvider.addToContacts(spContactPerson)

    // Initial endpoints
    // Assertion Consumer Services
    def postBinding = SamlURI.findByUri(SamlConstants.httpPost)
    def postLocation = params.sp?.acs?.post
    def httpPostACS = new AssertionConsumerService(approved: true, binding: postBinding, location:postLocation,
      index:params.sp?.acs?.'post-index', active:params.active, isDefault:(params.sp?.acs?.'post-isdefault' == 'true') ? true:false)
    serviceProvider.addToAssertionConsumerServices(httpPostACS)
    httpPostACS.validate()

    def artifactBinding = SamlURI.findByUri(SamlConstants.httpArtifact)
    def artifactLocation = params.sp?.acs?.artifact
    def httpArtifactACS = new AssertionConsumerService(approved: true, binding: artifactBinding, location:artifactLocation,
      index:params.sp?.acs?.'artifact-index', active:params.active, isDefault:(params.sp?.acs?.'artifact-isdefault' == 'true') ? true:false)
    serviceProvider.addToAssertionConsumerServices(httpArtifactACS)
    httpArtifactACS.validate()

    // Single Logout Services
    def sloArtifact, sloRedirect, sloSOAP, sloPost
    if(params.sp?.slo?.artifact){
      def sloArtifactBinding = SamlURI.findByUri(SamlConstants.httpArtifact)
      def sloArtifactLocation = params.sp?.slo?.artifact
      sloArtifact = new SingleLogoutService(approved: true, binding: sloArtifactBinding, location:sloArtifactLocation, active:params.active)
      serviceProvider.addToSingleLogoutServices(sloArtifact)
      sloArtifact.validate()
    }
    if(params.sp?.slo?.redirect){
      def sloRedirectBinding = SamlURI.findByUri(SamlConstants.httpRedirect)
      def sloRedirectLocation = params.sp?.slo?.redirect
      sloRedirect = new SingleLogoutService(approved: true, binding: sloRedirectBinding, location:sloRedirectLocation, active:params.active)
      serviceProvider.addToSingleLogoutServices(sloRedirect)
      sloRedirect.validate()
    }
    if(params.sp?.slo?.soap){
      def sloSOAPBinding = SamlURI.findByUri(SamlConstants.soap)
      def sloSOAPLocation = params.sp?.slo?.soap
      sloSOAP = new SingleLogoutService(approved: true, binding: sloSOAPBinding, location:sloSOAPLocation, active:params.active)
      serviceProvider.addToSingleLogoutServices(sloSOAP)
      sloSOAP.validate()
    }
    if(params.sp?.slo?.post){
      def sloPostBinding = SamlURI.findByUri(SamlConstants.httpPost)
      def sloPostLocation = params.sp?.slo?.post
      sloPost = new SingleLogoutService(approved: true, binding: sloPostBinding, location:sloPostLocation, active:params.active)
      serviceProvider.addToSingleLogoutServices(sloPost)
      sloPost.validate()
    }

    // Manage NameID Services - optional
    def mnidArtifact, mnidRedirect, mnidSOAP, mnidPost
    if(params.sp?.mnid?.artifact){
      def mnidArtifactBinding = SamlURI.findByUri(SamlConstants.httpArtifact)
      def mnidArtifactLocation = params.sp?.mnid?.artifact
      mnidArtifact = new ManageNameIDService(approved: true, binding: mnidArtifactBinding, location:mnidArtifactLocation, active:params.active)
      serviceProvider.addToManageNameIDServices(mnidArtifact)
      mnidArtifact.validate()
    }
    if(params.sp?.mnid?.redirect){
      def mnidRedirectBinding = SamlURI.findByUri(SamlConstants.httpRedirect)
      def mnidRedirectLocation = params.sp?.mnid?.redirect
      mnidRedirect  = new ManageNameIDService(approved: true, binding: mnidRedirectBinding, location:mnidRedirectLocation, active:params.active)
      serviceProvider.addToManageNameIDServices(mnidRedirect)
      mnidRedirect.validate()
    }
    if(params.sp?.mnid?.soap){
      def mnidSOAPBinding = SamlURI.findByUri(SamlConstants.soap)
      def mnidSOAPLocation = params.sp?.mnid?.soap
      mnidSOAP = new ManageNameIDService(approved: true, binding: mnidSOAPBinding, location:mnidSOAPLocation, active:params.active)
      serviceProvider.addToManageNameIDServices(mnidSOAP)
      mnidSOAP.validate()
    }
    if(params.sp?.mnid?.post){
      def mnidPostBinding = SamlURI.findByUri(SamlConstants.httpPost)
      def mnidPostLocation = params.sp?.mnid?.post
      mnidPost = new ManageNameIDService(approved: true, binding: mnidPostBinding, location:mnidPostLocation, active:params.active)
      serviceProvider.addToManageNameIDServices(mnidPost)
      mnidPost.validate()
    }

    // Discovery Response Services - optional
    def discoveryResponseService
    if(params.sp?.drs){
      def drsBinding = SamlURI.findByUri(SamlConstants.drs)
      def drsLocation = params.sp?.drs
      discoveryResponseService = new DiscoveryResponseService(approved: true, binding: drsBinding, location:drsLocation, active:params.active, isDefault:(params.sp?.'drs-isdefault' == 'true'))
      serviceProvider.addToDiscoveryResponseServices(discoveryResponseService)
      discoveryResponseService.validate()
    }

    // Cryptography
    // Signing
    def certSIG = cryptoService.createCertificate(params.sigcert)
    if(certSIG) {
      def keyInfo = new KeyInfo(certificate: certSIG)
      def keyDescriptor = new KeyDescriptor(keyInfo:keyInfo, keyType:KeyTypes.signing, encryptionMethod:null)
      keyDescriptor.roleDescriptor = serviceProvider
      serviceProvider.addToKeyDescriptors(keyDescriptor)
    } else {
      serviceProvider.errors.rejectValue('keyDescriptors', 'aaf.fr.foundation.SPSSODescriptor.crypto.signing.invalid')
    }

    // Encryption
    if(params.enccert) {
      def certEnc = cryptoService.createCertificate(params.enccert)
      if(certEnc) {
        def keyInfoEnc = new KeyInfo(certificate:certEnc)
        def keyDescriptorEnc = new KeyDescriptor(keyInfo:keyInfoEnc, keyType:KeyTypes.encryption, encryptionMethod:null)
        keyDescriptorEnc.roleDescriptor = serviceProvider
        serviceProvider.addToKeyDescriptors(keyDescriptorEnc)
      }
      else {
        serviceProvider.errors.rejectValue('keyDescriptors', 'aaf.fr.foundation.SPSSODescriptor.crypto.encryption.invalid')
      }
    }

    // Service Description
    def serviceDescription = new ServiceDescription(connectURL: params.sp?.servicedescription?.connecturl, logoURL: params.sp?.servicedescription?.logourl, descriptor: serviceProvider)
    serviceProvider.serviceDescription = serviceDescription

    // Generate return map
    def ret = [:]
    ret.organization = organization
    ret.entityDescriptor = entityDescriptor
    ret.serviceProvider = serviceProvider
    ret.hostname = params.hostname
    ret.httpPostACS = httpPostACS
    ret.httpArtifactACS = httpArtifactACS
    ret.sloArtifact = sloArtifact
    ret.sloRedirect = sloRedirect
    ret.sloSOAP = sloSOAP
    ret.sloPost = sloPost
    ret.mnidArtifact = mnidArtifact
    ret.mnidRedirect = mnidRedirect
    ret.mnidSOAP = mnidSOAP
    ret.mnidPost = mnidPost
    ret.discoveryResponseService = discoveryResponseService
    ret.contact = contact
    ret.sigcert = params.sigcert
    ret.enccert = params.enccert
    ret.supportedAttributes = supportedAttributes

    entityDescriptor.addToSpDescriptors(serviceProvider)

    if(!serviceProvider.hasErrors()) {
      serviceProvider.validate()
    }

    // Check contact explicitly to avoid TransientObjectException
    if(!entityDescriptor.validate() || contact.hasErrors() || serviceProvider.hasErrors()) {
      log.info "$subject attempted to create $serviceProvider but failed input validation"
      entityDescriptor.errors.each {log.debug it}
      TransactionAspectSupport.currentTransactionStatus().setRollbackOnly()
      return [false, ret]
    }

    // Force flush as we need identifiers
    if(!entityDescriptor.save(flush:true)) {
      log.info "$subject attempted to create $serviceProvider but failed save attempt"
      entityDescriptor.errors.each {log.debug it}
      TransactionAspectSupport.currentTransactionStatus().setRollbackOnly()
      return [false, ret]
    }

    def workflowParams = [ creator:contact?.id?.toString(), serviceProvider:serviceProvider?.id?.toString(), organization:organization.id?.toString(), locale:LCH.getLocale().getLanguage() ]
    def (initiated, processInstance) = workflowProcessService.initiate( "spssodescriptor_create", "Approval for creation of ${serviceProvider}", ProcessPriority.MEDIUM, workflowParams)

    if(initiated)
      workflowProcessService.run(processInstance)
    else
      throw new ErronousStateException("Unable to execute workflow when creating ${serviceProvider}")

    log.info "$subject created $serviceProvider"
    return [true, ret]
  }


  def update(def params) {
    def serviceProvider = SPSSODescriptor.get(params.id)
    if(!serviceProvider)
      return [false, null]

    serviceProvider.attributeConsumingServices.each {
      it.removeFromServiceNames(serviceProvider.displayName)
      it.addToServiceNames(params.sp?.displayName)
    }

    serviceProvider.displayName = params.sp.displayName
    serviceProvider.description = params.sp.description

    if(params.sp.status == 'true') {
      serviceProvider.active = true
      serviceProvider.entityDescriptor.active = true
    }
    else {
      serviceProvider.active = false
      def entityDescriptor = serviceProvider.entityDescriptor
      if(entityDescriptor.holdsSPOnly()) {
        entityDescriptor.active = false
      }
    }

    serviceProvider.serviceDescription.connectURL = params.sp?.servicedescription?.connecturl
    serviceProvider.serviceDescription.logoURL = params.sp?.servicedescription?.logourl
    serviceProvider.serviceDescription.furtherInfo = params.sp?.servicedescription?.furtherinfo
    serviceProvider.serviceDescription.provides = params.sp?.servicedescription?.provides
    serviceProvider.serviceDescription.benefits = params.sp?.servicedescription?.benefits
    serviceProvider.serviceDescription.audience = params.sp?.servicedescription?.audience
    serviceProvider.serviceDescription.restrictions = params.sp?.servicedescription?.restrictions
    serviceProvider.serviceDescription.accessing = params.sp?.servicedescription?.accessing
    serviceProvider.serviceDescription.support = params.sp?.servicedescription?.support
    serviceProvider.serviceDescription.maintenance = params.sp?.servicedescription?.maintenance

    if(!serviceProvider.entityDescriptor.validate()) {
      log.info "$subject attempted to update $serviceProvider but failed EntityDescriptor validation"
      serviceProvider.entityDescriptor.errors.each {log.warn it}
      return [false, serviceProvider]
    }

    if(!serviceProvider.entityDescriptor.save()) {
      serviceProvider.errors.each {log.warn it}
      throw new ErronousStateException("Unable to save when updating ${serviceProvider}")
    }

    log.info "$subject updated $serviceProvider"
    return [true, serviceProvider]
  }

  def delete(long id) {
    def serviceProvider = SPSSODescriptor.get(id)
    if(!serviceProvider)
      throw new ErronousStateException("Unable to delete service provider, no such instance")

    log.info "Deleting $serviceProvider on request of $subject"
    def entityDescriptor = serviceProvider.entityDescriptor

    serviceProvider.discoveryResponseServices?.each { it.delete() }
    serviceProvider.contacts?.each { it.delete() }
    serviceProvider.keyDescriptors?.each { it.delete() }
    serviceProvider.monitors?.each { it.delete() }

    entityDescriptor.spDescriptors.remove(serviceProvider)
    serviceProvider.delete()

    log.info "$subject deleted $serviceProvider"
  }

  def archive(long id) {
    def serviceProvider = SPSSODescriptor.get(id)
    if(!serviceProvider)
      throw new ErronousStateException("Unable to find SPSSODescriptor with id $id")

    serviceProvider.archived = true
    serviceProvider.active = false
    if(!serviceProvider.save()) {
      log.error "Unable to archive $serviceProvider"
      serviceProvider.errors.each { log.error it }
      throw new ErronousStateException("Unable to archive SPSSODescriptor with id $id")
    }

    log.info "$subject successfully archived $serviceProvider"
  }

  def unarchive(long id) {
    def serviceProvider = SPSSODescriptor.get(id)
    if(!serviceProvider)
      throw new ErronousStateException("Unable to find SPSSODescriptor with id $id")

    serviceProvider.archived = false
    if(!serviceProvider.save()) {
      log.error "Unable to unarchive $serviceProvider"
      serviceProvider.errors.each { log.error it }
      throw new ErronousStateException("Unable to unarchive SPSSODescriptor with id $id")
    }

    log.info "$subject successfully unarchived $serviceProvider"
  }

  def activate(long id) {
    def serviceProvider = SPSSODescriptor.get(id)
    if(!serviceProvider)
      throw new ErronousStateException("Unable to find SPSSODescriptor with id $id")

    serviceProvider.active = true
    if(!serviceProvider.save()) {
      log.error "Unable to activate $serviceProvider"
      serviceProvider.errors.each { log.error it }
      throw new ErronousStateException("Unable to activate SPSSODescriptor with id $id")
    }

    log.info "$subject successfully activate $serviceProvider"
  }
}
