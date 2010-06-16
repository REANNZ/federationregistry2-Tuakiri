package fedreg.workflow

class ExecuteJob {
    static triggers = {}
	def sessionRequired = true
	def grailsApplication

    def execute(def context) {
		println this.metaClass
		println this.metaClass.execute
		
        def serviceID = context.mergedJobDataMap.get('service')
		def method = context.mergedJobDataMap.get('method')
		def scriptID = context.mergedJobDataMap.get('script')
		def env = context.mergedJobDataMap.get('env')
		
		// We're invoking a prebuilt service
		if(serviceID) {
			def service = grailsApplication.mainContext.getBean(serviceID);
			
			if(service)
				service."$method"(env)
			else
				log.error "Attempt to execute workflow that references service named $serviceID. Unable to locate service, no execution has taken place"
		}
		else {
			// We're invoking a script
			def workflowScript = WorkflowScript.findByName(scriptID)
			
			if(workflowScript) {
				Binding binding = new Binding();
				binding.setVariable("env", env);
				binding.setVariable("grailsApplication", grailsApplication);
			
				def script = new GroovyShell().parse(workflowScript.definition)
				script.binding = binding
				script.run()
			}
			else
				log.error "Attempt to execute workflow that references script named $scriptID. Unable to locate script, no execution has taken place"
		}
		
    }
}
