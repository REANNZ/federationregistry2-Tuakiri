package fedreg.compliance

import grails.plugin.spock.*

import fedreg.core.*
import grails.plugins.nimble.core.*

class IDPSSODescriptorAttributeComplianceControllerSpec extends IntegrationSpec {

	def controller
	def savedMetaClasses
	
	def cleanup() {
	}
	
	def setup () {
		savedMetaClasses = [:]
		
		controller = new IDPSSODescriptorAttributeComplianceController()
		
		def user = UserBase.build()
		SpecHelpers.setupShiroEnv(user)
	}