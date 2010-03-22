package fedreg.core

class ContactsController {

	static allowedMethods = [search: "POST"]

	def index = {
		redirect(action: "list", params: params)
	}

	def list = {}
	
	
}