package aaf.fr.foundation

/**
 * @author Bradley Beddoes
 */
class MonitorType  {
	static auditable = true

	String name
	String description

	static constraints = {
		name(nullable: false, blank: false)
		description(nullable: false, blank: false)
	}

	public String toString() {	"monitortype:[id:$id, name: $name]" }

  def structureAsJson() {
    def json = new groovy.json.JsonBuilder()
    json {
      id id
      name name
      description description
    }
  }

}
