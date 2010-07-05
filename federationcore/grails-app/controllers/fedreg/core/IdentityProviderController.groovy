package fedreg.core

import grails.converters.XML

class IdentityProviderController {

	static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

	def defaultAction = "list"
	
	def cryptoService

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
		[identityProvider: identityProvider]
	}
	
	def save = {
		
		/* There is a lot of moving parts when creating an IdP (+AA hybrid) so below is more complex then usual,
			you'll note validation calls on most larger objects, this is to get finer grained object error population
			for views to render */
		
		// IDP
		def identityProvider = new IDPSSODescriptor()
		bindData(identityProvider, params.idp, [include:['displayName', 'description']])
		identityProvider.active = params.active
		
		// Initial endpoints
		// def httpPost = new SingleSignOnService()
		
		//def httpRedirect = new SingleSignOnService()
		
		//def artifactResolution = new SingleSignOnService()
		
		// Cryptography
		// Signing
		if(params.idp.crypto.sig) {
			def cert = cryptoService.createCertificate(params.idp.crypto.sigdata)
			def keyInfo = new KeyInfo(certificate: cert)
			def keyDescriptor = new KeyDescriptor(keyInfo:keyInfo, keyType:KeyTypes.signing, roleDescriptor:identityProvider)
			identityProvider.addToKeyDescriptors(keyDescriptor)
		}
		
		// Encryption
		if(params.idp.crypto.enc) {
			def certEnc = cryptoService.createCertificate(params.idp.crypto.encdata ?: params.idp.crypto.sigdata)
			def keyInfoEnc = new KeyInfo(certificate:certEnc)
			def keyDescriptorEnc = new KeyDescriptor(keyInfo:keyInfoEnc, keyType:KeyTypes.encryption, roleDescriptor:identityProvider)
			identityProvider.addToKeyDescriptors(keyDescriptorEnc)
		}
		
		// Attribute Authority
		def attributeAuthority
		if(params.aa.create) {
			attributeAuthority = new AttributeAuthorityDescriptor(displayName: identityProvider.displayName, description: identityProvider.description, collaborator: identityProvider)
			identityProvider.collaborator = attributeAuthority
			
			def attributeServiceBinding = SamlURI.findByUri('urn:oasis:names:tc:SAML:2.0:bindings:SOAP')
			def attributeServiceLocation = new UrlURI(uri: params.aa.attributeservice.uri)
		
			def attributeService = new AttributeService(binding: attributeServiceBinding, location:attributeServiceLocation)
			attributeAuthority.addToAttributeServices(attributeService)
			params.aa.attributes.each { attrID -> 
				def attr = Attribute.get(attrID)
				if(attr)
					attributeAuthority.addToAttributes(attr)
			}
		}
		
		// Entity Descriptor
		def entityDescriptor = EntityDescriptor.get(params.entity?.id)
		if(!entityDescriptor) {
			entityDescriptor = new EntityDescriptor(active: params.active, entityID: params.entity?.identifier)
		}
		entityDescriptor.addToIdpDescriptors(identityProvider)
		
		if(params.aa.create)
			entityDescriptor.addToAttributeAuthorityDescriptors(attributeAuthority)
		
		// Organization
		def organization = Organization.get(params.organization?.id)
		if(!organization) {
			flash.type="error"
			flash.message="fedreg.core.idpssodescriptor.save.no.organization"
			render view: 'create', model:[identityProvider:identityProvider]
			return
		}
		organization.addToEntityDescriptors(entityDescriptor)
		identityProvider.organization = organization
		
		if(params.aa.create)
			attributeAuthority.organization = organization
		
		
		// Submission validation		
		if(!identityProvider.validate()) {			
			identityProvider.errors.each {log.debug it}
			flash.type="error"
			flash.message="fedreg.core.idpssodescriptor.save.validation.error.identityprovider"
			render view: 'create', model:[organization:organization, entityDescriptor: entityDescriptor, identityProvider:identityProvider, attributeAuthority: attributeAuthority]
			return
		}
		
		if(params.aa.create)
			if(!attributeAuthority.validate()) {			
				attributeAuthority.errors.each {log.debug it}
				flash.type="error"
				flash.message="fedreg.core.idpssodescriptor.save.validation.error.attributeauthority"
				render view: 'create', model:[organization:organization, entityDescriptor: entityDescriptor, identityProvider:identityProvider, attributeAuthority: attributeAuthority]
				return
			}
			
		if(!entityDescriptor.validate()) {
			entityDescriptor.errors.each { log.error it }
			identityProvider.validate()
			flash.type="error"
			flash.message="fedreg.core.idpssodescriptor.save.validation.error.entitydescriptor"
			render view: 'create', model:[organization:organization, entityDescriptor: entityDescriptor, identityProvider:identityProvider, attributeAuthority: attributeAuthority]
			return
		}
		
		if(!identityProvider.save()) {			
			identityProvider.errors.each {log.debug it}
			flash.type="error"
			flash.message="fedreg.core.idpssodescriptor.save.failed"
			render view: 'create', model:[organization:organization, entityDescriptor: entityDescriptor, identityProvider:identityProvider, attributeAuthority: attributeAuthority]
			return
		}

		redirect(action: "show", id:identityProvider.id)
	}
}
