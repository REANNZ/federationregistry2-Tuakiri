package fedreg.workflow

class ProcessDelegate {

	def process

	ProcessDelegate(Process process) {
		this.process = process
	}

    void task(Map m, Closure closure) {
    	def task = new Task(name: m.name, description:m.description)

		closure.delegate = new TaskDelegate(task)
		closure()
		
		// TODO VALIDATE TASK
		// 1. Dependencies exist
		// 2. Hardcoded approver users/roles/groups exist

		process.addToTasks(task)
	}
}