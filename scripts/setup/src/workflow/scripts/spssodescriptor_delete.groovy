import grails.plugins.federatedgrails.*
import aaf.fr.identity.*
import aaf.fr.foundation.*

entityDescriptorService = ctx.getBean("entityDescriptorService")
spSSODescriptorService = ctx.getBean("SPSSODescriptorService")
workflowTaskService = ctx.getBean("workflowTaskService")
mailService = ctx.getBean("mailService")
messageSource = ctx.getBean("messageSource")

def sp = SPSSODescriptor.read(env.serviceProvider.toLong())
if(sp) {
	
	def creator = Contact.read(env.creator.toLong())
	def args = new Object[1]
	args[0] = sp.displayName
	mailService.sendMail {            
		to creator.email
		from ctx.grailsApplication.config.nimble.messaging.mail.from
		subject messageSource.getMessage("fedreg.templates.mail.workflow.sp.rejected.subject", args, "fedreg.templates.mail.workflow.sp.rejected.subject", new Locale(env.locale))
		body view:"/templates/mail/workflows/default/_rejected_sp", model:[serviceProvider:sp, locale:env.locale]
	}
	
	log.warn "Deleting $sp. Workflow indicates it is invalid and no longer needed."
	
	def entityDescriptor = sp.entityDescriptor
	
	if(entityDescriptor.holdsSPOnly())
		entityDescriptorService.delete(entityDescriptor.id)
	else
		spSSODescriptorService.delete(sp.id)
	
	workflowTaskService.complete(env.taskInstanceID.toLong(), 'spssodescriptordeleted')
}
else {
	throw new RuntimeException("Attempt to process delete in script spssodescriptor_delete. Failed because referenced SP does not exist")
}