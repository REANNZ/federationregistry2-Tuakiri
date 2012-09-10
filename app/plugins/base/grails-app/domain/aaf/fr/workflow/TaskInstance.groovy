package aaf.fr.workflow

import aaf.fr.identity.Subject

class TaskInstance {

	TaskStatus status = TaskStatus.PENDING
	Subject approver

	Date dateCreated
	Date lastUpdated

	static hasMany = [messages: WorkflowMessage, potentialApprovers: Subject]
	static belongsTo = [processInstance: ProcessInstance, task: Task]
	static constraints = {
		approver(nullable:true)
		dateCreated(nullable:true)
		lastUpdated(nullable:true)
	}
	
	static mapping = {
	      cache usage:'read-write', include:'non-lazy'
	}
	
	public String toString() {
		"taskinstance:[id:$id] of $task"
	}
}
