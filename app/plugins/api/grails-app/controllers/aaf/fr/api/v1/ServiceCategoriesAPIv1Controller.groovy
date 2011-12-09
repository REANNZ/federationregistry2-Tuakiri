package aaf.fr.api.v1

import aaf.fr.foundation.*
import grails.converters.JSON

class ServiceCategoriesAPIv1Controller {
	
	def list = {
		def results = []

		def serviceCategories = ServiceCategory.list()		
		serviceCategories.each { sc ->
			def result = [:]
			result.id = sc.id
			result.name = sc.name
			result.description = sc.description
			result.format = "json"
			result.link = g.createLink(controller: 'serviceCategoriesAPIv1', id: sc.id, absolute: true)
			
			results.add(result)
		}
		
		def response = [servicecategories:results]
		render response as JSON
	}
	
	def show = {
		if(!params.id) {
			log.warn "Service Category ID was not present"
			render message(code: 'fedreg.controllers.namevalue.missing')
			response.sendError(400)
			return
		}
	
		def result = []
		def serviceCategory = ServiceCategory.get(params.id)
		if (!serviceCategory) {
			log.warn "ServiceCategory does not exist for ${params.id}"
			render message(code: 'fedreg.controllers.namevalue.missing')
			response.sendError(400)
			return
		}
		
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
	
		def response = [name:serviceCategory.name, description:serviceCategory.description, serviceproviders:result]
		render response as JSON
	}
}