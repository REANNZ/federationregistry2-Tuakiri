package fedreg.core

class ContactsController {

	static allowedMethods = []

	def index = {
		redirect(action: "list", params: params)
	}

	def list = {}
	
	def linkDescriptorContact = {
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
		
		def contactPerson = new ContactPerson(contact:contact, type:contactType, descriptor: descriptor)
		contactPerson.save()
		if(contactPerson.hasErrors()) {
			contactPerson.errors.each {
				log.error it
			}
			render message(code: 'fedreg.contacttype.create.error')
			response.sendError(500)
			return
		}
		
		render message(code: 'fedreg.contacttype.create.success')
	}
}