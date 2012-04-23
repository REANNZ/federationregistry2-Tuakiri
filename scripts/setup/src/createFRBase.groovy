// FR Base (createFRBase.groovy)
roleService = ctx.getBean("roleService")
permissionService = ctx.getBean("permissionService")

// Create federation-administrators role, used in workflows etc
def adminRole = Role.findWhere(name:"federation-administrators")
if(!adminRole) {
	adminRole = roleService.createRole("federation-administrators", "Role representing federation level administrators who can make decisions onbehalf of the entire federation, granted global FR access", false)

  // Grant administrative 'ALL' permission
  Permission adminPermission = new Permission(target:'*')
  adminPermission.managed = true
  adminPermission.type = Permission.adminPerm

  permissionService.createPermission(adminPermission, adminRole)
}

// Create federation-reporting role, used to grant full reporting access to non fr wide admins
def reportingRole = Role.findWhere(name:"federation-reporting")
if(!reportingRole) {
  reportingRole = roleService.createRole("federation-reporting", "Access to federation wide reports for executive, management etc.", false)

  // Grant global reports access permission
  Permission adminPermission = new Permission(target:'federation:management:reporting')
  adminPermission.managed = true
  adminPermission.type = Permission.adminPerm

  permissionService.createPermission(adminPermission, reportingRole)
}

// Create contactmanagement-reporting role, used to grant full contact management access to non fr wide admins
def contactManagementRole = Role.findWhere(name:"federation-contactmanagement")
if(!contactManagementRole) {
  contactManagementRole = roleService.createRole("federation-contactmanagement", "Access to manage contacts for all IdP and SP", false)

  // Grant global reports access permission
  Permission adminPermission = new Permission(target:'federation:management:contacts')
  adminPermission.managed = true
  adminPermission.type = Permission.adminPerm

  permissionService.createPermission(adminPermission, contactManagementRole)
}

// Populate default administrative account if required
if(aaf.fr.identity.Subject.count() == 0) {
  def subject = new aaf.fr.identity.Subject(principal:'internaladministrator', cn:'internal administrator', email:'internaladministrator@not.valid')
  subject.save(flush: true)
  if(subject.hasErrors()) {
    subject.errors.each { println it }
  }
}