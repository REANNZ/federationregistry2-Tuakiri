package aaf.fr.foundation

import org.apache.shiro.SecurityUtils

/**
 * Provides contact management views for Organizations.
 *
 * @author Bradley Beddoes
 */
class OrganizationContactController {
	def allowedMethods = [create:'POST', delete:'DELETE']
	
	def search = {
		def contacts
			
		if(!params.givenName && !params.surname && !params.email)
			contacts = Contact.list()
		else {
			def c = Contact.createCriteria()
			contacts = c.list {
				or {
					ilike("givenName", params.givenName)
					ilike("surname", params.surname)
                    if(params.email)
					   ilike("email", "%${params.email}%".toString())
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
		
		def organization = Organization.get(params.id)
		if(!organization) {
			log.warn "Organization identified by id $params.id was not located"
			render message(code: 'fedreg.roleorganization.nonexistant', args: [params.id])
			response.setStatus(500)
			return
		}
		
		if(SecurityUtils.subject.isPermitted("organization:${organization.id}:contact:add") || SecurityUtils.subject.isPermitted("federation:management:contacts")) {
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
		
			def contactPerson = new ContactPerson(contact:contact, type:contactType, organization: organization)			
			contactPerson.save()
			if(contactPerson.hasErrors()) {
				log.debug "$subject failed to create $contactPerson linked to $contact for $organization"
				contactPerson.errors.each {
					log.error it
				}
				render message(code: 'fedreg.contactperson.create.error')
				response.setStatus(500)
				return
			}
			
			log.debug "$subject created $contactPerson linked to $contact for $organization"
			render message(code: 'fedreg.contactperson.create.success')
		}
		else {
			log.warn("Attempt to link contact to $organization by $subject was denied, incorrect permission set")
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
		
		def organization = contactPerson.organization
		if(SecurityUtils.subject.isPermitted("organization:${organization.id}:contact:remove") || SecurityUtils.subject.isPermitted("federation:management:contacts")) {
			contactPerson.delete();
			
			log.debug "$subject deleted $contactPerson from $organization"
			render message(code: 'fedreg.contactperson.delete.success')
		}
		else {
			log.warn("Attempt to remove $contactPerson from organization $id by $subject was denied, incorrect permission set")
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
		
		def organization = Organization.get(params.id)
		if(!organization) {
			log.warn "Organization identified by id $params.id was not located"
			render message(code: 'fedreg.roleorganization.nonexistant', args: [params.id])
			response.sendError(500)
			return
		}
		
		render template:"/templates/contacts/list", contextPath: pluginContextPath, model:[host:organization]
	}
}