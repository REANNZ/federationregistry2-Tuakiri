
import fedreg.workflow.ExecutionActor

class WorkflowBootStrap {
	
  def processService
  def taskService

  def init = {servletContext ->
	def executionActor = new ExecutionActor(processService:processService, taskService:taskService)
	processService.executionActor = executionActor
	taskService.executionActor = executionActor
  }

  def destroy = {

  }

  private internalBootStap(def servletContext) {
    nimbleService.init()
  }
}