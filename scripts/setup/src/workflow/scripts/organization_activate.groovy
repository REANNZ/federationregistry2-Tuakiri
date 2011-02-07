
import grails.plugins.nimble.core.*
import fedreg.core.*


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
	if(!role){	// Expected state
		role = roleService.createRole("organization-${org.id}-administrators", "Global administrators for $org", false)
	}
	
	def permission = new LevelPermission()
	permission.populate("organization", "${org.id}", "*", null, null, null)
	permission.managed = false
	permissionService.createPermission(permission, role)
	
	def invitation = invitationService.create(null, role.id, null, "organization", "show", org.id.toString())
	
	def creator = Contact.get(env.creator.toLong())
	mailService.sendMail {            
		to creator.email.uri
		from ctx.grailsApplication.config.nimble.messaging.mail.from
		subject messageSource.getMessage("fedreg.templates.mail.workflow.org.activated.subject", null, "fedreg.templates.mail.workflow.org.activated.subject", new Locale(env.locale))
		body view:"/templates/mail/workflows/default/_activated_organization", model:[organization:org, locale:env.locale, invitation:invitation]
	}

	workflowTaskService.complete(env.taskInstanceID.toLong(), 'organizationactivated')
}
else {
	throw new RuntimeException("Attempt to process activate in script organization_activate. Failed because referenced organization does not exist")
}