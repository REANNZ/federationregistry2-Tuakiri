import grails.plugins.federatedgrails.*
import aaf.fr.identity.*
import aaf.fr.foundation.*

organizationService = ctx.getBean("organizationService")
workflowTaskService = ctx.getBean("workflowTaskService")
mailService = ctx.getBean("mailService")
messageSource = ctx.getBean("messageSource")

def org = Organization.read(env.organization.toLong())
if(org) {
	def creator = Contact.read(env.creator.toLong())
	def args = new Object[1]
	args[0] = org.displayName
	mailService.sendMail {            
		to creator.email
		from ctx.grailsApplication.config.nimble.messaging.mail.from
		subject messageSource.getMessage("fedreg.templates.mail.workflow.org.rejected.subject", args, "fedreg.templates.mail.workflow.org.rejected.subject", new Locale(env.locale))
		body view:"/templates/mail/workflows/default/_rejected_organization", model:[organization:org, locale:env.locale]
	}
	
	log.warn "Deleting $org. Workflow indicates it is invalid and no longer needed."
	
	organizationService.delete(org.id)
	workflowTaskService.complete(env.taskInstanceID.toLong(), 'organizationdeleted')
}
else {
	throw new RuntimeException("Attempt to process delete in script organization_delete. Failed because referenced organization does not exist")
}
