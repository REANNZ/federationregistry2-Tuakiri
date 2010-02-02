package fedreg.host

import fedreg.core.IDPSSODescriptor
import fedreg.core.SPSSODescriptor

class AttributeReleaseController {
	
	def index = {
		
		def c = IDPSSODescriptor.createCriteria()
		def aIDP = c.list {
			entityDescriptor {
				eq("active", true)
			}
		}
		
		c = SPSSODescriptor.createCriteria()
		def aSP = c.list {
			entityDescriptor {
				eq("active", true)
			}
		}
		
		[activeIDP:aIDP, activeSP:aSP]
		
	}
	
}