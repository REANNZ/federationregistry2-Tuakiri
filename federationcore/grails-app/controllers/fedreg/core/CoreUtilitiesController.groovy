package fedreg.core

import fedreg.workflow.ProcessPriority

class CoreUtilitiesController {
	
	def grailsApplication
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
			def certificate = cryptoService.createCertificate(params.cert.trim().normalize())
			def subject = cryptoService.subject(certificate);
			def issuer = cryptoService.issuer(certificate);
			def expires = cryptoService.expiryDate(certificate);
			
			// TODO extend additional validation checks over time so we don't get stupid certs being entered.
			def valid = true
			def certerrors = []
		
			// Wilcard certificate
			if(subject.contains('*')) {
				valid = false
				certerrors.add("fedreg.templates.certificates.validation.wildcard")
				log.warn "Certificate contains wildcard"
			}

			// CN has hostname as value
			def matcher =  subject =~ /^(?:.*,)*[cC][nN]=([^,]+)(?:,.*)*$/
			if(matcher.matches()) {
				def cn = matcher[0][1]
				log.debug "Certificate CN is $cn and entity is ${params.entity}"
				if(!params.entity.contains(cn)) {
					valid = false
					certerrors.add("fedreg.templates.certificates.validation.subject.doesnot.contain.host")
					log.warn "Certificate CN does not contain hostname"
				}
			} else {
				certerrors.add("fedreg.templates.certificates.validation.subject.doesnot.contain.cn")
				log.warn "Certificate CN does not exist"
			}
			
			// Max validity matches configured allowable value
			def today = new Date()
			def maxValidDate = today + grailsApplication.config.fedreg.certificates.maxlifeindays
			if(expires.after(maxValidDate)) {
				valid = false
				certerrors.add("fedreg.templates.certificates.validation.expiry.tolong")
				log.warn "Certificate exceeds max time period for validity"
			}
		
			// Valid certifying authority
			def validca = false
			validca = cryptoService.validateCertificate(certificate);
		
			if(!validca) {
				valid = false
				certerrors.add("fedreg.templates.certificates.validation.invalidca")
				log.warn "Certificate requires CA and can't be verified"
			}
			
			render template:"/templates/certificates/validation",  contextPath: pluginContextPath, model:[corrupt: false, subject:subject, issuer:issuer, expires:expires, valid:valid, certerrors:certerrors]
			if(!valid)
				response.setStatus(500)	
		}
		catch(Exception e) {
			log.debug e
			log.warn "Certificate data is invalid"
			render template:"/templates/certificates/validation", contextPath: pluginContextPath, model:[corrupt:true]
			response.setStatus(500)
			return
		}
	}
	
}