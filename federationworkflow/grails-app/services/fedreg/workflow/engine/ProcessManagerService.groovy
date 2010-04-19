package fedreg.workflow.engine

import groovy.lang.Binding
import groovy.util.GroovyScriptEngine
import java.io.File
import java.util.Map

import org.apache.shiro.SecurityUtils
import grails.plugins.nimble.core.Role
import grails.plugins.nimble.core.UserBase

import fedreg.workflow.engine.ProcessDefinition
import fedreg.workflow.engine.ProcessInstance
import fedreg.workflow.engine.TaskDefinition
import fedreg.workflow.engine.TaskInstance

import fedreg.workflow.engine.ProcessDSL

import fedreg.workflow.engine.ProcessPriority
import fedreg.workflow.engine.ProcessStatus
import fedreg.workflow.engine.TaskStatus

import fedreg.workflow.engine.ComponentMissingException
import fedreg.workflow.engine.InvalidProcessDSLException
import fedreg.workflow.engine.InvalidNumberOfArgumentsException
import fedreg.workflow.engine.ProcessDefinitionNotFoundException
import fedreg.workflow.engine.TaskAlreadyCompletedException

class ProcessManagerService {

	def grailsApplication
	def messageService
	def roleService
	def messageSource

	def loadDSL(File file) throws InvalidProcessDSLException {

		GroovyScriptEngine gse = new GroovyScriptEngine(file.parent)
		Binding binding = new Binding()
		binding.process = { name, closure ->
			this.loadProcess(name, closure)
		}

		// Load the DSL into the database
		def processDefinition
		try {

			processDefinition = gse.run(file.name, binding)
			
			if (processDefinition) {

			    // Save the DSL file against the database record
			    BufferedReader reader = new BufferedReader(new FileReader(file))
			    def line
			    processDefinition.dsl = ""
			    while ((line = reader.readLine()) != null)
				    processDefinition.dsl = processDefinition.dsl + line + "\n"

			    processDefinition.validate()
			}

		} catch (groovy.util.ScriptException e) {
			throw new InvalidProcessDSLException(e.cause.message)

		} catch (groovy.lang.MissingMethodException e) {
			throw new InvalidProcessDSLException(e.message)

		} catch (groovy.lang.MissingPropertyException e) {
			throw new InvalidProcessDSLException(e.message)

		}

		return processDefinition

	}

	def loadProcess(String name, Closure closure) {
		def processes = ProcessDefinition.findAllByName(name)
		int version = 1
		if (processes) {
			version = processes.size() + 1
		}

		def processDefinition = new ProcessDefinition(name: name, processVersion: version)
		closure.delegate = new ProcessDSL(processDefinition)
		closure()

		return processDefinition
	}

	void createRoles(ProcessDefinition processDefinition) {

		processDefinition.tasks.each { task ->
			if (task.assignTo && !task.assignTo.equals("INITIATOR")) {
				def assignedToRole = Role.findByName(task.assignTo)
				if (!assignedToRole)
					roleService.createRole(task.assignTo, "Role created when uploading the '${processDefinition.name}' process", false)
			}

			task.actioners.each { actioner ->
				if (!actioner.equals("INITIATOR")) {
					def actionerRole = Role.findByName(actioner)
					if (!actionerRole)
						roleService.createRole(actioner, "Role created when uploading the '${processDefinition.name}' process", false)
				}
			}
		}

	}

	def getAssociatedRecord(int taskId) {
		def taskInstance = TaskInstance.findById(taskId)
		return taskInstance?.recordId
	}

	def getProcessInit(String processName) {
		def processDefinition = ProcessDefinition.findByName(processName)
		return processDefinition.getStartTask().action
	}

	private void actionTask(TaskInstance taskInstance, Map map) throws ComponentMissingException {

		if (taskInstance.status == TaskStatus.INITIATING || taskInstance.status == TaskStatus.INPROGRESS) {

			// Invoke the automated process if there is one
			if (taskInstance.definition.automated) {

				def taskDefinition = taskInstance.definition

				def componentType
				if (taskDefinition.action.containsKey('controller'))
					componentType = 'controller'
				else
					componentType = 'service'

				def componentClass = grailsApplication."${componentType + 'Classes'}".find {
								it.fullName.toLowerCase().startsWith(taskDefinition.action."${componentType}".toLowerCase())
							}

				if (componentClass == null) {
					throw new ComponentMissingException("Unable to locate the '" + taskDefinition.action[componentType] + "' " + componentType)
				}


				def component = componentClass.newInstance()
				if (componentType == 'controller') {
					component.processManagerService = this
					component.params.id = taskInstance.recordId
					component.params.taskId = taskInstance.id
					component."${taskDefinition.action.action}"()
				} else {
					component.processManagerService = this
					component."${taskDefinition.action.action}"(taskInstance)
				}

			// Assign to the role who will complete this task
			} else {
			
			    if (taskInstance.definition.assignTo && taskInstance.status != TaskStatus.INITIATING) {
			    		
				    taskInstance.assignedTo = taskInstance.definition.assignTo
				    taskInstance.save(flush: true)
				    
				    if (taskInstance.assignedTo == 'INITIATOR') {
				    
    			        def subject = message(code:"workflow.engine.assignment.initiator.subject", args:[taskInstance.parentProcess.name, taskInstance.name])
    		            def body =  message(code:"workflow.engine.assignment.initiator.body", args:[taskInstance.parentProcess.name, taskInstance.name])
                        messageService.sendMessage(user: taskInstance.parentProcess.initiatedBy, subject: subject, message: body)
                        
				    } else {
				    
       			        def subject = message(code:"workflow.engine.assignment.role.subject", args:[taskInstance.parentProcess.name, 
       			                                                                                    taskInstance.name, 
       			                                                                                    taskInstance.assignedTo])
       			                                                                                    
    		            def body =  message(code:"workflow.engine.assignment.role.body", args:[taskInstance.parentProcess.name, 
       			                                                                               taskInstance.name, 
       			                                                                               taskInstance.assignedTo])
       			                                                                               
				        messageService.sendMessage(role: taskInstance.assignedTo, subject: subject, message: body)
				    }
		        }
			}
			
			

			// Complete the task if an action result as been supplied
			if (taskInstance.definition.hasActionResult(map.actionResult) || taskInstance.definition.finishOnThisTask) {
				finaliseTask(taskInstance, map)
			}
		}
	}
	
	void cancelProcess(int processId) {
			
		def process = ProcessInstance.findById(processId)
		if (!process) {
		    throw new NotFoundException("Process instance with Id: " + processId + " could not be found!")
		}
		
		cancelProcess(process)
    }
		
    void cancelProcess(ProcessInstance process) {
		
		def authenticatedUser = UserBase.findById(SecurityUtils.subject.principal)
		
        process.status = ProcessStatus.CANCELLED
        process.dateCompleted = new Date()
        process.save(flush: true)
                
        process.tasks.each { task ->        
            if (task.status != TaskStatus.COMPLETED && task.status != TaskStatus.CANCELLED) {
                task.status = TaskStatus.CANCELLED
                task.actionResult = "CANCELLED"
                task.actionedBy = authenticatedUser.username
                task.dateCompleted = new Date()
                task.save(flush: true)
                
                // Send email message
                def subject = message(code:"workflow.engine.cancelled.assignedTo.subject", args:[process.name, process.initiatedBy])
                def body =  message(code:"workflow.engine.cancelled.assignedTo.body", args:[process.name, process.initiatedBy]) 
			
        		messageService.sendMessage(role: task.assignedTo, subject: subject, message: body)
            }
        }
        
        // Send email message to initiator
        def subject = message(code:"workflow.engine.cancelled.initiator.subject", args:[process.name])
        def body =  message(code:"workflow.engine.cancelled.initiator.body", args:[process.name]) 
	
		messageService.sendMessage(user: process.initiatedBy, subject: subject, message: body)
        
	}

	private void finaliseTask(TaskInstance taskInstance, Map map) throws TaskAlreadyCompletedException {
    
        if (taskInstance.status == TaskStatus.COMPLETED) {
            throw new TaskAlreadyCompletedException("The task has already been completed")
        } else if (taskInstance.status == TaskStatus.CANCELLED) {
            throw new TaskAlreadyCompletedException("The task has already been cancelled")
        }

		taskInstance.with {
			status = TaskStatus.COMPLETED
			actionedBy = map.actionedBy
			actionResult = map.remove('actionResult')
			if (map.containsKey('message'))
			    taskInstance.message = map.message
			dateCompleted = new Date()
		}

		// Check if the end of the process
		if (taskInstance.definition.finishOnThisTask) {
			taskInstance.parentProcess.dateCompleted = new Date()
			taskInstance.parentProcess.status = ProcessStatus.COMPLETED
			
			def subject = message(code:"workflow.engine.completion.subject", args:[taskInstance.parentProcess.name])
		    def body =  message(code:"workflow.engine.completion.body", args:[taskInstance.parentProcess.name]) 
			
			messageService.sendMessage(user: taskInstance.parentProcess.initiatedBy, subject: subject, message: body)
			
		}

		taskInstance.parentProcess.save(flush: true)

		taskInstance.definition.getNextTask(taskInstance.actionResult)?.each { taskName ->
			startNextTask(taskInstance, taskName, map)
		}
	}

    private void cancelTask(TaskInstance taskInstance) {        
        if (taskInstance.status != TaskStatus.COMPLETED) {
            taskInstance.status = TaskStatus.CANCELLED
            taskInstance.save(flush: true)
        } else {
            taskInstance.parentProcess.tasks.each { task ->
                if (task.calledBy.contains(taskInstance))
                    cancelTask(task)
            }
        }
    }

	private void startNextTask(TaskInstance currentTaskInstance, String taskName, Map map) {

		def taskDefinition = currentTaskInstance.definition.parentProcess.getTask(taskName)
		def taskInstance = currentTaskInstance.parentProcess.tasks.find { it.name == taskName && it.status == TaskStatus.PENDING }
		if (!taskInstance) {
			taskInstance = new TaskInstance()
			taskInstance.with {
				name = taskDefinition.name
				status = TaskStatus.PENDING
				if (map.recordId)
					taskInstance.recordId = map.recordId
				assignedTo = taskDefinition.assignTo
				addToCalledBy(currentTaskInstance)
			}
			currentTaskInstance.parentProcess.addToTasks(taskInstance)
			taskDefinition.addToInstances(taskInstance)

		} else {
			taskInstance.addToCalledBy(currentTaskInstance)
		}
		
		taskInstance.save(flush: true)
		
		// Check cancel list
		currentTaskInstance.definition.getCancelTask(currentTaskInstance.actionResult)?.each { cancelTaskName ->
		    def listOfTasks = currentTaskInstance.parentProcess.tasks.findAll { it.name == cancelTaskName } 
		    def taskToBeCancelled = listOfTasks.sort { it.id }.last()
		    cancelTask(taskToBeCancelled)
		}

		// Check dependencies to see whether the task can be actioned
		if (taskInstance.calledBy.size() == taskDefinition.dependencies.size() || taskDefinition.dependencies.size() == 0) {
			taskInstance.status = TaskStatus.INPROGRESS
		}
		
		taskInstance.save(flush: true)
		actionTask(taskInstance, map)
	}

	long startProcess(Map map) throws InvalidNumberOfArgumentsException, ProcessDefinitionNotFoundException, ComponentMissingException {

		if (!(map.containsKey('process') && map.containsKey('name') && map.containsKey('recordId') && map.containsKey('actionedBy'))) {
			throw new InvalidNumberOfArgumentsException("To start a process you must supply a map containing (as a minimum): process, recordId and actionedBy")
		}

		def processDefinitions = ProcessDefinition.findAllByName(map.process)
		if (!processDefinitions) {
			throw new ProcessDefinitionNotFoundException("The process definition for " + map.process + " was not found!")
		}

		def processDefinition = processDefinitions.sort { it.id }.last()
		def taskDefinition = processDefinition.getStartTask()

		def processInstance = new ProcessInstance(name: map.name, status: ProcessStatus.INPROGRESS, initiatedBy: map.actionedBy, dateInitiated: new Date())		


		if (map.priority)
    	    processInstance.priority = map.priority
        else
            processInstance.priority = ProcessPriority.LOW
            

		def taskInstance = new TaskInstance()
		taskInstance.with {
			name = taskDefinition.name
            if (map.actionResult)
                status = TaskStatus.INITIATING
            else
      			status = TaskStatus.INPROGRESS
			recordId = map.recordId
		}
		processInstance.addToTasks(taskInstance)
		processDefinition.addToInstances(processInstance)
		taskDefinition.addToInstances(taskInstance)
		processInstance.save(flush: true)

		actionTask(taskInstance, map)

        return processInstance.id
	}

	long taskCompleted(Map map) throws InvalidNumberOfArgumentsException, NotFoundException, TaskAlreadyCompletedException {

		// Start of a new process
		if (map.containsKey('process')) {
			return startProcess(map)

		// Continue a pre-existing process
		} else if (map.containsKey('taskId') && map.containsKey('actionResult') && map.containsKey('actionedBy')) {
			def taskInstance = TaskInstance.findById(map.taskId)
			if (taskInstance) {
					
        		if (map.priority) {
        		    taskInstance.parentProcess.priority = map.priority
        		    taskInstance.parentProcess.save(flush: true)
        		}
        		    
				finaliseTask(taskInstance, map)
                return taskInstance.parentProcess.id
			} else {
				throw new NotFoundException("Task instance with Id: " + map.taskId + " could not be found!")
			}
		} else {
			throw new InvalidNumberOfArgumentsException("To complete a task you must supply a map containing (as a minimum): process or taskId, actionResult, recordId and actionedBy")
		}
	}
	
	private def message = { map ->
        return messageSource.resolveCode(map.code, new java.util.Locale("EN")).format(map.args as Object[])
	}
}
