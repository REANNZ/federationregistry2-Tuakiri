package fedreg.core

import grails.plugin.spock.*

import fedreg.core.*
import fedreg.workflow.*
import grails.plugins.nimble.core.*

class OrganizationControllerSpec extends IntegrationSpec {
	
	def controller
	def savedMetaClasses
	def organizationService = new OrganizationService()
	
	def cleanup() {
		SpecHelpers.resetMetaClasses(savedMetaClasses)
		organizationService.metaClass = OrganizationService.metaClass
	}
	
	def setup () {
		savedMetaClasses = [:]
		
		SpecHelpers.registerMetaClass(OrganizationService, savedMetaClasses)
		organizationService.metaClass = OrganizationService.metaClass
		
		controller = new OrganizationController(organizationService:organizationService)
		def user = UserBase.build()
		SpecHelpers.setupShiroEnv(user)
	}
	
	def "Validate list"() {
		setup:
		
		(1..25).each { i ->
			def org = Organization.build()
			org.save()
		}
		
		when:
		def model = controller.list()

		then:
		model.organizationList.size() == 20
	}
	
	def "Validate list with max set"() {
		setup:
		(1..25).each { i ->
			def org = Organization.build()
			org.save()
		}
		controller.params.max = 10
		
		when:
		def model = controller.list()

		then:
		model.organizationList.size() == 10
		model.organizationList.get(0) == Organization.list().get(0)
		model.organizationList.get(9) == Organization.list().get(9)
	}
	
	def "Validate list with max and offset set"() {
		setup:
		(1..25).each { i->
			def org = Organization.build()
			org.save()
		}
		controller.params.max = 10
		controller.params.offset = 5
		
		when:
		def model = controller.list()

		then:
		model.organizationList.size() == 10
		model.organizationList.get(0) == Organization.list().get(5)
		model.organizationList.get(9) == Organization.list().get(14)
	}
	
	def "Show with no ID"() {		
		when:
		controller.show()
		
		then:
		controller.flash.type == "error"
		controller.flash.message == "fedreg.controllers.namevalue.missing"
		controller.response.redirectedUrl == "/organization/list"
	}
	
	def "Show with invalid Organization ID"() {
		setup:
		controller.params.id = 2
			
		when:
		controller.show()
		
		then:
		controller.flash.type == "error"
		controller.flash.message == "fedreg.core.organization.nonexistant"
		controller.response.redirectedUrl == "/organization/list"
	}
	
	def "Validate create"() {
		setup:
		(1..10).each {
			OrganizationType.build().save()
		}
		
		when:
		def model = controller.create()

		then:
		model.organization != null
		model.organization instanceof Organization
		model.organizationTypes.size() == 10
	}
	
	def "Validate successful save"() {
		setup:
		def organization = Organization.build().save()
		
		when:
		organizationService.metaClass.create = { def p -> 
			return [true, organization]
		} 
		def model = controller.save()
		
		then:
		controller.response.redirectedUrl == "/organization/show/${organization.id}"	
	}
	
	def "Validate failed save"() {
		setup:
		def organization = Organization.build().save()
		
		when:
		organizationService.metaClass.create = { def p -> 
			return [false, organization]
		} 
		def model = controller.save()
		
		then:
		controller.flash.type = "error"
		controller.flash.message = "fedreg.core.organization.save.validation.error"	
	}
	
	def "Validate successful update"() {
		setup:
		def organization = Organization.build().save()
		
		controller.params.id = organization.id
		
		when:
		organizationService.metaClass.update = { def p -> 
			return [true, organization]
		} 
		def model = controller.update()
		
		then:
		controller.response.redirectedUrl == "/organization/show/${organization.id}"	
	}
	
	def "Invalid or non existing Organization fails update"() {
		setup:		
		controller.params.id = 1
		
		when:
		def model = controller.update()
		
		then:
		controller.response.redirectedUrl == "/organization/list"	
		controller.flash.type = "error"
		controller.flash.message = "fedreg.core.organization.nonexistant"
	}
	
	def "Invalid organization data fails update"() {
		setup:
		def organization = Organization.build().save()
		
		controller.params.id = organization.id
		
		when:
		organizationService.metaClass.update = { def p -> 
			return [false, organization]
		} 
		def model = controller.update()
		
		then:		
		controller.flash.type = "error"
		controller.flash.message = "fedreg.core.organization.update.validation.error"	
	}
}