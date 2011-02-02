package fedreg.workflow

import grails.plugins.nimble.core.UserBase

class WorkflowMessage {

	UserBase creator
    String message
	Date dateCreated
    
    static belongsTo = [processInstance: ProcessInstance, taskInstance: TaskInstance]

	static constraints = {
		message(nullable:false, blank:false)
		creator(nullable:true)
		
		processInstance(validator: {val, obj ->
			if(val == null && obj.taskInstance == null) {
				return ['workflowmessage.validation.processinstance.and.taskinstance.not.null']
			}
		})
		
		taskInstance(validator: {val, obj ->
			if(val == null && obj.taskInstance == null) {
				return ['workflowmessage.validation.taskinstance.and.processinstance.not.null']
			}
		})
	}
	
	public String toString() {
		"workflowmessage:[id:$id, creator:$creator, message:$message]"
	}
    
}
