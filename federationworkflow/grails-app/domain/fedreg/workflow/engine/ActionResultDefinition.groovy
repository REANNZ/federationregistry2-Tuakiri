package fedreg.workflow.engine

class ActionResultDefinition {

    TaskDefinition parentTask
    String name
    List nextTasks = []
    List cancelTasks = []

    static hasMany = [nextTasks: String, cancelTasks: String]
    static belongsTo = TaskDefinition

    static constraints = {
    	nextTasks(validator: { val, obj ->
    		for (int i = 0; i < val.size(); i++) {
    			def taskName = val[i]
    			if (!obj.parentTask.parentProcess.getTask(taskName)) {
    				obj.errors.rejectValue('nextTasks', 'not.exists', [obj.name, obj.parentTask.name, taskName] as Object[], "The \"{0}\" resulting action of the \"{1}\" task references the \"{2}\" task which does not exist!")
	    			return false
    			}
    		}

    		return true

        })
        
       	cancelTasks(validator: { val, obj ->
    		for (int i = 0; i < val.size(); i++) {
    			def taskName = val[i]
    			if (!obj.parentTask.parentProcess.getTask(taskName)) {
    				obj.errors.rejectValue('cancelTasks', 'not.exists', [obj.name, obj.parentTask.name, taskName] as Object[], "The \"{0}\" cancel action of the \"{1}\" task references the \"{2}\" task which does not exist!")
	    			return false
    			}
    		}

    		return true

        })

    }
    

    String toString() {
    	return "on="+name+", to="+nextTasks
    }

}
