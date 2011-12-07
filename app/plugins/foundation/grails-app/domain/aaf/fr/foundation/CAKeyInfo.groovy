package aaf.fr.foundation

/**
 * @author Bradley Beddoes
 */
class CAKeyInfo  {
	static auditable = true

	String keyName
	Date expiryDate
	CACertificate certificate
	
	Date dateCreated
	Date lastUpdated

	static constraints = {
		keyName(nullable: true, blank: true)
		expiryDate(nullable: true)
		dateCreated(nullable:true)
		lastUpdated(nullable:true)
	}
	
	public String toString() {	"cakeyinfo:[id:$id, keyname: $keyName]" }
}
