package fedreg.workflow

import grails.plugin.spock.*
import grails.plugins.nimble.core.UserBase

class ProcessSpec extends IntegrationSpec {
	
	def "Ensure process with no tasks is invalid"() {
		setup: 
		def process = new Process(name:'test process', description:'test process', definition:'empty definition', processVersion:1, creator: new UserBase(username:'testusername'))
		
		when:
		def result = process.validate()

		then:
		!result
	}
	
	def "Ensure process with no finish task is invalid"() {
		setup: 
		def process = new Process(name:'test process', description:'test process', definition:'empty definition', processVersion:1, creator: new UserBase(username:'testusername'))
		def taskRejection = new TaskRejection(name:'test rejection', description:'test rejection description').addToStart('test3')
		def taskOutcome = new TaskOutcome(name:'testOutcomeVal', description:'testing outcome').addToStart('test2')
		def task = new Task(name:'test', description:'test description', finishOnThisTask:false)
		
		task.addToApprovers('userID')
		task.rejections.put('testReject', taskRejection)
		task.outcomes.put("testOutcomeVal", taskOutcome)
		process.addToTasks(task)
		
		def taskRejection2 = new TaskRejection(name:'test rejection2', description:'test rejection2 description').addToStart('test')
		def taskOutcome2 = new TaskOutcome(name:'testOutcomeVal2', description:'testing outcome2').addToStart('test3')
		def task2 = new Task(name:'test2', description:'test description2', finishOnThisTask:false)
		
		task2.addToDependencies('test')
		task2.addToApprovers('userID')
		task2.rejections.put('testReject2', taskRejection2)
		task2.outcomes.put("testOutcomeVal2", taskOutcome2)
		process.addToTasks(task2)
		
		when:
		def result = process.validate()

		then:
		!result
	}
	
	def "Ensure process with tasks that depend on tasks not valid to the process are invalid"() {
		setup: 
		def process = new Process(name:'test process', description:'test process', definition:'empty definition', processVersion:1, creator: new UserBase(username:'testusername'))
		def task = new Task(name:'test', description:'test description', finishOnThisTask:false)
		def task2 = new Task(name:'test2', description:'test description2', finishOnThisTask:false)
		task.addToApprovers('userID')
		task.rejections.put('testReject', new TaskRejection(name:'test rejection', description:'test rejection description'))
		def taskOutcome = new TaskOutcome(name:'testOutcomeVal', description:'testing outcome', task:task)
		task.outcomes.put("testOutcomeVal", taskOutcome)
		task.addToDependencies('test2')
		task.addToDependencies('invalidTaskName')
		process.addToTasks(task)
		process.addToTasks(task2)
		
		when:
		def result = process.validate()
		
		then:
		result == false
		process.errors.getFieldErrors('tasks').size() > 0
	}
	
	def "Ensure process with rejections that depend on starting existing tasks are valid"() {
		setup: 
		def process = new Process(name:'test process', description:'test process', definition:'empty definition', processVersion:1, creator: new UserBase(username:'testusername'))
		
		def taskRejection = new TaskRejection(name:'test rejection', description:'test rejection description').addToStart('test3')
		def taskOutcome = new TaskOutcome(name:'testOutcomeVal', description:'testing outcome').addToStart('test2')
		def task = new Task(name:'test', description:'test description', finishOnThisTask:false)
		
		task.addToApprovers('userID')
		task.rejections.put('testReject', taskRejection)
		task.outcomes.put("testOutcomeVal", taskOutcome)
		process.addToTasks(task)
		
		def taskRejection2 = new TaskRejection(name:'test rejection2', description:'test rejection2 description').addToStart('test')
		def taskOutcome2 = new TaskOutcome(name:'testOutcomeVal2', description:'testing outcome2').addToStart('test3')
		def task2 = new Task(name:'test2', description:'test description2', finishOnThisTask:false)
		
		task2.addToDependencies('test')
		task2.addToApprovers('userID')
		task2.rejections.put('testReject2', taskRejection2)
		task2.outcomes.put("testOutcomeVal2", taskOutcome2)
		process.addToTasks(task2)
		
		def task3 = new Task(name:'test3', description:'test description3', finishOnThisTask:true)
		process.addToTasks(task3)
		
		when:
		def result = process.validate()
		
		then:
		result
	}
	
	def "Ensure process with rejections that depend on starting non-existing tasks are invalid"() {
		setup: 
		def process = new Process(name:'test process', description:'test process', definition:'empty definition', processVersion:1, creator: new UserBase(username:'testusername'))
		
		def taskRejection = new TaskRejection(name:'test rejection', description:'test rejection description').addToStart('noSuchTask')
		def taskOutcome = new TaskOutcome(name:'testOutcomeVal', description:'testing outcome').addToStart('test2')
		def task = new Task(name:'test', description:'test description', finishOnThisTask:false)
		
		task.addToApprovers('userID')
		task.rejections.put('testReject', taskRejection)
		task.outcomes.put("testOutcomeVal", taskOutcome)
		process.addToTasks(task)
		
		def taskRejection2 = new TaskRejection(name:'test rejection2', description:'test rejection2 description').addToStart('test')
		def taskOutcome2 = new TaskOutcome(name:'testOutcomeVal2', description:'testing outcome2').addToStart('test3')
		def task2 = new Task(name:'test2', description:'test description2', finishOnThisTask:false)
		
		task2.addToDependencies('test')
		task2.addToApprovers('userID')
		task2.rejections.put('testReject2', taskRejection2)
		task2.outcomes.put("testOutcomeVal2", taskOutcome2)
		process.addToTasks(task2)
		
		def task3 = new Task(name:'test3', description:'test description3', finishOnThisTask:true)
		process.addToTasks(task3)
		
		when:
		def result = process.validate()
		
		then:
		!result
	}
	
	def "Ensure process with rejections that depend on terminating existing tasks are valid"() {
		setup: 
		def process = new Process(name:'test process', description:'test process', definition:'empty definition', processVersion:1, creator: new UserBase(username:'testusername'))
		
		def taskRejection = new TaskRejection(name:'test rejection', description:'test rejection description').addToStart('test3')
		taskRejection.addToTerminate('test2')
		def taskOutcome = new TaskOutcome(name:'testOutcomeVal', description:'testing outcome').addToStart('test2')
		def task = new Task(name:'test', description:'test description', finishOnThisTask:false)
		
		task.addToApprovers('userID')
		task.rejections.put('testReject', taskRejection)
		task.outcomes.put("testOutcomeVal", taskOutcome)
		process.addToTasks(task)
		
		def taskRejection2 = new TaskRejection(name:'test rejection2', description:'test rejection2 description').addToStart('test')
		def taskOutcome2 = new TaskOutcome(name:'testOutcomeVal2', description:'testing outcome2').addToStart('test3')
		def task2 = new Task(name:'test2', description:'test description2', finishOnThisTask:false)
		
		task2.addToApprovers('userID')
		task2.rejections.put('testReject2', taskRejection2)
		task2.outcomes.put("testOutcomeVal2", taskOutcome2)
		process.addToTasks(task2)
		
		def task3 = new Task(name:'test3', description:'test description3', finishOnThisTask:true)
		process.addToTasks(task3)
		
		when:
		def result = process.validate()
		
		then:
		result
	}
	
	def "Ensure process with rejections that depend on terminating non existant tasks are invalid"() {
		setup: 
		def process = new Process(name:'test process', description:'test process', definition:'empty definition', processVersion:1, creator: new UserBase(username:'testusername'))
		
		def taskRejection = new TaskRejection(name:'test rejection', description:'test rejection description').addToStart('test3')
		taskRejection.addToTerminate('noSuchTask')
		def taskOutcome = new TaskOutcome(name:'testOutcomeVal', description:'testing outcome').addToStart('test2')
		def task = new Task(name:'test', description:'test description', finishOnThisTask:false)
		
		task.addToApprovers('userID')
		task.rejections.put('testReject', taskRejection)
		task.outcomes.put("testOutcomeVal", taskOutcome)
		process.addToTasks(task)
		
		def taskRejection2 = new TaskRejection(name:'test rejection2', description:'test rejection2 description').addToStart('test')
		def taskOutcome2 = new TaskOutcome(name:'testOutcomeVal2', description:'testing outcome2').addToStart('test3')
		def task2 = new Task(name:'test2', description:'test description2', finishOnThisTask:false)
		
		task2.addToApprovers('userID')
		task2.rejections.put('testReject2', taskRejection2)
		task2.outcomes.put("testOutcomeVal2", taskOutcome2)
		process.addToTasks(task2)
		
		def task3 = new Task(name:'test3', description:'test description3', finishOnThisTask:true)
		process.addToTasks(task3)
		
		when:
		def result = process.validate()
		
		then:
		!result
	}
	
	def "Ensure process with outcomes that depend on starting existing tasks are valid"() {
		setup: 
		def process = new Process(name:'test process', description:'test process', definition:'empty definition', processVersion:1, creator: new UserBase(username:'testusername'))
		
		def taskOutcome = new TaskOutcome(name:'testOutcomeVal', description:'testing outcome').addToStart('test2')
		def task = new Task(name:'test', description:'test description', finishOnThisTask:false)
		task.execute.put('service', 'testService')
		task.execute.put('method', 'testServiceMethod')
		task.outcomes.put("testOutcomeVal", taskOutcome)
		process.addToTasks(task)
		
		def taskOutcome2 = new TaskOutcome(name:'testOutcomeVal2', description:'testing outcome2').addToStart('test3')
		def task2 = new Task(name:'test2', description:'test description2', finishOnThisTask:false)
		task2.execute.put('service', 'testService2')
		task2.execute.put('method', 'testServiceMethod2')
		task2.outcomes.put("testOutcomeVal2", taskOutcome2)
		process.addToTasks(task2)
		
		def task3 = new Task(name:'test3', description:'test description3', finishOnThisTask:true)
		process.addToTasks(task3)
		
		when:
		def result = process.validate()
		
		then:
		result
	}
	
	def "Ensure process with outcomes that depend on starting non-existing tasks are invalid"() {
		setup: 
		def process = new Process(name:'test process', description:'test process', definition:'empty definition', processVersion:1, creator: new UserBase(username:'testusername'))
		
		def taskOutcome = new TaskOutcome(name:'testOutcomeVal', description:'testing outcome').addToStart('noSuchTask')
		def task = new Task(name:'test', description:'test description', finishOnThisTask:false)
		task.execute.put('service', 'testService')
		task.execute.put('method', 'testServiceMethod')
		task.outcomes.put("testOutcomeVal", taskOutcome)
		process.addToTasks(task)
		
		def taskOutcome2 = new TaskOutcome(name:'testOutcomeVal2', description:'testing outcome2').addToStart('test3')
		def task2 = new Task(name:'test2', description:'test description2', finishOnThisTask:false)
		task2.execute.put('service', 'testService2')
		task2.execute.put('method', 'testServiceMethod2')
		task2.outcomes.put('testOutcomeVal2', taskOutcome2)
		process.addToTasks(task2)
		
		def task3 = new Task(name:'test3', description:'test description3', finishOnThisTask:true)
		process.addToTasks(task3)
		
		when:
		def result = process.validate()
		
		then:
		!result
	}
	
	def "Ensure process with outcomes that depend on terminating existing tasks are valid"() {
		setup: 
		def process = new Process(name:'test process', description:'test process', definition:'empty definition', processVersion:1, creator: new UserBase(username:'testusername'))
		
		def taskOutcome = new TaskOutcome(name:'testOutcomeVal', description:'testing outcome').addToStart('test2')
		taskOutcome.addToTerminate('test')
		def task = new Task(name:'test', description:'test description', finishOnThisTask:false)
		task.execute.put('service', 'testService')
		task.execute.put('method', 'testServiceMethod')
		task.outcomes.put("testOutcomeVal", taskOutcome)
		process.addToTasks(task)
		
		def taskOutcome2 = new TaskOutcome(name:'testOutcomeVal2', description:'testing outcome2').addToStart('test3')
		def task2 = new Task(name:'test2', description:'test description2', finishOnThisTask:false)
		task2.execute.put('service', 'testService2')
		task2.execute.put('method', 'testServiceMethod2')
		task2.outcomes.put("testOutcomeVal2", taskOutcome2)
		process.addToTasks(task2)
		
		def task3 = new Task(name:'test3', description:'test description3', finishOnThisTask:true)
		process.addToTasks(task3)
		
		when:
		def result = process.validate()
		
		then:
		result
	}
	
	def "Ensure process with outcomes that depend on termininating non-existing tasks are invalid"() {
		setup: 
		def process = new Process(name:'test process', description:'test process', definition:'empty definition', processVersion:1, creator: new UserBase(username:'testusername'))
		
		def taskOutcome = new TaskOutcome(name:'testOutcomeVal', description:'testing outcome').addToStart('test2')
		taskOutcome.addToTerminate('noSuchTask')
		def task = new Task(name:'test', description:'test description', finishOnThisTask:false)
		task.execute.put('service', 'testService')
		task.execute.put('method', 'testServiceMethod')
		task.outcomes.put("testOutcomeVal", taskOutcome)
		process.addToTasks(task)
		
		def taskOutcome2 = new TaskOutcome(name:'testOutcomeVal2', description:'testing outcome2').addToStart('test3')
		def task2 = new Task(name:'test2', description:'test description2', finishOnThisTask:false)
		task2.execute.put('service', 'testService2')
		task2.execute.put('method', 'testServiceMethod2')
		task2.outcomes.put('testOutcomeVal2', taskOutcome2)
		process.addToTasks(task2)
		
		def task3 = new Task(name:'test3', description:'test description3', finishOnThisTask:true)
		process.addToTasks(task3)
		
		when:
		def result = process.validate()
		
		then:
		!result
	}
	
}