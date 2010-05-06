package fedreg.workflow

import grails.plugins.nimble.core.UserBase
import grails.plugins.nimble.core.Role
import grails.plugins.nimble.core.Group

class Task {

    String name
	boolean automated = true
	
	Map execute = [:]
   	Map start = [:]
	Map terminate = [:]
	Map rejections = [:]
	
	List approverRoles = []
	List approverGroups = []
	List approvers = []
   	
   	boolean finishOnThisTask = false

   	static hasMany = [	instances: TaskInstance, 
						rejections: TaskRejection,
						dependencies: String,
						approverRoles: String,
						approverGroups: String,
						approvers: String
	]
	
    static belongsTo = [ process: Process ]
	
	static constraints = {

	}


/*
	static constraints = {

    	actioners(nullable: true)
    	
    	colour(nullable: true, matches:"#[0-9A-F][0-9A-F][0-9A-F][0-9A-F][0-9A-F][0-9A-F]")

    	dependencies(validator: { val, obj ->
    		for (int i = 0; i < val.size(); i++) {
    			def taskName = val.get(i)
    			if (!obj.parentProcess.getTask(taskName)) {
    				obj.errors.rejectValue('dependencies', 'invalid.task', [obj.name, taskName] as Object[], "The dependencies of the \"{0}\" task references the \"{1}\" task which does not exist!")
    				return false
    			}
    		}
    		return true
    	})

    	action(validator: { val, obj ->
    		if (!obj.finishOnThisTask) {
	    		if (!val) {
	    			obj.errors.rejectValue('action', 'undefined', [obj.name] as Object[], "The \"{0}\" task must have an 'initiate' or 'automate' action defined!")
	    			return false
	    		} else if (!val.containsKey('action')) {
	    			if (obj.automated) {
	    				obj.errors.rejectValue('action', 'missing.action', [obj.name] as Object[], "The automated clause of the \"{0}\" task must have an action defined!")
	    			} else {
	    				obj.errors.rejectValue('action', 'missing.action', [obj.name] as Object[], "The initiate clause of the \"{0}\" task must have an action defined!")
	    			}
	    			return false
	    		} else if (!val.containsKey('controller') && !val.containsKey('service')) {
	    			if (obj.automated) {
	    				obj.errors.rejectValue('action', 'missing.component', [obj.name] as Object[], "The automated clause of the \"{0}\" task must have either a controller or service defined!")
	    			} else {
	    				obj.errors.rejectValue('action', 'missing.component', [obj.name] as Object[], "The initiate clause of the \"{0}\" task must be defined with a controller!")
	    			}
	    			return false
	    		} else if (val.containsKey('service') && !obj.automated) {
	    			obj.errors.rejectValue('action', 'wrong.component', [obj.name] as Object[], "The initiate clause of the \"{0}\" task must be defined with a controller!")
	    			return false
	    		}
    		}
    		return true
    	})

    	assignTo(validator: { val, obj ->
    		if (!val && obj.action && !obj.automated && !obj.finishOnThisTask) {
    			if (obj.parentProcess.firstTask != obj.name) {
    				obj.errors.rejectValue('assignTo', 'undefined', [obj.name] as Object[], "The \"{0}\" task must be assigned to someone!")
    				return false
    			}
    		}
    		return true
    	}, nullable: true)

    	actionResults(validator: { val, obj ->
    		if (!val && !obj.finishOnThisTask) {
    			obj.errors.rejectValue('actionResults', 'undefined', [obj.name] as Object[], "The \"{0}\" task must handle the resulting action!")
    			return false
    		} else if (val && obj.finishOnThisTask) {
    			obj.errors.rejectValue('actionResults', 'invalid', [obj.name] as Object[], "The \"{0}\" task cannot follow onto another task if the process finishes here!")
    			return false
    		}
    		return true
    	}, nullable: true)
	}
	
	void setColour(String value) {
    	this.colour = value?.toUpperCase()
	}


    def getNextTask(String actionResult) {
    	def actionResultDefinition = actionResults.find { it.name == actionResult }
    	return actionResultDefinition?.nextTasks
	}
	
	def getCancelTask(String actionResult) {
    	def actionResultDefinition = actionResults.find { it.name == actionResult }
    	return actionResultDefinition?.cancelTasks
	}


    boolean hasActionResult(String name) {
		def actionResult = actionResults.find { it.name == name }
		return (actionResult ? true : false)
	}

    String toString() {
		return "id="+id+",name="+name+",actioners="+actioners+",dependencies="+dependencies+",action=["+action+"],automated="+automated+",assignTo="+assignTo+
			",actionResults="+actionResults+",finishOnThisTask="+finishOnThisTask
	}
	*/
}
