package aaf.fr.foundation

/**
 * @author Bradley Beddoes
 */
class AdditionalMetadataLocation  {
	static auditable = true

	String uri
	String namespace

	Date dateCreated
	Date lastUpdated
	
	static mapping = {
		sort "uri"
	}
	
	static constraints = {
		dateCreated(nullable:true)
		lastUpdated(nullable:true)
	}
	
	public String toString() {	"additionalmetadatalocation:[id:$id, uri: $uri]" }

}
