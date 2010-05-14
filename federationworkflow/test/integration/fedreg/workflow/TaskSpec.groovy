package fedreg.workflow

import grails.plugin.spock.*

class TaskSpec extends IntegrationSpec {
	
	def "Ensure non finish task with no approver, approverRoles, approverGroups or execute fails"() {
		setup: 
		def process = new Process(name:'test process', description:'test process')
		def task = new Task(name:'test', description:'test description', finishOnThisTask:false, process:process)
		def task2 = new Task(name:'test2', description:'test2 description', finishOnThisTask:true, process:process)
		def taskOutcome = new TaskOutcome(name:'testOutcomeVal', description:'testing outcome', task:task).addToStart('test2').addToStart('test2')
		task.outcomes.put('testOutcomeVal', taskOutcome)
		
		when:
		def result = task.validate()
		
		then:
		result == false
		task.errors.getFieldErrors('execute').size() > 0
		task.errors.getFieldErrors('approvers').size() > 0
		task.errors.getFieldErrors('approverGroups').size() > 0
		task.errors.getFieldErrors('approverRoles').size() > 0
	}
	
	def "Ensure non finish task with only approver is valid"() {
		setup: 
		def process = new Process(name:'test process', description:'test process')
		def task = new Task(name:'test', description:'test description', finishOnThisTask:false, process:process)
		task.addToApprovers('userID')
		task.rejections.put('testReject', new TaskRejection(name:'test rejection', description:'test rejection description').addToStart('test'))
		def task2 = new Task(name:'test2', description:'test2 description', finishOnThisTask:true, process:process)
		def taskOutcome = new TaskOutcome(name:'testOutcomeVal', description:'testing outcome', task:task).addToStart('test2')
		task.outcomes.put("testOutcomeVal", taskOutcome)
		
		when:
		def result = task.validate()
		
		then:
		println task.errors
		result == true
		task.errors.getFieldErrors('execute').size() == 0
		task.errors.getFieldErrors('approvers').size() == 0
		task.errors.getFieldErrors('approverGroups').size() == 0
		task.errors.getFieldErrors('approverRoles').size() == 0
	}
	
	def "Ensure non finish task with only approverGroup is valid"() {
		setup: 
		def process = new Process(name:'test process', description:'test process')
		def task = new Task(name:'test', description:'test description', finishOnThisTask:false, process:process)
		task.addToApproverGroups('userID')
		task.rejections.put('testReject', new TaskRejection(name:'test rejection', description:'test rejection description').addToStart('test'))
		def task2 = new Task(name:'test2', description:'test2 description', finishOnThisTask:true, process:process)
		def taskOutcome = new TaskOutcome(name:'testOutcomeVal', description:'testing outcome', task:task).addToStart('test2')
		task.outcomes.put('testOutcomeVal', taskOutcome)
		
		when:
		def result = task.validate()
		
		then:
		result == true
		task.errors.getFieldErrors('execute').size() == 0
		task.errors.getFieldErrors('approvers').size() == 0
		task.errors.getFieldErrors('approverGroups').size() == 0
		task.errors.getFieldErrors('approverRoles').size() == 0
	}
	
	def "Ensure non finish task with only approverRole is valid"() {
		setup: 
		def process = new Process(name:'test process', description:'test process')
		def task = new Task(name:'test', description:'test description', finishOnThisTask:false, process:process)
		task.addToApproverRoles('userID')
		task.rejections.put('testReject', new TaskRejection(name:'test rejection', description:'test rejection description').addToStart('test'))
		def task2 = new Task(name:'test2', description:'test2 description', finishOnThisTask:true, process:process)
		def taskOutcome = new TaskOutcome(name:'testOutcomeVal', description:'testing outcome', task:task).addToStart('test2')
		task.outcomes.put('testOutcomeVal', taskOutcome)
		
		when:
		def result = task.validate()
		
		then:
		result == true
		task.errors.getFieldErrors('execute').size() == 0
		task.errors.getFieldErrors('approvers').size() == 0
		task.errors.getFieldErrors('approverGroups').size() == 0
		task.errors.getFieldErrors('approverRoles').size() == 0
	}
	
	def "Ensure non finish task with approver but no reject is invalid"() {
		setup: 
		def process = new Process(name:'test process', description:'test process')
		def task = new Task(name:'test', description:'test description', finishOnThisTask:false, process:process)
		task.addToApprovers('userID')
		def task2 = new Task(name:'test2', description:'test2 description', finishOnThisTask:true, process:process)
		def taskOutcome = new TaskOutcome(name:'testOutcomeVal', description:'testing outcome', task:task).addToStart('test2')
		task.outcomes.put("testOutcomeVal", taskOutcome)
		
		when:
		def result = task.validate()
		
		then:
		result == false
		task.errors.getFieldErrors('rejections').size() > 0
	}
	
	def "Ensure non finish task with approverGroup but no reject is invalid"() {
		setup: 
		def process = new Process(name:'test process', description:'test process')
		def task = new Task(name:'test', description:'test description', finishOnThisTask:false, process:process)
		task.addToApproverGroups('userID')
		def task2 = new Task(name:'test2', description:'test2 description', finishOnThisTask:true, process:process)
		def taskOutcome = new TaskOutcome(name:'testOutcomeVal', description:'testing outcome', task:task).addToStart('test2')
		task.outcomes.put("testOutcomeVal", taskOutcome)
		
		when:
		def result = task.validate()
		
		then:
		result == false
		task.errors.getFieldErrors('rejections').size() > 0
	}
	
	def "Ensure non finish task with approverRole but no reject is invalid"() {
		setup: 
		def process = new Process(name:'test process', description:'test process')
		def task = new Task(name:'test', description:'test description', finishOnThisTask:false, process:process)
		task.addToApproverRoles('userID')
		def task2 = new Task(name:'test2', description:'test2 description', finishOnThisTask:true, process:process)
		def taskOutcome = new TaskOutcome(name:'testOutcomeVal', description:'testing outcome', task:task).addToStart('test2')
		task.outcomes.put("testOutcomeVal", taskOutcome)
		
		when:
		def result = task.validate()
		
		then:
		result == false
		task.errors.getFieldErrors('rejections').size() > 0
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
		result == false
		task.errors.getFieldErrors('outcomes').size() > 0
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
		result == false
		task.errors.getFieldErrors('outcomes').size() > 0
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
		result == false
		task.errors.getFieldErrors('outcomes').size() > 0
	}
	
	def "Ensure non finish task with only an executable is valid"() {
		setup: 
		def process = new Process(name:'test process', description:'test process')
		def task = new Task(name:'test', description:'test description', finishOnThisTask:false, process:process)
		task.execute.put('service', "testService")
		task.execute.put('method', "testMethod")
		def task2 = new Task(name:'test2', description:'test2 description', finishOnThisTask:true, process:process)
		def taskOutcome = new TaskOutcome(name:'testOutcomeVal', description:'testing outcome', task:task).addToStart('test2')
		task.outcomes.put('testOutcomeVal', taskOutcome)
		
		when:
		def result = task.validate()
		
		then:
		result == true
		task.errors.getFieldErrors('execute').size() == 0
		task.errors.getFieldErrors('approvers').size() == 0
		task.errors.getFieldErrors('approverGroups').size() == 0
		task.errors.getFieldErrors('approverRoles').size() == 0
	}
	
	def "Ensure executable task defining neither service nor controller is invalid"() {
		setup: 
		def process = new Process(name:'test process', description:'test process')
		def task = new Task(name:'test', description:'test description', finishOnThisTask:false, process:process)
		task.execute.put('taglib', 'testTaglib')
		def task2 = new Task(name:'test2', description:'test2 description', finishOnThisTask:true, process:process)
		def taskOutcome = new TaskOutcome(name:'testOutcomeVal', description:'testing outcome', task:task).addToStart('test2')
		task.outcomes.put('testOutcomeVal', taskOutcome)
		
		when:
		def result = task.validate()
		
		then:
		result == false
		task.errors.getFieldErrors('execute').size() > 0
	}
	
	def "Ensure executable task defining service and method is valid"() {
		setup: 
		def process = new Process(name:'test process', description:'test process')
		def task = new Task(name:'test', description:'test description', finishOnThisTask:false, process:process)
		task.execute.put('service', 'testService')
		task.execute.put('method', 'testMethod')
		def task2 = new Task(name:'test2', description:'test2 description', finishOnThisTask:true, process:process)
		def taskOutcome = new TaskOutcome(name:'testOutcomeVal', description:'testing outcome', task:task).addToStart('test2')
		task.outcomes.put('testOutcomeVal', taskOutcome)
		
		when:
		def result = task.validate()
		
		then:
		result == true
		task.errors.getFieldErrors('execute').size() == 0
	}
	
	def "Ensure executable task defining service but no method is invalid"() {
		setup: 
		def process = new Process(name:'test process', description:'test process')
		def task = new Task(name:'test', description:'test description', finishOnThisTask:false, process:process)
		task.execute.put('serivce', 'testService')
		def task2 = new Task(name:'test2', description:'test2 description', finishOnThisTask:true, process:process)
		def taskOutcome = new TaskOutcome(name:'testOutcomeVal', description:'testing outcome', task:task).addToStart('test2')
		task.outcomes.put('testOutcomeVal', taskOutcome)
		
		when:
		def result = task.validate()
		
		then:
		result == false
		task.errors.getFieldErrors('execute').size() > 0
	}
	
	def "Ensure executable task defining service, method and additional params is invalid"() {
		setup: 
		def process = new Process(name:'test process', description:'test process')
		def task = new Task(name:'test', description:'test description', finishOnThisTask:false, process:process)
		task.execute.put('service', 'testService')
		task.execute.put('method', 'testMethod')
		task.execute.put('someTest', 'testVal')
		def task2 = new Task(name:'test2', description:'test2 description', finishOnThisTask:true, process:process)
		def taskOutcome = new TaskOutcome(name:'testOutcomeVal', description:'testing outcome', task:task).addToStart('test2')
		task.outcomes.put('testOutcomeVal', taskOutcome)
		
		when:
		def result = task.validate()
		
		then:
		result == false
		task.errors.getFieldErrors('execute').size() > 0
	}
	
	def "Ensure executable task defining controller and action is valid"() {
		setup: 
		def process = new Process(name:'test process', description:'test process')
		def task = new Task(name:'test', description:'test description', finishOnThisTask:false, process:process)
		task.execute.put('controller', 'testService')
		task.execute.put('action', 'testMethod')
		def task2 = new Task(name:'test2', description:'test2 description', finishOnThisTask:true, process:process)
		def taskOutcome = new TaskOutcome(name:'testOutcomeVal', description:'testing outcome', task:task).addToStart('test2')
		task.outcomes.put('testOutcomeVal', taskOutcome)
		
		when:
		def result = task.validate()
		
		then:
		result == true
		task.errors.getFieldErrors('execute').size() == 0
	}
	
	def "Ensure executable task defining controller, action and id is valid"() {
		setup: 
		def process = new Process(name:'test process', description:'test process')
		def task = new Task(name:'test', description:'test description', finishOnThisTask:false, process:process)
		task.execute.put('controller', 'testService')
		task.execute.put('action', 'testMethod')
		task.execute.put('id', '1')
		def task2 = new Task(name:'test2', description:'test2 description', finishOnThisTask:true, process:process)
		def taskOutcome = new TaskOutcome(name:'testOutcomeVal', description:'testing outcome', task:task).addToStart('test2')
		task.outcomes.put('testOutcomeVal', taskOutcome)
		
		when:
		def result = task.validate()
		
		then:
		result == true
		task.errors.getFieldErrors('execute').size() == 0
	}
	
	def "Ensure executable task defining controller but no action is invalid"() {
		setup: 
		def process = new Process(name:'test process', description:'test process')
		def task = new Task(name:'test', description:'test description', finishOnThisTask:false, process:process)
		task.execute.put('controller', 'testService')
		def task2 = new Task(name:'test2', description:'test2 description', finishOnThisTask:true, process:process)
		def taskOutcome = new TaskOutcome(name:'testOutcomeVal', description:'testing outcome', task:task).addToStart('test2')
		task.outcomes.put('testOutcomeVal', taskOutcome)
		
		when:
		def result = task.validate()
		
		then:
		result == false
		task.errors.getFieldErrors('execute').size() > 0
	}
	
	def "Ensure executable task defining controller and action with a third non id param is invalid"() {
		setup: 
		def process = new Process(name:'test process', description:'test process')
		def task = new Task(name:'test', description:'test description', finishOnThisTask:false, process:process)
		task.execute.put('controller', 'testService')
		task.execute.put('action', 'testMethod')
		task.execute.put('blah', 'testMethod')
		def task2 = new Task(name:'test2', description:'test2 description', finishOnThisTask:true, process:process)
		def taskOutcome = new TaskOutcome(name:'testOutcomeVal', description:'testing outcome', task:task).addToStart('test2')
		task.outcomes.put('testOutcomeVal', taskOutcome)
		
		when:
		def result = task.validate()
		
		then:
		result == false
		task.errors.getFieldErrors('execute').size() > 0
	}
	
	def "Ensure executable task defining service and method but not outcomes is invalid"() {
		setup: 
		def process = new Process(name:'test process', description:'test process')
		def task = new Task(name:'test', description:'test description', finishOnThisTask:false, process:process)
		task.execute.put('service', 'testService')
		task.execute.put('method', 'testMethod')
		
		when:
		def result = task.validate()
		
		then:
		result == false
		task.errors.getFieldErrors('outcomes').size() > 0
	}
	
	def "Ensure executable task defining controller and action but not outcomes is invalid"() {
		setup: 
		def process = new Process(name:'test process', description:'test process')
		def task = new Task(name:'test', description:'test description', finishOnThisTask:false, process:process)
		task.execute.put('controller', 'testController')
		task.execute.put('action', 'testAction')
		
		when:
		def result = task.validate()
		
		then:
		result == false
		task.errors.getFieldErrors('outcomes').size() > 0
	}

}