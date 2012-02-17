package aaf.fr.foundation

import org.springframework.context.i18n.LocaleContextHolder as LCH
import org.springframework.transaction.interceptor.TransactionAspectSupport

import aaf.fr.workflow.ProcessPriority


/**
 * Provides methods for managing Organization instances.
 *
 * @author Bradley Beddoes
 */
class OrganizationService {
  static transactional = true
  
  def workflowProcessService
  def entityDescriptorService
  
  def create(def params) {
    def organization = new Organization(approved:false, active:params.active, name:params.organization?.name, displayName:params.organization?.displayName, lang: params.organization?.lang, url: params.organization?.url, primary:OrganizationType.get(params.organization?.primary))
    
    // Contact
    def contact
    if(params.contact?.email)
      contact = Contact.findByEmail(params.contact?.email)    // We may already have them referenced by email

    if(!contact) {
      // Due to hibernate cascade issues we have to actually save here to ensure no Transient Exception
      contact = new Contact(givenName: params.contact?.givenName, surname: params.contact?.surname, email: params.contact?.email)
      contact.save(flush:true)
      if(contact.hasErrors()) {
        log.info "$subject attempted to create identityProvider but contact details supplied were invalid"
        contact.errors.each { log.debug it }
      }
    }
    def ct = params.contact?.type ?: 'administrative'
    
    if(!organization.validate() || contact.hasErrors()) {
      log.info "$subject attempted to create $organization but failed input validation"
      organization?.errors.each { log.error it }
      TransactionAspectSupport.currentTransactionStatus().setRollbackOnly() 
      return [ false, organization, contact ]
    }
    
    // Force flush as we need identifiers
    if(!organization.save(flush:true)) {
      organization?.errors.each { log.error it }
      throw new ErronousStateException("Unable to save when creating $organization")
    }
    
    if(contact.organization == null)
      contact.organization = organization
    
    // Force flush as we need identifiers
    if(!contact.save(flush:true)) {
      contact?.errors.each { log.error it }
      TransactionAspectSupport.currentTransactionStatus().setRollbackOnly() 
      throw new ErronousStateException("Unable to save when creating ${contact}")
    }
    
    def workflowParams = [ creator:contact?.id?.toString(), organization:organization?.id?.toString(), locale:LCH.getLocale().getLanguage() ]
    def (initiated, processInstance) = workflowProcessService.initiate( "organization_create", "Approval for creation of Organization ${organization.displayName}", ProcessPriority.MEDIUM, workflowParams)
    
    if(initiated)
      workflowProcessService.run(processInstance)
    else
      throw new ErronousStateException("Unable to execute workflow when creating ${organization}")
    
    log.info "$subject created $organization"
    return [ true, organization, contact ]
  }
  
  def update(def params) {
    def organization = Organization.get(params.id)
    if(!organization)
      return [false, null]
    
    organization.displayName = params.organization.displayName
    organization.name = params.organization.name
    organization.lang = params.organization.lang
    organization.active = params.organization.active == 'true'
    organization.url = params.organization.url
    organization.primary = OrganizationType.get(params.organization.primary)
    organization.types = []
    params.organization.types.each {
      if(it.value == 'on') {
        organization.addToTypes(OrganizationType.get(it.key))
      }
    }
    
    if(!organization.validate()) {
      log.info "$subject attempted to update $organization but failed Organization validation"
      organization?.errors.each { log.error it }
      return [ false, organization ]
    }
    
    if(!organization.save()) {
      organization?.errors.each { log.error it }
      throw new ErronousStateException("Unable to save when updating ${organization}")
    }
    
    log.info "$subject updated $organization"
    return [true, organization]
  }
  
  def delete(def id) {
    def org = Organization.get(id)
    if(!org)
      throw new ErronousStateException("Unable to find Organization with id $id")

    def entityDescriptors = EntityDescriptor.findAllWhere(organization:org)
    entityDescriptors.each { entityDescriptorService.delete(it.id) }

    def contacts = Contact.findAllWhere(organization:org)
    contacts.each { contact ->      
      def contactPersons = ContactPerson.findAllWhere(contact:contact)
      contactPersons.each { cp -> cp.delete() }
      
      log.debug "Removing $org from $contact"
      contact.organization = null
      contact.save()
    }
    
    org.delete()
    log.info "$subject deleted $org"
  }
  
  def archive(def id) {
    def org = Organization.get(id)
    if(!org)
      throw new ErronousStateException("Unable to find Organization with id $id")

    def entityDescriptors = EntityDescriptor.findAllWhere(organization:org)
    entityDescriptors?.each { entityDescriptorService.archive(it.id) }
    
    org.archived = true
    org.active = false
    if(!org.save()) {
      log.error "Unable to archive $org"
      org.errors.each { log.error it }
      throw new ErronousStateException("Unable to archive Organization with id $id")
    }
    log.info "$subject successfully archived $org"
  }
  
  def unarchive(def id) {
    def org = Organization.get(id)
    if(!org)
      throw new ErronousStateException("Unable to find Organization with id $id")
    
    org.archived = false
    if(!org.save()) {
      log.error "Unable to unarchive $org"
      org.errors.each { log.error it }
      throw new ErronousStateException("Unable to unarchive Organization with id $id")
    }
    log.info "$subject successfully unarchived $org"
  }
}