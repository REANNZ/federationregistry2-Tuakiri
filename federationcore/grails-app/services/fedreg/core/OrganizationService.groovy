package fedreg.core

import org.springframework.context.i18n.LocaleContextHolder as LCH
import org.springframework.transaction.interceptor.TransactionAspectSupport

import fedreg.workflow.ProcessPriority

class OrganizationService {

	def workflowProcessService
	
	def create(def params) {
		def organization = new Organization(approved:false, active:params.active, name:params.organization?.name, displayName:params.organization?.displayName, lang: params.organization?.lang, url: new UrlURI(uri:params.organization?.url), primary:OrganizationType.get(params.organization?.primary))
		
		def contact = Contact.get(params.contact?.id)
		if(!contact) {
			contact = MailURI.findByUri(params.contact?.email)?.contact		// We may already have them referenced by email address and user doesn't realize
			if(!contact)
				contact = new Contact(givenName: params.contact?.givenName, surname: params.contact?.surname, email: new MailURI(uri:params.contact?.email), organization:organization)
		}
		
		if(!organization.validate()) {
			organization?.errors.each { log.error it }
			TransactionAspectSupport.currentTransactionInfo().setRollbackOnly() 
			return [ false, organization, contact ]
		}
		
		if(!organization.save()) {
			organization?.errors.each { log.error it }
			throw new RuntimeException("Unable to save when creating ${organization}")
		}
		
		if(!contact.validate()) {
			contact?.errors.each { log.error it }
			TransactionAspectSupport.currentTransactionInfo().setRollbackOnly() 
			return [ false, organization, contact ]
		}
		
		if(!contact.save()) {
			contact?.errors.each { log.error it }
			TransactionAspectSupport.currentTransactionInfo().setRollbackOnly() 
			throw new RuntimeException("Unable to save when creating ${contact}")
		}
		
		def workflowParams = [ creator:contact?.id?.toString(), organization:organization?.id?.toString(), locale:LCH.getLocale().getLanguage() ]
		def (initiated, processInstance) = workflowProcessService.initiate( "organization_create", "Approval for creation of Organization ${organization.displayName}", ProcessPriority.MEDIUM, workflowParams)
		
		if(initiated)
			workflowProcessService.run(processInstance)
		else
			throw new RuntimeException("Unable to execute workflow when creating ${organization}")
		
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
			organization?.errors.each { log.error it }
			return [ false, organization ]
		}
		
		if(!organization.save()) {
			organization?.errors.each { log.error it }
			throw new RuntimeException("Unable to save when updating ${organization}")
		}
		
		return [true, organization]
	}
}