package aaf.fr.metadata

import groovy.xml.MarkupBuilder
import aaf.fr.foundation.*

/**
 * Provides metadata views.
 *
 * @author Bradley Beddoes
 */
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
	
	def entity = {
		if(!params.id) {
			render message(code: 'fedreg.controllers.namevalue.missing')
			response.setStatus(500)
			return
		}
		
		def entity = EntityDescriptor.get(params.id)
		if (!entity) {
			render message(code: 'fedreg.endpoint.nonexistant', args: [params.id])
			response.setStatus(500)
			return
		}
		
		def md = entityMetadata(entity)
		
		render template:"/templates/entitymetadata", contextPath: pluginContextPath, model:[md:md], contentType: "text/plain"
	}
	
	private def currentPublishedMetadata(def minimal, def ext) {
		def now = new Date();
		def validUntil = now + grailsApplication.config.aaf.fr.metadata.current.validForDays
		def federation = grailsApplication.config.aaf.fr.metadata.federation
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
		def validUntil = now + grailsApplication.config.aaf.fr.metadata.all.validForDays
		def federation = grailsApplication.config.aaf.fr.metadata.federation
		def certificateAuthorities = CAKeyInfo.list()
		
		def writer = new StringWriter()
		def builder = new MarkupBuilder(writer)
		builder.doubleQuotes = true
		
		def entitiesDescriptor = EntitiesDescriptor.findWhere(name:federation)
		entitiesDescriptor.entityDescriptors = EntityDescriptor.list()
		
		metadataGenerationService.entitiesDescriptor(builder, true, minimal, true, entitiesDescriptor, validUntil, certificateAuthorities)
		writer.toString()
	}
	
	private def entityMetadata(def entity) {
		def writer = new StringWriter()
		def builder = new MarkupBuilder(writer)
		builder.doubleQuotes = true
		
		metadataGenerationService.entityDescriptor(builder, false, false, true, entity, true)
		writer.toString()
	}
		
}