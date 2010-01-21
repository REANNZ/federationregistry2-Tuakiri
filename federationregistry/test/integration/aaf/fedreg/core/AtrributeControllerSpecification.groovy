package fedreg.core

import grails.test.*
import grails.plugins.spock.*

class AttributeControllerSpecification extends IntegrationSpecification {
	
	def "save valid attribute"() {
		setup:
		def controller = new AttributeController()
		def scope = AttributeScope.build().save()
		def category = AttributeCategory.build().save()
		
		controller.params.oid = oid
		controller.params.name = name
		controller.params.friendlyName = friendlyName
		controller.params.headerName = headerName
		controller.params.alias = alias
		controller.params.description = description
		controller.params."scope.id" = scope.id
		controller.params."category.id" = category.id
		
		when:
		def model = controller.save()
		
		then:
		controller.response.redirectedUrl.startsWith("/attribute/show/")
		controller.flash.message.length() > 0
		
		
		where:
		oid << ['1.2.3.4.5', '1.2.3.4.6']
		friendlyName << ['Surname', 'Given Name']
		name << ['saml2:attr:sn', 'saml2:attr:givenName']
		headerName << ['HEADER_SN', 'HEADER_GIVENNAME']
		alias << ['alias', 'alias']
		description << ['A persons surname', 'A persons given name']
	}
	
	def "update valid attribute"() {
		setup:
		def controller = new AttributeController()
		def attr1 = Attribute.build().save()

		controller.params.id = attr1.id
		controller.params.version = attr1.version
		controller.params.oid = oid
		controller.params.name = name

		when:
		def model = controller.update()

		then:
		attr1.refresh()
		controller.response.redirectedUrl == "/attribute/show/${attr1.id}"	
		attr1.oid == oid
		attr1.name == name
		
		where:
		oid = "2.7.3.6.4.1.1.6"
		name = "super secret test name"
	}
}