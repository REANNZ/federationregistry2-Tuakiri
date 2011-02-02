package fedreg.host

import grails.plugin.spock.*

import org.apache.shiro.authc.UnknownAccountException
import org.apache.shiro.authc.DisabledAccountException
import org.apache.shiro.authc.IncorrectCredentialsException

import fedreg.auth.*

class ShibbolethRealmSpec extends IntegrationSpec {
	static transactional = true
	
	def grailsApplication
	def userService
	def adminsService
	def groupService
	def roleService
	
	def realm 
	
	def setup() {
		realm = new ShibbolethRealm(grailsApplication:grailsApplication, userService: userService, adminsService:adminsService, groupService:groupService, roleService:roleService)
	}
	
	def "ensure exception when principal not supplied"() {
		setup:
			def authToken = new ShibbolethToken(surname:'Bloggs', email:'fbloggs@test.com', 
							entityID:'http://idp.edu.au/shibboleth', homeOrganization:'testorg', homeOrganizationType:'university')
							
		when:
			realm.authenticate(authToken)
			
		then:
			UnknownAccountException e = thrown()
			e.message == "Authentication attempt for Shibboleth provider, denying attempt as no persistent identifier (ShibbolethToken.principal) was provided"
	}
	
	def "ensure exception when entityID not supplied"() {
		setup:
			def authToken = new ShibbolethToken(principal:'eduperson:targetedid:123', surname:'Bloggs', email:'fbloggs@test.com', 
							homeOrganization:'testorg', homeOrganizationType:'university')
							
		when:
			realm.authenticate(authToken)
			
		then:
			UnknownAccountException e = thrown()
			e.message == "Authentication attempt for Shibboleth provider, denying attempt as no entityID (ShibbolethToken.entityID) was provided"
	}
	
	def "ensure exception when homeOrganization not supplied"() {
		setup:
			def authToken = new ShibbolethToken(principal:'eduperson:targetedid:123', givenName: 'Fred', surname:'Bloggs', email:'fbloggs@test.com', 
							entityID:'http://idp.edu.au/shibboleth', homeOrganizationType:'university')
							
		when:
			realm.authenticate(authToken)
			
		then:
			UnknownAccountException e = thrown()
			e.message == "Authentication attempt for Shibboleth provider, denying attempt as no homeOrganization (ShibbolethToken.homeOrganization) was provided"
	}
	
	def "ensure exception when givenName and displayName not supplied"() {
		setup:
			def authToken = new ShibbolethToken(principal:'eduperson:targetedid:123', surname:'Bloggs', email:'fbloggs@test.com', 
							entityID:'http://idp.edu.au/shibboleth', homeOrganization:'testorg', homeOrganizationType:'university')
							
		when:
			realm.authenticate(authToken)
			
		then:
			UnknownAccountException e = thrown()
			e.message == "Authentication attempt for Shibboleth provider, denying attempt as no name details (either givenName + surname or displayName) provided"
	}
	
	def "ensure exception when surname and displayName not supplied"() {
		setup:
			def authToken = new ShibbolethToken(principal:'eduperson:targetedid:123', givenName: 'Fred', email:'fbloggs@test.com', 
							entityID:'http://idp.edu.au/shibboleth', homeOrganization:'testorg', homeOrganizationType:'university')
							
		when:
			realm.authenticate(authToken)
			
		then:
			UnknownAccountException e = thrown()
			e.message == "Authentication attempt for Shibboleth provider, denying attempt as no name details (either givenName + surname or displayName) provided"
	}
	
	def "ensure exception when displayName supplied without space delimiter"() {
		setup:
			def authToken = new ShibbolethToken(principal:'eduperson:targetedid:123', displayName: 'Fred', email:'fbloggs@test.com', 
							entityID:'http://idp.edu.au/shibboleth', homeOrganization:'testorg', homeOrganizationType:'university')
							
		when:
			realm.authenticate(authToken)
			
		then:
			UnknownAccountException e = thrown()
			e.message == "Authentication attempt for Shibboleth provider, provided displayName has only one part and will fail split on space char for local storage"
	}
	
	def "ensure exception when email not supplied"() {
		setup:
			def authToken = new ShibbolethToken(principal:'eduperson:targetedid:123', givenName: 'Fred', surname:'Bloggs', 
							entityID:'http://idp.edu.au/shibboleth', homeOrganization:'testorg', homeOrganizationType:'university')
							
		when:
			realm.authenticate(authToken)
			
		then:
			UnknownAccountException e = thrown()
			e.message == "Authentication attempt for Shibboleth provider, denying attempt as no email (ShibbolethToken.email) was provided"
	}
	
	def "ensure exception when IDP(entity) unknown"() {
		setup:
			def authToken = new ShibbolethToken(principal:'eduperson:targetedid:123', givenName: 'Fred', surname:'Bloggs', email:'fbloggs@test.com',
							entityID:'http://idp.edu.au/shibboleth', homeOrganization:'testorg', homeOrganizationType:'university')
							
		when:
			realm.authenticate(authToken)
			
		then:
			UnknownAccountException e = thrown()
			e.message == "Authentication attempt for Shibboleth provider, denying attempt as no Entity matching (ShibbolethToken.entityID) is available. Has bootstrap occured?"
	}
}