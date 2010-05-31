package fedreg.workflow

class ExecuteJob {
    static triggers = {}

	def taskService
	def sessionRequired = true

    def execute(context) {
        def taskInstanceID = context.mergedJobDataMap.get('taskInstanceID')
		taskService.execute(taskInstanceID)
    }
}
