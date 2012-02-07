package aaf.fr.workflow

class TaskDelegate {

	def task

	TaskDelegate(Task task) {
		this.task = task
	}

	void execute(Map map) {
		task.execute = map
	}

	void automate(Map map) {
		action(map, true)
	}

	void actioners(List roles) {
		task.actioners = roles
	}

	void dependencies(List list) {
		task.dependencies = list
	}

	void dependencies(String name) {
		task.addToDependencies(name)
	}

	def outcome(Map map, Closure closure) {		
		def taskOutcome = new TaskOutcome(name: map.name, description: map.description, task:task)
		closure.delegate = new OutcomeDelegate(taskOutcome)
		closure()
		
		task.outcomes.put(map.name, taskOutcome)
	}

	void finish() {
		task.finishOnThisTask = true
	}
	
	def approver(Map map, Closure closure) {
		closure.delegate = new ApprovalDelegate(task, map)
		closure()
	}

}
