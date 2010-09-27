package fedreg.workflow

import org.apache.commons.logging.LogFactory

/* Used Quartz jobs here so we can take advantange of serialization and possibly even clustering in the future */
class ExecuteJob {
    static triggers = {}
	def sessionRequired = true
	def grailsApplication

    def execute(def context) {		
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
				def _log = LogFactory.getLog("fedreg.workflow.script.$scriptID")
				
				Binding binding = new Binding()
				binding.setVariable("env", env)
				binding.setVariable("grailsApplication", grailsApplication)
				binding.setVariable("ctx", grailsApplication.mainContext)
				binding.setVariable("log", _log)
				
				def script = new GroovyShell(grailsApplication.classLoader, binding).parse(workflowScript.definition)
				script.binding = binding
				script.run()
			}
			else
				log.error "Attempt to execute workflow that references script named $scriptID. Unable to locate script, no execution has taken place"
		}
		
    }
}
