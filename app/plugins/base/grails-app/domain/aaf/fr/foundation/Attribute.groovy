package aaf.fr.foundation

/**
 * @author Bradley Beddoes
 */
class Attribute  {
	static auditable = true
	
	AttributeBase base
	Date dateCreated
	Date lastUpdated
	
	static hasMany = [
		values: AttributeValue
	]
	
	static belongsTo = [idpSSODescriptor: IDPSSODescriptor, attributeAuthorityDescriptor:AttributeAuthorityDescriptor]
	
	static constraints = {
		idpSSODescriptor(nullable:true)
		attributeAuthorityDescriptor(nullable:true)
		base(nullable:false)
	}
	
	public String toString() {	"attribute:[id:$id]" }
}