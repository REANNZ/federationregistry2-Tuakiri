package aaf.fr.foundation

import org.apache.shiro.SecurityUtils

/**
 * Provides nameID management views for Descriptors.
 *
 * @author Bradley Beddoes
 */
class DescriptorNameIDFormatController {
	def allowedMethods = [add:'POST', remove: 'DELETE']
	
	def remove = {
		if(!params.id) {
			log.warn "Descriptor ID was not present"
			render message(code: 'fedreg.controllers.namevalue.missing')
			response.setStatus(500)
			return
		}
		
		if(!params.formatID) {
			log.warn "NameIDFormat ID was not present"
			render message(code: 'fedreg.controllers.namevalue.missing')
			response.setStatus(500)
			return
		}
		
		def descriptor = RoleDescriptor.get(params.id)
		if(!descriptor) {
			log.warn "RoleDescriptor identified by id $params.id was not located"
			render message(code: 'fedreg.roledescriptor.nonexistant', args: [params.id])
			response.setStatus(500)
			return
		}
		if(SecurityUtils.subject.isPermitted("descriptor:${descriptor.id}:nameidformat:remove")) {
			def nameIDFormat = SamlURI.get(params.formatID)
			if(!nameIDFormat) {
				log.warn "NameIDFormat identified by id $params.formatID was not located"
				render message(code: 'fedreg.nameidformat.nonexistant', args: [params.formatID])
				response.setStatus(500)
				return
			}
		
			if(!descriptor.nameIDFormats.contains(nameIDFormat)) {
				log.warn "NameIDFormat identified by id $params.formatID was already supported by descriptor ${params.id}"
				response.setStatus(500)
				render message(code: 'fedreg.nameidformat.remove.notsupported', args:[nameIDFormat.uri])
				return
			}
			
			descriptor.removeFromNameIDFormats(nameIDFormat)
			descriptor.save()
			if(descriptor.hasErrors()) {
				log.warn "$subject removing $nameIDFormat from $descriptor failed"
				descriptor.errors.each {
					log.debug it
				}
				render message(code: 'fedreg.nameidformat.remove.failed', args:[nameIDFormat.uri])
				response.setStatus(500)
				return
			} else {
				log.info "$subject removed $nameIDFormat from $descriptor"
				render message(code: 'fedreg.nameidformat.remove.success', args:[nameIDFormat.uri])
			}
		}
		else {
			log.warn("Attempt to remove NameIDFormat from $descriptor by $subject was denied, incorrect permission set")
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
			log.warn "RoleDescriptor identified by id $params.id was not located"
			render message(code: 'fedreg.roledescriptor.nonexistant', args: [params.id])
			response.setStatus(500)
			return
		}
		
		render template: "/templates/nameidformats/list", contextPath: pluginContextPath, model:[descriptor:descriptor, nameIDFormats:descriptor.nameIDFormats, containerID:params.containerID]
	}
	
	def add = {
		if(!params.id) {
			log.warn "Descriptor ID was not present"
			render message(code: 'fedreg.controllers.namevalue.missing')
			response.setStatus(500)
			return
		}
		
		if(!params.formatID) {
			log.warn "NameIDFormat ID was not present"
			render message(code: 'fedreg.controllers.namevalue.missing')
			response.setStatus(500)
			return
		}
		
		def descriptor = RoleDescriptor.get(params.id)
		if(!descriptor) {
			log.warn "RoleDescriptor identified by id $params.id was not located"
			render message(code: 'fedreg.roledescriptor.nonexistant', args: [params.id])
			response.setStatus(500)
			return
		}
		
		if(SecurityUtils.subject.isPermitted("descriptor:${descriptor.id}:nameidformat:add")) {
			def nameIDFormat = SamlURI.get(params.formatID)
			if(!nameIDFormat) {
				log.warn "NameIDFormat identified by id $params.formatID was not located"
				render message(code: 'fedreg.nameidformat.nonexistant', args: [params.formatID])
				response.setStatus(500)
				return
			}
		
			if(descriptor.nameIDFormats?.contains(nameIDFormat)) {
				log.warn "NameIDFormat identified by id $params.formatID was already supported by descriptor ${params.id}"
				response.setStatus(500)
				render message(code: 'fedreg.nameidformat.add.alreadysupported', args:[nameIDFormat.uri])
				return
			}
		
			descriptor.addToNameIDFormats(nameIDFormat)
			descriptor.save()
			if(descriptor.hasErrors()) {
				log.warn "Adding nameIDFormat $nameIDFormat to $descriptor failed"
				descriptor.errors.each {
					log.error it
				}
				render message(code: 'fedreg.nameidformat.add.failed', args:[nameIDFormat.uri])
				response.setStatus(500)
				return
			} else {
				log.warn "Added nameIDFormat $nameIDFormat to $descriptor"
				render message(code: 'fedreg.nameidformat.add.success', args:[nameIDFormat.uri])
			}
		}
		else {
			log.warn("Attempt to add NameIDFormat to $descriptor by $subject was denied, incorrect permission set")
			response.sendError(403)
		}
	}

}
