package aaf.fr.foundation

import org.apache.shiro.SecurityUtils

/**
 * Provides crypto(certificate) management views for Descriptors.
 *
 * @author Bradley Beddoes
 */
class RoleDescriptorCryptoController {
	def allowedMethods = [create:'POST', delete: 'DELETE']
	
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
		
		def descriptor = keyDescriptor.roleDescriptor
		if(SecurityUtils.subject.isPermitted("descriptor:${descriptor.id}:crypto:delete")) {
			log.info "Deleting KeyDescriptor"
			cryptoService.unassociateCertificate(keyDescriptor)
			
			log.info "$subject soft deleted $keyDescriptor from $descriptor"
			render message(code: 'fedreg.keydescriptor.delete.success')
		}
		else {
			log.warn("Attempt to remove $keyDescriptor from ${keyDescriptor.roleDescriptor} by $subject was denied, incorrect permission set")
			response.sendError(403)
		}		
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
		
		if(SecurityUtils.subject.isPermitted("descriptor:${descriptor.id}:crypto:create")) {
			def associated
			if(params.signing == "on") {
				associated = cryptoService.associateCertificate(descriptor, params.cert, params.certname, KeyTypes.signing)
		
				if(!associated) {
					render message(code: 'fedreg.keydescriptor.create.signing.failed')
					response.setStatus(500)
					return
				}
				
				log.info "$subject created signing keyDescriptor for $descriptor"
			}
			
			if(params.encryption == "on") {
				associated = cryptoService.associateCertificate(descriptor, params.cert, params.certname, KeyTypes.encryption)
			
				if(!associated) {
					render message(code: 'fedreg.keydescriptor.create.encryption.failed')
					response.setStatus(500)
					return
				}
				
				log.info "$subject created encryption keyDescriptor for $descriptor"
			}
			
			render message(code: 'fedreg.keydescriptor.create.success')
		}
		else {
			log.warn("Attempt to add crypto to $descriptor by $subject was denied, incorrect permission set")
			response.sendError(403)
		}
	}
}