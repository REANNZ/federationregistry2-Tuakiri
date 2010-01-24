package fedreg.host

import fedreg.core.Organization

class Profile extends grails.plugins.nimble.core.ProfileBase {

	// Extend ProfileBase with your custom values here
	Organization organization
	
	static constraints = {
		organization(nullable:true)
	}

}
