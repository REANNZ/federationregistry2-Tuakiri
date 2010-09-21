package fedreg.core

import org.apache.shiro.SecurityUtils

import grails.plugins.nimble.core.UserBase
import grails.plugins.nimble.core.Role

class DescriptorAdministrationController {

	def roleService
	
	def listFullAdministration = {
		def descriptor = Descriptor.get(params.id)
		if (!descriptor) {
			flash.type="error"
			flash.message = message(code: 'fedreg.core.descriptor.nonexistant')
			redirect(action: "list")
			return
		}
		
		def adminRole = Role.findByName("descriptor-${descriptor.id}-administrators")
		render template: '/templates/descriptor/listfulladministration', contextPath: pluginContextPath, model:[descriptor:descriptor, administrators: adminRole?.users]
	}
	
	def searchFullAdministration = {
		def descriptor = Descriptor.get(params.id)
		if (!descriptor) {
			flash.type="error"
			flash.message = message(code: 'fedreg.core.descriptor.nonexistant')
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

		def adminRole = Role.findByName("descriptor-${descriptor.id}-administrators")
		users.removeAll(adminRole.users)
		
		render template: '/templates/descriptor/searchresultsfulladministration', contextPath: pluginContextPath, model:[descriptor:descriptor, users: users]
	}
	
	def grantFullAdministration = {
		def descriptor = Descriptor.get(params.id)
		if (!descriptor) {
			flash.type="error"
			flash.message = message(code: 'fedreg.core.descriptor.nonexistant')
			redirect(action: "list")
			return
		}	
		
		def user = UserBase.get(params.userID)
		if (!descriptor) {
			flash.type="error"
			flash.message = message(code: 'fedreg.core.user.nonexistant')
			redirect(action: "list")
			return
		}
		
		if(SecurityUtils.subject.isPermitted("descriptor:${descriptor.id}:manage:administrators")) {
			def adminRole = Role.findByName("descriptor-${descriptor.id}-administrators")
			roleService.addMember(user, adminRole)
			
			render message(code: 'fedreg.descriptor.administration.grant.success')
		}
		else {
			log.warn("Attempt to assign complete administrative control for $descriptor to $user by $authenticatedUser was denied, incorrect permission set")
			response.sendError(403)
		}
	}
	
	def revokeFullAdministration = {
		def descriptor = Descriptor.get(params.id)
		if (!descriptor) {
			flash.type="error"
			flash.message = message(code: 'fedreg.core.descriptor.nonexistant')
			redirect(action: "list")
			return
		}	
		
		def user = UserBase.get(params.userID)
		if (!descriptor) {
			flash.type="error"
			flash.message = message(code: 'fedreg.core.user.nonexistant')
			redirect(action: "list")
			return
		}
		
		if(SecurityUtils.subject.isPermitted("descriptor:${descriptor.id}:manage:administrators")) {
			def adminRole = Role.findByName("descriptor-${descriptor.id}-administrators")
			roleService.deleteMember(user, adminRole)
			
			render message(code: 'fedreg.descriptor.administration.revoke.success')
		}
		else {
			log.warn("Attempt to remove complete administrative control for $descriptor from $user by $authenticatedUser was denied, incorrect permission set")
			response.sendError(403)
		}
	}
}