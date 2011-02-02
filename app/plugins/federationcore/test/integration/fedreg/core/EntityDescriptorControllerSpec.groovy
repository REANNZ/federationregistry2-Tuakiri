package fedreg.core

import grails.plugin.spock.*

import fedreg.core.*
import fedreg.workflow.*
import grails.plugins.nimble.core.*

class EntityDescriptorControllerSpec extends IntegrationSpec {
	
	static transactional = true
	
	def controller
	def savedMetaClasses
	def entityDescriptorService
	def user
	
	def cleanup() {
		SpecHelpers.resetMetaClasses(savedMetaClasses)
		entityDescriptorService.metaClass = EntityDescriptorService.metaClass
	}
	
	def setup () {
		savedMetaClasses = [:]
		
		SpecHelpers.registerMetaClass(EntityDescriptorService, savedMetaClasses)
		entityDescriptorService.metaClass = EntityDescriptorService.metaClass
		
		controller = new EntityDescriptorController(entityDescriptorService:entityDescriptorService)
		user = UserBase.build()
		SpecHelpers.setupShiroEnv(user)
		
		// Clear storage - odd issue with 1.3.6 have not yet confirmed where bug lies
		EntityDescriptor.findAll()*.delete(flush:true)
		Organization.findAll()*.delete(flush:true)
	}
	
	def "Validate list"() {
		setup:		
		(1..20).each { i ->
			def ed = EntityDescriptor.build(entityID:"http://sp.test.com/$i")
			ed
		}
		
		when:
		def model = controller.list()

		then:
		println "OUTLIST: ${EntityDescriptor.list()}"
		model.entityList.size() == 20
	}
	
	def "Validate list with max set"() {
		setup:
		(1..25).each { i ->
			def ed = EntityDescriptor.build(entityID:"http://sp.test.com/$i")
			ed
		}
		controller.params.max = 10
		
		when:
		def model = controller.list()

		then:
		model.entityList.size() == 10
		model.entityList.get(0) == EntityDescriptor.list().get(0)
		model.entityList.get(9) == EntityDescriptor.list().get(9)
	}
	
	def "Validate list with max and offset set"() {
		setup:
		(1..25).each { i->
			def ed = EntityDescriptor.build(entityID:"http://sp.test.com/$i")
			ed
		}
		controller.params.max = 10
		controller.params.offset = 5
		
		when:
		def model = controller.list()

		then:
		model.entityList.size() == 10
		model.entityList.get(0) == EntityDescriptor.list().get(5)
		model.entityList.get(9) == EntityDescriptor.list().get(14)
	}
	
	def "Show with no ID"() {		
		when:
		controller.show()
		
		then:
		controller.flash.type == "error"
		controller.flash.message == "fedreg.controllers.namevalue.missing"
		controller.response.redirectedUrl == "/entityDescriptor/list"
	}
	
	def "Show with invalid EntityDescriptor ID"() {
		setup:
		controller.params.id = 2
			
		when:
		controller.show()
		
		then:
		controller.flash.type == "error"
		controller.flash.message == "fedreg.core.entitydescriptor.nonexistant"
		controller.response.redirectedUrl == "/entityDescriptor/list"
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
		model.organizationList.size() == 10
	}
	
	def "Validate successful save"() {
		setup:
		def organization = Organization.build()
		def entityDescriptor = EntityDescriptor.build(organization:organization) 
		
		when:
		entityDescriptorService.metaClass.create = { def p -> 
			return [true, entityDescriptor]
		} 
		def model = controller.save()
		
		then:
		controller.response.redirectedUrl == "/entityDescriptor/show/${entityDescriptor.id}"
	}
	
	def "Validate failed save"() {
		setup:
		def organization = Organization.build()
		def entityDescriptor = EntityDescriptor.build(organization:organization)
		
		when:
		entityDescriptorService.metaClass.create = { def p -> 
			return [false, entityDescriptor]
		} 
		def model = controller.save()
		
		then:		
		controller.flash.type == "error"
		controller.flash.message == "fedreg.core.entitydescriptor.save.validation.error"	
	}
	
	def "Validate successful update with correct permissions"() {
		setup:
		def organization = Organization.build()
		def entityDescriptor = EntityDescriptor.build(organization:organization)
		
		controller.params.id = entityDescriptor.id
		user.perms.add("descriptor:${entityDescriptor.id}:update")
		
		when:
		entityDescriptorService.metaClass.update = { def p -> 
			return [true, entityDescriptor]
		} 
		def model = controller.update()
		
		then:
		controller.response.redirectedUrl == "/entityDescriptor/show/${entityDescriptor.id}"
	}
	
	def "Invalid or non existing EntityDescriptor fails update"() {
		setup:		
		controller.params.id = 1
		
		when:
		def model = controller.update()
		
		then:
		controller.response.redirectedUrl == "/entityDescriptor/list"	
		controller.flash.type == "error"
		controller.flash.message == "fedreg.core.entitydescriptor.nonexistant"
	}
	
	def "Invalid service response fails update with correct permissions"() {
		setup:
		def organization = Organization.build()
		def entityDescriptor = EntityDescriptor.build(organization:organization)
		
		controller.params.id = entityDescriptor.id
		user.perms.add("descriptor:${entityDescriptor.id}:update")
		
		when:
		entityDescriptorService.metaClass.update = { def p -> 
			return [false, entityDescriptor]
		} 
		def model = controller.update()
		
		then:		
		controller.flash.type == "error"
		controller.flash.message == "fedreg.core.entitydescriptor.update.validation.error"	
	}
	
	def "Validate unsuccessful update without correct permissions"() {
		setup:
		def organization = Organization.build()
		def entityDescriptor = EntityDescriptor.build(organization:organization)
		
		controller.params.id = entityDescriptor.id
		
		when:
		def model = controller.update()
		
		then:
		controller.response.status == 403
	}

}