package fedreg.workflow

class StartJob {
    static triggers = {}

	def taskService
	def sessionRequired = true

    def execute(context) {
        def taskInstanceID = context.mergedJobDataMap.get('taskInstanceID')
    }
}
