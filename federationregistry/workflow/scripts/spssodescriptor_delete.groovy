import fedreg.core.*

workflowTaskService = ctx.getBean("workflowTaskService")
mailService = ctx.getBean("mailService")
messageSource = ctx.getBean("messageSource")

def sp = SPSSODescriptor.get(env.serviceProvider.toLong())
if(sp) {
	
	def creator = Contact.get(env.creator.toLong())
	def args = new Object[1]
	args[0] = sp.displayName
	mailService.sendMail {            
		to creator.email.uri
		from ctx.grailsApplication.config.nimble.messaging.mail.from
		subject messageSource.getMessage("fedreg.templates.mail.workflow.sp.rejected.subject", args, "fedreg.templates.mail.workflow.sp.rejected.subject", new Locale(env.locale))
		body view:"/templates/mail/workflows/default/_rejected_sp", model:[serviceProvider:sp, locale:env.locale]
	}
	
	log.warn "Deleting $sp. Workflow indicates it is invalid and no longer needed."
	
	def entityDescriptor = sp.entityDescriptor
	
	sp.delete()
	
	// Delete Entity? Determine if associated with any other child descriptors first
	if(entityDescriptor.idpDescriptors.size() == 0 && entityDescriptor.attributeAuthorityDescriptors.size() == 0 && entityDescriptor.pdpDescriptors.size() == 0 && entityDescriptor.additionalMetadataLocations.size() == 0) {
		// Ensure there are no other SP associated with this Entity
		if(entityDescriptor.spDescriptors.size() == 1) {
				entityDescriptor.delete()
		}
	}
	
	workflowTaskService.complete(env.taskInstanceID.toLong(), 'spssodescriptordeleted')
}
else {
	throw new RuntimeException("Attempt to process delete in script spssodescriptor_delete. Failed because referenced SP does not exist")
}