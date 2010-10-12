package fedreg.core

import org.springframework.context.i18n.LocaleContextHolder as LCH
import org.springframework.transaction.interceptor.TransactionAspectSupport

import fedreg.workflow.ProcessPriority

class IDPSSODescriptorService {
	
	def cryptoService
	def workflowProcessService
	def entityDescriptorService
	
	def create(def params) {
			
		/* There is a lot of moving parts when creating an IdP (+AA hybrid) so below is more complex then usual,
		you'll note validation calls on most larger objects, this is to get finer grained object error population */
	
		// Organization
		def organization = Organization.get(params.organization?.id)

		// Contact
		def contact = Contact.get(params.contact?.id)
		if(!contact) {
			if(params.contact?.email)
				contact = MailURI.findByUri(params.contact?.email)?.contact		// We may already have them referenced by email address and user doesn't realize
			if(!contact)
				contact = new Contact(givenName: params.contact?.givenName, surname: params.contact?.surname, email: new MailURI(uri:params.contact?.email), organization:organization)
				contact.save()
				if(contact.hasErrors()) {
					contact.errors.each {
						log.error it
					}
					throw new RuntimeException("Unable to create new contact when attempting to create new IDPSSODescriptor")
				}
		}
		def ct = params.contact?.type ?: 'administrative'
	
		// Entity Descriptor
		def entityDescriptor
		if(params.entity?.id) {		
			entityDescriptor = EntityDescriptor.get(params.entity?.id)
		}
	
		if(!entityDescriptor) {
			def created
			(created, entityDescriptor) = entityDescriptorService.createNoSave(params)	// Odd issues with transactions cross services not rolling back so we save here
		}
	
		// IDP
		def samlNamespace = SamlURI.findByUri('urn:oasis:names:tc:SAML:2.0:protocol')
		def identityProvider = new IDPSSODescriptor(approved:false, active:params.active, displayName: params.idp?.displayName, description: params.idp?.description, scope: params.idp?.scope, organization: organization, wantAuthnRequestsSigned:true)
		identityProvider.addToProtocolSupportEnumerations(samlNamespace)
		
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
		
		def supportedNameIDFormats = []
		params.idp.nameidformats.each { nameFormatID -> 
			if(nameFormatID.value == "on") {
				def nameid = SamlURI.get(nameFormatID.key)
				if(nameid) {
					supportedNameIDFormats.add(nameid)
					identityProvider.addToNameIDFormats(nameid)
				}
			}
		}
		def idpContactPerson = new ContactPerson(contact:contact, type:ContactType.findByName(ct))
		identityProvider.addToContacts(idpContactPerson)	
	
		// Initial endpoints
		def postBinding = SamlURI.findByUri(SamlConstants.httpPost)
		def postLocation = new UrlURI(uri: params.idp?.post?.uri)
		def httpPost = new SingleSignOnService(approved: true, binding: postBinding, location:postLocation, active:params.active)
		identityProvider.addToSingleSignOnServices(httpPost)
		httpPost.validate()

		def redirectBinding = SamlURI.findByUri(SamlConstants.httpRedirect)
		def redirectLocation = new UrlURI(uri: params.idp?.redirect?.uri)
		def httpRedirect = new SingleSignOnService(approved: true, binding: redirectBinding, location:redirectLocation, active:params.active)
		identityProvider.addToSingleSignOnServices(httpRedirect)
		httpRedirect.validate()

		def artifactBinding = SamlURI.findByUri(SamlConstants.soap)
		def artifactLocation = new UrlURI(uri: params.idp?.artifact?.uri)
		def soapArtifact = new ArtifactResolutionService(approved: true, binding: artifactBinding, location:artifactLocation, active:params.active, isDefault:true)
		identityProvider.addToArtifactResolutionServices(soapArtifact)
		soapArtifact.validate()

		// Cryptography
		// Signing
		if(params.idp?.crypto?.sig) {
			def cert = cryptoService.createCertificate(params.cert)
			cryptoService.validateCertificate(cert)
			def keyInfo = new KeyInfo(certificate: cert)
			def keyDescriptor = new KeyDescriptor(keyInfo:keyInfo, keyType:KeyTypes.signing, roleDescriptor:identityProvider)
			identityProvider.addToKeyDescriptors(keyDescriptor)
		}
	
		// Encryption
		if(params.idp?.crypto?.enc) {
			def certEnc = cryptoService.createCertificate(params.cert)
			cryptoService.validateCertificate(certEnc)
			def keyInfoEnc = new KeyInfo(certificate:certEnc)
			def keyDescriptorEnc = new KeyDescriptor(keyInfo:keyInfoEnc, keyType:KeyTypes.encryption, roleDescriptor:identityProvider)
			identityProvider.addToKeyDescriptors(keyDescriptorEnc)
		}
	
		// Attribute Authority - collaborates with created IDP
		def attributeAuthority, soapAttributeService
		if(params.aa?.create) {
			attributeAuthority = new AttributeAuthorityDescriptor(approved:false, active:params.active, displayName: params.idp?.displayName, description: params.idp?.description, scope: params.idp?.scope, collaborator: identityProvider, organization:organization)
			attributeAuthority.addToProtocolSupportEnumerations(samlNamespace)
			identityProvider.collaborator = attributeAuthority
		
			def attributeServiceBinding = SamlURI.findByUri('urn:oasis:names:tc:SAML:2.0:bindings:SOAP')
			def attributeServiceLocation = new UrlURI(uri: params.aa?.attributeservice?.uri)
			soapAttributeService = new AttributeService(approved: true, binding: attributeServiceBinding, location:attributeServiceLocation, active:params.active)
			attributeAuthority.addToAttributeServices(soapAttributeService)
		
			params.aa.attributes.each { attrID -> 
				if(attrID.value == "on") {
					def attr = AttributeBase.get(attrID.key)
					if(attr) {
						attributeAuthority.addToAttributes(new Attribute(base:attr))
					}
				}
			}
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
		ret.soapArtifact = soapArtifact
		ret.soapAttributeService = soapAttributeService
		ret.contact = contact
		ret.certificate = params.cert
		ret.supportedNameIDFormats = supportedNameIDFormats
		ret.supportedAttributes = supportedAttributes
	
		// Submission validation
		if(!entityDescriptor.validate()) {
			entityDescriptor?.errors.each { log.error it }
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly() 
			return [false, ret]
		}
	
		identityProvider.entityDescriptor = entityDescriptor
		entityDescriptor.addToIdpDescriptors(identityProvider)

		if(params.aa?.create) {
			attributeAuthority.entityDescriptor = entityDescriptor
			entityDescriptor.addToAttributeAuthorityDescriptors(attributeAuthority)
		}

		if(!identityProvider.validate()) {
			identityProvider.errors.each { log.debug it }
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly()
			entityDescriptor.delete()
			return [false, ret]
		}

		if(params.aa?.create)
			if(!attributeAuthority.validate()) {
				log.error "Error when attempting to validate new AttributeAuthority"
				attributeAuthority.errors.each {log.debug it}
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly() 
				entityDescriptor.delete()
				return [false, ret]
			}
			
		if(!entityDescriptor.save()) {
			entityDescriptor.errors.each {log.debug it}
			throw new RuntimeException("Unable to save when creating ${identityProvider}")
		}
		
		if(!identityProvider.save()) {
			identityProvider.errors.each {log.debug it}
			throw new RuntimeException("Unable to save when creating ${identityProvider}")
		}

		def workflowParams = [ creator:contact?.id?.toString(), identityProvider:identityProvider?.id?.toString(), attributeAuthority:attributeAuthority?.id?.toString(), organization:organization.id?.toString(), locale:LCH.getLocale().getLanguage() ]

		def (initiated, processInstance) = workflowProcessService.initiate( "idpssodescriptor_create", "Approval for creation of ${identityProvider}", ProcessPriority.MEDIUM, workflowParams)

		if(initiated)
			workflowProcessService.run(processInstance)
		else
			throw new RuntimeException("Unable to execute workflow when creating ${identityProvider}")

		return [true, ret]
	}
	
	def update(def params) {
		def identityProvider = IDPSSODescriptor.get(params.id)
		if(!identityProvider)
			return [false, null]
		
		identityProvider.displayName = params.idp.displayName
		identityProvider.description = params.idp.description
		identityProvider.scope = params.idp.scope
		identityProvider.active = params.idp.status == 'true'
		identityProvider.wantAuthnRequestsSigned = params.idp.wantauthnrequestssigned == 'true'
		identityProvider.autoAcceptServices = params.idp.autoacceptservices == 'true'
		
		// Ensure AA stays synced with scope
		if(identityProvider.collaborator) {
			identityProvider.collaborator.scope = params.idp.scope
		}
		
		log.debug "Updating $identityProvider active: ${identityProvider.active}, requestSigned: ${identityProvider.wantAuthnRequestsSigned}"
		
		if(!identityProvider.validate()) {			
			identityProvider.errors.each {log.warn it}
			return [false, identityProvider]
		}
		
		if(!identityProvider.save()) {			
			identityProvider.errors.each {log.warn it}
			throw new RuntimeException("Unable to save when updating ${identityProvider}")
		}
		
		return [true, identityProvider]
	}
	
}