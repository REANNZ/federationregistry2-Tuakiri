
import grails.plugins.nimble.core.*
import fedreg.core.*


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
	
	def role = Role.findWhere(name:"descriptor-${idp.id}-administrators")
	if(!role){	// Expected state
		role = roleService.createRole("descriptor-${idp.id}-administrators", "Global administrators for $idp", false)
	}
	
	def permission = new LevelPermission()
	permission.populate("descriptor", "${idp.id}", "*", null, null, null)
	permission.managed = false
	permissionService.createPermission(permission, role)
	
	def invitation = invitationService.create(null, role.id, null, "IDPSSODescriptor", "show", idp.id.toString())
	
	def creator = Contact.get(env.creator.toLong())
	mailService.sendMail {            
		to creator.email.uri
		from ctx.grailsApplication.config.nimble.messaging.mail.from
		subject messageSource.getMessage("fedreg.templates.mail.workflow.idp.activated.subject", null, "fedreg.templates.mail.workflow.idp.activated.subject", new Locale(env.locale))
		body view:"/templates/mail/workflows/default/_activated_idp", model:[identityProvider:idp, locale:env.locale, invitation:invitation]
	}

	workflowTaskService.complete(env.taskInstanceID.toLong(), 'idpssodescriptoractivated')
}
else {
	throw new RuntimeException("Attempt to process activate in script idpssodescriptor_activate. Failed because referenced IDP does not exist")
}