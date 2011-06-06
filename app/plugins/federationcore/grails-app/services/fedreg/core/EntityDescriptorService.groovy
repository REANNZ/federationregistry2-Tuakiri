package fedreg.core

import org.springframework.transaction.interceptor.TransactionAspectSupport

import grails.plugins.nimble.core.UserBase
import grails.plugins.nimble.core.Role
import grails.plugins.nimble.core.LevelPermission

/**
 * Provides methods for managing EntityDescriptor instances.
 *
 * @author Bradley Beddoes
 */
class EntityDescriptorService {

	def grailsApplication
	def workflowProcessService
	def roleService
	def permissionService
	
	// This is primarily to avoid an issue where cross service calls with a save
	// would not rollback if the calling service laters throws an exception.... weird.
	def createNoSave(def params) {
		// Organization
		def organization = Organization.get(params.organization?.id)
	
		// Entity Descriptor
		def entityDescriptor = new EntityDescriptor(approved:false, active: params.active, entityID: params.entity?.identifier, organization: organization)
		def entContactPerson = new ContactPerson(contact:contact, type:ContactType.findByName(ct))
		entityDescriptor.addToContacts(entContactPerson)

		if(!entityDescriptor.validate()) {			
			entityDescriptor.errors.each {log.warn it}
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly() 
			return [false, entityDescriptor]
		}
		
		log.info "$authenticatedUser created $entityDescriptor (not yet persisted)"
		return [true, entityDescriptor]
	}
	
	def create(def params) {
		def (created, entityDescriptor) = createNoSave(params)
		
		if(!created)
			return [false, entityDescriptor]
			
		// We approved directly created entity descriptors as only folks with higher privs should be allowed to do this
		entityDescriptor.approved = true
		entityDescriptor.active = true
	
		def savedEntityDescriptor = entityDescriptor.save()
		if(!savedEntityDescriptor) {
			entityDescriptor.errors.each {log.warn it}
			throw new ErronousStateException("Unable to save when creating ${entityDescriptor}")
		}
		
		def adminRole = roleService.createRole("descriptor-${savedEntityDescriptor.id}-administrators", "Global administrators for the entity ${savedEntityDescriptor.entityID}", false)
		LevelPermission permission = new LevelPermission()
	    permission.populate("descriptor", "${savedEntityDescriptor.id}", "*", null, null, null)
	    permission.managed = false
		permissionService.createPermission(permission, adminRole)
		
		roleService.addMember(authenticatedUser, adminRole)
	
		log.info "$authenticatedUser created $entityDescriptor"
		return [true, entityDescriptor]
	}
	
	def update(def params) {
		def entityDescriptor = EntityDescriptor.get(params.id)
		if(!entityDescriptor)
			return [false, null]
		
		entityDescriptor.entityID = params.entity.identifier
		
		if(!entityDescriptor.validate()) {			
			entityDescriptor.errors.each {log.warn it}
			return [false, entityDescriptor]
		}
		
		if(!entityDescriptor.save()) {			
			entityDescriptor.errors.each {log.warn it}
			throw new ErronousStateException("Unable to save when updating ${entityDescriptor}")
		}
		
		log.info "$authenticatedUser updated $entityDescriptor"
		return [true, entityDescriptor]
	}
	
	def delete (def id) {
		def ed = EntityDescriptor.get(id)
		if(!ed)
			throw new ErronousStateException("Unable to find EntityDescriptor with id $id")
			
		def idpService = grailsApplication.mainContext.IDPSSODescriptorService
		def spService = grailsApplication.mainContext.SPSSODescriptorService
			
		// We need to do this for GORM stupidity. If you delete an IDP (and hence collaborator) then try to process any remaining AA associted with the ED collaborators are still present
		// in the list (regardless of refresh type calls). So for now at least an AA only ED is not processed - more thought needed.
		if(ed.attributeAuthorityDescriptors?.size() > ed.idpDescriptors?.size() || ed.pdpDescriptors?.size() != 0)
			throw new ErronousStateException("EntityDescriptor $ed holds unique combination of IDP/SP/AA/PDP that is not supported by this delete method, manual intervention will be required")
			
		ed.idpDescriptors.each { idpService.delete(it.id) }
		ed.spDescriptors.each { spService.delete(it.id)}
		ed.contacts.each { it.delete() }
		
		ed.delete()
		def users = UserBase.findAllWhere(entityDescriptor:ed)
		users.each { user ->
			user.entityDescriptor = null
			if(!user.save())
				throw new ErronousStateException("Unable to update $user with nil entitydescriptor detail when removing $ed")
		}
		
		log.info "$authenticatedUser deleted $ed"
	}
	
	def archive (def id) {
		def ed = EntityDescriptor.get(id)
		if(!ed)
			throw new ErronousStateException("Unable to find EntityDescriptor with id $id")
			
		def idpService = grailsApplication.mainContext.IDPSSODescriptorService
		def spService = grailsApplication.mainContext.SPSSODescriptorService
		def aaService = grailsApplication.mainContext.attributeAuthorityDescriptorService
			
		ed.idpDescriptors?.each { idpService.archive(it.id) }
		ed.spDescriptors?.each { spService.archive(it.id) }
		ed.attributeAuthorityDescriptors?.each { aaService.archive(it.id) }
		
		ed.archived = true
		ed.active = false
		if(!ed.save()) {
			log.error "Unable to archive $ed"
			ed.errors.each { log.error it }
			throw new ErronousStateException("Unable to archive EntityDescriptor with id $id")
		}
		
		log.info "$authenticatedUser successfully archived $ed"
	}
	
	def unarchive (def id) {
		def ed = EntityDescriptor.get(id)
		if(!ed)
			throw new ErronousStateException("Unable to find EntityDescriptor with id $id")
			
		def idpService = grailsApplication.mainContext.IDPSSODescriptorService
		def spService = grailsApplication.mainContext.SPSSODescriptorService
		def aaService = grailsApplication.mainContext.attributeAuthorityDescriptorService
			
		ed.idpDescriptors?.each { idpService.unarchive(it.id) }
		ed.spDescriptors?.each { spService.unarchive(it.id) }
		ed.attributeAuthorityDescriptors?.each { aaService.unarchive(it.id) }
		
		ed.archived = false
		if(!ed.save()) {
			log.error "Unable to unarchive $ed"
			ed.errors.each { log.error it }
			throw new ErronousStateException("Unable to unarchive EntityDescriptor with id $id")
		}
		
		log.info "$authenticatedUser successfully unarchived $ed"
	}
	
	def activate (def id) {
		def ed = EntityDescriptor.get(id)
		if(!ed)
			throw new ErronousStateException("Unable to find EntityDescriptor with id $id")
			
		def idpService = grailsApplication.mainContext.IDPSSODescriptorService
		def spService = grailsApplication.mainContext.SPSSODescriptorService
		def aaService = grailsApplication.mainContext.attributeAuthorityDescriptorService
			
		ed.idpDescriptors?.each { idpService.activate(it.id) }
		ed.spDescriptors?.each { spService.activate(it.id) }
		ed.attributeAuthorityDescriptors?.each { aaService.activate(it.id) }
		
		ed.active = true
		if(!ed.save()) {
			log.error "Unable to activate $ed"
			ed.errors.each { log.error it }
			throw new ErronousStateException("Unable to activate EntityDescriptor with id $id")
		}
		
		log.info "$authenticatedUser successfully activated $ed"
	}

}