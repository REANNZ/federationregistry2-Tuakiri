package fedreg.workflow

class WorkflowApproval {
	
	def taskService
	def defaultAction = "list"
	
	def list = {
		def approvalList = TaskInstance.findWhere(status = TaskStatus.APPROVALREQUIRED)
	}
	
}