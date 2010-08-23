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

/**
 * @author Bradley Beddoes
 */
class AttributeBase  {

	String name
	SamlURI nameFormat
	String friendlyName
	
	String oid
	String headerName
	String alias
	String description
	
	AttributeCategory category
	
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
		nameFormat(nullable: true)
		friendlyName(nullable: false, blank: false)
		oid (nullable: true, blank:false)
		headerName (nullable: true, blank:false)
		alias (nullable: true, blank:false)
		description (nullable: true, blank:false)
		dateCreated(nullable:true)
		lastUpdated(nullable:true)
	}
	
	public String toString() {	"attributebase:[id:$id, name: $name, friendlyName: $friendlyName]" }
	
	public boolean equals(Object obj) {
		if ( !(obj instanceof AttributeBase) ) return false
		
		AttributeBase attr = (AttributeBase) obj
		if(attr.name.equals(name)) return true
		
		return false
	}
}
