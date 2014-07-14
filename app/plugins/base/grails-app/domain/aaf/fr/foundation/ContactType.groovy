package aaf.fr.foundation

/**
 * @author Bradley Beddoes
 */
class ContactType  {
	static auditable = true

	String name
	String displayName
	String description

	Date dateCreated
	Date lastUpdated

	static constraints = {
		name(nullable:false, displayName: false)
		displayName(nullable:false, displayName: false)
		description(nullable:false, displayName: false)
		dateCreated(nullable:true)
		lastUpdated(nullable:true)
	}

	public String toString() {	"contactType:[id:$id, name: $name]" }

	def structureAsJson() {
	  def json = new groovy.json.JsonBuilder()
	  json {
	    id id
	    name name
	    display_name displayName
	    description description
	    created_at dateCreated
	    updated_at lastUpdated
	  }
	}

}