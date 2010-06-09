package fedreg.workflow

class TaskOutcome {
	
	String name
	String description
	
	List start = []
	List terminate = []
	
    static belongsTo = [ task: Task ]

	static hasMany = [	start: String,
						terminate: String ]

	static constraints = {
		start(validator: {val ->
			val.size() > 0
		})
	}
	
	public String toString() {
		"taskoutcome:[id:$id, name:$name, starts:$start, terminates:$terminate]"
	}
}