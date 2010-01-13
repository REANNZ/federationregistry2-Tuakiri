package fedreg.controllers

import org.apache.shiro.SecurityUtils
import org.apache.shiro.authc.AuthenticationException
import org.apache.shiro.authc.UsernamePasswordToken
import org.apache.shiro.authc.IncorrectCredentialsException
import org.apache.shiro.authc.DisabledAccountException

import fedreg.auth.ShibbolethToken

/**
 * Overrides Nimble AuthController so as to only support Shibboleth based authentication
 *
 * @author Bradley Beddoes
 */
class AuthController {
	private static String TARGET = 'fedreg.controllers.AuthController.TARGET'
	
	def grailsApplication
	def userService
	
	def index = {
		redirect (action:login)
	}
	
	def login = {
		if (grailsApplication.config.fedreg.shibboleth.federationprovider.spactive) {
			redirect (action:shibauth)
			return
		}
	}
	
	def logout = {
		
	}
	
	def unauthorized = {
		
	}
	
	def shibauth = {
		def attr = [:]
		if (grailsApplication.config.fedreg.shibboleth.federationprovider.spactive) {
			
		}
		else {
			log.error("Attempt to do shibboleth authentication when Apache SP is not marked active in local configuration")
			response.sendError(500)
		}	
	}
	
	def devauth = {
		if (grailsApplication.config.fedreg.shibboleth.federationprovider.spactive) {
			response.sendError(403)
			return
		}
		
		def authToken = new ShibbolethToken(principal:params.uniqueID, givenName:params.givenName, surname:params.surname, email:params.email)
		
		SecurityUtils.subject.login(authToken)
        this.userService.createLoginRecord(request)

        def targetUri = session.getAttribute(AuthController.TARGET) ?: "/"
        session.removeAttribute(AuthController.TARGET)

        log.info("Authenticated user, $params.username. Directing to content $targetUri")
        redirect(uri: targetUri)
	}

}