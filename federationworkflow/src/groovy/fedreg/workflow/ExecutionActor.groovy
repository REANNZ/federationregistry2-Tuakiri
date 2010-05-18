package fedreg.workflow

import groovyx.gpars.actor.*

class ExecutionActor extends DynamicDispatchActor {
	
	def processService, taskService, workflowMessager
	
	public ExecutionActor(def processService, def taskService, def workflowMessager) {
		this.processService = processService
		this.taskService = taskService
		this.workflowMessager = workflowMessager
	}
	
	void onMessage(List message) {
        def taskInstance = message.get(0)
		def status = message.get(1)
			
		switch(status) {
			case TaskStatus.INITIATING:
				if(taskInstance.task.needsApproval)
					taskService.requestApproval(taskInstance)
				break
			case TaskStatus.APPROVALREQUIRED:
				break
			case TaskStatus.APPROVALFAILURE:
				break
			case TaskStatus.APPROVALGRANTED:
				break
			case TaskStatus.APPROVALREJECTED:
				break
			case TaskStatus.INPROGRESS:
				break
			case TaskStatus.SUCCESSFUL:
				break
			case TaskStatus.TERMINATED:
				break
		}
    }
	
}