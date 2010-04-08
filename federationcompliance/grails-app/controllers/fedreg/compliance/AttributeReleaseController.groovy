package fedreg.compliance

import fedreg.core.IDPSSODescriptor
import fedreg.core.SPSSODescriptor

class AttributeReleaseController {
	
	def index = {	
		[activeIDP:IDPSSODescriptor.findAllWhere(active:true), activeSP:SPSSODescriptor.findAllWhere(active:true)]
	}
	
	def compare = {
		if(!params.idp || !params.sp) {
			log.debug("ID not provided for IDP and SP to compare")
			render message(code: 'fedreg.compliance.attributerelease.incorrectparams', args: [params.id])
			response.sendError(500)
			return	
		}
		
		def idp = IDPSSODescriptor.get(params.idp)
		if(!idp) {
			log.debug("No IDP matching ID ${params.idp} exists")
			render message(code: 'fedreg.compliance.attributerelease.noidp', args: [params.idp])
			response.sendError(500)
			return	
		}
		
		def sp = SPSSODescriptor.get(params.sp)
		if(!sp) {
			log.debug("No SP matching ID ${params.sp} exists")
			render message(code: 'fedreg.compliance.attributerelease.nosp', args: [params.sp])
			response.sendError(500)
			return	
		}
		
		def requiredAttributes = [] as List
		def optionalAttributes = [] as List
		boolean minimumRequirements = true
		
		// Collate all required attributes across ACS instances defined for this SP
		sp.attributeConsumingServices.each { acs ->
			acs.requestedAttributes.each { attr ->
				if(attr.isRequired) {
					if(!requiredAttributes.contains(attr)) {
						requiredAttributes.add(attr)
						if(!idp.attributes.contains(attr.attribute))
							minimumRequirements = false
					}
				} else {
					if(!optionalAttributes.contains(attr))
						optionalAttributes.add(attr)
				}
			}
		}
		
		[requiredAttributes:requiredAttributes, optionalAttributes:optionalAttributes, idp:idp, minimumRequirements:minimumRequirements]
		
	}
	
}