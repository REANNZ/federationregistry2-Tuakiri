package fedreg.core

import grails.plugin.spock.*

import fedreg.core.*
import grails.plugins.nimble.core.*

class OrganizationContactControllerSpec extends IntegrationSpec {
	def user
	
	def setup () {
		user = new UserBase(username:"testuser", profile:new ProfileBase())
		SpecHelpers.setupShiroEnv(user)
		
		controller.metaClass.message = { Map map -> return "" }
		controller.metaClass.pluginContextPath = ""
	}
	
	def "Test contact search"() {
		setup:		
		def c1 = new Contact(givenName:'fred', surname:'bloggs', email:new MailURI(uri:'fbloggs@test.com')).save()
		def c2 = new Contact(givenName:'joe', surname:'schmoe', email:new MailURI(uri:'jschmoe@test.com')).save()
		def c3 = new Contact(givenName:'max', surname:'mustermaan', email:new MailURI(uri:'mmann@test2.com')).save()
		
		println MailURI.findAllByUriLike("%test.com%")
		
		controller.params.givenName = givenName
		controller.params.surname = surname
		controller.params.email = email
		
		when:
		controller.search()
		
		then:
		controller.renderArgs.model.contacts.size() == x
		
		where:
		x << [2, 1, 1, 0, 3]
		email << ["test.com", "", "", "mmann@test.com", null]		// spelling error on 4th for example
		givenName << ["", "joe", "", "", null]
		surname << ["", "", "bloggs", "", null]
	}
	
	def "Test contact addition"() {
		setup:		
		def c1 = new Contact(givenName:'fred', surname:'bloggs', email:new MailURI(uri:'fbloggs@test.com'))
		c1.save()
		c1.errors.each { println it }
		def ct = new ContactType(name:"technical",displayName:"technical",description:"technical contacts").save()
		def ot = new OrganizationType(name:"test", displayName:"test")
		ot.save()
		ot.errors.each { println it }
		
		def o = new Organization(name:"o.test.com", displayName:"organization", lang:"en", url:new UrlURI(uri:"http://o.test.com"), primary:ot)
		o.save()
		
		o.errors.each { println it }
		
		controller.params.id = o.id
		controller.params.contactID = c1.id
		controller.params.contactType = ct.name
		user.perms.add("organization:${o.id}:contact:add")
		
		when:
		controller.create()
		
		then:
		o.contacts.size() == 1
	}

}