package fedreg.api.v1

import groovy.xml.MarkupBuilder
import grails.converters.*
import fedreg.core.*

/**
 * Provides metadata exports.
 *
 * @author Bradley Beddoes
 */
class MetadataAPIv1Controller {
	def metadataGenerationService
	def grailsApplication
	
	def list = {
		def metadata = []
		metadata.add([minimal:[description:"Minimal set of SAML metadata compliant with Shibboleth 2.x clients", format:"xml", type:"federation", link:g.createLink(controller: 'metadataAPI', params:[type:"minimal"], absolute: true)]])
		metadata.add([minimalnoext:[description:"Minimal set of SAML metadata compliant with Shibboleth 1.x clients", format:"xml", type:"federation", link:g.createLink(controller: 'metadataAPI', params:[type:"minimalnoext"], absolute: true)]])
		metadata.add([current:[description:"Extended SAML 2 metadata compatible with Shibboleth 2.x clients and other SAML 2 compliant implementations", format:"xml", type:"federation", link:g.createLink(controller: 'metadataAPI', params:[type:"current"], absolute: true)]])
		metadata.add([all:[description:"Extended SAML 2 metadata includes all federation components including those unapproved, non-functioning and archived", format:"xml", type:"federation", link:g.createLink(controller: 'metadataAPI', params:[type:"all"], absolute: true)]])
		metadata.add([entity:[description:"SAML 2 compliant metadata snippet for specific entity descriptor ID", format:"xml", type:"federation", link:g.createLink(controller: 'metadataAPI', params:[type:"entity"], absolute: true)]])
		
		render metadata as JSON
	}
	
	def show = {
		def xml
		switch(params.type) {
			case "minimal": 		xml = currentPublishedMetadata(true, true)
									break
			case "minimalnoext":  	xml = currentPublishedMetadata(true, false)
									break
			case "current": 		xml = currentPublishedMetadata(false, true)
									break
			case "all": 			xml = allMetadata(false)
									break
			case "entity": 			xml = entityMetadata()
									break
		}
		
		if(xml)
			render(text:xml, contentType:"text/xml", encoding:"UTF-8")
		else {
			response.sendError(400)
		}			
	}
	
	private String entityMetadata() {
		if(!params.id) {
			log.warn "EntityDescriptor ID was not present"
			return
		}
		
		def entity = EntityDescriptor.get(params.id)
		if (!entity) {
			log.warn "EntityDescriptor ID ${params.id} was not valid"
			return
		}
		
		def writer = new StringWriter()
		def builder = new MarkupBuilder(writer)
		builder.doubleQuotes = true
		
		metadataGenerationService.entityDescriptor(builder, false, false, true, entity, true)
		writer.toString()
	}
	
	private String currentPublishedMetadata(def minimal, def ext) {
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
	
	private String allMetadata(def minimal) {
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