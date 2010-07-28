package fedreg.host

import org.apache.shiro.authc.UnknownAccountException
import org.apache.shiro.authc.DisabledAccountException
import org.apache.shiro.authc.SimpleAccount
import org.apache.shiro.authc.IncorrectCredentialsException

import grails.plugins.nimble.InstanceGenerator
import grails.plugins.nimble.core.*

import fedreg.core.EntityDescriptor
import fedreg.core.Contact
import fedreg.core.MailURI

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
	def groupService
	def roleService
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
		
		if (!authToken.homeOrganization) {
			log.error("Authentication attempt for Shibboleth provider, denying attempt as no homeOrganization (ShibbolethToken.homeOrganization) was provided")
			throw new UnknownAccountException("Authentication attempt for Shibboleth provider, denying attempt as no homeOrganization (ShibbolethToken.homeOrganization) was provided")
		}
		
		def entityDescriptor = EntityDescriptor.findWhere(entityID:authToken.entityID)
		if(!entityDescriptor) {
			log.error("Authentication attempt for Shibboleth provider, denying attempt as no Entity matching (ShibbolethToken.entityID) is available. Has bootstrap occured?")
			throw new UnknownAccountException("Authentication attempt for Shibboleth provider, denying attempt as no Entity matching (ShibbolethToken.entityID) is available. Has bootstrap occured?")
		}

		UserBase.withTransaction {
			def user = UserBase.findByUsername(authToken.principal)
			if (!user) {
				log.info("No account representing user ${authToken.principal} exists")
				def shibbolethFederationProvider = FederationProvider.findByUid(ShibbolethService.federationProviderUid)
				if (shibbolethFederationProvider && shibbolethFederationProvider.autoProvision) {	
					log.info("Shibboleth auto provision is enabled, creating user account for ${authToken.principal} belonging to Entity ${authToken.entityID}")

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
					newUser.profile.email = (authToken.email == "") ? null : authToken.email
					
					user = userService.createUser(newUser)
					if (user.hasErrors()) {
						log.error("Error creating user account from Shibboleth credentials for ${authToken.principal}")
						user.errors.each {
							log.error(it)
						}
						throw new RuntimeException("Account creation exception for new shibboleth based account");
					}
				
					// Attempt to link to local contact instance
					def contact = MailURI.findByUri(newUser.profile.email)?.contact
					if(!contact) {
						contact = new Contact(givenName:authToken.givenName, surname:authToken.surname, email:new MailURI(uri:authToken.email), userLink:true, userID: user.id, organization: entityDescriptor.organization)
						if(!contact.save()) {
							log.error "Unable to create Contact to link with incoming user" 
							contact.errors.each { log.error it }
							throw new UnknownAccountException("Unable to create Contact to link with incoming user")
						}
						user.contact = contact
						if(!user.save()) {
							log.error "Unable to create Contact link with user" 
							contact.errors.each { log.error it }
							throw new UnknownAccountException("Unable to create Contact link with user")
						}
					}
					log.info("Created new user [$user.id]$user.username and associated ${contact} from Shibboleth attribute statement")
				
					// To assist with bootstrap provide the first real user account with admin privilledges
					// ==2 because we creat internaladministrator in bootstrap and saved above
					if(UserBase.count() == 2) {
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
				def email = (authToken.email == "") ? null : authToken.email 
				def contact = MailURI.findByUri(user.profile.email).contact
			
				if(user.profile.fullName != fullName) {
					change = true
					contact.givenName = authToken.givenName
					contact.surname = authToken.surname
					user.profile.fullName = fullName
				}	
				if(user.profile.email != email) {
					change = true
					contact.email.uri = email
					user.profile.email = email
				}
				if(user.entityDescriptor != entityDescriptor) {
					change = true
					user.entityDescriptor = entityDescriptor
				}
				if(change) {
					userService.updateUser(user)
					if(!contact.save()) {
						log.error "Unable to update Contact to link new details of incoming user" 
						throw new UnknownAccountException("Unable to update Contact to link new details of incoming user")
					}
				
					log.info("Updated account ${authToken.principal} and associated ${contact} with new IDP supplied values for fullname $fullName and email $email")
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
	}

	public String toString() {
		"Realm: fedreg.host.ShibbolethRealm"
	}

}