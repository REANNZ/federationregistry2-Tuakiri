package fedreg.workflow

class WorkflowApprovalController {
	
	def taskService
	def defaultAction = "list"
	
	def list = {
		def approvalList = TaskInstance.findWhere(status = TaskStatus.APPROVALREQUIRED)
	}
	
}