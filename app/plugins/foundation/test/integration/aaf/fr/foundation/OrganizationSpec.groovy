package aaf.fr.foundation

import grails.plugin.spock.*

class OrganizationSpec extends IntegrationSpec {

	def "Ensure functioning operates as expected"() {
		when:
		def org = new Organization(active:true, approved:true)
		
		then:
		org.functioning()
	}
	
	def "Ensure functioning fails when not active as expected"() {
		when:
		def org = new Organization(active:false, approved:true)
		
		then:
		!org.functioning()
	}
	
	def "Ensure functioning fails when unapproved as expected"() {
		when:
		def org = new Organization(active:true, approved:false)
		
		then:
		!org.functioning()
	}
	
	def "Ensure functioning fails when not active and unapproved as expected"() {
		when:
		def org = new Organization(active:false, approved:false)
		
		then:
		!org.functioning()
	}

}