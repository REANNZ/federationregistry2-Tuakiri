package fedreg.core

import grails.plugin.spock.*

class EntityDescriptorSpec extends IntegrationSpec {

	def "Ensure functioning operates as expected"() {
		when:
		def org = new Organization(active:true, approved:true)
		def ed = new EntityDescriptor(organization:org, active:true, approved:true)
		
		then:
		ed.functioning()
	}
	
	def "Ensure functioning fails when not active as expected"() {
		when:
		def org = new Organization(active:true, approved:true)
		def ed = new EntityDescriptor(organization:org, active:false, approved:true)
		
		then:
		!ed.functioning()
	}
	
	def "Ensure functioning fails when unapproved as expected"() {
		when:
		def org = new Organization(active:true, approved:true)
		def ed = new EntityDescriptor(organization:org, active:true, approved:false)
		
		then:
		!ed.functioning()
	}
	
	def "Ensure functioning fails when not active and unapproved as expected"() {
		when:
		def org = new Organization(active:true, approved:true)
		def ed = new EntityDescriptor(organization:org, active:false, approved:false)
		
		then:
		!ed.functioning()
	}
	
	def "Ensure functioning fails locally fine but parent org not functioning"() {
		when:
		def org = new Organization(active:true, approved:false)
		def ed = new EntityDescriptor(organization:org, active:true, approved:false)
		
		then:
		!ed.functioning()
	}

}