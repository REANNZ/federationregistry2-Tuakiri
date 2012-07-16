package aaf.fr.admin

import org.springframework.dao.DataIntegrityViolationException
import grails.plugins.federatedgrails.*
import aaf.fr.identity.*

class RoleController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST", createpermission: "POST", deletepermission:"POST", addmember:"POST", removemember:"POST"]

    def roleService
    def permissionService

    def index() {
        redirect(action: "list", params: params)
    }

    def list() {
        [ roles: Role.list(),  roleTotal: Role.count()]
    }

    def create() {
        [ role: new Role(params)]
    }

    def save() {
      def role = roleService.createRole(params.name, params.description, params.protect == 'on')
      redirect(action: "show", id:  role.id)
    }

    def show() {
      def role = Role.get(params.id)
      if (!role) {
        log.warn "No role for $params.id located when attempting to show"
        redirect(action: "list")
        return
      }

      def subjects = aaf.fr.identity.Subject.list()
      subjects.removeAll(role.subjects)
      [role:role, subjects: subjects]
    }

    def update() {
      def role = Role.get(params.id)
      if (!role) {
        log.warn "No role for $params.id located when attempting to update"
        redirect(action: "list")
        return
      }

      if (params.version) {
        def version = params.version.toLong()
        if (role.version > version) {
            role.errors.rejectValue("version", "default.optimistic.locking.failure",
                [message(code: 'role.label', default: 'Role')] as Object[],
                "Another user has updated this Role while you were editing")
          render(view: "edit", model: [ role:  role])
          return
        }
      }

      role.name = params.name
      role.description = params.description
      role.protect = params.protect == 'on'

      roleService.updateRole(role)
      redirect(action: "show", id:  role.id)
    }

    def delete() {
      def role = Role.get(params.id)
      if (!role) {
        log.warn "No role for $params.id located when attempting to delete"
        redirect(action: "list")
        return
      }

      roleService.deleteRole(role)
      redirect(action: "list")
    }

    def searchNewMembers() {
      def role = Role.get(params.id)
      if (!role) {
        log.warn "No role for $params.id located when attempting to addmember"
        response.sendError(500)
        return
      }

      def subjects = aaf.fr.identity.Subject.list()

      render template: "/templates/role/searchnewmembers", plugin: 'administration', model:[subjects:subjects, role:role]
    }

    def addmember() {
      def role = Role.get(params.id)
      if (!role) {
        log.warn "No role for $params.id located when attempting to addmember"
        redirect(action: "list")
        return
      }

      def subject = Subject.get(params.subjectID)
      if (!subject) {
        log.warn "No subject for $params.subjectID located when attempting to addmember"
        redirect(action: "list")
        return
      }

      roleService.addMember(subject, role)
      redirect(action: "show", id:  role.id, fragment:"tab-members")
    }

    def removemember() {
      def role = Role.get(params.id)
      if (!role) {
        log.warn "No role for $params.id located when attempting to removemember"
        redirect(action: "list")
        return
      }

      def subject = Subject.get(params.subjectID)
      if (!subject) {
        log.warn "No subject for $params.subjectID located when attempting to removemember"
        redirect(action: "list")
        return
      }

      roleService.deleteMember(subject, role)
      redirect(action: "show", id:  role.id, fragment:"tab-members")
    }

    def createpermission() {
      def role = Role.get(params.id)
      if (!role) {
        log.warn "No role for $params.id located when attempting to createpermission"
        redirect(action: "list")
        return
      }

      def permission = new Permission()
      permission.type = Permission.defaultPerm
      permission.target = params.target
      permission.owner = role
      
      permissionService.createPermission(permission, role) 
      redirect(action: "show", id:  role.id, fragment:"tab-permissions")
    }

    def deletepermission() {
      def role = Role.get(params.id)
      if (!role) {
        log.warn "No role for $params.id located when attempting to createpermission"
        redirect(action: "list")
        return
      }

      def permission = Permission.get(params.permID)
      if (!permission) {
        log.warn "No permission for $params.permID located when attempting to deletepermission"
        redirect(action: "list")
        return
      }

      permissionService.deletePermission(permission)
      redirect(action: "show", id:  role.id, fragment:"tab-permissions")
    }
}
