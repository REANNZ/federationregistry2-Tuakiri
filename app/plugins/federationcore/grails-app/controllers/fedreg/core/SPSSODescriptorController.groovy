package fedreg.core

import org.apache.shiro.SecurityUtils
import grails.plugins.nimble.core.Role

class SPSSODescriptorController {
	static defaultAction = "list"
	def allowedMethods = [save: 'POST', update: 'PUT', delete: 'DELETE', addCategory:'POST', removeCategory:'DELETE']
	
	def SPSSODescriptorService
	
	def list = {
		[serviceProviderList: SPSSODescriptor.list(params), serviceProviderTotal: SPSSODescriptor.count()]
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
		
		def attributes = AttributeBase.list()
		def specAttr = AttributeBase.findAllWhere(specificationRequired:true)
		
		def adminRole = Role.findByName("descriptor-${serviceProvider.id}-administrators")
		[serviceProvider: serviceProvider, contactTypes:ContactType.list(), availableAttributes:attributes, specificationAttributes: specAttr, administrators:adminRole?.users]
	}
	
	def create = {
		def serviceProvider = new SPSSODescriptor()
		def c = AttributeBase.createCriteria()
		def attributeList = c.list {
			order("category", "asc")
			order("friendlyName", "asc")
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
			render (view:'create', model: ret + [organizationList: Organization.findAllWhere(active:true, approved:true), attributeList: AttributeBase.list(), nameIDFormatList: SamlURI.findAllWhere(type:SamlURIType.NameIdentifierFormat)])
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
			log.warn("Attempt to update $serviceProvider by $authenticatedUser was denied, incorrect permission set")
			response.sendError(403)
		}
	}
	
	def listCategories = {
		if(!params.id) {
			log.warn "SPSSODescriptor ID was not present"
			render message(code: 'fedreg.controllers.namevalue.missing')
			response.setStatus(500)
			return
		}
		
		if(!params.containerID) {
			log.warn "Container ID was not present"
			render message(code: 'fedreg.controllers.namevalue.missing')
			response.setStatus(500)
			return
		}
		
		def serviceProvider = SPSSODescriptor.get(params.id)
		if(!serviceProvider) {
			log.warn "SPSSODescriptor identified by id $params.id was not located"
			render message(code: 'fedreg.core.spssoroledescriptor.nonexistant', args: [params.id])
			response.setStatus(500)
			return
		}
		
		render template: "/templates/servicecategories/list", contextPath: pluginContextPath, model:[descriptor:serviceProvider, categories:serviceProvider.serviceCategories, containerID:params.containerID]
	}
	
	def addCategory = {
		if(!params.id) {
			log.warn "SPSSODescriptor ID was not present"
			flash.type="error"
			flash.message = message(code: 'fedreg.controllers.namevalue.missing')
			response.setStatus(500)
			return
		}
		if(!params.categoryID) {
			log.warn "Category ID was not present"
			flash.type="error"
			flash.message = message(code: 'fedreg.controllers.namevalue.missing')
			response.setStatus(500)
			return
		}
		
		def serviceProvider = SPSSODescriptor.get(params.id)
		if (!serviceProvider) {
			flash.type="error"
			flash.message = message(code: 'fedreg.core.spssoroledescriptor.nonexistant')
			response.setStatus(500)
			return
		}
		def category = ServiceCategory.get(params.categoryID)
		if (!category) {
			flash.type="error"
			flash.message = message(code: 'fedreg.core.servicecategory.nonexistant')
			response.setStatus(500)
			return
		}
		
		if(SecurityUtils.subject.isPermitted("descriptor:${serviceProvider.id}:category:add")) {
			if(!serviceProvider.serviceCategories?.contains(category)) {
				serviceProvider.addToServiceCategories(category)
				serviceProvider.save()
				if(serviceProvider.hasErrors()) {
					flash.type="error"
					flash.message = message(code: 'fedreg.core.servicecategory.error.adding')
					response.setStatus(500)
					return
				}
				
				render message(code: 'fedreg.core.servicecategory.added')
			}
			else {
				flash.type="error"
				flash.message = message(code: 'fedreg.core.servicecategory.already.supported')
				response.setStatus(500)
				return
			}
		}
		else {
			log.warn("Attempt to update $serviceProvider by $authenticatedUser was denied, incorrect permission set")
			response.sendError(403)
		}
	}
	
	def removeCategory = {
		if(!params.id) {
			log.warn "SPSSODescriptor ID was not present"
			flash.type="error"
			flash.message = message(code: 'fedreg.controllers.namevalue.missing')
			response.setStatus(500)
			return
		}
		if(!params.categoryID) {
			log.warn "Category ID was not present"
			flash.type="error"
			flash.message = message(code: 'fedreg.controllers.namevalue.missing')
			response.setStatus(500)
			return
		}
		
		def serviceProvider = SPSSODescriptor.get(params.id)
		if (!serviceProvider) {
			flash.type="error"
			flash.message = message(code: 'fedreg.core.spssoroledescriptor.nonexistant')
			response.setStatus(500)
			return
		}
		def category = ServiceCategory.get(params.categoryID)
		if (!category) {
			flash.type="error"
			flash.message = message(code: 'fedreg.core.servicecategory.nonexistant')
			response.setStatus(500)
			return
		}
		
		if(SecurityUtils.subject.isPermitted("descriptor:${serviceProvider.id}:category:remove")) {
			serviceProvider.removeFromServiceCategories(category)
			serviceProvider.save()
			if(serviceProvider.hasErrors()) {
				flash.type="error"
				flash.message = message(code: 'fedreg.core.servicecategory.error.removing')
				response.setStatus(500)
				return
			}
			
			render message(code: 'fedreg.core.servicecategory.removed')
		}
		else {
			log.warn("Attempt to update $serviceProvider by $authenticatedUser was denied, incorrect permission set")
			response.sendError(403)
		}
	}
}