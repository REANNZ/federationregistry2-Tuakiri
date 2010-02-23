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
		def ss = [:]
		Certificate.list().each { cert ->
			def subject = cryptoService.subject(cert)
			def issuer = cryptoService.issuer(cert)
			if(issuer.equals(subject)){		// Self signed
				def members = ss.get(issuer)
				if(!members) {
					ss.put(issuer,[cert.keyInfo.keyDescriptor.roleDescriptor.entityDescriptor])
				}
				else {
					if(!members.contains(cert.keyInfo.keyDescriptor.roleDescriptor.entityDescriptor))
						members.add(cert.keyInfo.keyDescriptor.roleDescriptor.entityDescriptor)
				}
			}
			else {		// External CA
				def members = ca.get(issuer)
				if(!members) {
					ca.put(issuer,[cert.keyInfo.keyDescriptor.roleDescriptor.entityDescriptor])
				}
				else {
					if(!members.contains(cert.keyInfo.keyDescriptor.roleDescriptor.entityDescriptor))
						members.add(cert.keyInfo.keyDescriptor.roleDescriptor.entityDescriptor)
				}
			}
		}
		// cheeky reverse sort ;)
		[causage:ca.sort{-it.value.size()}, ssusage:ss]
	}
	
}