package fedreg.workflow

class OnDelegate {

	def task
	def outcome

	OnDelegate(Task task, String outcome) {
		this.task = task
		this.outcome = outcome
	}

    void launch(List tasks) {
		this.task.launch.put(this.outcome, tasks)
	}
	
	void launch(String task) {
		this.task.launch.put(this.outcome, [task])
	}
	
	void terminate(List tasks) {
		this.task.terminate.put(this.outcome, tasks)
	}
	
	void terminate(String task) {
		this.task.terminate.put(this.outcome, [task])
	}
}