package fedreg.workflow

class WorkflowScript {
	String name
	String description
	String definition
	
    static mapping = {
		definition type: "text"
	}
	
	static constraints = {
		name(nullable: false, unique: true)
		description(nullable: false)
		definition(nullable: false)
	}
	
	public String toString() {
		"workflowscript:[id:$id, name:$name]"
	}
}
