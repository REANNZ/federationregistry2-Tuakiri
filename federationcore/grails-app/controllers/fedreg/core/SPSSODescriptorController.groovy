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
		
	}
	
	def create = {
		
	}
	
	def edit = {
		
	}
	
	def delete = {
		
	}
	
}