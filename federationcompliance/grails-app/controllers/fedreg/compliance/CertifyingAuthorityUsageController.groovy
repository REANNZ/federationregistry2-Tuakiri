package fedreg.compliance

import fedreg.core.Certificate

/**
 * Reports compliance to approved CA's across RoleDescriptors
 *
 * @author Bradley Beddoes
 */
class CertifyingAuthorityUsageController {
	
	def cryptoService
	
	def index = {
		def ca = [:]
		Certificate.list().each { cert ->
			def subject = cryptoService.subject(cert)
			def issuer = cryptoService.issuer(cert)
			
			// External CA only
			if(issuer != subject){
				def members = ca.get(issuer)
				if(members) {
					if(!members.contains(cert.keyInfo.keyDescriptor.roleDescriptor.entityDescriptor))
						members.add(cert.keyInfo.keyDescriptor.roleDescriptor.entityDescriptor)
				}
				else {
					ca.put(issuer,[cert.keyInfo.keyDescriptor.roleDescriptor.entityDescriptor])
				}
			}
		}

		[causage:ca.sort{-it.value.size()}]
	}
	
}