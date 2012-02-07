package aaf.fr.identity

import grails.plugins.federatedgrails.Role
import grails.plugins.federatedgrails.LevelPermission

/**
 * Manage roles including addition/removal of subjects and permissions
 *
 * @author Bradley Beddoes
 */
class RoleController {

  def permissionService
  def roleService

  static Map allowedMethods = [validname: 'POST', save: 'POST', update: 'POST', delete: 'POST', addmember: 'POST', removemember: 'POST', searchnewmembers: 'POST', createpermission: 'POST', removepermission: 'POST']

  def index = {
    redirect action: list
  }

  def list = {
    return [roles: Role.list(params)]
  }

  def show = {
    def role = Role.get(params.id)
    if (!role) {
      log.warn("Role identified by id '$params.id' was not located")
      flash.type = "error"
      flash.message = message(code: 'nimble.role.nonexistant', args: [params.id])
      redirect action: list
    }
	else
    	[role: role]
  }

  def create = {
    def role = new Role()
    [role: role]
  }

  def save = {
    def name = params.name
    def description = params.description

    def createdRole = roleService.createRole(name, description, false)
    if (createdRole.hasErrors()) {
      log.error("Unable to create new role $name with description $description")
		flash.type = "error"
		flash.message = message(code: 'nimble.role.create.error', args: [createdRole.name])
      	render view: 'create', model: [role: createdRole]
    }
    else {
      log.info("Created new role $name with description $description")
      flash.type = "success"
      flash.message = message(code: 'nimble.role.create.success', args: [createdRole.name])
      redirect action: show, params: [id: createdRole.id]
    }
  }

  def edit = {
    def role = Role.get(params.id)
    if (!role) {
      log.warn("Role identified by id '$params.id' was not located")
      flash.type = "error"
      flash.message = message(code: 'nimble.role.nonexistant', args: [params.id])
      redirect action: list
    }
	else {
    	log.debug("Editing role $role.name")
	    [role: role]
	}
  }

  def update = {
    def role = Role.get(params.id)
    if (!role) {
      log.warn("Role identified by id '$params.id' was not located")
      flash.type = "error"
      flash.message = message(code: 'nimble.role.nonexistant', args: [params.id])
      redirect action: list
    }
	else {
    	if (role.protect) {
	      log.warn("Role [$role.id]$role.name is protected and can't be updated via the web interface")
	      flash.type = "error"
	      flash.message = message(code: 'nimble.role.protected.no.modification', args: [params.id])
	      redirect (action: show, id: role.id)
	    }
		else {
	    	role.properties['name', 'description'] = params
		    if (!role.validate()) {			
				log.warn("Attempt to update role [$role.id]$role.name failed")
				flash.type = "error"
			    flash.message = message(code: 'nimble.role.update.error', args: [role.name])
				render view: 'edit', model: [role: role]
			}
			else {
		      	def updatedRole = roleService.updateRole(role)
				if (updatedRole.hasErrors()) {
			       log.warn("Attempt to update role [$role.id]$role.name failed")
				   flash.type = "error"
			       flash.message = message(code: 'nimble.role.update.error', args: [role.name])
			       render view: 'edit', model: [role: updatedRole]
				}
				else {
		        	log.info("Updated role $updatedRole.name with description $updatedRole.description")
		        	flash.type = "success"
		        	flash.message = message(code: 'nimble.role.update.success', args: [role.name])
		        	redirect action: show, params: [id: role.id]
		      	}
			}
		}    
	}
  }

  def delete = {
    def role = Role.get(params.id)
    if (!role) {
      log.warn("Role identified by id '$params.id' was not located")
      flash.type = "error"
      flash.message = message(code: 'nimble.role.nonexistant', args: [params.id])
      redirect action: list
    }
	else {
    	if (role.protect) {
	      log.warn("Role [$role.id]$role.name is protected and can't be updated via the web interface")
	      flash.type = "error"
	      flash.message = message(code: 'nimble.role.protected.no.modification', args: [params.id])
	      redirect (action: show, id: role.id)
	    }
		else {
	    	roleService.deleteRole(role)
		    log.info("Deleted role $role.name with description $role.description")
		    flash.type = "success"
		    flash.message = message(code: 'nimble.role.delete.success', args: [role.name])
		    redirect action: list
    	}
	}
  }

  // AJAX related actions
  def validname = {
	if (params?.val == null || params.val.length() < 4) {
 		render message(code: 'nimble.role.name.invalid', args: [params.val])
		response.status = 500
	}
	else {
		def roles = Role.findAllByName(params?.val)
		if (roles != null && roles.size() > 0) {
			render message(code: 'nimble.role.name.invalid', args: [params.val])
			response.status = 500
		}
		else
			render message(code: 'nimble.role.name.valid', args: [params.val])
	}
  }

  def listmembers = {
    def role = Role.get(params.id)
    if (!role) {
      	log.warn("Role identified by id '$params.id' was not located")
	    render message(code: 'nimble.role.nonexistant', args: [params.id])
	    response.status = 500
    }
	else {
    	log.debug("Listing role members")
	    render(template: '/templates/admin/members_list', contextPath: pluginContextPath, model: [parent:role, subjects: role.subjects, groups: role.groups, protect: role.protect, groupmembers: true])
	}
  }

  def addmember = {
    def role = Role.get(params.id)
    if (!role) {
      log.warn("Role identified by id '$params.id' was not located")
      render message(code: 'nimble.role.nonexistant', args: [params.roleID])
	  response.status = 500
    }
	else {
		if (role.protect) {
	      log.warn("Role [$role.id]$role.name is protected and can't be updated via the web interface")
	      render message(code: 'nimble.role.protected.no.modification', args: [params.id])
	      response.status = 500
	    }
		else {
			def subject = Subject.get(params.subjectID)
	    	if (!subject) {
		      log.warn("Subject identified by id '$params.subjectID' was not located")
		      render message(code: 'nimble.subject.nonexistant', args: [params.subjectID])
			  response.status = 500
		    }
			else {
		    	roleService.addMember(subject, role)
			    log.info("Added subject [$subject.id]$subject.subjectname to role [$role.id]$role.name")
			    render message(code: 'nimble.role.addmember.success', args: [role.name, subject.subjectname])
			}
		}
    }
  }

  def removemember = {
    def role = Role.get(params.id)
    if (!role) {
      	log.warn("Role identified by id '$params.id' was not located")
	  	render message(code: 'nimble.role.nonexistant', args: [params.id])
      	response.status = 500 
    }
	else {
		if (role.protect) {
	      log.warn("Role [$role.id]$role.name is protected and can't be updated via the web interface")
	      render message(code: 'nimble.role.protected.no.modification', args: [params.id])
	      response.status = 500
	    }
		else {
			def subject = Subject.get(params.subjectID)
	    	if (!subject) {
		      	log.warn("subject identified by id '$params.subjectID' was not located")
				render message(code: 'nimble.subject.nonexistant', args: [params.subjectID])
		      	response.status = 500
		    }
			else {
		    	roleService.deleteMember(subject, role)
		    	log.info("Removed subject [$subject.id]$subject.subjectname from role [$role.id]$role.name")
		    	render message(code: 'nimble.role.removemember.success', args: [role.name, subject.subjectname])
			}
		}
    }
  }

  def searchnewmembers = {
    def q = "%" + params.q + "%"
    log.debug("Performing search for subjects matching $q")

    def subjects = Subject.findAllBySubjectnameIlike(q)
    def profiles = ProfileBase.findAllByFullNameIlikeOrEmailIlike(q, q)
    def nonMembers = []

    def role = Role.get(params.id)
	if (!role) {
      	log.warn("Role identified by id '$params.id' was not located")
		render message(code: 'nimble.role.nonexistant', args: [params.id])
      	response.status = 500
    }
	else {
    	subjects.each {
	      if (!it.roles.contains(role)) {
	        nonMembers.add(it)
	        log.debug("Adding subject identified as [$it.id]$it.subjectname to search results")
	      }
	    }
	    profiles.each {
	      if (!it.owner.roles.contains(role) && !nonMembers.contains(it.owner)) {
	        nonMembers.add(it.owner)
	        log.debug("Adding subject identified as [$it.owner.id]$it.owner.subjectname based on profile to search results")
	      }
	    }

	    log.info("Search for new role subject members complete, returning $nonMembers.size records")
	    render(template: '/templates/admin/members_search', contextPath: pluginContextPath, model: [parent: role, subjects: nonMembers])
	}
  }

  def listpermissions = {
    def role = Role.get(params.id)
    if (!role) {
      	log.warn("Role identified by id '$params.id' was not located")
      	render message(code: 'nimble.role.nonexistant', args: [params.id])
      	response.status = 500
    }
	else {
    	log.debug("Listing permissions for role [$role.id]$role.name")
    	render(template: '/templates/admin/permissions_list', contextPath: pluginContextPath, model: [permissions: role.permissions, parent: role])
	}
  }

  def createpermission = {
    def role = Role.get(params.id)
    if (!role) {
      log.warn("Role identified by id '$params.id' was not located")
      render message(code: 'nimble.role.nonexistant', args: [params.id])
      response.status = 500
    }
	else {
    	LevelPermission permission = new LevelPermission()
	    permission.populate(params.first, params.second, params.third, params.fourth, params.fifth, params.sixth)
	    permission.managed = false

	    if (permission.hasErrors()) {
	      log.debug("Submitted permission was not valid")
	      render(template: "/templates/errors", contextPath: pluginContextPath, model: [bean: permission])
	      response.status = 500
	    }
		else {
	    	def savedPermission = permissionService.createPermission(permission, role)
		    if (savedPermission.hasErrors()) {
		      log.warn("Submitted permission was unable to be assigned to role [$role.id]$role.name")
		      render(template: "/templates/errors", contextPath: pluginContextPath, model: [bean: savedPermission])
		      response.status = 500
		    }
			else {
		    	log.info("Assigned permission $savedPermission.id to role [$role.id]$role.name")
			    render message(code: 'nimble.permission.create.success', args: [role.name])
		    }
		}
	}
  }

  def removepermission = {
    def role = Role.get(params.id)
    if (!role) {
      log.warn("Role identified by id '$params.id' was not located")
      render message(code: 'nimble.role.nonexistant', args: [params.id])
      response.status = 500
    }
	else {
		def permission = Permission.get(params.permID)
    	if (!permission) {
	      log.warn("Permission identified by id '$params.permID' was not located")
	      render message(code: 'nimble.permission.nonexistant', args: [params.id])
	      response.status = 500
	    }
		else {
	    	permissionService.deletePermission(permission)
		    log.info("Removed permission $permission.id from role [$role.id]$role.name")
		    render message(code: 'nimble.permission.remove.success', args: [role.name])
    	}
  	}
  }

}
