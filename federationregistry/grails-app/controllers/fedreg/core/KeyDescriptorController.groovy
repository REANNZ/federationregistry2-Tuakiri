package fedreg.core

class KeyDescriptorController {

	static allowedMethods = [delete: "POST", validateCertificate:"POST"]
	
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
		
		def invalidca = false
		if(!subject.equals(issuer))		// Not self signed do they use a valid CA?
			invalidca = cryptoService.validateCertificate(certificate);
		
		render (template:"/templates/certificates/certificatevalidation", model:[corrupt: false, subject:subject, issuer:issuer, expires:expires, invalidca:invalidca])
		}
		catch(Exception e) {
			log.warn "Certificate data is invalid"
			log.debug e
			render (template:"/templates/certificates/certificatevalidation", model:[corrupt:true])
			return
		}
	}

}