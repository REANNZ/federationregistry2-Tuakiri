package fedreg.controllers

import org.apache.shiro.SecurityUtils
import org.apache.shiro.authc.AuthenticationException

import fedreg.auth.ShibbolethToken

/**
 * Overrides Nimble AuthController so as to only support Shibboleth based authentication
 *
 * @author Bradley Beddoes
 */
class AuthController {

	def grailsApplication
	
	def login = {
		
		if (grailsApplication.config.fedreg.shibboleth.federationprovider.enabled) {
			
		}
		else {
			def uniqueID = "https://idp.qut.edu.au/idp/shibboleth!https://manager.aaf.edu.au/shibboleth!d2404817-6fb9-4165-90d8-f895745000000"
			def attr = [:]
			attr.put(grailsApplication.config.fedreg.shibboleth.headers.uniqueIdentifier, uniqueID)
			attr.put(grailsApplication.config.fedreg.shibboleth.headers.givenName, "John")
			attr.put(grailsApplication.config.fedreg.shibboleth.headers.surname, "Doe")
			attr.put(grailsApplication.config.fedreg.shibboleth.headers.email, "johndoe@anonymous.org")
			
			def authToken = new ShibbolethToken(principal:uniqueID, attr:attr)
			
			SecurityUtils.subject.login(authToken)
            this.userService.createLoginRecord(request)

            def targetUri = session.getAttribute(AuthController.TARGET) ?: "/"
            session.removeAttribute(AuthController.TARGET)

            log.info("Authenticated user, $params.username. Directing to content $targetUri")
            redirect(uri: targetUri)
            return
		}
		
	}

}