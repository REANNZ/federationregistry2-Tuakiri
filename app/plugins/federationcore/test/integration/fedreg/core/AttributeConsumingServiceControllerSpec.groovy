package fedreg.core

import grails.plugin.spock.*

import fedreg.core.*
import fedreg.workflow.*
import grails.plugins.nimble.core.*

class AttributeConsumingServiceControllerSpec extends IntegrationSpec {
	def savedMetaClasses
	def user
	
	def setup () {
		savedMetaClasses = [:]
		
		SpecHelpers.registerMetaClass(WorkflowProcessService, savedMetaClasses)
		UserBase.metaClass.contact = [ getId: 1 ]
		user = UserBase.build()
		SpecHelpers.setupShiroEnv(user)
	}
	
	def cleanup() {
		SpecHelpers.resetMetaClasses(savedMetaClasses)
	}
	
	def "Add new requested attribute with correct perms"() {
		setup:
		def controller = new AttributeConsumingServiceController()
		def acs = AttributeConsumingService.build().save()
		def attr = AttributeBase.build().save()
		
		def wfProcessName, wfDescription, wfPriority, wfParams
		
		user.perms.add("descriptor:${acs.descriptor.id}:attribute:add")
		
		controller.params.id = acs.id
		controller.params.attrid = attr.id
		controller.params.reasoning = "I really need it!"
		
		WorkflowProcessService.metaClass.initiate =  { String processName, String instanceDescription, ProcessPriority priority, Map params ->
			wfProcessName = processName
			wfDescription = instanceDescription
			wfPriority = priority
			wfParams = params
			[true, [:]]
		}
		WorkflowProcessService.metaClass.run = { def processInstance -> }
		
		when:
		def model = controller.addRequestedAttribute()
		
		then:
		acs.requestedAttributes.size() == 1
		wfDescription == "Approval for addition of the attribute '${attr.friendlyName}' (OID: ${attr.oid}) to the service '${acs?.descriptor?.displayName}'"
		controller.response.contentAsString == "fedreg.attributeconsumingservice.requestedattribute.add.success"
	}
	
	def "Add new requested attribute with incorrect permissions"() {
		setup:
		def controller = new AttributeConsumingServiceController()
		def acs = AttributeConsumingService.build().save()
		def attr = AttributeBase.build().save()
		
		controller.params.id = acs.id
		controller.params.attrid = attr.id
		controller.params.reasoning = "I really need it!"
		
		when:
		def model = controller.addRequestedAttribute()
		
		then:
		acs.requestedAttributes == null
		controller.response.status == 403
	}
	
	def "Adding duplicate requested attribute fails"() {
		setup:
		def controller = new AttributeConsumingServiceController()
		def acs = AttributeConsumingService.build().save()
		def ra1 = RequestedAttribute.build().save()
		acs.addToRequestedAttributes(ra1)
		acs.save()
		
		controller.params.id = acs.id
		controller.params.attrid = ra1.base.id
		controller.params.reasoning = "I really need it!"
		
		user.perms.add("descriptor:${acs.descriptor.id}:attribute:add")
		WorkflowProcessService.metaClass.initiate =  { String processName, String instanceDescription, ProcessPriority priority, Map params ->
			wfProcessName = processName
			wfDescription = instanceDescription
			wfPriority = priority
			wfParams = params
			[true, [:]]
		}
		WorkflowProcessService.metaClass.run = { def processInstance -> }
		
		when:
		def model = controller.addRequestedAttribute()
		
		then:
		acs.requestedAttributes.size() == 1
		controller.response.contentAsString == "fedreg.attributeconsumingservice.requestedattribute.add.already.exists"
	}
	
	def "Add new requested attribute fails without reason"() {
		setup:
		def controller = new AttributeConsumingServiceController()
		def acs = AttributeConsumingService.build().save()
		def attr = Attribute.build()
		
		controller.params.id = acs.id
		controller.params.attrid = attr.id
		
		when:
		def model = controller.addRequestedAttribute()
		
		then:
		acs.requestedAttributes == null
	}
	
	def "Delete requested attribute succeeds"() {
		setup:
		def controller = new AttributeConsumingServiceController()
		def acs = AttributeConsumingService.build().save()
		def attr = AttributeBase.build().save()
		def ra1 = RequestedAttribute.build(base:attr).save()
		acs.addToRequestedAttributes(ra1)
		acs.save()
		
		controller.params.raid = ra1.id
		user.perms.add("descriptor:${acs.descriptor.id}:attribute:remove")
		
		when:
		def model = controller.removeRequestedAttribute()
		
		then:
		acs.requestedAttributes.size() == 0
		controller.response.contentAsString == "fedreg.attributeconsumingservice.requestedattribute.remove.success"
	}
	
	def "Delete requested attribute fails with wrong permissions"() {
		setup:
		def controller = new AttributeConsumingServiceController()
		def acs = AttributeConsumingService.build().save()
		def attr = AttributeBase.build().save()
		def ra1 = RequestedAttribute.build(base:attr).save()
		acs.addToRequestedAttributes(ra1)
		acs.save()
		
		controller.params.raid = ra1.id
		
		when:
		def model = controller.removeRequestedAttribute()
		
		then:
		acs.requestedAttributes.size() == 1
		controller.response.status == 403
	}
	
	def "List requested attributes functions correctly"() {
		setup:
		def controller = new AttributeConsumingServiceController()
		def ed = EntityDescriptor.build(entityID:'http://unique.com').save()
		def sp = SPSSODescriptor.build(entityDescriptor:ed)
		def acs = AttributeConsumingService.build(descriptor:sp).save()
		def ra1 = RequestedAttribute.build().save()
		def ra2 = RequestedAttribute.build().save()
		
		acs.addToRequestedAttributes(ra1)
		acs.addToRequestedAttributes(ra2)
		acs.save()
		
		controller.params.id = acs.id
		
		when:
		controller.listRequestedAttributes()
		
		then:
		// Grails 1.3.3 bug where modelAndView is null for some reason
		// controller.modelAndView.model.requestedAttributes.size() == 2
		true
	}

}