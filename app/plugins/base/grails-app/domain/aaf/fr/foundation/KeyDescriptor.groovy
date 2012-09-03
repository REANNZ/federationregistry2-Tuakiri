package aaf.fr.foundation

/*
 * @author Bradley Beddoes
 */
class KeyDescriptor  {	
	static auditable = true
	
	KeyInfo keyInfo
	EncryptionMethod encryptionMethod
	KeyTypes keyType
	
	boolean disabled = false
	
	Date dateCreated
	Date lastUpdated

	static belongsTo = [roleDescriptor: RoleDescriptor]

	static constraints = {
		encryptionMethod(nullable: true)
		dateCreated(nullable:true)
		lastUpdated(nullable:true)
	}
	
	static mapping = {
		sort "dateCreated"
	}
	
	public String toString() {	"keydescriptor:[id:$id]" }

}

enum KeyTypes {
	encryption, signing
}
