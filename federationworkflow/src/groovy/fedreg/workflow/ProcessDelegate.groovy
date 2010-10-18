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

		process.addToTasks(task)
	}
}