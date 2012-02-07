package aaf.fr.workflow

import org.apache.shiro.SecurityUtils

/**
 * Provides workflow process management views.
 *
 * @author Bradley Beddoes
 */
class WorkflowProcessController {
	static defaultAction = "list"
		
	def workflowProcessService

	def list = {
		if(SecurityUtils.subject.isPermitted("workflow:processes:view")) {
			def processList = Process.findAllWhere(active: true)
			[processList: processList]
		}
		else {
			log.warn("Attempt to list workflow processes by $principal was denied, incorrect permission set")
			response.sendError(403)
		}
	}
	
	def create = {
		if(SecurityUtils.subject.isPermitted("workflow:process:create")) {
			def process = new Process()
			[process: process]
		}
		else {
			log.warn("Attempt to create workflow process by $principal was denied, incorrect permission set")
			response.sendError(403)
		}
	}
	
	def save = {
		if(SecurityUtils.subject.isPermitted("workflow:process:create")) {
			if(!params.code) {
				log.warn "Process definition was not present"
				render message(code: 'fedreg.controllers.namevalue.missing')
				redirect action:list
				return
			}
		
			def created, process
			try {
				(created, process) = workflowProcessService.create(params.code)
				if(!created) {
					process.errors.each {
						log.debug it
					}
					flash.type = "error"
				    flash.message = message(code: 'fedreg.workflow.process.create.error')
					render view: "create", model: [process: process]
					return
				}
		
				log.info "$principal created $process"
				redirect action: "show", id: process.id
			}
			catch(Exception e) {
				process = new Process(definition: params.code)
				flash.type = "error"
			    flash.message = message(code: 'fedreg.workflow.process.create.totalfailure')
				render view: "create", model: [process: process]
			}
		}
		else {
			log.warn("Attempt to save workflow process by $principal was denied, incorrect permission set")
			response.sendError(403)
		}
	}

	def show = {
		if(SecurityUtils.subject.isPermitted("workflow:processes:view")) {
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
		else {
			log.warn("Attempt to view workflow process by $principal was denied, incorrect permission set")
			response.sendError(403)
		}
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
		if(SecurityUtils.subject.isPermitted("workflow:process:${process.id}:update")) {
			[process:process]
		}
		else {
			log.warn("Attempt to edit $process by $principal was denied, incorrect permission set")
			response.sendError(403)
		}
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
		
		if(SecurityUtils.subject.isPermitted("workflow:process:${process.id}:update")) {
			def updated, process_
			try {
				log.info "$principal is updating $process"
				(updated, process_) = workflowProcessService.update(process.name, params.code)
		
				if(!updated) {
					process_.errors.each {
						log.debug it
					}
					flash.type = "error"
				    flash.message = message(code: 'fedreg.workflow.process.update.error')
					render view: "edit", model: [process: process_]
					return
				}
		
				log.info "$principal updated $process_"
				redirect action: "show", id: process_.id
			
			}
			catch(Exception e) {
				flash.type = "error"
			    flash.message = message(code: 'fedreg.workflow.process.update.totalfailure')
				render view: "edit", model: [process: process]
			}
		}
		else {
			log.warn("Attempt to update $process by $principal was denied, incorrect permission set")
			response.sendError(403)
		}
	}
}