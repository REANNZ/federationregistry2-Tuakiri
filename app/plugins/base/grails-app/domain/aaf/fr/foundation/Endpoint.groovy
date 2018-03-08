package aaf.fr.foundation

/**
 * While not marked explicitly due to GORM issues both Endpoint and IndexedEndpoint are considered 'Abstract'
 *
 * @author Bradley Beddoes
 */
abstract class Endpoint	{
	static auditable = true
	
	boolean active = false
	boolean approved = false
	
	SamlURI binding
	String location
	String responseLocation

	Date dateCreated
	Date lastUpdated

	static mapping = {
		tablePerHierarchy false
	}

	static constraints = {
		binding(nullable: false)
		location(nullable: false, url:true)
		responseLocation(nullable: true, url:true)
		dateCreated(nullable:true)
		lastUpdated(nullable:true)
	}

	public String toString() {	"endpoint:[id:$id, location: $location]" }
	
	// This method must be overloaded by all subclasses
	public boolean functioning() {
		false
	}

	public boolean exposed() {
		( active && approved )
	}

}
