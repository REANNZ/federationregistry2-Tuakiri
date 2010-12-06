package fedreg.core

import org.apache.shiro.SecurityUtils

class DescriptorContactController {
	def allowedMethods = [create:'POST', delete:'DELETE']
	
	def search = {
		def contacts
			
		if(!params.givenName && !params.surname && !params.email)
			contacts = Contact.list()
		else {
			def emails
			if(params.email)
				emails = MailURI.findAllByUriLike("%${params.email}%")
			def c = Contact.createCriteria()
			contacts = c.list {
				or {
					ilike("givenName", params.givenName)
					ilike("surname", params.surname)
					if(emails)
						'in'("email", emails)
				}
			}
		}
		render template:"/templates/contacts/results", contextPath: pluginContextPath, model:[contacts:contacts]
	}
	
	def create = {
		if(!params.id || !params.contactID || !params.contactType) {
			log.warn "All name/value pairs required for this call were not present"
			render message(code: 'fedreg.controllers.namevalue.missing')
			response.setStatus(500)
			return
		}
		
		def descriptor = Descriptor.get(params.id)
		if(!descriptor) {
			log.warn "RoleDescriptor identified by id $params.id was not located"
			render message(code: 'fedreg.roledescriptor.nonexistant', args: [params.id])
			response.setStatus(500)
			return
		}
		
		if(SecurityUtils.subject.isPermitted("descriptor:${descriptor.id}:contact:add")) {
			def contact = Contact.get(params.contactID)
			if(!contact) {
				log.warn "Contact identified by id $params.contactID was not located"
				render message(code: 'fedreg.contact.nonexistant', args: [params.contactID])
				response.setStatus(500)
				return
			}
		
			def contactType = ContactType.findByName(params.contactType)
			if(!contactType) {
				log.warn "ContactType identified by id $params.contactType was not located"
				render message(code: 'fedreg.contacttype.nonexistant', args: [params.contactID])
				response.setStatus(500)
				return
			}
		
			def contactPerson
			if(descriptor instanceof RoleDescriptor)
				contactPerson = new ContactPerson(contact:contact, type:contactType, descriptor: descriptor)
			else
				contactPerson = new ContactPerson(contact:contact, type:contactType, entity: descriptor)
			
			contactPerson.save()
			if(contactPerson.hasErrors()) {
				log.debug "$authenticatedUser failed to create $contactPerson linked to $contact for $descriptor"
				contactPerson.errors.each {
					log.error it
				}
				render message(code: 'fedreg.contactperson.create.error')
				response.setStatus(500)
				return
			}
			
			log.debug "$authenticatedUser created $contactPerson linked to $contact for $descriptor"
			render message(code: 'fedreg.contactperson.create.success')
		}
		else {
			log.warn("Attempt to link contact to $descriptor by $authenticatedUser was denied, incorrect permission set")
			response.sendError(403)
		}
	}
	
	def delete = {
		if(!params.id) {
			log.warn "All name/value pairs required for this call were not present"
			render message(code: 'fedreg.controllers.namevalue.missing')
			response.setStatus(500)
			return
		}
		
		def contactPerson = ContactPerson.get(params.id)
		if(!contactPerson) {
			log.warn "ContactPerson identified by id $params.id was not located"
			render message(code: 'fedreg.contactperson.nonexistant', args: [params.id])
			response.setStatus(500)
			return
		}
		
		def descriptor 
		if(contactPerson.descriptor)
			descriptor = contactPerson.descriptor
		else
			descriptor = contactPerson.entity
		
		if(SecurityUtils.subject.isPermitted("descriptor:${descriptor.id}:contact:remove")) {
			contactPerson.delete();
			
			log.debug "$authenticatedUser deleted $contactPerson from $descriptor"
			render message(code: 'fedreg.contactperson.delete.success')
		}
		else {
			log.warn("Attempt to remove $contactPerson from descriptor $id by $authenticatedUser was denied, incorrect permission set")
			response.sendError(403)
		}
	}
	
	def list = {
		if(!params.id) {
			log.warn "All name/value pairs required for this call were not present"
			render message(code: 'fedreg.controllers.namevalue.missing')
			response.setStatus(500)
			return
		}
		
		def descriptor = Descriptor.get(params.id)
		if(!descriptor) {
			log.warn "RoleDescriptor identified by id $params.id was not located"
			render message(code: 'fedreg.roledescriptor.nonexistant', args: [params.id])
			response.sendError(500)
			return
		}
		
		render template:"/templates/contacts/list", contextPath: pluginContextPath, model:[descriptor:descriptor, allowremove:params.allowremove?:true]
	}
}