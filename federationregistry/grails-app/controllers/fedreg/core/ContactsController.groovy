package fedreg.core

class ContactsController {

	static allowedMethods = []

	def index = {
		redirect(action: "list", params: params)
	}

	def list = {
		params.max = Math.min(params.max ? params.max.toInteger() : 20, 100)
		[contactList: Contact.list(params), contactTotal: Contact.count()]
	}
	
	def show = {
		if(!params.id) {
			log.warn "Contact id was not present"
			render message(code: 'fedreg.controllers.namevalue.missing')
			redirect action:list
			return
		}
		
		def contact = Contact.get(params.id)
		if (!contact) {
			flash.type="error"
			flash.message = message(code: 'fedreg.contact.nonexistant', args: [params.id])
			redirect(action: "list")
			return
		}

		[contact: contact]
	}
	
	// AJAX Bound
	def searchContacts = {
		def contacts, email
			
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
		render(template:"/templates/contacts/contactresults", model:[contacts:contacts])
	}
	
	def linkDescriptorContact = {
		if(!params.id || !params.contactID || !params.contactType) {
			log.warn "All name/value pairs required for this call were not present"
			render message(code: 'fedreg.controllers.namevalue.missing')
			response.sendError(500)
			return
		}
		
		def descriptor = RoleDescriptor.get(params.id)
		if(!descriptor) {
			log.warn "RoleDescriptor identified by id $params.id was not located"
			render message(code: 'fedreg.roledescriptor.nonexistant', args: [params.id])
			response.sendError(500)
			return
		}
		
		def contact = Contact.get(params.contactID)
		if(!contact) {
			log.warn "Contact identified by id $params.contactID was not located"
			render message(code: 'fedreg.contact.nonexistant', args: [params.contactID])
			response.sendError(500)
			return
		}
		
		def contactType = ContactType.findByName(params.contactType)
		if(!contactType) {
			log.warn "ContactType identified by id $params.contactType was not located"
			render message(code: 'fedreg.contacttype.nonexistant', args: [params.contactID])
			response.sendError(500)
			return
		}
		
		log.debug "Creating contactType ${params.contactType} linked to contact ${contact.email.uri} for descriptor ${descriptor.displayName}"
		
		def contactPerson = new ContactPerson(contact:contact, type:contactType, descriptor: descriptor)
		contactPerson.save()
		if(contactPerson.hasErrors()) {
			contactPerson.errors.each {
				log.error it
			}
			render message(code: 'fedreg.contactperson.create.error')
			response.sendError(500)
			return
		}
		
		render message(code: 'fedreg.contactperson.create.success')
	}
	
	def unlinkDescriptorContact = {
		if(!params.id) {
			log.warn "All name/value pairs required for this call were not present"
			render message(code: 'fedreg.controllers.namevalue.missing')
			response.sendError(500)
			return
		}
		
		def contactPerson = ContactPerson.get(params.id)
		if(!contactPerson) {
			log.warn "ContactPerson identified by id $params.id was not located"
			render message(code: 'fedreg.contactperson.nonexistant', args: [params.id])
			response.sendError(500)
			return
		}
		
		contactPerson.delete();
		render message(code: 'fedreg.contactperson.delete.success')
	}
	
	def listDescriptorContacts = {
		if(!params.id) {
			log.warn "All name/value pairs required for this call were not present"
			render message(code: 'fedreg.controllers.namevalue.missing')
			response.sendError(500)
			return
		}
		
		def descriptor = RoleDescriptor.get(params.id)
		if(!descriptor) {
			log.warn "RoleDescriptor identified by id $params.id was not located"
			render message(code: 'fedreg.roledescriptor.nonexistant', args: [params.id])
			response.sendError(500)
			return
		}
		
		render(template:"/templates/contacts/contactlist", model:[descriptor:descriptor, allowremove:params.allowremove?:true])
	}
}