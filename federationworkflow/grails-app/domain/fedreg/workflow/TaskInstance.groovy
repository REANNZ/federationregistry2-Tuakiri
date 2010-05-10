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
}
