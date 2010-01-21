package fedreg.core

import grails.test.*
import grails.plugins.spock.*

class AttributeScopeSpecification extends UnitSpecification {
	
	def "basic save"() {
		setup:
		mockDomain(AttributeScope)
		
		when:
		def attScope = new AttributeScope(name: name)
		attScope.save()
		
		then:
		AttributeScope.findByName(name) != null
		
		where:
		name << ['Federation', 'Local']
	}
	
}
