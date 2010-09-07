package fedreg.workflow

import grails.plugin.spock.*
import spock.util.concurrent.*
import java.util.concurrent.*

import org.apache.shiro.subject.Subject
import org.apache.shiro.util.ThreadContext
import org.apache.shiro.SecurityUtils

import grails.plugins.nimble.core.UserBase
import grails.plugins.nimble.core.ProfileBase
import grails.plugins.nimble.core.Role
import grails.plugins.nimble.core.Group

import com.icegreen.greenmail.util.*

import org.codehaus.groovy.runtime.InvokerHelper

class WorkflowTaskServiceSpec extends IntegrationSpec {
	
	def workflowProcessService
	def workflowTaskService
	def minimalDefinition
	def sessionFactory
	def savedMetaClasses
	def grailsApplication
	def greenMail
	
	def user
	def profile
	
	def setup() {
		savedMetaClasses = [:]
		
		profile = new ProfileBase(email:'test@testdomain.com')
		user = new UserBase(username:'testuser', profile: profile).save()

		SpecHelpers.setupShiroEnv(user)
	}
	
	def cleanup() {
		greenMail.deleteAllMessages()
		UserBase.findWhere(username:'testuser').delete()
		
		user = null
		profile = null
	}
	
	def "Validate first task in minimal process requires approval when process is run"() {
		setup:
		SpecHelpers.registerMetaClass(WorkflowTaskService, savedMetaClasses)
		workflowTaskService.metaClass = WorkflowTaskService.metaClass
		
		
		def result = new BlockingVariable<Boolean>(2, TimeUnit.SECONDS)
		
		def testScript = new WorkflowScript(name:'TestScript', description:'A script used in testing', definition:'return true', creator:workflowProcessService.authenticatedUser).save()
		minimalDefinition = new File('test/data/minimal.pr').getText()
		
		def originalMethod = WorkflowTaskService.metaClass.getMetaMethod("requestApproval", [Object])
		WorkflowTaskService.metaClass.requestApproval = { def taskInstanceID ->
			originalMethod.invoke(delegate, taskInstanceID)
			result.set(true)
		}
		
		def initiated, processInstance	// Spock bug, tuple assignment combined with cleanup explodes so we need to pre-declare
		
		when:
		workflowProcessService.create(minimalDefinition)
		(initiated, processInstance) = workflowProcessService.initiate('Minimal Test Process', "Approving XYZ Widget", ProcessPriority.LOW, ['TEST_VAR':'VALUE_1', 'TEST_VAR2':'VALUE_2', 'TEST_VAR3':'VALUE_3'])
		workflowProcessService.run(processInstance)
		
		then:
		result.get()
		processInstance.taskInstances.size() == 1
		TaskInstance.list().get(0).status == TaskStatus.APPROVALREQUIRED
		greenMail.getReceivedMessages().length == 1
		def message = greenMail.getReceivedMessages()[0]
		message.subject == 'fedreg.workflow.requestapproval.mail.subject'
		
		cleanup:
		SpecHelpers.resetMetaClasses(savedMetaClasses)
		workflowTaskService.metaClass = WorkflowTaskService.metaClass
	}
	
	def "Validate first task in minimal process requires approval when process is run and approval is provided to users in role1, also validates correct variable substition to workflow script"() {
		setup:
		SpecHelpers.registerMetaClass(WorkflowTaskService, savedMetaClasses)
		workflowTaskService.metaClass = WorkflowTaskService.metaClass
		
		def testScript = new WorkflowScript(name:'TestScript', description:'A script used in testing', definition:'return true', creator:workflowProcessService.authenticatedUser).save()
		minimalDefinition = new File('test/data/minimal.pr').getText()
		workflowProcessService.create(minimalDefinition)
		def initiated, processInstance
		(initiated, processInstance) = workflowProcessService.initiate('Minimal Test Process', "Approving XYZ Widget", ProcessPriority.LOW, ['TEST_VAR':'role1', 'TEST_VAR2':'VALUE_2', 'TEST_VAR3':'VALUE_3'])
		
		
		def result = new BlockingVariable<Boolean>(2, TimeUnit.SECONDS)
		
		def originalMethod = WorkflowTaskService.metaClass.getMetaMethod("requestApproval", [Object])
		WorkflowTaskService.metaClass.requestApproval = { def taskInstanceID ->
			originalMethod.invoke(delegate, taskInstanceID)
			result.set(true)
		}
		
		def role = new Role(name: 'role1')
		def profile2 = new ProfileBase(email:'test2@testdomain.com')
		def user2 = new UserBase(username:'testuser2', profile: profile2).save()
		role.addToUsers(user2)
		role.save()
		
		when:				
		workflowProcessService.run(processInstance)
		
		then:
		result.get() == true
		processInstance.taskInstances.size() == 1
		TaskInstance.list().get(0).status == TaskStatus.APPROVALREQUIRED
		greenMail.getReceivedMessages().length == 2
		def message = greenMail.getReceivedMessages()[0]
		'fedreg.workflow.requestapproval.mail.subject' == message.subject
		'test@testdomain.com' == GreenMailUtil.getAddressList(message.getRecipients(javax.mail.Message.RecipientType.TO))
		def message2 = greenMail.getReceivedMessages()[1]
		'fedreg.workflow.requestapproval.mail.subject' == message2.subject
		'test2@testdomain.com' == GreenMailUtil.getAddressList(message2.getRecipients(javax.mail.Message.RecipientType.TO))
		
		cleanup:
		SpecHelpers.resetMetaClasses(savedMetaClasses)
		workflowTaskService.metaClass = WorkflowTaskService.metaClass
	}
	
	def "Validate first task in minimal process requires approval when process is run and approval is provided to users in group1, also validates correct variable substition to workflow script"() {
		setup:
		SpecHelpers.registerMetaClass(WorkflowTaskService, savedMetaClasses)
		workflowTaskService.metaClass = WorkflowTaskService.metaClass
		
		def testScript = new WorkflowScript(name:'TestScript', description:'A script used in testing', definition:'return true', creator:workflowProcessService.authenticatedUser).save()
		minimalDefinition = new File('test/data/minimal.pr').getText()
		workflowProcessService.create(minimalDefinition)
		def initiated, processInstance
		(initiated, processInstance) = workflowProcessService.initiate('Minimal Test Process', "Approving XYZ Widget", ProcessPriority.LOW, ['TEST_VAR':'TEST_VAR1', 'TEST_VAR2':'group1', 'TEST_VAR3':'VALUE_3'])
		
		
		def result = new BlockingVariable<Boolean>(2, TimeUnit.SECONDS)
		
		def originalMethod = WorkflowTaskService.metaClass.getMetaMethod("requestApproval", [Object])
		WorkflowTaskService.metaClass.requestApproval = { def taskInstanceID ->
			originalMethod.invoke(delegate, taskInstanceID)
			result.set(true)
		}
		
		def group = new Group(name: 'group1')
		def profile2 = new ProfileBase(email:'test2@testdomain.com')
		def user2 = new UserBase(username:'testuser2', profile: profile2).save()
		group.addToUsers(user2)
		group.save()
		
		when:				
		workflowProcessService.run(processInstance)
		
		then:
		result.get() == true
		processInstance.taskInstances.size() == 1
		TaskInstance.list().get(0).status == TaskStatus.APPROVALREQUIRED
		greenMail.getReceivedMessages().length == 2
		def message = greenMail.getReceivedMessages()[0]
		'fedreg.workflow.requestapproval.mail.subject' == message.subject
		'test@testdomain.com' == GreenMailUtil.getAddressList(message.getRecipients(javax.mail.Message.RecipientType.TO))
		def message2 = greenMail.getReceivedMessages()[1]
		'fedreg.workflow.requestapproval.mail.subject' == message2.subject
		'test2@testdomain.com' == GreenMailUtil.getAddressList(message2.getRecipients(javax.mail.Message.RecipientType.TO))
		
		cleanup:
		SpecHelpers.resetMetaClasses(savedMetaClasses)
		workflowTaskService.metaClass = WorkflowTaskService.metaClass
	}

	def "Validate first task in minimal process executes when process is run and task approved"() {
		setup:
		SpecHelpers.registerMetaClass(WorkflowTaskService, savedMetaClasses)
		workflowTaskService.metaClass = WorkflowTaskService.metaClass
		BlockingVariables block = new BlockingVariables(4, TimeUnit.SECONDS)
		
		def testScript = new WorkflowScript(name:'TestScript', description:'A script used in testing', definition:'return true', creator:workflowProcessService.authenticatedUser).save()
		minimalDefinition = new File('test/data/minimal.pr').getText()
		workflowProcessService.create(minimalDefinition)
		def env = ['TEST_VAR':'VALUE_1', 'TEST_VAR2':'VALUE_2', 'TEST_VAR3':'VALUE_3']
		def initiated, processInstance
		(initiated, processInstance) = workflowProcessService.initiate('Minimal Test Process', "Approving XYZ Widget", ProcessPriority.LOW, env)
		
		def requestApproval = WorkflowTaskService.metaClass.getMetaMethod("requestApproval", [Object])
		WorkflowTaskService.metaClass.requestApproval = { def taskInstanceID ->
			requestApproval.invoke(delegate, taskInstanceID)
			block.approvalRequested = true
		}
		
		def execute = WorkflowTaskService.metaClass.getMetaMethod("execute", [Object])
		WorkflowTaskService.metaClass.execute = { def taskInstanceID ->
			execute.invoke(delegate, taskInstanceID)
			block.executed = true
		}
		
		when:				
		workflowProcessService.run(processInstance)
		if(block.approvalRequested) {
			workflowTaskService.approve(TaskInstance.list().get(0).id)
		}
		
		then:
		block.executed == true
		TaskInstance.list().get(0).status == TaskStatus.INPROGRESS
		
		cleanup:
		SpecHelpers.resetMetaClasses(savedMetaClasses)
		workflowTaskService.metaClass = WorkflowTaskService.metaClass
	}
	
	def "Validate task6 in minimal process executes when process is run and task1 rejected"() {
		setup:
		SpecHelpers.registerMetaClass(WorkflowTaskService, savedMetaClasses)
		workflowTaskService.metaClass = WorkflowTaskService.metaClass
		
		def testScript = new WorkflowScript(name:'TestScript', description:'A script used in testing', definition:'return true', creator:workflowProcessService.authenticatedUser).save()
		minimalDefinition = new File('test/data/minimal.pr').getText()
		workflowProcessService.create(minimalDefinition)
		def initiated, processInstance
		(initiated, processInstance) = workflowProcessService.initiate('Minimal Test Process', "Approving XYZ Widget", ProcessPriority.LOW, ['TEST_VAR':'VALUE_1', 'TEST_VAR2':'VALUE_2', 'TEST_VAR3':'VALUE_3'])
		
		BlockingVariables block = new BlockingVariables(4, TimeUnit.SECONDS)
		
		def requestApproval = WorkflowTaskService.metaClass.getMetaMethod("requestApproval", [Object])
		WorkflowTaskService.metaClass.requestApproval = { def taskInstanceID ->
			requestApproval.invoke(delegate, taskInstanceID)
			block.approvalRequested = true
		}
		
		def terminateAndStartTasks = WorkflowTaskService.metaClass.getMetaMethod("terminateAndStartTasks", [Object, Object] as Class[])
		WorkflowTaskService.metaClass.terminateAndStartTasks = { def taskInstance, def reaction ->
			block.terminateAndStartTasks = true
			block.terminates = reaction.terminate.size()
			block.starts = reaction.start.size()
			block.startTask6 = reaction.start.contains('task6')
		}
		
		when:				
		workflowProcessService.run(processInstance)
		if(block.approvalRequested) {
			workflowTaskService.reject(TaskInstance.list().get(0).id, 'rejection1')
		}
		
		then:
		block.terminateAndStartTasks == true
		block.terminates == 0
		block.starts == 1
		block.startTask6 == true
		
		TaskInstance.list().get(0).status == TaskStatus.APPROVALREJECTED
		
		cleanup:
		SpecHelpers.resetMetaClasses(savedMetaClasses)
		workflowTaskService.metaClass = WorkflowTaskService.metaClass
	}

	def "Validate first task in minimal process completes when process is run, the task is approved and execution is successful"() {
		setup:
		SpecHelpers.registerMetaClass(WorkflowTaskService, savedMetaClasses)
		workflowTaskService.metaClass = WorkflowTaskService.metaClass
		
		def testScript = new WorkflowScript(name:'TestScript', description:'A script used in testing', definition:'return true', creator:workflowProcessService.authenticatedUser).save()
		minimalDefinition = new File('test/data/minimal.pr').getText()
		workflowProcessService.create(minimalDefinition)
		def initiated, processInstance
		(initiated, processInstance) = workflowProcessService.initiate('Minimal Test Process', "Approving XYZ Widget", ProcessPriority.LOW, ['TEST_VAR':'VALUE_1', 'TEST_VAR2':'VALUE_2', 'TEST_VAR3':'VALUE_3'])
		
		BlockingVariables block = new BlockingVariables(4, TimeUnit.SECONDS)
		
		def requestApproval = WorkflowTaskService.metaClass.getMetaMethod("requestApproval", [Object])
		WorkflowTaskService.metaClass.requestApproval = { def taskInstanceID ->
			requestApproval.invoke(delegate, taskInstanceID)
			block.approvalRequested = true
		}
		
		def execute = WorkflowTaskService.metaClass.getMetaMethod("execute", [Object])
		WorkflowTaskService.metaClass.execute = { def taskInstanceID ->
			//execute.invoke(delegate, taskInstanceID)
			block.executed = true
		}
		
		def complete = WorkflowTaskService.metaClass.getMetaMethod("complete", [Object, Object] as Class[])
		WorkflowTaskService.metaClass.complete = { def taskInstanceID, def outcomeName ->
			block.complete = true
			block.outcome = outcomeName
		}
		
		when:				
		workflowProcessService.run(processInstance)
		if(block.approvalRequested) {
			workflowTaskService.approve(TaskInstance.list().get(0).id)
		}
		
		if(block.executed) {
			workflowTaskService.complete(TaskInstance.list().get(0).id, 'testoutcome1')
		}
		
		then:
		block.complete == true
		block.outcome == 'testoutcome1'
		
		cleanup:
		SpecHelpers.resetMetaClasses(savedMetaClasses)
		workflowTaskService.metaClass = WorkflowTaskService.metaClass
	}
	
	def "Validate second task in minimal process is started after full completion of first"() {
		setup:
		SpecHelpers.registerMetaClass(WorkflowTaskService, savedMetaClasses)
		workflowTaskService.metaClass = WorkflowTaskService.metaClass
		
		def testScript = new WorkflowScript(name:'TestScript', description:'A script used in testing', definition:'return true', creator:workflowProcessService.authenticatedUser).save()
		minimalDefinition = new File('test/data/minimal.pr').getText()
		workflowProcessService.create(minimalDefinition)
		def initiated, processInstance
		(initiated, processInstance) = workflowProcessService.initiate('Minimal Test Process', "Approving XYZ Widget", ProcessPriority.LOW, ['TEST_VAR':'VALUE_1', 'TEST_VAR2':'VALUE_2', 'TEST_VAR3':'VALUE_3'])
		
		BlockingVariables block = new BlockingVariables(4, TimeUnit.SECONDS)
		
		int approvalCount = 0
		def requestApproval = WorkflowTaskService.metaClass.getMetaMethod("requestApproval", [Object])
		WorkflowTaskService.metaClass.requestApproval = { def taskInstance ->
			requestApproval.invoke(delegate, taskInstance)	
			block."approval$approvalCount" = true
			approvalCount++
		}
		
		int executeCount = 0
		def execute = WorkflowTaskService.metaClass.getMetaMethod("execute", [Object])
		WorkflowTaskService.metaClass.execute = { def taskInstanceID ->
			//execute.invoke(delegate, taskInstanceID)
			block."execute$executeCount" = true
			executeCount++
		}
		
		int completeCount = 0
		def complete = WorkflowTaskService.metaClass.getMetaMethod("complete", [Object, Object] as Class[])
		WorkflowTaskService.metaClass.complete = { def taskInstanceID, def outcomeName ->
			complete.invoke(delegate, taskInstanceID, outcomeName)
			block."complete$completeCount" = true
			completeCount++
		}
		
		when:				
		workflowProcessService.run(processInstance)
		if(block.approval0) {
			workflowTaskService.approve(TaskInstance.list().get(0).id)
		}
		
		if(block.execute0) {
			workflowTaskService.complete(TaskInstance.list().get(0).id, 'testoutcome1')
		}
		
		then:
		block.approval1 == true
		approvalCount == 2
		executeCount == 1
		block.complete0 == true
		completeCount == 1
		TaskInstance.list().get(0).status == TaskStatus.SUCCESSFUL
		TaskInstance.list().get(1).status == TaskStatus.APPROVALREQUIRED
		
		cleanup:
		SpecHelpers.resetMetaClasses(savedMetaClasses)
		workflowTaskService.metaClass = WorkflowTaskService.metaClass
	}
	
	def "Validate third and forth task in minimal process are started (forth executed no approver needed) after full completion of first 2"() {			
		setup:
		SpecHelpers.registerMetaClass(WorkflowTaskService, savedMetaClasses)
		workflowTaskService.metaClass = WorkflowTaskService.metaClass
		
		def testScript = new WorkflowScript(name:'TestScript', description:'A script used in testing', definition:'return true', creator:workflowProcessService.authenticatedUser).save()
		minimalDefinition = new File('test/data/minimal.pr').getText()
		workflowProcessService.create(minimalDefinition)
		def initiated, processInstance
		(initiated, processInstance) = workflowProcessService.initiate('Minimal Test Process', "Approving XYZ Widget", ProcessPriority.LOW, ['TEST_VAR':'VALUE_1', 'TEST_VAR2':'VALUE_2', 'TEST_VAR3':'VALUE_3'])
		
		BlockingVariables block = new BlockingVariables(4, TimeUnit.SECONDS)
		
		int approvalCount = 0
		def requestApproval = WorkflowTaskService.metaClass.getMetaMethod("requestApproval", [Object])
		WorkflowTaskService.metaClass.requestApproval = { def taskInstance ->
			requestApproval.invoke(delegate, taskInstance)
			block."approval$approvalCount" = true
			approvalCount++
		}
		
		int executeCount = 0
		def execute = WorkflowTaskService.metaClass.getMetaMethod("execute", [Object])
		WorkflowTaskService.metaClass.execute = { def taskInstanceID ->
			execute.invoke(delegate, taskInstanceID)
			block."execute$executeCount" = true
			executeCount++
		}
		
		int completeCount = 0
		def complete = WorkflowTaskService.metaClass.getMetaMethod("complete", [Object, Object] as Class[])
		WorkflowTaskService.metaClass.complete = { def taskInstanceID, def outcomeName ->
			complete.invoke(delegate, taskInstanceID, outcomeName)
			block."complete$completeCount" = true
			completeCount++
		}
			
		when:		
		workflowProcessService.run(processInstance)
		if(block.approval0) {
			workflowTaskService.approve(TaskInstance.list().get(0).id)
		}
		
		if(block.execute0) {
			workflowTaskService.complete(TaskInstance.list().get(0).id, 'testoutcome1')
		}
		
		if(block.complete0 && block.approval1) {
			workflowTaskService.approve(TaskInstance.list().get(1).id)
		}
		
		if(block.execute1) {
			workflowTaskService.complete(TaskInstance.list().get(1).id, 'testoutcome2')
		}
		
		then:
		block.approval2 == true
		approvalCount == 3
		block.execute2 == true
		executeCount = 3
		TaskInstance.list().get(0).status == TaskStatus.SUCCESSFUL				// Task 1
		TaskInstance.list().get(1).status == TaskStatus.SUCCESSFUL				// Task 2
		TaskInstance.list().get(2).status == TaskStatus.APPROVALREQUIRED		// Task 3
		TaskInstance.list().get(3).status == TaskStatus.INPROGRESS				// Task 4
		
		cleanup:
		SpecHelpers.resetMetaClasses(savedMetaClasses)
		workflowTaskService.metaClass = WorkflowTaskService.metaClass
	}
	
	def "Validate fifth task is not executed before both task three and four are complete"() {			
		setup:
		SpecHelpers.registerMetaClass(WorkflowTaskService, savedMetaClasses)
		workflowTaskService.metaClass = WorkflowTaskService.metaClass
		
		def testScript = new WorkflowScript(name:'TestScript', description:'A script used in testing', definition:'return true', creator:workflowProcessService.authenticatedUser).save()
		minimalDefinition = new File('test/data/minimal.pr').getText()
		workflowProcessService.create(minimalDefinition)
		def initiated, processInstance
		(initiated, processInstance) = workflowProcessService.initiate('Minimal Test Process', "Approving XYZ Widget", ProcessPriority.LOW, ['TEST_VAR':'VALUE_1', 'TEST_VAR2':'VALUE_2', 'TEST_VAR3':'VALUE_3'])
		
		BlockingVariables block = new BlockingVariables(4, TimeUnit.SECONDS)
		
		int approvalCount = 0
		def requestApproval = WorkflowTaskService.metaClass.getMetaMethod("requestApproval", [Object])
		WorkflowTaskService.metaClass.requestApproval = { def taskInstance ->
			requestApproval.invoke(delegate, taskInstance)
			block."approval$approvalCount" = true
			approvalCount++
		}
		
		int executeCount = 0
		def execute = WorkflowTaskService.metaClass.getMetaMethod("execute", [Object])
		WorkflowTaskService.metaClass.execute = { def taskInstanceID ->
			execute.invoke(delegate, taskInstanceID)
			block."execute$executeCount" = true
			executeCount++
		}
		
		int completeCount = 0
		def complete = WorkflowTaskService.metaClass.getMetaMethod("complete", [Object, Object] as Class[])
		WorkflowTaskService.metaClass.complete = { def taskInstanceID, def outcomeName ->
			complete.invoke(delegate, taskInstanceID, outcomeName)
			block."complete$completeCount" = true
			completeCount++
		}
			
		when:		
		workflowProcessService.run(processInstance)
		if(block.approval0) {
			workflowTaskService.approve(TaskInstance.list().get(0).id)
		}
		
		if(block.execute0) {
			workflowTaskService.complete(TaskInstance.list().get(0).id, 'testoutcome1')
		}
		
		if(block.complete0 && block.approval1) {
			workflowTaskService.approve(TaskInstance.list().get(1).id)
		}
		
		if(block.execute1) {
			workflowTaskService.complete(TaskInstance.list().get(1).id, 'testoutcome2')
		}
		
		if(block.execute2) {
			workflowTaskService.complete(TaskInstance.list().get(3).id, 'testoutcome5')
		}
		
		then:
		block.approval2 == true
		approvalCount == 3
		block.execute2 == true
		executeCount = 3
		TaskInstance.list().get(0).status == TaskStatus.SUCCESSFUL				// Task 1
		TaskInstance.list().get(1).status == TaskStatus.SUCCESSFUL				// Task 2
		TaskInstance.list().get(2).status == TaskStatus.APPROVALREQUIRED		// Task 3
		TaskInstance.list().get(3).status == TaskStatus.SUCCESSFUL				// Task 4
		TaskInstance.list().get(4).status == TaskStatus.DEPENDENCYWAIT			// Task 5
		TaskInstance.list().size() == 5
		
		cleanup:
		SpecHelpers.resetMetaClasses(savedMetaClasses)
		workflowTaskService.metaClass = WorkflowTaskService.metaClass
	}
	
	def "Validate fifth task automatically executes when three and four are complete"() {			
		setup:
		SpecHelpers.registerMetaClass(WorkflowTaskService, savedMetaClasses)
		workflowTaskService.metaClass = WorkflowTaskService.metaClass
		
		def testScript = new WorkflowScript(name:'TestScript', description:'A script used in testing', definition:'return true', creator:workflowProcessService.authenticatedUser).save()
		minimalDefinition = new File('test/data/minimal.pr').getText()
		workflowProcessService.create(minimalDefinition)
		def initiated, processInstance
		(initiated, processInstance) = workflowProcessService.initiate('Minimal Test Process', "Approving XYZ Widget", ProcessPriority.LOW, ['TEST_VAR':'VALUE_1', 'TEST_VAR2':'VALUE_2', 'TEST_VAR3':'VALUE_3'])
		
		BlockingVariables block = new BlockingVariables(4, TimeUnit.SECONDS)
		
		int approvalCount = 0
		def requestApproval = WorkflowTaskService.metaClass.getMetaMethod("requestApproval", [Object])
		WorkflowTaskService.metaClass.requestApproval = { def taskInstance ->
			requestApproval.invoke(delegate, taskInstance)
			block."approval$approvalCount" = true
			approvalCount++
		}
		
		int executeCount = 0
		def execute = WorkflowTaskService.metaClass.getMetaMethod("execute", [Object])
		WorkflowTaskService.metaClass.execute = { def taskInstanceID ->
			execute.invoke(delegate, taskInstanceID)
			block."execute$executeCount" = true
			executeCount++
		}
		
		int completeCount = 0
		def complete = WorkflowTaskService.metaClass.getMetaMethod("complete", [Object, Object] as Class[])
		WorkflowTaskService.metaClass.complete = { def taskInstanceID, def outcomeName ->
			complete.invoke(delegate, taskInstanceID, outcomeName)
			block."complete$completeCount" = true
			completeCount++
		}
			
		when:		
		workflowProcessService.run(processInstance)
		if(block.approval0) {
			workflowTaskService.approve(TaskInstance.list().get(0).id)
		}
		
		if(block.execute0) {
			workflowTaskService.complete(TaskInstance.list().get(0).id, 'testoutcome1')
		}
		
		if(block.complete0 && block.approval1) {
			workflowTaskService.approve(TaskInstance.list().get(1).id)
		}
		
		if(block.execute1) {
			workflowTaskService.complete(TaskInstance.list().get(1).id, 'testoutcome2')
		}
		
		if(block.complete1) {
			workflowTaskService.approve(TaskInstance.list().get(2).id)
		}
		if(block.execute2) {
			workflowTaskService.complete(TaskInstance.list().get(3).id, 'testoutcome5')
		}
		
		then:
		block.approval2 == true
		approvalCount == 3
		block.execute2 == true
		executeCount = 3
		block.complete2 == true
		block.complete3 == true
		completeCount == 4
		TaskInstance.list().get(0).status == TaskStatus.SUCCESSFUL			// Task 1
		TaskInstance.list().get(1).status == TaskStatus.SUCCESSFUL			// Task 2
		TaskInstance.list().get(2).status == TaskStatus.SUCCESSFUL			// Task 3
		TaskInstance.list().get(3).status == TaskStatus.SUCCESSFUL			// Task 4
		TaskInstance.list().get(4).status == TaskStatus.INPROGRESS			// Task 5
		TaskInstance.list().size() == 5
		
		cleanup:
		SpecHelpers.resetMetaClasses(savedMetaClasses)
		workflowTaskService.metaClass = WorkflowTaskService.metaClass
	}
	
	def "Validate sixth task finalizes when task five is complete"() {			
		setup:
		SpecHelpers.registerMetaClass(WorkflowTaskService, savedMetaClasses)
		workflowTaskService.metaClass = WorkflowTaskService.metaClass
		
		def testScript = new WorkflowScript(name:'TestScript', description:'A script used in testing', definition:'return true', creator:workflowProcessService.authenticatedUser).save()
		minimalDefinition = new File('test/data/minimal.pr').getText()
		workflowProcessService.create(minimalDefinition)
		def initiated, processInstance
		(initiated, processInstance) = workflowProcessService.initiate('Minimal Test Process', "Approving XYZ Widget", ProcessPriority.LOW, ['TEST_VAR':'VALUE_1', 'TEST_VAR2':'VALUE_2', 'TEST_VAR3':'VALUE_3'])
		
		BlockingVariables block = new BlockingVariables(4, TimeUnit.SECONDS)
		
		int approvalCount = 0
		def requestApproval = WorkflowTaskService.metaClass.getMetaMethod("requestApproval", [Object])
		WorkflowTaskService.metaClass.requestApproval = { def taskInstance ->
			requestApproval.invoke(delegate, taskInstance)
			block."approval$approvalCount" = true
			approvalCount++
		}
		
		int executeCount = 0
		def execute = WorkflowTaskService.metaClass.getMetaMethod("execute", [Object])
		WorkflowTaskService.metaClass.execute = { def taskInstanceID ->
			execute.invoke(delegate, taskInstanceID)
			block."execute$executeCount" = true
			executeCount++
		}
		
		int completeCount = 0
		def complete = WorkflowTaskService.metaClass.getMetaMethod("complete", [Object, Object] as Class[])
		WorkflowTaskService.metaClass.complete = { def taskInstanceID, def outcomeName ->
			complete.invoke(delegate, taskInstanceID, outcomeName)
			block."complete$completeCount" = true
			completeCount++
		}
			
		when:		
		workflowProcessService.run(processInstance)
		if(block.approval0) {
			workflowTaskService.approve(TaskInstance.list().get(0).id)
		}
		
		if(block.execute0) {
			workflowTaskService.complete(TaskInstance.list().get(0).id, 'testoutcome1')
		}
		
		if(block.complete0 && block.approval1) {
			workflowTaskService.approve(TaskInstance.list().get(1).id)
		}
		
		if(block.execute1) {
			workflowTaskService.complete(TaskInstance.list().get(1).id, 'testoutcome2')
		}
		
		if(block.complete1) {
			workflowTaskService.approve(TaskInstance.list().get(2).id)
		}
		if(block.execute2) {
			workflowTaskService.complete(TaskInstance.list().get(3).id, 'testoutcome5')
		}
		
		if(block.execute3) {
			workflowTaskService.complete(TaskInstance.list().get(4).id, 'testoutcome6')
		}
		
		then:
		block.approval2 == true
		approvalCount == 3
		block.execute2 == true
		executeCount = 3
		block.complete4 == true
		completeCount == 5
		TaskInstance.list().get(0).status == TaskStatus.SUCCESSFUL			// Task 1
		TaskInstance.list().get(1).status == TaskStatus.SUCCESSFUL			// Task 2
		TaskInstance.list().get(2).status == TaskStatus.SUCCESSFUL			// Task 3
		TaskInstance.list().get(3).status == TaskStatus.SUCCESSFUL			// Task 4
		TaskInstance.list().get(4).status == TaskStatus.SUCCESSFUL			// Task 5
		TaskInstance.list().get(5).status == TaskStatus.FINALIZED			// Task 6
		TaskInstance.list().size() == 6
		
		cleanup:
		SpecHelpers.resetMetaClasses(savedMetaClasses)
		workflowTaskService.metaClass = WorkflowTaskService.metaClass
	}
	
	def "Validate initial process definition is run to completion even when process is updated mid run"() {			
		setup:
		SpecHelpers.registerMetaClass(WorkflowTaskService, savedMetaClasses)
		workflowTaskService.metaClass = WorkflowTaskService.metaClass
		
		def testScript = new WorkflowScript(name:'TestScript', description:'A script used in testing', definition:'return true', creator:workflowProcessService.authenticatedUser).save()
		minimalDefinition = new File('test/data/minimal.pr').getText()
		def updatedDefinition = new File('test/data/minimal4.pr').getText()
		workflowProcessService.create(minimalDefinition)
		def initiated, processInstance
		(initiated, processInstance) = workflowProcessService.initiate('Minimal Test Process', "Approving XYZ Widget", ProcessPriority.LOW, ['TEST_VAR':'VALUE_1', 'TEST_VAR2':'VALUE_2', 'TEST_VAR3':'VALUE_3'])
		
		BlockingVariables block = new BlockingVariables(4, TimeUnit.SECONDS)
		
		int approvalCount = 0
		def requestApproval = WorkflowTaskService.metaClass.getMetaMethod("requestApproval", [Object])
		WorkflowTaskService.metaClass.requestApproval = { def taskInstance ->
			requestApproval.invoke(delegate, taskInstance)
			block."approval$approvalCount" = true
			approvalCount++
		}
		
		int executeCount = 0
		def execute = WorkflowTaskService.metaClass.getMetaMethod("execute", [Object])
		WorkflowTaskService.metaClass.execute = { def taskInstanceID ->
			execute.invoke(delegate, taskInstanceID)
			block."execute$executeCount" = true
			executeCount++
		}
		
		int completeCount = 0
		def complete = WorkflowTaskService.metaClass.getMetaMethod("complete", [Object, Object] as Class[])
		WorkflowTaskService.metaClass.complete = { def taskInstanceID, def outcomeName ->
			complete.invoke(delegate, taskInstanceID, outcomeName)
			block."complete$completeCount" = true
			completeCount++
		}
			
		when:		
		workflowProcessService.run(processInstance)
		if(block.approval0) {
			workflowTaskService.approve(TaskInstance.list().get(0).id)
		}
		
		if(block.execute0) {
			workflowTaskService.complete(TaskInstance.list().get(0).id, 'testoutcome1')
		}
		
		workflowProcessService.update('Minimal Test Process', updatedDefinition)
		
		if(block.complete0 && block.approval1) {
			workflowTaskService.approve(TaskInstance.list().get(1).id)
		}
		
		if(block.execute1) {
			workflowTaskService.complete(TaskInstance.list().get(1).id, 'testoutcome2')
		}
		
		if(block.complete1) {
			workflowTaskService.approve(TaskInstance.list().get(2).id)
		}
		if(block.execute2) {
			workflowTaskService.complete(TaskInstance.list().get(3).id, 'testoutcome5')
		}
		
		if(block.execute3) {
			workflowTaskService.complete(TaskInstance.list().get(4).id, 'testoutcome6')
		}
		
		then:
		block.approval2 == true
		approvalCount == 3
		block.execute2 == true
		executeCount = 3
		block.complete4 == true
		completeCount == 5
		TaskInstance.list().get(0).status == TaskStatus.SUCCESSFUL			// Task 1
		TaskInstance.list().get(1).status == TaskStatus.SUCCESSFUL			// Task 2
		TaskInstance.list().get(2).status == TaskStatus.SUCCESSFUL			// Task 3
		TaskInstance.list().get(3).status == TaskStatus.SUCCESSFUL			// Task 4
		TaskInstance.list().get(4).status == TaskStatus.SUCCESSFUL			// Task 5
		TaskInstance.list().get(5).status == TaskStatus.FINALIZED			// Task 6
		TaskInstance.list().size() == 6
		Process.countByName('Minimal Test Process') == 2
		TaskInstance.list().get(0).task.description == 'Description of task1'
		processInstance.process != Process.findWhere(name: 'Minimal Test Process', active: true)
		
		cleanup:
		SpecHelpers.resetMetaClasses(savedMetaClasses)
		workflowTaskService.metaClass = WorkflowTaskService.metaClass
	}
	
	def "Ensure all task instances that a user can approve are returned when requested"() {
		setup:
		def profile2 = new ProfileBase(email:'test2@testdomain.com')
		def user2 = new UserBase(username:'testuser2', profile: profile)
		user2.save()
		
		def process = new Process(name:'test', description:'test description', processVersion:1, definition: 'return true', creator: user)
		def task = new Task(process:process, name:"test task", description:"test task description", finishOnThisTask:true)
		process.addToTasks(task)
		process.save()
		
		def processInstance = new ProcessInstance(process:process, description: "process instance description", priority: ProcessPriority.LOW, status:ProcessStatus.INPROGRESS)
		(1..5).each { i ->					
			def ti = new TaskInstance(processInstance:processInstance, task:task, status:TaskStatus.DEPENDENCYWAIT)
			ti.addToPotentialApprovers(user)
			processInstance.addToTaskInstances(ti)
		}
	
		(1..5).each { i ->					
			def ti = new TaskInstance(processInstance:processInstance, task:task, status:TaskStatus.APPROVALREQUIRED)
			ti.addToPotentialApprovers(user)
			processInstance.addToTaskInstances(ti)
		}
			
		(1..5).each { i ->					
			def ti = new TaskInstance(processInstance:processInstance, task:task, status:TaskStatus.APPROVALREQUIRED)
			ti.addToPotentialApprovers(user2)
			processInstance.addToTaskInstances(ti)
		}
		
		(1..5).each { i ->					
			def ti = new TaskInstance(processInstance:processInstance, task:task, status:TaskStatus.APPROVALGRANTED)
			ti.addToPotentialApprovers(user)
			processInstance.addToTaskInstances(ti)
		}
		
		processInstance.save()
		
		
		when:
		def tasks = workflowTaskService.retrieveTasksAwaitingApproval(user)
		
		then:
		TaskInstance.count() == 20
		tasks.size() == 5
	}
}