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
	def savedMetaClasses
	
	def setupSpec() {
		def profile = new ProfileBase(email:'test@testdomain.com')
		def user = new UserBase(username:'testuser', profile: profile).save()

		SpecHelpers.setupShiroEnv()
	}
	
	def setup() {
		savedMetaClasses = [:]
	}
	
	def "Validate first task in minimal process requires approval when process is run"() {
		setup:
		SpecHelpers.registerMetaClass(TaskService, savedMetaClasses)
		taskService.metaClass = TaskService.metaClass	// Register existing metaClass and utilize new instance with injected service
		
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
		sessionFactory.getCurrentSession().clear();
		processInstance.taskInstances.size() == 1
		TaskInstance.findById(1).status == TaskStatus.APPROVALREQUIRED
		
		cleanup:
		SpecHelpers.resetMetaClasses(savedMetaClasses)
		taskService.metaClass = TaskService.metaClass	// Restore to default metaClass and restore service
	}

	def "Validate first task in minimal process executes when process is run and task approved"() {
		setup:
		SpecHelpers.registerMetaClass(TaskService, savedMetaClasses)
		taskService.metaClass = TaskService.metaClass
		
		minimalDefinition = new File('test/data/minimal.pr').getText()
		processService.create(minimalDefinition)
		def processInstance = processService.initiate('Minimal Test Process', "Approving XYZ Widget", ProcessPriority.LOW, ['TEST_VAR':'VALUE_1', 'TEST_VAR2':'VALUE_2', 'TEST_VAR3':'VALUE_3'])
		
		BlockingVariables block = new BlockingVariables(4, TimeUnit.SECONDS)
		
		def requestApproval = TaskService.metaClass.getMetaMethod("requestApproval", [Object])
		TaskService.metaClass.requestApproval = { def taskInstanceID ->
			requestApproval.invoke(delegate, taskInstanceID)
			block.approvalRequested = true
		}
		
		def execute = TaskService.metaClass.getMetaMethod("execute", [Object])
		TaskService.metaClass.execute = { def taskInstanceID ->
			execute.invoke(delegate, taskInstanceID)
			block.executed = true
		}
		
		when:				
		processService.run(processInstance)
		if(block.approvalRequested) {
			sessionFactory.getCurrentSession().clear();
			taskService.approve(TaskInstance.list().get(0).id)
		}
		
		then:
		block.executed == true
		sessionFactory.getCurrentSession().clear();
		TaskInstance.list().get(0).status == TaskStatus.INPROGRESS
		
		cleanup:
		SpecHelpers.resetMetaClasses(savedMetaClasses)
		taskService.metaClass = TaskService.metaClass
	}
	
	def "Validate task6 in minimal process executes when process is run and task1 rejected"() {
		setup:
		SpecHelpers.registerMetaClass(TaskService, savedMetaClasses)
		taskService.metaClass = TaskService.metaClass
		
		minimalDefinition = new File('test/data/minimal.pr').getText()
		processService.create(minimalDefinition)
		def processInstance = processService.initiate('Minimal Test Process', "Approving XYZ Widget", ProcessPriority.LOW, ['TEST_VAR':'VALUE_1', 'TEST_VAR2':'VALUE_2', 'TEST_VAR3':'VALUE_3'])
		
		BlockingVariables block = new BlockingVariables(4, TimeUnit.SECONDS)
		
		def requestApproval = TaskService.metaClass.getMetaMethod("requestApproval", [Object])
		TaskService.metaClass.requestApproval = { def taskInstanceID ->
			requestApproval.invoke(delegate, taskInstanceID)
			block.approvalRequested = true
		}
		
		def terminateAndStartTasks = TaskService.metaClass.getMetaMethod("terminateAndStartTasks", [Object, Object] as Class[])
		TaskService.metaClass.terminateAndStartTasks = { def taskInstance, def reaction ->
			block.terminateAndStartTasks = true
			block.terminates = reaction.terminate.size()
			block.starts = reaction.start.size()
			block.startTask6 = reaction.start.contains('task6')
		}
		
		when:				
		processService.run(processInstance)
		if(block.approvalRequested) {
			sessionFactory.getCurrentSession().clear();
			taskService.reject(TaskInstance.list().get(0).id, 'rejection1', 'supplied test reason')
		}
		
		then:
		block.terminateAndStartTasks == true
		block.terminates == 0
		block.starts == 1
		block.startTask6 == true
		
		sessionFactory.getCurrentSession().clear();
		TaskInstance.list().get(0).status == TaskStatus.APPROVALREJECTED
		
		cleanup:
		SpecHelpers.resetMetaClasses(savedMetaClasses)
		taskService.metaClass = TaskService.metaClass
	}

	def "Validate first task in minimal process completes when process is run, the task is approved and execution is successful"() {
		setup:
		SpecHelpers.registerMetaClass(TaskService, savedMetaClasses)
		taskService.metaClass = TaskService.metaClass
		
		minimalDefinition = new File('test/data/minimal.pr').getText()
		processService.create(minimalDefinition)
		def processInstance = processService.initiate('Minimal Test Process', "Approving XYZ Widget", ProcessPriority.LOW, ['TEST_VAR':'VALUE_1', 'TEST_VAR2':'VALUE_2', 'TEST_VAR3':'VALUE_3'])
		
		BlockingVariables block = new BlockingVariables(4, TimeUnit.SECONDS)
		
		def requestApproval = TaskService.metaClass.getMetaMethod("requestApproval", [Object])
		TaskService.metaClass.requestApproval = { def taskInstanceID ->
			requestApproval.invoke(delegate, taskInstanceID)
			block.approvalRequested = true
		}
		
		def execute = TaskService.metaClass.getMetaMethod("execute", [Object])
		TaskService.metaClass.execute = { def taskInstanceID ->
			execute.invoke(delegate, taskInstanceID)
			block.executed = true
		}
		
		def complete = TaskService.metaClass.getMetaMethod("complete", [Object, Object] as Class[])
		TaskService.metaClass.complete = { def taskInstanceID, def outcomeName ->
			block.complete = true
			block.outcome = outcomeName
		}
		
		when:				
		processService.run(processInstance)
		if(block.approvalRequested) {
			sessionFactory.getCurrentSession().clear();
			taskService.approve(TaskInstance.list().get(0).id)
		}
		
		if(block.executed) {
			sessionFactory.getCurrentSession().clear();
			taskService.complete(TaskInstance.list().get(0).id, 'testoutcome1')
		}
		
		then:
		block.complete == true
		block.outcome == 'testoutcome1'
		
		cleanup:
		SpecHelpers.resetMetaClasses(savedMetaClasses)
		taskService.metaClass = TaskService.metaClass
	}
	
	def "Validate second task in minimal process is started after full completion of first"() {
		setup:
		SpecHelpers.registerMetaClass(TaskService, savedMetaClasses)
		taskService.metaClass = TaskService.metaClass
		
		minimalDefinition = new File('test/data/minimal.pr').getText()
		processService.create(minimalDefinition)
		def processInstance = processService.initiate('Minimal Test Process', "Approving XYZ Widget", ProcessPriority.LOW, ['TEST_VAR':'VALUE_1', 'TEST_VAR2':'VALUE_2', 'TEST_VAR3':'VALUE_3'])
		
		BlockingVariables block = new BlockingVariables(4, TimeUnit.SECONDS)
		
		int approvalCount = 0
		def requestApproval = TaskService.metaClass.getMetaMethod("requestApproval", [Object])
		TaskService.metaClass.requestApproval = { def taskInstance ->
			requestApproval.invoke(delegate, taskInstance)	
			block."approval$approvalCount" = true
			approvalCount++
		}
		
		int executeCount = 0
		def execute = TaskService.metaClass.getMetaMethod("execute", [Object])
		TaskService.metaClass.execute = { def taskInstanceID ->
			execute.invoke(delegate, taskInstanceID)
			block."execute$executeCount" = true
			executeCount++
		}
		
		int completeCount = 0
		def complete = TaskService.metaClass.getMetaMethod("complete", [Object, Object] as Class[])
		TaskService.metaClass.complete = { def taskInstanceID, def outcomeName ->
			complete.invoke(delegate, taskInstanceID, outcomeName)
			block."complete$completeCount" = true
			completeCount++
		}
		
		when:				
		processService.run(processInstance)
		if(block.approval0) {
			sessionFactory.getCurrentSession().clear()
			taskService.approve(TaskInstance.list().get(0).id)
		}
		
		if(block.execute0) {
			sessionFactory.getCurrentSession().clear();
			taskService.complete(TaskInstance.list().get(0).id, 'testoutcome1')
		}
		
		then:
		block.approval1 == true
		approvalCount == 2
		executeCount == 1
		block.complete0 == true
		completeCount == 1
		sessionFactory.getCurrentSession().clear();
		TaskInstance.list().get(0).status == TaskStatus.SUCCESSFUL
		TaskInstance.list().get(1).status == TaskStatus.APPROVALREQUIRED
		
		cleanup:
		SpecHelpers.resetMetaClasses(savedMetaClasses)
		taskService.metaClass = TaskService.metaClass
	}
	
	def "Validate third and forth task in minimal process are started (forth executed no approver needed) after full completion of first 2"() {			
		setup:
		SpecHelpers.registerMetaClass(TaskService, savedMetaClasses)
		taskService.metaClass = TaskService.metaClass
		
		minimalDefinition = new File('test/data/minimal.pr').getText()
		processService.create(minimalDefinition)
		def processInstance = processService.initiate('Minimal Test Process', "Approving XYZ Widget", ProcessPriority.LOW, ['TEST_VAR':'VALUE_1', 'TEST_VAR2':'VALUE_2', 'TEST_VAR3':'VALUE_3'])
		
		BlockingVariables block = new BlockingVariables(4, TimeUnit.SECONDS)
		
		int approvalCount = 0
		def requestApproval = TaskService.metaClass.getMetaMethod("requestApproval", [Object])
		TaskService.metaClass.requestApproval = { def taskInstance ->
			requestApproval.invoke(delegate, taskInstance)
			block."approval$approvalCount" = true
			approvalCount++
		}
		
		int executeCount = 0
		def execute = TaskService.metaClass.getMetaMethod("execute", [Object])
		TaskService.metaClass.execute = { def taskInstanceID ->
			execute.invoke(delegate, taskInstanceID)
			block."execute$executeCount" = true
			executeCount++
		}
		
		int completeCount = 0
		def complete = TaskService.metaClass.getMetaMethod("complete", [Object, Object] as Class[])
		TaskService.metaClass.complete = { def taskInstanceID, def outcomeName ->
			complete.invoke(delegate, taskInstanceID, outcomeName)
			block."complete$completeCount" = true
			completeCount++
		}
			
		when:		
		processService.run(processInstance)
		if(block.approval0) {
			sessionFactory.getCurrentSession().clear()
			taskService.approve(TaskInstance.list().get(0).id)
		}
		
		if(block.execute0) {
			sessionFactory.getCurrentSession().clear();
			taskService.complete(TaskInstance.list().get(0).id, 'testoutcome1')
		}
		
		if(block.complete0 && block.approval1) {
			sessionFactory.getCurrentSession().clear()
			taskService.approve(TaskInstance.list().get(1).id)
		}
		
		if(block.execute1) {
			sessionFactory.getCurrentSession().clear();
			taskService.complete(TaskInstance.list().get(1).id, 'testoutcome2')
		}
		
		then:
		block.approval2 == true
		approvalCount == 3
		block.execute2 == true
		executeCount = 3
		sessionFactory.getCurrentSession().clear();
		TaskInstance.list().get(0).status == TaskStatus.SUCCESSFUL				// Task 1
		TaskInstance.list().get(1).status == TaskStatus.SUCCESSFUL				// Task 2
		TaskInstance.list().get(2).status == TaskStatus.APPROVALREQUIRED		// Task 3
		TaskInstance.list().get(3).status == TaskStatus.INPROGRESS				// Task 4
		
		cleanup:
		SpecHelpers.resetMetaClasses(savedMetaClasses)
		taskService.metaClass = TaskService.metaClass
	}
	
}