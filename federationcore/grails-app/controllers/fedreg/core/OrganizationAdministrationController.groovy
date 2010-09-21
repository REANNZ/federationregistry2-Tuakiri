package fedreg.core

import org.apache.shiro.SecurityUtils

import grails.plugins.nimble.core.UserBase
import grails.plugins.nimble.core.Role

class OrganizationAdministrationController {

	def roleService
	
	def listFullAdministration = {
		def organization = Organization.get(params.id)
		if (!organization) {
			flash.type="error"
			flash.message = message(code: 'fedreg.core.organization.nonexistant')
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
			flash.message = message(code: 'fedreg.core.organization.nonexistant')
			redirect(action: "list")
			return
		}
		
		def users = []
		if(!params.givenName && !params.surname && !params.email)
			users = UserBase.list()
		else {
			def emails
			if(params.email)
				emails = MailURI.findAllByUriLike("%${params.email}%")
			def c = Contact.createCriteria()
			def contacts = c.list {
				or {
					ilike("givenName", params.givenName)
					ilike("surname", params.surname)
					if(emails)
						'in'("email", emails)
				}
			}
			contacts.each {
				if(it.userLink) {
					users.add(UserBase.get(it.userID))
				}
			}
			
		}

		def adminRole = Role.findByName("organization-${organization.id}-administrators")
		users.removeAll(adminRole.users)
		
		render template: '/templates/organization/searchresultsfulladministration', contextPath: pluginContextPath, model:[organization:organization, users: users]
	}
	
	def grantFullAdministration = {
		def organization = Organization.get(params.id)
		if (!organization) {
			flash.type="error"
			flash.message = message(code: 'fedreg.core.organization.nonexistant')
			redirect(action: "list")
			return
		}	
		
		def user = UserBase.get(params.userID)
		if (!organization) {
			flash.type="error"
			flash.message = message(code: 'fedreg.core.user.nonexistant')
			redirect(action: "list")
			return
		}
		
		if(SecurityUtils.subject.isPermitted("organization:${organization.id}:manage:administrators")) {
			def adminRole = Role.findByName("organization-${organization.id}-administrators")
			roleService.addMember(user, adminRole)
			
			render message(code: 'fedreg.organization.administration.grant.success')
		}
		else {
			log.warn("Attempt to assign complete administrative control for $organization to $user by $authenticatedUser was denied, incorrect permission set")
			response.sendError(403)
		}
	}
	
	def revokeFullAdministration = {
		def organization = Organization.get(params.id)
		if (!organization) {
			flash.type="error"
			flash.message = message(code: 'fedreg.core.organization.nonexistant')
			redirect(action: "list")
			return
		}	
		
		def user = UserBase.get(params.userID)
		if (!organization) {
			flash.type="error"
			flash.message = message(code: 'fedreg.core.user.nonexistant')
			redirect(action: "list")
			return
		}
		
		if(SecurityUtils.subject.isPermitted("organization:${organization.id}:manage:administrators")) {
			def adminRole = Role.findByName("organization-${organization.id}-administrators")
			roleService.deleteMember(user, adminRole)
			
			render message(code: 'fedreg.organization.administration.revoke.success')
		}
		else {
			log.warn("Attempt to remove complete administrative control for $organization from $user by $authenticatedUser was denied, incorrect permission set")
			response.sendError(403)
		}
	}
}