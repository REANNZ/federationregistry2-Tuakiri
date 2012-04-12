package aaf.fr.workflow

import org.apache.shiro.SecurityUtils
import aaf.fr.identity.SubjectService

/**
 * Provides workflow approval views.
 *
 * @author Bradley Beddoes
 */
class WorkflowApprovalController {
	static defaultAction = "list"
	
	def workflowTaskService
	
	def list = {
		def tasks = workflowTaskService.retrieveTasksAwaitingApproval(subject)
		[tasks:tasks]
	}
	
	def administrative = {
		if(SecurityUtils.subject.isPermitted("workflow:approval:administrator")) {
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
			log.warn("Attempt to do administrative taskInstance listing by $principal was denied, not administrative user")
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
			redirect action: "list"
			return
		}
		
		if(taskInstance.potentialApprovers.contains(principal) || SecurityUtils.subject.hasRole(AdminsService.ADMIN_ROLE)) {
			log.info "$principal is approving $taskInstance"
			workflowTaskService.approve(taskInstance.id)
			flash.type = "success"
		    flash.message = message(code: 'fedreg.workflow.taskinstance.successfully.approved')
		
			log.info "$principal approval of $taskInstance completed"
			redirect action: "list"
		}
		else {
			log.warn("Attempt to approve $taskInstance by $principal was denied, no permission to modify this record")
			response.sendError(403)
		}
	}
	
	def reject = {
		if(!params.id) {
			log.warn "Task instance id was not present"
			flash.type = "error"
		    flash.message = message(code: 'fedreg.controllers.namevalue.missing')
			redirect action: "list"
			return
		}
		
		if(!params.rejection) {
			log.warn "Rejection selection was not present"
			flash.type = "error"
		    flash.message = message(code: 'fedreg.controllers.namevalue.missing')
			redirect action: "list"
			return
		}
		
		def taskInstance = TaskInstance.get(params.id)
		if(!taskInstance) {
			flash.type = "error"
		    flash.message = message(code: 'fedreg.workflow.taskinstance.nonexistant', args: [params.id])
			redirect action: "list"
			return
		}
		
		if(!taskInstance.task.rejections.containsKey(params.rejection)) {
			flash.type = "error"
		    flash.message = message(code: 'fedreg.workflow.taskinstance.no.such.rejection', args: [params.id])
			redirect action: "list"
			return
		}
		
		if(taskInstance.potentialApprovers.contains(principal) || SecurityUtils.subject.hasRole(AdminsService.ADMIN_ROLE)) {
			log.info "$principal is rejecting $taskInstance"
			workflowTaskService.reject(taskInstance.id, params.rejection)
			flash.type = "success"
		    flash.message = message(code: 'fedreg.workflow.taskinstance.successfully.rejected')
		
			log.info "$principal rejection of $taskInstance completed"
			redirect action: "list"
		}
		else {
			log.warn("Attempt to reject $taskInstance with ${params.rejection} by $principal was denied, no permission to modify this record")
			response.sendError(403)
		}
	}
}