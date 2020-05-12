package aaf.fr.foundation

import grails.test.spock.*
import aaf.fr.workflow.*
import aaf.fr.identity.Subject

class ServiceProviderServiceSpec extends IntegrationSpec {
  
  def cryptoService
  def savedMetaClasses
  def workflowProcessService
    def entityDescriptorService
  def serviceProviderService
  def params
  
  def setup () {
    savedMetaClasses = [:]
    
    SpecHelpers.registerMetaClass(WorkflowProcessService, savedMetaClasses)
    workflowProcessService.metaClass = WorkflowProcessService.metaClass
    
    def user = Subject.build()
    SpecHelpers.setupShiroEnv(user)
    
    params = [:]
  }
  
  def cleanup() {
    SpecHelpers.resetMetaClasses(savedMetaClasses)
    workflowProcessService.metaClass = WorkflowProcessService.metaClass
  }
  
  def setupBindings() {
    def soap = new SamlURI(type:SamlURIType.ProtocolBinding, uri:'urn:oasis:names:tc:SAML:2.0:bindings:SOAP', description:'').save()
    def httpRedirect = new SamlURI(type:SamlURIType.ProtocolBinding, uri:'urn:oasis:names:tc:SAML:2.0:bindings:HTTP-Redirect', description:'').save()
    def httpPost = new SamlURI(type:SamlURIType.ProtocolBinding, uri:'urn:oasis:names:tc:SAML:2.0:bindings:HTTP-POST', description:'').save()
    def httpArtifact = new SamlURI(type:SamlURIType.ProtocolBinding, uri:'urn:oasis:names:tc:SAML:2.0:bindings:HTTP-Artifact', description:'').save()

    def trans = new SamlURI(type:SamlURIType.NameIdentifierFormat, uri:'urn:oasis:names:tc:SAML:2.0:nameid-format:transient', description:'Indicates that the content of the element is an identifier with transient semantics and SHOULD be treated as an opaque and temporary value by the relying party.').save()
  }
  
  def setupCrypto() {
    def ca = new File('./test/integration/data/demoCA/cacertminimal.pem').text
    def caCert = new CACertificate(data:ca)
    def caKeyInfo = new CAKeyInfo(certificate:caCert)
    caKeyInfo.save()
  }
  
  def String loadPK() {
    new File('./test/integration/data/newcertminimal.pem').text
  }
  
  def "Create succeeds when valid initial SPSSODescriptor data is provided (without existing EntityDescriptor or contact)"() {
    setup:
    setupBindings()
    setupCrypto()

    def saml2Prot = SamlURI.build(uri:'urn:oasis:names:tc:SAML:2.0:protocol')
    def organization = Organization.build()
    def attr1 = AttributeBase.build()
    def attr2 = AttributeBase.build()
    def nameID1 = SamlURI.build()
    def nameID2 = SamlURI.build()
    def pk = loadPK()
    def ct = ContactType.build()

    params.organization = [id: organization.id]
    params.active = true
    params.entity = [identifier:"https://service.test.com"]
    params.sigcert = pk
    params.sp = [displayName:"test service name", description:"test desc", attributes:[(attr1.id):[requested: 'on', reasoning:'reason for request', required:'on'], (attr2.id):[requested: 'on', reasoning:'reason for request2', required:'off']], crypto:[sig: true, enc:true],
            acs:[ post:'https://service.test.com/Shibboleth.sso/SAML2/POST', 'post-index':1, artifact:'https://service.test.com/Shibboleth.sso/SAML2/Artifact', 'artifact-index':2 ], 
            slo:[post:'https://service.test.com/Shibboleth.sso/SLO/Post', soap:'https://service.test.com/Shibboleth.sso/SLO/SOAP', redirect:'https://service.test.com/Shibboleth.sso/SLO/Redirect', artifact:'https://service.test.com/Shibboleth.sso/SLO/Artifact'] ] 
                
    params.contact = [givenName:"SP", surname:"Contact", email:"spcontact@spstest.com", type:ct.name]
    
    def wfProcessName, wfDescription, wfPriority, wfParams
    
    when:
    WorkflowProcessService.metaClass.initiate =  { String processName, String instanceDescription, ProcessPriority priority, Map params ->
      wfProcessName = processName
      wfDescription = instanceDescription
      wfPriority = priority
      wfParams = params
      [true, [:]]
    }
    WorkflowProcessService.metaClass.run = { def processInstance -> }
    def (created, ret) = serviceProviderService.create(params)
    
    then:
        created
    
    ret.serviceProvider.displayName == "test service name"
    ret.serviceProvider.description == "test desc"
    ret.serviceProvider.protocolSupportEnumerations.size() == 1
    ret.serviceProvider.nameIDFormats.size() == 1
    ret.serviceProvider.attributeConsumingServices.size() == 1
    def acs = ret.serviceProvider.attributeConsumingServices.toList().get(0)
    acs.requestedAttributes.size() == 2
    acs.requestedAttributes.each {
      if(it.base.id == attr1.id) {
        it.isRequired == true
        it.reasoning == "reason for request"
      }
      if(it.base.id == attr2.id) {
        it.isRequired == false
        it.reasoning == "reason for request2"
      }
    }
    ret.serviceProvider.assertionConsumerServices.size() == 2
    ret.serviceProvider.singleLogoutServices.size() == 4
    ret.serviceProvider.contacts.size() == 1
    
    wfProcessName == "spssodescriptor_create"
    wfPriority == ProcessPriority.MEDIUM
    wfParams.size() == 4
    wfParams.serviceProvider == "${ret.serviceProvider.id}"
    wfParams.organization == "${ret.organization.id}"
    
    ret.httpPostACS.location == "https://service.test.com/Shibboleth.sso/SAML2/POST"
    ret.httpArtifactACS.location == "https://service.test.com/Shibboleth.sso/SAML2/Artifact"
    ret.sloArtifact.location ==  "https://service.test.com/Shibboleth.sso/SLO/Artifact"
    ret.sloRedirect.location ==  "https://service.test.com/Shibboleth.sso/SLO/Redirect"
    ret.sloSOAP.location ==  "https://service.test.com/Shibboleth.sso/SLO/SOAP"
    ret.sloPost.location == "https://service.test.com/Shibboleth.sso/SLO/Post"
  }
  
  def "Create succeeds when valid initial SPSSODescriptor data is provided (with existing EntityDescriptor and contact)"() {
    setup:
    setupBindings()
    setupCrypto()

    def saml2Prot = SamlURI.build(uri:'urn:oasis:names:tc:SAML:2.0:protocol').save()
    def organization = Organization.build().save()
    def ed = EntityDescriptor.build(organization: organization).save()
    def attr1 = AttributeBase.build().save()
    def attr2 = AttributeBase.build().save()
    def nameID1 = SamlURI.build().save()
    def nameID2 = SamlURI.build().save()
    def pk = loadPK()
    def ct = ContactType.build().save()
    def contact = Contact.build().save()

    params.organization = [id: organization.id]
    params.active = true
    params.entity = [id: ed.id]
    params.sigcert = pk
    params.sp = [displayName:"test service name", description:"test desc", attributes:[(attr1.id):[requested: 'on', reasoning:'reason for request', required:'on'], (attr2.id):[requested: 'on', reasoning:'reason for request2', required:'off']], crypto:[sig: true, enc:true],
            acs:[post:'https://service.test.com/Shibboleth.sso/SAML2/POST', 'post-index':1, artifact:'https://service.test.com/Shibboleth.sso/SAML2/Artifact', 'artifact-index':2 ],
            slo:[post:'https://service.test.com/Shibboleth.sso/SLO/Post', soap:'https://service.test.com/Shibboleth.sso/SLO/SOAP', redirect:'https://service.test.com/Shibboleth.sso/SLO/Redirect', artifact:'https://service.test.com/Shibboleth.sso/SLO/Artifact'] ] 
                
    params.contact = [email:contact.email, type:ct.name]
    
    def wfProcessName, wfDescription, wfPriority, wfParams
    
    when:
    WorkflowProcessService.metaClass.initiate =  { String processName, String instanceDescription, ProcessPriority priority, Map params ->
      wfProcessName = processName
      wfDescription = instanceDescription
      wfPriority = priority
      wfParams = params
      [true, [:]]
    }
    WorkflowProcessService.metaClass.run = { def processInstance -> }
    def (created, ret) = serviceProviderService.create(params)
    
    then:
        created
            
    ret.serviceProvider.displayName == "test service name"
    ret.serviceProvider.description == "test desc"
    ret.serviceProvider.protocolSupportEnumerations.size() == 1
    ret.serviceProvider.nameIDFormats.size() == 1
    ret.serviceProvider.attributeConsumingServices.size() == 1
    def acs = ret.serviceProvider.attributeConsumingServices.toList().get(0)
    acs.requestedAttributes.size() == 2
    acs.requestedAttributes.each {
      if(it.base.id == attr1.id) {
        it.isRequired == true
        it.reasoning == "reason for request"
      }
      if(it.base.id == attr2.id) {
        it.isRequired == false
        it.reasoning == "reason for request2"
      }
    }
    ret.serviceProvider.assertionConsumerServices.size() == 2
    ret.serviceProvider.singleLogoutServices.size() == 4
    ret.serviceProvider.contacts.size() == 1
    
    wfProcessName == "spssodescriptor_create"
    wfPriority == ProcessPriority.MEDIUM
    wfParams.size() == 4
    wfParams.serviceProvider == "${ret.serviceProvider.id}"
    wfParams.organization == "${ret.organization.id}"
    
    ret.httpPostACS.location == "https://service.test.com/Shibboleth.sso/SAML2/POST"
    ret.httpArtifactACS.location == "https://service.test.com/Shibboleth.sso/SAML2/Artifact"
    ret.sloArtifact.location ==  "https://service.test.com/Shibboleth.sso/SLO/Artifact"
    ret.sloRedirect.location ==  "https://service.test.com/Shibboleth.sso/SLO/Redirect"
    ret.sloSOAP.location ==  "https://service.test.com/Shibboleth.sso/SLO/SOAP"
    ret.sloPost.location == "https://service.test.com/Shibboleth.sso/SLO/Post"
  }
  
  def "Create succeeds when valid initial SPSSODescriptor data is provided even though not all known SLO endpoints are supplied"() {
    setup:
    setupBindings()
    setupCrypto()

    def saml2Prot = SamlURI.build(uri:'urn:oasis:names:tc:SAML:2.0:protocol').save()
    def organization = Organization.build().save()
    def attr1 = AttributeBase.build().save()
    def attr2 = AttributeBase.build().save()
    def nameID1 = SamlURI.build().save()
    def nameID2 = SamlURI.build().save()
    def pk = loadPK()
    def ct = ContactType.build().save()

    params.organization = [id: organization.id]
    params.active = true
    params.entity = [identifier:"https://service2.test.com"]
    params.sigcert = pk
    params.sp = [displayName:"test service name", description:"test desc", attributes:[(attr1.id):[requested: 'on', reasoning:'reason for request', required:'on'], (attr2.id):[requested: 'on', reasoning:'reason for request2', required:'off']], crypto:[sig: true, enc:true],
            acs:[post:'https://service.test.com/Shibboleth.sso/SAML2/POST', 'post-index':1, artifact:'https://service.test.com/Shibboleth.sso/SAML2/Artifact', 'artifact-index':2 ],
            slo:[post:'https://service.test.com/Shibboleth.sso/SLO/Post', redirect:'https://service.test.com/Shibboleth.sso/SLO/Redirect' ] ]
                
    params.contact = [givenName:"Bradley", surname:"Beddoes", email:"bradleybeddoes@intient.com", type:ct.name]
    
    def wfProcessName, wfDescription, wfPriority, wfParams
    
    when:
    WorkflowProcessService.metaClass.initiate =  { String processName, String instanceDescription, ProcessPriority priority, Map params ->
      wfProcessName = processName
      wfDescription = instanceDescription
      wfPriority = priority
      wfParams = params
      [true, [:]]
    }
    WorkflowProcessService.metaClass.run = { def processInstance -> }
    def (created, ret) = serviceProviderService.create(params)
    
    then:
    created
    
    ret.serviceProvider.displayName == "test service name"
    ret.serviceProvider.description == "test desc"
    ret.serviceProvider.protocolSupportEnumerations.size() == 1
    ret.serviceProvider.nameIDFormats.size() == 1
    ret.serviceProvider.attributeConsumingServices.size() == 1
    def acs = ret.serviceProvider.attributeConsumingServices.toList().get(0)
    acs.requestedAttributes.size() == 2
    acs.requestedAttributes.each {
      if(it.base.id == attr1.id) {
        it.isRequired == true
        it.reasoning == "reason for request"
      }
      if(it.base.id == attr2.id) {
        it.isRequired == false
        it.reasoning == "reason for request2"
      }
    }
    ret.serviceProvider.assertionConsumerServices.size() == 2
    ret.serviceProvider.singleLogoutServices.size() == 2
    ret.serviceProvider.contacts.size() == 1
    
    wfProcessName == "spssodescriptor_create"
    wfPriority == ProcessPriority.MEDIUM
    wfParams.size() == 4
    [true, [:]]
    wfParams.organization == "${ret.organization.id}"
    
    ret.httpPostACS.location == "https://service.test.com/Shibboleth.sso/SAML2/POST"
    ret.httpArtifactACS.location == "https://service.test.com/Shibboleth.sso/SAML2/Artifact"
    ret.sloArtifact == null
    ret.sloRedirect.location ==  "https://service.test.com/Shibboleth.sso/SLO/Redirect"
    ret.sloSOAP == null
    ret.sloPost.location == "https://service.test.com/Shibboleth.sso/SLO/Post"
  }
  
  def "Create fails if both required assertion consumer endpoint types aren't supplied"() {
    setup:
    setupBindings()
    setupCrypto()

    def saml2Prot = SamlURI.build(uri:'urn:oasis:names:tc:SAML:2.0:protocol').save()
    def organization = Organization.build().save()
    def attr1 = AttributeBase.build().save()
    def attr2 = AttributeBase.build().save()
    def nameID1 = SamlURI.build().save()
    def nameID2 = SamlURI.build().save()
    def pk = loadPK()
    def ct = ContactType.build().save()

    params.organization = [id: organization.id]
    params.active = true
    params.entity = [identifier:"https://service.test.com"]
    params.sigcert = pk
    params.sp = [displayName:"test service name", description:"test desc", attributes:[(attr1.id):[requested: 'on', reasoning:'reason for request', required:'on'], (attr2.id):[requested: 'on', reasoning:'reason for request2', required:'off']], crypto:[sig: true, enc:true],
            acs:[ artifact:'https://service.test.com/Shibboleth.sso/SAML2/Artifact'] , 
            slo:[post:'https://service.test.com/Shibboleth.sso/SLO/Post', soap:'https://service.test.com/Shibboleth.sso/SLO/SOAP', redirect:'https://service.test.com/Shibboleth.sso/SLO/Redirect', artifact:'https://service.test.com/Shibboleth.sso/SLO/Artifact'] ] 
                
    params.contact = [givenName:"Bradley", surname:"Beddoes", email:"bradleybeddoes@intient.com", type:ct.name]
    
    def wfProcessName, wfDescription, wfPriority, wfParams
    
    when:
    WorkflowProcessService.metaClass.initiate =  { String processName, String instanceDescription, ProcessPriority priority, Map params ->
      wfProcessName = processName
      wfDescription = instanceDescription
      wfPriority = priority
      wfParams = params
      [true, [:]]
    }
    WorkflowProcessService.metaClass.run = { def processInstance -> }
    def (created, ret) = serviceProviderService.create(params)
    
    then:
        !created
    
    ret.serviceProvider.displayName == "test service name"
    ret.serviceProvider.description == "test desc"
    ret.serviceProvider.protocolSupportEnumerations.size() == 1
    ret.serviceProvider.nameIDFormats.size() == 1
    ret.serviceProvider.attributeConsumingServices.size() == 1
    def acs = ret.serviceProvider.attributeConsumingServices.toList().get(0)
    acs.requestedAttributes.size() == 2
    acs.requestedAttributes.each {
      if(it.base.id == attr1.id) {
        it.isRequired == true
        it.reasoning == "reason for request"
      }
      if(it.base.id == attr2.id) {
        it.isRequired == false
        it.reasoning == "reason for request2"
      }
    }
    ret.serviceProvider.assertionConsumerServices.size() == 2
    ret.serviceProvider.singleLogoutServices.size() == 4
    ret.serviceProvider.contacts.size() == 1
    
    ret.httpPostACS.hasErrors() == true
    wfProcessName == null
    
    ret.httpPostACS.location == null
    ret.httpArtifactACS.location == "https://service.test.com/Shibboleth.sso/SAML2/Artifact"
    ret.sloArtifact.location ==  "https://service.test.com/Shibboleth.sso/SLO/Artifact"
    ret.sloRedirect.location ==  "https://service.test.com/Shibboleth.sso/SLO/Redirect"
    ret.sloSOAP.location ==  "https://service.test.com/Shibboleth.sso/SLO/SOAP"
    ret.sloPost.location == "https://service.test.com/Shibboleth.sso/SLO/Post"
  }
  
  def "Create fails when reasoning for attribute request isn't supplied"() {
    setup:
    setupBindings()
    setupCrypto()

    def saml2Prot = SamlURI.build(uri:'urn:oasis:names:tc:SAML:2.0:protocol').save()
    def organization = Organization.build().save()
    def attr1 = AttributeBase.build().save()
    def attr2 = AttributeBase.build().save()
    def nameID1 = SamlURI.build().save()
    def nameID2 = SamlURI.build().save()
    def pk = loadPK()
    def ct = ContactType.build().save()

    params.organization = [id: organization.id]
    params.active = true
    params.entity = [identifier:"https://service.test.com"]
    params.sigcert = pk
    params.sp = [displayName:"test service name", description:"test desc", attributes:[(attr1.id):[requested: 'on', required:'on'], (attr2.id):[requested: 'on', reasoning:'', required:'off']], crypto:[sig: true, enc:true],
            acs:[ post:'https://service.test.com/Shibboleth.sso/SAML2/POST', artifact:'https://service.test.com/Shibboleth.sso/SAML2/Artifact'] , 
            slo:[post:'https://service.test.com/Shibboleth.sso/SLO/Post', soap:'https://service.test.com/Shibboleth.sso/SLO/SOAP', redirect:'https://service.test.com/Shibboleth.sso/SLO/Redirect', artifact:'https://service.test.com/Shibboleth.sso/SLO/Artifact'] ] 
                
    params.contact = [givenName:"Bradley", surname:"Beddoes", email:"bradleybeddoes@intient.com", type:ct.name]
    
    def wfProcessName, wfDescription, wfPriority, wfParams
    
    when:
    WorkflowProcessService.metaClass.initiate =  { String processName, String instanceDescription, ProcessPriority priority, Map params ->
      wfProcessName = processName
      wfDescription = instanceDescription
      wfPriority = priority
      wfParams = params
      [true, [:]]
    }
    WorkflowProcessService.metaClass.run = { def processInstance -> }
    def (created, ret) = serviceProviderService.create(params)
    
    then:
    !created
    
    ret.serviceProvider.displayName == "test service name"
    ret.serviceProvider.description == "test desc"
    ret.serviceProvider.protocolSupportEnumerations.size() == 1
    ret.serviceProvider.nameIDFormats.size() == 1
    ret.serviceProvider.attributeConsumingServices.size() == 1
    def acs = ret.serviceProvider.attributeConsumingServices.toList().get(0)
    acs.requestedAttributes.size() == 2
    acs.requestedAttributes.each {
      it.hasErrors() == true
      if(it.base.id == attr1.id) {
        it.isRequired == true
        it.reasoning == null
      }
      if(it.base.id == attr2.id) {
        it.isRequired == false
        it.reasoning == ''
      }
    }
    ret.serviceProvider.assertionConsumerServices.size() == 2
    ret.serviceProvider.singleLogoutServices.size() == 4
    ret.serviceProvider.contacts.size() == 1
    
    wfProcessName == null
    
    ret.httpPostACS.location == "https://service.test.com/Shibboleth.sso/SAML2/POST"
    ret.httpArtifactACS.location == "https://service.test.com/Shibboleth.sso/SAML2/Artifact"
    ret.sloArtifact.location ==  "https://service.test.com/Shibboleth.sso/SLO/Artifact"
    ret.sloRedirect.location ==  "https://service.test.com/Shibboleth.sso/SLO/Redirect"
    ret.sloSOAP.location ==  "https://service.test.com/Shibboleth.sso/SLO/SOAP"
    ret.sloPost.location == "https://service.test.com/Shibboleth.sso/SLO/Post"
  }
  
  def "Create fails when valid contact details aren't provided"() {
    setup:
    setupBindings()
    setupCrypto()

    def saml2Prot = SamlURI.build(uri:'urn:oasis:names:tc:SAML:2.0:protocol').save()
    def organization = Organization.build().save()
    def attr1 = AttributeBase.build().save()
    def attr2 = AttributeBase.build().save()
    def nameID1 = SamlURI.build().save()
    def nameID2 = SamlURI.build().save()
    def pk = loadPK()
    def ct = ContactType.build().save()

    params.organization = [id: organization.id]
    params.active = true
    params.entity = [identifier:"https://service.test.com"]
    params.sigcert = pk
    params.sp = [displayName:"test service name", description:"test desc", attributes:[(attr1.id):[requested: 'on', reasoning:'reason for request', required:'on'], (attr2.id):[requested: 'on', reasoning:'reason for request2', required:'off']], crypto:[sig: true, enc:true],
            acs:[ post:'https://service.test.com/Shibboleth.sso/SAML2/POST', artifact:'https://service.test.com/Shibboleth.sso/SAML2/Artifact'] , 
            slo:[post:'https://service.test.com/Shibboleth.sso/SLO/Post', soap:'https://service.test.com/Shibboleth.sso/SLO/SOAP', redirect:'https://service.test.com/Shibboleth.sso/SLO/Redirect', artifact:'https://service.test.com/Shibboleth.sso/SLO/Artifact'] ] 
    params.contact = [givenName:"Fred", surname:"Bloggs", email:"test.com", type:ct.name]
    
    def wfProcessName, wfDescription, wfPriority, wfParams
    
    when:
    WorkflowProcessService.metaClass.initiate =  { String processName, String instanceDescription, ProcessPriority priority, Map params ->
      wfProcessName = processName
      wfDescription = instanceDescription
      wfPriority = priority
      wfParams = params
      [true, [:]]
    }
    WorkflowProcessService.metaClass.run = { def processInstance -> }
    def (created, ret) = serviceProviderService.create(params)
    
    then:
    !created
    
    ret.serviceProvider.displayName == "test service name"
    ret.serviceProvider.description == "test desc"
    ret.serviceProvider.protocolSupportEnumerations.size() == 1
    ret.serviceProvider.nameIDFormats.size() == 1
    ret.serviceProvider.attributeConsumingServices.size() == 1
    def acs = ret.serviceProvider.attributeConsumingServices.toList().get(0)
    acs.requestedAttributes.size() == 2
    ret.serviceProvider.assertionConsumerServices.size() == 2
    ret.serviceProvider.singleLogoutServices.size() == 4
    ret.serviceProvider.contacts.size() == 1
    ret.contact.hasErrors() == true
    
    wfProcessName == null
    
    ret.httpPostACS.location == "https://service.test.com/Shibboleth.sso/SAML2/POST"
    ret.httpArtifactACS.location == "https://service.test.com/Shibboleth.sso/SAML2/Artifact"
    ret.sloArtifact.location ==  "https://service.test.com/Shibboleth.sso/SLO/Artifact"
    ret.sloRedirect.location ==  "https://service.test.com/Shibboleth.sso/SLO/Redirect"
    ret.sloSOAP.location ==  "https://service.test.com/Shibboleth.sso/SLO/SOAP"
    ret.sloPost.location == "https://service.test.com/Shibboleth.sso/SLO/Post"
  }
  
  def "Create fails when invalid description"() {
    setup:
    setupBindings()
    setupCrypto()

    def saml2Prot = SamlURI.build(uri:'urn:oasis:names:tc:SAML:2.0:protocol').save()
    def organization = Organization.build().save()
    def attr1 = AttributeBase.build().save()
    def attr2 = AttributeBase.build().save()
    def nameID1 = SamlURI.build().save()
    def nameID2 = SamlURI.build().save()
    def pk = loadPK()
    def ct = ContactType.build().save()

    params.organization = [id: organization.id]
    params.active = true
    params.entity = [identifier:"https://service.test.com"]
    params.sigcert = pk
    params.sp = [displayName:"test service name", description: null, attributes:[(attr1.id):[requested: 'on', reasoning:'reason for request', required:'on'], (attr2.id):[requested: 'on', reasoning:'reason for request2', required:'off']], crypto:[sig: true, enc:true],
            acs:[ post:'https://service.test.com/Shibboleth.sso/SAML2/POST', artifact:'https://service.test.com/Shibboleth.sso/SAML2/Artifact'] , 
            slo:[post:'https://service.test.com/Shibboleth.sso/SLO/Post', soap:'https://service.test.com/Shibboleth.sso/SLO/SOAP', redirect:'https://service.test.com/Shibboleth.sso/SLO/Redirect', artifact:'https://service.test.com/Shibboleth.sso/SLO/Artifact'] ] 
                
    params.contact = [givenName:"Bradley", surname:"Beddoes", email:"bradleybeddoes@intient.com", type:ct.name]
    
    def wfProcessName, wfDescription, wfPriority, wfParams
    
    when:
    WorkflowProcessService.metaClass.initiate =  { String processName, String instanceDescription, ProcessPriority priority, Map params ->
      wfProcessName = processName
      wfDescription = instanceDescription
      wfPriority = priority
      wfParams = params
      [true, [:]]
    }
    WorkflowProcessService.metaClass.run = { def processInstance -> }
    def (created, ret) = serviceProviderService.create(params)
    
    then:
    !created

        !ret.serviceProvider.validate()
    ret.serviceProvider.displayName == "test service name"
    ret.serviceProvider.description == null
    ret.serviceProvider.hasErrors() == true
    ret.serviceProvider.protocolSupportEnumerations.size() == 1
    ret.serviceProvider.nameIDFormats.size() == 1
    ret.serviceProvider.attributeConsumingServices.size() == 1
    def acs = ret.serviceProvider.attributeConsumingServices.toList().get(0)
    acs.requestedAttributes.size() == 2
    ret.serviceProvider.assertionConsumerServices.size() == 2
    ret.serviceProvider.singleLogoutServices.size() == 4
    ret.serviceProvider.contacts.size() == 1
    
    wfProcessName == null
    
    ret.httpPostACS.location == "https://service.test.com/Shibboleth.sso/SAML2/POST"
    ret.httpArtifactACS.location == "https://service.test.com/Shibboleth.sso/SAML2/Artifact"
    ret.sloArtifact.location ==  "https://service.test.com/Shibboleth.sso/SLO/Artifact"
    ret.sloRedirect.location ==  "https://service.test.com/Shibboleth.sso/SLO/Redirect"
    ret.sloSOAP.location ==  "https://service.test.com/Shibboleth.sso/SLO/SOAP"
    ret.sloPost.location == "https://service.test.com/Shibboleth.sso/SLO/Post"
  }
  
  def "Create fails when invalid display name"() {
    setup:
    setupBindings()
    setupCrypto()

    def saml2Prot = SamlURI.build(uri:'urn:oasis:names:tc:SAML:2.0:protocol').save()
    def organization = Organization.build().save()
    def attr1 = AttributeBase.build().save()
    def attr2 = AttributeBase.build().save()
    def nameID1 = SamlURI.build().save()
    def nameID2 = SamlURI.build().save()
    def pk = loadPK()
    def ct = ContactType.build().save()

    params.organization = [id: organization.id]
    params.active = true
    params.entity = [identifier:"https://service.test.com"]
    params.sigcert = pk
    params.sp = [description:"test desc", attributes:[(attr1.id):[requested: 'on', reasoning:'reason for request', required:'on'], (attr2.id):[requested: 'on', reasoning:'reason for request2', required:'off']], crypto:[sig: true, enc:true],
            acs:[ post:'https://service.test.com/Shibboleth.sso/SAML2/POST', artifact:'https://service.test.com/Shibboleth.sso/SAML2/Artifact'] , 
            slo:[post:'https://service.test.com/Shibboleth.sso/SLO/Post', soap:'https://service.test.com/Shibboleth.sso/SLO/SOAP', redirect:'https://service.test.com/Shibboleth.sso/SLO/Redirect', artifact:'https://service.test.com/Shibboleth.sso/SLO/Artifact'] ] 
                
    params.contact = [givenName:"Bradley", surname:"Beddoes", email:"bradleybeddoes@intient.com", type:ct.name]
    
    def wfProcessName, wfDescription, wfPriority, wfParams
    
    when:
    WorkflowProcessService.metaClass.initiate =  { String processName, String instanceDescription, ProcessPriority priority, Map params ->
      wfProcessName = processName
      wfDescription = instanceDescription
      wfPriority = priority
      wfParams = params
      [true, [:]]
    }
    WorkflowProcessService.metaClass.run = { def processInstance -> }
    def (created, ret) = serviceProviderService.create(params)
    
    then:
    !created

    !ret.serviceProvider.validate()
    ret.serviceProvider.displayName == null
    ret.serviceProvider.hasErrors() == true
    ret.serviceProvider.description == "test desc"
    ret.serviceProvider.protocolSupportEnumerations.size() == 1
    ret.serviceProvider.nameIDFormats.size() == 1
    ret.serviceProvider.attributeConsumingServices.size() == 1
    def acs = ret.serviceProvider.attributeConsumingServices.toList().get(0)
    acs.requestedAttributes.size() == 2
    ret.serviceProvider.assertionConsumerServices.size() == 2
    ret.serviceProvider.singleLogoutServices.size() == 4
    ret.serviceProvider.contacts.size() == 1
    
    wfProcessName == null
    
    ret.httpPostACS.location == "https://service.test.com/Shibboleth.sso/SAML2/POST"
    ret.httpArtifactACS.location == "https://service.test.com/Shibboleth.sso/SAML2/Artifact"
    ret.sloArtifact.location ==  "https://service.test.com/Shibboleth.sso/SLO/Artifact"
    ret.sloRedirect.location ==  "https://service.test.com/Shibboleth.sso/SLO/Redirect"
    ret.sloSOAP.location ==  "https://service.test.com/Shibboleth.sso/SLO/SOAP"
    ret.sloPost.location == "https://service.test.com/Shibboleth.sso/SLO/Post"
  }
  
  def "Attempt to update non existant service provider fails"() {
    setup:
    params.id = 1
    
    when: 
    def (updated, serviceProvider_) = serviceProviderService.update(params)
    
    then:
    !updated
    serviceProvider_ == null
  }
  
  def "Updating an existing service provider with valid changed content succeeds"() {
    setup:
    def orgType = OrganizationType.build()
    def organization = Organization.build(primary: orgType).save()
    def ed = EntityDescriptor.build(organization: organization).save()
    def sd = ServiceDescription.build(connectURL: "http://connecturl.com", furtherInfo:"this is further info")
    def sp = SPSSODescriptor.build(entityDescriptor:ed, serviceDescription:sd).save()
    params.id = sp.id
    params.sp = [displayName:"new displayName", description:"new description"]
    params.sp.servicedescription = [connecturl: "http://newconnecturl.com", furtherinfo:"this is new further info"]
    
    when:
    def (updated, serviceProvider) = serviceProviderService.update(params)
    
    then:
    updated
    serviceProvider.displayName == "new displayName"
    serviceProvider.description == "new description"
    serviceProvider.serviceDescription.connectURL == "http://newconnecturl.com"
    serviceProvider.serviceDescription.furtherInfo == "this is new further info"
  }

  def "Updating an existing service provider with invalid changed content fails"() {
    setup:
    def orgType = OrganizationType.build()
    def organization = Organization.build(primary: orgType).save()
    def ed = EntityDescriptor.build(organization: organization).save()
    def sd = ServiceDescription.build(connectURL: "http://connecturl.com", furtherInfo:"this is further info")
    def sp = SPSSODescriptor.build(entityDescriptor:ed, serviceDescription:sd).save()
    params.id = sp.id
    params.sp = [displayName:"", description:"new description"]
    params.sp.servicedescription = [connecturl: "http://newconnecturl.com", furtherinfo:"this is new further info"]
    
    when:
    def (updated, serviceProvider) = serviceProviderService.update(params)
    
    then:
    !updated

        !serviceProvider.validate()
    serviceProvider.hasErrors()
    serviceProvider.displayName == ""
    serviceProvider.description == "new description"
    serviceProvider.serviceDescription.connectURL == "http://newconnecturl.com"
    serviceProvider.serviceDescription.furtherInfo == "this is new further info"
  }
}
