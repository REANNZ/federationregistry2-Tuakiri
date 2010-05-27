package fedreg.workflow

class ApproveJob {
    static triggers = {}

	def taskService
	def sessionRequired = true

    def execute(context) {
        def taskInstanceID = context.mergedJobDataMap.get('taskInstanceID')
		taskService.approved(taskInstanceID)
    }
}
