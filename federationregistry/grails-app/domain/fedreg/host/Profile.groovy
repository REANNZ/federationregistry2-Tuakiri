package fedreg.host

import fedreg.core.OrganizationType

class Profile extends grails.plugin.nimble.core.ProfileBase {

	// Extend ProfileBase with your custom values here
	OrganizationType organization
	
	static constraints = {
		organization(nullable:true)
	}

}
