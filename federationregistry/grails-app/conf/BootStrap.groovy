
import org.codehaus.groovy.grails.commons.GrailsApplication
import org.codehaus.groovy.runtime.InvokerHelper

import grails.util.GrailsUtil

import org.apache.shiro.subject.Subject
import org.apache.shiro.util.ThreadContext
import org.apache.shiro.SecurityUtils

import fedreg.workflow.Process
import fedreg.workflow.WorkflowScript

import fedreg.host.User
import fedreg.host.Profile

class BootStrap {
	
	def grailsApplication
	def dataImporterService
	def processService
	def sessionFactory

     def init = { servletContext ->
		// Populate default administrative account if required
		if(User.count() == 0) {
			def profile = new Profile(email:'internaladministrator@fed.reg')
			def user = new User(username:'internaladministrator', enabled: false, external:false, federated: false, profile: profile)
			user.save(flush: true)
		}
		
		// Provide security manager as none yet exists
		def suMetaClass = new ExpandoMetaClass(SecurityUtils)
		suMetaClass.'static'.getSubject = {[getPrincipal:{User.findByUsername('internaladministrator').id}] as Subject}
		suMetaClass.initialize()
		SecurityUtils.metaClass = suMetaClass
			
		// Auto populate RR data in dev mode
        if(grails.util.GrailsUtil.isDevelopmentEnv()) {
			dataImporterService.initialPopulate()
			dataImporterService.populate(null)
		}
		
		// Populate Workflow Scripts on initial deployment name is set as filename <name>.groovy
		if(WorkflowScript.count() == 0) {	
			def scripts = new File("${System.getenv('FEDREG_CONFIG')}/workflow/scripts")
			scripts.eachFile { script ->
				def name = script.name =~ /(.+?)(\.[^.]*$|$)/
				def s = new WorkflowScript(name: name[0][1], definition: script.getText(), creator:User.findByUsername('internaladministrator'))
				if(!s.save()) {
					log.error "Unable to correctly process workflow script $script during bootstrap"
				}
			}
		}
		
		// Populate WorkFlows on initial deployment
		if(Process.count() == 0) {	
			def processes = new File("${System.getenv('FEDREG_CONFIG')}/workflow/processes")
			processes.eachFile { process ->
				processService.create(process.getText())
			}
		}
		
		SecurityUtils.metaClass = null
     }

     def destroy = {
     }
} 