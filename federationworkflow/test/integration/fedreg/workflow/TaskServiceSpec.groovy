package fedreg.workflow

import grails.plugin.spock.*
import spock.util.concurrent.*
import java.util.concurrent.*

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
	def sessionFactory
	
	def setupSpec() {
		def profile = new ProfileBase(email:'test@testdomain.com')
		def user = new UserBase(username:'testuser', profile: profile).save()
		
		SpecHelpers.setupShiroEnv()
	}
	
	def "Validate first 'Minimal Process' task requires approval when run"() {
		setup:
		minimalDefinition = new File('test/data/minimal.pr').getText()
		processService.create(minimalDefinition)
		def processInstance = processService.initiate('Minimal Test Process', "Approving XYZ Widget", ProcessPriority.LOW, ['TEST_VAR':'VALUE_1', 'TEST_VAR2':'VALUE_2', 'TEST_VAR3':'VALUE_3'])
		
		// Utilize new (31/5/10) Spock 0.4 support for spec'ing multi-threaded code (very cool use of CountDownLatch)
		def result = new BlockingVariable<Boolean>(2, TimeUnit.SECONDS)
		def originalMethod = TaskService.metaClass.getMetaMethod("requestApproval", [Object])
		TaskService.metaClass.requestApproval = { def taskInstanceID ->
			originalMethod.invoke(delegate, taskInstanceID)
			result.set(true)
		}
		
		when:				
		processService.run(processInstance)
		
		then:
		result.get() == true
		sessionFactory.getCurrentSession().flush();
		processInstance.taskInstances.size() == 1
		TaskInstance.findById(1).status == TaskStatus.APPROVALREQUIRED
	}

}