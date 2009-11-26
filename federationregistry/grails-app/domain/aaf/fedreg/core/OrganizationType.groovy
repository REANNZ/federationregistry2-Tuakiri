package aaf.fedreg.core

class OrganizationType {

	String name
	String displayName
	String description
	
	static constraints = {
		description(nullable:true)
	}

}