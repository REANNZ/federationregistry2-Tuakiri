package fedreg.core

class Organization extends fedreg.saml2.metadata.orm.Organization {
	
	OrganizationType primary
	
	static hasMany = [
		types : OrganizationType,
		entities: Entity
	]
	
	static constraints = {
		types(nullable:true)
	}

}