package fedreg.core

class IdentityProviderController {

	static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

	def index = {
		redirect(action: "list", params: params)
	}

	def list = {
		params.max = Math.min(params.max ? params.max.toInteger() : 20, 100)
		[identityProviderList: IDPSSODescriptor.list(params), identityProviderTotal: IDPSSODescriptor.count()]
	}

	def show = {
		def identityProvider = IDPSSODescriptor.get(params.id)
		if (!identityProvider) {
			flash.type="error"
			flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'identityProvider.label'), params.id])}"
			redirect(action: "list")
		}
		else {
			[identityProvider: identityProvider]
		}
	}
	
	def searchContacts = {
		sleep(1000);
		def contacts, email
		if(!params.id) {
			log.warn "RoleDescriptor ID was not supplied"
			render message(code: 'fedreg.roledescriptor.noid')
			response.status = 500
			return
		}
			
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
		[roleDescriptor:RoleDescriptor.get(params.id), contacts:contacts, contactTypes:ContactType.list()]
	}
}
