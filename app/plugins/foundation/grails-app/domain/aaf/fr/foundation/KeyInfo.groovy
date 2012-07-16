package aaf.fr.foundation

/**
 * @author Bradley Beddoes
 */
class KeyInfo  {
	static auditable = true

	String keyName
	Certificate certificate
	
	Date dateCreated
	Date lastUpdated

	static belongsTo = [keyDescriptor: KeyDescriptor]

	static constraints = {
		keyName(nullable: true, blank: true)
		dateCreated(nullable:true)
		lastUpdated(nullable:true)
	}
	
	static mapping = {
		sort "dateCreated"
	}
	
	public String toString() {	"keyinfo:[id:$id, keyName: $keyName]" }
	
}
