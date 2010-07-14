package fedreg.core

import fedreg.workflow.ProcessPriority

class IDPSSODescriptorController {

	static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

	def defaultAction = "list"
	
	def cryptoService
	def workflowProcessService

	def list = {
		[identityProviderList: IDPSSODescriptor.list(params), identityProviderTotal: IDPSSODescriptor.count()]
	}

	def show = {
		if(!params.id) {
			log.warn "IDPSSODescriptor ID was not present"
			flash.type="error"
			flash.message = message(code: 'fedreg.controllers.namevalue.missing')
			redirect(action: "list")
			return
		}
		
		def identityProvider = IDPSSODescriptor.get(params.id)
		if (!identityProvider) {
			flash.type="error"
			flash.message = message(code: 'fedreg.core.idpssoroledescriptor.nonexistant')
			redirect(action: "list")
			return
		}

		[identityProvider: identityProvider, contactTypes:ContactType.list()]
	}
	
	def create = {
		def identityProvider = new IDPSSODescriptor()
		[identityProvider: identityProvider, organizationList: Organization.list(), attributeList: Attribute.list(), nameIDFormatList: SamlURI.findAllWhere(type:SamlURIType.NameIdentifierFormat)]
	}
	
	def save = {
		
		/* There is a lot of moving parts when creating an IdP (+AA hybrid) so below is more complex then usual,
			you'll note validation calls on most larger objects, this is to get finer grained object error population
			for views to render */
		
		// IDP
		def identityProvider = new IDPSSODescriptor(active:params.active, displayName: params.idp.displayName, description: params.idp.description)
		params.idp.attributes.each { attrID -> 
			if(attrID.value == "on") {
				def attr = Attribute.get(attrID.key)
				if(attr)
					identityProvider.addToAttributes(attr)
			}
		}
		params.idp.nameidformats.each { nameFormatID -> 
			if(nameFormatID.value == "on") {
				def nameid = SamlURI.get(nameFormatID.key)
				if(nameid)
					identityProvider.addToNameIDFormats(nameid)
			}
		}
		
		// Initial endpoints
		def postBinding = SamlURI.findByUri(SamlConstants.httpPost)
		def postLocation = new UrlURI(uri: params.idp?.post?.uri)
		def httpPost = new SingleSignOnService(binding: postBinding, location:postLocation)
		identityProvider.addToSingleSignOnServices(httpPost)
		httpPost.validate()
		
		def redirectBinding = SamlURI.findByUri(SamlConstants.httpRedirect)
		def redirectLocation = new UrlURI(uri: params.idp?.redirect?.uri)
		def httpRedirect = new SingleSignOnService(binding: redirectBinding, location:redirectLocation)
		identityProvider.addToSingleSignOnServices(httpRedirect)
		httpRedirect.validate()
		
		def artifactBinding = SamlURI.findByUri(SamlConstants.soap)
		def artifactLocation = new UrlURI(uri: params.idp?.artifact?.uri)
		def soapArtifact = new ArtifactResolutionService(binding: artifactBinding, location:artifactLocation)
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
			attributeAuthority = new AttributeAuthorityDescriptor(active:params.active, displayName: params.aa.displayName, description: params.aa.description, collaborator: identityProvider)
			identityProvider.collaborator = attributeAuthority
			
			def attributeServiceBinding = SamlURI.findByUri('urn:oasis:names:tc:SAML:2.0:bindings:SOAP')
			def attributeServiceLocation = new UrlURI(uri: params.aa?.attributeservice?.uri)
		
			def attributeService = new AttributeService(binding: attributeServiceBinding, location:attributeServiceLocation)
			attributeAuthority.addToAttributeServices(attributeService)
			params.aa.attributes.each { attrID -> 
				if(attrID.value == "on") {
					def attr = Attribute.get(attrID.key)
					if(attr)
						attributeAuthority.addToAttributes(attr)
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
		
		// Entity Descriptor
		def entityDescriptor = EntityDescriptor.get(params.entity?.id)
		if(!entityDescriptor) {
			entityDescriptor = new EntityDescriptor(active: params.active, entityID: params.entity?.identifier)
		}
		
		// Organization
		def organization = Organization.get(params.organization?.id)
		if(!organization) {
			flash.type="error"
			flash.message="fedreg.core.idpssodescriptor.save.validation.error.organization"
			render view: 'create', model:[entityDescriptor: entityDescriptor, identityProvider:identityProvider, attributeAuthority: attributeAuthority, 
			httpPost: httpPost, httpRedirect: httpRedirect, soapArtifact: soapArtifact, organizationList: Organization.list(), attributeList: Attribute.list(), nameIDFormatList: SamlURI.findAllWhere(type:SamlURIType.ProtocolBinding)]
			return
		}
		organization.addToEntityDescriptors(entityDescriptor)
		identityProvider.organization = organization
		
		if(params.aa?.create)
			attributeAuthority.organization = organization
			
		// Submission validation
		if(!entityDescriptor.validate()) {
			entityDescriptor.errors.each { log.error it }
			flash.type="error"
			flash.message="fedreg.core.idpssodescriptor.save.validation.error.entitydescriptor"
			render view: 'create', model:[organization:organization, entityDescriptor: entityDescriptor, identityProvider:identityProvider, attributeAuthority: attributeAuthority, 
			httpPost: httpPost, httpRedirect: httpRedirect, soapArtifact: soapArtifact, organizationList: Organization.list(), attributeList: Attribute.list(), nameIDFormatList: SamlURI.findAllWhere(type:SamlURIType.ProtocolBinding)]
			return
		}
		entityDescriptor.addToIdpDescriptors(identityProvider)
		
		if(params.aa?.create)
			entityDescriptor.addToAttributeAuthorityDescriptors(attributeAuthority)
					
		if(!identityProvider.validate()) {			
			identityProvider.errors.each {log.debug it}
			flash.type="error"
			flash.message="fedreg.core.idpssodescriptor.save.validation.error.identityprovider"
			render view: 'create', model:[organization:organization, entityDescriptor: entityDescriptor, identityProvider:identityProvider, attributeAuthority: attributeAuthority, 
			httpPost: httpPost, httpRedirect: httpRedirect, soapArtifact: soapArtifact, organizationList: Organization.list(), attributeList: Attribute.list(), nameIDFormatList: SamlURI.findAllWhere(type:SamlURIType.ProtocolBinding)]
			return
		}
		
		if(params.aa?.create)
			if(!attributeAuthority.validate()) {			
				attributeAuthority.errors.each {log.debug it}
				flash.type="error"
				flash.message="fedreg.core.idpssodescriptor.save.validation.error.attributeauthority"
				render view: 'create', model:[organization:organization, entityDescriptor: entityDescriptor, identityProvider:identityProvider, attributeAuthority: attributeAuthority, 
				httpPost: httpPost, httpRedirect: httpRedirect, soapArtifact: soapArtifact, organizationList: Organization.list(), attributeList: Attribute.list(), nameIDFormatList: SamlURI.findAllWhere(type:SamlURIType.ProtocolBinding)]
				return
			}

		if(!identityProvider.save()) {			
			identityProvider.errors.each {log.debug it}
			flash.type="error"
			flash.message="fedreg.core.idpssodescriptor.save.failed"
			render view: 'create', model:[organization:organization, entityDescriptor: entityDescriptor, identityProvider:identityProvider, attributeAuthority: attributeAuthority, 
			httpPost: httpPost, httpRedirect: httpRedirect, soapArtifact: soapArtifact, organizationList: Organization.list(), attributeList: Attribute.list(), nameIDFormatList: SamlURI.findAllWhere(type:SamlURIType.ProtocolBinding)]
			return
		}
		
		def workflowParams = [ creator:authenticatedUser.id, identityProvider:identityProvider?.id, attributeAuthority:attributeAuthority?.id, organization:organization.name ]
		def processInstance = workflowProcessService.initiate( "idpssodescriptor_create", "Approval for creation of IDPSSODescriptor ${identityProvider.displayName}", ProcessPriority.MEDIUM, workflowParams)
		workflowProcessService.run(processInstance)
		
		redirect(action: "show", id: identityProvider.id)
	}
}
