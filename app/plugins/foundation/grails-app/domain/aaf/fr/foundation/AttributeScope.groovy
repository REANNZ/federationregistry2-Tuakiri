package aaf.fr.foundation

class AttributeScope {
	static auditable = true
	
	String name
	
	Date dateCreated
	Date lastUpdated

    static constraints = {
		name (blank:false)
		dateCreated(nullable:true)
		lastUpdated(nullable:true)
    }

	public String toString() {	"attributescope:[id:$id, name: $name]" }
}
