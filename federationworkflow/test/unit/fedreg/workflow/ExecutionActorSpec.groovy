package fedreg.workflow

import grails.plugin.spock.*

class ExecutionActorSpec extends UnitSpec {

	def "Validate execution actor correctly calls task service request approval for task that is being initialized and needs approval"() {
		setup:
		mockDomain(Task)
		mockDomain(TaskInstance)
		
		def t = new Task(approvers:'user1')
		def ti = new TaskInstance(task: t)
		
		def ps = Mock(ProcessService)		
		def ts = Mock(TaskService)
		
		def ea = new ExecutionActor(processService:ps, taskService:ts)
		
		when:
		ea.onMessage([ti, ExecutionAction.APPROVALREQUIRED])
		
		then:
		1 * ts.requestApproval(ti)
	}
	
	def "Validate execution actor correctly calls task service execute for task that is being initialized and doesn't need approval"() {
		setup:
		mockDomain(Task)
		mockDomain(TaskInstance)
		
		def t = new Task()
		def ti = new TaskInstance(task: t)
		
		def ps = Mock(ProcessService)		
		def ts = Mock(TaskService)
		
		def ea = new ExecutionActor(processService:ps, taskService:ts)
		
		when:
		ea.onMessage([ti, ExecutionAction.EXECUTE])
		
		then:
		1 * ts.execute(ti)
	}
	
}