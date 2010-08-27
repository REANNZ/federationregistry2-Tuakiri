package fedreg.metadata

import groovy.xml.MarkupBuilder
import fedreg.core.*

class MetadataController {
	def metadataGenerationService
	def grailsApplication
	
	def extensive = {
		def now = new Date();
		def validUntil = now + grailsApplication.config.fedreg.metadata.extensive.validForDays
		def cacheDuration = now + grailsApplication.config.fedreg.metadata.extensive.cacheForDays
		def federation = grailsApplication.config.fedreg.metadata.federation
		def certificateAuthorities = CAKeyInfo.list()
		
		def writer = new StringWriter()
		def builder = new MarkupBuilder(writer)
		
		def entitiesDescriptor = new EntitiesDescriptor(name:federation)
		entitiesDescriptor.entityDescriptors = EntityDescriptor.list()
		
		metadataGenerationService.entitiesDescriptor(builder, entitiesDescriptor, validUntil, cacheDuration, certificateAuthorities)
		def xml = writer.toString()
		render(text:xml, contentType:"text/xml", encoding:"UTF-8")
	}
		
}