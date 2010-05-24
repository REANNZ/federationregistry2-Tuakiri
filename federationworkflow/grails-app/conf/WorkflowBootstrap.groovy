
import fedreg.workflow.ExecutionActor

class WorkflowBootStrap {
	
  def processService
  def taskService

  def init = {servletContext ->

  }

  def destroy = {

  }

  private internalBootStap(def servletContext) {
    nimbleService.init()
  }
}