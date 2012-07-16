package aaf.fr.foundation

import grails.plugin.spock.*
import aaf.fr.identity.Subject

class OrganizationContactControllerSpec extends IntegrationSpec {
	
    def user, controller, renderMap
	
	def setup () {
        OrganizationContactController.metaClass.render = { Map map ->
            renderMap = map
        }
        controller = new OrganizationContactController()

		user = new Subject(principal:"testuser", email:"test@testdomain.com")
		SpecHelpers.setupShiroEnv(user)
	}
	
	def "Test contact search"() {
		setup:		
		def c1 = new Contact(givenName:'fred', surname:'bloggz', email:'fbloggs@abc.test.com').save()
		def c2 = new Contact(givenName:'roe', surname:'schmoe', email:'jschmoe@abc.test.com').save()
		def c3 = new Contact(givenName:'max', surname:'mustermaan', email:'mmann@test2.com').save()
		
		controller.params.givenName = givenName
		controller.params.surname = surname
		controller.params.email = email
		
		when:
		controller.search()
		
		then:
		renderMap.model.contacts.size() == x
		
		where:
		x << [2, 1, 1, 0]
		email << ["abc.test.com", "", "", "doesntexist@test.com"]		// spelling error on 4th for example
		givenName << ["", "roe", "", ""]
		surname << ["", "", "bloggz", ""]
	}
	
	def "Test contact addition"() {
		setup:		
		def c1 = new Contact(givenName:'fred', surname:'bloggs', email:'fbloggs@testing.com').save()
		def ct = new ContactType(name:"technical",displayName:"technical",description:"technical contacts").save()
		def ot = new OrganizationType(name:"test", displayName:"test").save()
		
		def o = new Organization(name:"o.test.com", displayName:"organization", lang:"en", url:"http://o.test.com", primary:ot).save()
		
		controller.params.id = o.id
		controller.params.contactID = c1.id
		controller.params.contactType = ct.name
		user.permissions.add("organization:${o.id}:contact:add")
		
		when:
		controller.create()
		o.refresh()

		then:
        o.contacts.size() == 1
	}

}