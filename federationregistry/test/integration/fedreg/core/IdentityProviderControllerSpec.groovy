package fedreg.core

import grails.plugin.spock.*

import fedreg.core.*

class IdentityProviderControllerSpec extends ControllerSpec {
	
	def "Validate create"() {
		when:
		def model = controller.create()

		then:
		model.identityProvider != null
		model.identityProvider instanceof IDPSSODescriptor
	}
	
}