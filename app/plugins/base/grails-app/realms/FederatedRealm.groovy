

import org.apache.shiro.authc.UnknownAccountException
import org.apache.shiro.authc.DisabledAccountException
import org.apache.shiro.authc.SimpleAccount
import org.apache.shiro.authc.IncorrectCredentialsException

import grails.plugins.federatedgrails.SubjectBase
import grails.plugins.federatedgrails.SessionRecord

import aaf.fr.foundation.Contact
import aaf.fr.foundation.EntityDescriptor

import grails.plugins.federatedgrails.InstanceGenerator

class FederatedRealm {
  static authTokenClass = grails.plugins.federatedgrails.FederatedToken
  
  def grailsApplication
  
  def authenticate(token) {

    if (!grailsApplication.config.federation.enabled) {
      log.error "Authentication attempt for federated provider, denying attempt as federation integration disabled"
      throw new UnknownAccountException ("Authentication attempt for federated provider, denying attempt as federation disabled")
    }

    log.info "FederatedRealm starting authentication for subject identified in $token"
    if (!token.principal) {
      log.error "Authentication attempt for federated provider, denying attempt as no persistent identifier was provided"
      throw new UnknownAccountException("Authentication attempt for federated provider, denying attempt as no persistent identifier was provided")
    }
    if (!token.credential) {
      log.error "Authentication attempt for federated provider, denying attempt as no credential was provided"
      throw new UnknownAccountException("Authentication attempt for federated provider, denying attempt as no credential was provided")
    }

    def entityDescriptor = EntityDescriptor.findWhere(entityID:token.attributes.entityID)
    if(!entityDescriptor) {
      log.error("Authentication attempt for Shibboleth provider, denying attempt as no Entity matching ($token.attributes.entityID) is available.")
      throw new UnknownAccountException("Authentication attempt for Shibboleth provider, denying attempt as no Entity matching ($token.attributes.entityID) is available.")
    }

    SubjectBase.withTransaction {
      SubjectBase subject = SubjectBase.findByPrincipal(token.principal)

      if (!subject) {
        if(!grailsApplication.config.federation.autoprovision) {
          log.error "Authentication attempt for federated provider, denying attempt as federation integration is denying automated account provisioning"
          throw new DisabledAccountException("Authentication attempt for federated provider, denying attempt as federation integration is denying automated account provisioning")
        }
        
        // Here we don't already have a subject stored in the system so we need to create one
        log.info "No subject represented by ${token.principal} exists in local repository, provisioning new account"
        
        subject = InstanceGenerator.subject()
        subject.principal = token.principal
        subject.enabled = true
        
        subject.cn = token.attributes.cn
        if(token.attributes.email.contains(';')) {
          log.warn "Email porvided for ${token.principal} is multivalued (${token.attributes.email}) attempting to split on ; and use first returned value."
          subject.email = token.attributes.email.toLowerCase().split(';')[0]
        } else {
          subject.email = token.attributes.email.toLowerCase()
        }
        subject.sharedToken = token.attributes.sharedToken

        // Attempt to link to local contact instance
        def contact = Contact.findByEmail(subject.email)
        if(!contact) {
          contact = new Contact(givenName:subject.givenName, surname:subject.surname, email:subject.email, organization: entityDescriptor.organization)
          if(!contact.save()) {
            log.error "Unable to create Contact to link with incoming user"
            contact.errors.each { log.error it }
           throw new UnknownAccountException("Unable to create Contact to link with incoming user")
          }
        }
        subject.contact = contact  

        // Store in data repository
        if(!subject.save()) {
          subject.errors.each { err ->
            log.error err
          }
          throw new RuntimeException("Account creation exception for new federated account for ${token.principal}")
        }  
        
        log.info("Created ${subject} with associated ${contact} from ${entityDescriptor} from federated attribute statement")

      } else {
        subject.cn = token.attributes.cn
        subject.contact.givenName = subject.givenName
        subject.contact.surname = subject.surname
        
        if(token.attributes.email.contains(';')) {
          log.warn "Email provided for ${token.principal} is multivalued (${token.attributes.email}) attempting to split on ; and use first returned value."
          subject.email = token.attributes.email.toLowerCase().split(';')[0]
          subject.contact.email = token.attributes.email.toLowerCase().split(';')[0]
        } else {
          subject.email = token.attributes.email.toLowerCase()
          subject.contact.email = token.attributes.email.toLowerCase()
        }

        // We've just started collecting these so update previously stored accounts
        if(!subject.sharedToken) {
          subject.sharedToken = token.attributes.sharedToken
        } else if (subject.sharedToken != token.attributes.sharedToken) {
          log.error("Authentication halted for ${subject} as current sharedToken ${subject.sharedToken} does not match incoming token ${token.attributes.sharedToken}")
          throw new IncorrectCredentialsException("${subject} authentication halted as current sharedToken ${subject.sharedToken} does not match incoming token ${token.attributes.sharedToken}")
        }

        // Store in data repository
        if(!subject.save()) {
          subject.errors.each { err ->
            log.error err
          }
          throw new RuntimeException("Account update exception for existing federated account ${token.principal}")
        }
        log.info("Updated ${subject} with associated ${subject.contact} from ${entityDescriptor} from federated attribute statement")
      }
      
      if (!subject.enabled) {
        log.warn("Attempt to authenticate using federated principal mapped to a locally disabled account [${subject.id}]${subject.principal}")
        throw new DisabledAccountException("Attempt to authenticate using federated principal mapped to a locally disabled account [${subject.id}]${subject.principal}")
      }
      
      // All done the security context is successfully established
      def sessionRecord = new SessionRecord(credential:token.credential, remoteHost:token.remoteHost, userAgent:token.userAgent)
      subject.addToSessionRecords(sessionRecord)
      if(!subject.save()) {
        subject.errors.each { err ->
          log.error err
        }
        throw new RuntimeException("Account modification for ${token.principal} when adding new session record")
      }
      
      log.info "Successfully logged in subject [$subject.id]$subject.principal using federated source"
      def account = new SimpleAccount(subject.id, token.credential, "aaf.sp.groovy.shiro.FederatedToken")
      return account
    }
  }
}
