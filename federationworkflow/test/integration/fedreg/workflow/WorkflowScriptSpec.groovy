package fedreg.workflow

import grails.plugin.spock.*

class WorkflowScriptSpec extends IntegrationSpec {
	
	def grailsApplication

	def "validate a correctly formed WorkflowScript is saved"() {		
		when:
		def wfs = new WorkflowScript(name:'TestWorkflow', description:'Test workflow description', definition:'import fedreg.workflow.*; return true').save()
		
		then:
		!wfs.hasErrors()
		wfs == WorkflowScript.findByName('TestWorkflow')
	}
	
	def "validate a non named WorkflowScript isn't valid"() {		
		when:
		def wfs = new WorkflowScript(description:'Test workflow description', definition:'return true')
		wfs.validate()
		
		then:
		wfs.hasErrors()
	}
	
	def "validate a non described WorkflowScript isn't valid"() {		
		when:
		def wfs = new WorkflowScript(name:'TestWorkflow', definition:'return true')
		wfs.validate()
		
		then:
		wfs.hasErrors()
	}
	
	def "validate a WorkflowScript with null execution definition is invalid"() {		
		when:
		def wfs = new WorkflowScript(name:'TestWorkflow', description:'Test workflow description', definition:null)
		wfs.validate()
		
		then:
		wfs.hasErrors()
		wfs.errors.getFieldError('definition').code == 'nullable'
	}
	
	def "validate a WorkflowScript with a blank execution definition is invalid"() {		
		when:
		def wfs = new WorkflowScript(name:'TestWorkflow', description:'Test workflow description', definition:'')
		wfs.validate()
		
		then:
		wfs.hasErrors()
		wfs.errors.getFieldError('definition').code == 'blank'
	}
	
	def "validate a WorkflowScript with a non-parsable execution definition is invalid"() {		
		when:
		def wfs = new WorkflowScript(name:'TestWorkflow', description:'Test workflow description', definition:'import this.will.never.exist; println x')
		wfs.validate()
		
		then:
		wfs.hasErrors()
		wfs.errors.getFieldError('definition').code == 'task.validation.workflowscript.parse.invalid'
	}
	
	def "validate a correctly formed WorkflowScript is able to be executed"() {		
		setup:
		def wfs = new WorkflowScript(name:'TestWorkflow', description:'Test workflow description', definition:'import fedreg.workflow.*; return "the test script validating execution ran fine"').save()
		Binding binding = new Binding();
		binding.setVariable("grailsApplication", grailsApplication);
		
		def script = new GroovyShell().parse(wfs.definition)
		script.binding = binding
		
		when:
		def outcome = script.run()
		
		then:
		!wfs.hasErrors()
		wfs == WorkflowScript.findByName('TestWorkflow')
		outcome == "the test script validating execution ran fine"
	}
}