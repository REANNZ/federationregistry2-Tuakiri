package fedreg.core

import org.apache.shiro.SecurityUtils
import org.springframework.context.i18n.LocaleContextHolder as LCH

import fedreg.workflow.ProcessPriority

/**
 * Provides AttributeConsumingService views.
 *
 * @author Bradley Beddoes
 */
class AttributeConsumingServiceController {
	def allowedMethods = [addSpecifiedAttributeValue:'POST', addRequestedAttribute:'POST', removeSpecifiedAttributeValue:'DELETE', removeRequestedAttribute:'DELETE', updateRequestedAttribute:'PUT']
		
	def workflowProcessService
	
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
		render (template:"/templates/acs/listrequestedattributes", contextPath: pluginContextPath, model:[requestedAttributes:acs.sortedAttributes(), containerID:params.containerID])
	}
	
	def listSpecifiedAttributes = {
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
		render (template:"/templates/acs/listspecifiedattributes", contextPath: pluginContextPath, model:[requestedAttributes:acs.sortedAttributes(), specificationAttributes: AttributeBase.findAllWhere(specificationRequired:true), containerID:params.containerID])
	}
	
	def listSpecifiedAttributeValue = {
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
		
		def reqAttr = RequestedAttribute.get(params.id)
		if(!reqAttr) {
			log.warn "Requested Attribute identified by id ${params.id} was not located"
			render message(code: 'fedreg.attr.nonexistant', args: [params.id])
			response.setStatus(500)
			return
		}
		
		render (template:"/templates/acs/listspecifiedattributevalues", contextPath: pluginContextPath, model:[requestedAttribute:reqAttr, containerID:params.containerID])
	}
	
	def addSpecifiedAttributeValue = {
		if(!params.id) {
			log.warn "RequestedAttribute ID was not present"
			render message(code: 'fedreg.controllers.namevalue.missing')
			response.setStatus(500)
			return
		}
		
		if(!params.value) {
			log.warn "Value to add was not present"
			render message(code: 'fedreg.controllers.namevalue.missing')
			response.setStatus(500)
			return
		}
		
		def reqAttr = RequestedAttribute.get(params.id)
		if(!reqAttr) {
			log.warn "Requested Attribute identified by id ${params.id} was not located"
			render message(code: 'fedreg.attr.nonexistant', args: [params.id])
			response.setStatus(500)
			return
		}

		if(SecurityUtils.subject.isPermitted("descriptor:${reqAttr.attributeConsumingService.descriptor.id}:attribute:value:add")) {
			reqAttr.addToValues(new AttributeValue(value:params.value))
			reqAttr.save(flush:true)
			if(reqAttr.hasErrors()) {
				reqAttr.errors.each {
					log.warn it
				}
				render message(code: 'fedreg.attributeconsumingservice.requestedattribute.add.failed')
				response.setStatus(500)
				return
			}
		
			log.info "$authenticatedUser added value ${params.value} to ${reqAttr} referencing ${reqAttr.base} for ${reqAttr.attributeConsumingService} belonging to ${reqAttr.attributeConsumingService.descriptor}"
			render message(code: 'fedreg.attributeconsumingservice.requestedattribute.specifiedvalue.add.success')
		} else {
			log.warn("Attempt to add a specifed attribute value by $authenticatedUser was denied, incorrect permission set")
			response.sendError(403)
		}
	}
	
	def removeSpecifiedAttributeValue = {
		if(!params.id) {
			log.warn "RequestedAttribute ID was not present"
			render message(code: 'fedreg.controllers.namevalue.missing')
			response.setStatus(500)
			return
		}
		
		if(!params.valueid || !params.valueid.isLong()) {
			log.warn "Value to add was not present"
			render message(code: 'fedreg.controllers.namevalue.missing')
			response.setStatus(500)
			return
		}
		
		def reqAttr = RequestedAttribute.get(params.id)
		if(!reqAttr) {
			log.warn "Requested Attribute identified by id ${params.id} was not located"
			render message(code: 'fedreg.attr.nonexistant', args: [params.id])
			response.setStatus(500)
			return
		}
		if(SecurityUtils.subject.isPermitted("descriptor:${reqAttr.attributeConsumingService.descriptor.id}:attribute:value:remove")) {
			def val
			for(v in reqAttr.values) {
				if(v.id == params.valueid.toLong()) {
					val = v
					break
				}
			}
		
			if(!val) {
				log.warn "Value identified by id ${params.valueid} was not associated with ${reqAttr}"
				render message(code: 'fedreg.attr.nonexistant', args: [params.id])
				response.setStatus(500)
				return
			}
		
			reqAttr.removeFromValues(val)
			reqAttr.save(flush:true)
			if(reqAttr.hasErrors()) {
				reqAttr.errors.each {
					log.warn it
				}
				render message(code: 'fedreg.attributeconsumingservice.requestedattribute.remove.failed')
				response.setStatus(500)
				return
			}
		
			log.info "$authenticatedUser removed ${val} from ${reqAttr} referencing ${reqAttr.base} for ${reqAttr.attributeConsumingService} belonging to ${reqAttr.attributeConsumingService.descriptor}"
			render message(code: 'fedreg.attributeconsumingservice.requestedattribute.specifiedvalue.remove.success')
		} else {
			log.warn("Attempt to remove a specifed attribute value by $authenticatedUser was denied, incorrect permission set")
			response.sendError(403)
		}
	}
	
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
		
		if(SecurityUtils.subject.isPermitted("descriptor:${acs.descriptor.id}:attribute:add")) {			
			def attr = AttributeBase.get(params.attrid)
			if(!attr) {
				log.warn "Attribute identified by id ${params.attrid} was not located"
				render message(code: 'fedreg.attr.nonexistant', args: [params.id])
				response.setStatus(500)
				return
			}
		
			for( a in acs.requestedAttributes) {
				if(a.base == attr) {
					log.warn "${a} already supported by ${acs} and ${acs.descriptor} not adding"
					render message(code: 'fedreg.attributeconsumingservice.requestedattribute.add.already.exists')
					response.setStatus(500)
					return
				}
			}
		
			def reqAttr = new RequestedAttribute(reasoning:params.reasoning, base: attr, isRequired: params.isrequired ? true:false, approved: false, attributeConsumingService:acs)
			acs.addToRequestedAttributes(reqAttr)

			if(!reqAttr.save()) {
				reqAttr.errors.each {
					log.warn it
				}
				render message(code: 'fedreg.attributeconsumingservice.requestedattribute.add.failed')
				response.setStatus(500)
				return
			}
			
			def workflowParams = [ creator:authenticatedUser?.contact?.id?.toString(), requestedAttribute:reqAttr?.id?.toString(), serviceProvider:acs?.descriptor?.id?.toString(), locale:LCH.getLocale().getLanguage() ]
			def (initiated, processInstance) = workflowProcessService.initiate( "requestedattribute_create", "Approval for addition of the attribute '${reqAttr.base?.friendlyName}' (OID: ${reqAttr.base?.oid}) to the service '${acs?.descriptor?.displayName}'", ProcessPriority.MEDIUM, workflowParams)

			if(initiated)
				workflowProcessService.run(processInstance)
			else
				throw new ErronousStateException("Unable to execute workflow when creating ${reqAttr}")
		
			log.info "$authenticatedUser added ${reqAttr} referencing ${attr} to ${acs} and ${acs.descriptor}"
			render message(code: 'fedreg.attributeconsumingservice.requestedattribute.add.success')
		} else {
			log.warn("Attempt to add a requested attribute by $authenticatedUser was denied, incorrect permission set")
			response.sendError(403)
		}
	}
	
	def updateRequestedAttribute = {
		if(!params.id) {
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
		
		def ra = RequestedAttribute.get(params.id)
		if(!ra) {
			log.warn "Requested Attribute identified by id ${params.id} was not located"
			render message(code: 'fedreg.attributeconsumingservice.nonexistant', args: [params.id])
			response.setStatus(500)
			return
		}
		
		if(SecurityUtils.subject.isPermitted("descriptor:${ra.attributeConsumingService.descriptor.id}:attribute:update")) {
			ra.reasoning = params.reasoning
			ra.isRequired = params.required ? true:false
			
			if(!ra.save()) {
				ra.errors.each {
					log.warn it
				}
				render message(code: 'fedreg.attributeconsumingservice.requestedattribute.update.failed')
				response.setStatus(500)
				return
			}
		
			log.info "$authenticatedUser updated ${ra} referencing ${ra.base}"
			render message(code: 'fedreg.attributeconsumingservice.requestedattribute.update.success')
		} else {
			log.warn("Attempt to update a requested attribute by $authenticatedUser was denied, incorrect permission set")
			response.sendError(403)
		}
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
		
		if(SecurityUtils.subject.isPermitted("descriptor:${acs.descriptor.id}:attribute:remove")) {
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
		
			log.info "$authenticatedUser removed ${requestedAttribute} referencing ${requestedAttribute.base} from ${acs} and ${acs.descriptor}"
			render message(code: 'fedreg.attributeconsumingservice.requestedattribute.remove.success')
		} else {
			log.warn("Attempt to remove a requested attribute by $authenticatedUser was denied, incorrect permission set")
			response.sendError(403)
		}
	}
	
}