package fedreg.workflow

class WorkflowScriptController {
	
	def defaultAction = "list"

	def list = {
		def scriptList = WorkflowScript.getAll()
		[scriptList: scriptList]
	}
	
	def create = {
		def script = new WorkflowScript()
		[script: script]
	}
	
	def save = {
		if(!params.definition) {
			log.warn "Script definition was not present"
			render message(code: 'fedreg.controllers.namevalue.missing')
			redirect action:list
			return
		}
		
		def script = new WorkflowScript(params)
		script.creator = authenticatedUser
		if(!script.save()) {
			flash.type = "error"
		    flash.message = message(code: 'fedreg.workflow.script.create.error')
			render view: "create", model: [script: script]
			return
		}
		
		redirect action: "show", id: script.id
	}

	def show = {
		if(!params.id) {
			log.warn "Workflow Script ID was not present"
			render message(code: 'fedreg.controllers.namevalue.missing')
			redirect action:list
			return
		}
		
		def script = WorkflowScript.get(params.id)
		if(!script) {
			flash.type = "error"
		    flash.message = message(code: 'fedreg.workflow.script.nonexistant', args: [params.id])
			render view: "list"
			return
		}
		
		[script:script]
	}
	
	def edit = {
		if(!params.id) {
			log.warn "Process ID was not present"
			render message(code: 'fedreg.controllers.namevalue.missing')
			redirect action:list
			return
		}
		
		def script = WorkflowScript.get(params.id)
		if(!script) {
			flash.type = "error"
		    flash.message = message(code: 'fedreg.workflow.script.nonexistant', args: [params.id])
			render view: "list"
			return
		}
		
		[script:script]
	}
	
	def update = {
		if(!params.id) {
			log.warn "WorkflowScript ID was not present"
			render message(code: 'fedreg.controllers.namevalue.missing')
			redirect action:list
			return
		}
		
		def script = WorkflowScript.get(params.id)
		if(!script) {
			flash.type = "error"
		    flash.message = message(code: 'fedreg.workflow.script.nonexistant', args: [params.id])
			render view: "list"
			return
		}
		
		script.properties = params
		script.save()
		if(script.hasErrors()) {
			flash.type = "error"
		    flash.message = message(code: 'fedreg.workflow.script.update.error')
			render view: "edit", model: [script: script]
			return
		}
		
		redirect action: "show", id: script.id
	}
}