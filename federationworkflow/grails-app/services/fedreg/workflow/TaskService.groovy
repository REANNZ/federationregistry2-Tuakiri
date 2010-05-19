package fedreg.workflow

import grails.plugins.nimble.core.UserBase
import grails.plugins.nimble.core.ProfileBase
import grails.plugins.nimble.core.Role
import grails.plugins.nimble.core.Group

class TaskService {
	
	def final paramKey = /\{(.+?)\}/
	def executionActor
	
	def initiate (ProcessInstance processInstance) {
		initiate(processInstance, processInstance.process.tasks.get(0))
	}
	
	def initiate (ProcessInstance processInstance, Task task) {
		def taskInstance = new TaskInstance(status:TaskStatus.INITIATING)
		
		task.addToInstances(taskInstance)
		processInstance.addToTaskInstances(taskInstance)
		
		if(!processInstance.save()) {
			log.error "Unable to update processInstance ${processInstance.description} with new taskInstance"
			task.errors.each { log.error it }
		}
		
		if(!task.save()) {
			log.error "Unable to update Task with new instance data"
			task.errors.each { log.error it }
		}
		
		execute(taskInstance)
	}
	
	def approve(TaskInstance taskInstance) {
		taskInstance.status = TaskStatus.APPROVALGRANTED
		taskInstance.approver = authenticatedUser
		if(!taskInstance.save()) {
			log.error "While attempting to update taskInstance ${taskInstance.id} with APPROVALGRANTED status a failure occured"
			taskInstance.errors.each { log.error it }
			// TODO: Terminate process??
		}
		executionActor << [taskInstance, TaskStatus.APPROVALGRANTED]
	}
	
	def reject(TaskInstance taskInstance, String identifier, String reason) {
		taskInstance.status = TaskStatus.APPROVALREJECTED
		taskInstance.approver = authenticatedUser
		if(!taskInstance.save()) {
			log.error "While attempting to update taskInstance ${taskInstance.id} with APPROVALREJECTED status a failure occured"
			taskInstance.errors.each { log.error it }
			// TODO: Terminate process??
		}
		
		// Notify process creator
	}
	
	def execute(TaskInstance taskInstance) {
		
	}
	
	def requestApproval(TaskInstance taskInstance) {
		// This is a task defined with an approver directive which means we need to request permission from USERS || GROUPS || ROLES to proceed
		def subject = "Action required for workflow ${taskInstance.task.process.name}"
		def message = "The workflow ${taskInstance.task.process.name} (${taskInstance.processInstance.description}) is waiting to execute a task."
	
		def locatedApprover = false
		def identifier, user, role, group
		for(approver in taskInstance.task.approvers) {
			identifier = processVal(approver, taskInstance.processInstance.params)
			if(identifier.isLong())
				user = UserBase.get(identifier)
			if(user) {
				log.debug "Identified user (${user.id}) ${user.username} by a match of the identifier '${identifier}' to their ID attribute"
			}
			else {
				user = UserBase.findByUsername(identifier)
				if(user) {
					log.debug "Identified user (${user.id}) ${user.username} by a match of the identifier '${identifier}' to their username attribute"
				}
				else {
					user = ProfileBase.findByEmail(identifier)?.owner
					if(user){
						log.debug "Identified user (${user.id}) ${user.username} by a match of the identifier '${identifier}' to their email attribute"
					}
					else {
						log.error "Attempting to identify user by identifier '${identifier}' for process '${taskInstance.processInstance.description}' to request approval for task '${taskInstance.task.name}' failed. Workflow is erronous and should be checked."
						continue
					}
				}
			}
		
			// We've found a valid user in the system
			locatedApprover = true
		}
		taskInstance.task.approverRoles.each {
			identifier = processVal(it, taskInstance.processInstance.params)
			role = Role.findByName(identifier)
			if(role) {
				// Loop through all membership of the role and send notification messages
				locatedApprover = true
			}
			else {
				log.error "Attempting to identify role by identifier '${identifier}' for process '${taskInstance.processInstance.description}' to request approval for task '${taskInstance.task.name}' failed. Workflow is erronous and should be checked."
			}
		}
		taskInstance.task.approverGroups.each {
			identifier = processVal(it, taskInstance.processInstance.params)
			group = Group.findByName(identifier)
			if(group) {
				// Loop through all membership of the group and send notification messages
				locatedApprover = true
			}
			else {
				log.error "Attempting to identify group by identifier '${identifier}' for process '${taskInstance.processInstance.description}' to request approval for task '${taskInstance.task.name}' failed. Workflow is erronous and should be checked."
			}
		}
	
		if(!locatedApprover) {
			// The task requires approval but all avenues to locate an authoritative source have failed. The process is now effectively dead
			log.error "Unable to locate a valid approver for process '${taskInstance.processInstance.description}' and task '${taskInstance.task.name}', process is invalid and will be terminated"
			taskInstance.status = TaskStatus.APPROVALFAILURE
			if(!taskInstance.save()) {
				log.error "While attempting to update taskInstance ${taskInstance.id} with APPROVALFAILURE status a failure occured"
				taskInstance.errors.each { log.error it }
			}
		
			//TODO Terminate process
		}
		else {
			// Messages have been queued to all concerned requesting approval so we're now in a wait state
			log.debug "Located valid approver(s) for process '${taskInstance.processInstance.description}' and task '${taskInstance.task.name}', task will continue once approved"
			taskInstance.status = TaskStatus.APPROVALREQUIRED
			if(!taskInstance.save()) {
				log.error "While attempting to update taskInstance ${taskInstance.id} with REQUIRESAPPROVAL status a failure occured"
				taskInstance.errors.each { log.error it }
				// TODO: Terminate process??
			}
		}
	}
	
	def processVal(def scriptedVal, def params) {
		// Check if the scripted value matches {.+} if so attempt to replace
		// with the value provided when the process was initialized
		def val = scriptedVal
		if(scriptedVal =~ paramKey) {
	        def key = scriptedVal =~ paramKey
	        if(params.containsKey(key[0][1]))
	            val = params.get(key[0][1])
	    }
		return val
	}

}