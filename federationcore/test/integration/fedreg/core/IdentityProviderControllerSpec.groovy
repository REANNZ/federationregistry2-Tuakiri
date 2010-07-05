package fedreg.core

import grails.plugin.spock.*

import fedreg.core.*

class IdentityProviderControllerSpec extends IntegrationSpec {
	
	def controller
	def cryptoService
	
	def setup () {
		controller = new IdentityProviderController(cryptoService: cryptoService)
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
	
	def "Save succeeds when valid initial IDPSSODescriptor and AttributeAuthorityDescriptor data are provided (without existing EntityDescriptor)"() {
		setup:
		def soap = new SamlURI(type:SamlURIType.ProtocolBinding, uri:'urn:oasis:names:tc:SAML:2.0:bindings:SOAP', description:'').save()
		def httpRedirect = new SamlURI(type:SamlURIType.ProtocolBinding, uri:'urn:oasis:names:tc:SAML:2.0:bindings:HTTP-Redirect', description:'').save()
		def httpPost = new SamlURI(type:SamlURIType.ProtocolBinding, uri:'urn:oasis:names:tc:SAML:2.0:bindings:HTTP-POST', description:'').save()
		def httpArtifact = new SamlURI(type:SamlURIType.ProtocolBinding, uri:'urn:oasis:names:tc:SAML:2.0:bindings:HTTP-Artifact', description:'').save()
		
		def ca = new File('./test/integration/data/demoCA/cacertminimal.pem').text
		def caCert = new CACertificate(data:ca)
		def caKeyInfo = new CAKeyInfo(certificate:caCert)
		caKeyInfo.save()
		
		def organization = Organization.build().save()
		def attr1 = Attribute.build().save()
		def attr2 = Attribute.build().save()
		
		def pk = new File('./test/integration/data/newcertminimal.pem').text

		controller.params.organization = [id: organization.id]
		controller.params.active = true
		controller.params.entity = [identifier:"http://idp.test.com"]
		controller.params.idp = [displayName:"test name", description:"test desc", crypto:[sig: true, sigdata:pk, enc:true, encdata:pk]]
		controller.params.aa = [create: true, attributeservice:[uri:"http://idp.test.com/SAML2/SOAP/AttributeQuery"], attributes:[1, 2]]
		
		when:
		def model = controller.save()
		
		then:
		Organization.count() == 1
		SamlURI.count() == 4
		EntityDescriptor.count() == 1
		IDPSSODescriptor.count() == 1
		AttributeAuthorityDescriptor.count() == 1
		def ed = EntityDescriptor.list().get(0)
		ed.entityID == "http://idp.test.com"
		def idp = IDPSSODescriptor.list().get(0)
		idp.organization == organization
		idp.entityDescriptor == ed
		idp.entityDescriptor.organization == organization
		
		controller.response.redirectedUrl == "/identityProvider/show/${idp.id}"
		idp.displayName = "test name"
		idp.description = "test desc"
		idp.keyDescriptors.size() == 2
	}
	
	/*
	def "Save succeeds when valid IDPSSODescriptor and base SingleSignOn endpoint data presented (without existing EntityDescriptor)"() {
		setup:
		def org = Organization.build().save()
		def binding = SamlURI.build(type: SamlURIType.ProtocolBinding).save()
		controller.params.organization = [id: org.id]
		controller.params.active = true
		controller.params.entity = [identifier:"http://idp.test.com"]
		controller.params.idp = [displayName:"test name", description:"test desc"]
		controller.params.sso = [active:true, binding:binding.id, uri:"http://idp.test.com/SSO/endpoint"]
		controller.params.aa = [uri:"http://idp.test.com/SAML2/SOAP/AttributeQuery", attributes:[1, 2]]
		
		when:
		def model = controller.save()
		
		then:
		Organization.count() == 1
		SamlURI.count() == 1
		EntityDescriptor.count() == 1
		IDPSSODescriptor.count() == 1
		AttributeAuthorityDescriptor.count() == 1
		def ed = EntityDescriptor.list().get(0)
		ed.entityID == "http://idp.test.com"
		def idp = IDPSSODescriptor.list().get(0)
		idp.organization == org
		idp.entityDescriptor == ed
		idp.entityDescriptor.organization == org
		controller.response.redirectedUrl == "/identityProvider/show/${idp.id}"
		idp.displayName = "test name"
		idp.description = "test desc"
		idp.singleSignOnServices.size() == 1
		idp.singleSignOnServices.asList().get(0).location.uri == "http://idp.test.com/SSO/endpoint"
		idp.singleSignOnServices.asList().get(0).binding == binding
	}
	
	def "Save succeeds when valid IDPSSODescriptor and base SingleSignOn endpoint data presented (with existing EntityDescriptor)"() {
		setup:
		def org = Organization.build().save()
		def binding = SamlURI.build(type: SamlURIType.ProtocolBinding).save()
		def ed = EntityDescriptor.build(organization: org).save()
		controller.params.organization = [id: org.id]
		controller.params.active = true
		controller.params.entity = [id:ed.id]
		controller.params.idp = [displayName:"test name", description:"test desc"]
		controller.params.sso = [active:true, binding:binding.id, uri:"http://idp.test.com/SSO/endpoint"]
		
		when:
		def model = controller.save()
		
		then:
		Organization.count() == 1
		SamlURI.count() == 1
		EntityDescriptor.count() == 1
		IDPSSODescriptor.count() == 1
		def idp = IDPSSODescriptor.list().get(0)
		idp.organization == org
		idp.entityDescriptor == ed
		idp.entityDescriptor.organization == org
		controller.response.redirectedUrl == "/identityProvider/show/${idp.id}"
		idp.displayName = "test name"
		idp.description = "test desc"
		idp.singleSignOnServices.size() == 1
		idp.singleSignOnServices.asList().get(0).location.uri == "http://idp.test.com/SSO/endpoint"
		idp.singleSignOnServices.asList().get(0).binding == binding
	}
	
	def "Save fails when IdP constraints not met"() {
		setup:
		def org = Organization.build().save()
		def binding = SamlURI.build(type: SamlURIType.ProtocolBinding).save()
		controller.params.organization = [id: org.id]
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
		controller.modelAndView.model.identityProvider.errors.getErrorCount() == 1
		controller.modelAndView.model.identityProvider.errors.getFieldError('displayName').code == 'nullable'
		controller.flash.type="error"
		controller.flash.message="fedreg.core.idpssoroledescriptor.save.failed"
		controller.modelAndView.model.identityProvider.displayName == null
		controller.modelAndView.model.identityProvider.description == "test desc"
		controller.modelAndView.model.identityProvider.singleSignOnServices.size() == 1
		controller.modelAndView.model.organization != null
		controller.modelAndView.model.entityDescriptor != null
	}
	
	def "Save fails when SSO endpoint constraints not met"() {
		setup:
		def org = Organization.build().save()
		controller.params.organization = [id: org.id]
		controller.params.active = true
		controller.params.entity = [identifier:"http://idp.test.com"]
		controller.params.idp = [displayName: "test", description:"test desc"]
		controller.params.sso = [active:true, uri:"http://idp.test.com/SSO/endpoint"]
		
		when:
		def model = controller.save()
		
		then:
		Organization.count() == 1
		EntityDescriptor.count() == 0
		IDPSSODescriptor.count() == 0
		controller.modelAndView.model.identityProvider.singleSignOnServices.size() == 1
		controller.modelAndView.model.identityProvider.singleSignOnServices.toList().get(0).errors.getErrorCount() == 1
		controller.modelAndView.model.identityProvider.singleSignOnServices.toList().get(0).errors.getFieldError('binding').code == 'nullable'
		controller.flash.type="error"
		controller.flash.message="fedreg.core.idpssoroledescriptor.save.failed"
		controller.modelAndView.model.identityProvider.displayName == "test"
		controller.modelAndView.model.identityProvider.description == "test desc"
	}
	
	def "Save fails when Organization not supplied"() {
		setup:
		def binding = SamlURI.build(type: SamlURIType.ProtocolBinding).save()
		controller.params.active = true
		controller.params.entity = [identifier:"http://idp.test.com"]
		controller.params.idp = [displayName: "test", description:"test desc"]
		controller.params.sso = [active:true, binding:binding.id, uri:"http://idp.test.com/SSO/endpoint"]
		
		when:
		def model = controller.save()
		
		then:
		Organization.count() == 0
		SamlURI.count() == 1
		EntityDescriptor.count() == 0
		IDPSSODescriptor.count() == 0
		controller.modelAndView.model.identityProvider.errors.getErrorCount() == 0
		controller.flash.type="error"
		controller.flash.message="fedreg.core.idpssodescriptor.save.no.organization"
		controller.modelAndView.model.identityProvider.displayName == "test"
		controller.modelAndView.model.identityProvider.description == "test desc"
		controller.modelAndView.model.identityProvider.singleSignOnServices.size() == 1
	}
	
	def "Save fails when Entity not supplied"() {
		setup:
		def org = Organization.build().save()
		def binding = SamlURI.build(type: SamlURIType.ProtocolBinding).save()
		controller.params.organization = [id: org.id]
		controller.params.active = true
		controller.params.idp = [displayName: "test", description:"test desc"]
		controller.params.sso = [active:true, binding:binding.id, uri:"http://idp.test.com/SSO/endpoint"]
		
		when:
		def model = controller.save()
		
		then:
		Organization.count() == 1
		SamlURI.count() == 1
		EntityDescriptor.count() == 0
		IDPSSODescriptor.count() == 0
		controller.modelAndView.model.identityProvider.errors.getErrorCount() == 0
		controller.flash.type="error"
		controller.flash.message="fedreg.core.idpssodescriptor.save.no.entitydescriptor"
		controller.modelAndView.model.identityProvider.displayName == "test"
		controller.modelAndView.model.identityProvider.description == "test desc"
		controller.modelAndView.model.identityProvider.singleSignOnServices.size() == 1
	}
	*/
}