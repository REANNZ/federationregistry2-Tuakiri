package fedreg.compliance

import grails.plugin.spock.*

import fedreg.core.*
import grails.plugins.nimble.core.*

class AttributeReleaseControllerSpec extends IntegrationSpec {

	def controller
	def savedMetaClasses
	
	def cleanup() {
	}
	
	def setup () {
		savedMetaClasses = [:]
		
		controller = new AttributeReleaseController()
		
		// def user = UserBase.build()
		// SpecHelpers.setupShiroEnv(user)
	}
	
	def "Validate index"() {
		setup:
		(1..24).each { i ->
			def ed = EntityDescriptor.build(entityID:"http://idp.test.com/$i")
			def idp = IDPSSODescriptor.build(entityDescriptor: ed, active:true)
			idp.save()
		}
		def ed_ = EntityDescriptor.build(entityID:"http://idp.test.com/51").save()
		def idp_ = IDPSSODescriptor.build(entityDescriptor: ed_, active:false).save()
		
		(25..48).each { i ->
			def ed = EntityDescriptor.build(entityID:"http://idp.test.com/$i")
			def sp = SPSSODescriptor.build(entityDescriptor: ed, active:true)
			sp.save()
		}
		def sp_ = SPSSODescriptor.build(entityDescriptor: ed_, active:false).save()
		
		when:
		def model = controller.index()
		
		then:
		IDPSSODescriptor.count() == 25
		model.activeIdentityProviderList.size() == 24
		SPSSODescriptor.count() == 25
		model.activeServiceProviderList.size() == 24
	}

	def "Validate compare when idp releases all requested attributes but no optional"() {
		setup:
		def entityDescriptor = EntityDescriptor.build(entityID:"http://idp.test.com").save()
		def identityProvider = IDPSSODescriptor.build(entityDescriptor: entityDescriptor).save()
		def serviceProvider = SPSSODescriptor.build(entityDescriptor:entityDescriptor).save()
		def attributeConsumingService = AttributeConsumingService.build().save()
		serviceProvider.addToAttributeConsumingServices(attributeConsumingService)
		
		def ac1 = AttributeCategory.build().save()
		def ab = AttributeBase.build(category:ac1).save()
		def ab2 = AttributeBase.build(category:ac1).save()
		def ab3 = AttributeBase.build(category:ac1).save()
		def ab4 = AttributeBase.build(category:ac1).save()
		def ab5 = AttributeBase.build(category:ac1).save()
		
		identityProvider.addToAttributes(new Attribute(base:ab))
		identityProvider.addToAttributes(new Attribute(base:ab2))
		
		attributeConsumingService.addToRequestedAttributes(new RequestedAttribute(base:ab, isRequired:true, reasoning:'test'))
		attributeConsumingService.addToRequestedAttributes(new RequestedAttribute(base:ab2, isRequired:true, reasoning:'test'))
		attributeConsumingService.addToRequestedAttributes(new RequestedAttribute(base:ab3, isRequired:false, reasoning:'test'))
		
		when:
		controller.params.idp = identityProvider.id
		controller.params.sp = serviceProvider.id
		def model = controller.compare()
		
		then:
		model.requiredAttributes.size() == 2
		model.requiredAttributes.contains(ab)
		model.requiredAttributes.contains(ab2)

		model.suppliedRequiredAttributes.size() == 2
		model.suppliedRequiredAttributes.contains(ab)
		model.suppliedRequiredAttributes.contains(ab2)
		model.minimumRequirements = true
		
		model.optionalAttributes.size() == 1
		model.optionalAttributes.contains(ab3)
		model.suppliedOptionalAttributes.size() == 0
		
		model.identityProvider = identityProvider
	}
	
	def "Validate compare when idp releases all requested attributes and optional attributes"() {
		setup:
		def entityDescriptor = EntityDescriptor.build(entityID:"http://idp.test.com").save()
		def identityProvider = IDPSSODescriptor.build(entityDescriptor: entityDescriptor).save()
		def serviceProvider = SPSSODescriptor.build(entityDescriptor:entityDescriptor).save()
		def attributeConsumingService = AttributeConsumingService.build().save()
		serviceProvider.addToAttributeConsumingServices(attributeConsumingService)
		
		def ac1 = AttributeCategory.build().save()
		def ab = AttributeBase.build(category:ac1).save()
		def ab2 = AttributeBase.build(category:ac1).save()
		def ab3 = AttributeBase.build(category:ac1).save()
		def ab4 = AttributeBase.build(category:ac1).save()
		def ab5 = AttributeBase.build(category:ac1).save()
		
		identityProvider.addToAttributes(new Attribute(base:ab))
		identityProvider.addToAttributes(new Attribute(base:ab2))
		identityProvider.addToAttributes(new Attribute(base:ab3))
		
		attributeConsumingService.addToRequestedAttributes(new RequestedAttribute(base:ab, isRequired:true, reasoning:'test'))
		attributeConsumingService.addToRequestedAttributes(new RequestedAttribute(base:ab2, isRequired:true, reasoning:'test'))
		attributeConsumingService.addToRequestedAttributes(new RequestedAttribute(base:ab3, isRequired:false, reasoning:'test'))
		
		when:
		controller.params.idp = identityProvider.id
		controller.params.sp = serviceProvider.id
		def model = controller.compare()
		
		then:
		model.requiredAttributes.size() == 2
		model.requiredAttributes.contains(ab)
		model.requiredAttributes.contains(ab2)

		model.suppliedRequiredAttributes.size() == 2
		model.suppliedRequiredAttributes.contains(ab)
		model.suppliedRequiredAttributes.contains(ab2)
		model.minimumRequirements = true
		
		model.optionalAttributes.size() == 1
		model.optionalAttributes.contains(ab3)
		model.suppliedOptionalAttributes.size() == 1
		
		model.identityProvider = identityProvider
	}
	
	def "Validate compare when idp doesn't release all requested attributes"() {
		setup:
		def entityDescriptor = EntityDescriptor.build(entityID:"http://idp.test.com").save()
		def identityProvider = IDPSSODescriptor.build(entityDescriptor: entityDescriptor).save()
		def serviceProvider = SPSSODescriptor.build(entityDescriptor:entityDescriptor).save()
		def attributeConsumingService = AttributeConsumingService.build().save()
		serviceProvider.addToAttributeConsumingServices(attributeConsumingService)
		
		def ac1 = AttributeCategory.build().save()
		def ab = AttributeBase.build(category:ac1).save()
		def ab2 = AttributeBase.build(category:ac1).save()
		def ab3 = AttributeBase.build(category:ac1).save()
		def ab4 = AttributeBase.build(category:ac1).save()
		def ab5 = AttributeBase.build(category:ac1).save()
		
		identityProvider.addToAttributes(new Attribute(base:ab))
		identityProvider.addToAttributes(new Attribute(base:ab3))
		
		attributeConsumingService.addToRequestedAttributes(new RequestedAttribute(base:ab, isRequired:true, reasoning:'test'))
		attributeConsumingService.addToRequestedAttributes(new RequestedAttribute(base:ab2, isRequired:true, reasoning:'test'))
		attributeConsumingService.addToRequestedAttributes(new RequestedAttribute(base:ab3, isRequired:false, reasoning:'test'))
		
		when:
		controller.params.idp = identityProvider.id
		controller.params.sp = serviceProvider.id
		def model = controller.compare()
		
		then:
		model.requiredAttributes.size() == 2
		model.requiredAttributes.contains(ab)
		model.requiredAttributes.contains(ab2)

		model.suppliedRequiredAttributes.size() == 1
		model.suppliedRequiredAttributes.contains(ab)
		model.minimumRequirements = true
		
		model.optionalAttributes.size() == 1
		model.optionalAttributes.contains(ab3)
		model.suppliedOptionalAttributes.size() == 1
		
		model.identityProvider = identityProvider
	}

}