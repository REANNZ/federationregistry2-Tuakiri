package fedreg.workflow

class TaskRejection {
	
	String name
	String description
	
	List start = []
	List terminate = []
	
    static belongsTo = [ task: Task ]

	static constraints = {
		start(minSize: 1)
	}
	
}