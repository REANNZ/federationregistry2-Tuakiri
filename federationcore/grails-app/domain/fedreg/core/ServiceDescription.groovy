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
class ServiceDescription {
	boolean publish = false
	
	String connectURL		// URL for end users to access service
	String logoURL			// URL to image of the services logo
	
	String furtherInfo		// Further information URL
	String provides			// What the Service Provides
	String benefits			// Benefits of using the service
	String audience			// Intended audience
	String restrictions		// Usage restrictions
	String accessing		// Accessing the service
	String support			// Available support
	String maintenance		// Maintenance periods, generally expected unavailability
	
	Date dateCreated
	Date lastUpdated
	
	static belongsTo = [descriptor:SPSSODescriptor]
	
	static constraints = {
		connectURL(nullable:true)
		logoURL(nullable:true)
		furtherInfo(nullable:true)
		provides(nullable:true)
		benefits(nullable:true)
		audience(nullable:true)
		restrictions(nullable:true)
		accessing(nullable:true)
		support(nullable:true)
		maintenance(nullable:true)
		dateCreated(nullable:true)
		lastUpdated(nullable:true)
	}
	
	public String toString() {	"servicedescription:[id:$id, publish:$publish]" }
}