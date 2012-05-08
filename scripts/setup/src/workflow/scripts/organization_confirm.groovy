import aaf.fr.foundation.*

workflowTaskService = ctx.getBean("workflowTaskService")
mailService = ctx.getBean("mailService")
messageSource = ctx.getBean("messageSource")

def org = Organization.get(env.organization.toLong())

if(org) {  
  def creator = Contact.get(env.creator.toLong())
  mailService.sendMail {
    to creator.email
    subject messageSource.getMessage("branding.fr.mail.workflow.org.registered.subject", null, "branding.fr.mail.workflow.org.registered.subject", new Locale(env.locale))
    body view:"/templates/mail/workflows/default/_registered_organization", model:[organization:org, locale:env.locale]
  }

  workflowTaskService.complete(env.taskInstanceID.toLong(), 'confirmedorganization')
}
else {
  throw new RuntimeException("Attempt to email confirmation in script organization_confirm. Failed because referenced organization does not exist")
}