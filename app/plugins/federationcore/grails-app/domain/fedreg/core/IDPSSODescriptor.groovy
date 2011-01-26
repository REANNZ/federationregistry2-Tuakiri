/*
 *	A Grails/Hibernate compatible environment for SAML2 metadata types with application specific 
 *	data extensions as appropriate.
 * 
 *	Copyright (C) 2010 Australian Access Federation
 *
 *	Licensed under the Apache License, Version 2.0 (the "License");
 *	you may not use this file except in compliance with the License.
 *	You may obtain a copy of the License at
 *
 *	http://www.apache.org/licenses/LICENSE-2.0
 *
 *	Unless required by applicable law or agreed to in writing, software
 *	distributed under the License is distributed on an "AS IS" BASIS,
 *	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *	See the License for the specific language governing permissions and
 *	limitations under the License.
 */
package fedreg.core

import org.apache.commons.lang.builder.EqualsBuilder
import org.apache.commons.lang.builder.HashCodeBuilder

/**
 * @author Bradley Beddoes
 */
class IDPSSODescriptor extends SSODescriptor  {
	static auditable = true
	
	String scope
	
	AttributeAuthorityDescriptor collaborator
		
	boolean wantAuthnRequestsSigned = false
	boolean autoAcceptServices = true

	List singleSignOnServices

	static belongsTo = [entityDescriptor:EntityDescriptor]

	static hasMany = [
		singleSignOnServices: SingleSignOnService,
		nameIDMappingServices: NameIDMappingService,
		assertionIDRequestServices: AssertionIDRequestService,
		attributeProfiles: SamlURI,
		attributes: Attribute
	]

 	static constraints = {
		scope(nullable: false)
		collaborator(nullable: true)
		singleSignOnServices(minSize: 1)
		nameIDMappingServices(nullable: true)
		assertionIDRequestServices(nullable: true)
		attributeProfiles(nullable: true)
		attributes(nullable: true)
	}
	
	public String toString() {	"idpssodescriptor:[id:$id, displayName: $displayName]" }
	
	public boolean functioning() {
		( active && approved && entityDescriptor.functioning() )
	}
	
	public List sortedAttributes() {
		def c = Attribute.createCriteria()
		def attributeList = c.list {
			eq("idpSSODescriptor", this)
			createAlias("base","_base")
			order("_base.category", "asc")
			order("_base.friendlyName", "asc")
		}
	}
	
	public boolean equals(Object obj) {
		if( this.is(obj) ) return true
		if ( obj == null ) return false
		if ( !obj.instanceOf(IDPSSODescriptor) ) return false
		
		IDPSSODescriptor rhs = (IDPSSODescriptor) obj
		return new EqualsBuilder()
			.append(this.id, rhs.id)
			.append(this.scope, rhs.scope)
			.append(this.displayName, rhs.displayName)
			.append(this.organization, rhs.organization)
			.isEquals()
	}

	public int hashCode() {
		// hard-coded, randomly chosen, non-zero, odd number different for each class
		return new HashCodeBuilder(21, 123).
		toHashCode();
	}
}
