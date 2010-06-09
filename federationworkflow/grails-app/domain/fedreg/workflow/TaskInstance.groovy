package fedreg.workflow

import fedreg.workflow.TaskStatus

import grails.plugins.nimble.core.UserBase

class TaskInstance {

	TaskStatus status = TaskStatus.PENDING
	UserBase approver

	Date dateCreated
	Date lastUpdated

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
