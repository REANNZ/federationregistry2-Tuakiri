package fedreg.core

class KeyDescriptorController {

	static allowedMethods = [delete: "POST", validateCertificate:"POST", listCertificates:"GET", createCertificate:"POST"]
	
	def cryptoService
	
	// AJAX Bound
	def delete = {
		if(!params.id) {
			log.warn "KeyDescriptor ID was not present"
			render message(code: 'fedreg.controllers.namevalue.missing')
			response.sendError(500)
			return
		}
		
		def keyDescriptor = KeyDescriptor.get(params.id)
		if(!keyDescriptor) {
			log.warn "KeyDescriptor identified by id $params.id was not located"
			render message(code: 'fedreg.keydescriptor.nonexistant', args: [params.id])
			response.sendError(500)
			return
		}
		
		log.info "Deleting KeyDescriptor"
		keyDescriptor.delete()
		render message(code: 'fedreg.keydescriptor.delete.success')
	}
	
	def listCertificates = {
		if(!params.id) {
			log.warn "Descriptor ID was not present"
			render message(code: 'fedreg.controllers.namevalue.missing')
			response.sendError(500)
			return
		}
		
		def descriptor = RoleDescriptor.get(params.id)
		if (!descriptor) {
			log.warn "Descriptor was not found for id ${params.id}"
			render message(code: 'fedreg.roledescriptor.nonexistant', args: [params.id])
			response.sendError(500)
			return
		}
		
		render (template:"/templates/certificates/certificatelist", model:[descriptor:descriptor, allowremove:true])
	}
	
	def validateCertificate = {
		if(!params.cert) {
			log.warn "Certificate data was not present"
			render message(code: 'fedreg.controllers.namevalue.missing')
			response.sendError(500)
			return
		}
		
		log.debug "About to validate new certificate:\n${params.cert}"
		try {
		def certificate = new Certificate(data: params.cert)
		def subject = cryptoService.subject(certificate);
		def issuer = cryptoService.issuer(certificate);
		def expires = cryptoService.expiryDate(certificate);
			
		//TODO extend validation checks over time
		def valid = true
		def certerrors = []
		
		// Wilcard certificate
		if(subject.contains('*')) {
			valid = false
			certerrors.add("fedreg.template.certificates.validation.wildcard")
		}
		
		// Valid certifying authority
		if(!subject.equals(issuer)) {
			def validca = false
			validca = cryptoService.validateCertificate(certificate);
			
			if(!validca) {
				valid = false
				certerrors.add("fedreg.template.certificates.validation.invalidca")
			}
		}	
		
		render (template:"/templates/certificates/certificatevalidation", model:[corrupt: false, subject:subject, issuer:issuer, expires:expires, valid:valid, certerrors:certerrors])
		}
		catch(Exception e) {
			log.warn "Certificate data is invalid"
			e.printStackTrace()
			render (template:"/templates/certificates/certificatevalidation", model:[corrupt:true])
			return
		}
	}


	def createCertificate = {
		if(!params.id) {
			log.warn "KeyDescriptor ID was not present"
			render message(code: 'fedreg.controllers.namevalue.missing')
			response.sendError(500)
			return
		}
		
		if(!params.cert) {
			log.warn "Certificate data was not present"
			render message(code: 'fedreg.controllers.namevalue.missing')
			response.sendError(500)
			return
		}
		
		def descriptor = RoleDescriptor.get(params.id)
		if (!descriptor) {
			log.warn "Descriptor was not found for id ${params.id}"
			render message(code: 'fedreg.roledescriptor.nonexistant', args: [params.id])
			response.sendError(500)
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
			response.sendError(500)
			return
		}
		
		render message(code: 'fedreg.keydescriptor.create.success')
	}
}