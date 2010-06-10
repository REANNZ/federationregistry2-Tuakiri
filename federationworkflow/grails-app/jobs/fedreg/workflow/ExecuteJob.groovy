package fedreg.workflow

class ExecuteJob {
    static triggers = {}
	def sessionRequired = true

    def execute(context) {
        def bean = context.mergedJobDataMap.get('bean')
		def method = context.mergedJobDataMap.get('method')
		
		def bean = grailsApplication.mainContext.getBean(bean);
    }
}
