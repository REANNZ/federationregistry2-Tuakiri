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
class Attribute  {

	String name
	SamlURI nameFormat
	String friendlyName
	
	String oid
	String headerName
	String alias
	String description
	
	AttributeScope scope
	AttributeCategory category
	
	Date dateCreated
	Date lastUpdated

	static mapping = {
		autoImport false
	}

	static constraints = {
		name(nullable: false, blank: false, unique: true)
		nameFormat(nullable: true)
		friendlyName(nullable: false, blank: false)
		oid (blank:false)
		headerName (blank:false)
		alias (blank:false)
		description (blank:false)
		dateCreated(nullable:true)
		lastUpdated(nullable:true)
	}
	
	public String toString() {
		return friendlyName
	}
	
	public boolean equals(Object obj) {
		if ( !(obj instanceof Attribute) ) return false;
		
		Attribute attr = (Attribute) obj
		if((attr.id == id) && (attr.name == name)) return true;
		
		return false;
	}
}
