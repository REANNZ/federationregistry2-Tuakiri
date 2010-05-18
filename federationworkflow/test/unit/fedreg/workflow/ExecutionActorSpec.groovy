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
		
		def wm = []
		def ea = new ExecutionActor(ps, ts, wm)
		
		when:
		ea.onMessage([ti, TaskStatus.INITIATING])
		
		then:
		ti.status == TaskStatus.APPROVALREQUIRED
	}
	
}