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
	
}