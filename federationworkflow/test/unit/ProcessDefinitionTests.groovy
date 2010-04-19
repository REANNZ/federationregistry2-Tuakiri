import grails.test.GrailsUnitTestCase

class ProcessDefinitionTests extends GrailsUnitTestCase {
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testContraints() {
    	def processDefinition = new ProcessDefinition()
    	mockDomain(ProcessDefinition, [processDefinition])

    	assertFalse processDefinition.validate()
    	assertEquals "nullable", processDefinition.errors['name']
    	assertEquals "min", processDefinition.errors['processVersion']
        assertEquals "nullable", processDefinition.errors['dsl']
        assertEquals "nullable", processDefinition.errors['firstTask']
        assertEquals "nullable", processDefinition.errors['tasks']

    }
}
