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
		invoker(nullable:false)
		
		dateCreated(nullable: true)
		lastUpdated(nullable: true)
	}
	
}