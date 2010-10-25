package fedreg.metadata

import groovy.xml.MarkupBuilder
import fedreg.core.*

class AttributeFilterController {
	def attributeFilterGenerationService
	def grailsApplication
	
	def generate = {
		if(!params.id) {
			log.warn "IDPSSODescriptor ID was not present"
			response.sendError(403)
			return
		}
		
		def identityProvider = IDPSSODescriptor.get(params.id)
		if (!identityProvider) {
			log.warn "IDPSSODescriptor does not exist for ${params.id}"
			response.sendError(403)
			return
		}
		
		def writer = new StringWriter()
		def builder = new MarkupBuilder(writer)
		builder.doubleQuotes = true
		
		def federation = grailsApplication.config.fedreg.metadata.federation
		
		attributeFilterGenerationService.generate(builder, "$federation:generatedpolicy", federation, identityProvider.id)
		def xml = writer.toString()
		
		render(text:xml, contentType:"text/xml", encoding:"UTF-8")
	}
}