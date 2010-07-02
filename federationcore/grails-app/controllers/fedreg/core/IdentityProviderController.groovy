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
		
		def organization = Organization.get(params.organization)
		if(!organization) {
			flash.type="error"
			flash.message="fedreg.core.idpssodescriptor.save.no.organization"
			render view: 'create', model:[identityProvider:identityProvider]
			return
		}
		
		def entityDescriptor = EntityDescriptor.get(params.idp.entitydescriptor)
		if(!entityDescriptor) {
			entityDescriptor = new EntityDescriptor(active: params.active, entityID: params.entity.identifier)
			organization.addToEntityDescriptors(entityDescriptor)
		}
		
		identityProvider.active = false
		identityProvider.addToSingleSignOnServices(sso)
		identityProvider.organization = organization
		entityDescriptor.addToIdpDescriptors(identityProvider)
		
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
