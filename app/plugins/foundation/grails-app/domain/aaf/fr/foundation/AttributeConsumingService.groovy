package aaf.fr.foundation

import org.apache.commons.lang.builder.EqualsBuilder
import org.apache.commons.lang.builder.HashCodeBuilder

/**
 * @author Bradley Beddoes
 */
class AttributeConsumingService {
	static auditable = true

	boolean isDefault = false
	boolean approved = true
	String lang
	
	Date dateCreated
	Date lastUpdated

	static hasMany = [
    	serviceNames: String,
	    serviceDescriptions: String,
	    requestedAttributes: RequestedAttribute
	]

	static constraints = {
		serviceDescriptions(nullable: true)
		dateCreated(nullable:true)
		lastUpdated(nullable:true)
	}
	
	static belongsTo = [descriptor: SPSSODescriptor]
	
	static mapping = {
		index column: "mdindex" // Avoid DB collisions with reserved word index
	}
	
	public String toString() {	"attributeconsumingservice:[id:$id, name: $serviceNames]" }
	
	public List sortedAttributes() {
		def c = RequestedAttribute.createCriteria()
		def attributeList = c.list {
			eq("attributeConsumingService", this)
			createAlias("base","_base")
			order("_base.category", "asc")
			order("_base.name", "asc")
		}
	}
	
	public boolean equals(Object obj) {
		if( this.is(obj) ) return true
		if ( obj == null ) return false
		if ( !obj.instanceOf(AttributeConsumingService) ) return false
		
		AttributeConsumingService rhs = (AttributeConsumingService) obj
		return new EqualsBuilder()
			.isEquals()
	}

	public int hashCode() {
		// hard-coded, randomly chosen, non-zero, odd number different for each class
		return new HashCodeBuilder(25, 153).
		toHashCode();
	}
}
