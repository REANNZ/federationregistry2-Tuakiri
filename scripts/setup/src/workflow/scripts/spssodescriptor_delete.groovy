import aaf.fr.foundation.*

entityDescriptorService = ctx.getBean("entityDescriptorService")
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
    subject messageSource.getMessage("branding.fr.mail.workflow.sp.rejected.subject", args, "branding.fr.mail.workflow.sp.rejected.subject", new Locale(env.locale))
    body view:"/templates/mail/workflows/default/_rejected_sp", model:[serviceProvider:sp, locale:env.locale]
  }
  
  log.warn "Deleting $sp. Workflow indicates it is invalid and no longer needed."
  
  def entityDescriptor = sp.entityDescriptor
  
  entityDescriptorService.delete(entityDescriptor.id)
  
  workflowTaskService.complete(env.taskInstanceID.toLong(), 'spssodescriptordeleted')
}
else {
  throw new RuntimeException("Attempt to process delete in script spssodescriptor_delete. Failed because referenced SP does not exist")
}