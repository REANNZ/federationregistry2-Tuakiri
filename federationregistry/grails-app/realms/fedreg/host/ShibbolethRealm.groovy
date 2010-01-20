package fedreg.host

import org.apache.shiro.authc.UnknownAccountException
import org.apache.shiro.authc.DisabledAccountException
import org.apache.shiro.authc.SimpleAccount
import org.apache.shiro.authc.IncorrectCredentialsException

import grails.plugin.nimble.InstanceGenerator
import grails.plugin.nimble.core.*

import fedreg.host.ShibbolethService

import fedreg.saml2.metadata.orm.EntityDescriptor

/**
 * Integrates with Shiro to establish a session for users accessing the system based
 * on authentication with a Shibboleth IDP in the federation (relies on Apache Shibboleth SP to initiate session and handle SAML internals).
 *
 * @author Bradley Beddoes
 */
class ShibbolethRealm {
	static authTokenClass = fedreg.auth.ShibbolethToken
	
	def userService
	def adminsService
	def grailsApplication
	
	def authenticate(authToken) {
        if (!grailsApplication.config.fedreg.shibboleth.federationprovider.enabled) {
            log.error("Authentication attempt for Shibboleth provider, denying attempt as Shibboleth disabled")
            throw new UnknownAccountException("Authentication attempt for Shibboleth provider, denying attempt as Shibboleth disabled")
        }

		if (!authToken.principal) {
            log.error("Authentication attempt for Shibboleth provider, denying attempt as no persistent identifier (ShibbolethToken.principal) was provided")
            throw new UnknownAccountException("Authentication attempt for Shibboleth provider, denying attempt as no persistent identifier (ShibbolethToken.principal) was provided")
        }

		if (!authToken.entityID) {
            log.error("Authentication attempt for Shibboleth provider, denying attempt as no entityID (ShibbolethToken.entityID) was provided")
            throw new UnknownAccountException("Authentication attempt for Shibboleth provider, denying attempt as no entityID (ShibbolethToken.entityID) was provided")
        }

		def user = UserBase.findByUsername(authToken.principal)
        if (!user) {
            log.info("No account representing user ${authToken.principal} exists")
			def shibbolethFederationProvider = FederationProvider.findByUid(ShibbolethService.federationProviderUid)
			if (shibbolethFederationProvider && shibbolethFederationProvider.autoProvision) {	
                log.info("Shibboleth auto provision is enabled, creating user account for ${authToken.principal} belonging to Entity ${authToken.entityID}")

				def entityDescriptor = EntityDescriptor.findWhere(entityID:authToken.entityID)
				if(!entityDescriptor) {
					log.error("Authentication attempt for Shibboleth provider, denying attempt as no Entity matching (ShibbolethToken.entityID) is available. Has bootstrap occured?")
		            throw new UnknownAccountException("Authentication attempt for Shibboleth provider, denying attempt as no Entity matching (ShibbolethToken.entityID) is available. Has bootstrap occured?")
				}

				UserBase newUser = InstanceGenerator.user()
	            newUser.username = authToken.principal
	            newUser.enabled = true
	            newUser.external = true
	            newUser.federated = true
				newUser.federationProvider = shibbolethFederationProvider
				newUser.entityDescriptor = entityDescriptor
			
				newUser.profile = InstanceGenerator.profile()
                newUser.profile.owner = newUser
                newUser.profile.fullName = "${authToken.givenName} ${authToken.surname}"
				newUser.profile.email = authToken.email
					
				user = userService.createUser(newUser)
                if (user.hasErrors()) {
                    log.error("Error creating user account from Shibboleth credentials for ${authToken.principal}")
                    user.errors.each {
                        log.error(it)
                    }
                    throw new RuntimeException("Account creation exception for new shibboleth based account");
                }
                log.info("Created new user [$user.id]$user.username from Shibboleth attribute statement")

				// To assist with bootstrap provide the first account with admin privilledges
				// ==1 because we created and saved above
				if(UserBase.count() == 1) {
					adminsService.add(user)
					log.info("Issued account $user.username with admin right as this was the first account entering the system")
				}
			}
			else
            	throw new UnknownAccountException("No account representing user $username exists and autoProvision is false")
		}else {
			// Update name and email to what IDP has supplied - this could be extended to role membership etc in the future
			boolean change = false
			def fullName = "${authToken.givenName} ${authToken.surname}"
			def email = authToken.email	
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
				log.info("Updated account ${authToken.principal} with new IDP supplied values for fullname $fullName and email $email")
			}
		}
		
		if (!user.enabled) {
            log.warn("Attempt to authenticate using using Shibboleth with locally disabled account [$user.id]$user.username")
            throw new DisabledAccountException("The account [$user.id]$user.username accessed via Shibboleth is disabled")
        }
		log.info("Successfully logged in user [$user.id]$user.username using Shibboleth")
        def account = new SimpleAccount(user.id, authToken.principal, "redreg.realm.ShibbolethRealm")
        return account
    }

	public String toString() {
		"Realm: fedreg.host.ShibbolethRealm"
	}

}