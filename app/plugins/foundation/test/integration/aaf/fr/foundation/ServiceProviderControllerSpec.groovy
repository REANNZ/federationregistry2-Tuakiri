package aaf.fr.foundation

import grails.plugin.spock.*
import aaf.fr.workflow.*
import aaf.fr.identity.Subject

class ServiceProviderControllerSpec extends IntegrationSpec {
	
    def user, spssoDescriptorService, controller
	
	def cleanup() {
       user.permissions = []
	}
	
	def setup () {
        spssoDescriptorService = new ServiceProviderService()
        controller = new ServiceProviderController(ServiceProviderService:spssoDescriptorService)

		user = Subject.build()
		SpecHelpers.setupShiroEnv(user)
	}
	
	def setupBindings() {
		def soap = new SamlURI(type:SamlURIType.ProtocolBinding, uri:'urn:oasis:names:tc:SAML:2.0:bindings:SOAP', description:'').save()
		def httpRedirect = new SamlURI(type:SamlURIType.ProtocolBinding, uri:'urn:oasis:names:tc:SAML:2.0:bindings:HTTP-Redirect', description:'').save()
		def httpPost = new SamlURI(type:SamlURIType.ProtocolBinding, uri:'urn:oasis:names:tc:SAML:2.0:bindings:HTTP-POST', description:'').save()
		def httpArtifact = new SamlURI(type:SamlURIType.ProtocolBinding, uri:'urn:oasis:names:tc:SAML:2.0:bindings:HTTP-Artifact', description:'').save()
	}
	
	def "Validate list"() {
		setup:
		
		(1..25).each { i ->
			def ed = EntityDescriptor.build(entityID:"http://sp.test.com/$i")
			def sp = SPSSODescriptor.build(entityDescriptor: ed)
			sp.save()
		}
		
		when:
		def model = controller.list()

		then:
		model.serviceProviderList.size() >= 25
	}
	
	def "Show with no ID"() {		
		when:
		controller.show()
		
		then:
		controller.flash.type == "error"
		controller.flash.message == "fedreg.controllers.namevalue.missing"
		controller.response.redirectedUrl == "/serviceProvider/list"
	}
	
	def "Show with invalid SPSSODescriptor ID"() {
		setup:
		controller.params.id = 200000000
			
		when:
		controller.show()
		
		then:
        controller.flash.type == "error"
		controller.flash.message == "aaf.fr.foundation.spssoroledescriptor.nonexistant"
        controller.response.redirectedUrl == "/serviceProvider/list"
	}
	
	def "Validate create"() {
		setup:
		(1..10).each {
			Organization.build(active:true, approved:true).save()
		}
		
		(1..11).each { i ->
			AttributeBase.build().save()
		}
		
		(1..12).each { i ->
			SamlURI.build(type:SamlURIType.NameIdentifierFormat).save()
		}
		
		when:
		def model = controller.create()

		then:
		model.serviceProvider != null
		model.serviceProvider instanceof SPSSODescriptor
		model.organizationList.size() >= 10
		model.attributeList.size() >= 11
		model.nameIDFormatList.size() >= 12
	}
	
	def "Validate successful save"() {
		setup:
		def organization = Organization.build().save()
		def entityDescriptor = EntityDescriptor.build(organization:organization).save()
		def serviceProvider = SPSSODescriptor.build(entityDescriptor:entityDescriptor).save()
		
        spssoDescriptorService.metaClass.create = { def p -> 
            return [true, [organization:organization, entityDescriptor:entityDescriptor, serviceProvider:serviceProvider]]  
            //.. deliberately leaving out other return vals here
        } 

		when:
		def model = controller.save()
		
		then:
		controller.response.redirectedUrl == "/serviceProvider/show/${serviceProvider.id}"	
	}
	
	def "Validate failed save"() {
		setup:
		def organization = Organization.build()
		def entityDescriptor = EntityDescriptor.build(organization:organization)
		def serviceProvider = SPSSODescriptor.build(entityDescriptor:entityDescriptor)
		
        spssoDescriptorService.metaClass.create = { def p -> 
            return [false, [organization:organization, entityDescriptor:entityDescriptor, serviceProvider:serviceProvider]] 
            //.. deliberately leaving out other return vals here
        } 

		when:
		def model = controller.save()
		
		then:
		organization == controller.modelAndView.model.organization	
		entityDescriptor == controller.modelAndView.model.entityDescriptor	
		serviceProvider == controller.modelAndView.model.serviceProvider
		
		controller.flash.type == "error"
		controller.flash.message == "aaf.fr.foundation.spssoroledescriptor.save.validation.error"	
	}
	
	def "Validate successful update"() {
		setup:
		def organization = Organization.build()
		def entityDescriptor = EntityDescriptor.build(organization:organization)
		def serviceProvider = SPSSODescriptor.build(entityDescriptor:entityDescriptor).save()
		
		controller.params.id = serviceProvider.id
        user.permissions.add("descriptor:${serviceProvider.id}:update")
		spssoDescriptorService.metaClass.update = { def p -> 
            return [true, serviceProvider]
        } 

		when:
		def model = controller.update()
		
		then:
		controller.response.redirectedUrl == "/serviceProvider/show/${serviceProvider.id}"	
	}

    def "Validate update with incorrect perms"() {
        setup:
        def organization = Organization.build()
        def entityDescriptor = EntityDescriptor.build(organization:organization)
        def serviceProvider = SPSSODescriptor.build(entityDescriptor:entityDescriptor).save()
        
        controller.params.id = serviceProvider.id

        user.permissions.add("descriptor:-1:update")
        
        when:
        def model = controller.update()
        
        then:
        controller.response.status == 403
    }
	
	def "Invalid or non existing SPSSODescriptor fails update"() {
		setup:		
		controller.params.id = 1
		
		when:
		def model = controller.update()
		
		then:
		controller.response.redirectedUrl == "/serviceProvider/list"	
		controller.flash.type == "error"
		controller.flash.message == "aaf.fr.foundation.spssoroledescriptor.nonexistant"
	}
	
	def "Invalid service response fails update"() {
		setup:
		def organization = Organization.build()
		def entityDescriptor = EntityDescriptor.build(organization:organization)
		def serviceProvider = SPSSODescriptor.build(entityDescriptor:entityDescriptor)
		
		controller.params.id = serviceProvider.id
        user.permissions.add("descriptor:${serviceProvider.id}:update")
		spssoDescriptorService.metaClass.update = { def p -> 
            return [false, serviceProvider]
        } 

		when:
		def model = controller.update()
		
		then:
		serviceProvider == controller.modelAndView.model.serviceProvider
		
		controller.flash.type == "error"
		controller.flash.message == "aaf.fr.foundation.spssoroledescriptor.update.validation.error"	
	}
}