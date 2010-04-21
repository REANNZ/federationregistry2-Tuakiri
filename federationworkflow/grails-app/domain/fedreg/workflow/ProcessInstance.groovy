package fedreg.workflow

import fedreg.workflow.ProcessStatus
import fedreg.workflow.ProcessPriority
import fedreg.workflow.ProcessMessage

class ProcessInstance {

	String name
	ProcessStatus status
	String initiatedBy
    ProcessPriority priority
	Date dateInitiated

	Date dateCompleted
	Date lastUpdated
	boolean completionAcknowlegded = false

	static hasMany = [tasks: TaskInstance, messages: ProcessMessage]
    static belongsTo = [process: Process]

	static constraints = {
		dateCompleted(nullable: true)
	}
	
	static mapping = {
	    tasks fetch: "join"
	}
	
}
