package fedreg.core

import grails.test.*
import grails.plugins.spock.*

class AttributeScopeSpecification extends UnitSpecification {
	
	def "Validate all constraints"() {		
		when:
		def scope = AttributeScope.build()	
		then:
		scope.validate()
	}
	
	def "Validate Name not null constraints"() {		
		setup:
		def scope = AttributeScope.build()	
		when:
		scope.name = null
		then:
		!scope.validate()
	}
	
	def "Validate Name not blank constraints"() {		
		setup:
		def scope = AttributeScope.build()	
		when:
		scope.name = ''
		then:
		!scope.validate()
	}
	
}