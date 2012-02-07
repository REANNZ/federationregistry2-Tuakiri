package aaf.fr.reporting

/**
 * @author Bradley Beddoes
 */
class Robot {
	
	String source
	String username
	
	boolean active = true
	
	Date dateCreated
	
	static constraints = {
		dateCreated(nullable: true)
		username(nullable:true, blank: false, unique: true, minSize: 4, maxSize: 255)
		source(nullable: true, blank:false)
 	}

}
