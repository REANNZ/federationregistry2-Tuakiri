package fedreg.workflow

import org.springframework.transaction.annotation.*

import grails.plugins.nimble.core.UserBase
import grails.plugins.nimble.core.ProfileBase
import grails.plugins.nimble.core.Role
import grails.plugins.nimble.core.Group

class TaskService {
	static transactional = false
	
	def final paramKey = /\{(.+?)\}/
	
	def sessionFactory
	
	def initiate (def processInstanceID, def taskID) {
		def processInstance = ProcessInstance.lock(processInstanceID)
		def task = Task.lock(taskID)
		def taskInstance = new TaskInstance(status:TaskStatus.INITIATING)
		
		log.info "Initiating taskInstance to represent $task within $processInstance"
	
		task.addToInstances(taskInstance)
		processInstance.addToTaskInstances(taskInstance)
	
		if(!processInstance.save()) {
			log.error "Unable to update $processInstance with new taskInstance"
			task.errors.each { log.error it }
			return // TODO
		}
	
		if(!task.save()) {
			log.error "Unable to update Task with new instance for $processInstance and $task"
			task.errors.each { log.error it }
			return // TODO
		}
	
		if(!taskInstance.save(flush:true)) {
			log.error "Unable to create taskInstance for $processInstance and $task"
			task.errors.each { log.error it }
			return	// TODO
		}
	
		taskInstance.lock()	 
		if(task.needsApproval()) {
			log.debug "Requesting approval for $taskInstance within $processInstance"
			ApprovalJob.triggerNow([taskInstanceID:taskInstance.id])
		}
		else
			if(task.executes())
				ExecuteJob.triggerNow(taskInstanceID:taskInstance.id)
			else
				finalize(taskInstance.id)	// minimal overhead not worth the extra thread
	}
	
	def terminate(def taskInstanceID) {
		def taskInstance = TaskInstance.lock(taskInstanceID)
		taskInstance.status = TaskStatus.TERMINATED
	}
	
	def approve(def taskInstanceID) {
		def taskInstance = TaskInstance.lock(taskInstanceID)
		if(!taskInstance) {
			// TODO
			println 'wtf'
		}
		log.info "Approving execution of $taskInstance in ${taskInstance.processInstance}"
		
		taskInstance.status = TaskStatus.APPROVALGRANTED
		taskInstance.approver = authenticatedUser
		if(!taskInstance.save(flush:true)) {
			log.error "While attempting to update taskInstance ${taskInstance.id} with APPROVALGRANTED status a failure occured"
			taskInstance.errors.each { log.error it }
			// TODO: Terminate process??
		}
		if(taskInstance.task.executes()) {
			log.debug "Triggering execute for taskInstance ${taskInstance.id} bound to task ${taskInstance.task.name}"
			ExecuteJob.triggerNow(taskInstanceID:taskInstance.id)
		}
		else
			finalize(taskInstance.id)
	}
	
	def reject(def taskInstanceID, def rejectionName, def rejectionComment) {
		def taskInstance = TaskInstance.lock(taskInstanceID)
		if(!taskInstance) {
			// TODO
		}
		log.info "Rejecting execution of $taskInstance in ${taskInstance.processInstance} due to $rejectionName with additional comment $rejectionComment"
		// TODO notify workflow messager
		
		taskInstance.status = TaskStatus.APPROVALREJECTED
		taskInstance.approver = authenticatedUser
		if(!taskInstance.save(flush:true)) {
			log.error "While attempting to update $taskInstance with APPROVALREJECTED status a failure occured"
			taskInstance.errors.each { log.error it }
			// TODO: Terminate process??
		}
		
		def rejection = taskInstance.task.rejections.get(rejectionName)
		if(!rejection) {
			// TODO: Terminate process??
		}
		terminateAndStartTasks(taskInstance, rejection)
	}
	
	def execute(def taskInstanceID) {
		def taskInstance = TaskInstance.lock(taskInstanceID)
		taskInstance.status = TaskStatus.INPROGRESS
		
		log.debug "Executing $taskInstance for ${taskInstance.processInstance}"
		// TODO find and execute service
		
		if(!taskInstance.save(flush:true)) {
			log.error "While attempting to update $taskInstance with INPROGRESS status a failure occured"
			taskInstance.errors.each { log.error it }
			// TODO: Terminate process??
		}
	}
	
	def complete(def taskInstanceID, def outcomeName) {
		def taskInstance = TaskInstance.lock(taskInstanceID)
		if(taskInstance.status != TaskStatus.INPROGRESS) {
			// TODO
		}
		
		def outcome = taskInstance.task.outcomes.get(outcomeName)
		if(!outcome) {
			// TODO: Terminate process??
		}
		terminateAndStartTasks(taskInstance, outcome)
		
		taskInstance.status = TaskStatus.SUCCESSFUL
		if(!taskInstance.save(flush:true)) {
			log.error "While attempting to update taskInstance ${taskInstance.id} with SUCCESSFUL status a failure occured"
			taskInstance.errors.each { log.error it }
			// TODO: Terminate process??
		}
		
		log.info "Completed $taskInstance for ${taskInstance.processInstance}"
	}
	
	def finalize (def taskInstanceID) {
		
	}
	
	def requestApproval(def taskInstanceID) {
		// This is a task defined with an approver directive which means we need to request permission from USERS || GROUPS || ROLES to proceed
		def taskInstance = TaskInstance.lock(taskInstanceID)
		taskInstance.status = TaskStatus.APPROVALREQUIRED

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
			if(!taskInstance.save(flush:true)) {
				log.error "While attempting to update taskInstance ${taskInstance.id} with APPROVALFAILURE status a failure occured"
				taskInstance.errors.each { log.error it }
			}
			//TODO Terminate process
		}
		else {
			// Messages have been queued to all concerned requesting approval so we're now in a wait state
			log.debug "Located valid approver(s) for process '${taskInstance.processInstance.description}' and task '${taskInstance.task.name}', task will continue once approved"
			taskInstance.status = TaskStatus.APPROVALREQUIRED
			if(!taskInstance.save(flush:true)) {
				log.error "While attempting to update taskInstance ${taskInstance.id} with REQUIRESAPPROVAL status a failure occured"
				taskInstance.errors.each { log.error it }
				// TODO: Terminate process??
			}
		}
	}
	
	def terminateAndStartTasks(def taskInstance, def reaction) {
		log.info "Processing $reaction terminate and start for $taskInstance"
		reaction.terminate.each {
			def task = taskInstance.processInstance.process.tasks.find { t -> t.name.equals(taskName) }
			if(!task) {
				log.error "Unable to locate terminate task with name $taskName defined in ${taskInstance.processInstance.process}"
			}
			log.debug "Firing start for $task in ${taskInstance.processInstance}"
			TerminateJob.triggerNow([processInstanceID: taskInstance.processInstance.id, taskID:task.id])
		}
		reaction.start.each { taskName ->
			def task = taskInstance.processInstance.process.tasks.find { t -> t.name.equals(taskName) }
			if(!task) {
				log.error "Unable to locate start task with name $taskName defined in ${taskInstance.processInstance.process}"
			}
			log.debug "Firing start for $task in ${taskInstance.processInstance}"
			StartJob.triggerNow([processInstanceID: taskInstance.processInstance.id, taskID:task.id])
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