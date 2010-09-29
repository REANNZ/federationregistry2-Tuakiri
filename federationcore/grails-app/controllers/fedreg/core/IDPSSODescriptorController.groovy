package fedreg.core

import org.apache.shiro.SecurityUtils
import grails.plugins.nimble.core.Role

class IDPSSODescriptorController {

	def IDPSSODescriptorService

	static allowedMethods = [save: "POST", update: "POST", delete: "POST"]
	def defaultAction = "list"

	def list = {
		params.max = Math.min(params.max ? params.max.toInteger() : 20, 100)
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
		
		def adminRole = Role.findByName("descriptor-${identityProvider.id}-administrators")
		[identityProvider: identityProvider, contactTypes:ContactType.list(), administrators:adminRole?.users]
	}
	
	def create = {
		if(SecurityUtils.subject.isPermitted("idpssodescriptor:create")) {
			def identityProvider = new IDPSSODescriptor()
			[identityProvider: identityProvider, organizationList: Organization.findAllWhere(active:true, approved:true), attributeList: AttributeBase.list(), nameIDFormatList: SamlURI.findAllWhere(type:SamlURIType.NameIdentifierFormat)]
		}
		else {
			log.warn("Attempt to create identity provider by $authenticatedUser was denied, incorrect permission set")
			response.sendError(403)
		}
	}
	
	def save = {
		if(SecurityUtils.subject.isPermitted("organization:${params.organization.id}:components:idpssodescriptor:create")) {
			def (created, organization, entityDescriptor, identityProvider, attributeAuthority, httpPost, httpRedirect, soapArtifact, organizationList, attributeList, nameIDFormatList, contact) = IDPSSODescriptorService.create(params)
		
			if(created) {
				redirect (action: "show", id: identityProvider.id)
			}
			else {
				flash.type="error"
				flash.message = message(code: 'fedreg.core.idpssoroledescriptor.save.validation.error')
				render (view:'create', model:[organization:organization, entityDescriptor:entityDescriptor, identityProvider:identityProvider, attributeAuthority:attributeAuthority, httpPost:httpPost, httpRedirect:httpRedirect, 
				soapArtifact:soapArtifact, organizationList:organizationList, attributeList:attributeList, nameIDFormatList:nameIDFormatList, contact:contact])
			}
		}
		else {
			log.warn("Attempt to save identity provider by $authenticatedUser was denied, incorrect permission set")
			response.sendError(403)
		}
	}
	
	def edit = {
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
		
		if(SecurityUtils.subject.isPermitted("descriptor:${identityProvider.id}:update")) {
			[identityProvider: identityProvider]	
		}
		else {
			log.warn("Attempt to edit $identityProvider by $authenticatedUser was denied, incorrect permission set")
			response.sendError(403)
		}
	}
	
	def update = {
		if(!params.id) {
			log.warn "IDPSSODescriptor ID was not present"
			flash.type="error"
			flash.message = message(code: 'fedreg.controllers.namevalue.missing')
			redirect(action: "list")
			return
		}
		
		def identityProvider_ = IDPSSODescriptor.get(params.id)
		if (!identityProvider_) {
			flash.type="error"
			flash.message = message(code: 'fedreg.core.idpssoroledescriptor.nonexistant')
			redirect(action: "list")
			return
		}
		if(SecurityUtils.subject.isPermitted("descriptor:${identityProvider_.id}:update")) {
			def (updated, identityProvider) = IDPSSODescriptorService.update(params)
			if(updated)
				redirect (action: "show", id: identityProvider.id)
			else {
				flash.type="error"
				flash.message = message(code: 'fedreg.core.idpssoroledescriptor.update.validation.error')
				render (view:'edit', model:[identityProvider:identityProvider])
			}
		}
		else {
			log.warn("Attempt to update $identityProvider by $authenticatedUser was denied, incorrect permission set")
			response.sendError(403)
		}
	}
}
