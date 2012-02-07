package aaf.fr.identity

import org.apache.shiro.SecurityUtils

/**
 * Manage app wide administrators
 *
 * @author Bradley Beddoes
 */
class AdminsController {

  static Map allowedMethods = [list: 'POST', create: 'POST', delete: 'POST', search: 'POST']

  def adminsService

  def index = { }

  def list = {
    def adminAuthority = Role.findByName(AdminsService.ADMIN_ROLE)
    def authenticatedSubject = Subject.get(SecurityUtils.getSubject()?.getPrincipal())

    if(!authenticatedSubject) {
      log.error("Not able to determine currently authenticated subject")
      response.sendError(403)
      return
    }

	render(template: '/templates/admin/administrators_list', contextPath: pluginContextPath, model: [currentAdmin:authenticatedSubject, admins: adminAuthority?.subjects])
  }

  def create = {
    def subject = Subject.get(params.id)
    if (!subject) {
      log.warn("Subject identified by id $params.id was not located")

      response.sendError(500)
      render message(code: 'nimble.subject.nonexistant', args: [params.id])
      return
    }

    def result = adminsService.add(subject)
    if (result) {
      log.debug("Subject identified as [$subject.id]$subject.subjectname was added as an administrator")
      render message(code: 'nimble.admin.grant.success', args: [subject.subjectname])
      return
    }
    else {
      log.warn("Subject identified as [$subject.id]$subject.subjectname was unable to be made an administrator")
      response.sendError(500)
      render message(code: 'nimble.admin.grant.failed', args: [subject.subjectname])
      return
    }
  }

  def delete = {
    def subject = Subject.get(params.id)

    if (!subject) {
      log.warn("Subject identified by id $params.id was not located")  
      render message(code: 'nimble.subject.nonexistant', args: [params.id])
      response.sendError(500)
      return
    }

    if(subject == authenticatedSubject) {
      log.warn("Administrators are not able to remove themselves from the administrative role") 
      render message(code: 'nimble.admin.revoke.self', args: [subject.subjectname])
      response.sendError(500)
      return
    }

    def result = adminsService.remove(subject)
    if (result) {
      render message(code: 'nimble.admin.revoke.success', args: [subject.subjectname])
      return
    }
    else {
      log.warn("Subject identified as [$subject.id]$subject.subjectname was unable to be removed as an administrator")
      render message(code: 'nimble.admin.revoke.error', args: [subject.subjectname])
	  response.sendError(500)
      return
    }
  }

  def search = {
    def q = "%" + params.q + "%"

    log.debug("Performing search for subjects matching $q")

    def subjects = Subject.findAllBySubjectnameIlike(q)
    def profiles = ProfileBase.findAllByFullNameIlikeOrEmailIlike(q, q)
    def nonAdmins = []

    subjects.each {
	  boolean admin = false
      it.roles.each { role ->
        if(role.name == AdminsService.ADMIN_ROLE)
			admin = true
      }
	  if(!admin)
		nonAdmins.add(it)
    }

    profiles.each {
		boolean admin = false
	      it.owner.roles.each { role ->
	        if(role.name == AdminsService.ADMIN_ROLE)
				admin = true
	      }
		  if(!admin && !nonAdmins.contains(it.owner))
			nonAdmins.add(it.owner)
    }

    log.info("Search for new administrators complete, returning $nonAdmins.size records")
	render(template: '/templates/admin/administrators_search', contextPath: pluginContextPath, model: [subjects: nonAdmins])
  }
}
