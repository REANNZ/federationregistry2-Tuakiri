package aaf.fr.foundation

import org.apache.shiro.SecurityUtils
import grails.plugins.federatedgrails.Role

/**
 * Provides Identity Provider views.
 *
 * @author Bradley Beddoes
 */
class IdentityProviderController {
	def defaultAction = "list"
	def allowedMethods = [save: 'POST', update: 'PUT', delete: 'DELETE']

	def IdentityProviderService
	
	def list = {
		[identityProviderList: IDPSSODescriptor.findAllWhere(archived:false), identityProviderTotal: IDPSSODescriptor.count()]
	}
	
	def listarchived = {
		[identityProviderList: IDPSSODescriptor.findAllWhere(archived:true), identityProviderTotal: IDPSSODescriptor.count()]
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
			flash.message = message(code: 'aaf.fr.foundation.idpssoroledescriptor.nonexistant')
			redirect(action: "list")
			return
		}
		
		def adminRole = Role.findByName("descriptor-${identityProvider.id}-administrators")
		def attributeFilter = g.include(controller:"attributeFilter", action:"generate", id:identityProvider.id)
		[identityProvider: identityProvider, attributeFilter:attributeFilter, contactTypes:ContactType.list(), administrators:adminRole?.subjects]
	}
	
	def create = {
		def identityProvider = new IDPSSODescriptor()
		def c = AttributeBase.createCriteria()
		def attributeList = c.list {
			order("category", "asc")
			order("name", "asc")
		}
		[identityProvider: identityProvider, organizationList: Organization.findAllWhere(active:true, approved:true), attributeList: attributeList, nameIDFormatList: SamlURI.findAllWhere(type:SamlURIType.NameIdentifierFormat)]
	}
	
	def save = {
		def (created, ret) = IdentityProviderService.create(params)
	
		if(created) {
			log.info "$subject created ${ret.identityProvider}"
			redirect (action: "show", id: ret.identityProvider.id)
		}
		else {
			flash.type="error"
			flash.message = message(code: 'aaf.fr.foundation.idpssoroledescriptor.save.validation.error')
			def c = AttributeBase.createCriteria()
			def attributeList = c.list {
				order("category", "asc")
				order("name", "asc")
			}
			render (view:'create', model:ret + [organizationList: Organization.findAllWhere(active:true, approved:true), attributeList: attributeList, nameIDFormatList: SamlURI.findAllWhere(type:SamlURIType.NameIdentifierFormat)])
		}
	}
	
	def update = {
		if(!params.id) {
			log.warn "IDPSSODescriptor ID was not present"
			response.sendError(500)
			return
		}
		
		def identityProvider_ = IDPSSODescriptor.get(params.id)
		if (!identityProvider_) {
      log.warn "IDPSSODescriptor identified by ID ${params.id} was not found"
			response.sendError(500)
			return
		}
    
		if(SecurityUtils.subject.isPermitted("descriptor:${identityProvider_.id}:update")) {
			def (updated, identityProvider) = IdentityProviderService.update(params)
			if(updated) {
				log.info "$subject updated $identityProvider"
				render template:'/templates/identityprovider/overview_editable', plugin:'foundation', model:[identityProvider:identityProvider]
			} else {
				log.info "$subject failed when attempting update on $identityProvider"
        identityProvider.errors.each {
          log.debug it
        }
        response.sendError(500)
			}
		}
		else {
			log.warn("Attempt to update $identityProvider_ by $subject was denied, incorrect permission set")
			response.sendError(403)
		}
	}
}
