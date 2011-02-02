package fedreg.core

import grails.plugin.spock.*

import fedreg.core.*
import fedreg.workflow.*
import grails.plugins.nimble.core.*
import com.icegreen.greenmail.util.*

class BootstrapControllerSpec extends IntegrationSpec {	
	def controller
	def savedMetaClasses
	def IDPSSODescriptorService
	def SPSSODescriptorService
	def organizationService
	
	def cleanup() {
		SpecHelpers.resetMetaClasses(savedMetaClasses)
		IDPSSODescriptorService.metaClass = fedreg.core.IDPSSODescriptorService.metaClass
		SPSSODescriptorService.metaClass = fedreg.core.SPSSODescriptorService.metaClass
		organizationService.metaClass = fedreg.core.OrganizationService.metaClass
	}
	
	def setup () {
		savedMetaClasses = [:]
		
		SpecHelpers.registerMetaClass(fedreg.core.IDPSSODescriptorService, savedMetaClasses)
		SpecHelpers.registerMetaClass(fedreg.core.SPSSODescriptorService, savedMetaClasses)
		SpecHelpers.registerMetaClass(fedreg.core.OrganizationService, savedMetaClasses)
		this.IDPSSODescriptorService.metaClass = fedreg.core.IDPSSODescriptorService.metaClass
		this.SPSSODescriptorService.metaClass = fedreg.core.SPSSODescriptorService.metaClass
		this.organizationService.metaClass = fedreg.core.OrganizationService.metaClass
		
		controller = new BootstrapController(IDPSSODescriptorService:IDPSSODescriptorService, SPSSODescriptorService:SPSSODescriptorService, organizationService:organizationService)
		def user = UserBase.build()
		SpecHelpers.setupShiroEnv(user)
	}
	
	def "Validate IDP start register"() {
		setup:
		(1..10).each {
			Organization.build(active:true, approved:true)
		}
		
		(1..11).each { i ->
			AttributeBase.build(name: "attr$i")
		}
		
		(1..12).each { i ->
			SamlURI.build(type:SamlURIType.NameIdentifierFormat)
		}
		
		when:
		def model = controller.idp()
		
		then:
		model.identityProvider != null
		model.identityProvider instanceof IDPSSODescriptor
	}
	
	def "Validate successful IDP registration"() {
		setup:
		def organization = Organization.build()
		def entityDescriptor = EntityDescriptor.build(organization:organization)
		def identityProvider = IDPSSODescriptor.build(entityDescriptor:entityDescriptor)
		def attributeAuthority = AttributeAuthorityDescriptor.build(entityDescriptor:entityDescriptor)
		def httpPost = SingleSignOnService.build(descriptor:identityProvider)
		def httpRedirect = SingleSignOnService.build(descriptor:identityProvider)
		def soapArtifact = SingleSignOnService.build(descriptor:identityProvider)
		def contact = Contact.build()
		
		def ret = [:]
		ret.organization = organization
		ret.entityDescriptor = entityDescriptor
		ret.identityProvider = identityProvider
		ret.attributeAuthority = attributeAuthority
		ret.hostname = "test.host.com"
		ret.scope = "host.com"
		ret.httpPost = httpPost
		ret.httpRedirect = httpRedirect
		ret.soapArtifact = soapArtifact
		ret.contact = contact
		
		when:
		IDPSSODescriptorService.metaClass.create = { def p -> 
			return [true, ret]
		} 
		def model = controller.saveidp()
		
		then:
		controller.response.redirectedUrl == "/bootstrap/idpregistered/${identityProvider.id}"
	}
	
	def "Validate failed IDP registration"() {
		setup:
		def organization = Organization.build()
		def entityDescriptor = EntityDescriptor.build(organization:organization)
		def identityProvider = IDPSSODescriptor.build(entityDescriptor:entityDescriptor)
		def attributeAuthority = AttributeAuthorityDescriptor.build(entityDescriptor:entityDescriptor)
		def httpPost = SingleSignOnService.build(descriptor:identityProvider)
		def httpRedirect = SingleSignOnService.build(descriptor:identityProvider)
		def soapArtifact = SingleSignOnService.build(descriptor:identityProvider)
		def contact = Contact.build()
		
		def ret = [:]
		ret.organization = organization
		ret.entityDescriptor = entityDescriptor
		ret.identityProvider = identityProvider
		ret.attributeAuthority = attributeAuthority
		ret.hostname = "test.host.com"
		ret.scope = "host.com"
		ret.httpPost = httpPost
		ret.httpRedirect = httpRedirect
		ret.soapArtifact = soapArtifact
		ret.contact = contact
		
		when:
		IDPSSODescriptorService.metaClass.create = { def p -> 
			return [false, ret]
		} 
		def model = controller.saveidp()
		
		then:
		controller.flash.type == "error"
		controller.flash.message == "fedreg.core.idpssoroledescriptor.register.validation.error"
	}
	
	def "Validate IDP Registered with no supplied ID"() {
		when:
		def model = controller.idpregistered()
		
		then:
		controller.flash.type == "error"
		controller.flash.message == "fedreg.controllers.namevalue.missing"
		controller.response.redirectedUrl == "/"
	}
	
	def "Validate IDP Registered with invalid supplied ID"() {
		setup:
		controller.params.id = -1
		
		when:
		def model = controller.idpregistered()
		
		then:
		controller.flash.type == "error"
		controller.flash.message == "fedreg.core.idpssoroledescriptor.nonexistant"
		controller.response.redirectedUrl == "/"
	}
	
	def "Validate SP register"() {
		setup:
		(1..10).each {
			Organization.build(active:true, approved:true)
		}
		
		(1..11).each { i ->
			AttributeBase.build()
		}
		
		(1..12).each { i ->
			SamlURI.build(type:SamlURIType.NameIdentifierFormat)
		}
		
		when:
		def model = controller.sp()
		
		then:
		model.serviceProvider != null
		model.serviceProvider instanceof SPSSODescriptor
	}
	
	def "Validate successful SP registration"() {
		setup:
		def organization = Organization.build(active:true, approved:true)
		def entityDescriptor = EntityDescriptor.build(organization:organization)
		def serviceProvider = SPSSODescriptor.build(entityDescriptor:entityDescriptor)
		def contact = Contact.build()
		
		def ret = [:]
		ret.organization = organization
		ret.entityDescriptor = entityDescriptor
		ret.serviceProvider = serviceProvider
		
		when:
		SPSSODescriptorService.metaClass.create = { def p -> 
			return [true, ret]
		} 
		def model = controller.savesp()
		
		then:
		controller.response.redirectedUrl == "/bootstrap/spregistered/${serviceProvider.id}"	
	}
	
	def "Validate failed SP registration"() {
		setup:
		def organization = Organization.build(active:true, approved:true)
		def entityDescriptor = EntityDescriptor.build(organization:organization)
		def serviceProvider = SPSSODescriptor.build(entityDescriptor:entityDescriptor)
		
		def ret = [:]
		ret.organization = organization
		ret.entityDescriptor = entityDescriptor
		ret.serviceProvider = serviceProvider
		SPSSODescriptorService.metaClass.create = { def p -> 
			return [false, ret]	//.. deliberately leaving out other return vals here
		}
		
		when:
		def model = controller.savesp()
		
		then:
		controller.flash.type == "error"
		controller.flash.message == "fedreg.core.spssoroledescriptor.register.validation.error"	
	}
	
	def "Validate SP Registered with no supplied ID"() {
		when:
		def model = controller.spregistered()
		
		then:
		controller.flash.type == "error"
		controller.flash.message == "fedreg.controllers.namevalue.missing"
		controller.response.redirectedUrl == "/"
	}
	
	def "Validate SP Registered with invalid supplied ID"() {		
		setup:
		controller.params.id = -1
		
		when:
		def model = controller.spregistered()
		
		then:
		controller.flash.type == "error"
		controller.flash.message == "fedreg.core.spssoroledescriptor.nonexistant"
		controller.response.redirectedUrl == "/"
	}
	
	def "Validate Organization register"() {
		setup:
		(1..10).each {
			OrganizationType.build()
		}
		
		when:
		def model = controller.organization()
		
		then:
		model.organization != null
		model.organization instanceof Organization
	}
	
	def "Validate successful organization register"() {
		setup:
		def organization = Organization.build(active:true, approved:true)
		def contact = Contact.build()
		
		when:
		OrganizationService.metaClass.create = { def p -> 
			return [true, organization, contact]
		} 
		def model = controller.saveorganization()
		
		then:
		controller.response.redirectedUrl == "/bootstrap/organizationregistered/${organization.id}"	
	}
	
	def "Validate failed organization register"() {
		setup:
		def organization = Organization.build(active:true, approved:true)
		
		when:
		OrganizationService.metaClass.create = { def p -> 
			return [false, organization]
		} 
		def model = controller.saveorganization()
		
		then:
		controller.flash.type == "error"
		controller.flash.message == "fedreg.core.organization.register.validation.error"	
	}
	
	def "Validate Organization Registered with no supplied ID"() {		
		when:
		def model = controller.organizationregistered()
		
		then:
		controller.flash.type == "error"
		controller.flash.message == "fedreg.controllers.namevalue.missing"
		controller.response.redirectedUrl == "/"
	}
	
	def "Validate Organization Registered with invalid supplied ID"() {		
		setup:
		controller.params.id = -1
		
		when:
		def model = controller.organizationregistered()
		
		then:
		controller.flash.type == "error"
		controller.flash.message == "fedreg.core.organization.nonexistant"
		controller.response.redirectedUrl == "/"
	}

}