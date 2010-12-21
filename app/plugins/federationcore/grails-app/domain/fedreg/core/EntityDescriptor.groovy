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
class EntityDescriptor extends Descriptor  {
	static auditable = true

	// Organization is the top teir in our model. All entities must belong to an organization definition
	Organization organization

	String entityID
	String extensions
	
	boolean active
	boolean approved
	
	Date dateCreated
	Date lastUpdated

	static hasMany = [
		  idpDescriptors: IDPSSODescriptor,
		  spDescriptors: SPSSODescriptor,
		  attributeAuthorityDescriptors: AttributeAuthorityDescriptor,
		  pdpDescriptors: PDPDescriptor,
		  additionalMetadataLocations: AdditionalMetadataLocation,
		  contacts: ContactPerson
	]

	static constraints = {
		organization(nullable:false)
		entityID(nullable:false, blank:false, unique:true)
		idpDescriptors(nullable: true)
		spDescriptors(nullable: true)
		attributeAuthorityDescriptors(nullable: true)
		pdpDescriptors(nullable: true)
		contacts(nullable: true)
		extensions(nullable: true)
		dateCreated(nullable:true)
		lastUpdated(nullable:true)
  	}

	static mapping = {
		tablePerHierarchy false
	}
	
	public String toString() {	"entitydescriptor:[id:$id, entityID: $entityID]" }
	
	public boolean holdsIDPOnly() {
		( idpDescriptors?.size() == 1 && (attributeAuthorityDescriptors?.size() == 0 || attributeAuthorityDescriptors?.size() == 1) && spDescriptors?.size() == 0 && pdpDescriptors?.size() == 0 )
	}
	
	public boolean holdsSPOnly() {
		( spDescriptors?.size() == 1 && idpDescriptors?.size() == 0 && attributeAuthorityDescriptors?.size() == 0 && pdpDescriptors?.size() == 0 )
	}
	
	public boolean functioning() {
		( active && approved && organization.functioning() )
	}
	
	public boolean empty() {
		( !idpDescriptors && !attributeAuthorityDescriptors && !spDescriptors && !pdpDescriptors )
	}

}
