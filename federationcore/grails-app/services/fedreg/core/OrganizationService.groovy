package fedreg.core

import org.springframework.context.i18n.LocaleContextHolder as LCH
import org.springframework.transaction.interceptor.TransactionAspectSupport

import fedreg.workflow.ProcessPriority
import grails.plugins.nimble.core.UserBase

class OrganizationService {

	def workflowProcessService
	def entityDescriptorService
	
	def create(def params) {
		def organization = new Organization(approved:false, active:params.active, name:params.organization?.name, displayName:params.organization?.displayName, lang: params.organization?.lang, url: new UrlURI(uri:params.organization?.url), primary:OrganizationType.get(params.organization?.primary))
		
		def contact = Contact.get(params.contact?.id)
		if(!contact) {
			contact = MailURI.findByUri(params.contact?.email)?.contact		// We may already have them referenced by email address and user doesn't realize
			if(!contact)
				contact = new Contact(givenName: params.contact?.givenName, surname: params.contact?.surname, email: new MailURI(uri:params.contact?.email))
		}
		
		if(!organization.validate()) {
			log.info "$authenticatedUser attempted to create $organization but failed Organization validation"
			organization?.errors.each { log.error it }
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly() 
			return [ false, organization, contact ]
		}
		
		def savedOrg = organization.save()
		if(!savedOrg) {
			organization?.errors.each { log.error it }
			throw new ErronousStateException("Unable to save when creating ${organization}")
		}
		
		if(contact.organization == null)
			contact.organization = savedOrg
			
		if(!contact.validate()) {
			log.info "$authenticatedUser attempted to create $organization but failed Contact validation"
			contact?.errors.each { log.error it }
			savedOrg.discard()
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly() 
			return [ false, organization, contact ]
		}
		
		if(!contact.save()) {
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
		
		log.info "$authenticatedUser created $organization"
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
		organization.url.uri = params.organization.url
		organization.primary = OrganizationType.get(params.organization.primary)
		organization.types = []
		params.organization.types.each {
			if(it.value == 'on') {
				organization.addToTypes(OrganizationType.get(it.key))
			}
		}
		
		if(!organization.validate()) {
			log.info "$authenticatedUser attempted to update $organization but failed Organization validation"
			organization?.errors.each { log.error it }
			return [ false, organization ]
		}
		
		if(!organization.save()) {
			organization?.errors.each { log.error it }
			throw new ErronousStateException("Unable to save when updating ${organization}")
		}
		
		log.info "$authenticatedUser updated $organization"
		return [true, organization]
	}
	
	def delete(def id) {
		def org = Organization.get(id)
		if(!org)
			throw new ErronousStateException("Unable to find Organization with id $id")

		def entityDescriptors = EntityDescriptor.findAllWhere(organization:org)
		entityDescriptors.each { println "Removing $it"; entityDescriptorService.delete(it.id) }

		def contacts = Contact.findAllWhere(organization:org)
		contacts.each { contact ->
			// This gets around a stupid hibernate cascade bug primarily but is also useful as a second level check to prevent accidents
			if(contact.id == authenticatedUser.contact.id)
				throw new RuntimeException("Authenticated user is a contact for this organization. Users are unable to remove their own organization.")
			
			def contactPersons = ContactPerson.findAllWhere(contact:contact)
			contactPersons.each { cp -> cp.delete() }
			
			log.debug "Removing $org from $contact"
			contact.organization = null
			contact.save()
		}
		
		org.delete()
		log.info "$authenticatedUser deleted $org"
	}
}