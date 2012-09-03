package aaf.fr.foundation

import org.apache.commons.lang.builder.EqualsBuilder
import org.apache.commons.lang.builder.HashCodeBuilder

/**
 * @author Bradley Beddoes
 */
class SPSSODescriptor extends SSODescriptor {
	static auditable = true

 	boolean authnRequestsSigned
 	boolean wantAssertionsSigned
  boolean forceAttributesInFilter = false

	ServiceDescription serviceDescription

 	static belongsTo = [entityDescriptor:EntityDescriptor]

	static hasMany = [
		assertionConsumerServices: AssertionConsumerService,
		attributeConsumingServices: AttributeConsumingService,
		discoveryResponseServices: DiscoveryResponseService,
		serviceCategories: ServiceCategory
	]

	static constraints = {
		attributeConsumingServices(nullable: true)
		serviceDescription(nullable:false)
 	}

	public String toString() {	"spssodescriptor:[id:$id, displayName: $displayName]" }
	
	public boolean functioning() {
		( !archived && active && approved && entityDescriptor.functioning() )
	}
	
	public boolean equals(Object obj) {
		if( this.is(obj) ) return true
		if ( obj == null ) return false
		if ( !obj.instanceOf(SPSSODescriptor) ) return false
		
		SPSSODescriptor rhs = (SPSSODescriptor) obj
		return new EqualsBuilder()
			.append(this.id, rhs.id)
			.append(this.displayName, rhs.displayName)
			.append(this.organization, rhs.organization)
			.isEquals()
	}

	public int hashCode() {
		// hard-coded, randomly chosen, non-zero, odd number different for each class
		return new HashCodeBuilder(43, 129).
		toHashCode();
	}
}
