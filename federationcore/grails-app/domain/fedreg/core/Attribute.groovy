package fedreg.core

class Attribute extends fedreg.saml2.metadata.orm.Attribute {

	String oid
	String headerName
	String alias
	String description
	
	AttributeScope scope
	AttributeCategory category
	
	static mapping = {
		autoImport false
	}

    static constraints = {
		oid (blank:false)
		headerName (blank:false)
		alias (blank:false)
		description (blank:false)
    }

    public String toString() {
		return friendlyName
	}
}
