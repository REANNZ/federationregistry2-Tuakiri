package fedreg.workflow

class ExecuteJob {
    static triggers = {}
	def sessionRequired = true
	def grailsApplication

    def execute(context) {
        def beanID = context.mergedJobDataMap.get('bean')
		def method = context.mergedJobDataMap.get('method')
		def env = context.mergedJobDataMap.get('env')
		
		def bean = grailsApplication.mainContext.getBean(beanID);
		bean."$method"(env)
    }
}
