package aaf.fr.metadata

import grails.test.spock.*

import aaf.fr.foundation.*

class MetadataControllerSpec extends IntegrationSpec {
	def controller
	def savedMetaClasses
	
	def cleanup() {
	}
	
	def setup () {
		controller = new MetadataController()
	}
}
