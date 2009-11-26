package aaf.fedreg.core

import grails.test.*
import grails.plugin.spock.*

class AttributeCategorySpecification extends UnitSpecification {

	def "basic save"() {
		setup:
		mockDomain(AttributeCategory)
		
		when:
		def attCategory = new AttributeCategory(name: name)
		attCategory.save()
		
		then:
		AttributeCategory.findByName(name) != null
		
		where:
		name << ['optional', 'required']
	}

}
