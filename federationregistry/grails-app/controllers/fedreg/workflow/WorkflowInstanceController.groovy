package fedreg.workflow

class WorkflowInstanceController {

	def list = {
		def processList = ProcessInstance.findWhere(initiatedBy: authenticatedUser)
		[processes: processList]
	}

}