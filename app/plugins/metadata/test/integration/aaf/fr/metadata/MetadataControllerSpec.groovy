package aaf.fr.metadata

import grails.plugin.spock.*

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