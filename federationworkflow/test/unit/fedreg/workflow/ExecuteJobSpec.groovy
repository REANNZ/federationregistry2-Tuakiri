package fedreg.workflow

import grails.plugin.spock.*
import spock.util.concurrent.*

class ExecuteJobSpec extends UnitSpec {

    void "test service execution occurs correctly"() {
		setup:
		def outcome
		
		def env = [somekey:'someval']
		def context = [ mergedJobDataMap: [service:'testBean', method:'testMethod', env: env] ] 
		
		def testBean = new Expando()
		testBean.testMethod = {e -> assert e == env; outcome = 'testMethod called'}
		
		def mainContext = new Expando()
		mainContext.getBean = {bean -> assert bean == 'testBean'; testBean}
		
		def grailsApplication = [mainContext : mainContext]
		
		when:
		new ExecuteJob(grailsApplication: grailsApplication).execute( context )
		
		then:
		outcome == 'testMethod called'
    }

    void "test script execution occurs correctly and supplied environment can be read and added to"() {
		setup:
		def outcome
		
		def env = [somekey: 'someval']
		def context = [ mergedJobDataMap: [script:'TestScript', env: env] ] 
		
		mockDomain(WorkflowScript)
		def testScript = new WorkflowScript(name:'TestScript', description:'A script used in testing', definition:"env.outcome = \"testMethod called ${env.somekey}\"").save()
		
		def grailsApplication = []
		
		when:
		new ExecuteJob(grailsApplication: grailsApplication).execute( context )
		
		then:
		env.outcome == 'testMethod called someval'
		
    }
}
