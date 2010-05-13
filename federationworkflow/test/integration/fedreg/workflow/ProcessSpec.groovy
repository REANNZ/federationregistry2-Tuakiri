package fedreg.workflow

import grails.plugin.spock.*
import grails.plugins.nimble.core.UserBase

class ProcessSpec extends IntegrationSpec {
	
	def "Ensure process with tasks that depend on tasks not valid to the process are invalid"() {
		setup: 
		def process = new Process(name:'test process', description:'test process', definition:'empty definition', processVersion:1, creator: new UserBase(username:'tstusername'))
		def task = new Task(name:'test', description:'test description', finishOnThisTask:false)
		task.addToApprovers('userID')
		task.rejections.put('testReject', new TaskRejection(name:'test rejection', description:'test rejection description'))
		def taskOutcome = new TaskOutcome(name:'testOutcomeVal', description:'testing outcome', task:task)
		task.outcomes.put("testOutcomeVal", taskOutcome)
		task.addToDependencies('invalidTaskName')
		process.addToTasks(task)
		
		when:
		def result = process.validate()
		
		then:
		result == false
		process.errors.getFieldErrors('tasks').size() > 0
	}
	
}