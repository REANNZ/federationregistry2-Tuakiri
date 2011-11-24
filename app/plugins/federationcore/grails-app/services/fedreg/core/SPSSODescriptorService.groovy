package fedreg.core

import org.springframework.context.i18n.LocaleContextHolder as LCH
import org.springframework.transaction.interceptor.TransactionAspectSupport

import fedreg.workflow.ProcessPriority

/**
 * Provides methods for managing SPSSODescriptor instances.
 *
 * @author Bradley Beddoes
 */
class SPSSODescriptorService {

	def cryptoService
	def entityDescriptorService
	def workflowProcessService

	def create(def params) {
			
        // Organization
        def organization = Organization.lock(params.organization?.id)

        // Contact
        def contact = Contact.get(params.contact?.id)
        if(!contact) {
            if(params.contact?.email)
                contact = Contact.findByEmail(params.contact?.email)       // We may already have them referenced by email
            
            if(!contact)
                contact = new Contact(givenName: params.contact?.givenName, surname: params.contact?.surname, email: params.contact?.email, organization:organization)
                contact.save()
                if(contact.hasErrors()) {
                    log.info "$authenticatedUser attempted to create serviceProvider but contact details supplied were invalid"
                    contact.errors.each { log.debug it }
                }
        }
        def ct = params.contact?.type ?: 'administrative'
	
		// Entity Descriptor
		def entityDescriptor
		if(params.entity?.id) {		
			entityDescriptor = EntityDescriptor.lock(params.entity?.id)
		}
	
		if(!entityDescriptor) {
			def created
			(created, entityDescriptor) = entityDescriptorService.createNoSave(params)	// Odd issues with transactions cross services not rolling back
		}
	
		// SP
		def saml2Namespace = SamlURI.findByUri('urn:oasis:names:tc:SAML:2.0:protocol')
		def serviceProvider = new SPSSODescriptor(approved:false, active:params.active, displayName: params.sp?.displayName, description: params.sp?.description, organization: organization, authnRequestsSigned:false, wantAssertionsSigned:false)
		
		serviceProvider.addToProtocolSupportEnumerations(saml2Namespace)
	
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
	
		def supportedNameIDFormats = []
		params.sp.nameidformats.each { nameFormatID -> 
			if(nameFormatID.value == "on") {
				def nameid = SamlURI.get(nameFormatID.key)
				if(nameid) {
					supportedNameIDFormats.add(nameid)
					serviceProvider.addToNameIDFormats(nameid)
				}
			}
		}
		def spContactPerson = new ContactPerson(contact:contact, type:ContactType.findByName(ct))
		serviceProvider.addToContacts(spContactPerson)
	
		// Initial endpoints
		// Assertion Consumer Services
		def postBinding = SamlURI.findByUri(SamlConstants.httpPost)
		def postLocation = params.sp?.acs?.post?.uri
		def httpPostACS = new AssertionConsumerService(approved: true, binding: postBinding, location:postLocation, index:params.sp?.acs?.post?.index, active:params.active, isDefault:(params.sp?.acs?.post?.isdefault == 'true') ? true:false)
		serviceProvider.addToAssertionConsumerServices(httpPostACS)
		httpPostACS.validate()

		def artifactBinding = SamlURI.findByUri(SamlConstants.httpArtifact)
		def artifactLocation = params.sp?.acs?.artifact?.uri
		def httpArtifactACS = new AssertionConsumerService(approved: true, binding: artifactBinding, location:artifactLocation, index:params.sp?.acs?.artifact?.index, active:params.active, isDefault:(params.sp?.acs?.artifact?.isdefault == 'true') ? true:false)
		serviceProvider.addToAssertionConsumerServices(httpArtifactACS)
		httpArtifactACS.validate()
	
		// Single Logout Services
		def sloArtifact, sloRedirect, sloSOAP, sloPost
		if(params.sp?.slo?.artifact?.uri){
			def sloArtifactBinding = SamlURI.findByUri(SamlConstants.httpArtifact)
			def sloArtifactLocation = params.sp?.slo?.artifact?.uri
			sloArtifact = new SingleLogoutService(approved: true, binding: sloArtifactBinding, location:sloArtifactLocation, active:params.active)
			serviceProvider.addToSingleLogoutServices(sloArtifact)
			sloArtifact.validate()
		}
		if(params.sp?.slo?.redirect?.uri){
			def sloRedirectBinding = SamlURI.findByUri(SamlConstants.httpRedirect)
			def sloRedirectLocation = params.sp?.slo?.redirect?.uri
			sloRedirect	= new SingleLogoutService(approved: true, binding: sloRedirectBinding, location:sloRedirectLocation, active:params.active)
			serviceProvider.addToSingleLogoutServices(sloRedirect)
			sloRedirect.validate()
		}
		if(params.sp?.slo?.soap?.uri){
			def sloSOAPBinding = SamlURI.findByUri(SamlConstants.soap)
			def sloSOAPLocation = params.sp?.slo?.soap?.uri
			sloSOAP = new SingleLogoutService(approved: true, binding: sloSOAPBinding, location:sloSOAPLocation, active:params.active)
			serviceProvider.addToSingleLogoutServices(sloSOAP)
			sloSOAP.validate()
		}
		if(params.sp?.slo?.post?.uri){
			def sloPostBinding = SamlURI.findByUri(SamlConstants.httpPost)
			def sloPostLocation = params.sp?.slo?.post?.uri
			sloPost = new SingleLogoutService(approved: true, binding: sloPostBinding, location:sloPostLocation, active:params.active)
			serviceProvider.addToSingleLogoutServices(sloPost)
			sloPost.validate()
		}
	
		// Manage NameID Services - optional
		def mnidArtifact, mnidRedirect, mnidSOAP, mnidPost
		if(params.sp?.mnid?.artifact?.uri){
			def mnidArtifactBinding = SamlURI.findByUri(SamlConstants.httpArtifact)
			def mnidArtifactLocation = params.sp?.mnid?.artifact?.uri
			mnidArtifact = new ManageNameIDService(approved: true, binding: mnidArtifactBinding, location:mnidArtifactLocation, active:params.active)
			serviceProvider.addToManageNameIDServices(mnidArtifact)
			mnidArtifact.validate()
		}
		if(params.sp?.mnid?.redirect?.uri){
			def mnidRedirectBinding = SamlURI.findByUri(SamlConstants.httpRedirect)
			def mnidRedirectLocation = params.sp?.mnid?.redirect?.uri
			mnidRedirect	= new ManageNameIDService(approved: true, binding: mnidRedirectBinding, location:mnidRedirectLocation, active:params.active)
			serviceProvider.addToManageNameIDServices(mnidRedirect)
			mnidRedirect.validate()
		}
		if(params.sp?.mnid?.soap?.uri){
			def mnidSOAPBinding = SamlURI.findByUri(SamlConstants.soap)
			def mnidSOAPLocation = params.sp?.mnid?.soap?.uri
			mnidSOAP = new ManageNameIDService(approved: true, binding: mnidSOAPBinding, location:mnidSOAPLocation, active:params.active)
			serviceProvider.addToManageNameIDServices(mnidSOAP)
			mnidSOAP.validate()
		}
		if(params.sp?.mnid?.post?.uri){
			def mnidPostBinding = SamlURI.findByUri(SamlConstants.httpPost)
			def mnidPostLocation = params.sp?.mnid?.post?.uri
			mnidPost = new ManageNameIDService(approved: true, binding: mnidPostBinding, location:mnidPostLocation, active:params.active)
			serviceProvider.addToManageNameIDServices(mnidPost)
			mnidPost.validate()
		}
	
		// Discovery Response Services - optional
		def discoveryResponseService
		if(params.sp?.drs?.uri){
			def drsBinding = SamlURI.findByUri(SamlConstants.drs)
			def drsLocation = params.sp?.drs?.uri
			discoveryResponseService = new DiscoveryResponseService(approved: true, binding: drsBinding, location:drsLocation, active:params.active, isDefault:true)
			serviceProvider.addToDiscoveryResponseServices(discoveryResponseService)
			discoveryResponseService.validate()
		}
	
		// Cryptography
		// Signing
		if(params.sp?.crypto?.sig) {
			def cert = cryptoService.createCertificate(params.cert)
			cryptoService.validateCertificate(cert)
			def keyInfo = new KeyInfo(certificate: cert)
			def keyDescriptor = new KeyDescriptor(keyInfo:keyInfo, keyType:KeyTypes.signing, roleDescriptor:serviceProvider)
			serviceProvider.addToKeyDescriptors(keyDescriptor)
		}
	
		// Encryption
		if(params.sp?.crypto?.enc) {
			def certEnc = cryptoService.createCertificate(params.cert)
			cryptoService.validateCertificate(certEnc)
			def keyInfoEnc = new KeyInfo(certificate:certEnc)
			def keyDescriptorEnc = new KeyDescriptor(keyInfo:keyInfoEnc, keyType:KeyTypes.encryption, roleDescriptor:serviceProvider)
			serviceProvider.addToKeyDescriptors(keyDescriptorEnc)
		}
	
		// Service Description
		def serviceDescription = new ServiceDescription(connectURL: params.sp?.servicedescription?.connecturl, logo: params.sp?.servicedescription?.logourl, descriptor: serviceProvider)
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
		ret.certificate = params.cert
		ret.supportedNameIDFormats = supportedNameIDFormats
		ret.supportedAttributes = supportedAttributes

		entityDescriptor.addToSpDescriptors(serviceProvider)
        serviceProvider.validate()
	
		// Force flush as we need identifiers
        if(!entityDescriptor.save(flush:true)) {
            log.info "$authenticatedUser attempted to create $serviceProvider but failed input validation"
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
	
		log.info "$authenticatedUser created $serviceProvider"
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
			log.info "$authenticatedUser attempted to update $serviceProvider but failed EntityDescriptor validation"
			serviceProvider.entityDescriptor.errors.each {log.warn it}
			return [false, serviceProvider]
		}
		
		if(!serviceProvider.entityDescriptor.save()) {
			serviceProvider.errors.each {log.warn it}
			throw new ErronousStateException("Unable to save when updating ${serviceProvider}")
		}
		
		log.info "$authenticatedUser updated $serviceProvider"
		return [true, serviceProvider]
	}
	
	def delete(long id) {
		def serviceProvider = SPSSODescriptor.get(id)
		if(!serviceProvider)
			throw new ErronousStateException("Unable to delete service provider, no such instance")
			
		log.info "Deleting $serviceProvider on request of $authenticatedUser"
		def entityDescriptor = serviceProvider.entityDescriptor

		serviceProvider.discoveryResponseServices?.each { it.delete() }
		serviceProvider.contacts?.each { it.delete() }
		serviceProvider.keyDescriptors?.each { it.delete() }
		serviceProvider.monitors?.each { it.delete() }

		entityDescriptor.spDescriptors.remove(serviceProvider)
		serviceProvider.delete()
		
		log.info "$authenticatedUser deleted $serviceProvider"
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
		
		log.info "$authenticatedUser successfully archived $serviceProvider"
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
		
		log.info "$authenticatedUser successfully unarchived $serviceProvider"
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
		
		log.info "$authenticatedUser successfully activate $serviceProvider"
	}
}