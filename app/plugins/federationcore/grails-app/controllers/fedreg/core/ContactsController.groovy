package fedreg.core

import org.apache.shiro.SecurityUtils

/**
 * Provides Contact views.
 *
 * @author Bradley Beddoes
 */
class ContactsController {
	static defaultAction = "index"
	def allowedMethods = [save: 'POST', update: 'PUT']
	
	def list = {
		[contactList: Contact.list(params), contactTotal: Contact.count()]
	}

	def show = {
		if(!params.id) {
			log.warn "Contact ID was not present"
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

	def create = {
		def contact = new Contact()
		def organizations = Organization.list()
		[contact: contact, organizations:organizations]
	}

	def save = {
		def contact = new Contact()

		contact.givenName = params.givenname
		contact.surname = params.surname
		contact.email = new MailURI(uri:params.email)
		contact.description = params.description

		if(params.secondaryEmail)
			contact.secondaryEmail = new MailURI(uri:params.secondaryEmail)

		if(params.workPhone)
			contact.workPhone = new TelNumURI(uri:params.workPhone)

		
		if(params.mobilePhone)
			contact.mobilePhone = new TelNumURI(uri:params.mobilePhone)
		
		if(params.homePhone)
			contact.homePhone = new TelNumURI(uri:params.homePhone)
			
		if(params.organization) {
			if(params.organization == "null") {
				contact.organization = null
			} else {
				def organization = Organization.get(params.organization)
				if(organization) {
					contact.organization = organization
				}
			}
		}
		
		contact.save()
		if(contact.hasErrors()) {
			contact.errors.each {
				log.warn it
			}
			flash.type = "error"
		    flash.message = message(code: 'fedreg.contact.create.error')
			def organizations = Organization.list()
			render view: "create", model: [contact: contact, organizations:organizations]
			return
		}

		flash.type = "success"
	    flash.message = message(code: 'fedreg.contact.create.success')
		log.info "$authenticatedUser created $contact"
		redirect action: "show", id: contact.id
	}

	def edit = {
		if(!params.id) {
			log.warn "Contact ID was not present"
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
		
		if(SecurityUtils.subject.isPermitted("contact:${contact.id}:update")) {
			def organizations = Organization.list()
			[contact: contact, organizations:organizations]
		} else {
			log.warn("Attempt to edit ${contact} by $authenticatedUser was denied, incorrect permission set")
			response.sendError(403)
		}
	}

	def update = {
		if(!params.id) {
			log.warn "Contact ID was not present"
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

		if(SecurityUtils.subject.isPermitted("contact:${contact.id}:update")) {
			contact.givenName = params.givenname
			contact.surname = params.surname
			contact.email.uri = params.email
			contact.description = params.description
			
			if(params.organization) {
				if(params.organization == "null") {
					contact.organization = null
				} else {
					def organization = Organization.get(params.organization)
					if(organization) {
						contact.organization = organization
					}
				}
			}
	
			if(params.secondaryEmail)
				if(!contact.secondaryEmail)
					contact.secondaryEmail = new MailURI(uri:params.secondaryEmail)
				else
					contact.secondaryEmail.uri = params.secondaryEmail
			else 
				if(contact.secondaryEmail)
					contact.secondaryEmail.delete()
	
			if(params.workPhone)
				if(!contact.workPhone)
					contact.workPhone = new TelNumURI(uri:params.workPhone)
				else
					contact.workPhone.uri = params.workPhone
			else 
				if(contact.workPhone) {
					contact.workPhone.delete()
					contact.workPhone = null
				}
			
			if(params.mobilePhone)
				if(!contact.mobilePhone)
					contact.mobilePhone = new TelNumURI(uri:params.mobilePhone)
				else
					contact.mobilePhone.uri = params.mobilePhone
			else 
				if(contact.mobilePhone) {
					contact.mobilePhone.delete()
					contact.mobilePhone = null
				}
			
			if(params.homePhone)
				if(!contact.homePhone)
					contact.homePhone = new TelNumURI(uri:params.homePhone)
				else 
					contact.homePhone.uri = params.homePhone
			else 
				if(contact.homePhone) {
					contact.homePhone.delete()
					contact.homePhone = null
				}
			
			contact.save()
			if(contact.hasErrors()) {
				contact.errors.each {
					log.warn it
				}
				flash.type = "error"
			    flash.message = message(code: 'fedreg.contact.update.error')
				def organizations = Organization.list()
				render view: "edit", model: [contact: contact, organizations:organizations]
				return
			}
	
			flash.type = "success"
		    flash.message = message(code: 'fedreg.contact.update.success')
			log.info "$authenticatedUser updated $contact"
			redirect action: "show", id: contact.id
		} else {
			log.warn("Attempt to update ${contact} by $authenticatedUser was denied, incorrect permission set")
			response.sendError(403)
		}
	}

}