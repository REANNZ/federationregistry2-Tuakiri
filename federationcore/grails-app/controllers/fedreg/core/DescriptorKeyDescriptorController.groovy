package fedreg.core

class DescriptorKeyDescriptorController {

	static allowedMethods = [delete: "POST", validateCertificate:"POST", listCertificates:"GET", createCertificate:"POST"]
	
	def cryptoService
	
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
		keyDescriptor.delete()
		render message(code: 'fedreg.keydescriptor.delete.success')
	}
	
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

	def create = {
		if(!params.id) {
			log.warn "KeyDescriptor ID was not present"
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
		
		def cert = new Certificate(data:params.cert)	
		cert.expiryDate = cryptoService.expiryDate(cert)
		cert.issuer = cryptoService.issuer(cert)
		cert.subject = cryptoService.subject(cert)
		
		def keyInfo = new KeyInfo(certificate:cert, keyName:params.certname)
		def keyDescriptor = new KeyDescriptor(keyInfo:keyInfo, keyType:KeyTypes.signing, roleDescriptor:descriptor)
		
		descriptor.addToKeyDescriptors(keyDescriptor)
		descriptor.save()
		
		if(descriptor.hasErrors()) {
			descriptor.errors.each {
				log.wearn it
			}
			render message(code: 'fedreg.keydescriptor.create.failed')
			response.setStatus(500)
			return
		}
		
		render message(code: 'fedreg.keydescriptor.create.success')
	}
}