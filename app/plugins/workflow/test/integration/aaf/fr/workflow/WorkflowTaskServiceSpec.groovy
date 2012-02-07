package aaf.fr.workflow

import grails.plugin.spock.*
import spock.util.concurrent.*
import java.util.concurrent.*

import org.apache.shiro.subject.Subject
import org.apache.shiro.util.ThreadContext
import org.apache.shiro.SecurityUtils

import com.icegreen.greenmail.util.*
import org.codehaus.groovy.runtime.InvokerHelper

import grails.plugins.federatedgrails.Role

import aaf.fr.identity.Subject

class WorkflowTaskServiceSpec extends IntegrationSpec {
	
	def workflowProcessService
	def workflowTaskService
	def minimalDefinition
	def sessionFactory
	def savedMetaClasses
	def grailsApplication
	def greenMail
	
	def user
	
	def setup() {
		savedMetaClasses = [:]
    		
    def role = new Role(name:'allsubjects')
    user = new Subject(principal:'testuser', email:'test@testdomain.com')
    role.addToSubjects(user)
    user.save()

		SpecHelpers.setupShiroEnv(user)

    new Role(name:'VALUE_1').save()
    new Role(name:'VALUE_2').save()
    new Role(name:'VALUE_3').save()
    new Role(name:'VALUE_4').save()
    new Role(name:'VALUE_5').save()
    new Role(name:'VALUE_6').save()
    new Role(name:'TEST_ROLE').save()
	}
	
	def cleanup() {
		greenMail.deleteAllMessages()
		
		user = null
	}
	
	def "Validate first task in minimal process requires approval when process is run"() {
		setup:
		def testScript = new WorkflowScript(name:'TestScript', description:'A script used in testing', definition:'return true', creator:user)
    testScript.save()

		minimalDefinition = new File('test/data/minimal.pr').getText()
		
		WorkflowTaskService.metaClass.messageApprovalRequest = { def user, def taskInstance -> } // Not rendering due to bug in Grails 1.3.6, try removing this in future versions
		def initiated, processInstance	// Spock bug, tuple assignment combined with cleanup explodes so we need to pre-declare
		
		when:
		workflowProcessService.create(minimalDefinition)
		(initiated, processInstance) = workflowProcessService.initiate('Minimal Test Process', "Approving XYZ Widget", ProcessPriority.LOW, ['TEST_VAR':'VALUE_1', 'TEST_VAR2':'VALUE_2', 'TEST_VAR3':'VALUE_3'])

		workflowProcessService.run(processInstance)

		then:
		processInstance.taskInstances.size() == 1
		processInstance.taskInstances.get(0).status == TaskStatus.APPROVALREQUIRED
    processInstance.taskInstances.get(0).potentialApprovers.size() == 1
		greenMail.getReceivedMessages().length == 1
		def message = greenMail.getReceivedMessages()[0]
		message.subject == 'fedreg.templates.mail.workflow.requestapproval.subject'
	}

	def "Validate first task in minimal process requires approval when process is run and approval is provided to users in role1, also validates correct variable substition to workflow script"() {
		setup:
		SpecHelpers.registerMetaClass(WorkflowTaskService, savedMetaClasses)
		workflowTaskService.metaClass = WorkflowTaskService.metaClass
		
		def testScript = new WorkflowScript(name:'TestScript', description:'A script used in testing', definition:'return true', creator:user).save()
		minimalDefinition = new File('test/data/minimal.pr').getText()
		workflowProcessService.create(minimalDefinition)
		def initiated, processInstance
		(initiated, processInstance) = workflowProcessService.initiate('Minimal Test Process', "Approving XYZ Widget", ProcessPriority.LOW, ['TEST_VAR':'role1', 'TEST_VAR2':'VALUE_2', 'TEST_VAR3':'VALUE_3'])
		
		def role = new Role(name: 'role1')
		def user2 = new Subject(principal:'testuser2', email:'test2@testdomain.com').save()
		role.addToSubjects(user2)
		role.save()
		
		WorkflowTaskService.metaClass.messageApprovalRequest = { def user, def taskInstance -> } // Not rendering due to bug in Grails 1.3.6, try removing this in future versions
		
		when:				
		workflowProcessService.run(processInstance)
		
		then:
		processInstance.taskInstances.size() == 1
		processInstance.taskInstances.get(0).status == TaskStatus.APPROVALREQUIRED
    processInstance.taskInstances.get(0).potentialApprovers.size() == 2
		greenMail.getReceivedMessages().length == 2
		def message = greenMail.getReceivedMessages()[0]
		'fedreg.templates.mail.workflow.requestapproval.subject' == message.subject
		'test@testdomain.com' == GreenMailUtil.getAddressList(message.getRecipients(javax.mail.Message.RecipientType.TO))
		def message2 = greenMail.getReceivedMessages()[1]
		'fedreg.templates.mail.workflow.requestapproval.subject' == message2.subject
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
		
		def testScript = new WorkflowScript(name:'TestScript', description:'A script used in testing', definition:'return true', creator:user).save()
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
		
		WorkflowTaskService.metaClass.messageApprovalRequest = { def user, def taskInstance -> } // Not rendering due to bug in Grails 1.3.6, try removing this in future versions
		
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
		
		def testScript = new WorkflowScript(name:'TestScript', description:'A script used in testing', definition:'return true', creator:user).save()
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
		
		WorkflowTaskService.metaClass.messageApprovalRequest = { def user, def taskInstance -> } // Not rendering due to bug in Grails 1.3.6, try removing this in future versions
		
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
		
		def testScript = new WorkflowScript(name:'TestScript', description:'A script used in testing', definition:'return true', creator:user).save()
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
		
		WorkflowTaskService.metaClass.messageApprovalRequest = { def user, def taskInstance -> } // Not rendering due to bug in Grails 1.3.6, try removing this in future versions
		
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
		
		def testScript = new WorkflowScript(name:'TestScript', description:'A script used in testing', definition:'return true', creator:user).save()
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
		
		WorkflowTaskService.metaClass.messageApprovalRequest = { def user, def taskInstance -> } // Not rendering due to bug in Grails 1.3.6, try removing this in future versions
		
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
		
		def testScript = new WorkflowScript(name:'TestScript', description:'A script used in testing', definition:'return true', creator:user).save()
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
		
		WorkflowTaskService.metaClass.messageApprovalRequest = { def user, def taskInstance -> } // Not rendering due to bug in Grails 1.3.6, try removing this in future versions
			
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
		executeCount == 3
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
		
		def testScript = new WorkflowScript(name:'TestScript', description:'A script used in testing', definition:'return true', creator:user).save()
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
		
		WorkflowTaskService.metaClass.messageApprovalRequest = { def user, def taskInstance -> } // Not rendering due to bug in Grails 1.3.6, try removing this in future versions
			
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
		executeCount == 3
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
		
		def testScript = new WorkflowScript(name:'TestScript', description:'A script used in testing', definition:'return true', creator:user).save()
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
		
		WorkflowTaskService.metaClass.messageApprovalRequest = { def user, def taskInstance -> } // Not rendering due to bug in Grails 1.3.6, try removing this in future versions
			
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
		executeCount == 4
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
		
		def testScript = new WorkflowScript(name:'TestScript', description:'A script used in testing', definition:'return true', creator:user).save()
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
		
		WorkflowTaskService.metaClass.messageApprovalRequest = { def user, def taskInstance -> } // Not rendering due to bug in Grails 1.3.6, try removing this in future versions
			
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
		executeCount == 4
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
		
		def testScript = new WorkflowScript(name:'TestScript', description:'A script used in testing', definition:'return true', creator:user).save()
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
		
		WorkflowTaskService.metaClass.messageApprovalRequest = { def user, def taskInstance -> } // Not rendering due to bug in Grails 1.3.6, try removing this in future versions
			
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
		executeCount == 4
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
		def user2 = new Subject(principal:'testuser2', email:'test2@testdomain.com')
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