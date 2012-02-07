package aaf.fr.foundation

/**
 * @author Bradley Beddoes
 */
class ServiceCategory {
	static auditable = true
	
	String name
	String description
	
	static constraints = {
		name(nullable: false)
		description(nullable: false)
 	}

	public String toString() {	"servicecategory:[id:$id, name: $name]" }
}