package fedreg.core

import grails.plugin.spock.*

class AttributeCategorySpecification extends UnitSpecification {
	
	def "Validate all constraints"() {		
		when:
		def category = AttributeCategory.build()	
		then:
		category.validate()
	}
	
	def "Validate Name not null constraints"() {		
		setup:
		def category = AttributeCategory.build()	
		when:
		category.name = null
		then:
		!category.validate()
	}
	
	def "Validate Name not blank constraints"() {		
		setup:
		def category = AttributeCategory.build()	
		when:
		category.name = ''
		then:
		!category.validate()
	}
	
}