package fedreg.core

import org.apache.shiro.SecurityUtils

class ContactsController {
	def index = {
		redirect(action: "list", params: params)
	}

	def list = {
		params.max = Math.min(params.max ? params.max.toInteger() : 20, 100)
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
		if(SecurityUtils.subject.isPermitted("contact:create")) {
			def contact = new Contact()
			[contact: contact]
		} else {
			log.warn("Attempt to create new contact by $authenticatedUser was denied, incorrect permission set")
			response.sendError(403)
		}
	}

	def save = {
		if(SecurityUtils.subject.isPermitted("contact:create")) {
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
			
			contact.save()
			if(contact.hasErrors()) {
				contact.errors.each {
					log.warn it
				}
				flash.type = "error"
			    flash.message = message(code: 'fedreg.contact.create.error')
				render view: "create", model: [contact: contact]
				return
			}
	
			flash.type = "success"
		    flash.message = message(code: 'fedreg.contact.create.success')
			redirect action: "show", id: contact.id
		} else {
			log.warn("Attempt to create new contact by $authenticatedUser was denied, incorrect permission set")
			response.sendError(403)
		}
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
			[contact: contact]
		}	else {
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
	
			if(params.secondaryEmail)
				if(!contact.secondaryEmail)
					contact.secondaryEmail = new MailURI(uri:params.secondaryEmail)
				else
					contact.secondaryEmail.uri = params.secondaryEmail
			else 
				if(params.secondaryEmail)
					params.secondaryEmail.delete()
	
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
				render view: "edit", model: [contact: contact]
				return
			}
	
			flash.type = "success"
		    flash.message = message(code: 'fedreg.contact.update.success')
			redirect action: "show", id: contact.id
		} else {
			log.warn("Attempt to update ${contact} by $authenticatedUser was denied, incorrect permission set")
			response.sendError(403)
		}
	}

}