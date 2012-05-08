import grails.plugins.federatedgrails.*
import aaf.fr.foundation.*


workflowTaskService = ctx.getBean("workflowTaskService")
mailService = ctx.getBean("mailService")
invitationService = ctx.getBean("invitationService")
roleService = ctx.getBean("roleService")
permissionService = ctx.getBean("permissionService")
messageSource = ctx.getBean("messageSource")

def org = Organization.get(env.organization.toLong())

if(org) {

  log.info "Activating $org. Workflow indicates it is valid and accepted for operation."
  
  org.approved = true
  org.active = true
  org.save()
  
  if(org.hasErrors()) {
    throw new RuntimeException("Attempt to process activate in script organization_activate. Failed due to ${org} fault on save")
  }
  
  def role = Role.findWhere(name:"organization-${org.id}-administrators")
  if(!role){  // Expected state
    role = roleService.createRole("organization-${org.id}-administrators", "Global administrators for $org", false)
  }
  
  Permission permission = new Permission(target:"organization:${org.id}:*")
  permission.managed = false
  permission.type = Permission.defaultPerm
  permissionService.createPermission(permission, role)
  
  def invitation = invitationService.create(role.id, "Organization", "show", org.id.toString())
  
  def creator = Contact.get(env.creator.toLong())
  mailService.sendMail {            
    to creator.email
    subject messageSource.getMessage("branding.fr.mail.workflow.org.activated.subject", null, "branding.fr.mail.workflow.org.activated.subject", new Locale(env.locale))
    body view:"/templates/mail/workflows/default/_activated_organization", model:[organization:org, locale:env.locale, invitation:invitation]
  }

  workflowTaskService.complete(env.taskInstanceID.toLong(), 'organizationactivated')
}
else {
  throw new RuntimeException("Attempt to process activate in script organization_activate. Failed because referenced organization does not exist")
}