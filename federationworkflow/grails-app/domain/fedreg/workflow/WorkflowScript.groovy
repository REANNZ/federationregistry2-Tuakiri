package fedreg.workflow

import org.codehaus.groovy.control.CompilationFailedException

class WorkflowScript {
	String name
	String description
	String definition
	
    static mapping = {
		definition type: "text"
	}
	
	static constraints = {
		name(nullable: false, unique: true)
		description(nullable: false)
		definition(nullable: false, blank: false, validator: { val, obj ->
			obj.validateScript()
		})
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
