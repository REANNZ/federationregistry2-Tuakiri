package fedreg.core

class EndpointController {

	static allowedMethods = [delete: "POST", listEndpoints:"GET"]
	
	def cryptoService
	def allowedEndpoints = ["singleSignOnServices", "artifactResolutionServices", "singleLogoutServices"]
	
	// AJAX Bound
	def delete = {
		if(!params.id) {
			log.warn "Endpoint ID was not present"
			render message(code: 'fedreg.controllers.namevalue.missing')
			response.sendError(500)
			return
		}
		
		def endpoint = Endpoint.get(params.id)
		if(!endpoint) {
			log.warn "Endpoint identified by id $params.id was not located"
			render message(code: 'fedreg.endpoint.nonexistant', args: [params.id])
			response.sendError(500)
			return
		}
		
		log.info "Deleting Endpoint"
		endpoint.delete()
		render message(code: 'fedreg.endpoint.delete.success')
	}
	
	def listEndpoints = {
		if(!params.id) {
			log.warn "Descriptor ID was not present"
			render message(code: 'fedreg.controllers.namevalue.missing')
			response.sendError(500)
			return
		}
		
		if(!params.endpointType) {
			log.warn "Endpoint type was not present"
			render message(code: 'fedreg.controllers.namevalue.missing')
			response.sendError(500)
			return
		}
		
		if(!params.containerID) {
			log.warn "Container ID was not present"
			render message(code: 'fedreg.controllers.namevalue.missing')
			response.sendError(500)
			return
		}
		
		def descriptor = RoleDescriptor.get(params.id)
		if (!descriptor) {
			log.warn "Descriptor was not found for id ${params.id}"
			render message(code: 'fedreg.roledescriptor.nonexistant', args: [params.id])
			response.sendError(500)
			return
		}
		
		def endpoint = params.endpointType
		if(allowedEndpoints.contains(endpoint) && descriptor.hasProperty(endpoint)) {
			log.debug "Listing endpoints for descriptor ID ${params.id} of type ${endpoint}"
			render (template:"/templates/endpoints/endpointlist", model:[endpoints:descriptor."${endpoint}", allowremove:true, endpointType:endpoint, containerID:params.containerID])
		}
		else {
			log.warn "Endpoint ${endpoint} is invalid for Descriptor with id ${params.id}"
			render message(code: 'fedreg.endpoint.invalid', args: [endpoint])
			response.sendError(500)
			return
		}
	}
}