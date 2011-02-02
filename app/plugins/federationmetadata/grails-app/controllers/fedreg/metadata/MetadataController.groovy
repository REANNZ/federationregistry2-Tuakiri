package fedreg.metadata

import groovy.xml.MarkupBuilder
import fedreg.core.*

class MetadataController {
	def metadataGenerationService
	def grailsApplication
	
	def currentminimal = {
		def xml = currentPublishedMetadata(true, true)
		render(text:xml, contentType:"text/xml", encoding:"UTF-8")
	}
	
	def currentminimalnoext = {
		def xml = currentPublishedMetadata(true, false)
		render(text:xml, contentType:"text/xml", encoding:"UTF-8")
	}
	
	def current = {
		def xml = currentPublishedMetadata(false, true)
		render(text:xml, contentType:"text/xml", encoding:"UTF-8")
	}
	
	def all = {
		def xml = allMetadata(false)
		render(text:xml, contentType:"text/xml", encoding:"UTF-8")
	}
	
	def view = {
		def md = currentPublishedMetadata(false, true)
		[md:md]
	}
	
	def viewall = {
		def md = allMetadata(false)
		[md:md]
	}
	
	private def currentPublishedMetadata(def minimal, def ext) {
		def now = new Date();
		def validUntil = now + grailsApplication.config.fedreg.metadata.current.validForDays
		def federation = grailsApplication.config.fedreg.metadata.federation
		def certificateAuthorities = CAKeyInfo.list()
		
		def writer = new StringWriter()
		def builder = new MarkupBuilder(writer)
		builder.doubleQuotes = true
		
		def entitiesDescriptor = EntitiesDescriptor.findWhere(name:federation)
		entitiesDescriptor.entityDescriptors = EntityDescriptor.list()
		
		metadataGenerationService.entitiesDescriptor(builder, false, minimal, ext, entitiesDescriptor, validUntil, certificateAuthorities)
		writer.toString()
	}
	
	private def allMetadata(def minimal) {
		def now = new Date();
		def validUntil = now + grailsApplication.config.fedreg.metadata.all.validForDays
		def federation = grailsApplication.config.fedreg.metadata.federation
		def certificateAuthorities = CAKeyInfo.list()
		
		def writer = new StringWriter()
		def builder = new MarkupBuilder(writer)
		builder.doubleQuotes = true
		
		def entitiesDescriptor = EntitiesDescriptor.findWhere(name:federation)
		entitiesDescriptor.entityDescriptors = EntityDescriptor.list()
		
		metadataGenerationService.entitiesDescriptor(builder, true, minimal, true, entitiesDescriptor, validUntil, certificateAuthorities)
		writer.toString()
	}
		
}