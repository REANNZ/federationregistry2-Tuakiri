package aaf.fr.workflow

import org.apache.commons.logging.LogFactory
import org.springframework.transaction.annotation.*

import grails.plugins.federatedgrails.Role

import aaf.fr.identity.Subject

/**
 * Provides functionality to create, manage and execute workflow tasks.
 *
 * @author Bradley Beddoes
 */
class WorkflowTaskService {
  static transactional = true
  
  def final paramKey = /\{(.+?)\}/
  
  def sessionFactory
  def grailsApplication
  def messageSource
  def mailService
  def groovyPageRenderer
  def grailsLinkGenerator
  
  def initiate (def processInstanceID, def taskID) {
    def processInstance = ProcessInstance.get(processInstanceID)
    if(!processInstance) {
      log.error "Initiate requested on processInstance $processInstanceID but no such instance exists"
      return
    }
    
    def task = Task.get(taskID)
    if(!task) {
      log.error "Initiate requested on task $taskID but no such instance exists"
      return
    }
    
    def taskInstance
    
    // This will generally only be the case where a task is a join point in 
    // our flow and must wait for multiple dependenices to complete
    processInstance.taskInstances.each { ti ->
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
  
      if(!processInstance.save()) {
        log.error "Unable to update $processInstance with new taskInstance"
        task.errors.each { log.error it }
        throw new ErronousWorkflowStateException("Unable to save ${processInstance} when initiating ${taskInstance}")
      }
  
      if(!task.save()) {
        log.error "Unable to update Task with new instance for $processInstance and $task"
        task.errors.each { log.error it }
        throw new ErronousWorkflowStateException("Unable to save ${task} when initiating ${taskInstance}")
      }
  
      if(!taskInstance.save()) {
        log.error "Unable to create taskInstance for $processInstance and $task"
        task.errors.each { log.error it }
        throw new ErronousWorkflowStateException("Unable to save when initiating ${taskInstance}")
      }
    }
  
    taskInstance.get()
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

        if(!taskInstance.save()) {
          log.error "While attempting to update taskInstance ${taskInstance.id} with DEPENDENCYWAIT status a failure occured"
          taskInstance.errors.each { log.error it }
          throw new ErronousWorkflowStateException("Unable to save when waiting for dependencies of ${taskInstance}")
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
    def taskInstance = TaskInstance.get(taskInstanceID)
    if(!taskInstance) {
      log.error "Termination requested on taskInstanceID $taskInstanceID but no such instance exists"
      return
    }
    log.info "Terminating execution of $taskInstance in ${taskInstance.processInstance}"
    taskInstance.status = TaskStatus.TERMINATED
    if(!taskInstance.save()) {
      log.error "While attempting to update taskInstance ${taskInstance.id} with TERMINATED status a failure occured"
      taskInstance.errors.each { log.error it }
      throw new ErronousWorkflowStateException("Unable to save when terminating ${taskInstance}")
    }
  }
  
  def approve(def taskInstanceID) {
    def taskInstance = TaskInstance.get(taskInstanceID)
    if(!taskInstance) {
      log.error "Approval requested on taskInstanceID $taskInstanceID but no such instance exists"
      return
    }
    log.info "Approving execution of $taskInstance in ${taskInstance.processInstance}"
    taskInstance.status = TaskStatus.APPROVALGRANTED
    taskInstance.approver = subject
    if(!taskInstance.save()) {
      log.error "While attempting to update taskInstance ${taskInstance.id} with APPROVALGRANTED status a failure occured"
      taskInstance.errors.each { log.error it }
      throw new ErronousWorkflowStateException("Unable to save when approving ${taskInstance}")
    }
    if(taskInstance.task.executes()) {
      log.debug "Triggering execute for taskInstance ${taskInstance.id} bound to task ${taskInstance.task.name}"
      execute(taskInstance.id)
    }
    else
      if(taskInstance.task.hasOutcome()) {
        log.debug "Triggering outcome for taskInstance ${taskInstance.id} bound to task ${taskInstance.task.name}"
        complete(taskInstanceID, taskInstance.task.outcomes.keySet().iterator().next()) // We know from validation that if a task doesn't execute it must have 1 and only 1 outcome
      }
      else {
        finalize(taskInstance.id)
      }
  }
  
  def reject(def taskInstanceID, def rejectionName) {
    def taskInstance = TaskInstance.get(taskInstanceID)
    if(!taskInstance) {
      log.error "Rejection requested on taskInstanceID $taskInstanceID but no such instance exists"
      return
    }
    
    def rejection = taskInstance.task.rejections.get(rejectionName)
    if(!rejection) {
      log.error "Rejection identifier $rejectionName requested on $taskInstance but no such rejection exists"
      return
    }
    
    log.info "Rejecting execution of $taskInstance in ${taskInstance.processInstance} due to $rejectionName"
    taskInstance.status = TaskStatus.APPROVALREJECTED
    taskInstance.approver = subject
    if(!taskInstance.save()) {
      log.error "While attempting to update $taskInstance with APPROVALREJECTED status a failure occured"
      taskInstance.errors.each { log.error it }
      throw new ErronousWorkflowStateException("Unable to save when rejecting ${taskInstance}")
    }
    
    terminateAndStartTasks(taskInstance, rejection)
  }
  
  def execute(def taskInstanceID) {
    def taskInstance = TaskInstance.get(taskInstanceID)
    if(!taskInstance) {
      log.error "Execute requested on taskInstanceID $taskInstanceID but no such instance exists"
      return
    }
    
    taskInstance.status = TaskStatus.INPROGRESS
    if(!taskInstance.save()) {
      log.error "While attempting to update $taskInstance with INPROGRESS status a failure occured"
      taskInstance.errors.each { log.error it }
      throw new ErronousWorkflowStateException("Unable to save when executing ${taskInstance}")
    }
    
    log.debug "Executing $taskInstance for ${taskInstance.processInstance}"
    def env = [:]
    env.putAll(taskInstance.processInstance.params)
    env.taskInstanceID = taskInstanceID
    
    def serviceName = taskInstance.task.execute.get('service')
    if(serviceName) {
      def methodName = taskInstance.task.execute.get('method')
      executeService(serviceName, methodName, env)
    }
    else {
      def scriptName = taskInstance.task.execute.get('script')
      executeScript(scriptName, env)
    }
  }
  
  def complete(def taskInstanceID, def outcomeName) {
    def taskInstance = TaskInstance.get(taskInstanceID)
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
    if(!taskInstance.save()) {
      log.error "While attempting to update taskInstance ${taskInstance.id} with SUCCESSFUL status a failure occured"
      taskInstance.errors.each { log.error it }
      throw new ErronousWorkflowStateException("Unable to save when completing ${taskInstance}")
    }
    
    log.info "Completed $taskInstance for ${taskInstance.processInstance} with outcome $outcomeName"
    terminateAndStartTasks(taskInstance, outcome)
  }
  
  def finalize (def taskInstanceID) {
    def taskInstance = TaskInstance.get(taskInstanceID)
    if(!taskInstance) {
      log.error "Finalize requested on taskInstanceID $taskInstanceID but no such instance exists"
      return
    }
    
    taskInstance.status = TaskStatus.FINALIZED
    if(!taskInstance.save()) {
      log.error "While attempting to update $taskInstance with FINALIZED status a failure occured"
      taskInstance.errors.each { log.error it }
      throw new ErronousWorkflowStateException("Unable to save when finalizing ${taskInstance}")
    }
    log.info "Finalized $taskInstance for ${taskInstance.processInstance}"

    taskInstance.processInstance.status = ProcessStatus.COMPLETED
    log.info "Finalized ${taskInstance.processInstance} as nothing left to execute in this branch of the decision tree"
  }
  
  def requestApproval(def taskInstanceID) {
    // This is a task defined with an approver directive which means we need to request permission from SUBJECTS || ROLES to proceed
    def taskInstance = TaskInstance.get(taskInstanceID)
    if(!taskInstance) {
      log.error "Approval generation requested on taskInstanceID $taskInstanceID but no such instance exists"
      return
    }
    
    taskInstance.status = TaskStatus.APPROVALREQUIRED

    def identifier, subject, role
    def subjectList = []
    for(approver in taskInstance.task.approvers) {
      identifier = processVal(approver, taskInstance.processInstance.params)
      if(identifier.isLong())
        subject = Subject.get(identifier)
      if(subject) {
        log.debug "Identified subject ${subject} by a match of the identifier '${identifier}' to their ID attribute"
        if (!subjectList.contains(subject))
          subjectList.add(subject)
      }
      else {
        subject = Subject.findByPrincipal(identifier)
        if(subject) {
          log.debug "Identified subject ${subject} by a match of the identifier '${identifier}' to their principal attribute"
          if (!subjectList.contains(subject))
            subjectList.add(subject)
        }
        else {
          subject = Subject.findByEmail(identifier)
          if(subject){
            log.debug "Identified subject ${subject} by a match of the identifier '${identifier}' to their email attribute"
            if (!subjectList.contains(subject))
              subjectList.add(subject)
          }
          else {
            log.error "Attempting to identify subject by identifier '${identifier}' for process '${taskInstance.processInstance.description}' to request approval for task '${taskInstance.task.name}' failed. Workflow is erronous and should be checked."
            continue
          }
        }
      }
    }
    taskInstance.task.approverRoles.each {
      identifier = processVal(it, taskInstance.processInstance.params)
      role = Role.findByName(identifier)
      if(role) {
        log.debug "Identified ${role} as a name match of the identifier '${identifier}', adding subjects to approval notification"
        role.subjects.each { sb -> 
          def s = aaf.fr.identity.Subject.get(sb.id)
          if (!subjectList.contains(s))
            subjectList.add(s)
        }
      }
      else {
        log.error "Attempting to identify role by identifier '${identifier}' for process '${taskInstance.processInstance.description}' to request approval for task '${taskInstance.task.name}' failed. Workflow is erronous and should be checked."
      }
    }
  
    if(subjectList.size() == 0) {
      // The task requires approval but all avenues to locate an authoritative source have failed. The process is now effectively dead
      log.error "Unable to locate a valid approver for process '${taskInstance.processInstance.description}' and task '${taskInstance.task.name}', process is invalid and will be terminated"
      taskInstance.status = TaskStatus.APPROVALFAILURE
      if(!taskInstance.save()) {
        log.error "While attempting to update taskInstance ${taskInstance.id} with APPROVALFAILURE status a failure occured"
        taskInstance.errors.each { log.error it }
        throw new ErronousWorkflowStateException("Unable to save invalid approval request for ${taskInstance}")
      }
    }
    else {
      subjectList.each { sb ->
        messageApprovalRequest(sb, taskInstance)
        taskInstance.addToPotentialApprovers(sb)
      }
      // Messages have been queued to all concerned requesting approval so we're now in a wait state
      log.debug "Located valid approver(s) for process '${taskInstance.processInstance.description}' and task '${taskInstance.task.name}', task will continue once approved"
      taskInstance.status = TaskStatus.APPROVALREQUIRED
      if(!taskInstance.save()) {
        log.error "While attempting to update taskInstance ${taskInstance.id} with REQUIRESAPPROVAL status a failure occured"
        taskInstance.errors.each { log.error it }
        throw new ErronousWorkflowStateException("Unable to save when requesting approval for ${taskInstance}")
      }
    }
  }
  
  void messageApprovalRequest(def sb, TaskInstance taskInstance) {
    log.debug "Sending approval request to $sb for $taskInstance"
    
    Object[] args = [taskInstance.task.name]/*
    def waListLink = grailsLinkGenerator.link(controller:"workflowApproval", action:"list", absolute:"true")
    def contents = groovyPageRenderer.render(view: '/templates/mail/_workflow_requestapproval', plugin: "base", model: [taskInstance: taskInstance, waListLink:waListLink])
    def email_msg = groovyPageRenderer.render(view: '/layouts/email')
    email_msg = email_msg.replace("<g:layoutBody/>", contents)*/
    mailService.sendMail {
      to sb.email   
      subject messageSource.getMessage('branding.fr.mail.workflow.requestapproval.subject', args, 'branding.fr.mail.workflow.requestapproval.subject', new Locale("EN"))
      //body email_msg
      body(view: '/templates/mail/_workflow_requestapproval', plugin: "federationworkflow", model: [taskInstance: taskInstance])
    }
  }
  
  def terminateAndStartTasks(def taskInstance, def reaction) {
    log.info "Processing $reaction terminate and start for $taskInstance"
    reaction.terminate?.each {
      def task = taskInstance.processInstance.process.tasks.find { t -> t.name == taskName }
      if(!task) {
        log.error "Unable to locate terminate task with name $taskName defined in ${taskInstance.processInstance.process}"
      }
      log.debug "Firing start for $task in ${taskInstance.processInstance}"
      terminate(taskInstance.processInstance.id, task.id)
    }
    reaction.start?.each { taskName ->
      def task = taskInstance.processInstance.process.tasks.find { t -> t.name == taskName }
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
              val = val.replaceAll(paramKey, params.get(key[0][1]))
      }
    return val
  }
  
  def retrieveTasksAwaitingApproval(def subject) {
    def c = TaskInstance.createCriteria()
    def tasks = c.listDistinct {
      and {
        eq("status", TaskStatus.APPROVALREQUIRED)
        potentialApprovers {
          eq("principal", subject.principal)
        }
        processInstance {
          eq("status", ProcessStatus.INPROGRESS)
        }
      }
    }
    tasks
  }

  def retrieveTasksSubmittedForApproval(def subject) {
    def c = TaskInstance.createCriteria()
    def tasks = c.listDistinct {
      and {
        eq("status", TaskStatus.APPROVALREQUIRED)
        potentialApprovers {
          eq("principal", subject.principal)
        }
        processInstance {
          eq("status", ProcessStatus.INPROGRESS)
        }
      }
    }
    tasks
  }
  
  def executeService(def serviceName, def methodName, def env) {
    def service = grailsApplication.mainContext.getBean(serviceName);
    
    if(service)
      service."$methodName"(env)
    else
      log.error "Attempt to execute workflow that references service named $serviceID. Unable to locate service, no execution has taken place"
  }
  
  def executeScript(def scriptName, def env) {
    def workflowScript = WorkflowScript.findByName(scriptName)
    
    if(workflowScript) {
      def _log = LogFactory.getLog("domains.fr.workflow.script.$scriptName")
      
      Binding binding = new Binding()
      binding.setVariable("env", env)
      binding.setVariable("grailsApplication", grailsApplication)
      binding.setVariable("ctx", grailsApplication.mainContext)
      binding.setVariable("log", _log)
      
      def script = new GroovyShell(grailsApplication.classLoader, binding).parse(workflowScript.definition)
      script.binding = binding
      script.run()
    }
    else
      log.error "Attempt to execute workflow that references script named $scriptID. Unable to locate script, no execution has taken place"
  }

}
