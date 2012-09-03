package aaf.fr.foundation

/**
 * @author Bradley Beddoes
 */
class ServiceDescription {
	static auditable = true
	
	boolean publish = false
	
	String connectURL		// URL for end users to access service
	String logoURL			// URL to image of the services logo
	
	String furtherInfo		// Further information URL
	String provides			// What the Service Provides
	String benefits			// Benefits of using the service
	String audience			// Intended audience
	String restrictions		// Usage restrictions
	String accessing		// Accessing the service
	String support			// Available support
	String maintenance		// Maintenance periods, generally expected unavailability
	
	Date dateCreated
	Date lastUpdated
	
	static belongsTo = [descriptor:SPSSODescriptor]
	
	static mapping = {
		furtherInfo type:"text"
		provides type:"text"
		benefits type:"text"
		audience type:"text"
		restrictions type:"text"
		accessing type:"text"
		support type:"text"
		maintenance type:"text"
		furtherInfo type:"text"
		furtherInfo type:"text"
	}
	
	static constraints = {
		connectURL(nullable:true)
		logoURL(nullable:true)
		furtherInfo(nullable:true)
		provides(nullable:true)
		benefits(nullable:true)
		audience(nullable:true)
		restrictions(nullable:true)
		accessing(nullable:true)
		support(nullable:true)
		maintenance(nullable:true)
		dateCreated(nullable:true)
		lastUpdated(nullable:true)
	}
	
	public String toString() {	"servicedescription:[id:$id, publish:$publish]" }
}