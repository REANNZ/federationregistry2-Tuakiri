package fedreg.core

class RoleDescriptorMonitorController {

	def list = {
		def roleDescriptor = RoleDescriptor.get(params.id)
		if (!roleDescriptor) {
			flash.type="error"
			flash.message = message(code: 'fedreg.core.roledescriptor.nonexistant')
			response.sendError(500)
			return
		}
		
		render template: '/templates/monitor/list', contextPath: pluginContextPath, model:[roleDescriptor:roleDescriptor]
	}
	
	def create = {
		
	}
	
	def save = {
		
	}
	
	def delete = {
		
	}

}