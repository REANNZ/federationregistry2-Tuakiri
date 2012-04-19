package aaf.fr.workflow

import org.apache.shiro.SecurityUtils

/**
 * Provides workflow scripting management views.
 *
 * @author Bradley Beddoes
 */
class WorkflowScriptController {
	def defaultAction = "list"

	def list = {
		if(SecurityUtils.subject.isPermitted("workflow:scripts:view")) {
			def scriptList = WorkflowScript.getAll()
			[scriptList: scriptList]
		}
		else {
			log.warn("Attempt to list workflow scripts by $principal was denied, incorrect permission set")
			response.sendError(403)
		}
	}
	
	def create = {
		if(SecurityUtils.subject.isPermitted("workflow:script:create")) {
			def script = new WorkflowScript()
			[script: script]
		}
		else {
			log.warn("Attempt to create workflow script by $principal was denied, incorrect permission set")
			response.sendError(403)
		}
	}
	
	def save = {
		if(SecurityUtils.subject.isPermitted("workflow:script:create")) {
			if(!params.definition) {
				log.warn "Script definition was not present"
				render message(code: 'controllers.fr.generic.namevalue.missing')
				redirect action:list
				return
			}
		
			def script = new WorkflowScript(params)
			script.creator = subject
			if(!script.save()) {
				flash.type = "error"
        flash.message = message(code: 'domains.fr.workflow.script.create.error')
				render view: "create", model: [script: script]
				return
			}
		
			log.info "$principal created $script"
			redirect action: "show", id: script.id
		}
		else {
			log.warn("Attempt to save workflow script by $principal was denied, incorrect permission set")
			response.sendError(403)
		}
	}

	def show = {
		if(SecurityUtils.subject.isPermitted("workflow:scripts:view")) {
			if(!params.id) {
				log.warn "Workflow Script ID was not present"
				render message(code: 'controllers.fr.generic.namevalue.missing')
				redirect action:list
				return
			}
		
			def script = WorkflowScript.get(params.id)
			if(!script) {
				flash.type = "error"
			    flash.message = message(code: 'domains.fr.workflow.script.nonexistant', args: [params.id])
				render view: "list"
				return
			}
		
			[script:script]
		}
		else {
			log.warn("Attempt to show workflow script by $principal was denied, incorrect permission set")
			response.sendError(403)
		}
	}
	
	def edit = {
		if(!params.id) {
			log.warn "Process ID was not present"
			render message(code: 'controllers.fr.generic.namevalue.missing')
			redirect action:list
			return
		}
		
		def script = WorkflowScript.get(params.id)
		if(!script) {
			flash.type = "error"
		    flash.message = message(code: 'domains.fr.workflow.script.nonexistant', args: [params.id])
			render view: "list"
			return
		}
		
		if(SecurityUtils.subject.isPermitted("workflow:script:${script.id}:update")) {
			[script:script]
		}
		else {
			log.warn("Attempt to edit $script by $principal was denied, incorrect permission set")
			response.sendError(403)
		}
	}
	
	def update = {
		if(!params.id) {
			log.warn "WorkflowScript ID was not present"
			render message(code: 'controllers.fr.generic.namevalue.missing')
			redirect action:list
			return
		}
		
		def script = WorkflowScript.get(params.id)
		if(!script) {
			flash.type = "error"
		    flash.message = message(code: 'domains.fr.workflow.script.nonexistant', args: [params.id])
			render view: "list"
			return
		}
		
		if(SecurityUtils.subject.isPermitted("workflow:script:${script.id}:update")) {
			script.properties = params
			script.save()
			if(script.hasErrors()) {
				flash.type = "error"
			    flash.message = message(code: 'domains.fr.workflow.script.update.error')
				render view: "edit", model: [script: script]
				return
			}
		
			log.info "$principal updated $script"
			redirect action: "show", id: script.id
		}
		else {
			log.warn("Attempt to update $script by $principal was denied, incorrect permission set")
			response.sendError(403)
		}
	}
}