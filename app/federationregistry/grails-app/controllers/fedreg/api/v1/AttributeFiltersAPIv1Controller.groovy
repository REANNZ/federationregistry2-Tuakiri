package fedreg.api.v1

import fedreg.core.*
import grails.converters.JSON
import groovy.xml.MarkupBuilder

class AttributeFiltersAPIv1Controller {
	def attributeFilterGenerationService
	
	def list = {	
		def attributefilters = [:]
		
		
		def filters = []
		attributefilters.attributefilters = filters
		
		// Distribute non functioning IdP filters (but ignore archived) incase they come online and request AA before subsequent update
		def identityProviders = IDPSSODescriptor.list(archived:false)		
		identityProviders.each { idp ->
			def filter = [:]
			filter.id = idp.id
			filter.name = idp.displayName
			filter.link = g.createLink(controller: 'attributeFiltersAPI', id: idp.id, absolute: true)
			
			filters.add(filter)
		}
		
		render attributefilters as JSON		
	}
	
	def show = {		
		if(!params.id) {
			log.warn "IDPSSODescriptor ID was not present"
			render message(code: 'fedreg.controllers.namevalue.missing')
			response.sendError(400)
			return
		}
		
		def identityProvider = IDPSSODescriptor.get(params.id)
		if (!identityProvider) {
			log.warn "IDPSSODescriptor does not exist for ${params.id}"
			render message(code: 'fedreg.controllers.namevalue.missing')
			response.sendError(400)
			return
		}
		
		def writer = new StringWriter()
		def builder = new MarkupBuilder(writer)
		builder.doubleQuotes = true
		
		def federation = grailsApplication.config.fedreg.metadata.federation
		
		attributeFilterGenerationService.generate(builder, "$federation:generatedpolicy", federation, identityProvider.id)
		def xml = writer.toString()
		
		render(text:"${xml}\n\n<!-- Exported from Federation Registry at ${new Date()} -->\n\n", contentType:"text/xml", encoding:"UTF-8")		
	}
	
}