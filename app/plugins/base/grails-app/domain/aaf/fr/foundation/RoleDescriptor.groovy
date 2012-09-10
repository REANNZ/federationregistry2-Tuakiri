package aaf.fr.foundation

/**
 * While not marked explicitly due to GORM issues RoleDescriptor is considered 'Abstract'
 *
 * @author Bradley Beddoes
 */
abstract class RoleDescriptor extends Descriptor {
	static auditable = true
	
	def cryptoService
	
	Organization organization
	String errorURL
	
	String displayName
	String description
	String extensions
	
	boolean active
	boolean archived = false
	boolean approved = false
	boolean reporting = true

	Date dateCreated
	Date lastUpdated

	static hasMany = [
		contacts: ContactPerson,
		protocolSupportEnumerations: SamlURI,
		keyDescriptors: KeyDescriptor,
		monitors: ServiceMonitor
	]

	static mapping = {
		tablePerHierarchy false
		sort "displayName"
	}

	static constraints = {
		displayName(nullable:false, blank:false)
		description(nullable:false, blank: false, maxSize:2000)
		
		organization(nullable: false)
		extensions(nullable: true, maxSize:2000)
		errorURL(nullable:true)
		protocolSupportEnumerations(nullable: false, minSize:1)
		contacts(nullable: true)
		keyDescriptors(nullable: true)
		dateCreated(nullable:true)
		lastUpdated(nullable:true)
	}
	
	public String toString() { "roledescriptor:[id:$id, displayName: $displayName]" }
	
	// RoleDescriptor is considered abstract but can't be marked as such due to GORM issues
	// This method should be overlaoded by all subclasses
	public boolean functioning() {
		false
	}
}
