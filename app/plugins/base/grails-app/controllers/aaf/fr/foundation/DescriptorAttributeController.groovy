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
			render message(code: 'controllers.fr.generic.namevalue.missing')
			response.setStatus(500)
			return
		}
		
		if(!params.attrid) {
			log.warn "Attribute ID was not present"
			render message(code: 'controllers.fr.generic.namevalue.missing')
			response.setStatus(500)
			return
		}
		
		def descriptor = RoleDescriptor.get(params.id)
		if(!descriptor) {
			log.warn "RoleDescriptor identified by id $params.id was not located"
			render message(code: 'domains.fr.foundation.attribute.nonexistant', args: [params.id])
			response.setStatus(500)
			return
		}
		
		if(SecurityUtils.subject.isPermitted("federation:management:descriptor:${descriptor.id}:attribute:remove")) {	
			def attribute = Attribute.get(params.attrid)
			if(!attribute) {
				log.warn "Attribute identified by id ${params.attrid} was not located"
				render message(code: 'domains.fr.foundation.attribute.nonexistant', args: [params.attrid])
				response.setStatus(500)
				return
			}
		
			if(!descriptor.attributes.contains(attribute)) {
				log.warn "${attribute} isn't supported by descriptor ${params.id}"
				response.setStatus(500)
				render message(code: 'domains.fr.foundation.attribute.remove.notsupported', args:[attribute.base.name])
				return
			}
		
			descriptor.removeFromAttributes(attribute)
			descriptor.save()
			if(descriptor.hasErrors()) {
				log.warn "$subject removing $attribute from descriptor ${params.id} failed"
				descriptor.errors.each {
					log.debug it
				}
				render message(code: 'domains.fr.foundation.attribute.remove.failed', args:[attribute.base.name])
				response.setStatus(500)
				return
			}else {
				log.info "$subject removed $attribute from descriptor ${params.id}"
				render message(code: 'domains.fr.foundation.attribute.remove.success', args:[attribute.base.name])
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
			render message(code: 'controllers.fr.generic.namevalue.missing')
			response.setStatus(500)
			return
		}
		
		def descriptor = RoleDescriptor.get(params.id)
		if(!descriptor) {
			log.warn "RoleDescriptor identified by id ${params.id} was not located"
			render message(code: 'domains.fr.foundation.roledescriptor.nonexistant', args: [params.id])
			response.setStatus(500)
			return
		}
		
		render template: "/templates/attributes/list", contextPath: pluginContextPath, model:[descriptor:descriptor, attrs:descriptor.sortedAttributes()]
	}
	
	def add = {
		if(!params.id) {
			log.warn "Descriptor ID was not present"
			render message(code: 'controllers.fr.generic.namevalue.missing')
			response.setStatus(500)
			return
		}
		
		if(!params.attrid) {
			log.warn "Attribute ID was not present"
			render message(code: 'controllers.fr.generic.namevalue.missing')
			response.setStatus(500)
			return
		}
		
		def descriptor = RoleDescriptor.get(params.id)
		if(!descriptor) {
			log.warn "RoleDescriptor identified by id ${params.id} was not located"
			render message(code: 'domains.fr.foundation.roledescriptor.nonexistant', args: [params.id])
			response.setStatus(500)
			return
		}
		
		if(SecurityUtils.subject.isPermitted("federation:management:descriptor:${descriptor.id}:attribute:add")) {
			def base = AttributeBase.get(params.attrid)
			if(!base) {
				log.warn "Attribute Base identified by id ${params.attrid} was not located"
				render message(code: 'domains.fr.foundation.nameidformat.nonexistant', args: [params.attrid])
				response.setStatus(500)
				return
			}
		
			for( a in descriptor.attributes) {
				if(a.base == base) {
					log.warn "${base} is already supported by descriptor ${params.id}"
					response.setStatus(500)
					render message(code: 'domains.fr.foundation.attribute.add.alreadysupported', args:[base.name])
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
				render message(code: 'domains.fr.foundation.attribute.add.failed', args:[base.name])
				response.setStatus(500)
				return
			} else {
				log.info "$subject added $base to $descriptor"
				render message(code: 'domains.fr.foundation.attribute.add.success', args:[base.name])
			}
		}
		else {
			log.warn("Attempt to add attribute to $descriptor by $subject was denied, incorrect permission set")
			response.sendError(403)
		}
	}

}
