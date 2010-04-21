package fedreg.workflow

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

	void actioner(String name) {
		this.actioners([name])
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

	def on(String outcome, Closure closure) {
		closure.delegate = new OnDelegate(task, outcome)
		closure()
	}

	void finish() {
		task.finishOnThisTask = true
	}

}
