package fedreg.workflow

import grails.plugin.spock.*

import grails.plugins.nimble.core.UserBase
import grails.plugins.nimble.core.ProfileBase

class ProcessServiceSpec extends IntegrationSpec {
	def processService

	def "Create minimal process"() {
		def definition = new File('test/data/minimal.pr').getText()
		
		setup:
		def profile = new ProfileBase()
		def user = new UserBase(username:'testuser', profile: profile)
		user.save()
		
		when:
		processService.metaClass.authenticatedUser = user
		processService.create(definition)
		
		then:
		def process = Process.findByName('Minimal Process')
		process.tasks.get(0).launch.get('approved').contains('task2')
		process.tasks.get(1).terminate.get('error').contains('task3')
		process.creator == user
	}
}