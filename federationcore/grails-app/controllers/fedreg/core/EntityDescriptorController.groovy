package fedreg.core

class EntityDescriptorController {
	
	def entityDescriptorService
	
	static allowedMethods = [save: "POST", update: "POST", delete: "POST"]
	def defaultAction = "list"

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
		def entityDescriptor = new EntityDescriptor()
		[entity: entityDescriptor, organizationList: Organization.list()]
	}
	
	def save = {
		def (created, entityDescriptor) = entityDescriptorService.create(params)
		
		if(created)
			redirect (action: "show", id: entityDescriptor.id)
		else {
			flash.type="error"
			flash.message = message(code: 'fedreg.core.entitydescriptor.save.validation.error')
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
		
		[entity: entityDescriptor]
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
		
		def (updated, entityDescriptor) = entityDescriptorService.update(params)
		if(updated)
			redirect (action: "show", id: entityDescriptor.id)
		else {
			flash.type="error"
			flash.message = message(code: 'fedreg.core.entitydescriptor.update.validation.error')
			render (view:'edit', model:[entity:entityDescriptor])
		}
	}

}
