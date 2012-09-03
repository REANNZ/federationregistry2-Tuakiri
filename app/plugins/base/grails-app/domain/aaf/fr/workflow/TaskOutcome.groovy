package aaf.fr.workflow

class TaskOutcome {
	
	String name
	String description
	
	List start = []
	List terminate = []
	
    static belongsTo = [ task: Task ]

	static hasMany = [	start: String,
						terminate: String ]

	static constraints = {
		name(nullable:false, blank:false)
		description(nullable:false, blank:false)
		start(validator: {val ->
			val.size() > 0
		})
	}
	
	public String toString() {
		"taskoutcome:[id:$id, name:$name, starts:$start, terminates:$terminate]"
	}
}