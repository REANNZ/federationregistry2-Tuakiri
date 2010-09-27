import fedreg.core.*

workflowTaskService = ctx.getBean("workflowTaskService")
mailService = ctx.getBean("mailService")
messageSource = ctx.getBean("messageSource")

def idp = IDPSSODescriptor.get(env.identityProvider.toLong())
if(idp) {
	
	def creator = Contact.get(env.creator.toLong())
	def args = new Object[1]
	args[0] = idp.displayName
	mailService.sendMail {            
		to creator.email.uri
		from ctx.grailsApplication.config.nimble.messaging.mail.from
		subject messageSource.getMessage("fedreg.templates.mail.workflow.idp.rejected.subject", args, "fedreg.templates.mail.workflow.idp.rejected.subject", new Locale(env.locale))
		body view:"/templates/mail/workflows/default/_rejected_idp", model:[identityProvider:idp, locale:env.locale]
	}
	
	log.warn "Deleting $idp. Workflow indicates it is invalid and no longer needed."
	
	def attributeAuthority = idp.collaborator
	def entityDescriptor = idp.entityDescriptor
	
	idp.delete()
	attributeAuthority.delete()
	
	// Delete Entity? Determine if associated with any other child descriptors first
	if(entityDescriptor.spDescriptors.size() == 0 && entityDescriptor.pdpDescriptors.size() == 0 && entityDescriptor.additionalMetadataLocations.size() == 0) {
		// Ensure there are no other IDP associated with this Entity
		if(entityDescriptor.idpDescriptors.size() == 1) {
			// Ensure only our collaborating AA is tied to the Entity
			if(idp.collaborator && entityDescriptor.attributeAuthorityDescriptors.size() == 1) {
				entityDescriptor.delete()
			}
		}
	}
}
else {
	throw new RuntimeException("Attempt to process delete in script idpssodescriptor_delete. Failed because referenced IDP does not exist")
}