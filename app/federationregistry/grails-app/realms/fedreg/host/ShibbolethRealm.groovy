package fedreg.host

import org.apache.shiro.authc.UnknownAccountException
import org.apache.shiro.authc.DisabledAccountException
import org.apache.shiro.authc.SimpleAccount
import org.apache.shiro.authc.IncorrectCredentialsException

import grails.plugins.nimble.InstanceGenerator
import grails.plugins.nimble.core.*

import fedreg.core.*

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
		
		if(authToken.displayName == null) {
			if (authToken.givenName == null || authToken.surname == null) {
				log.error("Authentication attempt for Shibboleth provider, denying attempt as no name details (either givenName + surname or displayName) provided")
				throw new UnknownAccountException("Authentication attempt for Shibboleth provider, denying attempt as no name details (either givenName + surname or displayName) provided")
			}
		}
		else { 
			if (!authToken.displayName.contains(' ')) {
				log.error("Authentication attempt for Shibboleth provider, provided displayName has only one part and will fail split on space char for local storage")
				throw new UnknownAccountException("Authentication attempt for Shibboleth provider, provided displayName has only one part and will fail split on space char for local storage")
			}
		}
		
		if (!authToken.email) {
			log.error("Authentication attempt for Shibboleth provider, denying attempt as no email (ShibbolethToken.email) was provided")
			throw new UnknownAccountException("Authentication attempt for Shibboleth provider, denying attempt as no email (ShibbolethToken.email) was provided")
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
					newUser.profile.email = (authToken.email == "") ? null : authToken.email.toLowerCase()
					
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
						contact = new Contact(givenName:authToken.givenName, surname:authToken.surname, email:new MailURI(uri:authToken.email?.toLowerCase()), organization: entityDescriptor.organization)
						if(!contact.save()) {
							log.error "Unable to create Contact to link with incoming user" 
							contact.errors.each { log.error it }
							throw new UnknownAccountException("Unable to create Contact to link with incoming user")
						}
					}
					
					user.contact = contact
					if(!user.save()) {
						log.error "Unable to create Contact link with user" 
						contact.errors.each { log.error it }
						throw new UnknownAccountException("Unable to create Contact link with user")
					}
					log.info("Created new user [$user.id]$user.username and associated ${contact} from Shibboleth attribute statement")
				
					// To assist with bootstrap provide the first account actually logging into the system with admin rights
					def adminAuthority = Role.findByName(AdminsService.ADMIN_ROLE)
					if(adminAuthority.users?.size() == 0) {
						adminsService.add(user)
						def federationAdminRole = Role.findByName("federation-administrators")
						roleService.addMember(user, federationAdminRole)
						log.info("Issued account $user with admin right as this was the first account entering the system")
					}
				}
				else
					throw new UnknownAccountException("No account representing user $username exists and autoProvision is false")
			}else {
				// Update name and email to what IDP has supplied - this could be extended to role membership etc in the future
				boolean change = false
				def fullName = "${authToken.givenName} ${authToken.surname}"
				def email = (authToken.email == "") ? null : authToken.email?.toLowerCase() 
			
				if(user.profile.email.toLowerCase() != email.toLowerCase()) {
					
					if(MailURI.findByUri(email)) {
						def contact_ = Contact.findWhere(email:MailURI.findByUri(email))
					
						if(contact_) {
							change = true
							def oldContact = user.contact
							// We need to update this user to point to the existing contact for the same email address, 
							// update all the referencing ContactPerson objects and then delete the now unused Contact
							def contactPersons = ContactPerson.findAllWhere(contact:oldContact)
							contactPersons.each {
								it.contact = contact_
								it.save()
							}
							user.contact = contact_
							user.profile.email = email
							user = userService.updateUser(user)
						
							oldContact.delete()
							oldContact.email?.delete()
							oldContact.secondaryEmail?.delete()

							oldContact.workPhone?.delete()
							oldContact.homePhone?.delete()
							oldContact.mobilePhone?.delete()	
						}
						else {
							user.contact.email.uri = email
							user.profile.email = email
						}
					}
					else {
						user.contact.email.uri = email
						user.profile.email = email
					}
				}
				if(user.profile.fullName != fullName || user.contact.givenName != authToken.givenName || user.contact.surname != authToken.surname) {
					change = true
					user.contact.givenName = authToken.givenName
					user.contact.surname = authToken.surname
					user.profile.fullName = fullName
				}	
				if(user.entityDescriptor != entityDescriptor) {
					change = true
					user.entityDescriptor = entityDescriptor
				}
				if(change) {
					userService.updateUser(user)
					if(!user.contact.save()) {
						log.error "Unable to update Contact to link new details of incoming user" 
						user.contact.errors.each {
							log.error it
						}
						throw new UnknownAccountException("Unable to update Contact to link new details of incoming user")
					}
				
					log.info("Updated account ${authToken.principal} and associated ${user.contact} with new IDP supplied values for fullname $fullName and email $email")
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