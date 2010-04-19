import groovy.util.GroovyTestCase;

class ProcessManagerServiceTests extends GroovyTestCase {

	def processManagerService

    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testFirstProcess() {

    	def processDefinition = processManagerService.loadDSL(new File('test-data/process1.dsl'))
    	assertTrue processDefinition.validate()

    	assertEquals (processManagerService.getProcessInit('Firewall Request'), [controller: 'test', action: 'submit'])
    	processManagerService.taskCompleted(process: 'Firewall Request', nameSuffix: 'for GRAPE', actionResult: 'submitted', recordId: 1, actionedBy: 'testuser')

    	assertEquals(TaskInstance.findById(2).assignedTo, "FIREWALL_RULE_APPROVER")
    	assertEquals (processManagerService.getTaskInit(2), [controller: 'test', action: 'approve'])
    	processManagerService.taskCompleted([taskId: 2, actionResult: 'rejected', recordId: processManagerService.getAssociatedRecord(2),
											message: 'Your request was rejected because I said so! Blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah',
											actionedBy: 'approver'])

		assertEquals(TaskInstance.findById(3).assignedTo, "REQUESTOR")
		assertEquals (processManagerService.getTaskInit(3), [controller: 'test', action: 'submit'])
		processManagerService.taskCompleted(taskId: 3, actionResult: 'submitted', actionedBy: 'testuser')

		assertEquals(TaskInstance.findById(4).assignedTo, "FIREWALL_RULE_APPROVER")
		assertEquals (processManagerService.getTaskInit(4), [controller: 'test', action: 'approve'])
		processManagerService.taskCompleted([taskId: 4, actionResult: 'approved',
											recordId: processManagerService.getAssociatedRecord(4),
											actionedBy: 'approver'])


		def processInstance = ProcessInstance.findById(1)
		assertTrue (processInstance.tasks.size() == 6)
		assertTrue (!(processInstance.tasks.find { it.status != TaskStatus.COMPLETED }))

    }

    void testSecondProcess() {

    	def processDefinition = processManagerService.loadDSL(new File('test-data/process2.dsl'))
    	assertTrue processDefinition.validate()

    	processManagerService.startProcess(process: 'Monthly Audit', recordId: 1, actionedBy: 'testuser')
    	assertEquals(TaskInstance.findById(7).assignedTo, "AUDITOR")

    	assertEquals (processManagerService.getTaskInit(7), [controller: 'test', action: 'audit'])
		processManagerService.taskCompleted([taskId: 7, actionResult: 'complete',
											recordId: processManagerService.getAssociatedRecord(7),
											actionedBy: 'testuser'])

		def processInstance = ProcessInstance.findById(2)
		assertTrue (processInstance.tasks.size() == 2)
		assertTrue (!(processInstance.tasks.find { it.status != TaskStatus.COMPLETED }))

    }

    void testThirdProcess() {

    	def processDefinition = processManagerService.loadDSL(new File('test-data/process3.dsl'))
    	assertTrue processDefinition.validate()

    	processManagerService.startProcess(process: 'Monthly Scan', recordId: 1, actionedBy: 'testuser')

		def processInstance = ProcessInstance.findById(3)
		assertTrue (processInstance.tasks.size() == 2)
		assertTrue (!(processInstance.tasks.find { it.status != TaskStatus.COMPLETED }))

    }

    void testInvalidNumberOfArgumentsToStartProcess() {
    	def startProcess1 = {
    		processManagerService.startProcess(process: 'Firewall Request')
    	}

    	def startProcess2 = {
    		processManagerService.startProcess(process: 'Firewall Request', recordId: 1)
    	}

    	def startProcess3 = {
        	processManagerService.startProcess(process: 'Firewall Request', actionedBy: 'testuser')
        }

    	shouldFail(InvalidNumberOfArgumentsException, startProcess1)
    	shouldFail(InvalidNumberOfArgumentsException, startProcess2)
    	shouldFail(InvalidNumberOfArgumentsException, startProcess3)
    }

    void testInvalidNumberOfArgumentsToCompleteTask() {
    	def completeTask1 = {
    		processManagerService.taskCompleted(taskId: 1245)
    	}

    	def completeTask2 = {
    		processManagerService.taskCompleted(taskId: 1245, actionResult: 'complete')
    	}

    	def completeTask3 = {
    		processManagerService.taskCompleted(taskId: 1245, actionedBy: 'testuser')
    	}

    	shouldFail(InvalidNumberOfArgumentsException, completeTask1)
    	shouldFail(InvalidNumberOfArgumentsException, completeTask2)
    	shouldFail(InvalidNumberOfArgumentsException, completeTask3)

    }

    void testProcessDefinition() {

    	def startProcess = {
    		processManagerService.startProcess(process: 'Undefined Process', recordId: 1, actionedBy: 'testuser')
    	}

    	shouldFail(ProcessDefinitionNotFoundException, startProcess)

    }

    void testTaskInstanceNotFound() {
    	def completeTask = {
    		processManagerService.taskCompleted(taskId: 1245, actionResult: 'complete', actionedBy: 'testuser')
    	}

    	shouldFail(TaskInstanceNotFoundException, completeTask)
    }

    void testMissingComponent() {

    	def startProcess = {
    		processManagerService.startProcess(process: 'Security Scan', recordId: 1, actionedBy: 'testuser')
    	}

    	def processDefinition = processManagerService.loadDSL(new File('test-data/process4.dsl'))
    	assertTrue processDefinition.validate()

    	shouldFail(ComponentMissingException, startProcess)
    }

}