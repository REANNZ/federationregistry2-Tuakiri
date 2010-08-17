package fedreg.core

class OrganizationController {

	def organizationService

	static allowedMethods = [save: "POST", update: "POST"]

	def index = {
		redirect(action: "list", params: params)
	}

	def list = {
		params.max = Math.min(params.max ? params.max.toInteger() : 20, 100)
		[organizationList: Organization.list(params), organizationTotal: Organization.count()]
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
		if (!organization) {
			flash.type="error"
			flash.message = message(code: 'fedreg.core.organization.nonexistant')
			redirect(action: "list")
		}
		else {
			def entities = EntityDescriptor.findAllWhere(organization:organization)
			def contacts = Contact.findAllWhere(organization:organization)
			[organization: organization, entities:entities, contacts:contacts]
		}
	}
	
	def create = {
		def organization = new Organization()
		[organization:organization, organizationTypes: OrganizationType.list()]
	}
	
	def save = {
		def (created, organization, contact) = organizationService.create(params)
		
		if(created)
			redirect (action: "show", id: organization.id)
		else
			render (view:'create', model:[organization:organization, contact:contact])
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
		
		[organization: organization, organizationTypes: OrganizationType.list()]	
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
		
		def (updated, organization) = organizationService.update(params)
		if(updated)
			redirect (action: "show", id: organization.id)
		else {
			flash.type="error"
			flash.message = message(code: 'fedreg.core.organization.update.validation.error')
			render (view:'edit', model:[organization:organization, organizationTypes: OrganizationType.list()])
		}
	}

}
