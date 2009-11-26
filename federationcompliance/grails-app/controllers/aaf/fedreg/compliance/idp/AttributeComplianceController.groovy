package aaf.fedreg.compliance.idp

import aaf.fedreg.core.IdentityProvider

class AttributeComplianceController {
	
	def list = {
		params.max = Math.min(params.max ? params.max.toInteger() : 10, 100)
        [idpInstanceList: IdentityProvider.list(params), idpInstanceTotal: IdentityProvider.count()]
	}
	
}