import aaf.fr.identity.AdminsService

import aaf.fr.identity.Subject
import grails.plugins.federatedgrails.Role
import grails.plugins.federatedgrails.Permission

class BootStrap {

  def roleService
  def permissionService

  def init = { servletContext ->
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
  }

  def destroy = {
  }
} 