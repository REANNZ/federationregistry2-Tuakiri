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

import groovy.time.TimeCategory

/*
 * @author Bradley Beddoes
 */
class Certificate {
	
	String data
	
	String subject
	String issuer
	Date expiryDate
	
	Date dateCreated
	Date lastUpdated
	
	static belongsTo = [keyInfo: KeyInfo]
	
	static mapping = { 
		data(type: 'text') 
	}
	
	static constraints = {
		data(nullable:false, blank:false)
		subject(nullable:false, blank:false, maxSize: 2048)
		issuer(nullable:false, blank:false, maxSize: 2048)
		expiryDate(nullable:false)
		
		dateCreated(nullable:true)
		lastUpdated(nullable:true)
	}
	
	boolean infoAlert() {
		use ( TimeCategory ) {
			def today = new Date()
			(today + 6.months).after(expiryDate)
		}
	}
	
	boolean warnAlert() {
		use ( TimeCategory ) {
			def today = new Date()
			(today + 3.months).after(expiryDate)
		}
	}
	
	boolean criticalAlert() {
		use ( TimeCategory ) {
			def today = new Date()
			(today + 1.months).after(expiryDate)
		}
	}
	
}
