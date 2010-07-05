package fedreg.core

import grails.converters.XML

class IdentityProviderController {

	static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

	def defaultAction = "list"

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
		def identityProvider = new IDPSSODescriptor()
		bindData(identityProvider, params.idp, [include:['displayName', 'description']])
		identityProvider.active = params.active
		
		def binding = SamlURI.get(params.sso.binding)
		def location = new UrlURI()
		bindData(location, params.sso, [include:['uri']])
		
		def sso = new SingleSignOnService()
		sso.active = params.active
		sso.location = location
		sso.binding = binding
		identityProvider.addToSingleSignOnServices(sso)
		sso.validate()
			
		def organization = Organization.get(params.organization?.id)
		if(!organization) {
			flash.type="error"
			flash.message="fedreg.core.idpssodescriptor.save.no.organization"
			render view: 'create', model:[identityProvider:identityProvider]
			return
		}
		identityProvider.organization = organization
		
		def entityDescriptor = EntityDescriptor.get(params.entity?.id)
		if(!entityDescriptor) {
			entityDescriptor = new EntityDescriptor(active: params.active, entityID: params.entity?.identifier)
		}
		entityDescriptor.addToIdpDescriptors(identityProvider)
		organization.addToEntityDescriptors(entityDescriptor)
		if(!entityDescriptor.validate()) {
			entityDescriptor.errors.each { log.error it }
			identityProvider.validate()	// Validate IdP as well at this point to get local errors object populated incase IdP triggered validation fault
			flash.type="error"
			flash.message="fedreg.core.idpssodescriptor.save.no.entitydescriptor"
			render view: 'create', model:[organization:organization, entityDescriptor: entityDescriptor, identityProvider:identityProvider]
			return
		}
		
		
		if(!identityProvider.save()) {			
			identityProvider.errors.each {log.debug it}
			flash.type="error"
			flash.message="fedreg.core.idpssodescriptor.save.failed"
			render view: 'create', model:[organization:organization, entityDescriptor: entityDescriptor, identityProvider:identityProvider]
			return
		}

		redirect(action: "show", id:identityProvider.id)
	}
}
