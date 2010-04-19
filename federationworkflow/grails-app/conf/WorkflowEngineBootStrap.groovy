import fedreg.workflow.engine.ProcessDefinition

class WorkflowEngineBootStrap {

    def pluginManager
	def processManagerService

    def init = { servletContext ->

        def loadProcessDefinitionFiles = { processesFolder ->
            processesFolder.listFiles().each { file ->
                if (file.isFile() && file.name.endsWith(".dsl")) {
                 	def processDefinition = processManagerService.loadDSL(file)

                 	def existingProcesses = ProcessDefinition.findAllByName(processDefinition.name)
                 	if (!existingProcesses) {
                 		processDefinition.save(flush: true)
                 		processManagerService.createRoles(processDefinition)
                 		
                 		log.info("$processDefinition.name loaded into the database")
                 	}
                }
            }
        }

        // Load processes defined by plugin
        pluginManager?.pluginList.each { grailsPlugin -> 
            loadProcessDefinitionFiles(new File(servletContext.getRealPath("${grailsPlugin.plugin.pluginPath}/processes/")))
        }
        
        // Load processes defined in main application
        //loadProcessDefinitionFiles(new File(servletContext.getRealPath("/processes/")))
    }

    def destroy = {
    }
}
