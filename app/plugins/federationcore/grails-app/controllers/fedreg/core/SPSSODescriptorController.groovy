package fedreg.core

import org.apache.shiro.SecurityUtils
import grails.plugins.nimble.core.Role

/**
 * Provides SPSSODescriptor views.
 *
 * @author Bradley Beddoes
 */
class SPSSODescriptorController {
	static defaultAction = "list"
	def allowedMethods = [save: 'POST', update: 'PUT', delete: 'DELETE']
	
	def SPSSODescriptorService
	
	def list = {
		[serviceProviderList: SPSSODescriptor.findAllWhere(archived:false), serviceProviderTotal: SPSSODescriptor.count()]
	}
	
	def listarchived = {
		[serviceProviderList: SPSSODescriptor.findAllWhere(archived:true), serviceProviderTotal: SPSSODescriptor.count()]
	}
	
	def show = {
		if(!params.id) {
			log.warn "SPSSODescriptor ID was not present"
			flash.type="error"
			flash.message = message(code: 'fedreg.controllers.namevalue.missing')
			redirect(action: "list")
			return
		}
		
		def serviceProvider = SPSSODescriptor.get(params.id)
		if (!serviceProvider) {
			flash.type="error"
			flash.message = message(code: 'fedreg.core.spssoroledescriptor.nonexistant')
			redirect(action: "list")
			return
		}
		
		def attributes, specAttr
		if(SecurityUtils.subject.isPermitted("modify:restricted:attributes")) {
			attributes = AttributeBase.list()
			specAttr = AttributeBase.findAllWhere(specificationRequired:true)
		} else {
			attributes = AttributeBase.findAllWhere(adminRestricted:false)
			specAttr = AttributeBase.findAllWhere(adminRestricted:false, specificationRequired:true)	
		}
		
		def adminRole = Role.findByName("descriptor-${serviceProvider.id}-administrators")
		[serviceProvider: serviceProvider, contactTypes:ContactType.list(), availableAttributes:attributes, specificationAttributes: specAttr, administrators:adminRole?.users]
	}
	
	def create = {
		def serviceProvider = new SPSSODescriptor()
		def c = AttributeBase.createCriteria()
		def attributeList = c.list {
			order("category", "asc")
			order("name", "asc")
		}
		[serviceProvider:serviceProvider, organizationList: Organization.findAllWhere(active:true, approved:true), attributeList: attributeList, nameIDFormatList: SamlURI.findAllWhere(type:SamlURIType.NameIdentifierFormat)]
	}
	
	def save = {
		def (created, ret) = SPSSODescriptorService.create(params)
	
		if(created) {
			log.info "$authenticatedUser created ${ret.serviceProvider}"
			redirect (action: "show", id: ret.serviceProvider.id)
		}
		else {
			flash.type="error"
			flash.message = message(code: 'fedreg.core.spssoroledescriptor.save.validation.error')
			
			log.info "$authenticatedUser failed attempting to create ${ret.serviceProvider}"
			
			def c = AttributeBase.createCriteria()
			def attributeList = c.list {
				order("category", "asc")
				order("name", "asc")
			}
			render (view:'create', model: ret + [organizationList: Organization.findAllWhere(active:true, approved:true), attributeList: attributeList, nameIDFormatList: SamlURI.findAllWhere(type:SamlURIType.NameIdentifierFormat)])
		}
	}
	
	def edit = {
		if(!params.id) {
			log.warn "SPSSODescriptor ID was not present"
			flash.type="error"
			flash.message = message(code: 'fedreg.controllers.namevalue.missing')
			redirect(action: "list")
			return
		}
		
		def serviceProvider = SPSSODescriptor.get(params.id)
		if (!serviceProvider) {
			flash.type="error"
			flash.message = message(code: 'fedreg.core.spssoroledescriptor.nonexistant')
			redirect(action: "list")
			return
		}	
		
		if(SecurityUtils.subject.isPermitted("descriptor:${serviceProvider.id}:update")) {
			[serviceProvider: serviceProvider]	
		}
		else {
			log.warn("Attempt to edit $serviceProvider by $authenticatedUser was denied, incorrect permission set")
			response.sendError(403)
		}	
	}
	
	def update = {
		if(!params.id) {
			log.warn "SPSSODescriptor ID was not present"
			flash.type="error"
			flash.message = message(code: 'fedreg.controllers.namevalue.missing')
			redirect(action: "list")
			return
		}
		def serviceProvider_ = SPSSODescriptor.get(params.id)
		if (!serviceProvider_) {
			flash.type="error"
			flash.message = message(code: 'fedreg.core.spssoroledescriptor.nonexistant')
			redirect(action: "list")
			return
		}
		if(SecurityUtils.subject.isPermitted("descriptor:${serviceProvider_.id}:update")) {
			def (updated, serviceProvider) = SPSSODescriptorService.update(params)
			if(updated) {
				log.info "$authenticatedUser updated $serviceProvider"
				redirect (action: "show", id: serviceProvider.id)
			} else {
				flash.type="error"
				flash.message = message(code: 'fedreg.core.spssoroledescriptor.update.validation.error')
				
				log.info "$authenticatedUser failed when attempting update on $serviceProvider"
				render (view:'edit', model:[serviceProvider:serviceProvider])
			}
		}
		else {
			log.warn("Attempt to update $serviceProvider_ by $authenticatedUser was denied, incorrect permission set")
			response.sendError(403)
		}
	}	
}