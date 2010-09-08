package fedreg.core

import org.apache.shiro.SecurityUtils

class EntityDescriptorController {
	static defaultAction = "list"
	static allowedMethods = [save: "POST", update: "POST", delete: "POST"]
		
	def entityDescriptorService
	
	def list = {
		params.max = Math.min(params.max ? params.max.toInteger() : 20, 100)
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
		
		[entity: entity, contactTypes:ContactType.list()]
	}
	
	def create = {
		if(SecurityUtils.subject.isPermitted("entitydescriptor:create")) {
			def entityDescriptor = new EntityDescriptor()
			[entity: entityDescriptor, organizationList: Organization.list()]
		}
		else {
			log.warn("Attempt to create identity provider by $authenticatedUser was denied, incorrect permission set")
			response.sendError(403)
		}
	}
	
	def save = {
		if(SecurityUtils.subject.isPermitted("organization:${params.organization.id}:components:entitydescriptor:create")) {
			def (created, entityDescriptor) = entityDescriptorService.create(params)
		
			if(created)
				redirect (action: "show", id: entityDescriptor.id)
			else {
				flash.type="error"
				flash.message = message(code: 'fedreg.core.entitydescriptor.save.validation.error')
				render (view:'create', model:[entity:entityDescriptor, organizationList: Organization.list()])
			}
		}
		else {
			log.warn("Attempt to save entity descriptor by $authenticatedUser was denied, incorrect permission set")
			response.sendError(403)
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
			if(updated)
				redirect (action: "show", id: entityDescriptor.id)
			else {
				flash.type="error"
				flash.message = message(code: 'fedreg.core.entitydescriptor.update.validation.error')
				render (view:'edit', model:[entity:entityDescriptor])
			}
		}
		else {
			log.warn("Attempt to update $entityDescriptor by $authenticatedUser was denied, incorrect permission set")
			response.sendError(403)
		}
	}

}
