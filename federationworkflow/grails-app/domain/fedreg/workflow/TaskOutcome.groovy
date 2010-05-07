package fedreg.workflow

class TaskOutcome {
	
	String name
	String description
	
	List start = []
	List terminate = []
	
    static belongsTo = [ task: Task ]
	
}