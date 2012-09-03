package aaf.fr.foundation

import org.apache.commons.lang.builder.EqualsBuilder
import org.apache.commons.lang.builder.HashCodeBuilder

class AttributeCategory {
	static auditable = true
	
	String name
	
	Date dateCreated
	Date lastUpdated

    static constraints = {
		name (blank:false)
		dateCreated(nullable:true)
		lastUpdated(nullable:true)
	}

	public String toString() {	"attributecategory:[id:$id, name: $name]" }

  public boolean equals(Object obj) {
    if( this.is(obj) ) return true
    if ( obj == null ) return false
    if ( !obj.instanceOf(AttributeCategory) ) return false
    
    AttributeCategory rhs = (AttributeCategory) obj;
    return new EqualsBuilder()
      .append(name, rhs.name)
      .isEquals()
  }

  public int hashCode() {
    // hard-coded, randomly chosen, non-zero, odd number different for each class
    return new HashCodeBuilder(33, 123).
    append(name).
    toHashCode()
  }
}
