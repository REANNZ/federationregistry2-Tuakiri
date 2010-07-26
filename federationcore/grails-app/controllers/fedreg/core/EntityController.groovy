package fedreg.core

class EntityController {

	static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

	def index = {
		redirect(action: "list", params: params)
	}

	def list = {
		params.max = Math.min(params.max ? params.max.toInteger() : 10, 100)
		[entityList: EntityDescriptor.list(params), entityTotal: EntityDescriptor.count()]
	}

	def show = {
		def entity = EntityDescriptor.get(params.id)
		if (!entity) {
			flash.type="error"
			flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'organization.label'), params.id])}"
			redirect(action: "list")
		}
		else {
			[entity: entity]
		}
	}
	
	def create = {
		
	}
	
	def edit = {
		
	}

}
