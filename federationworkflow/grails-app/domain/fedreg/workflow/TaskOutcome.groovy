package fedreg.workflow

class TaskOutcome {
	
	String name
	String description
	
	List start = []
	List terminate = []
	
    static belongsTo = [ task: Task ]

	static constraints = {
		start(minSize: 1)
	}
	
}