package fedreg.host

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
			if(issuer.equals(subject)){
				def members = ss.get(issuer)
				if(!members) {
					// Certificate.KeyInfo.KeyDescriptor.RoleDescriptor[SP|IDP]
					ss.put(issuer,[cert.owner.owner.owner.entityDescriptor])
				}
				else {
					if(!members.contains(cert.owner.owner.owner.entityDescriptor))
						members.add(cert.owner.owner.owner.entityDescriptor)
				}
			}
			else {
				def members = ca.get(issuer)
				if(!members) {
					// Certificate.KeyInfo.KeyDescriptor.RoleDescriptor[SP|IDP]
					ca.put(issuer,[cert.owner.owner.owner.entityDescriptor])
				}
				else {
					if(!members.contains(cert.owner.owner.owner.entityDescriptor))
						members.add(cert.owner.owner.owner.entityDescriptor)
				}
			}
		}
		
		[causage:ca, ssusage:ss]
	}
	
}