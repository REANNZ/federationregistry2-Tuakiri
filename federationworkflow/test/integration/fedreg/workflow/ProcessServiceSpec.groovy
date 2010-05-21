package fedreg.workflow

import grails.plugin.spock.*
import org.apache.shiro.subject.Subject
import org.apache.shiro.util.ThreadContext
import org.apache.shiro.SecurityUtils

import grails.plugins.nimble.core.UserBase
import grails.plugins.nimble.core.ProfileBase

class ProcessServiceSpec extends IntegrationSpec {
	static transactional = true
	
	def processService
	def minimalDefinition
	
	def setupSpec() {
		def profile = new ProfileBase(email:'test@testdomain.com')
		def user = new UserBase(username:'testuser', profile: profile).save()
		
		SpecHelpers.setupShiroEnv()
	}

	def "Create minimal process"() {
		setup:
		minimalDefinition = new File('test/data/minimal.pr').getText()
		
		when:
		processService.create(minimalDefinition)
		
		then:
		def process = Process.findByName('Minimal Test Process')
		
		process.tasks.get(0).outcomes.get('testoutcome1').start.contains('task2')
		process.tasks.get(0).needsApproval() == true
		process.tasks.get(0).approverRoles.size() == 1
		process.tasks.get(0).approverRoles.contains('{TEST_VAR}')
		process.tasks.get(0).rejections.get('rejection1') != null
		process.tasks.get(0).rejections.get('rejection1').start.size() == 1
		process.tasks.get(0).rejections.get('rejection1').start.contains('task6')
		
		process.tasks.get(1).needsApproval() == true
		process.tasks.get(1).approverRoles.size() == 3
		process.tasks.get(1).approverRoles.contains('{TEST_VAR2}')
		process.tasks.get(1).approverRoles.contains('{TEST_VAR3}')
		process.tasks.get(1).approverRoles.contains('TEST_ROLE')
		
		process.tasks.get(3).needsApproval() == false
		process.tasks.get(3).approverRoles.size() == 0
		
		process.tasks.get(4).needsApproval() == false
		process.tasks.get(4).approverRoles.size() == 0
		
		process.creator == processService.authenticatedUser
	}
	
	def "Initiate minimal process"() {
		setup:
		minimalDefinition = new File('test/data/minimal.pr').getText()
		processService.create(minimalDefinition)
		def process = Process.findByName('Minimal Test Process')
		
		when:		
		processService.initiate(process.name, "Approving XYZ Widget", ProcessPriority.LOW, ['TEST_VAR':'VALUE_1', 'TEST_VAR2':'VALUE_2', 'TEST_VAR3':'VALUE_3'])
		
		then:
		def processInstance = ProcessInstance.get(1)
		processInstance.process == process
		processInstance.description == "Approving XYZ Widget"
		processInstance.params.get('TEST_VAR').equals('VALUE_1')
		processInstance.params.get('TEST_VAR2').equals('VALUE_2')
		processInstance.params.get('TEST_VAR3').equals('VALUE_3')
		processInstance.params.get('NOSUCH_VAR') == null
	}
	
	def "Run minimal process"() {
		setup:
		minimalDefinition = new File('test/data/minimal.pr').getText()
		processService.create(minimalDefinition)
		def processInstance = processService.initiate('Minimal Test Process', "Approving XYZ Widget", ProcessPriority.LOW, ['TEST_VAR':'VALUE_1', 'TEST_VAR2':'VALUE_2', 'TEST_VAR3':'VALUE_3'])
		
		when:		
		processService.run(processInstance)
		
		then:
		processInstance.taskInstances.size() == 1
		def ti = processInstance.taskInstances.get(0).status == TaskStatus.APPROVALREQUIRED
	}
}