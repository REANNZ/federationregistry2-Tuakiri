
import org.codehaus.groovy.grails.commons.GrailsApplication
import grails.util.GrailsUtil

import fedreg.core.AttributeScope
import fedreg.core.AttributeCategory
import fedreg.core.SamlURI
import fedreg.core.SamlURIType

class BootStrap {
	
	def dataImporterService

     def init = { servletContext ->
			def fedScope = new AttributeScope(name:'Federation')
			fedScope.save()
	
			def localScope = new AttributeScope(name:'Local')
			localScope.save()
	
			def mandatoryCategory = new AttributeCategory(name:'Mandatory')
			mandatoryCategory.save()
	
			def recommendedCategory = new AttributeCategory(name:'Recommended')
			recommendedCategory.save()
	
			def optionalCategory = new AttributeCategory(name:'Optional')
			optionalCategory.save()
			
			// TODO externalize to some text file for easier addition in the future?
			def httpRedirect = new SamlURI(type:SamlURIType.ProtocolBinding, uri:'urn:oasis:names:tc:SAML:2.0:bindings:HTTP-Redirect')
			httpRedirect.save()
			
			def httpPost = new SamlURI(type:SamlURIType.ProtocolBinding, uri:'urn:oasis:names:tc:SAML:2.0:bindings:HTTP-POST')
			httpPost.save()
			
			def httpPostSimple = new SamlURI(type:SamlURIType.ProtocolBinding, uri:'urn:oasis:names:tc:SAML:2.0:bindings:HTTP-POST-SimpleSign')
			httpPostSimple.save()
			
			def shibAuthn = new SamlURI(type:SamlURIType.ProtocolBinding, uri:'urn:mace:shibboleth:1.0:profiles:AuthnRequest')
			shibAuthn.save()
			
			
			dataImporterService.importOrganizations()
			dataImporterService.importContacts()
			dataImporterService.importAttributes()
			dataImporterService.importEntities()
			dataImporterService.importIdentityProviders()
     }

     def destroy = {
     }
} 