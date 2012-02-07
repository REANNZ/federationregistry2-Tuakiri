// FR Base (createFRBase.groovy)
roleService = ctx.getBean("roleService")
permissionService = ctx.getBean("permissionService")

// Create federation-administrators role, used in workflows etc
def fedAdminRole = Role.findWhere(name:"federation-administrators")
if(!fedAdminRole)
	roleService.createRole("federation-administrators", "Role representing federation level administrators who can make decisions onbehalf of the entire federation, particuarly in workflows", false)

// Create federation-reporting role, used to grant full reporting access to non fr wide admins
def fedReportingRole = Role.findWhere(name:"federation-reporting")
if(!fedReportingRole)
  roleService.createRole("federation-reporting", "Access to federation wide reports for executive, management etc.", false)

// Populate default administrative account if required
if(aaf.fr.identity.Subject.count() == 0) {
	def subject = new aaf.fr.identity.Subject(principal:'internaladministrator', displayName:'internal administrator', email:'internaladministrator@not.valid')
	subject.save(flush: true)
}

def adminRole = Role.findByName(AdminsService.ADMIN_ROLE)
  if (!adminRole) {
    adminRole = new Role()
    adminRole.description = 'Assigned to users who are considered to be system wide administrator'
    adminRole.name = AdminsService.ADMIN_ROLE
    adminRole.protect = true
    def savedAdminRole = adminRole.save()

  if (adminRole.hasErrors()) {
    adminRole.errors.each {
      log.error(it)
    }
    throw new RuntimeException("Unable to create valid administrative role")
  }

  // Grant administrative 'ALL' permission
  Permission adminPermission = new Permission(target:'*')
  adminPermission.managed = true
  adminPermission.type = Permission.adminPerm

  permissionService.createPermission(adminPermission, savedAdminRole)
}