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
class Organization  {	// Also called a participant in AAF land
	static auditable = true

	String name
	String displayName
	String description
	String logoURL
	String lang
	String extensions
	
	boolean active
	boolean approved
	
	UrlURI url
	
	OrganizationType primary
	
	Date dateCreated
	Date lastUpdated
	
	static hasMany = [
		types : OrganizationType,
		suspensions: OrganizationType,	// Won't render in DS/WAYF listing
		sponsors: Organization,
		affiliates: Organization,
		entityDescriptors: EntityDescriptor
	]

	static mapping = {
		autoImport false
		sort "name"
	}

	static constraints = {
		name(nullable: false, blank: false, unique:true)
		displayName(nullable: false, blank: false)
		description(nullable:true, blank: false, maxSize:2000)
		logoURL(nullable:true)
		lang(nullable: false, blank: false)
		url(nullable: false, blank: false, url: true)
		extensions(nullable: true, blank: true)
		types(nullable:true)
		dateCreated(nullable:true)
		lastUpdated(nullable:true)
		entityDescriptors(nullable:true)
	}
	
	public String toString() {	"organization:[id:$id, name: $name, displayName: $displayName]" }
}
