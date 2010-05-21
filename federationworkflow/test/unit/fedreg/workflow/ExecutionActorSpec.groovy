package fedreg.workflow

import grails.test.*
import groovy.mock.interceptor.*
import grails.plugin.spock.*

class ExecutionActorSpec extends UnitSpec {

	def "Validate execution actor correctly calls task service request approval for task that is in initiating state and needs approval"() {
		setup:
		mockDomain(Task)
		mockDomain(TaskInstance)
		
		def t = new Task(approvers:'user1')
		def ti = new TaskInstance(task: t)
		
		def psMock = new MockFor(ProcessService)
		def ps = psMock.proxyInstance()
		
		def tsMock = new MockFor(TaskService )
		tsMock.demand.requestApproval() { i -> i.status = TaskStatus.APPROVALREQUIRED; i.save() }
		def ts = tsMock.proxyInstance()
	
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
		ti.status == TaskStatus.APPROVALREQUIRED
	}
	
}