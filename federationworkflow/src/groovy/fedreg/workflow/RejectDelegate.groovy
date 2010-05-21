package fedreg.workflow

class RejectDelegate {

	TaskRejection rejection
	
	RejectDelegate(TaskRejection rejection) {
		this.rejection = rejection
	}
	
	void start(List tasks) {
		this.rejection.start.addAll(tasks)
	}
	
	void start(String task) {
		this.rejection.start.addAll([task])
	}
	
	void terminate(List tasks) {
		this.rejection.terminate.addAll(tasks)
	}
	
	void terminate(String task) {
		this.rejection.terminate.addAll([task])
	}
	
}