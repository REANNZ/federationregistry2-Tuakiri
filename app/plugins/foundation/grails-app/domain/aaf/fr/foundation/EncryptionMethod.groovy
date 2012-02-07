package aaf.fr.foundation

/*
 * @author Bradley Beddoes
 */
class EncryptionMethod  {
	static auditable = true

	String algorithm
	String keySize
	String oaeParams
	
	Date dateCreated
	Date lastUpdated

	static belongsTo = [keyDescriptor: KeyDescriptor]

	static constraints = {
		algorithm(nullable:false, blank:false)
		keySize(nullable: true)
		oaeParams(nullable: true)
		dateCreated(nullable:true)
		lastUpdated(nullable:true)
	}

	public String toString() {	"encryptionmethod:[id:$id, algorithm: $algorithm]" }
}
