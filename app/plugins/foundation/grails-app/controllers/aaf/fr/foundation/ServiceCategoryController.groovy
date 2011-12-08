package aaf.fr.foundation

import org.apache.shiro.SecurityUtils


import grails.converters.JSON

/**
 * Provides ServiceCategory views.
 *
 * @author Bradley Beddoes
 */
class ServiceCategoryController {
	def allowedMethods = [add: 'POST', update: 'PUT', delete: 'DELETE']
	
	def json = {
		if(!params.id) {
			log.warn "Service Category ID was not present"
			render message(code: 'fedreg.controllers.namevalue.missing')
			response.setStatus(500)
			return
		}
		
		def result = []
		def serviceCategory = ServiceCategory.get(params.id)
		
		def c = SPSSODescriptor.createCriteria()
		def serviceProviders = c.list {
			serviceCategories {
				idEq(serviceCategory.id)
			}
		}
		
		serviceProviders.each { sp -> 
			if(sp.functioning()) {
				def data = [:]
				data.id = sp.id
				data.displayName = sp.displayName
				data.description=sp.description
				data.organization=sp.organization.displayName
				data.organizationURL=sp.organization.url?.uri
				data.url = sp.serviceDescription?.connectURL
				data.logoURL = sp.serviceDescription?.logoURL
				data.frURL = createLink(controller:'SPSSODescriptor', action:'show', id:sp.id, absolute:true )
				
				result.add(data)
			}
		}
		
		render result as JSON
	}
	
	def list = {
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
			render message(code: 'aaf.fr.foundation.spssoroledescriptor.nonexistant', args: [params.id])
			response.setStatus(500)
			return
		}
		
		render template: "/templates/servicecategories/list", contextPath: pluginContextPath, model:[descriptor:serviceProvider, categories:serviceProvider.serviceCategories, containerID:params.containerID]
	}
	
	def add = {
		if(!params.id) {
			log.warn "SPSSODescriptor ID was not present"
			render message(code: 'fedreg.controllers.namevalue.missing')
			response.setStatus(500)
			return
		}
		if(!params.categoryID) {
			log.warn "Category ID was not present"
			render message(code: 'fedreg.controllers.namevalue.missing')
			response.setStatus(500)
			return
		}
		
		def serviceProvider = SPSSODescriptor.get(params.id)
		if (!serviceProvider) {
			render message(code: 'aaf.fr.foundation.spssoroledescriptor.nonexistant')
			response.setStatus(500)
			return
		}
		def category = ServiceCategory.get(params.categoryID)
		if (!category) {
			render message(code: 'aaf.fr.foundation.servicecategory.nonexistant')
			response.setStatus(500)
			return
		}
		
		if(SecurityUtils.subject.isPermitted("descriptor:${serviceProvider.id}:category:add")) {
			if(!serviceProvider.serviceCategories?.contains(category)) {
				serviceProvider.addToServiceCategories(category)
				serviceProvider.save()
				if(serviceProvider.hasErrors()) {
					render message(code: 'aaf.fr.foundation.servicecategory.error.adding')
					response.setStatus(500)
					return
				}
				
				render message(code: 'aaf.fr.foundation.servicecategory.added')
			}
			else {
				render message(code: 'aaf.fr.foundation.servicecategory.already.supported')
				response.setStatus(500)
				return
			}
		}
		else {
			log.warn("Attempt to update $serviceProvider by $subject was denied, incorrect permission set")
			response.sendError(403)
		}
	}
	
	def remove = {
		if(!params.id) {
			log.warn "SPSSODescriptor ID was not present"
			render message(code: 'fedreg.controllers.namevalue.missing')
			response.setStatus(500)
			return
		}
		if(!params.categoryID) {
			log.warn "Category ID was not present"
			render message(code: 'fedreg.controllers.namevalue.missing')
			response.setStatus(500)
			return
		}
		
		def serviceProvider = SPSSODescriptor.get(params.id)
		if (!serviceProvider) {
			render message(code: 'aaf.fr.foundation.spssoroledescriptor.nonexistant')
			response.setStatus(500)
			return
		}
		def category = ServiceCategory.get(params.categoryID)
		if (!category) {
			render message(code: 'aaf.fr.foundation.servicecategory.nonexistant')
			response.setStatus(500)
			return
		}
		
		if(SecurityUtils.subject.isPermitted("descriptor:${serviceProvider.id}:category:remove")) {
			serviceProvider.removeFromServiceCategories(category)
			serviceProvider.save()
			if(serviceProvider.hasErrors()) {
				render message(code: 'aaf.fr.foundation.servicecategory.error.removing')
				response.setStatus(500)
				return
			}
			
			render message(code: 'aaf.fr.foundation.servicecategory.removed')
		}
		else {
			log.warn("Attempt to update $serviceProvider by $subject was denied, incorrect permission set")
			response.sendError(403)
		}
	}
}