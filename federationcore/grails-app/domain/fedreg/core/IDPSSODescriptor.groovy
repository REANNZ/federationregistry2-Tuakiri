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
}
