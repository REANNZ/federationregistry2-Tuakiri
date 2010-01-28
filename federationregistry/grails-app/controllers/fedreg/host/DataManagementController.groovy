package fedreg.host

import org.apache.shiro.SecurityUtils

import grails.util.GrailsUtil
import org.codehaus.groovy.grails.commons.GrailsApplication

/**
 * Allows upstream data from existing resource registry to be reloaded
 *
 * @author Bradley Beddoes
 */
class DataManagementController {
	
	static Map allowedMethods = [ refreshdata: 'POST' ]

	def dataImporterService
	
	def index = {
		[records:DataLoadRecord.list()]
	}
	
	// TODO
	// Only allow this to run when no Users are populated into the database as an inital bootstrap process
	// This will need to be extended and made more solid as resource registry is phased out.
	def bootstrap = {
		if((User.count() == 0 ) && (DataLoadRecord.count() == 0) ) {
			log.info("Doing initial bootstrap process..")
			dataImporterService.initialPopulate()
			dataImporterService.populate(request)
			redirect(action:"index")
		}
		else {
			response.sendError(403)
		}
	}
	
	def refreshdata = {
		dataImporterService.dumpData()
		dataImporterService.populate(request)
		SecurityUtils.subject?.logout()
		redirect(action:"index")
	}

}