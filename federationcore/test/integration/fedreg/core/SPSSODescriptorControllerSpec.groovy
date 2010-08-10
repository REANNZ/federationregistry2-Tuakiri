package fedreg.core

import grails.plugin.spock.*

import fedreg.core.*
import fedreg.workflow.*
import grails.plugins.nimble.core.*

class SPSSODescriptorControllerSpec extends IntegrationSpec {
	
	def controller
	def savedMetaClasses
	def spssoDescriptorService = new SPSSODescriptorService()
	
	def cleanup() {
		SpecHelpers.resetMetaClasses(savedMetaClasses)
		spssoDescriptorService.metaClass = SPSSODescriptorService.metaClass
	}
	
	def setup () {
		savedMetaClasses = [:]
		
		SpecHelpers.registerMetaClass(SPSSODescriptorService, savedMetaClasses)
		spssoDescriptorService.metaClass = SPSSODescriptorService.metaClass
		
		controller = new SPSSODescriptorController(SPSSODescriptorService:spssoDescriptorService)
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
			def ed = EntityDescriptor.build(entityID:"http://sp.test.com/$i")
			def sp = SPSSODescriptor.build(entityDescriptor: ed)
			sp.save()
		}
		
		when:
		def model = controller.list()

		then:
		model.serviceProviderList.size() == 20
	}
	
	def "Validate list with max set"() {
		setup:
		(1..25).each { i ->
			def ed = EntityDescriptor.build(entityID:"http://sp.test.com/$i")
			def sp = SPSSODescriptor.build(entityDescriptor: ed)
			sp.save()
		}
		controller.params.max = 10
		
		when:
		def model = controller.list()

		then:
		model.serviceProviderList.size() == 10
		model.serviceProviderList.get(0) == SPSSODescriptor.list().get(0)
		model.serviceProviderList.get(9) == SPSSODescriptor.list().get(9)
	}
	
	def "Validate list with max and offset set"() {
		setup:
		(1..25).each { i->
			def ed = EntityDescriptor.build(entityID:"http://sp.test.com/$i")
			def sp = SPSSODescriptor.build(entityDescriptor: ed)
			sp.save()
		}
		controller.params.max = 10
		controller.params.offset = 5
		
		when:
		def model = controller.list()

		then:
		model.serviceProviderList.size() == 10
		model.serviceProviderList.get(0) == SPSSODescriptor.list().get(5)
		model.serviceProviderList.get(9) == SPSSODescriptor.list().get(14)
	}
	
	def "Show with no ID"() {		
		when:
		controller.show()
		
		then:
		controller.flash.type == "error"
		controller.flash.message == "fedreg.controllers.namevalue.missing"
		controller.response.redirectedUrl == "/SPSSODescriptor/list"
	}
	
	def "Show with invalid SPSSODescriptor ID"() {
		setup:
		controller.params.id = 2
			
		when:
		controller.show()
		
		then:
		controller.flash.type == "error"
		controller.flash.message == "fedreg.core.spssoroledescriptor.nonexistant"
		controller.response.redirectedUrl == "/SPSSODescriptor/list"
	}
	
	def "Validate create"() {
		setup:
		(1..10).each {
			Organization.build().save()
		}
		
		(1..11).each { i ->
			AttributeBase.build(name: "attr$i").save()
		}
		
		(1..12).each { i ->
			SamlURI.build(type:SamlURIType.NameIdentifierFormat).save()
		}
		
		when:
		def model = controller.create()

		then:
		model.serviceProvider != null
		model.serviceProvider instanceof SPSSODescriptor
		model.organizationList.size() == 10
		model.attributeList.size() == 11
		model.nameIDFormatList.size() == 12
	}
	
	def "Validate successful save"() {
		setup:
		def params = [:]
		def organization = Organization.build().save()
		def entityDescriptor = EntityDescriptor.build(organization:organization).save()
		def serviceProvider = SPSSODescriptor.build(entityDescriptor:entityDescriptor).save()
		
		when:
		spssoDescriptorService.metaClass.create = { def p -> 
			return [true, organization, entityDescriptor, serviceProvider]	//.. deliberately leaving out other return vals here
		} 
		def model = controller.save()
		
		then:
		controller.response.redirectedUrl == "/SPSSODescriptor/show/${serviceProvider.id}"	
	}
	
	def "Validate failed save"() {
		setup:
		def params = [:]
		def organization = Organization.build().save()
		def entityDescriptor = EntityDescriptor.build(organization:organization).save()
		def serviceProvider = SPSSODescriptor.build(entityDescriptor:entityDescriptor).save()
		
		when:
		spssoDescriptorService.metaClass.create = { def p -> 
			return [true, organization, entityDescriptor, serviceProvider]	//.. deliberately leaving out other return vals here
		} 
		def model = controller.save()
		
		then:
		//organization == controller.modelAndView.model.organization	
		//entityDescriptor == controller.modelAndView.model.entityDescriptor	
		//serviceProvider == controller.modelAndView.model.serviceProvider
		
		controller.flash.type = "error"
		controller.flash.message = "fedreg.core.spssoroledescriptor.save.validation.error"	
	}
}