package fedreg.core

import grails.plugin.spock.*

import fedreg.core.*
import fedreg.workflow.*
import grails.plugins.nimble.core.*

class IDPSSODescriptorControllerSpec extends IntegrationSpec {
	
	def controller
	def savedMetaClasses
	def idpssoDescriptorService = new IDPSSODescriptorService()
	
	def cleanup() {
		SpecHelpers.resetMetaClasses(savedMetaClasses)
		idpssoDescriptorService.metaClass = IDPSSODescriptorService.metaClass
	}
	
	def setup () {
		savedMetaClasses = [:]
		
		SpecHelpers.registerMetaClass(IDPSSODescriptorService, savedMetaClasses)
		idpssoDescriptorService.metaClass = IDPSSODescriptorService.metaClass
		
		controller = new IDPSSODescriptorController(IDPSSODescriptorService:idpssoDescriptorService)
		def user = UserBase.build()
		SpecHelpers.setupShiroEnv(user)
	}
	
	def setupBindings() {
		def soap = new SamlURI(type:SamlURIType.ProtocolBinding, uri:'urn:oasis:names:tc:SAML:2.0:bindings:SOAP', description:'').save()
		def httpRedirect = new SamlURI(type:SamlURIType.ProtocolBinding, uri:'urn:oasis:names:tc:SAML:2.0:bindings:HTTP-Redirect', description:'').save()
		def httpPost = new SamlURI(type:SamlURIType.ProtocolBinding, uri:'urn:oasis:names:tc:SAML:2.0:bindings:HTTP-POST', description:'').save()
		def httpArtifact = new SamlURI(type:SamlURIType.ProtocolBinding, uri:'urn:oasis:names:tc:SAML:2.0:bindings:HTTP-Artifact', description:'').save()
	}
	
	def setupCrypto() {
		def ca = new File('./test/integration/data/demoCA/cacertminimal.pem').text
		def caCert = new CACertificate(data:ca)
		def caKeyInfo = new CAKeyInfo(certificate:caCert)
		caKeyInfo.save()
	}
	
	def String loadPK() {
		new File('./test/integration/data/newcertminimal.pem').text
	}
	
	def "Validate list"() {
		setup:
		setupCrypto()
		def pk = loadPK()
		
		(1..25).each { i ->
			def ed = EntityDescriptor.build(entityID:"http://idp.test.com/$i")
			def idp = IDPSSODescriptor.build(entityDescriptor: ed)
			idp.save()
		}
		
		when:
		def model = controller.list()

		then:
		model.identityProviderList.size() == 20
	}
	
	def "Validate list with max set"() {
		setup:
		(1..25).each { i ->
			def ed = EntityDescriptor.build(entityID:"http://idp.test.com/$i")
			def idp = IDPSSODescriptor.build(entityDescriptor: ed)
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
		(1..25).each { i->
			def ed = EntityDescriptor.build(entityID:"http://idp.test.com/$i")
			def idp = IDPSSODescriptor.build(entityDescriptor: ed)
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
		controller.response.redirectedUrl == "/IDPSSODescriptor/list"
	}
	
	def "Show with invalid IDPSSODescriptor ID"() {
		setup:
		controller.params.id = 2
			
		when:
		controller.show()
		
		then:
		controller.flash.type == "error"
		controller.flash.message == "fedreg.core.idpssoroledescriptor.nonexistant"
		controller.response.redirectedUrl == "/IDPSSODescriptor/list"
	}
	
	def "Validate create"() {
		setup:
		(1..10).each {
			Organization.build().save()
		}
		
		(1..11).each { i ->
			Attribute.build(name: "attr$i").save()
		}
		
		(1..12).each { i ->
			SamlURI.build(type:SamlURIType.NameIdentifierFormat).save()
		}
		
		when:
		def model = controller.create()

		then:
		model.identityProvider != null
		model.identityProvider instanceof IDPSSODescriptor
		model.organizationList.size() == 10
		model.attributeList.size() == 11
		model.nameIDFormatList.size() == 12
	}
	
	def "Validate successful save"() {
		setup:
		def params = [:]
		def organization = Organization.build().save()
		def entityDescriptor = EntityDescriptor.build(organization:organization).save()
		def identityProvider = IDPSSODescriptor.build(entityDescriptor:entityDescriptor).save()
		def attributeAuthority = AttributeAuthorityDescriptor.build(entityDescriptor:entityDescriptor).save()
		def httpPost = SamlURI.build().save()
		def httpRedirect = SamlURI.build().save()
		def soapArtifact = SamlURI.build().save()
		def organizationList = [organization]
		def attributeList = [Attribute.build().save()]
		def nameIDFormatList = [SamlURI.build().save()]
		def contact = Contact.build().save()
		
		when:
		idpssoDescriptorService.metaClass.create = { def p -> 
			return [true, organization, entityDescriptor, identityProvider, attributeAuthority, httpPost, httpRedirect, soapArtifact, organizationList, attributeList, nameIDFormatList, contact]
		} 
		def model = controller.save()
		
		then:
		controller.response.redirectedUrl == "/IDPSSODescriptor/show/${identityProvider.id}"	
	}
	
	def "Validate failed save"() {
		setup:
		def params = [:]
		def organization = Organization.build().save()
		def entityDescriptor = EntityDescriptor.build(organization:organization).save()
		def identityProvider = IDPSSODescriptor.build(entityDescriptor:entityDescriptor).save()
		def attributeAuthority = AttributeAuthorityDescriptor.build(entityDescriptor:entityDescriptor).save()
		def httpPost = SamlURI.build().save()
		def httpRedirect = SamlURI.build().save()
		def soapArtifact = SamlURI.build().save()
		def organizationList = [organization]
		def attributeList = [Attribute.build().save()]
		def nameIDFormatList = [SamlURI.build().save()]
		def contact = Contact.build().save()
		
		when:
		idpssoDescriptorService.metaClass.create = { def p -> 
			return [false, organization, entityDescriptor, identityProvider, attributeAuthority, httpPost, httpRedirect, soapArtifact, organizationList, attributeList, nameIDFormatList, contact]
		} 
		def model = controller.save()
		
		then:
		organization == controller.modelAndView.model.organization	
		entityDescriptor == controller.modelAndView.model.entityDescriptor	
		identityProvider == controller.modelAndView.model.identityProvider
		attributeAuthority == controller.modelAndView.model.attributeAuthority
		
		controller.flash.type = "error"
		controller.flash.message = "fedreg.core.idpssoroledescriptor.save.validation.error"	
	}
}