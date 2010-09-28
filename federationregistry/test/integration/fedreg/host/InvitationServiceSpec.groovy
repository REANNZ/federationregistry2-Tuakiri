package fedreg.host

import grails.plugin.spock.*

class InvitationServiceSpec extends IntegrationSpec {
	static transactional = true
	
	def invitationService
	
	def "Ensure no duplicate invitations are created"() {		
		when:
			(1..5000).each {
				def invite = invitationService.create(null, 1, null, "controller", "action", null)
			}
		
		then:
		Invitation.count() == 5000
	}
}