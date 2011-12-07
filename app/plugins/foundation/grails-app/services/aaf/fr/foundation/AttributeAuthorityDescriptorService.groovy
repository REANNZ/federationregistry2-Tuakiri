package aaf.fr.foundation

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
	
	def archive(long id) {
		def aa = AttributeAuthorityDescriptor.get(id)
		if(!aa)
			throw new ErronousStateException("Unable to find AttributeAuthorityDescriptor with id $id")

		aa.archived = true
		aa.active = false
		if(!aa.save()) {
			log.error "Unable to archive $aa"
			aa.errors.each { log.error it }
			throw new ErronousStateException("Unable to archive AttributeAuthorityDescriptor with id $id")
		}
		
		log.info "$authenticatedUser successfully archived $aa"
	}

	def unarchive(long id) {
		def aa = AttributeAuthorityDescriptor.get(id)
		if(!aa)
			throw new ErronousStateException("Unable to find AttributeAuthorityDescriptor with id $id")

		aa.archived = false
		if(!aa.save()) {
			log.error "Unable to unarchive $aa"
			aa.errors.each { log.error it }
			throw new ErronousStateException("Unable to unarchive AttributeAuthorityDescriptor with id $id")
		}
		
		log.info "$authenticatedUser successfully unarchived $aa"
	}
	
	def activate(long id) {
		def aa = AttributeAuthorityDescriptor.get(id)
		if(!aa)
			throw new ErronousStateException("Unable to find AttributeAuthorityDescriptor with id $id")

		aa.active = true
		if(!aa.save()) {
			log.error "Unable to activate $aa"
			aa.errors.each { log.error it }
			throw new ErronousStateException("Unable to activate AttributeAuthorityDescriptor with id $id")
		}
		
		log.info "$authenticatedUser successfully activate $aa"
	}
}