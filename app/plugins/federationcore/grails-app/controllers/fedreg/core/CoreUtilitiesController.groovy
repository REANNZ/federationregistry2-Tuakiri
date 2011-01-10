package fedreg.core

import grails.converters.JSON

class CoreUtilitiesController {
	def allowedMethods = [validateCertificate: 'POST']
	
	def grailsApplication
	def cryptoService
	
	def knownIDPImpl = {
		
		def implementations = [:]
		
		def shib22 = [:]
		shib22.id = "shib22x"
		shib22.displayName = "Shibboleth Identity Provider (2.2.x)"
		shib22.'default' = 'true'
		shib22.entitydescriptor = '$host/idp/shibboleth'
		shib22.post = [:]
		shib22.post.uri = '$host/idp/profile/SAML2/POST/SSO'
		shib22.redirect = [:]
		shib22.redirect.uri = '$host/idp/profile/SAML2/Redirect/SSO'
		shib22.artifact = [:]
		shib22.artifact.uri = '$host/idp/profile/SAML2/SOAP/ArtifactResolution'
		shib22.artifact.index = 1
		shib22.attributeservice = [:]
		shib22.attributeservice.uri = '$host/idp/profile/SAML2/SOAP/AttributeQuery'
		
		implementations.shib22 = shib22
		
		def shib23 = [:]
		shib23.id = "shib23x"
		shib23.displayName = "Shibboleth Identity Provider (2.3.x)"
		shib23.entitydescriptor = '$host/idp/shibboleth'
		shib23.post = [:]
		shib23.post.uri = '$host/idp/profile/SAML2/POST/SSO'
		shib23.redirect = [:]
		shib23.redirect.uri = '$host/idp/profile/SAML2/Redirect/SSO'
		shib23.artifact = [:]
		shib23.artifact.uri = '$host/idp/profile/SAML2/SOAP/ArtifactResolution'
		shib23.artifact.index = 2
		shib23.attributeservice = [:]
		shib23.attributeservice.uri = '$host/idp/profile/SAML2/SOAP/AttributeQuery'
		
		implementations.shib23 = shib23
		
		render implementations as JSON
	}
	
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
			
			def valid = true
			def certerrors = []
			
			log.info "Attempting to validate certificate data:\n$certificate"
		
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
				log.info "Certificate CN is $cn and entity is ${params.entity}"
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
			if(!valid) {
				log.warn "Certificate being marked as invalid, progression will be halted"
				response.setStatus(500)	
			}
		}
		catch(Exception e) {
			log.debug e
			log.warn "Certificate data is invalid"
			render template:"/templates/certificates/validation", contextPath: pluginContextPath, model:[corrupt:true]
			response.setStatus(500)
		}
	}
}