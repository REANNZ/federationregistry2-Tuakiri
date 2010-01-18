package fedreg.core

class Organization  extends aaf.fedreg.saml2.metadata.orm.Organization {
	
	OrganizationType primary
	
	static hasMany = [
		types : OrganizationType
	]
	
	static constraints = {
		types(nullable:true)
	}

}