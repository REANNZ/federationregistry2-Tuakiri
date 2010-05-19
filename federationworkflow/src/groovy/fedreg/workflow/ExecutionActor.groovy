package fedreg.workflow

import static groovyx.gpars.GParsPool.withPool
import static groovyx.gpars.GParsPool.executeAsync
import groovyx.gpars.actor.*

class ExecutionActor extends DynamicDispatchActor {
	
	def processService
	def taskService
	def workflowMessager
	
	void onMessage(List message) {
        def taskInstance = message.get(0)
		def status = message.get(1)
		
		switch(status) {
			case TaskStatus.INITIATING:
				if(taskInstance.task.needsApproval()) {
					taskService.requestApproval(taskInstance)
				}
				else {
					if(taskInstance.task.executes())
						taskService.execute(taskInstance)
				}	
				break
			case TaskStatus.APPROVALREQUIRED:
				break
			case TaskStatus.APPROVALFAILURE:
				break
			case TaskStatus.APPROVALGRANTED:
				if(taskInstance.task.executes())
					taskService.execute(taskInstance)
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