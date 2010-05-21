package fedreg.workflow

class TaskRejection {
	
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
	
}