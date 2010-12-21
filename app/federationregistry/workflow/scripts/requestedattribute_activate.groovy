
import grails.plugins.nimble.core.*
import fedreg.core.*


workflowTaskService = ctx.getBean("workflowTaskService")

def requestedAttribute = RequestedAttribute.get(env.requestedAttribute.toLong())

if(requestedAttribute) {

	log.info "Activating $requestedAttribute. Workflow indicates it is valid and accepted for operation."
	
	requestedAttribute.approved = true
	requestedAttribute.save()
	
	if(requestedAttribute.hasErrors()) {
		throw new RuntimeException("Attempt to process activate in script requestedattribute_activate. Failed due to ${requestedAttribute} fault on save")
	}

	workflowTaskService.complete(env.taskInstanceID.toLong(), 'requestedattributeactivated')
}
else {
	throw new RuntimeException("Attempt to process activate in script requestedattribute_activate. Failed because referenced requested attribute does not exist")
}