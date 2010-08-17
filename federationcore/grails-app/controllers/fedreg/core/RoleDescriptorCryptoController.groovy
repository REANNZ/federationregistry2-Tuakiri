package fedreg.core

class RoleDescriptorCryptoController {

	static allowedMethods = [delete: "POST", create:"POST"]
	
	def cryptoService
	
	def list = {
		if(!params.id) {
			log.warn "Descriptor ID was not present"
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
		
		render template:"/templates/certificates/list", contextPath: pluginContextPath, model:[descriptor:descriptor, allowremove:true]
	}
	
	def delete = {
		if(!params.id) {
			log.warn "KeyDescriptor ID was not present"
			render message(code: 'fedreg.controllers.namevalue.missing')
			response.setStatus(500)
			return
		}
		
		def keyDescriptor = KeyDescriptor.get(params.id)
		if(!keyDescriptor) {
			log.warn "KeyDescriptor identified by id $params.id was not located"
			render message(code: 'fedreg.keydescriptor.nonexistant', args: [params.id])
			response.setStatus(500)
			return
		}
		
		log.info "Deleting KeyDescriptor"
		cryptoService.unassociateCertificate(keyDescriptor)
		
		render message(code: 'fedreg.keydescriptor.delete.success')
	}

	def create = {
		if(!params.id) {
			log.warn "RoleDescriptor ID was not present"
			render message(code: 'fedreg.controllers.namevalue.missing')
			response.setStatus(500)
			return
		}
		
		if(!params.cert) {
			log.warn "Certificate data was not present"
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
		
		def associated
		if(params.signing == "on") {
			associated = cryptoService.associateCertificate(descriptor, params.cert, params.certname, KeyTypes.signing)
		
			if(!associated) {
				render message(code: 'fedreg.keydescriptor.create.signing.failed')
				response.setStatus(500)
				return
			}
		}
			
		if(params.encryption == "on") {
			associated = cryptoService.associateCertificate(descriptor, params.cert, params.certname, KeyTypes.encryption)
			
			if(!associated) {
				render message(code: 'fedreg.keydescriptor.create.encryption.failed')
				response.setStatus(500)
				return
			}
		}
		
		render message(code: 'fedreg.keydescriptor.create.success')
	}
}