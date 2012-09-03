

import org.apache.shiro.authz.permission.WildcardPermission

import grails.plugins.federatedgrails.SubjectBase

class DatabaseRealm {

    def credentialMatcher
    def shiroPermissionResolver

    def hasRole(principal, roleName) {
        def roles = SubjectBase.withCriteria {
            roles {
                eq("name", roleName)
            }
            eq("id", principal)
        }

        return roles.size() > 0
    }

    def hasAllRoles(principal, roleNames) {
        def r = SubjectBase.withCriteria {
            roles {
                'in'("name", roleNames)
            }
            eq("id", principal)
        }

        return r.size() == roleNames.size()
    }

    def isPermitted(principal, requiredPermission) {      
      // Required permission directly applied to the subject
      def subject = SubjectBase.get(principal)
      def permissions = subject?.permissions

      def permitted = permissions?.find { ps ->
        def perm = shiroPermissionResolver.resolvePermission(ps.target)

        if (perm.implies(requiredPermission)) {
            return true
        }
        else {
            return false
        }
      }

      if (permitted != null) { return true }

      // Required permission applied to a role of which the subject has membership
      def results = SubjectBase.executeQuery("select distinct p from SubjectBase as subject join subject.roles as role join role.permissions as p where subject.id = '$principal'")

      permitted = results.find { ps ->
        def perm = shiroPermissionResolver.resolvePermission(ps.target)

        if (perm.implies(requiredPermission)) {
          return true
        }
        else {
          return false
        }
      }

      if (permitted != null) { return true }
      else { return false }
    }
}
