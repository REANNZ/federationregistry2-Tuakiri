package fedreg.compliance

import fedreg.core.AttributeBase
import fedreg.core.AttributeCategory
import fedreg.core.IDPSSODescriptor
import fedreg.compliance.CategorySupportStatus

class IDPSSODescriptorAttributeComplianceController {
	
	static defaultAction = "summary"
	
	def summary = {
		def identityProviderList = IDPSSODescriptor.list()
		
		def categorySupportSummaries = []
		identityProviderList.each { idp ->			
			def categories = AttributeCategory.listOrderByName()
			categories.each {
				def total = AttributeBase.countByCategory(it)
				def supported = idp.attributes.findAll{a ->	a.base.category == it }
				def summary = new CategorySupportStatus(totalCount:total, supportedCount:supported.size(), name:it.name, idp: idp)
				categorySupportSummaries.add(summary)
			}
		}
		
		[identityProviderList:identityProviderList, categorySupportSummaries:categorySupportSummaries]
	}
	
	def comprehensive = {
		if(!params.id) {
			log.warn "IDPSSODescriptor ID was not present"
			flash.type="error"
			flash.message = message(code: 'fedreg.controllers.namevalue.missing')
			redirect action:'summary'
			return
		}
		
		def identityProvider = IDPSSODescriptor.get(params.id)
        if (!identityProvider) {
			flash.type="error"
            flash.message = message(code: 'fedreg.compliance.idpssoroledescriptor.nonexistant')
            redirect(action: "summary")
			return
        }

		def categorySupport = []
		def categories = AttributeCategory.list()
		categories.each {
			def total = AttributeBase.countByCategory(it)
			def supported = identityProvider.attributes.findAll{att -> att.base.category == it }
			def currentStatus = new CategorySupportStatus(totalCount:total, supportedCount:supported.size(), available:AttributeBase.findAllByCategory(it), supported:supported, name:it.name)
			categorySupport.add(currentStatus)
		}
		
        [identityProvider:identityProvider, categorySupport: categorySupport]
	}
	
	def federationwide = {
		if(!params.id) {
			log.warn "AttributeBase ID was not present"
			flash.type="error"
			flash.message = message(code: 'fedreg.controllers.namevalue.missing')
			redirect action:'summary'
			return
		}
		
		def attribute = AttributeBase.get(params.id)
		if (!attribute) {
			flash.type="error"
            flash.message = message(code: 'fedreg.compliance.attributebase.nonexistant')
            redirect(action: "summary")
			return
        }

		def identityProviderList = IDPSSODescriptor.list()
		def supportingIdentityProviderList = [] as List
		identityProviderList.each{idp -> 
			if(idp.attributes.findAll{att -> att.base == attribute }.size() > 0)
				supportingIdentityProviderList.add(idp)
		}
		
		[identityProviderList:identityProviderList, supportingIdentityProviderList: supportingIdentityProviderList, attribute: attribute]
	}
	
}