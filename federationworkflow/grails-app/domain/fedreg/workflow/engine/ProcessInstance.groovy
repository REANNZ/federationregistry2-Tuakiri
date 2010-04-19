package fedreg.workflow.engine

import fedreg.workflow.engine.ProcessStatus
import fedreg.workflow.engine.ProcessPriority
import fedreg.workflow.engine.ProcessMessage

class ProcessInstance {

	String name
	ProcessStatus status
	String initiatedBy
    ProcessPriority priority
	Date dateInitiated
	//Date dateRequired
	Date dateCompleted
	Date lastUpdated
	boolean completionAcknowlegded = false

	static hasMany = [tasks: TaskInstance, messages: ProcessMessage]
    static belongsTo = [definition: ProcessDefinition]

	static constraints = {
		dateCompleted(nullable: true)
	}
	
	static mapping = {
	    tasks fetch: "join"
	}
    
	String toString() {
		return "ID:\t\t"+id+"\nName:\t\t" + name + "\nInitiatedBy:\t" + initiatedBy + "\nDateInitiated:\t" + dateInitiated + "\nDateCompleted:\t"+dateCompleted+"\nTasks:\n" + tasks
	}

}
