package fedreg.core

import grails.plugin.spock.*

import fedreg.core.*

class IdentityProviderControllerSpec extends IntegrationSpec {
	
	def controller
	
	def setup () {
		controller = new IdentityProviderController()
	}
	
	def "Validate list"() {
		setup:
		(1..25).each {
			def idp = IDPSSODescriptor.build()
			idp.save()
		}
		
		when:
		def model = controller.list()

		then:
		model.identityProviderList.size() == 25
	}
	
	def "Validate list with max set"() {
		setup:
		(1..25).each {
			def idp = IDPSSODescriptor.build()
			idp.save()
		}
		controller.params.max = 10
		
		when:
		def model = controller.list()

		then:
		model.identityProviderList.size() == 10
		model.identityProviderList.get(0) == IDPSSODescriptor.list().get(0)
		model.identityProviderList.get(9) == IDPSSODescriptor.list().get(9)
	}
	
	def "Validate list with max and offset set"() {
		setup:
		(1..25).each {
			def idp = IDPSSODescriptor.build()
			idp.save()
		}
		controller.params.max = 10
		controller.params.offset = 5
		
		when:
		def model = controller.list()

		then:
		model.identityProviderList.size() == 10
		model.identityProviderList.get(0) == IDPSSODescriptor.list().get(5)
		model.identityProviderList.get(9) == IDPSSODescriptor.list().get(14)
	}
	
	def "Show with no ID"() {		
		when:
		controller.show()
		
		then:
		controller.flash.type == "error"
		controller.flash.message == "fedreg.controllers.namevalue.missing"
		controller.response.redirectedUrl == "/identityProvider/list"
	}
	
	def "Show with invalid IDPSSODescriptor ID"() {
		setup:
		controller.params.id = 2
			
		when:
		controller.show()
		
		then:
		controller.flash.type == "error"
		controller.flash.message == "fedreg.core.idpssoroledescriptor.nonexistant"
		controller.response.redirectedUrl == "/identityProvider/list"
	}
	
	def "Validate create"() {
		when:
		def model = controller.create()

		then:
		model.identityProvider != null
		model.identityProvider instanceof IDPSSODescriptor
	}
	
	def "Save succeeds when valid IDPSSODescriptor and base SingleSignOn endpoint data presented"() {
		setup:
		def org = Organization.build().save()
		def binding = SamlURI.build(type: SamlURIType.ProtocolBinding).save()
		controller.params.organization = org.id
		controller.params.active = true
		controller.params.entity = [identifier:"http://idp.test.com"]
		controller.params.idp = [displayName:"test name", description:"test desc"]
		controller.params.sso = [active:true, binding:binding.id, uri:"http://idp.test.com/SSO/endpoint"]
		
		when:
		def model = controller.save()
		
		then:
		Organization.count() == 1
		SamlURI.count() == 1
		EntityDescriptor.count() == 1
		IDPSSODescriptor.count() == 1
		controller.response.redirectedUrl == "/identityProvider/show/${IDPSSODescriptor.list().get(0).id}"
		def ed = EntityDescriptor.list().get(0)
		ed.entityID == "http://idp.test.com"
		def idp = IDPSSODescriptor.list().get(0)
		idp.displayName = "test name"
		idp.description = "test desc"
		idp.singleSignOnServices.size() == 1
		idp.singleSignOnServices.asList().get(0).location.uri == "http://idp.test.com/SSO/endpoint"
		idp.singleSignOnServices.asList().get(0).binding == binding
	}
	
	def "Save fails when missing idp displayName"() {
		setup:
		def org = Organization.build().save()
		def binding = SamlURI.build(type: SamlURIType.ProtocolBinding).save()
		controller.params.organization = org.id
		controller.params.active = true
		controller.params.entity = [identifier:"http://idp.test.com"]
		controller.params.idp = [description:"test desc"]
		controller.params.sso = [active:true, binding:binding.id, uri:"http://idp.test.com/SSO/endpoint"]
		
		when:
		def model = controller.save()
		
		then:
		Organization.count() == 1
		SamlURI.count() == 1
		EntityDescriptor.count() == 0
		IDPSSODescriptor.count() == 0
		controller.flash.type="error"
		controller.flash.message="fedreg.core.idpssoroledescriptor.save.failed"
		controller.modelAndView.model.identityProvider.displayName == null
		controller.modelAndView.model.identityProvider.description == "test desc"
	}
	
}