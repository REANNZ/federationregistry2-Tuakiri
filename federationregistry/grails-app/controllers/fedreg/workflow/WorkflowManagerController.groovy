package fedreg.workflow

class WorkflowManagerController {

	def list = {
		def processList = Process.list()
		[processList: processList]
	}

}