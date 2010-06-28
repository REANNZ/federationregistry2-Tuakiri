package fedreg.workflow

class WorkflowProcessController {
	
	def processService
	def defaultAction = "list"

	def list = {
		def processList = Process.findWhere(active: true)
		[processList: processList]
	}
	
	def create = {
		def process = new Process()
		[process: process]
	}
	
	def save = {
		if(!params.code) {
			log.warn "Process definition was not present"
			render message(code: 'fedreg.controllers.namevalue.missing')
			redirect action:list
			return
		}
		
		def process
		try {
			process = processService.create(params.code)
		}
		catch(Exception e) {
			process = new Process(definition: params.code)
			flash.type = "error"
		    flash.message = message(code: 'fedreg.workflow.process.create.totalfailure')
			render view: "create", model: [process: process]
			return
		}
		
		if(process.hasErrors()) {
			flash.type = "error"
		    flash.message = message(code: 'fedreg.workflow.process.create.error')
			render view: "create", model: [process: process]
			return
		}
		
		redirect action: "show", id: process.id
	}

	def show = {
		if(!params.id) {
			log.warn "Process ID was not present"
			render message(code: 'fedreg.controllers.namevalue.missing')
			redirect action:list
			return
		}
		
		def process = Process.get(params.id)
		if(!process) {
			flash.type = "error"
		    flash.message = message(code: 'fedreg.workflow.process.nonexistant', args: [params.id])
			render view: "list"
			return
		}
		
		[process:process]
	}
	
	def edit = {
		if(!params.id) {
			log.warn "Process ID was not present"
			render message(code: 'fedreg.controllers.namevalue.missing')
			redirect action:list
			return
		}
		
		def process = Process.get(params.id)
		if(!process) {
			flash.type = "error"
		    flash.message = message(code: 'fedreg.workflow.process.nonexistant', args: [params.id])
			render view: "list"
			return
		}
		
		[process:process]
	}
	
	def update = {
		if(!params.id) {
			log.warn "Process ID was not present"
			render message(code: 'fedreg.controllers.namevalue.missing')
			redirect action:list
			return
		}
		
		def process = Process.get(params.id)
		if(!process) {
			flash.type = "error"
		    flash.message = message(code: 'fedreg.workflow.process.nonexistant', args: [params.id])
			render view: "list"
			return
		}
		
		def updatedProcess
		try {
			updatedProcess = processService.update(process.name, params.code)
		}
		catch(Exception e) {
			flash.type = "error"
		    flash.message = message(code: 'fedreg.workflow.process.update.totalfailure')
			render view: "create", model: [process: process]
			return
		}
		
		if(!updatedProcess) {
			flash.type = "error"
		    flash.message = message(code: 'fedreg.workflow.process.nonexistant', args: [params.id])
			render view: "list"
			return
		}
		
		if(updatedProcess.hasErrors()) {
			flash.type = "error"
		    flash.message = message(code: 'fedreg.workflow.process.update.error')
			render view: "edit", model: [process: updatedProcess]
			return
		}
		
		redirect action: "show", id: updatedProcess.id
	}
}