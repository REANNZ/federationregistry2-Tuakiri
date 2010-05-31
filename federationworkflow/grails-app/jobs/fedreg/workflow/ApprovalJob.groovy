package fedreg.workflow

class ApprovalJob {
    static triggers = {}

	def sessionFactory
	def taskService
	def sessionRequired = true

    def execute(context) {
		
        def taskInstanceID = context.mergedJobDataMap.get('taskInstanceID')
		taskService.requestApproval(taskInstanceID)
    }
}
