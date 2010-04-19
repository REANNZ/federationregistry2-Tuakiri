import grails.test.*
import groovy.lang.Closure;

class ProcessDefinitionTests extends GroovyTestCase {

	def processManagerService

    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testValidImportedDSL() {
    	def processDefinition = processManagerService.loadDSL(new File('test-data/valid.dsl'))
    	assertTrue processDefinition.validate()
    }

    void testFirstInvalidImportedDSL() {
    	def processDefinition = processManagerService.loadDSL(new File('test-data/invalid1.dsl'))
    	assertFalse processDefinition.validate()

    	def fieldError = processDefinition.errors.getFieldError("tasks.action")
    	assertEquals("no initate/automated action", "validator.invalid", fieldError.code)

    	fieldError = processDefinition.errors.getFieldError("tasks.actionResults")
    	assertEquals("no action result", "validator.invalid", fieldError.code)

    }

    void testSecondInvalidImportedDSL() {
    	def processDefinition = processManagerService.loadDSL(new File('test-data/invalid2.dsl'))
    	assertFalse processDefinition.validate()

    	def fieldError = processDefinition.errors.getFieldError("tasks.dependencies")
    	assertEquals("dependencies reference non-existent task", "validator.invalid", fieldError.code)

    	fieldError = processDefinition.errors.getFieldError("tasks.actionResults")
    	assertEquals("action result on finishing task", "validator.invalid", fieldError.code)

    	fieldError = processDefinition.errors.getFieldError("tasks.actionResults.tasks")
    	assertEquals("action result references non-existent task", "validator.invalid", fieldError.code)
    }

    void testThirdInvalidImportedDSL() {
    	def processDefinition = processManagerService.loadDSL(new File('test-data/invalid3.dsl'))
    	assertFalse processDefinition.validate()

       	def fieldError = processDefinition.errors.getFieldError("tasks.assignTo")
    	assertEquals("assignTo undefined", "validator.invalid", fieldError.code)

    	fieldError = processDefinition.errors.getFieldError("tasks.action")
    	assertEquals("controller/service undefined of action", "validator.invalid", fieldError.code)
    }

    void testFourthInvalidImportedDSL() {
    	def processDefinition = processManagerService.loadDSL(new File('test-data/invalid4.dsl'))
    	assertFalse processDefinition.validate()

    	def fieldError = processDefinition.errors.getFieldError("tasks.action")
    	assertEquals("controller action not defined in task action ", "validator.invalid", fieldError.code)

    }

    void testFifthInvalidImportedDSL() {

    	def parse = {
    		def processDefinition = processManagerService.loadDSL(new File('test-data/invalid5.dsl'))
    	}

    	shouldFailWithCause(InvalidProcessDSLException, parse)
    }

    void testSixthInvalidImportedDSL() {

    	def parse = {
    		def processDefinition = processManagerService.loadDSL(new File('test-data/invalid6.dsl'))
    	}

    	shouldFailWithCause(InvalidProcessDSLException, parse)
    }
}
