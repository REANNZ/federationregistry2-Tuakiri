package fedreg.api.v1

import fedreg.core.*
import grails.converters.JSON
import groovy.xml.MarkupBuilder

class ServiceProvidersAPIv1Controller {
	
	def list = {	
		def results = []

		def serviceProviders = SPSSODescriptor.list().sort{it.id}		
		serviceProviders.each { sp ->
			def result = [:]
			result.id = sp.id
			result.name = sp.displayName
			result.functioning = sp.functioning()
			result.archived = sp.archived
			result.link = g.createLink(controller: 'serviceProvidersAPIv1', id: sp.id, absolute: true)
			result.format = "json"
      
			results.add(result)
		}
		
		def response = [serviceproviders:results]
		render response as JSON		
	}
	
	def show = {		
		if(!params.id) {
			log.warn "SPSSODescriptor ID was not present"
			render message(code: 'fedreg.controllers.namevalue.missing')
			response.sendError(400)
			return
		}
		
		def sp = SPSSODescriptor.get(params.id)
		if (!sp) {
			log.warn "SPSSODescriptor does not exist for ${params.id}"
			render message(code: 'fedreg.controllers.namevalue.missing')
			response.sendError(400)
			return
		}
		
		def result = [:]
		result.id = sp.id
		result.created = sp.dateCreated
		result.updated = sp.lastUpdated
		result.name = sp.displayName
		result.description = sp.description
		result.weblink = g.createLink(controller: 'SPSSODescriptor', action:'show', id: sp.id, absolute: true)
		result.functioning = sp.functioning()
		result.archived = sp.archived
		result.entityid = sp.entityDescriptor.entityID
		result.categories = []
		sp.serviceCategories.each { result.categories.add(it.name) }
		
		def protocolSupportEnumerations = []
		sp.protocolSupportEnumerations.each { protocolSupportEnumerations.add(it.uri) }
		result.protocolsupportenumerations = protocolSupportEnumerations
		
		def response = [serviceprovider:result]
		render response as JSON	
	}
	
}