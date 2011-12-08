package aaf.fr.identity

/**
 * Subject accounts
 *
 * @author Bradley Beddoes
 */
class SubjectController {

  static Map allowedMethods = [	save: 'POST', update: 'POST', enable: 'POST', disable: 'POST', createpermission: 'POST', removepermisson: 'POST', searchroles: 'POST', grantrole: 'POST', removerole: 'POST']

  def subjectService
  def groupService
  def roleService
  def permissionService
  def grailsApplication

  def index = {
    redirect action: list, params: params
  }

  def list = {
    [subjects: Subject.list(params)]
  }

  def show = {
    def subject = Subject.get(params.id)
    if (!subject) {
      log.warn("Subject identified by id '$params.id' was not located")
      flash.type = "error"
      flash.message = message(code: 'nimble.subject.nonexistant', args: [params.id])
      redirect action: list
    }
	else {
    	log.debug("Showing subject [$subject.id]$subject.subjectname")
	    [subject: subject]
	}
  }

  def edit = {
    def subject = Subject.get(params.id)
    if (!subject) {
      log.warn("Subject identified by id '$params.id' was not located")

      flash.type = "error"
      flash.message = message(code: 'nimble.subject.nonexistant', args: [params.id])
      redirect action: list
    }
	else {
    	log.debug("Editing subject [$subject.id]$subject.subjectname")
	    [subject: subject]
	}
  }

  def update = {
    def subject = Subject.get(params.id)
    if (!subject) {
      log.warn("Subject identified by id '$params.id' was not located")
      flash.type = "error"
      flash.message = message(code: 'nimble.subject.nonexistant', args: [params.id])
      redirect action: edit, id: params.id
    }
	else {
      def fields = grailsApplication.config.nimble.fields.admin.subject
      def profileFields = grailsApplication.config.nimble.fields.endsubject.profile
      subject.properties[fields] = params
      subject.profile.properties[profileFields] = params
      if (!subject.validate()) {
            log.debug("Updated details for subject [$subject.id]$subject.subjectname are invalid")
            render view: 'edit', model: [subject: subject]
      }
      else {
	      	def updatedSubject = subjectService.updateSubject(subject)
	        log.info("Successfully updated details for subject [$subject.id]$subject.subjectname")
	        flash.type = "success"
	        flash.message = message(code: 'nimble.subject.update.success', args: [subject.subjectname])
	        redirect action: show, id: updatedSubject.id
	    }
	}
  }

  // AJAX related actions
  def enable = {
    def subject = Subject.get(params.id)
    if (!subject) {
      log.warn("Subject identified by id '$params.id' was not located")
      render message(code: 'nimble.subject.nonexistant', args: [params.id])
      response.status = 500
    }
	else {
    	def enabledSubject = subjectService.enableSubject(subject)
	    log.info("Enabled subject [$subject.id]$subject.subjectname")
	    render message(code: 'nimble.subject.enable.success', args: [subject.subjectname])
	}
  }

  def disable = {
    def subject = Subject.get(params.id)
    if (!subject) {
      log.warn("Subject identified by id '$params.id' was not located")
      render = message(code: 'nimble.subject.nonexistant', args: [params.id])
      response.status = 500
    }
	else {
    	def disabledSubject = subjectService.disableSubject(subject)
	    log.info("Disabled subject [$subject.id]$subject.subjectname")
	    render message(code: 'nimble.subject.disable.success', args: [subject.subjectname])
    }
  }

  def listlogins = {
    def subject = Subject.get(params.id)
    if (!subject) {
      log.warn("Subject identified by id '$params.id' was not located")
      render message(code: 'nimble.subject.nonexistant', args: [params.id])
      response.status = 500
    }
	else {
    	log.debug("Listing login events for subject [$subject.id]$subject.subjectname")
	    def c = LoginRecord.createCriteria()
	    def logins = c.list {
	      	eq("owner", subject)
		    order("dateCreated", "desc")
		    maxResults(20)
	    }
	    render(template: '/templates/admin/logins_list', contextPath: pluginContextPath, model: [logins: logins, ownerID: subject.id])
	}
  }

  def listpermissions = {
    def subject = Subject.get(params.id)
    if (!subject) {
      	log.warn("Subject identified by id '$params.id' was not located")
	    render message(code: 'nimble.subject.nonexistant', args: [params.id])
	    response.status = 500
    }
	else {
    	log.debug("Listing permissions subject [$subject.id]$subject.subjectname is granted")
	    render(template: '/templates/admin/permissions_list', contextPath: pluginContextPath, model: [permissions: subject.permissions, parent: subject])
	}
  }

  def createpermission = {
    def subject = Subject.get(params.id)
    if (!subject) {
      	log.warn("Subject identified by id '$params.id' was not located")
      	render message(code: 'nimble.subject.nonexistant', args: [params.id])
      	response.status = 500
    }
	else {
    	LevelPermission permission = new LevelPermission()
	    permission.populate(params.first, params.second, params.third, params.fourth, params.fifth, params.sixth)
	    permission.managed = false

	    if (permission.hasErrors()) {
	      	log.warn("Creating new permission for subject [$subject.id]$subject.subjectname failed, permission is invalid")
	      	render(template: "/templates/errors", contextPath: pluginContextPath, model: [bean: permission])
	      	response.status = 500
	    }
		else {
	    	def savedPermission = permissionService.createPermission(permission, subject)
		    log.info("Creating new permission for subject [$subject.id]$subject.subjectname succeeded")
			render message(code: 'nimble.permission.create.success', args: [subject.subjectname])
		}
    }
  }

  def removepermission = {
    def subject = Subject.get(params.id)
    if (!subject) {
      	log.warn("Subject identified by id '$params.id' was not located")
      	render message(code: 'nimble.subject.nonexistant', args: [params.id])
      	response.status = 500
    }
	else {
    	def permission = Permission.get(params.long('permID'))
	    if (!permission) {
	      	log.warn("Permission identified by id '$params.permID' was not located")
	      	render message(code: 'nimble.permission.nonexistant', args: [params.permID])
	      	response.status = 500
	    }
		else {
	    	permissionService.deletePermission(permission)
		    log.info("Removing permission [$permission.id] from subject [$subject.id]$subject.subjectname succeeded")
		    render message(code: 'nimble.permission.remove.success', args: [subject.subjectname])
		}
    }
  }

  def listroles = {
    def subject = Subject.get(params.id)
    if (!subject) {
      	log.warn("Subject identified by id '$params.id' was not located")
      	render message(code: 'nimble.subject.nonexistant', args: [params.id])
      	response.status = 500
    }
	else {
    	log.debug("Listing roles subject [$subject.id]$subject.subjectname is granted")
    	render(template: '/templates/admin/roles_list', contextPath: pluginContextPath, model: [roles: subject.roles, ownerID: subject.id])
	}
  }

  def searchroles = {
    def q = "%" + params.q + "%"
    log.debug("Performing search for roles matching $q")
    def subject = Subject.get(params.id)
    if (!subject) {
      log.warn("Subject identified by id '$params.id' was not located")
      render message(code: 'nimble.subject.nonexistant', args: [params.id])
      response.status = 500
    }
	else {
    	def roles = Role.findAllByNameIlikeOrDescriptionIlike(q, q, false)
	    def respRoles = []
	    roles.each {
	      if (!subject.roles.contains(it) && !it.protect) {
	        respRoles.add(it)    // Eject already assigned roles for this subject
	        log.debug("Adding role identified as [$it.id]$it.name to search results")
	      }
	    }
	    log.info("Search for new roles subject [$subject.id]$subject.subjectname can be assigned complete, returning $respRoles.size records")
	    render(template: '/templates/admin/roles_search', contextPath: pluginContextPath, model: [roles: respRoles, ownerID: subject.id])
    }
  }

  def grantrole = {
    def subject = Subject.get(params.id)
    if (!subject) {
      	log.warn("Subject identified by id '$params.id' was not located")
	    render message(code: 'nimble.subject.nonexistant', args: [params.id])
	    response.status = 500
    }
	else {
		def role = Role.get(params.roleID)
    	if (!role) {
	      log.warn("Role identified by id '$params.roleID' was not located")
	      render message(code: 'nimble.role.nonexistant', args: [params.roleID])
	      response.status = 500
	    }
		else {
	    	if (role.protect) {
		      	log.warn("Can't assign subject [$subject.id]$subject.subjectname role [$role.id]$role.name as role is protected")
			    render message(code: 'nimble.role.protected.no.modification', args: [role.name, subject.subjectname])
			    response.status = 500
		    }
			else {
		    	roleService.addMember(subject, role)
			    log.info("Assigned subject [$subject.id]$subject.subjectname role [$role.id]$role.name")
			    render message(code: 'nimble.role.addmember.success', args: [role.name, subject.subjectname])
			}
		}
    }
  }

  def removerole = {
    def subject = Subject.get(params.id)
    if (!subject) {
      log.warn("Subject identified by id '$params.id' was not located")
      render message(code: 'nimble.subject.nonexistant', args: [params.id])
      response.status = 500
    }
	else {
		def role = Role.get(params.roleID)
    	if (!role) {
	      log.warn("Role identified by id '$params.roleID' was not located")
	      render message(code: 'nimble.role.nonexistant', args: [params.roleID])
	      response.status = 500
	    }
		else {
	    	if (role.protect) {
		      	log.warn("Can't assign subject [$subject.id]$subject.subjectname role [$role.id]$role.name as role is protected")
			    render message(code: 'nimble.role.protected.no.modification', args: [role.name, subject.subjectname])
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

}

