package fedreg.core

import grails.test.*
import grails.plugin.spock.*

class AttributeSpecification extends UnitSpecification {
	
	def "Ensure save and validate assignments"() {
		setup:
		mockDomain(Attribute)
		mockDomain(AttributeScope)
		mockDomain(AttributeCategory)
		
		when:
		new Attribute(name: name, oid:oid, friendlyName:friendlyName, headerName:headerName, alias:alias, description:description, scope:scope, category:category).save()
		
		then:
		def attr = Attribute.findByName(name) 
		attr != null
		attr.oid == oid
		attr.name == name
		attr.friendlyName == friendlyName
		attr.headerName == headerName
		attr.alias == alias
		attr.description == description
		attr.scope.name = scope.name
		attr.category.name = category.name
		
		where:
		oid << ['1.2.3.4.5', '1.2.3.4.6']
		friendlyName << ['Surname', 'Given Name']
		name << ['saml2:attr:sn', 'saml2:attr:givenName']
		headerName << ['HEADER_SN', 'HEADER_GIVENNAME']
		alias << ['alias', 'alias']
		description << ['A persons surname', 'A persons given name']
		scope << [ new AttributeScope(name:'Federation'), new AttributeScope(name:'Local')]
		category << [new AttributeCategory(name:'mandatory'), new AttributeCategory(name:'optional')]
	}
	
}
