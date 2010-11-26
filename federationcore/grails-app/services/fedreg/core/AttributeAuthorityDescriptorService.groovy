package fedreg.core

class AttributeAuthorityDescriptorService {

	def delete(def id) {
		def aa = AttributeAuthorityDescriptor.get(id)
		if(!aa)
			throw new RuntimeException("Unable to delete attribute authority, no such instance")
		
		def idp = aa.collaborator
		def entityDescriptor = aa.entityDescriptor	
		
		if(idp){ // Untangle this linkage - horrible but necessay GORM delete sucks.
			idp.collaborator = null
			if(!idp.save()) {
				idp.errors.each { log.error it }
				log.info "$authenticatedUser falied to delete $aa" 
				throw new RuntimeException("Unable to remove collaborating IDP")
			}
		
			aa.collaborator = null
			if(!aa.save()) {
				aa.errors.each { log.error it }
				log.info "$authenticatedUser falied to delete $aa" 
				throw new RuntimeException("Unable to remove collaborating AA")
			}
		}
		
		aa.attributeServices?.each { it.delete() }
		aa.assertionIDRequestServices?.each { it.delete() }
		aa.attributes?.each { it.delete() }
		aa.contacts?.each { it.delete() }
		aa.keyDescriptors?.each { it.delete() }
		aa.monitors?.each { it.delete() }
		
		entityDescriptor.attributeAuthorityDescriptors.remove(aa)
		
		log.info "$authenticatedUser deleted $aa" 
		aa.delete()
	}

}