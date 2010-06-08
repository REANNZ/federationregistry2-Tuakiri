package fedreg.workflow

class TerminateJob {
    static triggers = {}

	def taskService
	def sessionRequired = true

    def execute(context) {
        def processID = context.mergedJobDataMap.get('processID')
		
    }
}
