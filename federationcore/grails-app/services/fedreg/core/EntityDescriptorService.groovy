package fedreg.core

import org.springframework.transaction.interceptor.TransactionAspectSupport

import fedreg.workflow.ProcessPriority

class EntityDescriptorService {

	def workflowProcessService
	
	def create(def params) {
		// Organization
		def organization = Organization.get(params.organization?.id)

		// Contact
		def contact 
		if(params.contact.id)
			contact = Contact.get(params.contact?.id)
			
		if(!contact) {
			if(params.contact?.email)
				contact = MailURI.findByUri(params.contact?.email)?.contact		// We may already have them referenced by email address and user doesn't realize
			if(!contact)
				contact = new Contact(givenName: params.contact?.givenName, surname: params.contact?.surname, email: new MailURI(uri:params.contact?.email), organization:organization)
				contact.save()
		}
		def ct = params.contact?.type ?: 'administrative'
		
		// Entity Descriptor
		def entityDescriptor = new EntityDescriptor(approved:false, active: params.active, entityID: params.entity?.identifier, organization: organization)
		def entContactPerson = new ContactPerson(contact:contact, type:ContactType.findByName(ct))
		entityDescriptor.addToContacts(entContactPerson)
		
		if(!entityDescriptor.validate()) {			
			entityDescriptor.errors.each {log.warn it}
			return [false, entityDescriptor]
		}
		
		if(!entityDescriptor.save()) {			
			entityDescriptor.errors.each {log.warn it}
			throw new RuntimeException("Unable to save when creating ${entityDescriptor}")
		}
		
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
			throw new RuntimeException("Unable to save when updating ${entityDescriptor}")
		}
		
		return [true, entityDescriptor]
	}

}