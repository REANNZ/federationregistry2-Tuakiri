/*
 *  A Grails/Hibernate compatible environment for SAML2 metadata types with application specific 
 *  data extensions as appropriate.
 * 
 *  Copyright (C) 2010 Australian Access Federation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package fedreg.core

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
	
	boolean adminRestricted = false		// Only provided to administrative users for registration into SP request
	boolean specificationRequired = false	// Generally used for something like Entitlement where values are potentially huge and privacy leaking. 
											// This will force service providers to manually specify the set of values they require to operate 
											// Potentially future extented to IDPSSODescriptor management to indicate the set supported
	
	Date dateCreated
	Date lastUpdated

	static mapping = {
		autoImport false
	}

	static constraints = {
		name(nullable: false, blank: false, unique: true)
		legacyName(nullable:true, blank:false)
		nameFormat(nullable: true)
		adminRestricted(nullable:false)
		oid (nullable: false, blank:false)
		description (nullable: true, blank:false)
		dateCreated(nullable:true)
		lastUpdated(nullable:true)
	}
	
	public String toString() {	"attributebase:[id:$id, name: $name, name: $name]" }
	
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
