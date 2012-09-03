package aaf.fr.foundation

/**
 * @author Bradley Beddoes
 */
class AttributeValue  {
	static auditable = true

	String value
	
	boolean approved = true
	
	Date dateCreated
	Date lastUpdated

	public String toString() {	"attributevalue:[id:$id, value: $value]" }
}