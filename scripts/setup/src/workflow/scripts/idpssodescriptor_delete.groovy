import aaf.fr.foundation.*

entityDescriptorService = ctx.getBean("entityDescriptorService")
identityProviderService = ctx.getBean("identityProviderService")
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
    subject messageSource.getMessage("branding.fr.mail.workflow.idp.rejected.subject", args, "branding.fr.mail.workflow.idp.rejected.subject", new Locale(env.locale))
    body view:"/templates/mail/workflows/default/_rejected_idp", model:[identityProvider:idp, locale:env.locale]
  }
  
  log.warn "Deleting $idp. Workflow indicates it is invalid and no longer needed."
  
  entityDescriptorService.delete(idp.entityDescriptor.id)
  
  workflowTaskService.complete(env.taskInstanceID.toLong(), 'idpssodescriptordeleted')
}
else {
  throw new RuntimeException("Attempt to process delete in script idpssodescriptor_delete. Failed because referenced IDP does not exist")
}