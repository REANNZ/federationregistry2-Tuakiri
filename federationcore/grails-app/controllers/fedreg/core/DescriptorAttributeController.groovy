package fedreg.core

class DescriptorAttributeController {

	static allowedMethods = [remove: "POST"]
	
	def remove = {
		if(!params.id) {
			log.warn "Descriptor ID was not present"
			render message(code: 'fedreg.controllers.namevalue.missing')
			response.setStatus(500)
			return
		}
		
		if(!params.attributeID) {
			log.warn "Attribute ID was not present"
			render message(code: 'fedreg.controllers.namevalue.missing')
			response.setStatus(500)
			return
		}
		
		def descriptor = RoleDescriptor.get(params.id)
		if(!descriptor) {
			log.warn "RoleDescriptor identified by id $params.id was not located"
			render message(code: 'fedreg.attribute.nonexistant', args: [params.id])
			response.setStatus(500)
			return
		}
		
		def attribute = Attribute.get(params.attributeID)
		if(!attribute) {
			log.warn "Attribute identified by id ${params.attributeID} was not located"
			render message(code: 'fedreg.attribute.nonexistant', args: [params.attributeID])
			response.setStatus(500)
			return
		}
		
		if(!descriptor.attributes.contains(attribute)) {
			log.warn "Attribute identified by id ${params.attributeID} isn't supported by descriptor ${params.id}"
			response.setStatus(500)
			render message(code: 'fedreg.attribute.remove.notsupported', args:[attribute.friendlyName])
			return
		}
		
		log.info "Removing attribute (${params.attributeID})${attribute.friendlyName} from descriptor ${params.id}"
		descriptor.removeFromAttributes(attribute)
		descriptor.save()
		if(descriptor.hasErrors()) {
			log.warn "Removing attribute (${params.attributeID})${attribute.friendlyName} from descriptor ${params.id} failed"
			descriptor.errors.each {
				log.debug it
			}
			render message(code: 'fedreg.attribute.remove.failed', args:[attribute.friendlyName])
			response.setStatus(500)
			return
		}else {
			render message(code: 'fedreg.attribute.remove.success', args:[attribute.friendlyName])
		}
	}
	
	def list = {
		if(!params.id) {
			log.warn "Descriptor ID was not present"
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
		if(!descriptor) {
			log.warn "RoleDescriptor identified by id ${params.id} was not located"
			render message(code: 'fedreg.roledescriptor.nonexistant', args: [params.id])
			response.setStatus(500)
			return
		}
		
		render template: "/templates/attributes/list", contextPath: pluginContextPath, model:[attrs:descriptor.attributes, containerID:params.containerID]
	}
	
	def add = {
		if(!params.id) {
			log.warn "Descriptor ID was not present"
			render message(code: 'fedreg.controllers.namevalue.missing')
			response.setStatus(500)
			return
		}
		
		if(!params.attributeID) {
			log.warn "Attribute ID was not present"
			render message(code: 'fedreg.controllers.namevalue.missing')
			response.setStatus(500)
			return
		}
		
		def descriptor = RoleDescriptor.get(params.id)
		if(!descriptor) {
			log.warn "RoleDescriptor identified by id ${params.id} was not located"
			render message(code: 'fedreg.roledescriptor.nonexistant', args: [params.id])
			response.setStatus(500)
			return
		}
		
		def attribute = Attribute.get(params.attributeID)
		if(!attribute) {
			log.warn "Attribute identified by id ${params.attributeID} was not located"
			render message(code: 'fedreg.nameidformat.nonexistant', args: [params.attributeID])
			response.setStatus(500)
			return
		}
		
		if(descriptor.attributes.contains(attribute)) {
			log.warn "Attribute identified by id ${params.attributeID} was already supported by descriptor ${params.id}"
			response.setStatus(500)
			render message(code: 'fedreg.attribute.add.alreadysupported', args:[attribute.friendlyName])
			return
		}
		
		descriptor.addToAttributes(attribute)
		descriptor.save()
		if(descriptor.hasErrors()) {
			log.warn "Adding attribute (${params.attributeID})${attribute.friendlyName} to descriptor ${params.id} failed"
			descriptor.errors.each {
				log.debug it
			}
			render message(code: 'fedreg.attribute.add.failed', args:[attribute.friendlyName])
			response.setStatus(500)
			return
		}else {
			render message(code: 'fedreg.attribute.add.success', args:[attribute.friendlyName])
		}
	}

}
