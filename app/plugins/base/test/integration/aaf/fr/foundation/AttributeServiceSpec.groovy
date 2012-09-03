package aaf.fr.foundation

import grails.plugin.spock.*

class AttributeServiceSpec extends IntegrationSpec {

	def "Ensure functioning operates as expected"() {
		when:
		def org = new Organization(active:true, approved:true)
		def ed = new EntityDescriptor(organization:org, active:true, approved:true)
		def aa = new AttributeAuthorityDescriptor(organization:org, entityDescriptor:ed, active:true, approved:true)
		def ep = new AttributeService(descriptor:aa, active:true, approved:true)
		
		then:
		ep.functioning()
	}
	
	def "Ensure functioning fails when not active as expected"() {
		when:
		def org = new Organization(active:true, approved:true)
		def ed = new EntityDescriptor(organization:org, active:true, approved:true)
		def aa = new AttributeAuthorityDescriptor(organization:org, entityDescriptor:ed, active:true, approved:true)
		def ep = new AttributeService(descriptor:aa, active:false, approved:true)
		
		then:
		!ep.functioning()
	}
	
	def "Ensure functioning fails when unapproved as expected"() {
		when:
		def org = new Organization(active:true, approved:true)
		def ed = new EntityDescriptor(organization:org, active:true, approved:true)
		def aa = new AttributeAuthorityDescriptor(organization:org, entityDescriptor:ed, active:true, approved:true)
		def ep = new AttributeService(descriptor:aa, active:true, approved:false)
		
		then:
		!ep.functioning()
	}
	
	def "Ensure functioning fails when not active and unapproved as expected"() {
		when:
		def org = new Organization(active:true, approved:true)
		def ed = new EntityDescriptor(organization:org, active:true, approved:true)
		def aa = new AttributeAuthorityDescriptor(organization:org, entityDescriptor:ed, active:true, approved:true)
		def ep = new AttributeService(descriptor:aa, active:false, approved:false)
		
		then:
		!ep.functioning()
	}
	
	def "Ensure functioning fails locally fine but parent descriptor not functioning"() {
		when:
		def org = new Organization(active:true, approved:true)
		def ed = new EntityDescriptor(organization:org, active:true, approved:true)
		def aa = new AttributeAuthorityDescriptor(organization:org, entityDescriptor:ed, active:false, approved:true)
		def ep = new AttributeService(descriptor:aa, active:true, approved:true)
		
		then:
		!ep.functioning()
	}

}