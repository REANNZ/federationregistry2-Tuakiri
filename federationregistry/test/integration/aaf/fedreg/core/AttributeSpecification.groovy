package fedreg.core

import grails.plugin.spock.*

class AttributeSpecification extends IntegrationSpecification {

	def "Validate all constraints"() {		
		when:
		def attr = Attribute.build()	
		then:
		attr.validate()
	}
	
	def "Validate Name not null constraint"() {		
		setup:
		def attr = Attribute.build()
		when:
		attr.name = null
		then:
		!attr.validate()
	}
	
	def "Validate OID not null constraint"() {		
		setup:
		def attr = Attribute.build()
		when:
		attr.oid = null
		then:
		!attr.validate()
	}

	def "Validate friendlyName not null constraint"() {		
		setup:
		def attr = Attribute.build()
		when:
		attr.friendlyName = null
		then:
		!attr.validate()
	}
	
	def "Validate headerName not null constraint"() {		
		setup:
		def attr = Attribute.build()
		when:
		attr.headerName = null
		then:
		!attr.validate()
	}
	
	def "Validate alias not null constraint"() {		
		setup:
		def attr = Attribute.build()
		when:
		attr.alias = null
		then:
		!attr.validate()
	}
	
	def "Validate description not null constraint"() {		
		setup:
		def attr = Attribute.build()
		when:
		attr.description = null
		then:
		!attr.validate()
	}
	
	def "Validate scope not null constraint"() {		
		setup:
		def attr = Attribute.build()
		when:
		attr.scope = null
		then:
		!attr.validate()
	}
	
	def "Validate category not null constraint"() {		
		setup:
		def attr = Attribute.build()
		when:
		attr.category = null
		then:
		!attr.validate()
	}
	
	def "Validate Name not blank constraint"() {		
		setup:
		def attr = Attribute.build()
		when:
		attr.name = ''
		then:
		!attr.validate()
	}
	
	def "Validate OID not blank constraint"() {		
		setup:
		def attr = Attribute.build()
		when:
		attr.oid = ''
		then:
		!attr.validate()
	}

	def "Validate friendlyName not blank constraint"() {		
		setup:
		def attr = Attribute.build()
		when:
		attr.friendlyName = ''
		then:
		!attr.validate()
	}
	
	def "Validate headerName not blank constraint"() {		
		setup:
		def attr = Attribute.build()
		when:
		attr.headerName = ''
		then:
		!attr.validate()
	}
	
	def "Validate alias not blank constraint"() {		
		setup:
		def attr = Attribute.build()
		when:
		attr.alias = ''
		then:
		!attr.validate()
	}
	
	def "Validate description not blank constraint"() {		
		setup:
		def attr = Attribute.build()
		when:
		attr.description = ''
		then:
		!attr.validate()
	}
}