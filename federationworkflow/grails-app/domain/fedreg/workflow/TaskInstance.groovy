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
		result(nullable:true)
		message(nullable:true)
		dateCreated(nullable:true)
		lastUpdated(nullable:true)
	}
	
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
