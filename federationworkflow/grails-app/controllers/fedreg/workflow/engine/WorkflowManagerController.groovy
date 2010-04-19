package fedreg.workflow.engine

import fedreg.workflow.engine.AdminCommand
import fedreg.workflow.engine.ProcessInstance
import fedreg.workflow.engine.TaskInstance
import fedreg.workflow.engine.InvalidProcessDSLException
import fedreg.workflow.engine.PermissionDeniedException

import fedreg.workflow.engine.ProcessStatus
import fedreg.workflow.engine.TaskStatus

import java.text.SimpleDateFormat

import org.hibernate.FetchMode as FM


class WorkflowManagerController {

	def processManagerService
	def messageService
	
	def defaultAction = "manager"
	
    def manager = {
        [authenticatedUser: authenticatedUser]
    }
    
    /*******************************
     *       User Queries
     *******************************/ 
	def myProcesses = {
	
	    if (params.clear) {
            def process = ProcessInstance.findById(params.processId as Integer)
            process.completionAcknowlegded = true
            process.save(flush: true)
	    }
	
	    def processes = ProcessInstance.createCriteria().list() {
	        eq("initiatedBy", authenticatedUser.username)
	        eq("completionAcknowlegded", false)
	        fetchMode('tasks', FM.LAZY)
	        if (params.sort && params.order)
    	        order(params.sort, params.order)
    	    else
        	    order("dateCompleted", "asc")
        }
    
    def cancelProcess = {
    
        def process = ProcessInstance.findById(params.id as Integer)
        if (process) {
            if (process.initiatedBy == authenticatedUser.username || 
                authenticatedUser.roles.find { it.name == 'SYSTEM ADMINISTRATOR' }) {
                
                processManagerService.cancelProcess(process)
                redirect(controller: 'processManager', action: 'process', id: params.id)
                
            } else {
                throw new PermissionDeniedException("You are not permitted to complete this operation")
            }
        } else {
            throw new NotFoundException("Process instance with Id: " + params.id + " could not be found!")
        }
    }
        
        [myProcesses: processes]

	}
	
	def myTasks = {
	
    	def processes = ProcessInstance.createCriteria().list() {
			eq("initiatedBy", authenticatedUser.username)
			eq("completionAcknowlegded", false)
			fetchMode('tasks', FM.LAZY)
		}

		def tasks = TaskInstance.createCriteria().list() {
			and {
				eq("status", TaskStatus.INPROGRESS)
				or {

                    if (authenticatedUser.metaClass.respondsTo(authenticatedUser, 'activeRoles')) {
                        authenticatedUser.activeRoles().each { role ->
                            eq("assignedTo", role.name)
                        }
                        
                    } else {

					    authenticatedUser.roles?.each { role ->
						    eq("assignedTo", role.name)
					    }
					
					    authenticatedUser.groups?.each { group ->
					        group.roles?.each { role ->
					            eq("assignedTo", role.name)
					        }
					    }
					}
					
					if (processes) {
						and {
							eq("assignedTo", "INITIATOR")
							or {
								processes.each { process ->
									eq("parentProcess.id", process.id)
								}
							}
						}
					}
				}
			}
			fetchMode('calledBy', FM.LAZY)
    	}
    	
    	tasks = tasks.sort { it.parentProcess.priority }
		
		if (params.sort) {
		    if (params.order == "desc")		
        		tasks = tasks.sort { "${params.sort}" }.reverse()
            else
                tasks = tasks.sort { "${params.sort}" }
        }
		
		[myTasks: tasks]
    }
    
   /**********************************
     *  Process Definitions
    **********************************/
    
    def processDefinitions = {
	    def processNames = ProcessDefinition.createCriteria().list {
	        projections {
	            distinct("name")
	        }
	    }
	    
	    def processes = processNames.collect { name ->
	        def processDefinitions = ProcessDefinition.findAllByName(name)
	        return processDefinitions.sort { it.processVersion }.last()
	    }
	    
		[processes: processes]
	}

    
    def newProcess = {
        if (params.save) {
        
            flash.remove("message")
        
            def loadResult = loadDSL(null)
            if (loadResult.success.value) {
                redirect(action: 'processDefinition', id: loadResult.newProcessDefinition.id, params: [saved: true])
            } else {
                return loadResult
            }
        }        
    }
    
    def processDefinition = {
        
        if (params.id) {      

            def processDefinition = ProcessDefinition.findById(params.id as Integer)        

            if (params.save) {
                
                flash.remove("message")
            
                def loadResult = loadDSL(processDefinition)
                if (loadResult.success.value) {
                    redirect(action: 'processDefinition', id: loadResult.newProcessDefinition.id, params: [saved: true])
                } else {
                    [processDefinition: processDefinition] + loadResult
                }

            } else {
                [processDefinition: processDefinition, code: processDefinition.dsl, saved: params.saved]
            }         

        } else {
            redirect(controller: 'processManager')
        }
    }
    
   private def loadDSL = { ProcessDefinition currentProcessDefinition ->
   
    	def file = new File(System.getProperty("java.io.tmpdir")+'/dsl.tmp')
		file << params.code

		def newProcessDefinition
		try {
			newProcessDefinition = processManagerService.loadDSL(file)
			file.delete()
		} catch (InvalidProcessDSLException e) {
			flash.message = e.message
			file.delete()
			return [code: params.code, success: false]
		}
		
		if (!newProcessDefinition) {
            return [code: params.code, newProcessDefinition: newProcessDefinition, success: false]
		}
		
	    if (currentProcessDefinition) {
	        if (newProcessDefinition.name != currentProcessDefinition.name) {
	            flash.message = "You cannot change the name of the process"
	            return [code: params.code, newProcessDefinition: newProcessDefinition, success: false]
	        }
	    } else {
	        def existingProcess = ProcessDefinition.findByName(newProcessDefinition.name)
	        if (existingProcess) {
	            flash.message = "A process with this name already exists!"
	            return [code: params.code, newProcessDefinition: newProcessDefinition, success: false]
	        }
	    }

		if (!newProcessDefinition.hasErrors()) {
			
			newProcessDefinition.uploadedBy = authenticatedUser.username
			newProcessDefinition.save(flush: true)

			processManagerService.createRoles(newProcessDefinition)
			
            return [code: params.code, newProcessDefinition: newProcessDefinition, success: true]
            
		} else {
			return [code: params.code, newProcessDefinition: newProcessDefinition, success: false]
		}
    }

    /*****************************
     *   Process Instances
     *****************************/

    def processInstances = { AdminCommand adminCommand ->

        def processes

        if (params.search) {
    
            if (adminCommand.processId){
                processes = ProcessInstance.findById(adminCommand.processId as Long)
            } else {    
                processes = ProcessInstance.createCriteria().list {
                    if (adminCommand.processName)
                        ilike('name', adminCommand.processName)
                
                    if (adminCommand.status)
                        eq('status', adminCommand.status)
                    
                    if (adminCommand.initiatedBy)
                        ilike('initiatedBy', adminCommand.initiatedBy)
                        
                    if (adminCommand.startDateInitiated)
                        gt('dateInitiated', adminCommand.startDateInitiated)
                    
                    if (adminCommand.endDateInitiated)
                        lt('dateInitiated', adminCommand.endDateInitiated)
                        
                    if (adminCommand.startDateRequired)
                        gt('dateRequired', adminCommand.startDateRequired)
                    
                    if (adminCommand.endDateRequired)
                        lt('dateRequired', adminCommand.endDateRequired)
                        
                    if (adminCommand.startDateCompleted)
                        gt('dateCompleted', adminCommand.startDateCompleted)
                    
                    if (adminCommand.endDateCompleted)
                        lt('dateCompleted', adminCommand.endDateCompleted)

                    
                    if (params.sort && params.order)
                        order(params.sort, params.order)
                    else
                        order("dateInitiated", "asc")
                }
            }
        } else {
            processes = ProcessInstance.createCriteria().list {
                eq('status', ProcessStatus.INPROGRESS)                
                eq('initiatedBy', authenticatedUser.username)
                if (params.sort && params.order)
                    order(params.sort, params.order)
                else
                    order("dateInitiated", "asc")
            }
            
            adminCommand.status = ProcessStatus.INPROGRESS
            adminCommand.initiatedBy = authenticatedUser.username
        }
            
        [authenticatedUser: authenticatedUser, adminCommand: adminCommand, processes: processes]
    }
    
    def dateSearch = { AdminCommand adminCommand ->
        processInstances(adminCommand)       
    }

	def processInstance = {
		def processInstance = ProcessInstance.findById(params.id)
		
		if (params.priority) {
		    def previousPriority = processInstance.priority 
		    processInstance.priority = params.priority as ProcessPriority
		    processInstance.save(flush: true)
		    
		    
		    processInstance.tasks.each { task ->
		        if (task.status == TaskStatus.INPROGRESS) {
		            def subject = message(code:"workflow.engine.prioritychange.subject", args:[processInstance.name, 
		                                                                                       task.name, 
		                                                                                       previousPriority, 
		                                                                                       processInstance.priority])
		                                                                                       
		            def body = message(code:"workflow.engine.prioritychange.body", args:[processInstance.name, 
		                                                                                 task.name, 
		                                                                                 previousPriority, 
		                                                                                 processInstance.priority])
		        
        		    messageService.sendMessage(role: task.assignedTo, subject: subject, message: body)
        		}
            }   
		}
		
		[authenticatedUser: authenticatedUser, process: processInstance]
	}
	
	
	def cancelProcess = {
    
        def process = ProcessInstance.findById(params.id as Integer)
        if (process) {
            if (process.initiatedBy == authenticatedUser.username || 
                authenticatedUser.roles.find { it.name == 'SYSTEM ADMINISTRATOR' }) {
                
                processManagerService.cancelProcess(process)
                redirect(controller: 'processManager', action: 'processInstance', id: params.id)
                
            } else {
                redirect(controller: 'auth', action: 'unauthorized')
            }
        } else {
            flash.message = ("Process instance with Id: " + params.id + " could not be found!")
            redirect(action: 'manager')
        }
    }

	def initiate = {
	    def taskInstance = TaskInstance.findById(params.id as Integer)
	    if (taskInstance) {
	        if (taskInstance.status == TaskStatus.INPROGRESS) {
                redirect(taskInstance.definition.action + [id: params.id])
            } else if (taskInstance.status == TaskStatus.COMPLETED) {
                flash.message = "'${taskInstance.name}' for '${taskInstance.parentProcess.name}' has already been completed"
                redirect(action: 'manager')
            } else if(taskInstance.status == TaskStatus.CANCELLED) {
                flash.message = "'${taskInstance.name}' for '${taskInstance.parentProcess.name}' has been cancelled"
                redirect(action: 'manager')
            }                
	    } else {
	        redirect(view:'/error')
	    }
	}

	
	/************************
	 *   Process Messages
	 ************************/
	 
	def postMessage = {
	
	    if (params.id && params.message) {
	        if (params.message.size() > 0) {
                def processInstance = ProcessInstance.findById(params.id as Integer)
                
                def now = new Date()
	            def processMessage = new ProcessMessage(date: now, postedBy: authenticatedUser.username, message: params.message)
	            processInstance.addToMessages(processMessage)
	            processInstance.save(flush: true)

                if (params.email) {
                    // Notify all associated parties by mail
                    def subject = message(code:"workflow.engine.newmessage.subject", args:[processInstance.name])

                    def formattedDate = (new SimpleDateFormat("EEE dd/MMM/yyyy, h:mm:ssa")).format(now)
                    def body = message(code: "workflow.engine.newmessage.body", args: [processInstance.name, authenticatedUser.profile.fullName, formattedDate]) + \
                               "\n\n\n" + \
                               params.message
                               
                    if (processInstance.initiatedBy != authenticatedUser.username)
        	            messageService.sendMessage(user: processInstance.initiatedBy, subject: subject, message: body)
        	        
        	        processInstance.tasks.each { task ->
        	            if (task.status == TaskStatus.INPROGRESS)
                            messageService.sendMessage(role: task.assignedTo, subject: subject, message: body)
        	        }
        	    }
	        }
	    }
	    
        redirect(action: 'processInstance', id: params.id)
	}
	
	def deleteMessage = {
	
	    if (params.messageId) {
            def message = ProcessMessage.findById(params.messageId as Integer)
	        if (message.postedBy == authenticatedUser.username || authenticatedUser.roles.find { it.name == 'SYSTEM ADMINISTRATOR' })
                message.delete(flush: true)
            else
                return redirect(controller: 'auth', action: 'unauthorized')
	    }
	    
        redirect(action: 'processInstance', id: params.processId)
	}
}
