package aaf.fr.foundation

import groovy.time.TimeCategory

/*
 * @author Bradley Beddoes
 */
class Certificate {
	static auditable = true
	
	String data
	
	String subject
	String issuer
	Date expiryDate
	
	Date dateCreated
	Date lastUpdated
	
	static belongsTo = [keyInfo: KeyInfo]
	
	static mapping = { 
		data(type: 'text') 
		sort "dateCreated"
	}
	
	static constraints = {
		data(nullable:false, blank:false)
		subject(nullable:false, blank:false, maxSize: 2048)
		issuer(nullable:false, blank:false, maxSize: 2048)
		expiryDate(nullable:false)
		
		dateCreated(nullable:true)
		lastUpdated(nullable:true)
	}
	
	public String toString() {	"certificate:[id:$id, subject: $subject, issuer:$issuer, expires: $expiryDate]" }
	
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
