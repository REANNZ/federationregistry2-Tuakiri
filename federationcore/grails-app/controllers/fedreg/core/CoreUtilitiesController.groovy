package fedreg.core

import fedreg.workflow.ProcessPriority

class CoreUtilitiesController {
	
	def cryptoService
	
	static allowedMethods = [validate: "POST"]
	
	def validateCertificate = {
		if(!params.cert || params.cert.length() == 0) {
			log.warn "Certificate data was not present"
			render template:"/templates/certificates/validation", contextPath: pluginContextPath, model:[corrupt:true]
			response.setStatus(500)
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
			render template:"/templates/certificates/validation",  contextPath: pluginContextPath, model:[corrupt: false, subject:subject, issuer:issuer, expires:expires, valid:valid, certerrors:certerrors]
		}
		catch(Exception e) {
			log.warn "Certificate data is invalid"
			render template:"/templates/certificates/validation", contextPath: pluginContextPath, model:[corrupt:true]
			response.setStatus(500)
			return
		}
	}
	
}