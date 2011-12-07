package aaf.fr.foundation

/*
 * @author Bradley Beddoes
 */
class CACertificate {
	static auditable = true
	
	String data
	
	Date dateCreated
	Date lastUpdated
	
	static belongsTo = [caKeyInfo: CAKeyInfo]
	
	static constraints = {
		dateCreated(nullable:true)
		lastUpdated(nullable:true)
	}
	
	static mapping = { 
	    data(type: 'text') 
	}
	
	public String toString() {	"cacertificate:[id:$id]" }
}
