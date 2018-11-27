package aaf.fr.foundation

import grails.plugin.spock.*
import aaf.fr.workflow.*
import aaf.fr.identity.Subject

class IdentityProviderServiceSpec extends IntegrationSpec {

  def cryptoService
  def savedMetaClasses
  def workflowProcessService
  def entityDescriptorService
  def IdentityProviderService
  def params, user, svedMetaClasses

  def setup () {
    params = [:]

    user = Subject.build()
    SpecHelpers.setupShiroEnv(user)

    savedMetaClasses = [:]

    SpecHelpers.registerMetaClass(WorkflowProcessService, savedMetaClasses)
    workflowProcessService.metaClass = WorkflowProcessService.metaClass
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
    new File('./test/integration/data/newcertminimal.pem').text.trim()
  }

  def String loadBackPK() {
    new File('./test/integration/data/newcertminimal2.pem').text.trim()
  }

  def String loadEncPK() {
    new File('./test/integration/data/newcertminimal3.pem').text.trim()
  }

  def "Create succeeds when valid initial IDPSSODescriptor and AttributeAuthorityDescriptor data are provided (without existing EntityDescriptor or contact)"() {
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
    def ct = new ContactType(name:'technical', displayName:'Technical', description: 'Technical contacts').save()

    params.organization = [id: organization.id]
    params.active = true
    params.sigcert = pk
    params.entity = [identifier:"http://identityProvider1.test.com"]
    params.idp = [displayName:"test name", description:"test desc", scope: "test.com", attributes:[(attr1.id):'on', (attr2.id):'on'], crypto:[sig: true, enc:true], post:'http://identityProvider.test.com/SAML2/POST/SSO',
                redirect:'http://identityProvider.test.com/SAML2/Redirect/SSO', artifact:'http://identityProvider.test.com/SAML2/SOAP/ArtifactResolution', 'artifact-index':1]
    params.aa = [create: true, displayName:"test name", description:"test desc", scope:"test.com", crypto:[sig: true, enc:true], attributeservice:"http://identityProvider.test.com/SAML2/SOAP/AttributeQuery", attributes:[(attr1.id):'on', (attr2.id):'on']]
    params.contact = [givenName:"Fred", surname:"Bloggs", email:"fredbloggs@test.com", type:ct.name]

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
    def (created, ret) = IdentityProviderService.create(params)

    then:
    created

    def entityDescriptor = ret.entityDescriptor
    entityDescriptor.entityID == "http://identityProvider1.test.com"

    def identityProvider = ret.identityProvider
    identityProvider.organization == organization
    identityProvider.entityDescriptor == entityDescriptor
    identityProvider.entityDescriptor.organization == organization

    identityProvider.displayName == "test name"
    identityProvider.description == "test desc"
    identityProvider.keyDescriptors.size() == 1
    identityProvider.keyDescriptors.toList().get(0).keyInfo.certificate.data == pk

    identityProvider.singleSignOnServices.size() == 2
    def sso1 = identityProvider.singleSignOnServices.toList().get(0)
    def sso2 = identityProvider.singleSignOnServices.toList().get(1)
    if(sso1.binding == SamlURI.findByUri(SamlConstants.httpPost)) {
      sso1.location == "http://identityProvider.test.com/SAML2/POST/SSO"
      sso2.location == "http://identityProvider.test.com/SAML2/Redirect/SSO"
    }
    else {
      sso1.location == "http://identityProvider.test.com/SAML2/Redirect/SSO"
      sso2.location == "http://identityProvider.test.com/SAML2/POST/SSO"
    }

    identityProvider.artifactResolutionServices.size() == 1
    identityProvider.artifactResolutionServices.toList().get(0).location == "http://identityProvider.test.com/SAML2/SOAP/ArtifactResolution"

    identityProvider.attributes != null
    identityProvider.attributes.size() == 2

    identityProvider.nameIDFormats != null
    identityProvider.nameIDFormats.size() == 1

    identityProvider.contacts.size() == 1
    identityProvider.contacts.toList().get(0).type.name == ct.name
    identityProvider.contacts.toList().get(0).contact.givenName == "Fred"
    identityProvider.contacts.toList().get(0).contact.surname == "Bloggs"
    identityProvider.contacts.toList().get(0).contact.email == "fredbloggs@test.com"

    def attributeAuthority = ret.attributeAuthority
    attributeAuthority.organization == organization
    attributeAuthority.entityDescriptor == entityDescriptor
    attributeAuthority.collaborator == identityProvider
    identityProvider.collaborator == attributeAuthority


    attributeAuthority.attributes == null   // Metadata renders off IdP attribute set so we can maintain in one place.

    wfProcessName == "idpssodescriptor_create"

    wfPriority == ProcessPriority.MEDIUM
    wfParams.size() == 5
    wfParams.identityProvider == "${identityProvider.id}"
    wfParams.organization == "${organization.id}"
  }

  def "Create succeeds when valid initial IDPSSODescriptor and AttributeAuthorityDescriptor data are provided (without existing EntityDescriptor but with existing contact)"() {
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
    def contact = Contact.build(organization: organization)
    def ct = new ContactType(name:'technical', displayName:'Technical', description: 'Technical contacts').save()

    params.organization = [id: organization.id]
    params.active = true
    params.sigcert = pk
    params.entity = [identifier:"http://identityProvider2.test.com"]
    params.idp = [displayName:"test name", description:"test desc", scope:"test.com", attributes:[(attr1.id):'on', (attr2.id):'on'], crypto:[sig: true, enc:true], post:'http://identityProvider.test.com/SAML2/POST/SSO',
                redirect:'http://identityProvider.test.com/SAML2/Redirect/SSO', artifact:'http://identityProvider.test.com/SAML2/SOAP/ArtifactResolution', 'artifact-index':1]
    params.aa = [create: true, displayName:"test name", description:"test desc", scope:"test.com", crypto:[sig: true, enc:true],attributeservice:'http://identityProvider.test.com/SAML2/SOAP/AttributeQuery']
    params.contact = [email: contact.email, type:'technical']

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
    def (created, ret) = IdentityProviderService.create(params)

    then:
    created

    def entityDescriptor = ret.entityDescriptor
    entityDescriptor.entityID == "http://identityProvider2.test.com"

    def identityProvider = ret.identityProvider
    identityProvider.organization == organization
    identityProvider.entityDescriptor == entityDescriptor
    identityProvider.entityDescriptor.organization == organization

    identityProvider.displayName == "test name"
    identityProvider.description == "test desc"
    identityProvider.keyDescriptors.size() == 1
    identityProvider.keyDescriptors.toList().get(0).keyInfo.certificate.data == pk

    identityProvider.singleSignOnServices.size() == 2
    def sso1 = identityProvider.singleSignOnServices.toList().get(0)
    def sso2 = identityProvider.singleSignOnServices.toList().get(1)
    if(sso1.binding == SamlURI.findByUri(SamlConstants.httpPost)) {
      sso1.location == "http://identityProvider.test.com/SAML2/POST/SSO"
      sso2.location == "http://identityProvider.test.com/SAML2/Redirect/SSO"
    }
    else {
      sso1.location == "http://identityProvider.test.com/SAML2/Redirect/SSO"
      sso2.location == "http://identityProvider.test.com/SAML2/POST/SSO"
    }

    identityProvider.artifactResolutionServices.size() == 1
    identityProvider.artifactResolutionServices.toList().get(0).location == "http://identityProvider.test.com/SAML2/SOAP/ArtifactResolution"

    identityProvider.attributes != null
    identityProvider.attributes.size() == 2

    identityProvider.nameIDFormats != null
    identityProvider.nameIDFormats.size() == 1

    identityProvider.contacts.size() == 1
    identityProvider.contacts.toList().get(0).type.name == ct.name
    identityProvider.contacts.toList().get(0).contact.givenName == contact.givenName
    identityProvider.contacts.toList().get(0).contact.surname == contact.surname
    identityProvider.contacts.toList().get(0).contact.email == contact.email

    def attributeAuthority = ret.attributeAuthority
    attributeAuthority.organization == organization
    attributeAuthority.entityDescriptor == entityDescriptor
    attributeAuthority.collaborator == identityProvider
    identityProvider.collaborator == attributeAuthority

    wfProcessName == "idpssodescriptor_create"

    wfPriority == ProcessPriority.MEDIUM
    wfParams.size() == 5
    wfParams.creator == "${contact.id}"
    wfParams.identityProvider == "${identityProvider.id}"
    wfParams.organization == "${organization.id}"
  }

  def "Create succeeds when valid initial IDPSSODescriptor and AttributeAuthorityDescriptor data are provided (with existing EntityDescriptor)"() {
    setup:
    setupBindings()
    setupCrypto()

    def saml2Prot = SamlURI.build(uri:'urn:oasis:names:tc:SAML:2.0:protocol')
    def organization = Organization.build()
    def entityDescriptor = EntityDescriptor.build(organization:organization).save()
    def attr1 = AttributeBase.build()
    def attr2 = AttributeBase.build()
    def pk = loadPK()
    def contact = Contact.build(organization: organization)
    def ct = new ContactType(name:'technical', displayName:'Technical', description: 'Technical contacts').save()

    params.organization = [id: organization.id]
    params.active = true
    params.sigcert = pk
    params.entity = [id: entityDescriptor.id]
    params.idp = [displayName:"test name", description:"test desc", scope:"test.com", attributes:[(attr1.id):'on', (attr2.id):'on'], crypto:[sig: true, enc:true], post:'http://identityProvider.test.com/SAML2/POST/SSO',
                redirect:'http://identityProvider.test.com/SAML2/Redirect/SSO', artifact:'http://identityProvider.test.com/SAML2/SOAP/ArtifactResolution', 'artifact-index':1]
    params.aa = [create: true, displayName:"test name", description:"test desc", scope:"test.com", crypto:[sig: true, enc:true],attributeservice:'http://identityProvider.test.com/SAML2/SOAP/AttributeQuery']
    params.contact = [email: contact.email, type:'technical']

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
    def (created, ret) = IdentityProviderService.create(params)

    then:
    created

    def identityProvider = ret.identityProvider
    def attributeAuthority = ret.attributeAuthority

    identityProvider.organization == organization
    identityProvider.entityDescriptor == entityDescriptor
    identityProvider.entityDescriptor.organization == organization

    identityProvider.displayName == "test name"
    identityProvider.description == "test desc"
    identityProvider.active
    !identityProvider.approved
    identityProvider.keyDescriptors.size() == 1
    identityProvider.keyDescriptors.toList().get(0).keyInfo.certificate.data == pk

    identityProvider.singleSignOnServices.size() == 2
    def sso1 = identityProvider.singleSignOnServices.toList().get(0)
    def sso2 = identityProvider.singleSignOnServices.toList().get(1)
    if(sso1.binding == SamlURI.findByUri(SamlConstants.httpPost)) {
      sso1.location == "http://identityProvider.test.com/SAML2/POST/SSO"
      sso2.location == "http://identityProvider.test.com/SAML2/Redirect/SSO"
    }
    else {
      sso1.location == "http://identityProvider.test.com/SAML2/Redirect/SSO"
      sso2.location == "http://identityProvider.test.com/SAML2/POST/SSO"
    }

    identityProvider.artifactResolutionServices.size() == 1
    identityProvider.artifactResolutionServices.toList().get(0).location == "http://identityProvider.test.com/SAML2/SOAP/ArtifactResolution"

    identityProvider.attributes != null
    identityProvider.attributes.size() == 2

    identityProvider.contacts.size() == 1
    identityProvider.contacts.toList().get(0).type.name == ct.name
    identityProvider.contacts.toList().get(0).contact.givenName == contact.givenName
    identityProvider.contacts.toList().get(0).contact.surname == contact.surname
    identityProvider.contacts.toList().get(0).contact.email == contact.email

    attributeAuthority.organization == organization
    attributeAuthority.entityDescriptor == entityDescriptor
    attributeAuthority.collaborator == identityProvider
    identityProvider.collaborator == attributeAuthority
    attributeAuthority.active
    !attributeAuthority.approved

    wfProcessName == "idpssodescriptor_create"

    wfPriority == ProcessPriority.MEDIUM
    wfParams.size() == 5
    wfParams.creator == "${contact.id}"
    wfParams.identityProvider == "${identityProvider.id}"
    wfParams.organization == "${organization.id}"
  }

  def "Create adds ECP endpoint when ECP submitted in params"() {
    setup:
    setupBindings()
    setupCrypto()

    def saml2Prot = SamlURI.build(uri:'urn:oasis:names:tc:SAML:2.0:protocol')
    def organization = Organization.build()
    def entityDescriptor = EntityDescriptor.build(organization:organization).save()
    def attr1 = AttributeBase.build()
    def attr2 = AttributeBase.build()
    def pk = loadPK()
    def contact = Contact.build(organization: organization)
    def ct = new ContactType(name:'technical', displayName:'Technical', description: 'Technical contacts').save()

    params.organization = [id: organization.id]
    params.active = true
    params.sigcert = pk
    params.entity = [id: entityDescriptor.id]
    params.idp = [displayName:"test name", description:"test desc", scope:"test.com", attributes:[(attr1.id):'on', (attr2.id):'on'], crypto:[sig: true, enc:true], post:'http://identityProvider.test.com/SAML2/POST/SSO',
                redirect:'http://identityProvider.test.com/SAML2/Redirect/SSO', artifact:'http://identityProvider.test.com/SAML2/SOAP/ArtifactResolution', 'artifact-index':1,
                ecp:'http://identityProvider.test.com/idp/profile/SAML2/SOAP/ECP']
    params.aa = [create: true, displayName:"test name", description:"test desc", scope:"test.com", crypto:[sig: true, enc:true],attributeservice:'http://identityProvider.test.com/SAML2/SOAP/AttributeQuery']
    params.contact = [email: contact.email, type:'technical']

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
    def (created, ret) = IdentityProviderService.create(params)

    then:
    created

    def identityProvider = ret.identityProvider

    identityProvider.singleSignOnServices.size() == 3
    def ecp = identityProvider.singleSignOnServices.toList().get(2)
    ecp.binding == SamlURI.findByUri(SamlConstants.soap)
    ecp.location == "http://identityProvider.test.com/idp/profile/SAML2/SOAP/ECP"
  }

  def "Create fails when IDPSSODescriptor fails constraints even though a valid AttributeAuthorityDescriptor is provided"() {
    setup:
    setupBindings()
    setupCrypto()

    def saml2Prot = SamlURI.build(uri:'urn:oasis:names:tc:SAML:2.0:protocol')
    def organization = Organization.build()
    def entityDescriptor = EntityDescriptor.build(organization:organization)
    def attr1 = Attribute.build()
    def attr2 = Attribute.build()
    def pk = loadPK()
    def contact = Contact.build(organization: organization)
    def ct = new ContactType(name:'technical', displayName:'Technical', description: 'Technical contacts').save()

    params.organization = [id: organization.id]
    params.active = true
    params.sigcert = pk
    params.entity = [id: entityDescriptor.id]
    params.idp = [description:"test desc", scope:"test.com", crypto:[sig: true, enc:true], post:'http://identityProvider.test.com/SAML2/POST/SSO',
                redirect:'http://identityProvider.test.com/SAML2/Redirect/SSO', artifact:'http://identityProvider.test.com/SAML2/SOAP/ArtifactResolution', 'artifact-index':1]
    params.aa = [create: true, displayName:"test name", description:"test desc", scope:"test.com", crypto:[sig: true, enc:true], attributeservice:[uri:"http://identityProvider.test.com/SAML2/SOAP/AttributeQuery"], attributes:[1, 2]]
    params.contact = [email: contact.email, type:'technical']

    when:
    def (created, ret) = IdentityProviderService.create(params)

    then:
    !created

        def identityProvider = ret.identityProvider
        def attributeAuthority = ret.attributeAuthority

    identityProvider.organization == organization
    identityProvider.entityDescriptor == entityDescriptor
    identityProvider.entityDescriptor.organization == organization

    identityProvider.displayName == null
    identityProvider.description == "test desc"
    identityProvider.keyDescriptors.size() == 1
    identityProvider.keyDescriptors.toList().get(0).keyInfo.certificate.data == pk

    identityProvider.singleSignOnServices.size() == 2

    identityProvider.singleSignOnServices.contains(ret.httpPost)
    ret.httpPost.location == "http://identityProvider.test.com/SAML2/POST/SSO"


    identityProvider.singleSignOnServices.contains(ret.httpRedirect)
    ret.httpRedirect.location == "http://identityProvider.test.com/SAML2/Redirect/SSO"

    identityProvider.artifactResolutionServices.size() == 1

    identityProvider.artifactResolutionServices.contains(ret.soapArtifact)
    ret.soapArtifact.location == "http://identityProvider.test.com/SAML2/SOAP/ArtifactResolution"

    identityProvider.contacts.size() == 1
    identityProvider.contacts.toList().get(0).id == null


    attributeAuthority.organization == organization
    attributeAuthority.entityDescriptor == entityDescriptor
    attributeAuthority.collaborator == null

  }

  def "Create fails when IDPSSODescriptor post endpoint fails constraints"() {
    setup:
    setupBindings()
    setupCrypto()

    def saml2Prot = SamlURI.build(uri:'urn:oasis:names:tc:SAML:2.0:protocol')
    def organization = Organization.build()
    def entityDescriptor = EntityDescriptor.build(organization:organization)
    def attr1 = Attribute.build()
    def attr2 = Attribute.build()
    def pk = loadPK()
    def contact = Contact.build(organization: organization)
    def ct = new ContactType(name:'technical', displayName:'Technical', description: 'Technical contacts').save()

    params.organization = [id: organization.id]
    params.active = true
    params.sigcert = pk
    params.entity = [id: entityDescriptor.id]
    params.idp = [displayName: "test name", description:"test desc", scope:"test.com", crypto:[sig: true, enc:true], post:'/SAML2/POST/SSO',
                redirect:'http://identityProvider.test.com/SAML2/Redirect/SSO', artifact:'http://identityProvider.test.com/SAML2/SOAP/ArtifactResolution', 'artifact-index':1]
    params.aa = [create: true, displayName:"test name", description:"test desc", scope:"test.com", crypto:[sig: true, enc:true], attributeservice:[uri:"http://identityProvider.test.com/SAML2/SOAP/AttributeQuery"], attributes:[1, 2]]
    params.contact = [email: contact.email, type:'technical']

    when:
    def (created, ret) = IdentityProviderService.create(params)

    then:
    !created

        def identityProvider = ret.identityProvider
        def attributeAuthority = ret.attributeAuthority

    identityProvider.organization == organization
    identityProvider.entityDescriptor == entityDescriptor
    identityProvider.entityDescriptor.organization == organization

    identityProvider.displayName == "test name"
    identityProvider.description == "test desc"
    identityProvider.keyDescriptors.size() == 1
    identityProvider.keyDescriptors.toList().get(0).keyInfo.certificate.data == pk

    identityProvider.singleSignOnServices.size() == 2

    identityProvider.singleSignOnServices.contains(ret.httpPost)
    ret.httpPost.location == "/SAML2/POST/SSO"
    ret.httpPost.errors.getErrorCount() == 1
    ret.httpPost.errors.getFieldError('location').code == 'url.invalid'


    identityProvider.singleSignOnServices.contains(ret.httpRedirect)
    ret.httpRedirect.location == "http://identityProvider.test.com/SAML2/Redirect/SSO"

    identityProvider.artifactResolutionServices.size() == 1

    identityProvider.artifactResolutionServices.contains(ret.soapArtifact)
    ret.soapArtifact.location == "http://identityProvider.test.com/SAML2/SOAP/ArtifactResolution"


    attributeAuthority.organization == organization
    attributeAuthority.entityDescriptor == entityDescriptor
    attributeAuthority.collaborator == null

  }

  def "Create fails when IDPSSODescriptor post endpoint not supplied"() {
    setup:
    setupBindings()
    setupCrypto()

    def saml2Prot = SamlURI.build(uri:'urn:oasis:names:tc:SAML:2.0:protocol')
    def organization = Organization.build()
    def entityDescriptor = EntityDescriptor.build(organization:organization)
    def attr1 = Attribute.build()
    def attr2 = Attribute.build()
    def pk = loadPK()
    def contact = Contact.build(organization: organization)
    def ct = new ContactType(name:'technical', displayName:'Technical', description: 'Technical contacts').save()

    params.organization = [id: organization.id]
    params.active = true
    params.sigcert = pk
    params.entity = [id: entityDescriptor.id]
    params.idp = [displayName: "test name", description:"test desc", scope:"test.com", crypto:[sig: true, enc:true],
                redirect:'http://identityProvider.test.com/SAML2/Redirect/SSO', artifact:'http://identityProvider.test.com/SAML2/SOAP/ArtifactResolution', 'artifact-index':1]
    params.aa = [create: true, displayName:"test name", description:"test desc", scope:"test.com", crypto:[sig: true, enc:true], attributeservice:[uri:"http://identityProvider.test.com/SAML2/SOAP/AttributeQuery"], attributes:[1, 2]]
    params.contact = [email: contact.email, type:'technical']

    when:
    def (created, ret) = IdentityProviderService.create(params)

    then:
    !created

        def identityProvider = ret.identityProvider
        def attributeAuthority = ret.attributeAuthority

    identityProvider.organization == organization
    identityProvider.entityDescriptor == entityDescriptor
    identityProvider.entityDescriptor.organization == organization

    identityProvider.displayName == "test name"
    identityProvider.description == "test desc"
    identityProvider.keyDescriptors.size() == 1
    identityProvider.keyDescriptors.toList().get(0).keyInfo.certificate.data == pk

    identityProvider.singleSignOnServices.size() == 2

    identityProvider.singleSignOnServices.contains(ret.httpPost)
    ret.httpPost.location == null
    ret.httpPost.hasErrors()
    ret.httpPost.errors.getFieldError('location').code == 'nullable'
    identityProvider.hasErrors()
    identityProvider.errors.getFieldError('singleSignOnServices[0].location').code == 'nullable'


    identityProvider.singleSignOnServices.contains(ret.httpRedirect)
    ret.httpRedirect.location == "http://identityProvider.test.com/SAML2/Redirect/SSO"

    identityProvider.artifactResolutionServices.size() == 1

    identityProvider.artifactResolutionServices.contains(ret.soapArtifact)
    ret.soapArtifact.location == "http://identityProvider.test.com/SAML2/SOAP/ArtifactResolution"


    attributeAuthority.organization == organization
    attributeAuthority.entityDescriptor == entityDescriptor
    attributeAuthority.collaborator == null

  }

  def "Create fails when IDPSSODescriptor redirect endpoint fails constraints"() {
    setup:
    setupBindings()
    setupCrypto()

    def saml2Prot = SamlURI.build(uri:'urn:oasis:names:tc:SAML:2.0:protocol')
    def organization = Organization.build()
    def entityDescriptor = EntityDescriptor.build(organization:organization)
    def attr1 = Attribute.build()
    def attr2 = Attribute.build()
    def pk = loadPK()
    def contact = Contact.build(organization: organization)
    def ct = new ContactType(name:'technical', displayName:'Technical', description: 'Technical contacts').save()

    params.organization = [id: organization.id]
    params.active = true
    params.sigcert = pk
    params.entity = [id: entityDescriptor.id]
    params.idp = [displayName: "test name", description:"test desc", scope:"test.com", crypto:[sig: true, enc:true], post:'http://identityProvider.test.com/SAML2/POST/SSO',
                redirect:'asdfasdasdf', artifact:'http://identityProvider.test.com/SAML2/SOAP/ArtifactResolution', 'artifact-index':1]
    params.aa = [create: true, displayName:"test name", description:"test desc", scope:"test.com", crypto:[sig: true, enc:true], attributeservice:[uri:"http://identityProvider.test.com/SAML2/SOAP/AttributeQuery"], attributes:[1, 2]]
    params.contact = [email: contact.email, type:'technical']

    when:
    def (created, ret) = IdentityProviderService.create(params)

    then:
    !created

        def identityProvider = ret.identityProvider
        def attributeAuthority = ret.attributeAuthority

    identityProvider.organization == organization
    identityProvider.entityDescriptor == entityDescriptor
    identityProvider.entityDescriptor.organization == organization

    identityProvider.displayName == "test name"
    identityProvider.description == "test desc"
    identityProvider.keyDescriptors.size() == 1
    identityProvider.keyDescriptors.toList().get(0).keyInfo.certificate.data == pk

    identityProvider.singleSignOnServices.size() == 2

    identityProvider.singleSignOnServices.contains(ret.httpPost)
    ret.httpPost.location == "http://identityProvider.test.com/SAML2/POST/SSO"


    identityProvider.singleSignOnServices.contains(ret.httpRedirect)
    ret.httpRedirect.location == "asdfasdasdf"
    ret.httpRedirect.errors.getErrorCount() == 1
    ret.httpRedirect.errors.getFieldError('location').code == 'url.invalid'

    identityProvider.artifactResolutionServices.size() == 1

    identityProvider.artifactResolutionServices.contains(ret.soapArtifact)
    ret.soapArtifact.location == "http://identityProvider.test.com/SAML2/SOAP/ArtifactResolution"

    attributeAuthority.organization == organization
    attributeAuthority.entityDescriptor == entityDescriptor
    attributeAuthority.collaborator == null

  }

  def "Create fails when IDPSSODescriptor redirect endpoint not supplied"() {
    setup:
    setupBindings()
    setupCrypto()

    def saml2Prot = SamlURI.build(uri:'urn:oasis:names:tc:SAML:2.0:protocol')
    def organization = Organization.build()
    def entityDescriptor = EntityDescriptor.build(organization:organization)
    def attr1 = Attribute.build()
    def attr2 = Attribute.build()
    def pk = loadPK()
    def contact = Contact.build(organization: organization)
    def ct = new ContactType(name:'technical', displayName:'Technical', description: 'Technical contacts').save()

    params.organization = [id: organization.id]
    params.active = true
    params.sigcert = pk
    params.entity = [id: entityDescriptor.id]
    params.idp = [displayName: "test name", description:"test desc", scope:"test.com", crypto:[sig: true, enc:true], post:'http://identityProvider.test.com/SAML2/POST/SSO',
                artifact:'http://identityProvider.test.com/SAML2/SOAP/ArtifactResolution', 'artifact-index':1]
    params.aa = [create: true, displayName:"test name", description:"test desc", scope:"test.com", crypto:[sig: true, enc:true], attributeservice:[uri:"http://identityProvider.test.com/SAML2/SOAP/AttributeQuery"], attributes:[1, 2]]
    params.contact = [email: contact.email, type:'technical']

    when:
    def (created, ret) = IdentityProviderService.create(params)

    then:
    !created

        def identityProvider = ret.identityProvider
        def attributeAuthority = ret.attributeAuthority

    identityProvider.organization == organization
    identityProvider.entityDescriptor == entityDescriptor
    identityProvider.entityDescriptor.organization == organization

    identityProvider.displayName == "test name"
    identityProvider.description == "test desc"

    identityProvider.keyDescriptors.size() == 1
    identityProvider.keyDescriptors.toList().get(0).keyInfo.certificate.data == pk

    identityProvider.singleSignOnServices.size() == 2

    identityProvider.singleSignOnServices.contains(ret.httpPost)
    ret.httpPost.location == "http://identityProvider.test.com/SAML2/POST/SSO"

    identityProvider.hasErrors()
    identityProvider.singleSignOnServices.contains(ret.httpRedirect)
    ret.httpRedirect.location == null
    ret.httpRedirect.hasErrors()
    ret.httpRedirect.errors.getFieldError('location').code == 'nullable'

    identityProvider.artifactResolutionServices.size() == 1

    identityProvider.artifactResolutionServices.contains(ret.soapArtifact)
    ret.soapArtifact.location == "http://identityProvider.test.com/SAML2/SOAP/ArtifactResolution"

    attributeAuthority.organization == organization
    attributeAuthority.entityDescriptor == entityDescriptor
    attributeAuthority.collaborator == null

  }

  def "Create fails when IDPSSODescriptor artifact endpoint fails constraints"() {
    setup:
    setupBindings()
    setupCrypto()

    def saml2Prot = SamlURI.build(uri:'urn:oasis:names:tc:SAML:2.0:protocol')
    def organization = Organization.build()
    def entityDescriptor = EntityDescriptor.build(organization:organization)
    def attr1 = Attribute.build()
    def attr2 = Attribute.build()
    def pk = loadPK()
    def contact = Contact.build(organization: organization)
    def ct = new ContactType(name:'technical', displayName:'Technical', description: 'Technical contacts').save()

    params.organization = [id: organization.id]
    params.active = true
    params.sigcert = pk
    params.entity = [id: entityDescriptor.id]
    params.idp = [displayName: "test name", description:"test desc", scope:"test.com", crypto:[sig: true, enc:true], post:'http://identityProvider.test.com/SAML2/POST/SSO',
                redirect:'http://identityProvider.test.com/SAML2/Redirect/SSO', artifact:'/SAML2/SOAP/ArtifactResolution', 'artifact-index':1]
    params.aa = [create: true, displayName:"test name", description:"test desc", scope:"test.com", crypto:[sig: true, enc:true], attributeservice:[uri:"http://identityProvider.test.com/SAML2/SOAP/AttributeQuery"], attributes:[1, 2]]
    params.contact = [email: contact.email, type:'technical']

    when:
    def (created, ret) = IdentityProviderService.create(params)

    then:
    !created

        def identityProvider = ret.identityProvider
        def attributeAuthority = ret.attributeAuthority

    identityProvider.organization == organization
    identityProvider.entityDescriptor == entityDescriptor
    identityProvider.entityDescriptor.organization == organization

    identityProvider.displayName == "test name"
    identityProvider.description == "test desc"

    identityProvider.keyDescriptors.size() == 1
    identityProvider.keyDescriptors.toList().get(0).keyInfo.certificate.data == pk

    identityProvider.singleSignOnServices.size() == 2

    identityProvider.singleSignOnServices.contains(ret.httpPost)
    ret.httpPost.location == "http://identityProvider.test.com/SAML2/POST/SSO"


    identityProvider.singleSignOnServices.contains(ret.httpRedirect)
    ret.httpRedirect.location == "http://identityProvider.test.com/SAML2/Redirect/SSO"

    identityProvider.artifactResolutionServices.size() == 1

    identityProvider.artifactResolutionServices.contains(ret.soapArtifact)
    ret.soapArtifact.location == "/SAML2/SOAP/ArtifactResolution"
    ret.soapArtifact.errors.getErrorCount() == 1
    ret.soapArtifact.errors.getFieldError('location').code == 'url.invalid'


    attributeAuthority.organization == organization
    attributeAuthority.entityDescriptor == entityDescriptor
    attributeAuthority.collaborator == null

  }

  def "Create fails when IDPSSODescriptor artifact endpoint not supplied"() {
    setup:
    setupBindings()
    setupCrypto()

    def saml2Prot = SamlURI.build(uri:'urn:oasis:names:tc:SAML:2.0:protocol')
    def organization = Organization.build()
    def entityDescriptor = EntityDescriptor.build(organization:organization)
    def attr1 = Attribute.build()
    def attr2 = Attribute.build()
    def pk = loadPK()
    def contact = Contact.build(organization: organization)
    def ct = new ContactType(name:'technical', displayName:'Technical', description: 'Technical contacts').save()

    params.organization = [id: organization.id]
    params.active = true
    params.sigcert = pk
    params.entity = [id: entityDescriptor.id]
    params.idp = [displayName: "test name", description:"test desc", scope:"test.com", crypto:[sig: true, enc:true], post:'http://identityProvider.test.com/SAML2/POST/SSO',
                redirect:'http://identityProvider.test.com/SAML2/Redirect/SSO']
    params.aa = [create: true, displayName:"test name", description:"test desc", scope:"test.com", crypto:[sig: true, enc:true], attributeservice:[uri:"http://identityProvider.test.com/SAML2/SOAP/AttributeQuery"], attributes:[1, 2]]
    params.contact = [email: contact.email, type:'technical']

    when:
    def (created, ret) = IdentityProviderService.create(params)

    then:
    !created

        def identityProvider = ret.identityProvider
        def attributeAuthority = ret.attributeAuthority

    identityProvider.organization == organization
    identityProvider.entityDescriptor == entityDescriptor
    identityProvider.entityDescriptor.organization == organization

    identityProvider.displayName == "test name"
    identityProvider.description == "test desc"

    identityProvider.keyDescriptors.size() == 1
    identityProvider.keyDescriptors.toList().get(0).keyInfo.certificate.data == pk

    identityProvider.singleSignOnServices.size() == 2

    identityProvider.singleSignOnServices.contains(ret.httpPost)
    ret.httpPost.location == "http://identityProvider.test.com/SAML2/POST/SSO"


    identityProvider.singleSignOnServices.contains(ret.httpRedirect)
    ret.httpRedirect.location == "http://identityProvider.test.com/SAML2/Redirect/SSO"

    identityProvider.artifactResolutionServices.size() == 1

    identityProvider.artifactResolutionServices.contains(ret.soapArtifact)
    ret.soapArtifact.location == null
    ret.soapArtifact.hasErrors()
    ret.soapArtifact.errors.getFieldError('location').code == 'nullable'
    identityProvider.hasErrors()
    identityProvider.errors.getFieldError('artifactResolutionServices[0].location').code == 'nullable'

    attributeAuthority.organization == organization
    attributeAuthority.entityDescriptor == entityDescriptor
    attributeAuthority.collaborator == null

  }

  def "Create fails when IDPSSODescriptor ecp endpoint fails constraints"() {
    setup:
    setupBindings()
    setupCrypto()

    def saml2Prot = SamlURI.build(uri:'urn:oasis:names:tc:SAML:2.0:protocol')
    def organization = Organization.build()
    def entityDescriptor = EntityDescriptor.build(organization:organization)
    def attr1 = Attribute.build()
    def attr2 = Attribute.build()
    def pk = loadPK()
    def contact = Contact.build(organization: organization)
    def ct = new ContactType(name:'technical', displayName:'Technical', description: 'Technical contacts').save()

    params.organization = [id: organization.id]
    params.active = true
    params.sigcert = pk
    params.entity = [id: entityDescriptor.id]
    params.idp = [displayName: "test name", description:"test desc", scope:"test.com", crypto:[sig: true, enc:true], post:'http://identityProvider.test.com/SAML2/POST/SSO',
                redirect:'http://identityProvider.test.com/SAML2/Redirect/SSO', artifact:'http://identityProvider.test.com/SAML2/SOAP/ArtifactResolution', 'artifact-index':1,
                ecp: '/idp/profile/SAML2/SOAP/ECP']
    params.aa = [create: true, displayName:"test name", description:"test desc", scope:"test.com", crypto:[sig: true, enc:true], attributeservice:[uri:"http://identityProvider.test.com/SAML2/SOAP/AttributeQuery"], attributes:[1, 2]]
    params.contact = [email: contact.email, type:'technical']

    when:
    def (created, ret) = IdentityProviderService.create(params)

    then:
    !created

    def identityProvider = ret.identityProvider
    def attributeAuthority = ret.attributeAuthority

    identityProvider.singleSignOnServices.size() == 3

    identityProvider.singleSignOnServices.contains(ret.ecp)
    ret.ecp.location == "/idp/profile/SAML2/SOAP/ECP"
  }

  def "Create fails when IDPSSODescriptor crypto not supplied"() {
    setup:
    setupBindings()
    setupCrypto()

    def saml2Prot = SamlURI.build(uri:'urn:oasis:names:tc:SAML:2.0:protocol')
    def organization = Organization.build()
    def entityDescriptor = EntityDescriptor.build(organization:organization)
    def attr1 = Attribute.build()
    def attr2 = Attribute.build()
    def pk = loadPK()
    def contact = Contact.build(organization: organization)
    def ct = new ContactType(name:'technical', displayName:'Technical', description: 'Technical contacts').save()

    params.organization = [id: organization.id]
    params.active = true
    params.entity = [id: entityDescriptor.id]
    params.idp = [displayName: "test name", description:"test desc", scope:"test.com", post:'http://identityProvider.test.com/SAML2/POST/SSO',
                redirect:'http://identityProvider.test.com/SAML2/Redirect/SSO', artifact:'http://identityProvider.test.com/SAML2/SOAP/ArtifactResolution', 'artifact-index':1]
    params.aa = [create: true, displayName:"test name", description:"test desc", scope:"test.com", attributeservice:'http://identityProvider.test.com/SAML2/SOAP/AttributeQuery']
    params.contact = [email: contact.email, type:'technical']

    def wfProcessName, wfDescription, wfPriority, wfParams

    when:
    def (created, ret) = IdentityProviderService.create(params)

    then:
    !created

    def identityProvider = ret.identityProvider
    def attributeAuthority = ret.attributeAuthority

    identityProvider.organization == organization
    identityProvider.entityDescriptor == entityDescriptor
    identityProvider.entityDescriptor.organization == organization
    identityProvider.collaborator == null

    identityProvider.displayName == "test name"
    identityProvider.description == "test desc"
    identityProvider.keyDescriptors == null
    identityProvider.singleSignOnServices.size() == 2
    def sso1 = identityProvider.singleSignOnServices.toList().get(0)
    def sso2 = identityProvider.singleSignOnServices.toList().get(1)
    if(sso1.binding == SamlURI.findByUri(SamlConstants.httpPost)) {
      sso1.location == "http://identityProvider.test.com/SAML2/POST/SSO"
      sso2.location == "http://identityProvider.test.com/SAML2/Redirect/SSO"
    }
    else {
      sso1.location == "http://identityProvider.test.com/SAML2/Redirect/SSO"
      sso2.location == "http://identityProvider.test.com/SAML2/POST/SSO"
    }

    identityProvider.artifactResolutionServices.size() == 1
    identityProvider.artifactResolutionServices.toList().get(0).location == "http://identityProvider.test.com/SAML2/SOAP/ArtifactResolution"
  }

  def "Backchannel crypto is correctly registered"() {
    setup:
    setupBindings()
    setupCrypto()

    def saml2Prot = SamlURI.build(uri:'urn:oasis:names:tc:SAML:2.0:protocol')
    def organization = Organization.build()
    def entityDescriptor = EntityDescriptor.build(organization:organization)
    def attr1 = Attribute.build()
    def attr2 = Attribute.build()
    def pk = loadPK()
    def backPK = loadBackPK();
    def contact = Contact.build(organization: organization)
    def ct = new ContactType(name:'technical', displayName:'Technical', description: 'Technical contacts').save()

    params.organization = [id: organization.id]
    params.active = true
    params.sigcert = pk
    params.bccert = backPK
    params.entity = [id: entityDescriptor.id]
    params.idp = [displayName:"test name", description:"test desc", scope:"test.com", crypto:[sig: true, bc:true], post:'http://identityProvider.test.com/SAML2/POST/SSO',
                redirect:'http://identityProvider.test.com/SAML2/Redirect/SSO', artifact:'http://identityProvider.test.com/SAML2/SOAP/ArtifactResolution', 'artifact-index':1]
    params.aa = [create: false]
    params.contact = [email: contact.email, type:'technical']

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
    def (created, ret) = IdentityProviderService.create(params)

    then:
    created

    def identityProvider = ret.identityProvider
    identityProvider.keyDescriptors.size() == 2
    identityProvider.keyDescriptors.toList().sort().get(0).keyInfo.certificate.data !=
    identityProvider.keyDescriptors.toList().sort().get(1).keyInfo.certificate.data
  }

  def "Encryption crypto is correctly registered"() {
    setup:
    setupBindings()
    setupCrypto()

    def saml2Prot = SamlURI.build(uri:'urn:oasis:names:tc:SAML:2.0:protocol')
    def organization = Organization.build()
    def entityDescriptor = EntityDescriptor.build(organization:organization)
    def attr1 = Attribute.build()
    def attr2 = Attribute.build()
    def pk = loadPK()
    def encPK = loadEncPK();
    def contact = Contact.build(organization: organization)
    def ct = new ContactType(name:'technical', displayName:'Technical', description: 'Technical contacts').save()

    params.organization = [id: organization.id]
    params.active = true
    params.sigcert = pk
    params.enccert = encPK
    params.entity = [id: entityDescriptor.id]
    params.idp = [displayName:"test name", description:"test desc", scope:"test.com", crypto:[sig: true, enc:true], post:'http://identityProvider.test.com/SAML2/POST/SSO',
                redirect:'http://identityProvider.test.com/SAML2/Redirect/SSO', artifact:'http://identityProvider.test.com/SAML2/SOAP/ArtifactResolution', 'artifact-index':1]
    params.aa = [create: false]
    params.contact = [email: contact.email, type:'technical']

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
    def (created, ret) = IdentityProviderService.create(params)

    then:
    created

    def identityProvider = ret.identityProvider

    identityProvider.keyDescriptors.size() == 2
    identityProvider.keyDescriptors.toList().sort().get(0).keyInfo.certificate.data !=
    identityProvider.keyDescriptors.toList().sort().get(1).keyInfo.certificate.data
  }

  def "Create succeeds when AttributeAuthorityDescriptor not required"() {
    setup:
    setupBindings()
    setupCrypto()

    def saml2Prot = SamlURI.build(uri:'urn:oasis:names:tc:SAML:2.0:protocol')
    def organization = Organization.build()
    def entityDescriptor = EntityDescriptor.build(organization:organization)
    def attr1 = Attribute.build()
    def attr2 = Attribute.build()
    def pk = loadPK()
    def contact = Contact.build(organization: organization)
    def ct = new ContactType(name:'technical', displayName:'Technical', description: 'Technical contacts').save()

    params.organization = [id: organization.id]
    params.active = true
    params.sigcert = pk
    params.entity = [id: entityDescriptor.id]
    params.idp = [displayName:"test name", description:"test desc", scope:"test.com", crypto:[sig: true, enc:true], post:'http://identityProvider.test.com/SAML2/POST/SSO',
                redirect:'http://identityProvider.test.com/SAML2/Redirect/SSO', artifact:'http://identityProvider.test.com/SAML2/SOAP/ArtifactResolution', 'artifact-index':1]
    params.aa = [create: false]
    params.contact = [email: contact.email, type:'technical']

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
    def (created, ret) = IdentityProviderService.create(params)

    then:
    created

    def identityProvider = ret.identityProvider
    def attributeAuthority = ret.attributeAuthority

    identityProvider.organization == organization
    identityProvider.entityDescriptor == entityDescriptor
    identityProvider.entityDescriptor.organization == organization
    identityProvider.collaborator == null

    identityProvider.displayName == "test name"
    identityProvider.description == "test desc"
    identityProvider.keyDescriptors.size() == 1
    identityProvider.singleSignOnServices.size() == 2
    def sso1 = identityProvider.singleSignOnServices.toList().get(0)
    def sso2 = identityProvider.singleSignOnServices.toList().get(1)
    if(sso1.binding == SamlURI.findByUri(SamlConstants.httpPost)) {
      sso1.location == "http://identityProvider.test.com/SAML2/POST/SSO"
      sso2.location == "http://identityProvider.test.com/SAML2/Redirect/SSO"
    }
    else {
      sso1.location == "http://identityProvider.test.com/SAML2/Redirect/SSO"
      sso2.location == "http://identityProvider.test.com/SAML2/POST/SSO"
    }

    identityProvider.artifactResolutionServices.size() == 1
    identityProvider.artifactResolutionServices.toList().get(0).location == "http://identityProvider.test.com/SAML2/SOAP/ArtifactResolution"

    wfProcessName == "idpssodescriptor_create"

    wfPriority == ProcessPriority.MEDIUM
    wfParams.size() == 5
    wfParams.creator == "${contact.id}"
    wfParams.identityProvider == "${identityProvider.id}"
    wfParams.organization == "${organization.id}"
  }

  def "Create succeeds when AttributeAuthorityDescriptor data not presented"() {
    setup:
    setupBindings()
    setupCrypto()

    def saml2Prot = SamlURI.build(uri:'urn:oasis:names:tc:SAML:2.0:protocol')
    def organization = Organization.build()
    def entityDescriptor = EntityDescriptor.build(organization:organization)
    def attr1 = Attribute.build()
    def attr2 = Attribute.build()
    def pk = loadPK()
    def contact = Contact.build(organization: organization)
    def ct = new ContactType(name:'technical', displayName:'Technical', description: 'Technical contacts').save()

    params.organization = [id: organization.id]
    params.active = true
    params.sigcert = pk
    params.entity = [id: entityDescriptor.id]
    params.idp = [displayName:"test name", description:"test desc", scope:"test.com", crypto:[sig: true, enc:true], post:'http://identityProvider.test.com/SAML2/POST/SSO',
                redirect:'http://identityProvider.test.com/SAML2/Redirect/SSO', artifact:'http://identityProvider.test.com/SAML2/SOAP/ArtifactResolution', 'artifact-index':1]
    params.contact = [email: contact.email, type:'technical']

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
    def (created, ret) = IdentityProviderService.create(params)

    then:
    created

        def identityProvider = ret.identityProvider
        def attributeAuthority = ret.attributeAuthority

    identityProvider.organization == organization
    identityProvider.entityDescriptor == entityDescriptor
    identityProvider.entityDescriptor.organization == organization
    identityProvider.collaborator == null

    identityProvider.displayName == "test name"
    identityProvider.description == "test desc"
    identityProvider.keyDescriptors.size() == 1
    identityProvider.singleSignOnServices.size() == 2
    def sso1 = identityProvider.singleSignOnServices.toList().get(0)
    def sso2 = identityProvider.singleSignOnServices.toList().get(1)
    if(sso1.binding == SamlURI.findByUri(SamlConstants.httpPost)) {
      sso1.location == "http://identityProvider.test.com/SAML2/POST/SSO"
      sso2.location == "http://identityProvider.test.com/SAML2/Redirect/SSO"
    }
    else {
      sso1.location == "http://identityProvider.test.com/SAML2/Redirect/SSO"
      sso2.location == "http://identityProvider.test.com/SAML2/POST/SSO"
    }

    identityProvider.artifactResolutionServices.size() == 1
    identityProvider.artifactResolutionServices.toList().get(0).location == "http://identityProvider.test.com/SAML2/SOAP/ArtifactResolution"

    wfProcessName == "idpssodescriptor_create"

    wfPriority == ProcessPriority.MEDIUM
    wfParams.size() == 5
    wfParams.creator == "${contact.id}"
    wfParams.identityProvider == "${identityProvider.id}"
    wfParams.organization == "${organization.id}"
  }

  def "Create fails when AttributeAuthorityDescriptor fails to meet constraints"() {
    setup:
    setupBindings()
    setupCrypto()

    def saml2Prot = SamlURI.build(uri:'urn:oasis:names:tc:SAML:2.0:protocol')
    def organization = Organization.build()
    def entityDescriptor = EntityDescriptor.build(organization:organization)
    def attr1 = Attribute.build()
    def attr2 = Attribute.build()
    def pk = loadPK()
    def contact = Contact.build(organization: organization)
    def ct = new ContactType(name:'technical', displayName:'Technical', description: 'Technical contacts').save()

    params.organization = [id: organization.id]
    params.active = true
    params.sigcert = pk
    params.entity = [id: entityDescriptor.id]
    params.idp = [displayName:"test name", description:"test desc", scope:"test.com", crypto:[sig: true, enc:true], post:'http://identityProvider.test.com/SAML2/POST/SSO',
                redirect:'http://identityProvider.test.com/SAML2/Redirect/SSO', artifact:'http://identityProvider.test.com/SAML2/SOAP/ArtifactResolution', 'artifact-index':1]
    params.aa = [create: true, crypto:[sig: true, enc:true], attributeservice:[uri:"abcd"], attributes:[1, 2]]
    params.contact = [email: contact.email, type:'technical']

    when:
    def (created, ret) = IdentityProviderService.create(params)

    then:
    !created

        def identityProvider = ret.identityProvider
        def attributeAuthority = ret.attributeAuthority

    identityProvider.organization == organization
    identityProvider.entityDescriptor == entityDescriptor
    identityProvider.entityDescriptor.organization == organization

    identityProvider.displayName == "test name"
    identityProvider.description == "test desc"
    identityProvider.keyDescriptors.size() == 1
    identityProvider.keyDescriptors.toList().get(0).keyInfo.certificate.data == pk

    identityProvider.singleSignOnServices.size() == 2

    identityProvider.singleSignOnServices.contains(ret.httpPost)
    ret.httpPost.location == "http://identityProvider.test.com/SAML2/POST/SSO"


    identityProvider.singleSignOnServices.contains(ret.httpRedirect)
    ret.httpRedirect.location == "http://identityProvider.test.com/SAML2/Redirect/SSO"

    identityProvider.artifactResolutionServices.size() == 1

    identityProvider.artifactResolutionServices.contains(ret.soapArtifact)
    ret.soapArtifact.location == "http://identityProvider.test.com/SAML2/SOAP/ArtifactResolution"

    attributeAuthority.displayName == "test name"
  }

  def "Create fails when invalid existing EnityDescriptor provided"() {
    setup:
    setupBindings()
    setupCrypto()

    def saml2Prot = SamlURI.build(uri:'urn:oasis:names:tc:SAML:2.0:protocol')
    def organization = Organization.build()
    def entityDescriptor = EntityDescriptor.build(organization:organization)
    def attr1 = Attribute.build()
    def attr2 = Attribute.build()
    def pk = loadPK()
    def contact = Contact.build(organization: organization)
    def ct = new ContactType(name:'technical', displayName:'Technical', description: 'Technical contacts').save()

    params.organization = [id: organization.id]
    params.active = true
    params.sigcert = pk
    params.entity = [id: 20000]
    params.idp = [displayName:"test name", description:"test desc", scope:"test.com", crypto:[sig: true, enc:true], post:'http://identityProvider.test.com/SAML2/POST/SSO',
                redirect:'http://identityProvider.test.com/SAML2/Redirect/SSO', artifact:'http://identityProvider.test.com/SAML2/SOAP/ArtifactResolution', 'artifact-index':1]
    params.aa = [create: true, displayName:"test name", description:"test desc", scope:"test.com", crypto:[sig: true, enc:true], attributeservice:[uri:"http://identityProvider.test.com/SAML2/SOAP/AttributeQuery"], attributes:[1, 2]]
    params.contact = [email: contact.email, type:'technical']

    when:
    def (created, ret) = IdentityProviderService.create(params)

    then:
    !created

    def identityProvider = ret.identityProvider
    def attributeAuthority = ret.attributeAuthority

    identityProvider.organization == organization
    identityProvider.entityDescriptor.hasErrors()

    identityProvider.displayName == "test name"
    identityProvider.description == "test desc"
    identityProvider.keyDescriptors.size() == 1
    identityProvider.keyDescriptors.toList().get(0).keyInfo.certificate.data == pk

    identityProvider.singleSignOnServices.size() == 2

    identityProvider.singleSignOnServices.contains(ret.httpPost)
    ret.httpPost.location == "http://identityProvider.test.com/SAML2/POST/SSO"


    identityProvider.singleSignOnServices.contains(ret.httpRedirect)
    ret.httpRedirect.location == "http://identityProvider.test.com/SAML2/Redirect/SSO"

    identityProvider.artifactResolutionServices.size() == 1

    identityProvider.artifactResolutionServices.contains(ret.soapArtifact)
    ret.soapArtifact.location == "http://identityProvider.test.com/SAML2/SOAP/ArtifactResolution"

    attributeAuthority.organization == organization
    attributeAuthority.entityDescriptor.hasErrors()
        attributeAuthority.collaborator == null

  }

  def "Create fails when EnityDescriptor does not meet constraints"() {
    setup:
    setupBindings()
    setupCrypto()

    def saml2Prot = SamlURI.build(uri:'urn:oasis:names:tc:SAML:2.0:protocol')
    def organization = Organization.build()
    def attr1 = Attribute.build()
    def attr2 = Attribute.build()
    def pk = loadPK()
    def contact = Contact.build(organization: organization)
    def ct = new ContactType(name:'technical', displayName:'Technical', description: 'Technical contacts').save()

    params.organization = [id: organization.id]
    params.active = true
    params.sigcert = pk
    params.entity = [identifier:""]
    params.idp = [displayName:"test name", description:"test desc", scope:"test.com", crypto:[sig: true, enc:true], post:'http://identityProvider.test.com/SAML2/POST/SSO',
                redirect:'http://identityProvider.test.com/SAML2/Redirect/SSO', artifact:'http://identityProvider.test.com/SAML2/SOAP/ArtifactResolution', 'artifact-index':1]
    params.aa = [create: true, displayName:"test name", description:"test desc", scope:"test.com", crypto:[sig: true, enc:true], attributeservice:[uri:"http://identityProvider.test.com/SAML2/SOAP/AttributeQuery"], attributes:[1, 2]]
    params.contact = [email: contact.email, type:'technical']

    when:
    def (created, ret) = IdentityProviderService.create(params)

    then:
    !created

        def identityProvider = ret.identityProvider
        def attributeAuthority = ret.attributeAuthority

    identityProvider.organization == organization
    identityProvider.entityDescriptor.hasErrors()

    identityProvider.displayName == "test name"
    identityProvider.description == "test desc"
    identityProvider.keyDescriptors.size() == 1
    identityProvider.keyDescriptors.toList().get(0).keyInfo.certificate.data == pk

    identityProvider.singleSignOnServices.size() == 2

    identityProvider.singleSignOnServices.contains(ret.httpPost)
    ret.httpPost.location == "http://identityProvider.test.com/SAML2/POST/SSO"


    identityProvider.singleSignOnServices.contains(ret.httpRedirect)
    ret.httpRedirect.location == "http://identityProvider.test.com/SAML2/Redirect/SSO"

    identityProvider.artifactResolutionServices.size() == 1

    identityProvider.artifactResolutionServices.contains(ret.soapArtifact)
    ret.soapArtifact.location == "http://identityProvider.test.com/SAML2/SOAP/ArtifactResolution"

    attributeAuthority.organization == organization
    attributeAuthority.entityDescriptor.hasErrors()
    attributeAuthority.collaborator == null

  }

  def "Create fails when invalid Organization provided"() {
    setup:
    setupBindings()
    setupCrypto()

    def saml2Prot = SamlURI.build(uri:'urn:oasis:names:tc:SAML:2.0:protocol')
    def organization = Organization.build()
    def attr1 = Attribute.build()
    def attr2 = Attribute.build()
    def pk = loadPK()
    def contact = Contact.build(organization: organization)
    def ct = new ContactType(name:'technical', displayName:'Technical', description: 'Technical contacts').save()

    params.organization = [id: 200000000]
    params.active = true
    params.sigcert = pk
    params.entity = [identifier: "http://test.example.com"]
    params.idp = [displayName:"test name", description:"test desc", scope:"test.com", crypto:[sig: true, enc:true], post:'http://identityProvider.test.com/SAML2/POST/SSO',
                redirect:'http://identityProvider.test.com/SAML2/Redirect/SSO', artifact:'http://identityProvider.test.com/SAML2/SOAP/ArtifactResolution', 'artifact-index':1]
    params.aa = [create: true, displayName:"test name", description:"test desc", scope:"test.com", crypto:[sig: true, enc:true], attributeservice:[uri:"http://identityProvider.test.com/SAML2/SOAP/AttributeQuery"], attributes:[1, 2]]
    params.contact = [email: contact.email, type:'technical']

    when:
    def (created, ret) = IdentityProviderService.create(params)

    then:
    !created

        def identityProvider = ret.identityProvider
        def attributeAuthority = ret.attributeAuthority

    identityProvider.organization == null
    identityProvider.entityDescriptor.id == null
    identityProvider.entityDescriptor.entityID == 'http://test.example.com'

    identityProvider.displayName == "test name"
    identityProvider.description == "test desc"
    identityProvider.keyDescriptors.size() == 1
    identityProvider.keyDescriptors.toList().get(0).keyInfo.certificate.data == pk

    identityProvider.singleSignOnServices.size() == 2

    identityProvider.singleSignOnServices.contains(ret.httpPost)
    ret.httpPost.location == "http://identityProvider.test.com/SAML2/POST/SSO"


    identityProvider.singleSignOnServices.contains(ret.httpRedirect)
    ret.httpRedirect.location == "http://identityProvider.test.com/SAML2/Redirect/SSO"

    identityProvider.artifactResolutionServices.size() == 1

    identityProvider.artifactResolutionServices.contains(ret.soapArtifact)
    ret.soapArtifact.location == "http://identityProvider.test.com/SAML2/SOAP/ArtifactResolution"

    attributeAuthority.organization == null
    attributeAuthority.entityDescriptor.hasErrors()
        attributeAuthority.collaborator == null
  }

  def "Create false when valid invalid contact details provided"() {
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
    def ct = new ContactType(name:'technical', displayName:'Technical', description: 'Technical contacts').save()

    params.organization = [id: organization.id]
    params.active = true
    params.sigcert = pk
    params.entity = [identifier:"http://identityProvider1.test.com"]
    params.idp = [displayName:"test name", description:"test desc", scope: "test.com", attributes:[(attr1.id):'on', (attr2.id):'on'], crypto:[sig: true, enc:true], post:'http://identityProvider.test.com/SAML2/POST/SSO',
                            redirect:'http://identityProvider.test.com/SAML2/Redirect/SSO', artifact:'http://identityProvider.test.com/SAML2/SOAP/ArtifactResolution', 'artifact-index':1]
    params.aa = [create: true, displayName:"test name", description:"test desc", scope:"test.com", crypto:[sig: true, enc:true],attributeservice:'http://identityProvider.test.com/SAML2/SOAP/AttributeQuery']
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
    def (created, ret) = IdentityProviderService.create(params)

    then:
    !created

    ret.contact.hasErrors()

    def entityDescriptor = ret.entityDescriptor
    !entityDescriptor.hasErrors()
    entityDescriptor.entityID == "http://identityProvider1.test.com"

    def identityProvider = ret.identityProvider
    identityProvider.organization == organization
    identityProvider.entityDescriptor == entityDescriptor
    identityProvider.entityDescriptor.organization == organization

    identityProvider.displayName == "test name"
    identityProvider.description == "test desc"
    identityProvider.keyDescriptors.size() == 1
    identityProvider.keyDescriptors.toList().get(0).keyInfo.certificate.data == pk

    identityProvider.singleSignOnServices.size() == 2
    def sso1 = identityProvider.singleSignOnServices.toList().get(0)
    def sso2 = identityProvider.singleSignOnServices.toList().get(1)
    if(sso1.binding == SamlURI.findByUri(SamlConstants.httpPost)) {
        sso1.location == "http://identityProvider.test.com/SAML2/POST/SSO"
        sso2.location == "http://identityProvider.test.com/SAML2/Redirect/SSO"
    }
    else {
        sso1.location == "http://identityProvider.test.com/SAML2/Redirect/SSO"
        sso2.location == "http://identityProvider.test.com/SAML2/POST/SSO"
    }

    identityProvider.artifactResolutionServices.size() == 1
    identityProvider.artifactResolutionServices.toList().get(0).location == "http://identityProvider.test.com/SAML2/SOAP/ArtifactResolution"

    identityProvider.attributes != null
    identityProvider.attributes.size() == 2

    identityProvider.nameIDFormats != null
    identityProvider.nameIDFormats.size() == 1

    identityProvider.contacts.size() == 1
    identityProvider.contacts.toList().get(0).type.name == ct.name
    identityProvider.contacts.toList().get(0).contact.givenName == "Fred"
    identityProvider.contacts.toList().get(0).contact.surname == "Bloggs"
    identityProvider.contacts.toList().get(0).contact.email == "test.com"

    def attributeAuthority = ret.attributeAuthority
    attributeAuthority.organization == organization
    attributeAuthority.entityDescriptor == entityDescriptor

    attributeAuthority.collaborator == null
    identityProvider.collaborator == null
  }

  def "Updating an existing identity provider with valid changed content succeeds"() {
    setup:
    def organization = Organization.build()
    def entityDescriptor = EntityDescriptor.build(organization:organization)
    def idp = IDPSSODescriptor.build(entityDescriptor:entityDescriptor, active:false, wantAuthnRequestsSigned:false)
    params.id = idp.id
    params.idp = [displayName:"new displayName", description:"new description", scope:"test.com", status:'true']

    when:
    def (updated, identityProvider_) = IdentityProviderService.update(params)

    then:
    updated

    identityProvider_.displayName == "new displayName"
    identityProvider_.description == "new description"
    identityProvider_.active
  }

  def "Updating an existing identity provider with invalid changed content fails"() {
    setup:
    def organization = Organization.build()
    def entityDescriptor = EntityDescriptor.build(organization:organization)
    def idp = IDPSSODescriptor.build(entityDescriptor:entityDescriptor, active:false, wantAuthnRequestsSigned:false)
    params.id = idp.id
    params.idp = [displayName:"", description:"new description", status:'true']

    when:
    def (updated, identityProvider_) = IdentityProviderService.update(params)

    then:
    !updated

    identityProvider_.displayName == ""
    identityProvider_.description == "new description"
    identityProvider_.active
  }

}
