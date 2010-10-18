package fedreg.core

import grails.plugin.spock.*

import fedreg.core.*
import fedreg.workflow.*
import grails.plugins.nimble.core.*
import com.icegreen.greenmail.util.*

class BootstrapControllerSpec extends IntegrationSpec {
	
	def greenMail
	def controller
	def savedMetaClasses
	def idpssoDescriptorService = new IDPSSODescriptorService()
	def spssoDescriptorService = new SPSSODescriptorService()
	def organizationService = new OrganizationService()
	
	def cleanup() {
		SpecHelpers.resetMetaClasses(savedMetaClasses)
		idpssoDescriptorService.metaClass = IDPSSODescriptorService.metaClass
		spssoDescriptorService.metaClass = SPSSODescriptorService.metaClass
		organizationService.metaClass = OrganizationService.metaClass
		greenMail.deleteAllMessages()
	}
	
	def setup () {
		savedMetaClasses = [:]
		
		SpecHelpers.registerMetaClass(IDPSSODescriptorService, savedMetaClasses)
		SpecHelpers.registerMetaClass(SPSSODescriptorService, savedMetaClasses)
		SpecHelpers.registerMetaClass(OrganizationService, savedMetaClasses)
		idpssoDescriptorService.metaClass = IDPSSODescriptorService.metaClass
		spssoDescriptorService.metaClass = SPSSODescriptorService.metaClass
		organizationService.metaClass = OrganizationService.metaClass
		
		controller = new BootstrapController(IDPSSODescriptorService:idpssoDescriptorService, SPSSODescriptorService:spssoDescriptorService, organizationService:organizationService)
		def user = UserBase.build()
		SpecHelpers.setupShiroEnv(user)
	}
	
	def "Validate IDP start register"() {
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
		def model = controller.idp()
		
		then:
		model.identityProvider != null
		model.identityProvider instanceof IDPSSODescriptor
		model.organizationList.size() == 10
		model.attributeList.size() == 11
		model.nameIDFormatList.size() == 12
	}
	
	def "Validate successful IDP registration"() {
		setup:
		def params = [:]
		def organization = Organization.build().save()
		def entityDescriptor = EntityDescriptor.build(organization:organization).save()
		def identityProvider = IDPSSODescriptor.build(entityDescriptor:entityDescriptor).save()
		def attributeAuthority = AttributeAuthorityDescriptor.build(entityDescriptor:entityDescriptor).save()
		def httpPost = SingleSignOnService.build(descriptor:identityProvider).save()
		def httpRedirect = SingleSignOnService.build(descriptor:identityProvider).save()
		def soapArtifact = SingleSignOnService.build(descriptor:identityProvider).save()
		def organizationList = [organization]
		def attributeList = [Attribute.build().save()]
		def nameIDFormatList = [SamlURI.build().save()]
		def contact = Contact.build().save()
		
		when:
		idpssoDescriptorService.metaClass.create = { def p -> 
			return [true, organization, entityDescriptor, identityProvider, attributeAuthority, httpPost, httpRedirect, soapArtifact, organizationList, attributeList, nameIDFormatList, contact]
		} 
		def model = controller.saveidp()
		
		then:
		controller.response.redirectedUrl == "/bootstrap/idpregistered/${identityProvider.id}"
		greenMail.getReceivedMessages().length == 1
		def message = greenMail.getReceivedMessages()[0]
		message.subject == "fedreg.templates.mail.idpssoroledescriptor.register.subject"
		GreenMailUtil.getBody(message).contains(organization.displayName)
		GreenMailUtil.getBody(message).contains(httpPost.location.uri)
		GreenMailUtil.getBody(message).contains(httpRedirect.location.uri)
		GreenMailUtil.getBody(message).contains(httpRedirect.location.uri)
		GreenMailUtil.getBody(message).contains("fedreg.templates.mail.get.support")
	}
	
	def "Validate failed IDP registration"() {
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
		def model = controller.saveidp()
		
		then:
		controller.flash.type = "error"
		controller.flash.message = "fedreg.core.idpssoroledescriptor.register.validation.error"
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
		controller.params.id = 2
		
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
			Organization.build().save()
		}
		
		(1..11).each { i ->
			AttributeBase.build(name: "attr$i").save()
		}
		
		(1..12).each { i ->
			SamlURI.build(type:SamlURIType.NameIdentifierFormat).save()
		}
		
		when:
		def model = controller.sp()
		
		then:
		model.serviceProvider != null
		model.serviceProvider instanceof SPSSODescriptor
		model.organizationList.size() == 10
		model.attributeList.size() == 11
		model.nameIDFormatList.size() == 12
	}
	
	def "Validate successful SP registration"() {
		setup:
		def organization = Organization.build().save()
		def entityDescriptor = EntityDescriptor.build(organization:organization).save()
		def serviceProvider = SPSSODescriptor.build(entityDescriptor:entityDescriptor).save()
		def contact = Contact.build().save()
		
		when:
		spssoDescriptorService.metaClass.create = { def p -> 
			return [true, organization, entityDescriptor, serviceProvider, null, null, null, null, null, null, null, null, null, contact]
		} 
		def model = controller.savesp()
		
		then:
		controller.response.redirectedUrl == "/bootstrap/spregistered/${serviceProvider.id}"	
		greenMail.getReceivedMessages().length == 1
		def message = greenMail.getReceivedMessages()[0]
		message.subject == "fedreg.templates.mail.spssoroledescriptor.register.subject"
		GreenMailUtil.getBody(message).contains(organization.displayName)
		GreenMailUtil.getBody(message).contains(entityDescriptor.entityID)
		GreenMailUtil.getBody(message).contains(serviceProvider.displayName)
		GreenMailUtil.getBody(message).contains("fedreg.templates.mail.get.support")
	}
	
	def "Validate failed SP registration"() {
		setup:
		def organization = Organization.build().save()
		def entityDescriptor = EntityDescriptor.build(organization:organization).save()
		def serviceProvider = SPSSODescriptor.build(entityDescriptor:entityDescriptor).save()
		
		when:
		spssoDescriptorService.metaClass.create = { def p -> 
			return [false, organization, entityDescriptor, serviceProvider]	//.. deliberately leaving out other return vals here
		} 
		def model = controller.savesp()
		
		then:		
		controller.flash.type = "error"
		controller.flash.message = "fedreg.core.spssoroledescriptor.register.validation.error"	
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
		controller.params.id = 2
		
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
			OrganizationType.build().save()
		}
		
		when:
		def model = controller.organization()
		
		then:
		model.organization != null
		model.organization instanceof Organization
		model.organizationTypes.size() == 10
	}
	
	def "Validate successful organization register"() {
		setup:
		def organization = Organization.build().save()
		def contact = Contact.build().save()
		
		when:
		organizationService.metaClass.create = { def p -> 
			return [true, organization, contact]
		} 
		def model = controller.saveorganization()
		
		then:
		controller.response.redirectedUrl == "/bootstrap/organizationregistered/${organization.id}"	
		greenMail.getReceivedMessages().length == 1
		def message = greenMail.getReceivedMessages()[0]
		message.subject == "fedreg.templates.mail.organization.register.subject"
		GreenMailUtil.getBody(message).contains(organization.displayName)
	}
	
	def "Validate failed  organization register"() {
		setup:
		def organization = Organization.build().save()
		
		when:
		organizationService.metaClass.create = { def p -> 
			return [false, organization]
		} 
		def model = controller.saveorganization()
		
		then:
		controller.flash.type = "error"
		controller.flash.message = "fedreg.core.organization.register.validation.error"	
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
		controller.params.id = 2
		
		when:
		def model = controller.organizationregistered()
		
		then:
		controller.flash.type == "error"
		controller.flash.message == "fedreg.core.organization.nonexistant"
		controller.response.redirectedUrl == "/"
	}

}