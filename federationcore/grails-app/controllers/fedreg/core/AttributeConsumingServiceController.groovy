package fedreg.core

class AttributeConsumingServiceController {
	
	static allowedMethods = [remove: "POST"]
	
	def addRequestedAttribute = {
		if(!params.id) {
			log.warn "Attribute Consuming Service ID was not present"
			render message(code: 'fedreg.controllers.namevalue.missing')
			response.setStatus(500)
			return
		}
		
		if(!params.attrid) {
			log.warn "Requested attribute ID was not present"
			render message(code: 'fedreg.controllers.namevalue.missing')
			response.setStatus(500)
			return
		}
		
		if(!params.reasoning) {
			log.warn "Reason for requesting attribute was not present"
			render message(code: 'fedreg.controllers.namevalue.missing')
			response.setStatus(500)
			return
		}
		
		def acs = AttributeConsumingService.get(params.id)
		if(!acs) {
			log.warn "Attribute Consuming Service identified by id ${params.id} was not located"
			render message(code: 'fedreg.attributeconsumingservice.nonexistant', args: [params.id])
			response.setStatus(500)
			return
		}
		
		def attr = Attribute.get(params.attrid)
		if(!attr) {
			log.warn "Attribute identified by id ${params.attrid} was not located"
			render message(code: 'fedreg.attr.nonexistant', args: [params.id])
			response.setStatus(500)
			return
		}
		
		for( a in acs.requestedAttributes) {
			if(a.attribute == attr) {
				log.warn "${a} already supported by ${acs} and ${acs.descriptor} not adding"
				render message(code: 'fedreg.attributeconsumingservice.requestedattribute.add.already.exists')
				response.setStatus(500)
				return
			}
		}
		
		def reqAttr = new RequestedAttribute(reasoning:params.reasoning, attribute: attr, isRequired: params.isrequired, approved: false)
		acs.addToRequestedAttributes(reqAttr)
		acs.save(flush:true)
		if(acs.hasErrors()) {
			acs.errors.each {
				log.warn it
			}
			render message(code: 'fedreg.attributeconsumingservice.requestedattribute.add.failed')
			response.setStatus(500)
			return
		}
		
		log.debug "Added ${reqAttr} referencing ${attr} to ${acs} and ${acs.descriptor}"
		render message(code: 'fedreg.attributeconsumingservice.requestedattribute.add.success')
	}
	
	def listRequestedAttributes = {
		if(!params.id) {
			log.warn "Attribute Consuming Service ID was not present"
			render message(code: 'fedreg.controllers.namevalue.missing')
			response.setStatus(500)
			return
		}
		if(!params.containerID) {
			log.warn "Rendering container ID was not present"
			render message(code: 'fedreg.controllers.namevalue.missing')
			response.setStatus(500)
			return
		}
		
		def acs = AttributeConsumingService.get(params.id)
		if(!acs) {
			log.warn "Attribute Consuming Service identified by id ${params.id} was not located"
			render message(code: 'fedreg.attributeconsumingservice.nonexistant', args: [params.id])
			response.setStatus(500)
			return
		}
		render (template:"/templates/acs/listrequestedattributes", contextPath: pluginContextPath, model:[requestedAttributes:acs.requestedAttributes, containerID:params.containerID])
	}
	
	def removeRequestedAttribute = {		
		if(!params.raid) {
			log.warn "Requested attribute ID was not present"
			render message(code: 'fedreg.controllers.namevalue.missing')
			response.setStatus(500)
			return
		}
		
		def requestedAttribute = RequestedAttribute.get(params.raid)
		if(!requestedAttribute) {
			log.warn "Requested attribute identified by id ${params.raid} was not located"
			render message(code: 'fedreg.attributeconsumingservice.nonexistant', args: [params.id])
			response.setStatus(500)
			return
		}
		
		def acs = requestedAttribute.attributeConsumingService
		acs.removeFromRequestedAttributes(requestedAttribute)
		acs.save()
		if(acs.hasErrors()) {
			acs.errors.each {log.debug it}
			log.warn "Attempt to save ${acs} after trying to remove ${requestedAttribute} failed"
			render message(code: 'fedreg.attributeconsumingservice.requestedattribute.remove.failed')
			response.setStatus(500)
			return
		}
		requestedAttribute.delete()
		
		log.debug "Removed ${requestedAttribute} referencing ${requestedAttribute.attribute} from ${acs}"
		render message(code: 'fedreg.attributeconsumingservice.requestedattribute.remove.success')
	}
	
}