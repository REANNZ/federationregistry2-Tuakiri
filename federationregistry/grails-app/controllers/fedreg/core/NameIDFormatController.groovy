package fedreg.core

class NameIDFormatController {

	static allowedMethods = [delete: "POST"]
							
	// AJAX Bound
	def delete = {
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
		
		def nameIDFormat = SamlURI.get(params.formatID)
		if(!nameIDFormat) {
			log.warn "NameIDFormat identified by id $params.formatID was not located"
			render message(code: 'fedreg.nameidformat.nonexistant', args: [params.formatID])
			response.setStatus(500)
			return
		}
		
		log.info "Removing nameIDFormat (${params.formatID})${nameIDFormat.uri} from descriptor ${params.id}"
		descriptor.removeFromNameIDFormats(nameIDFormat)
		descriptor.save()
		if(descriptor.hasErrors()) {
			log.warn "Removing nameIDFormat (${params.formatID})${nameIDFormat.uri} from descriptor ${params.id} failed"
			descriptor.errors.each {
				log.debug it
			}
			render message(code: 'fedreg.nameidformat.delete.failed')
			response.setStatus(500)
			return
		}else {
			render message(code: 'fedreg.nameidformat.delete.success')
		}
	}
	
	def listNameIDFormats = {
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
		
		render template: "/templates/nameidformats/list", model:[nameIDFormats:descriptor.nameIDFormats, containerID:params.containerID]
	}

}
