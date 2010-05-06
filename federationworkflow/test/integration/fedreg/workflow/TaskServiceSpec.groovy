package fedreg.workflow

import grails.plugin.spock.*
import org.apache.shiro.subject.Subject
import org.apache.shiro.util.ThreadContext
import org.apache.shiro.SecurityUtils

import grails.plugins.nimble.core.UserBase
import grails.plugins.nimble.core.ProfileBase

class TaskServiceSpec extends IntegrationSpec {
	static transactional = true
	
	def processService
	def taskService
	def minimalDefinition
	
	def setupSpec() {
		def profile = new ProfileBase(email:'test@testdomain.com')
		def user = new UserBase(username:'testuser', profile: profile).save()
		
		SpecHelpers.setupShiroEnv()
	}
	
	def "Validate first 'Minimal Process' task requires approval when run"() {
		setup:
		minimalDefinition = new File('test/data/minimal.pr').getText()
		processService.create(minimalDefinition)
		def processInstance = processService.initiate('Minimal Process', "Approving XYZ Widget", ProcessPriority.LOW, ['TEST_VAR':'VALUE_1', 'TEST_VAR2':'VALUE_2', 'TEST_VAR3':'VALUE_3'])
		
		when:		
		processService.run(processInstance)
		
		then:
		processInstance.taskInstances.size() == 1
		processInstance.taskInstances.get(0).status == TaskStatus.APPROVALREQUIRED
	}
	
}