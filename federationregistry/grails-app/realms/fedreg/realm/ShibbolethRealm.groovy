package fedreg.realm

import org.apache.shiro.authc.UnknownAccountException
import org.apache.shiro.authc.DisabledAccountException
import org.apache.shiro.authc.SimpleAccount
import org.apache.shiro.authc.IncorrectCredentialsException

import intient.nimble.InstanceGenerator
import intient.nimble.core.*

/**
 * Integrates with Shiro to establish a session for users accessing the system based
 * on authentication with a Shibboleth IDP in the federation (relies on Apache Shibboleth SP to initiate session and handle SAML internals).
 *
 * @author Bradley Beddoes
 */
public class ShibbolethRealm {
	
	static authTokenClass = fedreg.auth.ShibbolethToken
	
	def userService
	def adminsService
	
	def authenticate(authToken) {
        if (!grailsApplication.config.fedreg.shibboleth.federationprovider.enabled) {
            log.error("Authentication attempt for Shibboleth provider, denying attempt as Shibboleth disabled")
            throw new UnknownAccountException("Authentication attempt for Shibboleth provider, denying attempt as Shibboleth disabled")
        }

		def username = authToken.attr.getHeader(grailsApplication.config.fedreg.shibboleth.headers.uniqueIdentifier)
		def user = UserBase.findByUsername(username)
        if (!user) {
            log.info("No account representing user $username exists")
			def shibbolethFederationProvider = FederationProvider.findByUid(ShibbolethService.federationProviderUid)
			
			if (shibbolethFederationProvider && shibbolethFederationProvider.autoProvision) {
                log.debug("Shibboleth auto provision is enabled, creating user account for $username")

				UserBase newUser = InstanceGenerator.user()
	            newUser.username = username
	            newUser.enabled = true
	            newUser.external = true
	            newUser.federated = true
			
				newUser.profile = InstanceGenerator.profile()
                newUser.profile.owner = newUser
                newUser.profile.fullName = "${authToken.attr.get(grailsApplication.config.fedreg.shibboleth.headers.givenName)} ${authToken.attr.get(grailsApplication.config.fedreg.shibboleth.headers.surname)}"
				newUser.profile.email = authToken.attr.get(grailsApplication.config.fedreg.shibboleth.headers.mail)
				
				user = userService.createUser(newUser)
                if (user.hasErrors()) {
                    log.error("Error creating user account from Shibboleth credentials for $username")
                    user.errors.each {
                        log.error(it)
                    }
                    throw new RuntimeException("Account creation exception for new shibboleth based account");
                }
                log.info("Created new user [$user.id]$user.username from Shibboleth attribute statement")

				// To assist with bootstrap provide the first account with admin privilledges
				if(UserBase.count() == 0) {
					adminsService.add(admin)
					log.info("Issued account $username with admin privs as this was the first account entering the system")
				}
			}
			else
            	throw new UnknownAccountException("No account representing user $username exists and autoProvision is false")
		}else {
			// Update name and email to what IDP has supplied - this could be extended to role membership etc in the future
			boolean change = false
			def fullName = "${authToken.attr.get(grailsApplication.config.fedreg.shibboleth.headers.givenName)} ${authToken.attr.get(grailsApplication.config.fedreg.shibboleth.headers.surname)}"
			def email = authToken.attr.get(grailsApplication.config.fedreg.shibboleth.headers.mail)	
			if(user.profile.fullName != fullName) {
				change = true
				user.profile.fullName = fullName
			}	
			if(user.profile.email != email) {
				change = true
				user.profile.email != email
			}
			if(change) {
				userService.updateUser(user)
				log.info("Updated account $username with new IDP supplied values for fullname $fullName and email $email")
			}
		}
		
		if (!user.enabled) {
            log.warn("Attempt to authenticate using using Shibboleth with locally disabled account [$user.id]$user.username")
            throw new DisabledAccountException("The account [$user.id]$user.username accessed via Shibboleth is disabled")
        }
		log.info("Successfully logged in user [$user.id]$user.username using Shibboleth")
        def account = new SimpleAccount(user.id, username, "redreg.realm.ShibbolethRealm")
        return account
    }

}