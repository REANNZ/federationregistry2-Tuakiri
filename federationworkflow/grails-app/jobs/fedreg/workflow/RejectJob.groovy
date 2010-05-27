package fedreg.workflow

class RejectJob {
    static triggers = {}

	def taskService
	def sessionRequired = true

    def execute(context) {
        def taskInstanceID = context.mergedJobDataMap.get('taskInstanceID')
    }
}