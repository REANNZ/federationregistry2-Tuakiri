package fedreg.workflow

class WorkflowProcessController {

	def list = {
		def processList = Process.list()
		[processList: processList]
	}

}