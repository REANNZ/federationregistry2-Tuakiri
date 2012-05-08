import aaf.fr.foundation.*

workflowTaskService = ctx.getBean("workflowTaskService")
mailService = ctx.getBean("mailService")
messageSource = ctx.getBean("messageSource")

def sp = SPSSODescriptor.get(env.serviceProvider.toLong())

if(sp) {  
  def creator = Contact.get(env.creator.toLong())
  mailService.sendMail {
    to creator.email
    subject messageSource.getMessage("branding.fr.mail.workflow.sp.registered.subject", null, "branding.fr.mail.workflow.sp.registered.subject", new Locale(env.locale))
    body view:"/templates/mail/workflows/default/_registered_sp", model:[serviceProvider:sp, locale:env.locale]
  }

  workflowTaskService.complete(env.taskInstanceID.toLong(), 'confirmedspssodescriptor')
}
else {
  throw new RuntimeException("Attempt to email confirmation in script spssodescriptor_confirm. Failed because referenced SP does not exist")
}