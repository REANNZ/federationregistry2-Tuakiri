package fedreg.workflow

import grails.plugin.spock.*
import spock.util.concurrent.*

class ExecuteJobSpec extends UnitSpec {

    void "testSomething"() {
		setup:
		def outcome
		
		def env = [somekey:'someval']
		def context = [ mergedJobDataMap: [bean:'testBean', method:'testMethod', env: env] ] 
		
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
}
