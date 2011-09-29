/*
 *	Reporting functionality for Federation Registry.
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

package fedreg.reporting

/**
 * @author Bradley Beddoes
 */
class WayfAccessRecord {
	static auditable = true
	
	String source
	String requestType
	String dsHost

    // Logs provide us an odd format of idpEntity and SP sessioninitiator endpoint.
    // We store these for when things go weird for manual intervention.
    String idpEntity
    String spEndpoint
	
    // We use ID instead of direct links to allow for descriptors to be deleted without impacting reporting
	long idpID	
	long spID
	
	boolean robot = false
	
	Date dateCreated

	static constraints = {
		dateCreated(nullable: true)
		robot(nullable: true)
 	}

}
