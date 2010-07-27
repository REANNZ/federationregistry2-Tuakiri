package fedreg.host

import fedreg.core.EntityDescriptor
import fedreg.core.Contact

class User extends grails.plugins.nimble.core.UserBase {

	EntityDescriptor entityDescriptor
	Contact contact
	
	static constraints = {
		entityDescriptor(nullable:true)
		contact(nullable: true)
	}
	
}
