package fedreg.workflow

import grails.plugin.spock.*

class ExecutionActorSpec extends UnitSpec {

	def "Validate execution actor correctly calls task service request approval for task that is in initiating state and needs approval"() {
		setup:
		mockDomain(Task)
		mockDomain(TaskInstance)
		
		int id = 1
		
		def ps = Mock(ProcessService)		
		def ts = Mock(TaskService)
		
		def ea = new ExecutionActor(processService:ps, taskService:ts)
	
		when:
		ea.onMessage([id, ExecutionAction.APPROVALREQUIRED])
		
		then:
		1 * ts.requestApproval(id)
	}
	
	def "Validate execution actor correctly calls task service execute for task that is being initialized and doesn't need approval"() {
		setup:
		mockDomain(Task)
		mockDomain(TaskInstance)
		
		int id = 1
		
		def ps = Mock(ProcessService)
		def ts = Mock(TaskService)
		
		def ea = new ExecutionActor(processService:ps, taskService:ts)
		
		when:
		ea.onMessage([id, ExecutionAction.EXECUTE])
		
		then:
		1 * ts.execute(id)
	}
	
}