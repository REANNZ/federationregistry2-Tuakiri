package fedreg.workflow

import org.apache.shiro.SecurityUtils
import grails.plugins.nimble.core.AdminsService

class WorkflowApprovalController {
	
	def workflowTaskService
	static defaultAction = "list"
	
	def list = {
		def tasks = workflowTaskService.retrieveTasksAwaitingApproval(authenticatedUser)
		[tasks:tasks]
	}
	
	def administrative = {
		if(SecurityUtils.subject.isPermitted("fedreg:workflow:administrator") || SecurityUtils.subject.hasRole(AdminsService.ADMIN_ROLE)) {
			def c = TaskInstance.createCriteria()
			def tasks = c.listDistinct {
				and {
					eq("status", TaskStatus.APPROVALREQUIRED)
					processInstance {
						eq("status", ProcessStatus.INPROGRESS)
					}
				}
			}
			[tasks:tasks]
		}
		else {
			log.warn("Attempt to do administrative taskInstance listing by $authenticatedUser was denied, not administrative user")
			response.sendError(403)
		}
	}
	
	def approve = {
		if(!params.id) {
			log.warn "Task instance id was not present"
			flash.type = "error"
		    flash.message = message(code: 'fedreg.controllers.namevalue.missing')
			redirect action:list
			return
		}
		
		def taskInstance = TaskInstance.get(params.id)
		if(!taskInstance) {
			flash.type = "error"
		    flash.message = message(code: 'fedreg.workflow.taskinstance.nonexistant', args: [params.id])
			render view: "list"
			return
		}
		
		if(taskInstance.potentialApprovers.contains(authenticatedUser) || SecurityUtils.subject.hasRole(AdminsService.ADMIN_ROLE)) {
			workflowTaskService.approve(taskInstance.id)
			flash.type = "success"
		    flash.message = message(code: 'fedreg.workflow.taskinstance.successfully.approved')
			render view: "list"
		}
		else {
			log.warn("Attempt to approve $taskInstance by $authenticatedUser was denied, no permission to modify this record")
			response.sendError(403)
		}
	}
	
	def reject = {
		if(!params.id) {
			log.warn "Task instance id was not present"
			flash.type = "error"
		    flash.message = message(code: 'fedreg.controllers.namevalue.missing')
			redirect action:list
			return
		}
		
		if(!params.rejection) {
			log.warn "Rejection selection was not present"
			flash.type = "error"
		    flash.message = message(code: 'fedreg.controllers.namevalue.missing')
			redirect action:list
			return
		}
		
		def taskInstance = TaskInstance.get(params.id)
		if(!taskInstance) {
			flash.type = "error"
		    flash.message = message(code: 'fedreg.workflow.taskinstance.nonexistant', args: [params.id])
			render view: "list"
			return
		}
		
		if(!taskInstance.task.rejections.containsKey(params.rejection)) {
			flash.type = "error"
		    flash.message = message(code: 'fedreg.workflow.taskinstance.no.such.rejection', args: [params.id])
			render view: "list"
			return
		}
		
		if(taskInstance.potentialApprovers.contains(authenticatedUser) || SecurityUtils.subject.hasRole(AdminsService.ADMIN_ROLE)) {
			workflowTaskService.reject(taskInstance.id, params.rejection)
			flash.type = "success"
		    flash.message = message(code: 'fedreg.workflow.taskinstance.successfully.rejected')
			render view: "list"
		}
		else {
			log.warn("Attempt to reject $taskInstance with ${params.rejection} by $authenticatedUser was denied, no permission to modify this record")
			response.sendError(403)
		}
	}
}