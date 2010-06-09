package fedreg.workflow

class StartJob {
    static triggers = {}

	def taskService
	def sessionRequired = true

    def execute(context) {
        def processInstanceID = context.mergedJobDataMap.get('processInstanceID')
		def taskID = context.mergedJobDataMap.get('taskID')
		
		taskService.initiate(processInstanceID, taskID)
    }
}
