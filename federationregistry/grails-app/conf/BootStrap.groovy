
import org.codehaus.groovy.grails.commons.GrailsApplication
import grails.util.GrailsUtil

class BootStrap {
	
	def dataImporterService

     def init = { servletContext ->
	
		// Only populate data in dev mode
        if(grails.util.GrailsUtil.isDevelopmentEnv()) {
			dataImporterService.initialPopulate()
			dataImporterService.populate(null)
		}
			
     }

     def destroy = {
     }
} 