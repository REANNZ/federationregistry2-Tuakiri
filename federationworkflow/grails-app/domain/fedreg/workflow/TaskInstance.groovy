package fedreg.workflow

import fedreg.workflow.TaskStatus

import grails.plugins.nimble.core.UserBase

class TaskInstance {

	String name
	TaskStatus status
	int recordId
	String assignedTo

	UserBase actionedBy
	String result
	String message

	Date dateCompleted
	Date lastUpdated

	static belongsTo = [processInstance: ProcessInstance, task: Task]
	
	
//	static hasMany = [calledBy: TaskInstance]
/*
	static constraints = {
		assignedTo(nullable: true)
		actionedBy(nullable: true)
		actionResult(nullable: true)
		message(nullable: true)
		dateCompleted(nullable: true)
	}
	
	static mapping = {
	    calledBy fetch:"join"
	}


	String toString() {
		return "Id="+this.id+",Name="+this.name+",Status="+this.status+",recordId="+this.recordId+",assignedTo="+this.assignedTo+",actionedBy="+this.actionedBy+",actionResult="+this.actionResult+",message="+this.message+",dateCompleted="+this.dateCompleted+"\n"
	}
*/
}
