package fedreg.workflow

class TaskRejection {
	
	String name
	String description
	
	List start = []
	List terminate = []
	
    static belongsTo = [ task: Task ]
	
}