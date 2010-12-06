package fedreg.core

import org.apache.shiro.SecurityUtils
import grails.plugins.nimble.core.Role

class EntityDescriptorController {
	static defaultAction = "list"
	def allowedMethods = [save: 'POST', update: 'PUT', delete: 'DELETE']
		
	def entityDescriptorService
	
	def list = {
		[entityList: EntityDescriptor.list(params), entityTotal: EntityDescriptor.count()]
	}

	def show = {
		if(!params.id) {
			log.warn "EntityDescriptor ID was not present"
			flash.type="error"
			flash.message = message(code: 'fedreg.controllers.namevalue.missing')
			redirect(action: "list")
			return
		}
		
		def entity = EntityDescriptor.get(params.id)
		if (!entity) {
			flash.type="error"
			flash.message = message(code: 'fedreg.core.entitydescriptor.nonexistant')
			redirect(action: "list")
			return
		}
		
		def adminRole = Role.findByName("descriptor-${entity.id}-administrators")
		[entity: entity, contactTypes:ContactType.list(), administrators:adminRole?.users]
	}
	
	def create = {
		def entityDescriptor = new EntityDescriptor()
		[entity: entityDescriptor, organizationList: Organization.list()]
	}
	
	def save = {
		def (created, entityDescriptor) = entityDescriptorService.create(params)
	
		if(created) {
			log.info "$authenticatedUser created $entityDescriptor"
			redirect (action: "show", id: entityDescriptor.id)
		} else {
			flash.type="error"
			flash.message = message(code: 'fedreg.core.entitydescriptor.save.validation.error')
			
			log.info "$authenticatedUser failed attempting to create $entityDescriptor"
			render (view:'create', model:[entity:entityDescriptor, organizationList: Organization.list()])
		}
	}
	
	def edit = {
		if(!params.id) {
			log.warn "EntityDescriptor ID was not present"
			flash.type="error"
			flash.message = message(code: 'fedreg.controllers.namevalue.missing')
			redirect(action: "list")
			return
		}
		
		def entityDescriptor = EntityDescriptor.get(params.id)
		if (!entityDescriptor) {
			flash.type="error"
			flash.message = message(code: 'fedreg.core.entitydescriptor.nonexistant')
			redirect(action: "list")
			return
		}	
		
		if(SecurityUtils.subject.isPermitted("descriptor:${entityDescriptor.id}:update")) {
			[entity: entityDescriptor]
		}
		else {
			log.warn("Attempt to edit $entityDescriptor by $authenticatedUser was denied, incorrect permission set")
			response.sendError(403)
		}		
	}
	
	def update = {
		if(!params.id) {
			log.warn "EntityDescriptor ID was not present"
			flash.type="error"
			flash.message = message(code: 'fedreg.controllers.namevalue.missing')
			redirect(action: "list")
			return
		}
		
		def entityDescriptor_ = EntityDescriptor.get(params.id)
		if (!entityDescriptor_) {
			flash.type="error"
			flash.message = message(code: 'fedreg.core.entitydescriptor.nonexistant')
			redirect(action: "list")
			return
		}
		
		if(SecurityUtils.subject.isPermitted("descriptor:${entityDescriptor_.id}:update")) {
			def (updated, entityDescriptor) = entityDescriptorService.update(params)
			if(updated) {
				log.info "$authenticatedUser updated $entityDescriptor"
				redirect (action: "show", id: entityDescriptor.id)
			} else {
				flash.type="error"
				flash.message = message(code: 'fedreg.core.entitydescriptor.update.validation.error')
				log.info "$authenticatedUser failed attempt to update $entityDescriptor"
				render (view:'edit', model:[entity:entityDescriptor])
			}
		}
		else {
			log.warn("Attempt to update $entityDescriptor by $authenticatedUser was denied, incorrect permission set")
			response.sendError(403)
		}
	}

}
