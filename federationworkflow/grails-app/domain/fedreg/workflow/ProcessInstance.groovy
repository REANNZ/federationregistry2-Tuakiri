package fedreg.workflow

import grails.plugins.nimble.core.UserBase

class ProcessInstance {

	String description
	ProcessStatus status
    ProcessPriority priority

	Date dateCreated
	Date lastUpdated
	
	boolean completionAcknowlegded = false
	
	List taskInstances
	Map params

	static hasMany = [taskInstances: TaskInstance, messages: WorkflowMessage]
    static belongsTo = [process: Process]

	static constraints = {
		dateCreated(nullable: true)
		lastUpdated(nullable: true)
	}
	
	public String toString() {
		"processinstance:[id:$id] of $process"
	}
}
