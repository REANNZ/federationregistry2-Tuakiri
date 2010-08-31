package fedreg.core

import static org.apache.commons.lang.StringUtils.*

class DescriptorEndpointController {

	static allowedMethods = [delete: "POST", create: "POST", toggle:"POST", listEndpoints:"GET"]
	
	// Maps allowed endpoints to internal class representation
	def allowedEndpoints = [singleSignOnServices:"fedreg.core.SingleSignOnService", artifactResolutionServices:"fedreg.core.ArtifactResolutionService", 
							singleLogoutServices:"fedreg.core.SingleLogoutService", assertionConsumerServices:"fedreg.core.AssertionConsumerService", attributeServices:"fedreg.core.AttributeService"]

	def cryptoService

	def delete = {
		if(!params.id) {
			log.warn "Endpoint ID was not present"
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
		
		def endpoint = Endpoint.get(params.id)
		if(!endpoint) {
			log.warn "Endpoint identified by id $params.id was not located"
			render message(code: 'fedreg.endpoint.nonexistant', args: [params.id])
			response.setStatus(500)
			return
		}
		
		log.info "Deleting Endpoint"
		endpoint.delete(flush:true)
		def descriptor = endpoint.descriptor
		descriptor."removeFrom${capitalize(params.endpointType)}"(endpoint)
		descriptor.save(flush:true)
		render message(code: 'fedreg.endpoint.delete.success')
	}
	
	def list = {
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
		
		def endpointType = params.endpointType
		def descriptor = RoleDescriptor.get(params.id)
		if (!descriptor) {
			log.warn "Descriptor was not found for id ${params.id}"
			render message(code: 'fedreg.roledescriptor.nonexistant', args: [params.id])
			response.setStatus(500)
			return
		}
		
		// Determine if we're actually listing from the collaborator (useful for AA endpoints on IDP screen)
		if(!descriptor.hasProperty(endpointType)) {
			if(descriptor.collaborator.hasProperty(endpointType))
				descriptor = descriptor.collaborator
		}
		
		if(allowedEndpoints.containsKey(endpointType) && descriptor.hasProperty(endpointType)) {
			log.debug "Listing endpoints for descriptor ID ${params.id} of type ${endpointType}"
			render template:"/templates/endpoints/list", contextPath: pluginContextPath, model:[endpoints:descriptor."${endpointType}", allowremove:true, endpointType:endpointType, containerID:params.containerID]
		}
		else {
			log.warn "Endpoint ${endpointType} is invalid for Descriptor with id ${params.id}"
			render message(code: 'fedreg.endpoint.invalid', args: [endpointType])
			response.setStatus(500)
			return
		}
	}
	
	def create = {
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
		
		def endpointType = params.endpointType
		def descriptor = RoleDescriptor.get(params.id)
		if (!descriptor) {
			log.warn "Descriptor was not found for id ${params.id}"
			render message(code: 'fedreg.roledescriptor.nonexistant', args: [params.id])
			response.setStatus(500)
			return
		}
		
		// Determine if we're actually updating the collaborator (useful for AA endpoints on IDP screen)
		if(!descriptor.hasProperty(endpointType)) {
			if(descriptor.collaborator.hasProperty(endpointType))
				descriptor = descriptor.collaborator
		}
		
		def binding = SamlURI.get(params.binding)
		if (!binding) {
			log.warn "SamURI (binding) was not found for id ${params.id}"
			render message(code: 'fedreg.samluri.nonexistant', args: [params.binding])
			response.setStatus(500)
			return
		}
		
		if(allowedEndpoints.containsKey(endpointType) && descriptor.hasProperty(endpointType)) {
			log.debug "Creating endpoint for ${descriptor} of type ${endpointType} at location ${params.location}"
			
			def location = new UrlURI(uri:params.location)
			def endpoint = grailsApplication.classLoader.loadClass(allowedEndpoints.get(endpointType)).newInstance(descriptor:descriptor, binding: binding, location: location, active:true)
			descriptor."addTo${capitalize(endpointType)}"(endpoint)
			endpoint.save()
			descriptor.save()
			if(descriptor.hasErrors() || endpoint.hasErrors()) {
				descriptor.errors.each {
					log.warn it
				}
				endpoint.errors.each {
					log.warn it
				}
				render message(code: 'fedreg.endpoint.create.failed')
				response.setStatus(500)
				return
			}
			else {
				descriptor.refresh()
				log.debug "Created endpoint for ${descriptor} of type ${endpointType} at location ${params.location}"
				render message(code: 'fedreg.endpoint.create.success')
				return
			}
		}
		else {
			log.warn "Endpoint ${endpointType} is invalid for ${descriptor}, unable to create"
			render message(code: 'fedreg.endpoint.invalid', args: [endpointType])
			response.setStatus(500)
			return
		}
	}
	
	def toggle = {
		if(!params.id) {
			log.warn "Endpoint ID was not present"
			render message(code: 'fedreg.controllers.namevalue.missing')
			response.sendError(500)
			return
		}
		
		def endpoint = Endpoint.lock(params.id)
		if(!endpoint) {
			log.warn "Endpoint identified by id $params.id was not located"
			render message(code: 'fedreg.endpoint.nonexistant', args: [params.id])
			response.setStatus(500)
			return
		}
	
		log.info "Toggling Endpoint State"
		endpoint.active = !endpoint.active
		endpoint.descriptor.save(flush:true)
		if(!endpoint.hasErrors() && !endpoint.descriptor.hasErrors()) {
			log.debug "Toggled state for endpoint ID ${params.id}"
			render message(code: 'fedreg.endpoint.toggle.success')
		}
		else {
			log.warn "Unable to toggle state for endpoint ID ${params.id}"
			endpoint.errors.each { log.warn it }
			endpoint.descriptor.errors.each { log.warn it }
			render message(code: 'fedreg.endpoint.toggle.failed')
		}
	}
}