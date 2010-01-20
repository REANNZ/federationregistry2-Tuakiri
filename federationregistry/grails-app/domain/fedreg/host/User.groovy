package fedreg.host

import fedreg.saml2.metadata.orm.EntityDescriptor

class User extends grails.plugin.nimble.core.UserBase {

	// Extend UserBase with your custom values here
	EntityDescriptor entityDescriptor
}
