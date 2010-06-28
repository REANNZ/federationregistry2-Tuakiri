package fedreg.workflow

import org.codehaus.groovy.control.CompilationFailedException

import grails.plugins.nimble.core.UserBase

class WorkflowScript {
	String name
	String description
	String definition
	
	UserBase creator
	UserBase lastEditor
	
	Date dateCreated
	Date lastUpdated
	
    static mapping = {
		definition type: "text"
	}
	
	static constraints = {
		name(nullable: false, blank:false, unique: true)
		description(nullable: false, blank:false)
		definition(nullable: false, blank: false, validator: { val, obj ->
			obj.validateScript()
		})
		
		creator(nullable: false)
		lastEditor(nullable: true)
	}
	
	public String toString() {
		"workflowscript:[id:$id, name:$name]"
	}
	
	def validateScript() {
		try {
			new GroovyShell().parse(definition)
		}
		catch(CompilationFailedException e) {
			log.error "Compilation error when compiling workflowscript:[id:$id, name:$name, description:$description]"
			log.debug e
			return ['task.validation.workflowscript.parse.invalid', e.getLocalizedMessage()]
		}
		true
	}
}
