package aaf.fr.reporting

import grails.plugin.spock.*
import aaf.fr.foundation.*

class IdentityProviderAttributeComplianceControllerSpec extends IntegrationSpec {

	def controller
	def savedMetaClasses
	
	def cleanup() {
	}
	
	def setup () {
		savedMetaClasses = [:]
		
		controller = new IdentityProviderAttributeComplianceController()
	}
	
	def "Ensure summary output functions correctly"() {
		setup:
		def entityDescriptor = EntityDescriptor.build(entityID:"http://idp.test.com").save()
		def identityProvider = IDPSSODescriptor.build(entityDescriptor: entityDescriptor).save()
		
		(1..24).each { i ->
			def ed = EntityDescriptor.build(entityID:"http://idp.test.com/$i")
			def idp = IDPSSODescriptor.build(entityDescriptor: ed)
			idp.save()
		}
		
		def ac1 = AttributeCategory.build().save()
		def ac2 = AttributeCategory.build().save()
		def ac3 = AttributeCategory.build().save()
		
		// Create attributes and make supported by core IDPSSODescriptor
		(1..2).each { i ->
			def ab = AttributeBase.build(category:ac1).save()
			identityProvider.addToAttributes(new Attribute(base:ab)).save()
		}
		(1..3).each { i ->
			def ab = AttributeBase.build(category:ac2).save()
			identityProvider.addToAttributes(new Attribute(base:ab)).save()
		}
		(1..4).each { i ->
			def ab = AttributeBase.build(category:ac3).save()
			identityProvider.addToAttributes(new Attribute(base:ab)).save()
		}
		
		// Create additional attributes not supported by core IDPSSODescriptor
		(1..3).each { i ->
			AttributeBase.build(category:ac1).save()
		}
		(1..3).each { i ->
			AttributeBase.build(category:ac2).save()
		}
		(1..3).each { i ->
			AttributeBase.build(category:ac3).save()
		}
		
		when:
		def model = controller.summary()
		
		then:
		model.identityProviderList.size() == 25
		model.categorySupportSummaries.size() == 75		// 25 IDP with 3 categories per IDP
		model.categorySupportSummaries.each {
			if(it.name == ac1.name) {
				it.totalCount == 5
				if(it.idp == identityProvider)
					it.supportedCount == 2
			}
			if(it.name == ac2.name) {
				it.totalCount == 6
				if(it.idp == identityProvider)
					it.supportedCount == 3
			}
			if(it.name == ac2.name) {
				it.totalCount == 7
				if(it.idp == identityProvider)
					it.supportedCount == 4
			}
		}
	}
	
	def "Ensure comprehensive output functions correctly"() {
		setup:
		def entityDescriptor = EntityDescriptor.build(entityID:"http://idp.test.com").save()
		def identityProvider = IDPSSODescriptor.build(entityDescriptor: entityDescriptor).save()
		
		def ac1 = AttributeCategory.build().save()
		def ac2 = AttributeCategory.build().save()
		def ac3 = AttributeCategory.build().save()
		
		// Create attributes and make supported by core IDPSSODescriptor
		(1..2).each { i ->
			def ab = AttributeBase.build(category:ac1).save()
			identityProvider.addToAttributes(new Attribute(base:ab)).save()
		}
		(1..3).each { i ->
			def ab = AttributeBase.build(category:ac2).save()
			identityProvider.addToAttributes(new Attribute(base:ab)).save()
		}
		(1..4).each { i ->
			def ab = AttributeBase.build(category:ac3).save()
			identityProvider.addToAttributes(new Attribute(base:ab)).save()
		}
		
		// Create additional attributes not supported by core IDPSSODescriptor
		(1..3).each { i ->
			AttributeBase.build(category:ac1).save()
		}
		(1..3).each { i ->
			AttributeBase.build(category:ac2).save()
		}
		(1..3).each { i ->
			AttributeBase.build(category:ac3).save()
		}
		
		when:
		controller.params.id = identityProvider.id
		def model = controller.comprehensive()
		
		then:
		model.identityProvider == identityProvider
		model.categorySupport.size() == 3
		model.categorySupport.each {
			if(it.name == ac1.name) {
				it.totalCount == 5
				it.supportedCount == 2
			}
			if(it.name == ac2.name) {
				it.totalCount == 6
				it.supportedCount == 3
			}
			if(it.name == ac2.name) {
				it.totalCount == 7
				it.supportedCount == 4
			}
		}
	}
	
	def "Ensure federationwide output functions correctly"() {
		setup:
		def ac1 = AttributeCategory.build().save()
		def ab = AttributeBase.build(category:ac1).save()
		def ab2 = AttributeBase.build(category:ac1).save()
		def ab3 = AttributeBase.build(category:ac1).save()
		def ab4 = AttributeBase.build(category:ac1).save()
		def ab5 = AttributeBase.build(category:ac1).save()
		
		(1..24).each { i ->
			def ed = EntityDescriptor.build(entityID:"http://idp.test.com/$i")
			def idp = IDPSSODescriptor.build(entityDescriptor: ed).save()
			idp.addToAttributes(new Attribute(base:ab))
			if(i % 3 == 0)
				idp.addToAttributes(new Attribute(base:ab2))
			idp.save()
		}
		
		when:
		controller.params.id = ab.id
		def model = controller.federationwide()
		
		then:
		model.identityProviderList.size() == 24
		model.supportingIdentityProviderList.size() == 24
	}
	
	def "Ensure federationwide output functions correctly (subset)"() {
		setup:
		def ac1 = AttributeCategory.build().save()
		def ab = AttributeBase.build(category:ac1).save()
		def ab2 = AttributeBase.build(category:ac1).save()
		def ab3 = AttributeBase.build(category:ac1).save()
		def ab4 = AttributeBase.build(category:ac1).save()
		def ab5 = AttributeBase.build(category:ac1).save()
		
		(1..24).each { i ->
			def ed = EntityDescriptor.build(entityID:"http://idp.test.com/$i")
			def idp = IDPSSODescriptor.build(entityDescriptor: ed).save()
			idp.addToAttributes(new Attribute(base:ab))
			if(i % 3 == 0)
				idp.addToAttributes(new Attribute(base:ab2))
			idp.save()
		}
		
		when:
		controller.params.id = ab2.id
		def model = controller.federationwide()
		
		then:
		model.identityProviderList.size() == 24
		model.supportingIdentityProviderList.size() == 8
	}
	
}