package aaf.fr.foundation

import grails.test.spock.*
import aaf.fr.workflow.*
import aaf.fr.identity.Subject

class EntityDescriptorControllerSpec extends IntegrationSpec {
	
	static transactional = true
	
	def controller, entityDescriptorService, user
	
	def cleanup() {
	}
	
	def setup () {
        entityDescriptorService = new EntityDescriptorService()
		
		controller = new EntityDescriptorController(entityDescriptorService:entityDescriptorService)
		user = Subject.build()
		SpecHelpers.setupShiroEnv(user)
	}
	
	def "Validate list"() {
		setup:		
		(1..20).each { i ->
			def ed = EntityDescriptor.build()
			ed
		}
		
		when:
		def model = controller.list()

		then:
		model.entityList.size() >= 20
	}
	
	def "Show with no ID"() {		
		when:
		controller.show()
		
		then:
		controller.flash.type == "error"
		controller.flash.message == "controllers.fr.generic.namevalue.missing"
		controller.response.redirectedUrl == "/membership/entitydescriptor/list"
	}
	
	def "Show with invalid EntityDescriptor ID"() {
		setup:
		controller.params.id = 2000000
			
		when:
		controller.show()
		
		then:
		controller.flash.type == "error"
		controller.flash.message == "aaf.fr.foundation.entitydescriptor.nonexistant"
		controller.response.redirectedUrl == "/membership/entitydescriptor/list"
	}
	
	def "Validate create"() {
		setup:
		(1..10).each {
			Organization.build()
		}
		
		when:
		def model = controller.create()

		then:
		model.entity != null
		model.entity instanceof EntityDescriptor
		model.organizationList.size() > 0
	}
	
	def "Validate successful save"() {
		setup:
		def organization = Organization.build()
		def entityDescriptor = EntityDescriptor.build(organization:organization) 
		
		when:
		entityDescriptorService.metaClass.create = { def p -> 
			return [true, entityDescriptor]
		} 
		controller.request.method = 'POST'
		def model = controller.save()
		
		then:
		controller.response.redirectedUrl == "/membership/entitydescriptor/show/${entityDescriptor.id}"
	}
	
	def "Validate failed save"() {
		setup:
		def organization = Organization.build()
		def entityDescriptor = EntityDescriptor.build(organization:organization)
		
		when:
		entityDescriptorService.metaClass.create = { def p -> 
			return [false, entityDescriptor]
		} 
		controller.request.method = 'POST'
		def model = controller.save()
		
		then:		
		controller.flash.type == "error"
		controller.flash.message == "aaf.fr.foundation.entitydescriptor.save.validation.error"	
	}
	
	def "Validate successful update with correct permissions"() {
		setup:
		def organization = Organization.build()
		def entityDescriptor = EntityDescriptor.build(organization:organization)
		
		controller.params.id = entityDescriptor.id
		user.permissions.add("federation:management:descriptor:${entityDescriptor.id}:update")
		
		when:
		entityDescriptorService.metaClass.update = { def p -> 
			return [true, entityDescriptor]
		} 
		controller.request.method = 'PUT'
		def model = controller.update()
		
		then:
		controller.response.redirectedUrl == "/membership/entitydescriptor/show/${entityDescriptor.id}"
	}
	
	def "Invalid or non existing EntityDescriptor fails update"() {
		setup:		
		controller.params.id = 2000000
		
		when:
		controller.request.method = 'PUT'
		def model = controller.update()
		
		then:
		controller.response.redirectedUrl == "/membership/entitydescriptor/list"	
		controller.flash.type == "error"
		controller.flash.message == "aaf.fr.foundation.entitydescriptor.nonexistant"
	}
	
	def "Invalid service response fails update with correct permissions"() {
		setup:
		def organization = Organization.build()
		def entityDescriptor = EntityDescriptor.build(organization:organization)
		
		controller.params.id = entityDescriptor.id
		user.permissions.add("federation:management:descriptor:${entityDescriptor.id}:update")
		
		when:
		entityDescriptorService.metaClass.update = { def p -> 
			return [false, entityDescriptor]
		} 
		controller.request.method = 'PUT'
		def model = controller.update()
		
		then:		
		controller.flash.type == "error"
		controller.flash.message == "aaf.fr.foundation.entitydescriptor.update.validation.error"	
	}
	
	def "Validate unsuccessful update without correct permissions"() {
		setup:
		def organization = Organization.build()
		def entityDescriptor = EntityDescriptor.build(organization:organization)
		
		controller.params.id = entityDescriptor.id
		
		when:
		controller.request.method = 'PUT'
		def model = controller.update()
		
		then:
		controller.response.status == 403
	}

}
