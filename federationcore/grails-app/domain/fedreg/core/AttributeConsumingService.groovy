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
			order("_base.friendlyName", "asc")
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
