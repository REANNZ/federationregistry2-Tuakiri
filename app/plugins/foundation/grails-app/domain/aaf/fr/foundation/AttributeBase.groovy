package aaf.fr.foundation

import org.apache.commons.lang.builder.EqualsBuilder
import org.apache.commons.lang.builder.HashCodeBuilder

/**
 * @author Bradley Beddoes
 */
class AttributeBase  {
  static auditable = true

  String name
  String legacyName
  SamlURI nameFormat
  
  String oid
  String description
  
  AttributeCategory category
  
  boolean adminRestricted = false   // Only provided to administrative users for registration into SP request
  boolean specificationRequired = false // Generally used for something like Entitlement where values are potentially huge and privacy leaking. 
                                        // This will force service providers to manually specify the set of values they require to operate 
                                        // Potentially future extented to IDPSSODescriptor management to indicate the set supported
  
  Date dateCreated
  Date lastUpdated

  static mapping = {
    autoImport false
  }

  static constraints = {
    name(nullable: false, blank: false, unique: true)
    legacyName(nullable:false, blank:false)
    oid(nullable: false, blank:false)
    description (nullable: false, blank:false)
    nameFormat(nullable: false) 
    dateCreated(nullable:true)
    lastUpdated(nullable:true)
  }
  
  public String toString() {  "attributebase:[id:$id, name: $name, legacyName: $legacyName]" }
  
  public boolean equals(Object obj) {
    if( this.is(obj) ) return true
    if ( obj == null ) return false
    if ( !obj.instanceOf(AttributeBase) ) return false
    
    AttributeBase rhs = (AttributeBase) obj;
    return new EqualsBuilder()
      .append(name, rhs.name)
      .append(oid, rhs.oid)
      .isEquals()
  }

  public int hashCode() {
    // hard-coded, randomly chosen, non-zero, odd number different for each class
    return new HashCodeBuilder(17, 187).
    append(name).
    append(oid).
    toHashCode()
  }
}
