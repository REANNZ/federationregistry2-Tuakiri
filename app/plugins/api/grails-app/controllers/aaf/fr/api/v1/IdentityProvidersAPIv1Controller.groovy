package aaf.fr.api.v1

import aaf.fr.foundation.*
import grails.converters.JSON
import groovy.xml.MarkupBuilder

class IdentityProvidersAPIv1Controller {
	
	def list = {	
		def results = []

		def identityProviders = IDPSSODescriptor.list().sort{it.id}		
		identityProviders.each { idp ->
			def result = [:]
			result.id = idp.id
			result.name = idp.displayName
			result.functioning = idp.functioning()
			result.archived = idp.archived
			result.format = "json"
			result.link = g.createLink(controller: 'identityProvidersAPIv1', id: idp.id, absolute: true)
			
			results.add(result)
		}
		
		def response = [identityproviders:results]
		render response as JSON		
	}
	
	def show = {		
		if(!params.id) {
			log.warn "IDPSSODescriptor ID was not present"
			render message(code: 'controllers.fr.generic.namevalue.missing')
			response.sendError(400)
			return
		}
		
		def idp = IDPSSODescriptor.get(params.id)
		if (!idp) {
			log.warn "IDPSSODescriptor does not exist for ${params.id}"
			render message(code: 'controllers.fr.generic.namevalue.missing')
			response.sendError(400)
			return
		}
		
		def result = [:]
		result.id = idp.id
		result.created = idp.dateCreated
		result.updated = idp.lastUpdated
		result.name = idp.displayName
		result.description = idp.description
		result.weblink = g.createLink(controller: 'IDPSSODescriptor', action:'show', id: idp.id, absolute: true)
		result.functioning = idp.functioning()
		result.archived = idp.archived
		result.scope = idp.scope
		result.wantauthnrequestssigned = idp.wantAuthnRequestsSigned
		result.entityid = idp.entityDescriptor.entityID
		result.filter = [:]
		result.filter.format = "xml"
		result.filter.link = g.createLink(controller: 'attributeFiltersAPIv1', id: idp.id, absolute: true)
		
		def protocolSupportEnumerations = []
		idp.protocolSupportEnumerations.each { protocolSupportEnumerations.add(it.uri) }
		result.protocolsupportenumerations = protocolSupportEnumerations
		
		def response = [identityprovider:result]
		render response as JSON	
	}
	
}