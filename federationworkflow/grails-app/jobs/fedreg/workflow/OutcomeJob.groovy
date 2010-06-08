package fedreg.workflow

class OutcomeJob {
    static triggers = {}

	def sessionFactory
	def taskService
	def sessionRequired = true

    def execute(context) {
		
        def taskInstanceID = context.mergedJobDataMap.get('taskInstanceID')
		def outcome = context.mergedJobDataMap.get('outcome')

		taskService.outcome(taskInstanceID)
    }
}
