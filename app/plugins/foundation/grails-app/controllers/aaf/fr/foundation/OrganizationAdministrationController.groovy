package aaf.fr.foundation

import org.apache.shiro.SecurityUtils





/**
 * Provides administration management views for Organizations.
 *
 * @author Bradley Beddoes
 */
class OrganizationAdministrationController {
	def allowedMethods = [grantFullAdministration: 'POST', revokeFullAdministration: 'DELETE']
	
	def roleService
	
	def listFullAdministration = {
		def organization = Organization.get(params.id)
		if (!organization) {
			flash.type="error"
			flash.message = message(code: 'aaf.fr.foundation.organization.nonexistant')
			redirect(action: "list")
			return
		}
		
		def adminRole = Role.findByName("organization-${organization.id}-administrators")
		render template: '/templates/organization/listfulladministration', contextPath: pluginContextPath, model:[organization:organization, administrators: adminRole?.users]
	}
	
	def searchFullAdministration = {
		def organization = Organization.get(params.id)
		if (!organization) {
			flash.type="error"
			flash.message = message(code: 'aaf.fr.foundation.organization.nonexistant')
			redirect(action: "list")
			return
		}
		
		def q = "%" + params.q + "%"
	    log.debug("Performing search for users matching $q")

	    def users = Subject.findAllByUsernameIlike(q)
	    ProfileBase.findAllByFullNameIlikeOrEmailIlike(q, q)?.each {
			if(!users.contains(it.owner))
				users.add(it.owner)
	    }

		def adminRole = Role.findByName("organization-${organization.id}-administrators")
		users.removeAll(adminRole.users)
		
		render template: '/templates/organization/searchresultsfulladministration', contextPath: pluginContextPath, model:[organization:organization, users: users]
	}
	
	def grantFullAdministration = {
		def organization = Organization.get(params.id)
		if (!organization) {
			flash.type="error"
			flash.message = message(code: 'aaf.fr.foundation.organization.nonexistant')
			redirect(action: "list")
			return
		}	
		
		def user = Subject.get(params.userID)
		if (!user) {
			flash.type="error"
			flash.message = message(code: 'aaf.fr.foundation.user.nonexistant')
			redirect(action: "list")
			return
		}
		
		if(SecurityUtils.subject.isPermitted("organization:${organization.id}:manage:administrators")) {
			def adminRole = Role.findByName("organization-${organization.id}-administrators")
			roleService.addMember(user, adminRole)
			
			log.info "$subject granted $adminRole to $user"
			render message(code: 'fedreg.organization.administration.grant.success')
		}
		else {
			log.warn("Attempt to assign complete administrative control for $organization to $user by $subject was denied, incorrect permission set")
			response.sendError(403)
		}
	}
	
	def revokeFullAdministration = {
		def organization = Organization.get(params.id)
		if (!organization) {
			flash.type="error"
			flash.message = message(code: 'aaf.fr.foundation.organization.nonexistant')
			redirect(action: "list")
			return
		}	
		
		def user = Subject.get(params.userID)
		if (!user) {
			flash.type="error"
			flash.message = message(code: 'aaf.fr.foundation.user.nonexistant')
			redirect(action: "list")
			return
		}
		
		if(SecurityUtils.subject.isPermitted("organization:${organization.id}:manage:administrators")) {
			def adminRole = Role.findByName("organization-${organization.id}-administrators")
			roleService.deleteMember(user, adminRole)
			
			log.info "$subject revoked $adminRole from $user"
			render message(code: 'fedreg.organization.administration.revoke.success')
		}
		else {
			log.warn("Attempt to remove complete administrative control for $organization from $user by $subject was denied, incorrect permission set")
			response.sendError(403)
		}
	}
}