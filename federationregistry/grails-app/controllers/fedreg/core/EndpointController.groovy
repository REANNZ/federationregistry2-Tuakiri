package fedreg.core

import static org.apache.commons.lang.StringUtils.*

class EndpointController {

	static allowedMethods = [delete: "POST", listEndpoints:"GET"]
	
	def cryptoService
	
	// Maps allowed endpoints to internal class representation
	def allowedEndpoints = [singleSignOnServices:"fedreg.core.SingleSignOnService", artifactResolutionServices:"fedreg.core.ArtifactResolutionService", 
							singleLogoutServices:"fedreg.core.SingleLogoutService"]
							
	// AJAX Bound
	def delete = {
		if(!params.id) {
			log.warn "Endpoint ID was not present"
			render message(code: 'fedreg.controllers.namevalue.missing')
			response.setStatus(500)
			return
		}
		
		def endpoint = Endpoint.get(params.id)
		if(!endpoint) {
			log.warn "Endpoint identified by id $params.id was not located"
			render message(code: 'fedreg.endpoint.nonexistant', args: [params.id])
			response.setStatus(500)
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
			response.setStatus(500)
			return
		}
		
		if(!params.endpointType) {
			log.warn "Endpoint type was not present"
			render message(code: 'fedreg.controllers.namevalue.missing')
			response.setStatus(500)
			return
		}
		
		if(!params.containerID) {
			log.warn "Container ID was not present"
			render message(code: 'fedreg.controllers.namevalue.missing')
			response.setStatus(500)
			return
		}
		
		def descriptor = RoleDescriptor.get(params.id)
		if (!descriptor) {
			log.warn "Descriptor was not found for id ${params.id}"
			render message(code: 'fedreg.roledescriptor.nonexistant', args: [params.id])
			response.setStatus(500)
			return
		}
		
		def endpoint = params.endpointType
		if(allowedEndpoints.containsKey(endpoint) && descriptor.hasProperty(endpoint)) {
			log.debug "Listing endpoints for descriptor ID ${params.id} of type ${endpoint}"
			render (template:"/templates/endpoints/endpointlist", model:[endpoints:descriptor."${endpoint}", allowremove:true, endpointType:endpoint, containerID:params.containerID])
		}
		else {
			log.warn "Endpoint ${endpoint} is invalid for Descriptor with id ${params.id}"
			render message(code: 'fedreg.endpoint.invalid', args: [endpoint])
			response.setStatus(500)
			return
		}
	}
	
	def createEndpoint = {
		if(!params.id) {
			log.warn "Descriptor ID was not present"
			render message(code: 'fedreg.controllers.namevalue.missing')
			response.setStatus(500)
			return
		}
		
		if(!params.endpointType) {
			log.warn "Endpoint type was not present"
			render message(code: 'fedreg.controllers.namevalue.missing')
			response.setStatus(500)
			return
		}
		
		if(!params.binding) {
			log.warn "Binding ID was not present"
			render message(code: 'fedreg.controllers.namevalue.missing')
			response.setStatus(500)
			return
		}
		
		if(!params.location) {
			log.warn "Location URI was not present"
			render message(code: 'fedreg.controllers.namevalue.missing')
			response.setStatus(500)
			return
		}
		
		def descriptor = RoleDescriptor.get(params.id)
		if (!descriptor) {
			log.warn "Descriptor was not found for id ${params.id}"
			render message(code: 'fedreg.roledescriptor.nonexistant', args: [params.id])
			response.setStatus(500)
			return
		}
		
		def binding = SamlURI.get(params.binding)
		if (!binding) {
			log.warn "SamURI (binding) was not found for id ${params.id}"
			render message(code: 'fedreg.samluri.nonexistant', args: [params.binding])
			response.setStatus(500)
			return
		}
		
		def endpoint = params.endpointType
		if(allowedEndpoints.containsKey(endpoint) && descriptor.hasProperty(endpoint)) {
			log.debug "Creating endpoint for descriptor ID ${params.id} of type ${endpoint} at location ${params.location}"
			
			def location = new UrlURI(uri:params.location)
			def service = grailsApplication.classLoader.loadClass(allowedEndpoints.get(endpoint)).newInstance(binding: binding, location: location, active:true)
			descriptor."addTo${capitalize(endpoint)}"(service)
			descriptor.save(flush:true)
			if(descriptor.hasErrors()) {
				descriptor.errors.each {
					log.warn it
				}
				render message(code: 'fedreg.endpoint.create.failed')
				response.setStatus(500)
				return
			}
			else {
				descriptor.refresh()
				log.debug "Created endpoint for descriptor ID ${params.id} of type ${endpoint} at location ${params.location}"
				render message(code: 'fedreg.endpoint.create.success')
				return
			}
		}
		else {
			log.warn "Endpoint ${endpoint} is invalid for Descriptor with id ${params.id}, unable to create"
			render message(code: 'fedreg.endpoint.invalid', args: [endpoint])
			response.setStatus(500)
			return
		}
	}
	
	def toggleState = {
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
			response.setStatus(500)
			return
		}
		
		log.info "Toggling Endpoint State"
		endpoint.active = !endpoint.active
		endpoint.save()
		if(!endpoint.hasErrors()) {
			log.debug "Toggled state for endpoint ID ${params.id}"
			render message(code: 'fedreg.endpoint.toggle.success')
		}
		else {
			log.warn "Unable to toggle state for endpoint ID ${params.id}"
			endpoint.errors.each {
				log.warn it
			}
			render message(code: 'fedreg.endpoint.toggle.failed')
		}
	}
}