package fedreg.core

import fedreg.workflow.ProcessPriority

class SPSSODescriptorController {
	
	static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

	def defaultAction = "list"
	
	def list = {
		params.max = Math.min(params.max ? params.max.toInteger() : 20, 100)
		params.sort="organization"
		[serviceProviderList: SPSSODescriptor.list(params), serviceProviderTotal: SPSSODescriptor.count()]
	}
	
	def show = {
		if(!params.id) {
			log.warn "SPSSODescriptor ID was not present"
			flash.type="error"
			flash.message = message(code: 'fedreg.controllers.namevalue.missing')
			redirect(action: "list")
			return
		}
		
		def serviceProvider = SPSSODescriptor.get(params.id)
		if (!serviceProvider) {
			flash.type="error"
			flash.message = message(code: 'fedreg.core.spssoroledescriptor.nonexistant')
			redirect(action: "list")
			return
		}

		[serviceProvider: serviceProvider, contactTypes:ContactType.list()]
	}
	
	def create = {
		
	}
	
	def edit = {
		
	}
	
	def delete = {
		
	}
	
}