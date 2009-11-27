package aaf.fedreg.compliance.idp

import aaf.fedreg.core.Attribute
import aaf.fedreg.core.AttributeCategory
import aaf.fedreg.core.IdentityProvider
import aaf.fedreg.compliance.CategorySupportStatus

class AttributeComplianceController {
	
	def list = {
		params.max = Math.min(params.max ? params.max.toInteger() : 10, 100)
        [idpInstanceList: IdentityProvider.list(params), idpInstanceTotal: IdentityProvider.count()]
	}
	
	def show = {
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
	
}