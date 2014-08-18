package aaf.fr.foundation

import org.apache.commons.lang.builder.EqualsBuilder
import org.apache.commons.lang.builder.HashCodeBuilder

class OrganizationType {
	static auditable = true

	String name
	String displayName
	String description

	Date dateCreated
	Date lastUpdated

	boolean discoveryServiceCategory = false

	static constraints = {
		name(unique: true, blank:false)
    displayName(unique: true, blank:false)
		description(nullable:false, blank:false)
		dateCreated(nullable:true)
		lastUpdated(nullable:true)
	}

	public String toString() {	"organizationtype:[id:$id, name:$name, displayName: $displayName]" }

	public boolean equals(Object obj) {
		if( this.is(obj) ) return true
		if ( obj == null ) return false
		if ( !obj.instanceOf(OrganizationType) ) return false

		OrganizationType rhs = (OrganizationType) obj;
		return new EqualsBuilder()
			.append(name, rhs.name)
			.isEquals()
	}

	public int hashCode() {
		// hard-coded, randomly chosen, non-zero, odd number different for each class
		return new HashCodeBuilder(17, 187).
		append(name).
		toHashCode()
	}

	def structureAsJson() {
	  def json = new groovy.json.JsonBuilder()
	  json {
	    id id
	    name name
	    display_name displayName
	    description description

	    created_at dateCreated
	    updated_at lastUpdated
	  }
	}

}