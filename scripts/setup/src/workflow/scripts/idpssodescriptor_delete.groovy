import grails.plugins.federatedgrails.*
import aaf.fr.identity.*
import aaf.fr.foundation.*

entityDescriptorService = ctx.getBean("entityDescriptorService")
idpSSODescriptorService = ctx.getBean("IDPSSODescriptorService")
workflowTaskService = ctx.getBean("workflowTaskService")
mailService = ctx.getBean("mailService")
messageSource = ctx.getBean("messageSource")

def idp = IDPSSODescriptor.read(env.identityProvider.toLong())
if(idp) {
	
	def creator = Contact.read(env.creator.toLong())
	def args = new Object[1]
	args[0] = idp.displayName
	mailService.sendMail {            
		to creator.email
		from ctx.grailsApplication.config.nimble.messaging.mail.from
		subject messageSource.getMessage("fedreg.templates.mail.workflow.idp.rejected.subject", args, "fedreg.templates.mail.workflow.idp.rejected.subject", new Locale(env.locale))
		body view:"/templates/mail/workflows/default/_rejected_idp", model:[identityProvider:idp, locale:env.locale]
	}
	
	log.warn "Deleting $idp. Workflow indicates it is invalid and no longer needed."
	
	def entityDescriptor = idp.entityDescriptor
	if(entityDescriptor.holdsIDPOnly())
		entityDescriptorService.delete(entityDescriptor.id)
	else
		idpSSODescriptorService.delete(idp.id)
	
	workflowTaskService.complete(env.taskInstanceID.toLong(), 'idpssodescriptordeleted')
}
else {
	throw new RuntimeException("Attempt to process delete in script idpssodescriptor_delete. Failed because referenced IDP does not exist")
}