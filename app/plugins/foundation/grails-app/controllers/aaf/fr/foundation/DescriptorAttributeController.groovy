package aaf.fr.foundation

import org.apache.shiro.SecurityUtils

/**
 * Provides attribute management views for Descriptors.
 *
 * @author Bradley Beddoes
 */
class DescriptorAttributeController {
	def allowedMethods = [add: 'POST', remove: 'DELETE']
	
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
		
		if(SecurityUtils.subject.isPermitted("descriptor:${descriptor.id}:attribute:remove")) {	
			def attribute = Attribute.get(params.attributeID)
			if(!attribute) {
				log.warn "Attribute identified by id ${params.attributeID} was not located"
				render message(code: 'fedreg.attribute.nonexistant', args: [params.attributeID])
				response.setStatus(500)
				return
			}
		
			if(!descriptor.attributes.contains(attribute)) {
				log.warn "${attribute} isn't supported by descriptor ${params.id}"
				response.setStatus(500)
				render message(code: 'fedreg.attribute.remove.notsupported', args:[attribute.base.name])
				return
			}
		
			descriptor.removeFromAttributes(attribute)
			descriptor.save()
			if(descriptor.hasErrors()) {
				log.warn "$subject removing $attribute from descriptor ${params.id} failed"
				descriptor.errors.each {
					log.debug it
				}
				render message(code: 'fedreg.attribute.remove.failed', args:[attribute.base.name])
				response.setStatus(500)
				return
			}else {
				log.info "$subject removed $attribute from descriptor ${params.id}"
				render message(code: 'fedreg.attribute.remove.success', args:[attribute.base.name])
			}
		}
		else {
			log.warn("Attempt to remove attribute from $descriptor by $subject was denied, incorrect permission set")
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
		
		render template: "/templates/attributes/list", contextPath: pluginContextPath, model:[descriptor:descriptor, attrs:descriptor.sortedAttributes(), containerID:params.containerID]
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
		
		if(SecurityUtils.subject.isPermitted("descriptor:${descriptor.id}:attribute:add")) {
			def base = AttributeBase.get(params.attributeID)
			if(!base) {
				log.warn "Attribute Base identified by id ${params.attributeID} was not located"
				render message(code: 'fedreg.nameidformat.nonexistant', args: [params.attributeID])
				response.setStatus(500)
				return
			}
		
			for( a in descriptor.attributes) {
				if(a.base == base) {
					log.warn "${base} is already supported by descriptor ${params.id}"
					response.setStatus(500)
					render message(code: 'fedreg.attribute.add.alreadysupported', args:[base.name])
					return
				}
			}
		
			def attribute = new Attribute(base:base)
			descriptor.addToAttributes(attribute)
			descriptor.save()
			if(descriptor.hasErrors()) {
				log.warn "$subject adding $attribute to descriptor ${params.id} failed"
				descriptor.errors.each {
					log.debug it
				}
				render message(code: 'fedreg.attribute.add.failed', args:[base.name])
				response.setStatus(500)
				return
			} else {
				log.info "$subject added $base to $descriptor"
				render message(code: 'fedreg.attribute.add.success', args:[base.name])
			}
		}
		else {
			log.warn("Attempt to add attribute to $descriptor by $subject was denied, incorrect permission set")
			response.sendError(403)
		}
	}

}
