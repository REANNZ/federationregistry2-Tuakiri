import grails.plugins.federatedgrails.*
import aaf.fr.foundation.*


workflowTaskService = ctx.getBean("workflowTaskService")
mailService = ctx.getBean("mailService")
invitationService = ctx.getBean("invitationService")
roleService = ctx.getBean("roleService")
permissionService = ctx.getBean("permissionService")
messageSource = ctx.getBean("messageSource")

def idp = IDPSSODescriptor.get(env.identityProvider.toLong())

if(idp) {

  log.info "Activating $idp. Workflow indicates it is valid and accepted for operation."
  
  idp.approved = true
  idp.active = true
  idp.save()
  
  if(idp.hasErrors()) {
    throw new RuntimeException("Attempt to process activate in script idpssodescriptor_activate. Failed due to IDP fault on save")
  }
    
  if(idp.collaborator) {
    idp.collaborator.active = true
    idp.collaborator.approved = true
    idp.collaborator.save()
    
    if(idp.collaborator.hasErrors()) {
      throw new RuntimeException("Attempt to process activate in script idpssodescriptor_activate. Failed due to IDP collaborator fault on save")
    }
  }

  if(idp.entityDescriptor.approved == false || idp.entityDescriptor.active == false) {
    idp.entityDescriptor.approved = true
    idp.entityDescriptor.active = true
    idp.entityDescriptor.save()
    if(idp.entityDescriptor.hasErrors()) {
      throw new RuntimeException("Attempt to process activate in script idpssodescriptor_activate. Failed due to IDP entityDescriptor fault on save")
    }
  }
  
  // Create ED access control role
  def edRole = Role.findWhere(name:"descriptor-${idp.entityDescriptor.id}-administrators")
  if(!edRole){  // Generally expected state
    edRole = roleService.createRole("descriptor-${idp.entityDescriptor.id}-administrators", "Global administrators for ${idp.entityDescriptor}", false)
  
    Permission permission = new Permission(target:"federation:management:descriptor:${idp.entityDescriptor.id}:*")
    permission.managed = false
    permission.type = Permission.defaultPerm
    permissionService.createPermission(permission, edRole)
  }
  
  // Create IDP access control role
  def role = Role.findWhere(name:"descriptor-${idp.id}-administrators")
  if(!role){  // Expected state
    role = roleService.createRole("descriptor-${idp.id}-administrators", "Global administrators for $idp", false)
  }
  
  // In our model the IDP role has permissions to edit the IDP and the AA
  // Manage IDP
  Permission permission = new Permission(target:"federation:management:descriptor:${idp.id}:*")
  permission.managed = false
  permission.type = Permission.defaultPerm
  permissionService.createPermission(permission, role)
  
  // Manage collaborating AA
  def aaPermission = new Permission(target:"federation:management:descriptor:${idp.collaborator.id}:*")       
  aaPermission.managed = false
  aaPermission.type = Permission.defaultPerm
  permissionService.createPermission(aaPermission, role)
  
  def invitation = invitationService.create(role.id, "IDPSSODescriptor", "show", idp.id.toString())
  
  def creator = Contact.get(env.creator.toLong())
  mailService.sendMail {            
    to creator.email
    subject messageSource.getMessage("branding.fr.mail.workflow.idp.activated.subject", null, "branding.fr.mail.workflow.idp.activated.subject", new Locale(env.locale))
    body view:"/templates/mail/workflows/default/_activated_idp", model:[identityProvider:idp, locale:env.locale, invitation:invitation]
  }

  workflowTaskService.complete(env.taskInstanceID.toLong(), 'idpssodescriptoractivated')
}
else {
  throw new RuntimeException("Attempt to process activate in script idpssodescriptor_activate. Failed because referenced IDP does not exist")
}