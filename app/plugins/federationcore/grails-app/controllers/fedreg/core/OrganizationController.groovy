package fedreg.core

import org.apache.shiro.SecurityUtils

import grails.plugins.nimble.core.Role

/**
 * Provides Organization views.
 *
 * @author Bradley Beddoes
 */
class OrganizationController {
	static defaultAction = "index"
	def allowedMethods = [save: 'POST', update: 'PUT']
		
	def organizationService

	def list = {
		[organizationList: Organization.findAllWhere(archived:false), organizationTotal: Organization.count()]
	}
	
	def listarchived = {
		[organizationList: Organization.findAllWhere(archived:true), organizationTotal: Organization.count()]
	}

	def show = {
		if(!params.id) {
			log.warn "Organization ID was not present"
			flash.type="error"
			flash.message = message(code: 'fedreg.controllers.namevalue.missing')
			redirect(action: "list")
			return
		}
		
		def organization = Organization.get(params.id)
		if (organization) {
			def entities = EntityDescriptor.findAllWhere(organization:organization)
			def adminRole = Role.findByName("organization-${organization.id}-administrators")
			def identityproviders = []
			def serviceproviders = []
						
			entities.each { e ->
				e.idpDescriptors.each { idp -> identityproviders.add(idp)}
				e.spDescriptors.each { sp -> serviceproviders.add(sp) }
			}
			
			[organization: organization, statistics:organization.buildStatistics(), entities:entities, identityproviders:identityproviders, serviceproviders:serviceproviders, administrators:adminRole?.users, contactTypes:ContactType.list()]
		}
		else {
			flash.type="error"
			flash.message = message(code: 'fedreg.core.organization.nonexistant')
			redirect(action: "list")
		}
	}
	
	def create = {
		def organization = new Organization()
		[organization:organization, organizationTypes: OrganizationType.list()]
	}
	
	def save = {
		def (created, organization, contact) = organizationService.create(params)
	
		if(created) {
			log.info "$authenticatedUser created $organization"
			redirect (action: "show", id: organization.id)
		} else {
			log.info "$authenticatedUser failed to create $organization"
			
			flash.type="error"
			flash.message = message(code: 'fedreg.core.organization.nonexistant')
			render (view:'create', model:[organization:organization, contact:contact, organizationTypes: OrganizationType.list()])
		}
	}
	
	def edit = {
		if(!params.id) {
			log.warn "Organization ID was not present"
			flash.type="error"
			flash.message = message(code: 'fedreg.controllers.namevalue.missing')
			redirect(action: "list")
			return
		}
		
		def organization = Organization.get(params.id)
		if (!organization) {
			flash.type="error"
			flash.message = message(code: 'fedreg.core.organization.nonexistant')
			redirect(action: "list")
			return
		}	
		
		if(SecurityUtils.subject.isPermitted("organization:${organization.id}:update")) {
			[organization: organization, organizationTypes: OrganizationType.list()]	
		}
		else {
			log.warn("Attempt to edit $organization by $authenticatedUser was denied, incorrect permission set")
			response.sendError(403)
		}
	}
	
	def update = {
		if(!params.id) {
			log.warn "Organization ID was not present"
			flash.type="error"
			flash.message = message(code: 'fedreg.controllers.namevalue.missing')
			redirect(action: "list")
			return
		}
		
		def organization_ = Organization.get(params.id)
		if (!organization_) {
			flash.type="error"
			flash.message = message(code: 'fedreg.core.organization.nonexistant')
			redirect(action: "list")
			return
		}
		
		if(SecurityUtils.subject.isPermitted("organization:${organization_.id}:update")) {
			def (updated, organization) = organizationService.update(params)
			if(updated) {
				log.info "$authenticatedUser updated $organization"
				redirect (action: "show", id: organization.id)
			}
			else {
				log.info "$authenticatedUser failed to update $organization"
				
				flash.type="error"
				flash.message = message(code: 'fedreg.core.organization.update.validation.error')
				render (view:'edit', model:[organization:organization, organizationTypes: OrganizationType.list()])
			}
		}
		else {
			log.warn("Attempt to update $organization_ by $authenticatedUser was denied, incorrect permission set")
			response.sendError(403)
		}
	}

}
