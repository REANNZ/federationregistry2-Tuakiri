package fedreg.wayf

import fedreg.core.*

/**
 * Integrates with Switch based WAYF by providing configuration for selectable categories etc
 *
 * @author Bradley Beddoes
*/
class WayfController {
	
	def grailsApplication

	def generateconfiguration = {
		def organizationTypes = [] as List		
		def identityProviders = IDPSSODescriptor.list().findAll { idp -> idp.functioning() }.sort { idp -> idp.displayName }
		
		def ssoPostEndpoints = [:]
		
		def types = grailsApplication.config.fedreg.metadata.wayf.generateconfig.orgtypes
		types.each { name ->
			def ot = OrganizationType.findWhere(name:name, discoveryServiceCategory:true)
			if(ot)
				organizationTypes.add(ot)
		}

		// Figure out SSO Post profile for each IDP, favouring SAML 2
		identityProviders.each { idp ->
			def ssoEndpoint = false
			idp.singleSignOnServices.each { ep ->
				if (ep.binding.uri == 'urn:mace:shibboleth:1.0:profiles:AuthnRequest') {
					ssoPostEndpoints.put(idp.id,  ep.location.uri)
					ssoEndpoint = true
				}
			}
			if(!ssoEndpoint) {
				idp.singleSignOnServices.each { ep ->
					if (ep.binding.uri == 'urn:oasis:names:tc:SAML:2.0:bindings:HTTP-POST')
						ssoPostEndpoints.put(idp.id,  ep.location.uri)
				}
			}
		}
		
		render view: "generateconfiguration", model:[organizationTypes:organizationTypes, identityProviders: identityProviders, ssoPostEndpoints:ssoPostEndpoints], contentType:"text/plain", encoding:"UTF-8"
	}

}