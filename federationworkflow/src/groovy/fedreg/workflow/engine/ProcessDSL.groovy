package fedreg.workflow.engine

import fedreg.workflow.engine.TaskDefinition
import fedreg.workflow.engine.TaskDSL

class ProcessDSL {

	def processDefinition

	ProcessDSL(ProcessDefinition processDefinition) {
		this.processDefinition = processDefinition
	}

    void task(String name, Closure closure) {
    	def taskDefinition = new TaskDefinition(name: name)
    	processDefinition.addToTasks(taskDefinition)

    	if (!processDefinition.firstTask)
    		processDefinition.firstTask = name

		closure.delegate = new TaskDSL(taskDefinition)
		closure()

	}

    void description(String description) {
    	processDefinition.description = description
    }

	String toString() {
		return tasks
	}

}