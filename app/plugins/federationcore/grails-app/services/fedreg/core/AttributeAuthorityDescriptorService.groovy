package fedreg.core

/**
 * Provides methods for managing AttributeAuthorityDescriptor instances.
 *
 * @author Bradley Beddoes
 */
class AttributeAuthorityDescriptorService {

	def delete(def id) {
		def aa = AttributeAuthorityDescriptor.get(id)
		if(!aa)
			throw new ErronousStateException("Unable to delete attribute authority, no such instance")
		
		if(aa.collaborator)
			throw new ErronousStateException("Unable to delete attribute authority linked to collaborator. Delete collaborator ${aa.collaborator} instead or manually remove linkage first")
		
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