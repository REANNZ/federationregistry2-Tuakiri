package fedreg.core

import static org.apache.commons.lang.StringUtils.*

import org.apache.shiro.SecurityUtils

/**
 * Provides endpoint management views for Descriptors.
 *
 * @author Bradley Beddoes
 */
class DescriptorEndpointController {
	def allowedMethods = [create:'POST', update:'PUT', toggle:'PUT', makeDefault:'PUT', delete:'DELETE']
	
	// Maps allowed endpoints to internal class representation
	def allowedEndpoints = [singleSignOnServices:"fedreg.core.SingleSignOnService", artifactResolutionServices:"fedreg.core.ArtifactResolutionService", manageNameIDServices:"fedreg.core.ManageNameIDService",
							singleLogoutServices:"fedreg.core.SingleLogoutService", assertionConsumerServices:"fedreg.core.AssertionConsumerService", attributeServices:"fedreg.core.AttributeService", 
							discoveryResponseServices:"fedreg.core.DiscoveryResponseService"]

	def endpointService
	
	def edit = {
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
		
		render template:"/templates/endpoints/edit", contextPath: pluginContextPath, model:[endpoint:endpoint, endpointType:params.endpointType, containerID:params.containerID]
	}
	
	def update = {
		if(!params.id) {
			log.warn "Endpoint ID was not present"
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
		
		def binding = SamlURI.get(params.binding)
		if (!binding) {
			log.warn "SamURI (binding) was not found for id ${params.binding}"
			render message(code: 'fedreg.samluri.nonexistant', args: [params.binding])
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
		
		if(SecurityUtils.subject.isPermitted("descriptor:${endpoint.descriptor.id}:endpoint:update")) {
			endpointService.update(endpoint, binding, params.location, params.int('samlindex'))
		
			log.info "$authenticatedUser updated $endpoint for ${endpoint.descriptor}"
			render message(code: 'fedreg.endpoint.update.success')
		} else {
			log.warn("Attempt to update $endpoint by $authenticatedUser was denied, incorrect permission set")
			response.sendError(403)
		}
	}

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
		
		def descriptor = endpoint.descriptor
		
		if(SecurityUtils.subject.isPermitted("descriptor:${endpoint.descriptor.id}:endpoint:remove")) {
			endpointService.delete(endpoint, params.endpointType)
			endpointService.determineDescriptorProtocolSupport(descriptor)
			render message(code: 'fedreg.endpoint.delete.success')
		}
		else {
			log.warn("Attempt to remove $endpoint by $authenticatedUser was denied, incorrect permission set")
			response.sendError(403)
		}
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
			
			def minSizeConstraint = descriptor.constraints."$endpointType"?.getAppliedConstraint('minSize')
			def minEndpoints = minSizeConstraint ? minSizeConstraint.getMinSize():0
			
			render template:"/templates/endpoints/list", contextPath: pluginContextPath, model:[endpoints:descriptor."${endpointType}", allowremove:true, endpointType:endpointType, containerID:params.containerID, minEndpoints:minEndpoints]
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
		
		if(SecurityUtils.subject.isPermitted("descriptor:${descriptor.id}:endpoint:create")) {
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
				println params
				endpointService.create(descriptor, allowedEndpoints.get(endpointType), endpointType, binding, params.location, params.int('samlindex'), params.active ? true:false)
				endpointService.determineDescriptorProtocolSupport(descriptor)
				
				log.info "$authenticatedUser created new endpoint location ${params.location}, $binding for $descriptor"
				render message(code: 'fedreg.endpoint.create.success')
			}
			else {
				log.warn "$authenticatedUser unable to create endpoint as ${endpointType} is invalid for ${descriptor}"
				render message(code: 'fedreg.endpoint.invalid', args: [endpointType])
				response.setStatus(500)
			}
		}
		else {
			log.warn("Attempt to create endpoint for $descriptor by $authenticatedUser was denied, incorrect permission set")
			response.sendError(403)
		}
	}
	
	def toggle = {
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
	
		if(SecurityUtils.subject.isPermitted("descriptor:${endpoint.descriptor.id}:endpoint:toggle")) {
			endpointService.toggle(endpoint)
			
			log.info "$authenticatedUser toggled state of $endpoint"
			render message(code: 'fedreg.endpoint.toggle.success')
		}
		else {
			log.warn("Attempt to toggle $endpoint state by $authenticatedUser was denied, incorrect permission set")
			response.sendError(403)
		}
	}
		
	def makeDefault = {
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
		
		def endpointType = params.endpointType
	
		if(SecurityUtils.subject.isPermitted("descriptor:${endpoint.descriptor.id}:endpoint:makedefault")) {
			def descriptor = endpoint.descriptor
			
			// Determine if we're actually updating the collaborator (useful for AA endpoints on IDP screen)
			if(!descriptor.hasProperty(endpointType)) {
				if(descriptor.collaborator.hasProperty(endpointType))
					descriptor = descriptor.collaborator
			}
			
			if(allowedEndpoints.containsKey(endpointType) && descriptor.hasProperty(endpointType)) {
				endpointService.makeDefault(endpoint, endpointType)
				
				log.info "$authenticatedUser made $endpoint default for $descriptor"
				render message(code: 'fedreg.endpoint.makedefault.success')
			}
			else {
				log.warn "Endpoint ${endpointType} is invalid for ${descriptor}, unable to make default"
				render message(code: 'fedreg.endpoint.invalid', args: [endpointType])
				response.setStatus(500)
			}
		}
		else {
			log.warn("Attempt to make $endpoint default by $authenticatedUser was denied, incorrect permission set")
			response.sendError(403)
		}
	}
}