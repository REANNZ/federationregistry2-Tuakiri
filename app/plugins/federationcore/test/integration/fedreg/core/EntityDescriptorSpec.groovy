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
	
	def "Ensure EntityDescriptor not empty with IDP child"() {
		when:
		def ed = new EntityDescriptor()
		def idp = new IDPSSODescriptor()
		ed.addToIdpDescriptors(idp)
		
		then:
		!ed.empty()
	}
	
	def "Ensure EntityDescriptor not empty with SP child"() {
		when:
		def ed = new EntityDescriptor()
		def sp = new SPSSODescriptor()
		ed.addToSpDescriptors(idp)
		
		then:
		!ed.empty()
	}
	
	def "Ensure EntityDescriptor not empty with AA child"() {
		when:
		def ed = new EntityDescriptor()
		def aa = new AttributeAuthorityDescriptor()
		ed.addToAttributeAuthorityDescriptors(aa)
		
		then:
		!ed.empty()
	}
	
	def "Ensure EntityDescriptor not empty with IDP and AA child"() {
		when:
		def ed = new EntityDescriptor()
		def idp = new IDPSSODescriptor()
		ed.addToIdpDescriptors(idp)
		
		def aa = new AttributeAuthorityDescriptor()
		ed.addToAttributeAuthorityDescriptors(aa)
		
		then:
		!ed.empty()
	}
	
	def "Ensure EntityDescriptor empty no child nodes"() {
		when:
		def ed = new EntityDescriptor()
		
		then:
		ed.empty()
	}

}