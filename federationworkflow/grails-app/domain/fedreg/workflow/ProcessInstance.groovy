package fedreg.workflow

import fedreg.workflow.ProcessStatus
import fedreg.workflow.ProcessPriority
import fedreg.workflow.ProcessMessage

import grails.plugins.nimble.core.UserBase

class ProcessInstance {

	String description
	ProcessStatus status
    ProcessPriority priority

	Date dateCreated
	Date lastUpdated
	UserBase initiatedBy
	
	boolean completionAcknowlegded = false
	
	List taskInstances
	Map params

	static hasMany = [taskInstances: TaskInstance, messages: ProcessMessage]
    static belongsTo = [process: Process]

	static constraints = {
		dateCreated(nullable: true)
		lastUpdated(nullable: true)
	}
	
	public String toString() {
		"processinstance:[id:$id] of $process"
	}
}
