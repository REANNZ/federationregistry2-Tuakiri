package fedreg.core

import fedreg.workflow.ProcessPriority

class IDPSSODescriptorService {
	
	def cryptoService
	def workflowProcessService
	
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
		}
		def ct = params.contact?.type ?: 'administrative'
		
		// Entity Descriptor
		def entityDescriptor = EntityDescriptor.get(params.entity.id)
		if(!entityDescriptor) {
			entityDescriptor = new EntityDescriptor(active: params.active, entityID: params.entity?.identifier, organization: organization)
			def entContactPerson = new ContactPerson(contact:contact, type:ContactType.findByName(ct))
			entityDescriptor.addToContacts(entContactPerson)
		}
		
		// IDP
		def samlNamespace = SamlURI.findByUri('urn:oasis:names:tc:SAML:2.0:protocol')
		def identityProvider = new IDPSSODescriptor(active:params.active, displayName: params.idp?.displayName, description: params.idp?.description, organization: organization)
		identityProvider.addToProtocolSupportEnumerations(samlNamespace)
		params.idp.attributes.each { a -> 
			if(a.value == "on") {
				def attr = AttributeBase.get(a.key)
				if(attr)
					identityProvider.addToAttributes(new Attribute(base:attr))
			}
		}
		params.idp.nameidformats.each { nameFormatID -> 
			if(nameFormatID.value == "on") {
				def nameid = SamlURI.get(nameFormatID.key)
				if(nameid)
					identityProvider.addToNameIDFormats(nameid)
			}
		}
		def idpContactPerson = new ContactPerson(contact:contact, type:ContactType.findByName(ct))
		identityProvider.addToContacts(idpContactPerson)	
		
		// Initial endpoints
		def postBinding = SamlURI.findByUri(SamlConstants.httpPost)
		def postLocation = new UrlURI(uri: params.idp?.post?.uri)
		def httpPost = new SingleSignOnService(binding: postBinding, location:postLocation, active:params.active)
		identityProvider.addToSingleSignOnServices(httpPost)
		httpPost.validate()

		def redirectBinding = SamlURI.findByUri(SamlConstants.httpRedirect)
		def redirectLocation = new UrlURI(uri: params.idp?.redirect?.uri)
		def httpRedirect = new SingleSignOnService(binding: redirectBinding, location:redirectLocation, active:params.active)
		identityProvider.addToSingleSignOnServices(httpRedirect)
		httpRedirect.validate()

		def artifactBinding = SamlURI.findByUri(SamlConstants.soap)
		def artifactLocation = new UrlURI(uri: params.idp?.artifact?.uri)
		def soapArtifact = new ArtifactResolutionService(binding: artifactBinding, location:artifactLocation, active:params.active)
		identityProvider.addToArtifactResolutionServices(soapArtifact)
		soapArtifact.validate()

		// Cryptography
		// Signing
		if(params.idp?.crypto?.sig) {
			def cert = cryptoService.createCertificate(params.idp?.crypto?.sigdata)
			cryptoService.validateCertificate(cert)
			def keyInfo = new KeyInfo(certificate: cert)
			def keyDescriptor = new KeyDescriptor(keyInfo:keyInfo, keyType:KeyTypes.signing, roleDescriptor:identityProvider)
			identityProvider.addToKeyDescriptors(keyDescriptor)
		}
		
		// Encryption
		if(params.idp?.crypto?.enc) {
			def certEnc = cryptoService.createCertificate(params.idp?.crypto?.encdata)
			cryptoService.validateCertificate(certEnc)
			def keyInfoEnc = new KeyInfo(certificate:certEnc)
			def keyDescriptorEnc = new KeyDescriptor(keyInfo:keyInfoEnc, keyType:KeyTypes.encryption, roleDescriptor:identityProvider)
			identityProvider.addToKeyDescriptors(keyDescriptorEnc)
		}
		
		// Attribute Authority
		def attributeAuthority
		if(params.aa?.create) {
			attributeAuthority = new AttributeAuthorityDescriptor(active:params.active, displayName: params.aa.displayName, description: params.aa.description, collaborator: identityProvider, organization:organization)
			identityProvider.collaborator = attributeAuthority
			
			def attributeServiceBinding = SamlURI.findByUri('urn:oasis:names:tc:SAML:2.0:bindings:SOAP')
			def attributeServiceLocation = new UrlURI(uri: params.aa?.attributeservice?.uri)
		
			def attributeService = new AttributeService(binding: attributeServiceBinding, location:attributeServiceLocation)
			attributeAuthority.addToAttributeServices(attributeService)
			params.aa.attributes.each { attrID -> 
				if(attrID.value == "on") {
					def attr = AttributeBase.get(attrID.key)
					if(attr)
						attributeAuthority.addToAttributes(new Attribute(base:attr))
				}
			}
			
			// Cryptography
			// Signing
			if(params.aa?.crypto?.sig) {
				def cert = cryptoService.createCertificate(params.aa?.crypto?.sigdata)
				cryptoService.validateCertificate(cert)
				def keyInfo = new KeyInfo(certificate: cert)
				def keyDescriptor = new KeyDescriptor(keyInfo:keyInfo, keyType:KeyTypes.signing, roleDescriptor:attributeAuthority)
				attributeAuthority.addToKeyDescriptors(keyDescriptor)
			}

			// Encryption
			if(params.aa?.crypto?.enc) {
				def certEnc = cryptoService.createCertificate(params.aa?.crypto?.encdata)
				cryptoService.validateCertificate(certEnc)
				def keyInfoEnc = new KeyInfo(certificate:certEnc)
				def keyDescriptorEnc = new KeyDescriptor(keyInfo:keyInfoEnc, keyType:KeyTypes.encryption, roleDescriptor:attributeAuthority)
				attributeAuthority.addToKeyDescriptors(keyDescriptorEnc)
			}
		}
		
		// Submission validation
		if(!entityDescriptor.save()) {
			entityDescriptor?.errors.each { log.error it }
			return [false, organization, entityDescriptor, identityProvider, attributeAuthority, httpPost, httpRedirect, soapArtifact, Organization.list(), AttributeBase.list(), SamlURI.findAllWhere(type:SamlURIType.ProtocolBinding), contact]
		}
		identityProvider.entityDescriptor = entityDescriptor
		entityDescriptor.addToIdpDescriptors(identityProvider)
		
		if(params.aa?.create) {
			attributeAuthority.entityDescriptor = entityDescriptor
			entityDescriptor.addToAttributeAuthorityDescriptors(attributeAuthority)
		}
		
		if(!identityProvider.validate()) {			
			identityProvider.errors.each { log.debug it }
			return [false, organization, entityDescriptor, identityProvider, attributeAuthority, httpPost, httpRedirect, soapArtifact, Organization.list(), AttributeBase.list(), SamlURI.findAllWhere(type:SamlURIType.ProtocolBinding), contact]
		}
		
		if(params.aa?.create)
			if(!attributeAuthority.validate()) {			
				attributeAuthority.errors.each {log.debug it}
				return [false, organization, entityDescriptor, identityProvider, attributeAuthority, httpPost, httpRedirect, soapArtifact, Organization.list(), AttributeBase.list(), SamlURI.findAllWhere(type:SamlURIType.ProtocolBinding), contact]
			}
		
		if(!identityProvider.save()) {			
			identityProvider.errors.each {log.debug it}
			return [false, organization, entityDescriptor, identityProvider, attributeAuthority, httpPost, httpRedirect, soapArtifact, Organization.list(), AttributeBase.list(), SamlURI.findAllWhere(type:SamlURIType.ProtocolBinding), contact]
		}
		
		def workflowParams = [ creator:contact?.id, identityProvider:identityProvider?.id, attributeAuthority:attributeAuthority?.id, organization:organization.name ]
		def processInstance = workflowProcessService.initiate( "idpssodescriptor_create", "Approval for creation of ${identityProvider}", ProcessPriority.MEDIUM, workflowParams)
		workflowProcessService.run(processInstance)
		
		return [true, organization, entityDescriptor, identityProvider, attributeAuthority, httpPost, httpRedirect, soapArtifact, Organization.list(), AttributeBase.list(), SamlURI.findAllWhere(type:SamlURIType.ProtocolBinding), contact]
	}
}