package fedreg.host

import org.apache.shiro.SecurityUtils

import grails.util.GrailsUtil
import org.codehaus.groovy.grails.commons.GrailsApplication

import fedreg.core.AttributeScope
import fedreg.core.AttributeCategory
import fedreg.core.SamlURI
import fedreg.core.SamlURIType
import fedreg.host.BootstrapRecord

/**
 * Overrides Nimble AuthController so as to only support Shibboleth based authentication
 *
 * @author Bradley Beddoes
 */
class DataManagementController {

	def dataImporterService
	
	def index = {
		[records:DataLoadRecord.list()]
	}
	
	// TODO
	// Only allow this to run when no Users are populated into the database as an inital bootstrap process
	// This will need to be extended and made more solid as resource registry is phased out.
	def bootstrap = {
		if((User.count() == 0 ) && (DataLoadRecord.count() == 0) ) {
			log.info("Doing initial bootstrap process..")
			initialPopulate()
			populate()
			redirect(action:"index")
		}
		else {
			response.sendError(403)
		}
	}
	
	def refreshdata = {
		dataImporterService.dumpData()
		populate()
		SecurityUtils.subject?.logout()
		redirect(action:"index")
	}
	
	private void populate() {
		dataImporterService.importOrganizations()
		dataImporterService.importContacts()
		dataImporterService.importAttributes()
		dataImporterService.importEntities()
		dataImporterService.importIdentityProviders()
		
		def dataLoadRecord = new DataLoadRecord(invoker:authenticatedUser?:null, remoteAddr: request.getRemoteAddr(), remoteHost: request.getRemoteHost(), userAgent:request.getHeader("User-Agent"))
		dataLoadRecord.save()
	}
	
	private void initialPopulate() {
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
	}

}