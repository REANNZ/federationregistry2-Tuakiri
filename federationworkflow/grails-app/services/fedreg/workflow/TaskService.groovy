package fedreg.workflow

import java.util.Locale

import org.springframework.transaction.annotation.*

import grails.plugins.nimble.core.UserBase
import grails.plugins.nimble.core.ProfileBase
import grails.plugins.nimble.core.Role
import grails.plugins.nimble.core.Group

/*
This service is designed to be concurrent hence passing of ID rather then hibernate instances.
At this time several faults are present in the hibernate/spring stack that cause violation of
ACID when run in concurrent mode so only execute is currently multithreaded. It is expected that more 
concurrency will be introduced back here over time as framework stability improves

@author Bradley Beddoes
*/
class TaskService {
	static transactional = true
	
	def final paramKey = /\{(.+?)\}/
	
	def sessionFactory
	def grailsApplication
	def messageSource
	
	def initiate (def processInstanceID, def taskID) {
		def processInstance = ProcessInstance.lock(processInstanceID)
		if(!processInstance) {
			log.error "Initiate requested on processInstance $processInstanceID but no such instance exists"
			return
		}
		
		def task = Task.lock(taskID)
		if(!task) {
			log.error "Initiate requested on task $taskID but no such instance exists"
			return
		}
		
		def taskInstance
		
		// This will generally only be the case where a task is a join point in 
		// our flow and must wait for multiple dependenices to complete
		processInstance.taskInstances.each { ti ->
			log.debug "WTF ${ti.task} ${ti.status}"
			if(ti.task.id == task.id && ti.status == TaskStatus.DEPENDENCYWAIT ) {
				log.info "Located existing $ti to represent $task within $processInstance"
				taskInstance = ti
			}
		}
		
		if(!taskInstance) {
			log.info "Initiating taskInstance to represent $task within $processInstance"
			taskInstance = new TaskInstance(status:TaskStatus.INITIATING)
			task.addToInstances(taskInstance)
			processInstance.addToTaskInstances(taskInstance)
	
			if(!processInstance.save(flush:true)) {
				log.error "Unable to update $processInstance with new taskInstance"
				task.errors.each { log.error it }
				return
			}
	
			if(!task.save(flush:true)) {
				log.error "Unable to update Task with new instance for $processInstance and $task"
				task.errors.each { log.error it }
				return
			}
	
			if(!taskInstance.save(flush:true)) {
				log.error "Unable to create taskInstance for $processInstance and $task"
				task.errors.each { log.error it }
				return
			}
		}
	
		taskInstance.lock()
		if(task.hasDependencies()) {
			log.info "Task $task has dependencies ${task.dependencies} validating each is complete"
			def allMet = true
			task.dependencies.each { dep ->
				def pass = false
				// We need to iterate taskInstances and figure out if this task is successful
				processInstance.taskInstances.findAll{ ti -> ti.task.name == dep}.each { ti ->
					log.debug "Located taskInstance $ti representing dependency $dep in process $processInstance"
					if(ti.status == TaskStatus.SUCCESSFUL) {
						pass = true
						log.info "Found SUCCESSFUL status $ti"
					}
				}
				if(!pass)
					log.info "Did not locate taskInstance in SUCCESSFUL state for ${Task.findByName(dep)}"
					
				allMet = (allMet && pass)
			}
			if(!allMet) {
				log.info "Task $task has dependencies ${task.dependencies} which are not all complete, sleeping until all are ready"
				taskInstance.status = TaskStatus.DEPENDENCYWAIT

				if(!taskInstance.save(flush:true)) {
					log.error "While attempting to update taskInstance ${taskInstance.id} with DEPENDENCYWAIT status a failure occured"
					taskInstance.errors.each { log.error it }
				}
				return
			}
			
			log.info "Task $task dependencies ${task.dependencies} are all complete, proceeding"
		}
		
		if(task.needsApproval()) {
			log.debug "Requesting approval for $taskInstance within $processInstance"
			requestApproval(taskInstance.id)
		}
		else
			if(task.executes()) {
				log.debug "Executing $taskInstance within $processInstance"
				execute(taskInstance.id)
			}
			else {
				log.debug "Finalizing $taskInstance and hence $processInstance"
				finalize(taskInstance.id)
			}
	}
	
	def terminate(def taskInstanceID) {
		def taskInstance = TaskInstance.lock(taskInstanceID)
		if(!taskInstance) {
			log.error "Termination requested on taskInstanceID $taskInstanceID but no such instance exists"
			return
		}
		log.info "Terminating execution of $taskInstance in ${taskInstance.processInstance}"
		taskInstance.status = TaskStatus.TERMINATED
		if(!taskInstance.save(flush:true)) {
			log.error "While attempting to update taskInstance ${taskInstance.id} with TERMINATED status a failure occured"
			taskInstance.errors.each { log.error it }
		}
	}
	
	def approve(def taskInstanceID) {
		def taskInstance = TaskInstance.lock(taskInstanceID)
		if(!taskInstance) {
			log.error "Approval requested on taskInstanceID $taskInstanceID but no such instance exists"
			return
		}
		log.info "Approving execution of $taskInstance in ${taskInstance.processInstance}"
		taskInstance.status = TaskStatus.APPROVALGRANTED
		taskInstance.approver = authenticatedUser
		if(!taskInstance.save(flush:true)) {
			log.error "While attempting to update taskInstance ${taskInstance.id} with APPROVALGRANTED status a failure occured"
			taskInstance.errors.each { log.error it }
			return
		}
		if(taskInstance.task.executes()) {
			log.debug "Triggering execute for taskInstance ${taskInstance.id} bound to task ${taskInstance.task.name}"
			execute(taskInstance.id)
		}
		else
			if(taskInstance.task.hasOutcome()) {
				log.debug "Triggering outcome for taskInstance ${taskInstance.id} bound to task ${taskInstance.task.name}"
				complete(taskInstanceID, taskInstance.task.outcomes.keySet().iterator().next())	// We know from validation that if a task doesn't execute it must have 1 and only 1 outcome
			}
			else
				finalize(taskInstance.id)
	}
	
	def reject(def taskInstanceID, def rejectionName, def rejectionComment) {
		def taskInstance = TaskInstance.lock(taskInstanceID)
		if(!taskInstance) {
			log.error "Rejection requested on taskInstanceID $taskInstanceID but no such instance exists"
			return
		}
		
		def rejection = taskInstance.task.rejections.get(rejectionName)
		if(!rejection) {
			log.error "Rejection identifier $rejectionName requested on $taskInstance but no such rejection exists"
			return
		}
		
		log.info "Rejecting execution of $taskInstance in ${taskInstance.processInstance} due to $rejectionName with additional comment $rejectionComment"
		taskInstance.status = TaskStatus.APPROVALREJECTED
		taskInstance.approver = authenticatedUser
		if(!taskInstance.save(flush:true)) {
			log.error "While attempting to update $taskInstance with APPROVALREJECTED status a failure occured"
			taskInstance.errors.each { log.error it }
			return
		}
		
		terminateAndStartTasks(taskInstance, rejection)
	}
	
	def execute(def taskInstanceID) {
		def taskInstance = TaskInstance.lock(taskInstanceID)
		if(!taskInstance) {
			log.error "Execute requested on taskInstanceID $taskInstanceID but no such instance exists"
			return
		}
		
		taskInstance.status = TaskStatus.INPROGRESS
		if(!taskInstance.save(flush:true)) {
			log.error "While attempting to update $taskInstance with INPROGRESS status a failure occured"
			taskInstance.errors.each { log.error it }
			return
		}
		
		log.debug "Executing $taskInstance for ${taskInstance.processInstance}"
		def env = [:]
		env.putAll(taskInstance.processInstance.params)
		env.taskInstanceID = taskInstanceID
		ExecuteJob.triggerNow([service:taskInstance.task.execute.get('service'), method:taskInstance.task.execute.get('method'), script:taskInstance.task.execute.get('script'), env: env])
	}
	
	def complete(def taskInstanceID, def outcomeName) {
		def taskInstance = TaskInstance.lock(taskInstanceID)
		if(!taskInstance) {
			log.error "Complete requested on taskInstanceID $taskInstanceID but no such instance exists"
			return
		}
		
		def outcome = taskInstance.task.outcomes.get(outcomeName)
		if(!outcome) {
			log.error "No such outcome $outcomeName for $taskInstance"
			return
		}
		
		taskInstance.status = TaskStatus.SUCCESSFUL
		if(!taskInstance.save(flush:true)) {
			log.error "While attempting to update taskInstance ${taskInstance.id} with SUCCESSFUL status a failure occured"
			taskInstance.errors.each { log.error it }
			return
		}
		
		log.info "Completed $taskInstance for ${taskInstance.processInstance} with outcome $outcomeName"
		terminateAndStartTasks(taskInstance, outcome)
	}
	
	def finalize (def taskInstanceID) {
		def taskInstance = TaskInstance.lock(taskInstanceID)
		if(!taskInstance) {
			log.error "Finalize requested on taskInstanceID $taskInstanceID but no such instance exists"
			return
		}
		
		taskInstance.status = TaskStatus.FINALIZED
		if(!taskInstance.save(flush:true)) {
			log.error "While attempting to update $taskInstance with FINALIZED status a failure occured"
			taskInstance.errors.each { log.error it }
			return
		}
		log.info "Finalized $taskInstance for ${taskInstance.processInstance}"
	}
	
	def requestApproval(def taskInstanceID) {
		// This is a task defined with an approver directive which means we need to request permission from USERS || GROUPS || ROLES to proceed
		def taskInstance = TaskInstance.lock(taskInstanceID)
		if(!taskInstance) {
			log.error "Approval generation requested on taskInstanceID $taskInstanceID but no such instance exists"
			return
		}
		
		taskInstance.status = TaskStatus.APPROVALREQUIRED

		def identifier, user, role, group
		def userList = []
		for(approver in taskInstance.task.approvers) {
			identifier = processVal(approver, taskInstance.processInstance.params)
			if(identifier.isLong())
				user = UserBase.get(identifier)
			if(user) {
				log.debug "Identified user (${user.id}) ${user.username} by a match of the identifier '${identifier}' to their ID attribute"
				if (!userList.contains(user))
					userList.add(user)
			}
			else {
				user = UserBase.findByUsername(identifier)
				if(user) {
					log.debug "Identified user (${user.id}) ${user.username} by a match of the identifier '${identifier}' to their username attribute"
					if (!userList.contains(user))
						userList.add(user)
				}
				else {
					user = ProfileBase.findByEmail(identifier)?.owner
					if(user){
						log.debug "Identified user (${user.id}) ${user.username} by a match of the identifier '${identifier}' to their email attribute"
						if (!userList.contains(user))
							userList.add(user)
					}
					else {
						log.error "Attempting to identify user by identifier '${identifier}' for process '${taskInstance.processInstance.description}' to request approval for task '${taskInstance.task.name}' failed. Workflow is erronous and should be checked."
						continue
					}
				}
			}
		}
		taskInstance.task.approverRoles.each {
			identifier = processVal(it, taskInstance.processInstance.params)
			role = Role.findByName(identifier)
			if(role) {
				log.debug "Identified ${role} as a name match of the identifier '${identifier}', adding users to approval notification"
				role.users.each { u -> 
					if (!userList.contains(u))
						userList.add(u)
				}
			}
			else {
				log.error "Attempting to identify role by identifier '${identifier}' for process '${taskInstance.processInstance.description}' to request approval for task '${taskInstance.task.name}' failed. Workflow is erronous and should be checked."
			}
		}
		taskInstance.task.approverGroups.each {
			identifier = processVal(it, taskInstance.processInstance.params)
			group = Group.findByName(identifier)
			if(group) {
				log.debug "Identified ${group} as a name match of the identifier '${identifier}', adding users to approval notification"
				group.users.each { u -> 
					if (!userList.contains(u))
						userList.add(u)
				}
			}
			else {
				log.error "Attempting to identify group by identifier '${identifier}' for process '${taskInstance.processInstance.description}' to request approval for task '${taskInstance.task.name}' failed. Workflow is erronous and should be checked."
			}
		}
	
		if(userList.size() == 0) {
			// The task requires approval but all avenues to locate an authoritative source have failed. The process is now effectively dead
			log.error "Unable to locate a valid approver for process '${taskInstance.processInstance.description}' and task '${taskInstance.task.name}', process is invalid and will be terminated"
			taskInstance.status = TaskStatus.APPROVALFAILURE
			if(!taskInstance.save(flush:true)) {
				log.error "While attempting to update taskInstance ${taskInstance.id} with APPROVALFAILURE status a failure occured"
				taskInstance.errors.each { log.error it }
			}
		}
		else {
			userList.each { u ->
				messageApprovalRequest(u, taskInstance)
			}
			// Messages have been queued to all concerned requesting approval so we're now in a wait state
			log.debug "Located valid approver(s) for process '${taskInstance.processInstance.description}' and task '${taskInstance.task.name}', task will continue once approved"
			taskInstance.status = TaskStatus.APPROVALREQUIRED
			if(!taskInstance.save(flush:true)) {
				log.error "While attempting to update taskInstance ${taskInstance.id} with REQUIRESAPPROVAL status a failure occured"
				taskInstance.errors.each { log.error it }
			}
		}
	}
	
	def messageApprovalRequest(def user, def taskInstance) {
		log.debug "Sending approval request to $user for $taskInstance"
		
		Object[] args = [taskInstance.task.name]
		sendMail {
            to user.profile.email		
			from grailsApplication.config.workflow.messaging.mail.from
            subject messageSource.getMessage('workflow.requestapproval.mail.subject', args, 'workflow.requestapproval.mail.subject', new Locale("EN"))	// TODO: Draw language from user object when supported by Nimble
            body(view: '/templates/mail/_workflow_requestapproval', model: [taskInstance: taskInstance])
        }
	}
	
	def terminateAndStartTasks(def taskInstance, def reaction) {
		log.info "Processing $reaction terminate and start for $taskInstance"
		reaction.terminate?.each {
			def task = taskInstance.processInstance.process.tasks.find { t -> t.name.equals(taskName) }
			if(!task) {
				log.error "Unable to locate terminate task with name $taskName defined in ${taskInstance.processInstance.process}"
			}
			log.debug "Firing start for $task in ${taskInstance.processInstance}"
			terminate(taskInstance.processInstance.id, task.id)
		}
		reaction.start?.each { taskName ->
			def task = taskInstance.processInstance.process.tasks.find { t -> t.name.equals(taskName) }
			if(!task) {
				log.error "Unable to locate start task with name $taskName defined in ${taskInstance.processInstance.process}"
			}
			log.debug "Firing start for $task in ${taskInstance.processInstance}"
			initiate(taskInstance.processInstance.id, task.id)
		}
	}
	
	def processVal(def scriptedVal, def params) {
		// Check if the scripted value matches {.+} if so attempt to replace with the value provided when the process was initialized
		def val = scriptedVal
		if(scriptedVal =~ paramKey) {
	        def key = scriptedVal =~ paramKey
	        if(params.containsKey(key[0][1]))
	            val = params.get(key[0][1])
	    }
		return val
	}

}