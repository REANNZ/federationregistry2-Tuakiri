package aaf.fedreg.core

import grails.test.*
import grails.plugin.spock.*

import aaf.fedreg.domain.*

class AttributeControllerSpecification extends ControllerSpecification {
	
	// FIXME: Multiple requirements in this specification are failing due to http://jira.codehaus.org/browse/GRAILS-5348
	// Exception is - No signature of method: aaf.fedreg.AttributeController.message()
	
	def "index redirect to list"() {
		setup:
		def params = [:]
		
		when:
		controller.index()
		
		then:
		redirectArgs == [action: "list", params: params]
	}
	
	def "list attributes" () {
		setup:
		def attr1 = [:] as Attribute
		def attr2 = [:] as Attribute
		def attrs = [attr1, attr2]
		
		mockDomain(Attribute, attrs)
		
		when:
		def model = controller.list()
		
		then:
		model.attributeInstanceList.containsAll(attrs)
		model.attributeInstanceTotal = 2	
	}
	
	def "list attributes with max" () {
		setup:
		def attr1 = [:] as Attribute
		def attr2 = [:] as Attribute
		def attrs = [attr1, attr2]
		
		controller.params.max = 1
		
		mockDomain(Attribute, attrs)
		
		when:
		def model = controller.list()
		
		then:
		model.attributeInstanceList.contains(attr1)
		!model.attributeInstanceList.contains(attr2)
		model.attributeInstanceTotal = 1	
	}
	
	def "create new attribute" () {
		setup:
		mockDomain(Attribute)
		
		when:
		def model = controller.create()
		
		then:
		model.attributeInstance != null
	}
	
	def "save invalid attribute"() {
		setup:
		mockDomain(Attribute)
		
		when:
		def model = controller.save()
		
		then:
		controller.modelAndView.view == "create"
		model.attributeInstance != null
	}
	
	def "show attribute" () {
		setup:
		def attr1 = [id:1] as Attribute
		def attrs = [attr1]
		
		mockDomain(Attribute, attrs)
		controller.params.id = 1
		
		when:
		def model = controller.show()
		
		then:
		model.attributeInstance == attr1	
	}
	
	def "show attribute invalid id" () {
		setup:
		def attr1 = [id:1] as Attribute
		def attrs = [attr1]
		
		mockDomain(Attribute, attrs)
		controller.params.id = 2
		
		when:
		def model = controller.show()
		
		then:
		redirectArgs == [action: "list", params: [:]]	
	}
	
	def "edit attribute"() {
		setup:
		def attr1 = [id:1] as Attribute
		def attrs = [attr1]
		
		mockDomain(Attribute, attrs)
		controller.params.id = 1
		
		when:
		def model = controller.edit()
		
		then:
		model.attributeInstance == attr1
	}
	
	def "edit attribute invalid id" () {
		setup:
		def attr1 = [id:1] as Attribute
		def attrs = [attr1]
		
		mockDomain(Attribute, attrs)
		controller.params.id = 2
		
		when:
		def model = controller.edit()
		
		then:
		redirectArgs == [action: "list", params: [:]]
		mockFlash.type = "error"	
	}
	
	def "update attribute with intermediate version change"() {
		setup:
		def attr1 = [id:1, version:2] as Attribute
		def attrs = [attr1]
		
		mockDomain(Attribute, attrs)
		controller.params.id = 1
		controller.params.version = 1
		
		when:
		def model = controller.update()
		
		then:
		controller.modelAndView.view = "edit"
		model.attributeInstance = attr1
	}
	
	def "update attribute with invalid data"() {
		setup:
		def attr1 = [id:1, version:1] as Attribute
		def attrs = [attr1]
		
		mockDomain(Attribute, attrs)
		controller.params.id = 1
		controller.params.version = 1
		
		when:
		def model = controller.update()
		
		then:
		controller.modelAndView.view = "edit"
		model.attributeInstance = attr1
	}
	
	def "update attribute with invalid ID"() {
		setup:
		def attr1 = [id:2, version:1] as Attribute
		def attrs = [attr1]
		
		mockDomain(Attribute, attrs)
		controller.params.id = 1
		controller.params.version = 1
		
		when:
		def model = controller.update()
		
		then:
		redirectArgs == [action: "list", params: [:]]
		flash.type == "error"
	}
	
	def "delete attribute"() {
		setup:
		def attr1 = [id:1, version:1] as Attribute
		def attrs = [attr1]
		
		mockDomain(Attribute, attrs)
		controller.params.id = 1
		
		when:
		def model = controller.delete()
		
		then:
		!attrs.contains(attr1)
		redirectArgs == [action: "list", params: [:]]
		flash.type == "success"
	}
	
	def "delete attribute invalid ID"() {
		setup:
		def attr1 = [id:1, version:1] as Attribute
		def attrs = [attr1]
		
		mockDomain(Attribute, attrs)
		controller.params.id = 2
		
		when:
		def model = controller.delete()
		
		then:
		attrs.contains(attr1)
		redirectArgs == [action: "list", params: [:]]
		flash.type == "error"
	}
	
}