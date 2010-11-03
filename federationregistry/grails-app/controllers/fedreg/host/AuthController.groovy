package fedreg.host

import grails.util.GrailsUtil
import org.codehaus.groovy.grails.commons.GrailsApplication

import org.apache.shiro.SecurityUtils
import org.apache.shiro.authc.AuthenticationException
import org.apache.shiro.authc.UsernamePasswordToken
import org.apache.shiro.authc.IncorrectCredentialsException
import org.apache.shiro.authc.DisabledAccountException

import fedreg.auth.ShibbolethToken
import fedreg.core.EntityDescriptor

/**
 * Overrides Nimble AuthController so as to only support Shibboleth based authentication
 *
 * @author Bradley Beddoes
 */
class AuthController {
	private static String TARGET = 'fedreg.controllers.AuthController.TARGET'
	
	def grailsApplication
	def userService
	def dataImporterService
	
	static Map allowedMethods = [ devauth: 'POST' ]
	
	def index = {
		redirect (action:login)
	}
	
	def login = {
		if(params.targetUri)
        	session.setAttribute(AuthController.TARGET, params.targetUri)

		// Integrates with Shibboleth NativeSPSessionCreationParameters as per https://spaces.internet2.edu/display/SHIB2/NativeSPSessionCreationParameters
		// This allows us to mix and match publicly available and private content within Federation Registry by making use of Nimble provided
		// security filters in conf/SecurityFilters.groovy
		if (grailsApplication.config.fedreg.shibboleth.federationprovider.spactive) {
			def localAction = createLink(action:'shibauth', absolute: true)
			def url = "${grailsApplication.config.fedreg.shibboleth.federationprovider.ssoendpoint}?target=${localAction}"
			
			redirect (url: url)
			return
		}
		
		if (GrailsUtil.environment != GrailsApplication.ENV_DEVELOPMENT) {
			response.sendError(403)
			return
		}
	}
	
	def logout = {
        signout()
    }

	def echo = {
	    def attr = [:]
		request.headerNames.each {
			attr.put(it, request.getHeader(it))
		}
		return [attr: attr]
	}

    def signout = { 
        log.info("Signing out user")
        SecurityUtils.subject?.logout()
        redirect(uri: '/')
    }
	
	def unauthorized = {
		
	}
	
	def shibauth = {
		def attr = [:]
		if (grailsApplication.config.fedreg.shibboleth.federationprovider.spactive) {	

			def uniqueID = request.getHeader(grailsApplication.config.fedreg.shibboleth.headers.uniqueID)
			def givenName = request.getHeader(grailsApplication.config.fedreg.shibboleth.headers.givenName)
			def surname = request.getHeader(grailsApplication.config.fedreg.shibboleth.headers.surname)
			def email = request.getHeader(grailsApplication.config.fedreg.shibboleth.headers.email)
			def entityID = request.getHeader(grailsApplication.config.fedreg.shibboleth.headers.entityID)
			def homeOrganization = request.getHeader(grailsApplication.config.fedreg.shibboleth.headers.homeOrganization)
			def homeOrganizationType = request.getHeader(grailsApplication.config.fedreg.shibboleth.headers.homeOrganizationType)
			
			def incomplete = false
			def errors = []
			
			if (!uniqueID) {
				incomplete = true
				errors.add('fedreg.controller.auth.incomplete.uniqueID')
			}

			if (!homeOrganization) {
				incomplete = true
				errors.add('fedreg.controller.auth.incomplete.homeorganization')
			}
			
			if (!homeOrganizationType) {
				incomplete = true
				errors.add('fedreg.controller.auth.incomplete.homeorganizationtype')
			}

			if (!givenName) {
				incomplete = true
				errors.add('fedreg.controller.auth.incomplete.givenname')
			}

			if (!surname) {
				incomplete = true
				errors.add('fedreg.controller.auth.incomplete.surname')
			}

			if (!email) {
				incomplete = true
				errors.add('fedreg.controller.auth.incomplete.email')
			}
			
			if(entityID) {
				def entityDescriptor = EntityDescriptor.findWhere(entityID:entityID)
				if(!entityDescriptor) {
					incomplete = true
					errors.add('fedreg.controller.auth.incomplete.entityunknown')
				}
			} else {
				incomplete = true
				errors.add('fedreg.controller.auth.incomplete.entitynotprovided')	
			}
			
			if(incomplete) {
				log.info "Incomplete shibboleth based authentication attempt with the following details: $uniqueID, $givenName, $surname, $email, $entityID, $homeOrganization, $homeOrganizationType was aborted"
				render (view:"shibincomplete", model:[errors:errors])
				return
			}
			
			
			log.debug "Attempting shibboleth based authentication with the following details: $uniqueID, $givenName, $surname, $email, $entityID, $homeOrganization, $homeOrganizationType"
			
			try {
				def authToken = new ShibbolethToken(principal:uniqueID, givenName:givenName, surname:surname, email:email, entityID:entityID, homeOrganization:homeOrganization, homeOrganizationType:homeOrganizationType)
				log.info "Attempting to establish session for user based on Shibboleth authentication with the following details: $uniqueID, $givenName, $surname, $email, $entityID" 
				
				SecurityUtils.subject.login(authToken)
		        this.userService.createLoginRecord(request)
		
				def targetUri = session.getAttribute(AuthController.TARGET)
	            session.removeAttribute(AuthController.TARGET)
				if(targetUri)
	            	redirect(uri: targetUri)
				else
					redirect(controller: "dashboard", action:"index")
				return
			}
	        catch (IncorrectCredentialsException e) {
	            log.info "Shibboleth credentials failure for user '${uniqueID}'."
	            log.debug(e)
	        }
	        catch (DisabledAccountException e) {
	            log.warn "Attempt to login via shibboleth to disabled account for user '${uniqueID}'."
	            log.debug(e)
	        }
	        catch (AuthenticationException e) {
	            log.info "General authentication failure for user '${uniqueID}'."
	            log.debug(e)
	        }
	
			redirect(action: shiberror)
		}
		else {
			log.error("Attempt to do shibboleth authentication when Apache SP is not marked active in local configuration")
			response.sendError(500)
		}	
	}
	
	def shiberror = {
		
	}
	
	def shibincomplete = {
		
	}
	
	def devauth = {
		if (!(GrailsUtil.environment == GrailsApplication.ENV_DEVELOPMENT) || grailsApplication.config.fedreg.shibboleth.federationprovider.spactive) {
			response.sendError(403)
			return
		}
		
		def authToken = new ShibbolethToken(principal:params.uniqueID, givenName:params.givenName, surname:params.surname, email:params.email, entityID:params.entityID, homeOrganization:params.homeOrganization, homeOrganizationType:params.homeOrganizationType)
		SecurityUtils.subject.login(authToken)
        this.userService.createLoginRecord(request)
        def targetUri = session.getAttribute(AuthController.TARGET)
        session.removeAttribute(AuthController.TARGET)
        if(targetUri)
        	redirect(uri: targetUri)
		else
			redirect(controller: "dashboard", action:"index")
	}

}