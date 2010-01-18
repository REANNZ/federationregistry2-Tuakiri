package fedreg.compliance.idp

import aaf.fedreg.core.Attribute
import aaf.fedreg.core.AttributeCategory
import aaf.fedreg.core.IdentityProvider
import aaf.fedreg.compliance.CategorySupportStatus

class AttributeComplianceController {
	def index = {
		redirect action:summary
	}
	
	def summary = {
		def idpInstanceList = IdentityProvider.list()
		
		def categorySupportSummaries = []
		idpInstanceList.each { idp ->			
			def categories = AttributeCategory.listOrderByName()
			categories.each {
				def total = Attribute.countByCategory(it)
				def supported = idp.attributes.findAll{a ->	a.category == it }
				def summary = new CategorySupportStatus(totalCount:total, supportedCount:supported.size(), name:it.name, idp: idp)
				categorySupportSummaries.add(summary)
			}
		}
		
		[idpInstanceList:idpInstanceList, categorySupportSummaries:categorySupportSummaries]
	}
	
	def identityprovider = {
		def idp = IdentityProvider.get(params.id)
        if (!idp) {
			flash.type="error"
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'identityProvider.label'), params.id])}"
            redirect(action: "list")
			return
        }

		def categorySupport = []
		def categories = AttributeCategory.list()
		categories.each {
			def total = Attribute.countByCategory(it)
			def supported = idp.attributes.findAll{a ->	a.category == it }
			def currentStatus = new CategorySupportStatus(totalCount:total, supportedCount:supported.size(), available:Attribute.findAllByCategory(it), supported:supported, name:it.name)
			categorySupport.add(currentStatus)
		}
        [idp:idp, categorySupport: categorySupport]
	}
	
	def attribute = {
		def attribute = Attribute.get(params.id)
		def idpInstanceList = IdentityProvider.list()
		def supportingIdpInstanceList = idpInstanceList.findAll{idp -> attribute in idp.attributes}
		[idpInstanceList:idpInstanceList, supportingIdpInstanceList: supportingIdpInstanceList, attribute: attribute]
	}
	
}