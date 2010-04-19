package fedreg.workflow.engine

import fedreg.workflow.engine.TaskStatus

import grails.plugins.nimble.core.UserBase

class TaskInstance {

	String name
	TaskStatus status
	int recordId
	String assignedTo

	String actionedBy
	String actionResult
	String message

	Date dateCompleted
	Date lastUpdated

	static belongsTo = [parentProcess: ProcessInstance, definition: TaskDefinition]
	static hasMany = [calledBy: TaskInstance]

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

}
