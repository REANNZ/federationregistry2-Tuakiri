package aaf.fr.workflow

import grails.plugin.spock.*

import aaf.fr.identity.Subject

class TaskSpec extends IntegrationSpec {
	static transactional = true
	
	def "Ensure non finish task with no approver, approverRoles, approverGroups or execute fails"() {
		setup: 
		def process = new Process(name:'test process', description:'test process')
		def task = new Task(name:'test', description:'test description', finishOnThisTask:false, process:process)
		def task2 = new Task(name:'test2', description:'test2 description', finishOnThisTask:true, process:process)
		def taskOutcome = new TaskOutcome(name:'testOutcomeVal', description:'testing outcome').addToStart('test2')
		task.outcomes.put('testOutcomeVal', taskOutcome)
		
		when:
		def result = task.validate()
		
		then:
		!result
		task.errors.getFieldError('execute').code == 'task.validation.invalid.directive.set'
		task.errors.getFieldError('approvers').code == 'task.validation.directives.approvers.invalid'
		task.errors.getFieldError('approverGroups').code == 'task.validation.directives.approvers.invalid'
		task.errors.getFieldError('approverRoles').code == 'task.validation.directives.approvers.invalid'
	}
	
	def "Ensure non finish task with only approver is valid"() {
		setup: 
		def process = new Process(name:'test process', description:'test process')
		def task = new Task(name:'test', description:'test description', finishOnThisTask:false, process:process)
		task.addToApprovers('userID')
		task.rejections.put('testReject', new TaskRejection(name:'test rejection', description:'test rejection description').addToStart('test'))
		def task2 = new Task(name:'test2', description:'test2 description', finishOnThisTask:true, process:process)
		def taskOutcome = new TaskOutcome(name:'testOutcomeVal', description:'testing outcome').addToStart('test2')
		task.outcomes.put("testOutcomeVal", taskOutcome)
		
		when:
		def result = task.validate()
		
		then:
		result
	}
	
	def "Ensure non finish task with only approverGroup is valid"() {
		setup: 
		def process = new Process(name:'test process', description:'test process')
		def task = new Task(name:'test', description:'test description', finishOnThisTask:false, process:process)
		task.addToApproverGroups('userID')
		task.rejections.put('testReject', new TaskRejection(name:'test rejection', description:'test rejection description').addToStart('test'))
		def task2 = new Task(name:'test2', description:'test2 description', finishOnThisTask:true, process:process)
		def taskOutcome = new TaskOutcome(name:'testOutcomeVal', description:'testing outcome').addToStart('test2')
		task.outcomes.put('testOutcomeVal', taskOutcome)
		
		when:
		def result = task.validate()
		
		then:
		result
	}
	
	def "Ensure non finish task with only approverRole is valid"() {
		setup: 
		def process = new Process(name:'test process', description:'test process')
		def task = new Task(name:'test', description:'test description', finishOnThisTask:false, process:process)
		task.addToApproverRoles('userID')
		task.rejections.put('testReject', new TaskRejection(name:'test rejection', description:'test rejection description').addToStart('test'))
		def task2 = new Task(name:'test2', description:'test2 description', finishOnThisTask:true, process:process)
		def taskOutcome = new TaskOutcome(name:'testOutcomeVal', description:'testing outcome').addToStart('test2')
		task.outcomes.put('testOutcomeVal', taskOutcome)
		
		when:
		def result = task.validate()
		
		then:
		result
	}
	
	def "Ensure non finish task with approver but no reject is invalid"() {
		setup: 
		def process = new Process(name:'test process', description:'test process')
		def task = new Task(name:'test', description:'test description', finishOnThisTask:false, process:process)
		task.addToApprovers('userID')
		def task2 = new Task(name:'test2', description:'test2 description', finishOnThisTask:true, process:process)
		def taskOutcome = new TaskOutcome(name:'testOutcomeVal', description:'testing outcome').addToStart('test2')
		task.outcomes.put("testOutcomeVal", taskOutcome)
		
		when:
		def result = task.validate()
		
		then:
		!result
		task.errors.getFieldError('rejections').code == 'task.validation.approval.rejections.invalid.count'
	}
	
	def "Ensure non finish task with approverGroup but no reject is invalid"() {
		setup: 
		def process = new Process(name:'test process', description:'test process')
		def task = new Task(name:'test', description:'test description', finishOnThisTask:false, process:process)
		task.addToApproverGroups('userID')
		def task2 = new Task(name:'test2', description:'test2 description', finishOnThisTask:true, process:process)
		def taskOutcome = new TaskOutcome(name:'testOutcomeVal', description:'testing outcome').addToStart('test2')
		task.outcomes.put("testOutcomeVal", taskOutcome)
		
		when:
		def result = task.validate()
		
		then:
		!result
		task.errors.getFieldError('rejections').code == 'task.validation.approval.rejections.invalid.count'
	}
	
	def "Ensure non finish task with approverRole but no reject is invalid"() {
		setup: 
		def process = new Process(name:'test process', description:'test process')
		def task = new Task(name:'test', description:'test description', finishOnThisTask:false, process:process)
		task.addToApproverRoles('userID')
		def task2 = new Task(name:'test2', description:'test2 description', finishOnThisTask:true, process:process)
		def taskOutcome = new TaskOutcome(name:'testOutcomeVal', description:'testing outcome').addToStart('test2')
		task.outcomes.put("testOutcomeVal", taskOutcome)
		
		when:
		def result = task.validate()
		
		then:
		!result
		task.errors.getFieldError('rejections').code == 'task.validation.approval.rejections.invalid.count'
	}
	
	def "Ensure non finish task with approver but no outcome is invalid"() {
		setup: 
		def process = new Process(name:'test process', description:'test process')
		def task = new Task(name:'test', description:'test description', finishOnThisTask:false, process:process)
		task.addToApprovers('userID')
		task.rejections.put('testReject', new TaskRejection(name:'test rejection', description:'test rejection description').addToStart('test'))
		
		when:
		def result = task.validate()
		
		then:
		!result
		task.errors.getFieldError('outcomes').code == 'task.validation.outcomes.approvalonly.invalid.count'
	}
	
	def "Ensure non finish task with approverGroups but no outcome is invalid"() {
		setup: 
		def process = new Process(name:'test process', description:'test process')
		def task = new Task(name:'test', description:'test description', finishOnThisTask:false, process:process)
		task.addToApproverGroups('userID')
		task.rejections.put('testReject', new TaskRejection(name:'test rejection', description:'test rejection description').addToStart('test'))
		
		when:
		def result = task.validate()
		
		then:
		!result
		task.errors.getFieldError('outcomes').code == 'task.validation.outcomes.approvalonly.invalid.count'
	}
	
	def "Ensure non finish task with approverRoles but no outcome is invalid"() {
		setup: 
		def process = new Process(name:'test process', description:'test process')
		def task = new Task(name:'test', description:'test description', finishOnThisTask:false, process:process)
		task.addToApproverRoles('userID')
		task.rejections.put('testReject', new TaskRejection(name:'test rejection', description:'test rejection description').addToStart('test'))
		
		when:
		def result = task.validate()
		
		then:
		!result
		task.errors.getFieldError('outcomes').code == 'task.validation.outcomes.approvalonly.invalid.count'
	}
	
	def "Ensure non finish task with only an executable is valid"() {
		setup: 
		def process = new Process(name:'test process', description:'test process')
		def taskOutcome = new TaskOutcome(name:'testOutcomeVal', description:'testing outcome').addToStart('test2')
		def task = new Task(name:'test', description:'test description', finishOnThisTask:false, process:process)
		task.execute.put('service', "testService")
		task.execute.put('method', "testMethod")
		task.outcomes.put('testOutcomeVal', taskOutcome)
		
		when:
		def result = task.validate()
		
		then:
		result
	}
	
	def "Ensure executable task defining neither service nor script is invalid"() {
		setup: 
		def process = new Process(name:'test process', description:'test process')
		def taskOutcome = new TaskOutcome(name:'testOutcomeVal', description:'testing outcome').addToStart('test2')
		def task = new Task(name:'test', description:'test description', finishOnThisTask:false, process:process)
		task.execute.put('taglib', 'testTaglib')
		task.outcomes.put('testOutcomeVal', taskOutcome)
		
		when:
		def result = task.validate()
		
		then:
		!result
		task.errors.getFieldError('execute').code == 'task.validation.execute.invalid.definition'
	}
	
	def "Ensure executable task defining service and method is valid"() {
		setup: 
		def process = new Process(name:'test process', description:'test process')
		def task = new Task(name:'test', description:'test description', finishOnThisTask:false, process:process)
		task.execute.put('service', 'testService')
		task.execute.put('method', 'testMethod')

		def taskOutcome = new TaskOutcome(name:'testOutcomeVal', description:'testing outcome').addToStart('test2')
		task.outcomes.put('testOutcomeVal', taskOutcome)
		
		when:
		def result = task.validate()
		
		then:
		result
	}
	
	def "Ensure executable task defining service but no method is invalid"() {
		setup: 
		def process = new Process(name:'test process', description:'test process')
		def taskOutcome = new TaskOutcome(name:'testOutcomeVal', description:'testing outcome').addToStart('test2')
		def task = new Task(name:'test', description:'test description', finishOnThisTask:false, process:process)
		task.execute.put('service', 'testService')
		task.outcomes.put('testOutcomeVal', taskOutcome)
		
		when:
		def result = task.validate()
		
		then:
		!result
		task.errors.getFieldError('execute').code == 'task.validation.execute.service.invalid.definition'
	}
	
	def "Ensure executable task defining service, method and additional params is invalid"() {
		setup: 
		def process = new Process(name:'test process', description:'test process')
		def taskOutcome = new TaskOutcome(name:'testOutcomeVal', description:'testing outcome').addToStart('test2')
		def task = new Task(name:'test', description:'test description', finishOnThisTask:false, process:process)
		task.execute.put('service', 'testService')
		task.execute.put('method', 'testMethod')
		task.execute.put('someTest', 'testVal')
		task.outcomes.put('testOutcomeVal', taskOutcome)
		
		when:
		def result = task.validate()
		
		then:
		!result
		task.errors.getFieldError('execute').code == 'task.validation.execute.service.invalid.definition'
	}
	
	def "Ensure executable task defining script is valid"() {
		setup: 
		def user = new Subject(username:'testuser')
		user.save()
		def testScript = new WorkflowScript(name:'TestScript', description:'A script used in testing', definition:'return true', creator:user)
		testScript.save()
		def process = new Process(name:'test process', description:'test process')
		def taskOutcome = new TaskOutcome(name:'testOutcomeVal', description:'testing outcome').addToStart('test2')
		def task = new Task(name:'test', description:'test description', finishOnThisTask:false, process:process)
		task.execute.put('script', 'TestScript')
		task.outcomes.put('testOutcomeVal', taskOutcome)
		
		when:
		def result = task.validate()
		
		then:
		result
	}
	
	def "Ensure executable task defining non existant script is invalid"() {
		setup: 
		def process = new Process(name:'test process', description:'test process')
		def task = new Task(name:'test', description:'test description', finishOnThisTask:false, process:process)
		task.execute.put('script', 'TestScript')
		def task2 = new Task(name:'test2', description:'test2 description', finishOnThisTask:true, process:process)
		def taskOutcome = new TaskOutcome(name:'testOutcomeVal', description:'testing outcome').addToStart('test2')
		task.outcomes.put('testOutcomeVal', taskOutcome)
		
		when:
		def result = task.validate()
		
		then:
		!result
		task.errors.getFieldError('execute').code == 'task.validation.execute.script.invalid.definition'
	}
	
	def "Ensure executable task defining service and method but no outcomes is invalid"() {
		setup: 
		def process = new Process(name:'test process', description:'test process')
		def task = new Task(name:'test', description:'test description', finishOnThisTask:false, process:process)
		task.execute.put('service', 'testService')
		task.execute.put('method', 'testMethod')
		
		when:
		def result = task.validate()
		
		then:
		!result
		task.errors.getFieldError('outcomes').code == 'task.validation.outcomes.invalid.count'
	}
	
	def "Ensure executable task defining controller and action but no outcomes is invalid"() {
		setup: 
		def process = new Process(name:'test process', description:'test process')
		def task = new Task(name:'test', description:'test description', finishOnThisTask:false, process:process)
		task.execute.put('controller', 'testController')
		task.execute.put('action', 'testAction')
		
		when:
		def result = task.validate()
		
		then:
		!result
		task.errors.getFieldError('outcomes').code == 'task.validation.outcomes.invalid.count'
	}

}