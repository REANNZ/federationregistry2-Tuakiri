package aaf.fr.foundation

import grails.plugin.spock.*
import aaf.fr.workflow.*
import aaf.fr.identity.Subject

class AttributeConsumingServiceControllerSpec extends IntegrationSpec {

  def user, controller, renderMap
  
  def setup () {
    Subject.metaClass.contact = [ getId: 1 ]
    user = Subject.build()
    SpecHelpers.setupShiroEnv(user)

    AttributeConsumingServiceController.metaClass.render = { Map map ->
      renderMap = map
    }
    controller = new AttributeConsumingServiceController()
  }
  
  def cleanup() {
    user.permissions = []
  }
  
  def "Add new requested attribute with correct perms"() {
    setup:
    def acs = AttributeConsumingService.build().save()
    def attr = AttributeBase.build().save()
    
    def wfProcessName, wfDescription, wfPriority, wfParams
    
    user.permissions.add("federation:management:descriptor:${acs.descriptor.id}:attribute:add")
    
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
    wfDescription == "Approval for addition of the attribute '${attr.name}' (OID: ${attr.oid}) to the service '${acs?.descriptor?.displayName}'"
    controller.response.contentAsString == "domains.fr.foundation.attributeconsumingservice.requestedattribute.add.success"
  }
  
  def "Add new requested attribute with incorrect permissions"() {
    setup:
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
    def acs = AttributeConsumingService.build().save()
    def ra1 = RequestedAttribute.build().save()
    acs.addToRequestedAttributes(ra1)
    acs.save()
    
    def wfProcessName, wfDescription, wfPriority, wfParams

    controller.params.id = acs.id
    controller.params.attrid = ra1.base.id
    controller.params.reasoning = "I really need it!"
    
    user.permissions.add("federation:management:descriptor:${acs.descriptor.id}:attribute:add")
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
    controller.response.contentAsString == "domains.fr.foundation.attributeconsumingservice.requestedattribute.add.already.exists"
  }
  
  def "Add new requested attribute fails without reason"() {
    setup:
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
    def acs = AttributeConsumingService.build().save()
    def attr = AttributeBase.build().save()
    def ra1 = RequestedAttribute.build(base:attr).save()
    acs.addToRequestedAttributes(ra1)
    acs.save()
    
    controller.params.raid = ra1.id
    user.permissions.add("federation:management:descriptor:${acs.descriptor.id}:attribute:remove")
    
    when:
    def model = controller.removeRequestedAttribute()
    
    then:
    acs.requestedAttributes.size() == 0
    controller.response.contentAsString == "domains.fr.foundation.attributeconsumingservice.requestedattribute.remove.success"
  }
  
  def "Delete requested attribute fails with wrong permissions"() {
    setup:
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
    def ed = EntityDescriptor.build(entityID:'http://unique.com')
    def sp = SPSSODescriptor.build(entityDescriptor:ed)
    def acs = AttributeConsumingService.build(descriptor:sp)
    def ra1 = RequestedAttribute.build()
    def ra2 = RequestedAttribute.build()
    
    acs.addToRequestedAttributes(ra1)
    acs.addToRequestedAttributes(ra2)
    acs.save()
    
    controller.params.id = acs.id
        controller.params.containerID = 'ra'
    
    when:
    controller.listRequestedAttributes()
    
    then:
    renderMap.model.requestedAttributes.size() == 2
        renderMap.template == "/templates/acs/listrequestedattributes"
  }

}
