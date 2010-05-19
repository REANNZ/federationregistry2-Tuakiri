package fedreg.workflow

class TaskRejection {
	
	String name
	String description
	
	List start = []
	
    static belongsTo = [ task: Task ]

	static hasMany = [	start: String ]

	static constraints = {
		start(validator: {val ->
			val.size() > 0
		})
	}
	
}