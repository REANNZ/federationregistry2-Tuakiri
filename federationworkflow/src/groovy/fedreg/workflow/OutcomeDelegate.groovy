package fedreg.workflow

class OutcomeDelegate {

	def taskOutcome

	OutcomeDelegate(TaskOutcome taskOutcome) {
		this.taskOutcome = taskOutcome
	}

    void start(List tasks) {
		this.taskOutcome.start.addAll(tasks)
	}
	
	void start(String task) {
		this.taskOutcome.start.add(task)
	}

}