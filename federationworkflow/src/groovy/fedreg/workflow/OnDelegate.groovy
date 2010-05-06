package fedreg.workflow

class OnDelegate {

	def task
	def outcome

	OnDelegate(Task task, String outcome) {
		this.task = task
		this.outcome = outcome
	}

    void start(List tasks) {
		this.task.start.put(this.outcome, tasks)
	}
	
	void start(String task) {
		this.task.start.put(this.outcome, [task])
	}
	
	void terminate(List tasks) {
		this.task.terminate.put(this.outcome, tasks)
	}
	
	void terminate(String task) {
		this.task.terminate.put(this.outcome, [task])
	}
}