package aaf.fr.reporting

import fedreg.core.IDPSSODescriptor
import fedreg.core.SPSSODescriptor

/**
 * Provides attribute release compliance views.
 *
 * @author Bradley Beddoes
 */
class AttributeReleaseController {
	
	def index = {	
		[activeIdentityProviderList:IDPSSODescriptor.findAllWhere(active:true), activeServiceProviderList:SPSSODescriptor.findAllWhere(active:true)]
	}
	
	def compare = {
		if(!params.idp || !params.sp) {
			log.debug("ID not provided for IDP and SP to compare")
			render message(code: 'fedreg.compliance.attributerelease.incorrectparams', args: [params.id])
			response.sendError(500)
			return	
		}
		
		def identityProvider = IDPSSODescriptor.get(params.idp)
		if(!identityProvider) {
			log.debug("No IDP matching ID ${params.idp} exists")
			render message(code: 'fedreg.compliance.attributerelease.noidp', args: [params.idp])
			response.sendError(500)
			return	
		}
		
		def serviceProvider = SPSSODescriptor.get(params.sp)
		if(!serviceProvider) {
			log.debug("No SP matching ID ${params.sp} exists")
			render message(code: 'fedreg.compliance.attributerelease.nosp', args: [params.sp])
			response.sendError(500)
			return	
		}
		
		def requiredAttributes = [] as List
		def optionalAttributes = [] as List
		def suppliedRequiredAttributes = [] as List
		def suppliedOptionalAttributes = [] as List
		boolean minimumRequirements = true
		
		// Collate all required attributes across ACS instances defined for this SP
		serviceProvider.attributeConsumingServices.each { acs ->
			acs.requestedAttributes.each { attr ->
				if(attr.isRequired) {
					if(!requiredAttributes.contains(attr.base)) {
						requiredAttributes.add(attr.base)
						if( !identityProvider.attributes.find {it.base == attr.base} )
							minimumRequirements = false
						else
							suppliedRequiredAttributes.add(attr.base)
					}
				} else {
					if(!optionalAttributes.contains(attr.base)) {
						optionalAttributes.add(attr.base)
						if( identityProvider.attributes.find {it.base == attr.base} )
							suppliedOptionalAttributes.add(attr.base)
					}
				}
			}
		}
		
		[requiredAttributes:requiredAttributes, optionalAttributes:optionalAttributes, suppliedRequiredAttributes:suppliedRequiredAttributes, suppliedOptionalAttributes:suppliedOptionalAttributes, identityProvider:identityProvider, minimumRequirements:minimumRequirements]
		
	}
	
}