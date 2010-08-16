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
class RoleDescriptor  {
	def cryptoService
	
	Organization organization
	UrlURI errorURL
	
	String displayName
	String description
	String extensions
	
	boolean active
	boolean approved

	Date dateCreated
	Date lastUpdated

	static hasMany = [
		contacts: ContactPerson,
		protocolSupportEnumerations: SamlURI,
		keyDescriptors: KeyDescriptor
	]

	static mapping = {
		tablePerHierarchy false
		sort "displayName"
	}

	static constraints = {
		displayName(nullable:false, blank:false)
		description(nullable:false, blank: false, maxSize:2000)
		
		organization(nullable: false)
		extensions(nullable: true, maxSize:2000)
		errorURL(nullable:true)
		protocolSupportEnumerations(nullable: true)
		contacts(nullable: true)
		keyDescriptors(nullable: true, validator: { val, obj ->
			obj.validateKeyDescriptors()
		})
		dateCreated(nullable:true)
		lastUpdated(nullable:true)
	}
	
	public String toString() {	"roledescriptor:[id:$id, displayName: $displayName]" }
	
	def validateKeyDescriptors = {
		if(!keyDescriptors || keyDescriptors.size() == 0)
			return true
		
		keyDescriptors.each { kd ->
			if(!cryptoService.validateCertificate(kd.keyInfo.certificate)) {
				return ['fedreg.core.roledescriptor.validation.crypto.invalid', kd.keyInfo.keyName]
			}
		}
		
		return true
	}

}
