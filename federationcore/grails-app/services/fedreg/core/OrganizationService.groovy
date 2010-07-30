package fedreg.core

import org.springframework.transaction.interceptor.TransactionAspectSupport

import fedreg.workflow.ProcessPriority

class OrganizationService {

	def workflowProcessService
	
	def create(def params) {
		def organization = new Organization(active:params.active, name:params.organization?.name, displayName:params.organization?.displayName, lang: params.organization?.lang, url: new UrlURI(uri:params.organization?.url), primary:OrganizationType.get(params.organization?.primary))
		
		def contact = Contact.get(params.contact?.id)
		if(!contact) {
			contact = MailURI.findByUri(params.contact?.email)?.contact		// We may already have them referenced by email address and user doesn't realize
			if(!contact)
				contact = new Contact(givenName: params.contact?.givenName, surname: params.contact?.surname, email: new MailURI(uri:params.contact?.email), organization:organization)
		}
		
		if(!organization.save()) {
			organization?.errors.each { log.error it }
			return [ false, organization, contact ]
		}
		
		if(!contact.save()) {
			contact?.errors.each { log.error it }
			TransactionAspectSupport.currentTransactionInfo().setRollbackOnly(true) 
			return [ false, organization, contact ]
		}
		
		def workflowParams = [ creator:contact?.id, organization:organization?.id ]
		def processInstance = workflowProcessService.initiate( "organization_create", "Approval for creation of Organization ${organization.displayName}", ProcessPriority.MEDIUM, workflowParams)
		workflowProcessService.run(processInstance)
		
		return [ true, organization, contact ]
	}
}