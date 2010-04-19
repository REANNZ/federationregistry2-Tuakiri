package fedreg.workflow.engine

import fedreg.workflow.engine.TaskDefinition
import fedreg.workflow.engine.ActionResultDSL

class TaskDSL {

	def taskDefinition

	TaskDSL(TaskDefinition taskDefinition) {
		this.taskDefinition = taskDefinition
	}

	private void action(Map map, boolean automated) {
		if (!(map.containsKey('controller') || map.containsKey('action'))) {
			// throws exception
		}

		taskDefinition.action = map
		taskDefinition.automated = automated
	}

	void automate(Map map) {
		action(map, true)
	}

	void initiate(Map map) {
		if (!map.containsKey('assignTo')) {
			// throws exception
		}

		taskDefinition.assignTo = map['assignTo']
		map.remove('assignTo')

		action(map, false)
	}


	void actioner(String name) {
		this.actioners([name])
	}

	void actioners(List roles) {
		taskDefinition.actioners = roles
	}

	void dependencies(List list) {
		taskDefinition.dependencies = list
	}

	void dependencies(String name) {
		dependencies([name])
	}

    void color(String value) {
        taskDefinition.colour = value
    }
	
	void colour(String value) {
	    taskDefinition.colour = value
	}

	def on(String name) {
		def actionResultDefinition = new ActionResultDefinition(name: name)
		taskDefinition.addToActionResults(actionResultDefinition)
		return new ActionResultDSL(actionResultDefinition)
	}

	void finish() {
		taskDefinition.finishOnThisTask = true
	}

}
