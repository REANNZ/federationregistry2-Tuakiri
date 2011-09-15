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
class SPSSODescriptor extends SSODescriptor {
	static auditable = true

 	boolean authnRequestsSigned
 	boolean wantAssertionsSigned

	ServiceDescription serviceDescription

 	static belongsTo = [entityDescriptor:EntityDescriptor]

	static hasMany = [
		assertionConsumerServices: AssertionConsumerService,
		attributeConsumingServices: AttributeConsumingService,
		discoveryResponseServices: DiscoveryResponseService,
		serviceCategories: ServiceCategory
	]

	static constraints = {
		attributeConsumingServices(nullable: true)
		serviceDescription(nullable:false)
 	}

	public String toString() {	"spssodescriptor:[id:$id, displayName: $displayName]" }
	
	public boolean functioning() {
		( !archived && active && approved && entityDescriptor.functioning() )
	}
	
	public boolean equals(Object obj) {
		if( this.is(obj) ) return true
		if ( obj == null ) return false
		if ( !obj.instanceOf(IDPSSODescriptor) ) return false
		
		SPSSODescriptor rhs = (SPSSODescriptor) obj
		return new EqualsBuilder()
			.append(this.id, rhs.id)
			.append(this.displayName, rhs.displayName)
			.append(this.organization, rhs.organization)
			.isEquals()
	}

	public int hashCode() {
		// hard-coded, randomly chosen, non-zero, odd number different for each class
		return new HashCodeBuilder(43, 129).
		toHashCode();
	}
}
