package fedreg.host

import fedreg.core.EntityDescriptor

class DataLoadRecord {
	
	User invoker
	
	String remoteAddr
    String remoteHost
    String userAgent

	Date dateCreated
	Date lastUpdated

	static constraints = {
		invoker(nullable:true)
		dateCreated(nullable: true) // must be true to enable grails
		lastUpdated(nullable: true) // auto-inject to be useful which occurs post validation
	}
	
}