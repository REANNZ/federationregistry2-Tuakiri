import grails.plugins.federatedgrails.*
import aaf.fr.foundation.*


workflowTaskService = ctx.getBean("workflowTaskService")

def requestedAttribute = RequestedAttribute.get(env.requestedAttribute.toLong())

if(requestedAttribute) {

  log.info "Deleting $requestedAttribute. Workflow indicates it is invalid and not accepted for operation."
  
  requestedAttribute.delete()

  workflowTaskService.complete(env.taskInstanceID.toLong(), 'requestedattributedeleted')
}
else {
  throw new RuntimeException("Attempt to process delete in script requestedattribute_delete. Failed because referenced requested attribute does not exist")
}
