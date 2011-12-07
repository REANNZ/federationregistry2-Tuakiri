package aaf.fr.foundation

class AttributeCategory {
	static auditable = true
	
	String name
	
	Date dateCreated
	Date lastUpdated

    static constraints = {
		name (blank:false)
		dateCreated(nullable:true)
		lastUpdated(nullable:true)
	}

	public String toString() {	"attributecategory:[id:$id, name: $name]" }
}
