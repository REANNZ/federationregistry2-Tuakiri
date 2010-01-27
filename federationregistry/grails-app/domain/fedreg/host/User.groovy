package fedreg.host

import fedreg.core.EntityDescriptor

class User extends grails.plugins.nimble.core.UserBase {

	EntityDescriptor entityDescriptor
	
	static constraints = {
		entityDescriptor(nullable:true)
	}
	
}
