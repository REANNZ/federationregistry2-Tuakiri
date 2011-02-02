package fedreg.metadata

import grails.plugin.spock.*

import fedreg.core.*

class MetadataControllerSpec extends IntegrationSpec {
	def controller
	def savedMetaClasses
	
	def cleanup() {
	}
	
	def setup () {
		controller = new MetadataController()
	}
}