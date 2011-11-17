package fedreg.core

import grails.plugin.spock.*

import fedreg.core.*
import fedreg.workflow.*
import grails.plugins.nimble.core.*

class IDPSSODescriptorServiceSpec extends IntegrationSpec {
	
	def cryptoService
	def savedMetaClasses
	def workflowProcessService
	def entityDescriptorService
	def IDPSSODescriptorService
	def params, user, svedMetaClasses
	
	def setup () {
        params = [:]

        user = UserBase.build()
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
		def ct = ContactType.build()

		params.organization = [id: organization.id]
		params.active = true
		params.cert = pk
		params.entity = [identifier:"http://identityProvider.test.com"]
		params.idp = [displayName:"test name", description:"test desc", scope: "test.com", attributes:[(attr1.id):'on', (attr2.id):'on'], nameidformats:[(nameID1.id):'on', (nameID2.id):'on'], crypto:[sig: true, enc:true], post:[uri:"http://identityProvider.test.com/SAML2/POST/SSO"], 
								redirect:[uri:"http://identityProvider.test.com/SAML2/Redirect/SSO"], artifact:[uri:"http://identityProvider.test.com/SAML2/SOAP/ArtifactResolution"]]
		params.aa = [create: true, displayName:"test name", description:"test desc", scope:"test.com", crypto:[sig: true, enc:true], attributeservice:[uri:"http://identityProvider.test.com/SAML2/SOAP/AttributeQuery"], attributes:[(attr1.id):'on', (attr2.id):'on']]
		params.contact = [givenName:"Fred", surname:"Bloggs", email:"fredbloggs@test.com", type:ct.name]
		
		def wfProcessName, wfDescription, wfPriority, wfParams
		
		when:
		WorkflowProcessService.metaClass.initiate =  { String processName, String instanceDescription, ProcessPriority priority, Map params ->
            println "73"
			wfProcessName = processName
			wfDescription = instanceDescription
			wfPriority = priority
			wfParams = params
			[true, [:]]
		}
		WorkflowProcessService.metaClass.run = { def processInstance -> }
		def (created, ret) = IDPSSODescriptorService.create(params)
		
		then:
		created
		
		Organization.count() == 1
		SamlURI.count() == 7
		EntityDescriptor.count() == 1
		IDPSSODescriptor.count() == 1
		AttributeAuthorityDescriptor.count() == 1
		
		def entityDescriptor = ret.entityDescriptor
		entityDescriptor.entityID == "http://identityProvider.test.com"
		
		def identityProvider = ret.identityProvider
		identityProvider.organization == organization
		identityProvider.entityDescriptor == entityDescriptor
		identityProvider.entityDescriptor.organization == organization
		
		identityProvider.displayName == "test name"
		identityProvider.description == "test desc"
		identityProvider.keyDescriptors.size() == 2
		identityProvider.keyDescriptors.toList().get(0).keyInfo.certificate.data == pk
		identityProvider.keyDescriptors.toList().get(1).keyInfo.certificate.data == pk
		
		identityProvider.singleSignOnServices.size() == 2
		def sso1 = identityProvider.singleSignOnServices.toList().get(0)
		def sso2 = identityProvider.singleSignOnServices.toList().get(1)
		if(sso1.binding == SamlURI.findByUri(SamlConstants.httpPost)) {
			sso1.location.uri == "http://identityProvider.test.com/SAML2/POST/SSO"
			sso2.location.uri == "http://identityProvider.test.com/SAML2/Redirect/SSO"
		}
		else {
			sso1.location.uri == "http://identityProvider.test.com/SAML2/Redirect/SSO"
			sso2.location.uri == "http://identityProvider.test.com/SAML2/POST/SSO"
		}
				
		identityProvider.artifactResolutionServices.size() == 1
		identityProvider.artifactResolutionServices.toList().get(0).location.uri == "http://identityProvider.test.com/SAML2/SOAP/ArtifactResolution"
		
		identityProvider.attributes != null
		identityProvider.attributes.size() == 2
		
		identityProvider.nameIDFormats != null
		identityProvider.nameIDFormats.size() == 2
		
		identityProvider.contacts.size() == 1
		identityProvider.contacts.toList().get(0).type.name == ct.name
		identityProvider.contacts.toList().get(0).contact.givenName == "Fred"
		identityProvider.contacts.toList().get(0).contact.surname == "Bloggs"
		identityProvider.contacts.toList().get(0).contact.email.uri == "fredbloggs@test.com"
		
		def attributeAuthority = ret.attributeAuthority
		attributeAuthority.organization == organization
		attributeAuthority.entityDescriptor == entityDescriptor
		attributeAuthority.collaborator == identityProvider
		identityProvider.collaborator == attributeAuthority

		
		attributeAuthority.attributes != null
		attributeAuthority.attributes.size() == 2
		
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
		def ct = ContactType.build()

		params.organization = [id: organization.id]
		params.active = true
		params.cert = pk
		params.entity = [identifier:"http://identityProvider2.test.com"]
		params.idp = [displayName:"test name", description:"test desc", scope:"test.com", attributes:[(attr1.id):'on', (attr2.id):'on'], nameidformats:[(nameID1.id):'on', (nameID2.id):'on'], crypto:[sig: true, enc:true], post:[uri:"http://identityProvider.test.com/SAML2/POST/SSO"], 
								redirect:[uri:"http://identityProvider.test.com/SAML2/Redirect/SSO"], artifact:[uri:"http://identityProvider.test.com/SAML2/SOAP/ArtifactResolution"]]
		params.aa = [create: true, displayName:"test name", description:"test desc", scope:"test.com", crypto:[sig: true, enc:true], attributeservice:[uri:"http://identityProvider.test.com/SAML2/SOAP/AttributeQuery"], attributes:[(attr1.id):'on', (attr2.id):'on']]
		params.contact = [id: contact.id, type: ct.name]
		
		def wfProcessName, wfDescription, wfPriority, wfParams
		
		when:
		WorkflowProcessService.metaClass.initiate =  { String processName, String instanceDescription, ProcessPriority priority, Map params ->
            println "178"
			wfProcessName = processName
			wfDescription = instanceDescription
			wfPriority = priority
			wfParams = params
			[true, [:]]
		}
		WorkflowProcessService.metaClass.run = { def processInstance -> }
		def (created, ret) = IDPSSODescriptorService.create(params)
		
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
		identityProvider.keyDescriptors.size() == 2
		identityProvider.keyDescriptors.toList().get(0).keyInfo.certificate.data == pk
		identityProvider.keyDescriptors.toList().get(1).keyInfo.certificate.data == pk
		
		identityProvider.singleSignOnServices.size() == 2
		def sso1 = identityProvider.singleSignOnServices.toList().get(0)
		def sso2 = identityProvider.singleSignOnServices.toList().get(1)
		if(sso1.binding == SamlURI.findByUri(SamlConstants.httpPost)) {
			sso1.location.uri == "http://identityProvider.test.com/SAML2/POST/SSO"
			sso2.location.uri == "http://identityProvider.test.com/SAML2/Redirect/SSO"
		}
		else {
			sso1.location.uri == "http://identityProvider.test.com/SAML2/Redirect/SSO"
			sso2.location.uri == "http://identityProvider.test.com/SAML2/POST/SSO"
		}
				
		identityProvider.artifactResolutionServices.size() == 1
		identityProvider.artifactResolutionServices.toList().get(0).location.uri == "http://identityProvider.test.com/SAML2/SOAP/ArtifactResolution"
		
		identityProvider.attributes != null
		identityProvider.attributes.size() == 2
		
		identityProvider.nameIDFormats != null
		identityProvider.nameIDFormats.size() == 2
		
		identityProvider.contacts.size() == 1
		identityProvider.contacts.toList().get(0).type.name == ct.name
		identityProvider.contacts.toList().get(0).contact.givenName == contact.givenName
		identityProvider.contacts.toList().get(0).contact.surname == contact.surname
		identityProvider.contacts.toList().get(0).contact.email.uri == contact.email.uri
		
		def attributeAuthority = ret.attributeAuthority
		attributeAuthority.organization == organization
		attributeAuthority.entityDescriptor == entityDescriptor
		attributeAuthority.collaborator == identityProvider
		identityProvider.collaborator == attributeAuthority

		
		attributeAuthority.attributes != null
		attributeAuthority.attributes.size() == 2
		
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
		def entityDescriptor = EntityDescriptor.build(organization:organization)
		def attr1 = AttributeBase.build()
		def attr2 = AttributeBase.build()
		def pk = loadPK()
		def contact = Contact.build(organization: organization)
		def ct = ContactType.build()

		params.organization = [id: organization.id]
		params.active = true
		params.cert = pk
		params.entity = [id: entityDescriptor.id]
		params.idp = [displayName:"test name", description:"test desc", scope:"test.com", attributes:[(attr1.id):'on', (attr2.id):'on'], crypto:[sig: true, enc:true], post:[uri:"http://identityProvider.test.com/SAML2/POST/SSO"], 
								redirect:[uri:"http://identityProvider.test.com/SAML2/Redirect/SSO"], artifact:[uri:"http://identityProvider.test.com/SAML2/SOAP/ArtifactResolution"]]
		params.aa = [create: true, displayName:"test name", description:"test desc", scope:"test.com", crypto:[sig: true, enc:true], attributeservice:[uri:"http://identityProvider.test.com/SAML2/SOAP/AttributeQuery"], attributes:[(attr1.id):'on', (attr2.id):'on']]
		params.contact = [id: contact.id, type: ct.name]
		
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
		def (created, ret) = IDPSSODescriptorService.create(params)
		
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
		identityProvider.keyDescriptors.size() == 2
		identityProvider.keyDescriptors.toList().get(0).keyInfo.certificate.data == pk
		identityProvider.keyDescriptors.toList().get(1).keyInfo.certificate.data == pk
		
		identityProvider.singleSignOnServices.size() == 2
		def sso1 = identityProvider.singleSignOnServices.toList().get(0)
		def sso2 = identityProvider.singleSignOnServices.toList().get(1)
		if(sso1.binding == SamlURI.findByUri(SamlConstants.httpPost)) {
			sso1.location.uri == "http://identityProvider.test.com/SAML2/POST/SSO"
			sso2.location.uri == "http://identityProvider.test.com/SAML2/Redirect/SSO"
		}
		else {
			sso1.location.uri == "http://identityProvider.test.com/SAML2/Redirect/SSO"
			sso2.location.uri == "http://identityProvider.test.com/SAML2/POST/SSO"
		}
				
		identityProvider.artifactResolutionServices.size() == 1
		identityProvider.artifactResolutionServices.toList().get(0).location.uri == "http://identityProvider.test.com/SAML2/SOAP/ArtifactResolution"
		
		identityProvider.attributes != null
		identityProvider.attributes.size() == 2
		
		identityProvider.contacts.size() == 1
		identityProvider.contacts.toList().get(0).type.name == ct.name
		identityProvider.contacts.toList().get(0).contact.givenName == contact.givenName
		identityProvider.contacts.toList().get(0).contact.surname == contact.surname
		identityProvider.contacts.toList().get(0).contact.email.uri == contact.email.uri
		
		attributeAuthority.organization == organization
		attributeAuthority.entityDescriptor == entityDescriptor
		attributeAuthority.collaborator == identityProvider
		identityProvider.collaborator == attributeAuthority
		attributeAuthority.active 
		!attributeAuthority.approved

		
		attributeAuthority.attributes != null
		attributeAuthority.attributes.size() == 2
		
		wfProcessName == "idpssodescriptor_create"
		
		wfPriority == ProcessPriority.MEDIUM
		wfParams.size() == 5
		wfParams.creator == "${contact.id}"
		wfParams.identityProvider == "${identityProvider.id}"
		wfParams.organization == "${organization.id}"
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
		def ct = ContactType.build()

		params.organization = [id: organization.id]
		params.active = true
		params.cert = pk
		params.entity = [id: entityDescriptor.id]
		params.idp = [description:"test desc", scope:"test.com", crypto:[sig: true, enc:true], post:[uri:"http://identityProvider.test.com/SAML2/POST/SSO"], 
								redirect:[uri:"http://identityProvider.test.com/SAML2/Redirect/SSO"], artifact:[uri:"http://identityProvider.test.com/SAML2/SOAP/ArtifactResolution"]]
		params.aa = [create: true, displayName:"test name", description:"test desc", scope:"test.com", crypto:[sig: true, enc:true], attributeservice:[uri:"http://identityProvider.test.com/SAML2/SOAP/AttributeQuery"], attributes:[1, 2]]
		params.contact = [id: contact.id, type: ct.name]
		
		when:
		def (created, ret) = IDPSSODescriptorService.create(params)
		
		then:
		!created

        def identityProvider = ret.identityProvider
        def attributeAuthority = ret.attributeAuthority

		identityProvider.organization == organization
		identityProvider.entityDescriptor == entityDescriptor
		identityProvider.entityDescriptor.organization == organization
		
		identityProvider.displayName == null
		identityProvider.description == "test desc"
		identityProvider.keyDescriptors.size() == 2
		identityProvider.keyDescriptors.toList().get(0).keyInfo.certificate.data == pk
		identityProvider.keyDescriptors.toList().get(1).keyInfo.certificate.data == pk
		
		identityProvider.singleSignOnServices.size() == 2
		
		identityProvider.singleSignOnServices.contains(ret.httpPost)
		ret.httpPost.location.uri == "http://identityProvider.test.com/SAML2/POST/SSO"
		
		
		identityProvider.singleSignOnServices.contains(ret.httpRedirect)
		ret.httpRedirect.location.uri == "http://identityProvider.test.com/SAML2/Redirect/SSO"
		
		identityProvider.artifactResolutionServices.size() == 1
		
		identityProvider.artifactResolutionServices.contains(ret.soapArtifact)
		ret.soapArtifact.location.uri == "http://identityProvider.test.com/SAML2/SOAP/ArtifactResolution"
		
		identityProvider.contacts.size() == 1
		identityProvider.contacts.toList().get(0).id == null
		
		
		attributeAuthority.organization == organization
		attributeAuthority.entityDescriptor == entityDescriptor
		attributeAuthority.collaborator == identityProvider

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
		def ct = ContactType.build()

		params.organization = [id: organization.id]
		params.active = true
		params.cert = pk
		params.entity = [id: entityDescriptor.id]
		params.idp = [displayName: "test name", description:"test desc", scope:"test.com", crypto:[sig: true, enc:true], post:[uri:"/SAML2/POST/SSO"], 
								redirect:[uri:"http://identityProvider.test.com/SAML2/Redirect/SSO"], artifact:[uri:"http://identityProvider.test.com/SAML2/SOAP/ArtifactResolution"]]
		params.aa = [create: true, displayName:"test name", description:"test desc", scope:"test.com", crypto:[sig: true, enc:true], attributeservice:[uri:"http://identityProvider.test.com/SAML2/SOAP/AttributeQuery"], attributes:[1, 2]]
		params.contact = [id: contact.id, type: ct.name]
		
		when:
		def (created, ret) = IDPSSODescriptorService.create(params)
		
		then:
		!created

        def identityProvider = ret.identityProvider
        def attributeAuthority = ret.attributeAuthority

		identityProvider.organization == organization
		identityProvider.entityDescriptor == entityDescriptor
		identityProvider.entityDescriptor.organization == organization
		
		identityProvider.displayName == "test name"
		identityProvider.description == "test desc"
		identityProvider.keyDescriptors.size() == 2
		identityProvider.keyDescriptors.toList().get(0).keyInfo.certificate.data == pk
		identityProvider.keyDescriptors.toList().get(1).keyInfo.certificate.data == pk
		
		identityProvider.singleSignOnServices.size() == 2
		
		identityProvider.singleSignOnServices.contains(ret.httpPost)
		ret.httpPost.location.uri == "/SAML2/POST/SSO"
		ret.httpPost.errors.getErrorCount() == 1
		ret.httpPost.errors.getFieldError('location.uri').code == 'url.invalid'
		
		
		identityProvider.singleSignOnServices.contains(ret.httpRedirect)
		ret.httpRedirect.location.uri == "http://identityProvider.test.com/SAML2/Redirect/SSO"
		
		identityProvider.artifactResolutionServices.size() == 1
		
		identityProvider.artifactResolutionServices.contains(ret.soapArtifact)
		ret.soapArtifact.location.uri == "http://identityProvider.test.com/SAML2/SOAP/ArtifactResolution"
		
		
		attributeAuthority.organization == organization
		attributeAuthority.entityDescriptor == entityDescriptor
		attributeAuthority.collaborator == identityProvider

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
		def ct = ContactType.build()

		params.organization = [id: organization.id]
		params.active = true
		params.cert = pk
		params.entity = [id: entityDescriptor.id]
		params.idp = [displayName: "test name", description:"test desc", scope:"test.com", crypto:[sig: true, enc:true], 
								redirect:[uri:"http://identityProvider.test.com/SAML2/Redirect/SSO"], artifact:[uri:"http://identityProvider.test.com/SAML2/SOAP/ArtifactResolution"]]
		params.aa = [create: true, displayName:"test name", description:"test desc", scope:"test.com", crypto:[sig: true, enc:true], attributeservice:[uri:"http://identityProvider.test.com/SAML2/SOAP/AttributeQuery"], attributes:[1, 2]]
		params.contact = [id: contact.id, type: ct.name]
		
		when:
		def (created, ret) = IDPSSODescriptorService.create(params)
		
		then:
		!created

        def identityProvider = ret.identityProvider
        def attributeAuthority = ret.attributeAuthority
		
		identityProvider.organization == organization
		identityProvider.entityDescriptor == entityDescriptor
		identityProvider.entityDescriptor.organization == organization
		
		identityProvider.displayName == "test name"
		identityProvider.description == "test desc"
		identityProvider.keyDescriptors.size() == 2
		identityProvider.keyDescriptors.toList().get(0).keyInfo.certificate.data == pk
		identityProvider.keyDescriptors.toList().get(1).keyInfo.certificate.data == pk
		
		identityProvider.singleSignOnServices.size() == 2
		
		identityProvider.singleSignOnServices.contains(ret.httpPost)
		ret.httpPost.location.uri == null
		ret.httpPost.errors.getErrorCount() == 1
		ret.httpPost.errors.getFieldError('location.uri').code == 'nullable'
		identityProvider.errors.getErrorCount() == 1
		identityProvider.errors.getFieldError('singleSignOnServices.location.uri').code == 'nullable'
		
		
		identityProvider.singleSignOnServices.contains(ret.httpRedirect)
		ret.httpRedirect.location.uri == "http://identityProvider.test.com/SAML2/Redirect/SSO"
		
		identityProvider.artifactResolutionServices.size() == 1
		
		identityProvider.artifactResolutionServices.contains(ret.soapArtifact)
		ret.soapArtifact.location.uri == "http://identityProvider.test.com/SAML2/SOAP/ArtifactResolution"
		
		
		attributeAuthority.organization == organization
		attributeAuthority.entityDescriptor == entityDescriptor
		attributeAuthority.collaborator == identityProvider

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
		def ct = ContactType.build()

		params.organization = [id: organization.id]
		params.active = true
		params.cert = pk
		params.entity = [id: entityDescriptor.id]
		params.idp = [displayName: "test name", description:"test desc", scope:"test.com", crypto:[sig: true, enc:true], post:[uri:"http://identityProvider.test.com/SAML2/POST/SSO"], 
								redirect:[uri:"asdfasdasdf"], artifact:[uri:"http://identityProvider.test.com/SAML2/SOAP/ArtifactResolution"]]
		params.aa = [create: true, displayName:"test name", description:"test desc", scope:"test.com", crypto:[sig: true, enc:true], attributeservice:[uri:"http://identityProvider.test.com/SAML2/SOAP/AttributeQuery"], attributes:[1, 2]]
		params.contact = [id: contact.id, type: ct.name]
		
		when:
		def (created, ret) = IDPSSODescriptorService.create(params)
		
		then:
		!created

        def identityProvider = ret.identityProvider
        def attributeAuthority = ret.attributeAuthority
		
		identityProvider.organization == organization
		identityProvider.entityDescriptor == entityDescriptor
		identityProvider.entityDescriptor.organization == organization
		
		identityProvider.displayName == "test name"
		identityProvider.description == "test desc"
		identityProvider.keyDescriptors.size() == 2
		identityProvider.keyDescriptors.toList().get(0).keyInfo.certificate.data == pk
		identityProvider.keyDescriptors.toList().get(1).keyInfo.certificate.data == pk
		
		identityProvider.singleSignOnServices.size() == 2
		
		identityProvider.singleSignOnServices.contains(ret.httpPost)
		ret.httpPost.location.uri == "http://identityProvider.test.com/SAML2/POST/SSO"

		
		identityProvider.singleSignOnServices.contains(ret.httpRedirect)
		ret.httpRedirect.location.uri == "asdfasdasdf"
		ret.httpRedirect.errors.getErrorCount() == 1
		ret.httpRedirect.errors.getFieldError('location.uri').code == 'url.invalid'
		
		identityProvider.artifactResolutionServices.size() == 1
		
		identityProvider.artifactResolutionServices.contains(ret.soapArtifact)
		ret.soapArtifact.location.uri == "http://identityProvider.test.com/SAML2/SOAP/ArtifactResolution"		
		
		attributeAuthority.organization == organization
		attributeAuthority.entityDescriptor == entityDescriptor
		attributeAuthority.collaborator == identityProvider

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
		def ct = ContactType.build()

		params.organization = [id: organization.id]
		params.active = true
		params.cert = pk
		params.entity = [id: entityDescriptor.id]
		params.idp = [displayName: "test name", description:"test desc", scope:"test.com", crypto:[sig: true, enc:true], post:[uri:"http://identityProvider.test.com/SAML2/POST/SSO"],
								artifact:[uri:"http://identityProvider.test.com/SAML2/SOAP/ArtifactResolution"]]
		params.aa = [create: true, displayName:"test name", description:"test desc", scope:"test.com", crypto:[sig: true, enc:true], attributeservice:[uri:"http://identityProvider.test.com/SAML2/SOAP/AttributeQuery"], attributes:[1, 2]]
		params.contact = [id: contact.id, type: ct.name]
		
		when:
		def (created, ret) = IDPSSODescriptorService.create(params)
		
		then:
		!created

        def identityProvider = ret.identityProvider
        def attributeAuthority = ret.attributeAuthority
		
		identityProvider.organization == organization
		identityProvider.entityDescriptor == entityDescriptor
		identityProvider.entityDescriptor.organization == organization
		
		identityProvider.displayName == "test name"
		identityProvider.description == "test desc"
		identityProvider.keyDescriptors.size() == 2
		identityProvider.keyDescriptors.toList().get(0).keyInfo.certificate.data == pk
		identityProvider.keyDescriptors.toList().get(1).keyInfo.certificate.data == pk
		
		identityProvider.singleSignOnServices.size() == 2
		
		identityProvider.singleSignOnServices.contains(ret.httpPost)
		ret.httpPost.location.uri == "http://identityProvider.test.com/SAML2/POST/SSO"

		
		identityProvider.singleSignOnServices.contains(ret.httpRedirect)
		ret.httpRedirect.location.uri == null
		ret.httpRedirect.errors.getErrorCount() == 1
		ret.httpRedirect.errors.getFieldError('location.uri').code == 'nullable'
		identityProvider.errors.getErrorCount() == 1
		identityProvider.errors.getFieldError('singleSignOnServices.location.uri').code == 'nullable'
		
		identityProvider.artifactResolutionServices.size() == 1
		
		identityProvider.artifactResolutionServices.contains(ret.soapArtifact)
		ret.soapArtifact.location.uri == "http://identityProvider.test.com/SAML2/SOAP/ArtifactResolution"	
		
		attributeAuthority.organization == organization
		attributeAuthority.entityDescriptor == entityDescriptor
		attributeAuthority.collaborator == identityProvider

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
		def ct = ContactType.build()

		params.organization = [id: organization.id]
		params.active = true
		params.cert = pk
		params.entity = [id: entityDescriptor.id]
		params.idp = [displayName: "test name", description:"test desc", scope:"test.com", crypto:[sig: true, enc:true], post:[uri:"http://identityProvider.test.com/SAML2/POST/SSO"], 
								redirect:[uri:"http://identityProvider.test.com/SAML2/Redirect/SSO"], artifact:[uri:"/SAML2/SOAP/ArtifactResolution", index:1]]
		params.aa = [create: true, displayName:"test name", description:"test desc", scope:"test.com", crypto:[sig: true, enc:true], attributeservice:[uri:"http://identityProvider.test.com/SAML2/SOAP/AttributeQuery"], attributes:[1, 2]]
		params.contact = [id: contact.id, type: ct.name]
		
		when:
		def (created, ret) = IDPSSODescriptorService.create(params)
		
		then:
		!created

        def identityProvider = ret.identityProvider
        def attributeAuthority = ret.attributeAuthority
		
		identityProvider.organization == organization
		identityProvider.entityDescriptor == entityDescriptor
		identityProvider.entityDescriptor.organization == organization
		
		identityProvider.displayName == "test name"
		identityProvider.description == "test desc"
		identityProvider.keyDescriptors.size() == 2
		identityProvider.keyDescriptors.toList().get(0).keyInfo.certificate.data == pk
		identityProvider.keyDescriptors.toList().get(1).keyInfo.certificate.data == pk
		
		identityProvider.singleSignOnServices.size() == 2
		
		identityProvider.singleSignOnServices.contains(ret.httpPost)
		ret.httpPost.location.uri == "http://identityProvider.test.com/SAML2/POST/SSO"

		
		identityProvider.singleSignOnServices.contains(ret.httpRedirect)
		ret.httpRedirect.location.uri == "http://identityProvider.test.com/SAML2/Redirect/SSO"
		
		identityProvider.artifactResolutionServices.size() == 1
		
		identityProvider.artifactResolutionServices.contains(ret.soapArtifact)
		ret.soapArtifact.location.uri == "/SAML2/SOAP/ArtifactResolution"
		ret.soapArtifact.errors.getErrorCount() == 1
		ret.soapArtifact.errors.getFieldError('location.uri').code == 'url.invalid'
		
		
		attributeAuthority.organization == organization
		attributeAuthority.entityDescriptor == entityDescriptor
		attributeAuthority.collaborator == identityProvider

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
		def ct = ContactType.build()

		params.organization = [id: organization.id]
		params.active = true
		params.cert = pk
		params.entity = [id: entityDescriptor.id]
		params.idp = [displayName: "test name", description:"test desc", scope:"test.com", crypto:[sig: true, enc:true], post:[uri:"http://identityProvider.test.com/SAML2/POST/SSO"],
								redirect:[uri:"http://identityProvider.test.com/SAML2/Redirect/SSO"]]
		params.aa = [create: true, displayName:"test name", description:"test desc", scope:"test.com", crypto:[sig: true, enc:true], attributeservice:[uri:"http://identityProvider.test.com/SAML2/SOAP/AttributeQuery"], attributes:[1, 2]]
		params.contact = [id: contact.id, type: ct.name]
		
		when:
		def (created, ret) = IDPSSODescriptorService.create(params)
		
		then:
		!created

        def identityProvider = ret.identityProvider
        def attributeAuthority = ret.attributeAuthority
		
		identityProvider.organization == organization
		identityProvider.entityDescriptor == entityDescriptor
		identityProvider.entityDescriptor.organization == organization
		
		identityProvider.displayName == "test name"
		identityProvider.description == "test desc"
		identityProvider.keyDescriptors.size() == 2
		identityProvider.keyDescriptors.toList().get(0).keyInfo.certificate.data == pk
		identityProvider.keyDescriptors.toList().get(1).keyInfo.certificate.data == pk
		
		identityProvider.singleSignOnServices.size() == 2
		
		identityProvider.singleSignOnServices.contains(ret.httpPost)
		ret.httpPost.location.uri == "http://identityProvider.test.com/SAML2/POST/SSO"

		
		identityProvider.singleSignOnServices.contains(ret.httpRedirect)
		ret.httpRedirect.location.uri == "http://identityProvider.test.com/SAML2/Redirect/SSO"
		
		identityProvider.artifactResolutionServices.size() == 1
		
		identityProvider.artifactResolutionServices.contains(ret.soapArtifact)
		ret.soapArtifact.location.uri == null
		ret.soapArtifact.errors.getErrorCount() == 2
		ret.soapArtifact.errors.getFieldError('location.uri').code == 'nullable'
		identityProvider.errors.getErrorCount() == 1
		identityProvider.errors.getFieldError('artifactResolutionServices.location.uri').code == 'nullable'		
		
		attributeAuthority.organization == organization
		attributeAuthority.entityDescriptor == entityDescriptor
		attributeAuthority.collaborator == identityProvider

	}
	
	def "Create succeeds when IDPSSODescriptor crypto not supplied"() {
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
		def ct = ContactType.build()

		params.organization = [id: organization.id]
		params.active = true
		params.cert = pk
		params.entity = [id: entityDescriptor.id]
		params.idp = [displayName: "test name", description:"test desc", scope:"test.com", post:[uri:"http://identityProvider.test.com/SAML2/POST/SSO"],
								redirect:[uri:"http://identityProvider.test.com/SAML2/Redirect/SSO"], artifact:[uri:"http://identityProvider.test.com/SAML2/SOAP/ArtifactResolution"]]
		params.aa = [create: true, displayName:"test name", description:"test desc", scope:"test.com", crypto:[sig: true, enc:true], attributeservice:[uri:"http://identityProvider.test.com/SAML2/SOAP/AttributeQuery"], attributes:[1, 2]]
		params.contact = [id: contact.id, type: ct.name]
		
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
		def (created, ret) = IDPSSODescriptorService.create(params)
		
		then:
		created

        def identityProvider = ret.identityProvider
        def attributeAuthority = ret.attributeAuthority
		
		identityProvider.organization == organization
		identityProvider.entityDescriptor == entityDescriptor
		identityProvider.entityDescriptor.organization == organization
		identityProvider.collaborator == attributeAuthority
		
		identityProvider.displayName == "test name"
		identityProvider.description == "test desc"
		identityProvider.keyDescriptors == null
		identityProvider.singleSignOnServices.size() == 2
		def sso1 = identityProvider.singleSignOnServices.toList().get(0)
		def sso2 = identityProvider.singleSignOnServices.toList().get(1)
		if(sso1.binding == SamlURI.findByUri(SamlConstants.httpPost)) {
			sso1.location.uri == "http://identityProvider.test.com/SAML2/POST/SSO"
			sso2.location.uri == "http://identityProvider.test.com/SAML2/Redirect/SSO"
		}
		else {
			sso1.location.uri == "http://identityProvider.test.com/SAML2/Redirect/SSO"
			sso2.location.uri == "http://identityProvider.test.com/SAML2/POST/SSO"
		}
				
		identityProvider.artifactResolutionServices.size() == 1
		identityProvider.artifactResolutionServices.toList().get(0).location.uri == "http://identityProvider.test.com/SAML2/SOAP/ArtifactResolution"
		
		wfProcessName == "idpssodescriptor_create"
		
		wfPriority == ProcessPriority.MEDIUM
		wfParams.size() == 5
		wfParams.creator == "${contact.id}"
		wfParams.identityProvider == "${identityProvider.id}"
		wfParams.organization == "${organization.id}"
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
		def ct = ContactType.build()

		params.organization = [id: organization.id]
		params.active = true
		params.cert = pk
		params.entity = [id: entityDescriptor.id]
		params.idp = [displayName:"test name", description:"test desc", scope:"test.com", crypto:[sig: true, enc:true], post:[uri:"http://identityProvider.test.com/SAML2/POST/SSO"], 
								redirect:[uri:"http://identityProvider.test.com/SAML2/Redirect/SSO"], artifact:[uri:"http://identityProvider.test.com/SAML2/SOAP/ArtifactResolution"]]
		params.aa = [create: false]
		params.contact = [id: contact.id, type: ct.name]
		
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
		def (created, ret) = IDPSSODescriptorService.create(params)
		
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
		identityProvider.keyDescriptors.size() == 2
		identityProvider.singleSignOnServices.size() == 2
		def sso1 = identityProvider.singleSignOnServices.toList().get(0)
		def sso2 = identityProvider.singleSignOnServices.toList().get(1)
		if(sso1.binding == SamlURI.findByUri(SamlConstants.httpPost)) {
			sso1.location.uri == "http://identityProvider.test.com/SAML2/POST/SSO"
			sso2.location.uri == "http://identityProvider.test.com/SAML2/Redirect/SSO"
		}
		else {
			sso1.location.uri == "http://identityProvider.test.com/SAML2/Redirect/SSO"
			sso2.location.uri == "http://identityProvider.test.com/SAML2/POST/SSO"
		}
				
		identityProvider.artifactResolutionServices.size() == 1
		identityProvider.artifactResolutionServices.toList().get(0).location.uri == "http://identityProvider.test.com/SAML2/SOAP/ArtifactResolution"
		
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
		def ct = ContactType.build()

		params.organization = [id: organization.id]
		params.active = true
		params.cert = pk
		params.entity = [id: entityDescriptor.id]
		params.idp = [displayName:"test name", description:"test desc", scope:"test.com", crypto:[sig: true, enc:true], post:[uri:"http://identityProvider.test.com/SAML2/POST/SSO"], 
								redirect:[uri:"http://identityProvider.test.com/SAML2/Redirect/SSO"], artifact:[uri:"http://identityProvider.test.com/SAML2/SOAP/ArtifactResolution"]]
		params.contact = [id: contact.id, type: ct.name]
		
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
		def (created, ret) = IDPSSODescriptorService.create(params)
		
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
		identityProvider.keyDescriptors.size() == 2
		identityProvider.singleSignOnServices.size() == 2
		def sso1 = identityProvider.singleSignOnServices.toList().get(0)
		def sso2 = identityProvider.singleSignOnServices.toList().get(1)
		if(sso1.binding == SamlURI.findByUri(SamlConstants.httpPost)) {
			sso1.location.uri == "http://identityProvider.test.com/SAML2/POST/SSO"
			sso2.location.uri == "http://identityProvider.test.com/SAML2/Redirect/SSO"
		}
		else {
			sso1.location.uri == "http://identityProvider.test.com/SAML2/Redirect/SSO"
			sso2.location.uri == "http://identityProvider.test.com/SAML2/POST/SSO"
		}
				
		identityProvider.artifactResolutionServices.size() == 1
		identityProvider.artifactResolutionServices.toList().get(0).location.uri == "http://identityProvider.test.com/SAML2/SOAP/ArtifactResolution"
		
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
		def ct = ContactType.build()

		params.organization = [id: organization.id]
		params.active = true
		params.cert = pk
		params.entity = [id: entityDescriptor.id]
		params.idp = [displayName:"test name", description:"test desc", scope:"test.com", crypto:[sig: true, enc:true], post:[uri:"http://identityProvider.test.com/SAML2/POST/SSO"], 
								redirect:[uri:"http://identityProvider.test.com/SAML2/Redirect/SSO"], artifact:[uri:"http://identityProvider.test.com/SAML2/SOAP/ArtifactResolution"]]
		params.aa = [create: true, crypto:[sig: true, enc:true], attributeservice:[uri:"abcd"], attributes:[1, 2]]
		params.contact = [id: contact.id, type: ct.name]
		
		when:
		def (created, ret) = IDPSSODescriptorService.create(params)
		
		then:
		!created

        def identityProvider = ret.identityProvider
        def attributeAuthority = ret.attributeAuthority
		
		identityProvider.organization == organization
		identityProvider.entityDescriptor == entityDescriptor
		identityProvider.entityDescriptor.organization == organization
		
		identityProvider.displayName == "test name"
		identityProvider.description == "test desc"
		identityProvider.keyDescriptors.size() == 2
		identityProvider.keyDescriptors.toList().get(0).keyInfo.certificate.data == pk
		identityProvider.keyDescriptors.toList().get(1).keyInfo.certificate.data == pk
		
		identityProvider.singleSignOnServices.size() == 2
		
		identityProvider.singleSignOnServices.contains(ret.httpPost)
		ret.httpPost.location.uri == "http://identityProvider.test.com/SAML2/POST/SSO"

		
		identityProvider.singleSignOnServices.contains(ret.httpRedirect)
		ret.httpRedirect.location.uri == "http://identityProvider.test.com/SAML2/Redirect/SSO"
		
		identityProvider.artifactResolutionServices.size() == 1
		
		identityProvider.artifactResolutionServices.contains(ret.soapArtifact)
		ret.soapArtifact.location.uri == "http://identityProvider.test.com/SAML2/SOAP/ArtifactResolution"
			
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
		def ct = ContactType.build()

		params.organization = [id: organization.id]
		params.active = true
		params.cert = pk
		params.entity = [id: entityDescriptor.id + 5]
		params.idp = [displayName:"test name", description:"test desc", scope:"test.com", crypto:[sig: true, enc:true], post:[uri:"http://identityProvider.test.com/SAML2/POST/SSO"], 
								redirect:[uri:"http://identityProvider.test.com/SAML2/Redirect/SSO"], artifact:[uri:"http://identityProvider.test.com/SAML2/SOAP/ArtifactResolution"]]
		params.aa = [create: true, displayName:"test name", description:"test desc", scope:"test.com", crypto:[sig: true, enc:true], attributeservice:[uri:"http://identityProvider.test.com/SAML2/SOAP/AttributeQuery"], attributes:[1, 2]]
		params.contact = [id: contact.id, type: ct.name]
		
		when:
		def (created, ret) = IDPSSODescriptorService.create(params)
		
		then:
		!created

        def identityProvider = ret.identityProvider
        def attributeAuthority = ret.attributeAuthority
		
		identityProvider.organization == organization
		identityProvider.entityDescriptor == null
		
		identityProvider.displayName == "test name"
		identityProvider.description == "test desc"
		identityProvider.keyDescriptors.size() == 2
		identityProvider.keyDescriptors.toList().get(0).keyInfo.certificate.data == pk
		identityProvider.keyDescriptors.toList().get(1).keyInfo.certificate.data == pk
		
		identityProvider.singleSignOnServices.size() == 2
		
		identityProvider.singleSignOnServices.contains(ret.httpPost)
		ret.httpPost.location.uri == "http://identityProvider.test.com/SAML2/POST/SSO"

		
		identityProvider.singleSignOnServices.contains(ret.httpRedirect)
		ret.httpRedirect.location.uri == "http://identityProvider.test.com/SAML2/Redirect/SSO"
		
		identityProvider.artifactResolutionServices.size() == 1
		
		identityProvider.artifactResolutionServices.contains(ret.soapArtifact)
		ret.soapArtifact.location.uri == "http://identityProvider.test.com/SAML2/SOAP/ArtifactResolution"		
		
		attributeAuthority.organization == organization
		attributeAuthority.entityDescriptor == null
		attributeAuthority.collaborator == identityProvider

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
		def ct = ContactType.build()

		params.organization = [id: organization.id]
		params.active = true
		params.cert = pk
		params.entity = [identifier:""]
		params.idp = [displayName:"test name", description:"test desc", scope:"test.com", crypto:[sig: true, enc:true], post:[uri:"http://identityProvider.test.com/SAML2/POST/SSO"], 
								redirect:[uri:"http://identityProvider.test.com/SAML2/Redirect/SSO"], artifact:[uri:"http://identityProvider.test.com/SAML2/SOAP/ArtifactResolution"]]
		params.aa = [create: true, displayName:"test name", description:"test desc", scope:"test.com", crypto:[sig: true, enc:true], attributeservice:[uri:"http://identityProvider.test.com/SAML2/SOAP/AttributeQuery"], attributes:[1, 2]]
		params.contact = [id: contact.id, type: ct.name]
		
		when:
		def (created, ret) = IDPSSODescriptorService.create(params)
		
		then:
		!created

        def identityProvider = ret.identityProvider
        def attributeAuthority = ret.attributeAuthority
		
		identityProvider.organization == organization
		identityProvider.entityDescriptor == null
		
		identityProvider.displayName == "test name"
		identityProvider.description == "test desc"
		identityProvider.keyDescriptors.size() == 2
		identityProvider.keyDescriptors.toList().get(0).keyInfo.certificate.data == pk
		identityProvider.keyDescriptors.toList().get(1).keyInfo.certificate.data == pk
		
		identityProvider.singleSignOnServices.size() == 2
		
		identityProvider.singleSignOnServices.contains(ret.httpPost)
		ret.httpPost.location.uri == "http://identityProvider.test.com/SAML2/POST/SSO"

		
		identityProvider.singleSignOnServices.contains(ret.httpRedirect)
		ret.httpRedirect.location.uri == "http://identityProvider.test.com/SAML2/Redirect/SSO"
		
		identityProvider.artifactResolutionServices.size() == 1
		
		identityProvider.artifactResolutionServices.contains(ret.soapArtifact)
		ret.soapArtifact.location.uri == "http://identityProvider.test.com/SAML2/SOAP/ArtifactResolution"		
		
		attributeAuthority.organization == organization
		attributeAuthority.entityDescriptor == null
		attributeAuthority.collaborator == identityProvider

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
		def ct = ContactType.build()

		params.organization = [id: organization.id + 1]
		params.active = true
		params.cert = pk
		params.entity = [identifier: "http://test.example.com"]
		params.idp = [displayName:"test name", description:"test desc", scope:"test.com", crypto:[sig: true, enc:true], post:[uri:"http://identityProvider.test.com/SAML2/POST/SSO"], 
								redirect:[uri:"http://identityProvider.test.com/SAML2/Redirect/SSO"], artifact:[uri:"http://identityProvider.test.com/SAML2/SOAP/ArtifactResolution"]]
		params.aa = [create: true, displayName:"test name", description:"test desc", scope:"test.com", crypto:[sig: true, enc:true], attributeservice:[uri:"http://identityProvider.test.com/SAML2/SOAP/AttributeQuery"], attributes:[1, 2]]
		params.contact = [id: contact.id, type: ct.name]
		
		when:
		def (created, ret) = IDPSSODescriptorService.create(params)
		
		then:
		!created

        def identityProvider = ret.identityProvider
        def attributeAuthority = ret.attributeAuthority
		
		identityProvider.organization == null
		identityProvider.entityDescriptor == null
		
		identityProvider.displayName == "test name"
		identityProvider.description == "test desc"
		identityProvider.keyDescriptors.size() == 2
		identityProvider.keyDescriptors.toList().get(0).keyInfo.certificate.data == pk
		identityProvider.keyDescriptors.toList().get(1).keyInfo.certificate.data == pk
		
		identityProvider.singleSignOnServices.size() == 2
		
		identityProvider.singleSignOnServices.contains(ret.httpPost)
		ret.httpPost.location.uri == "http://identityProvider.test.com/SAML2/POST/SSO"

		
		identityProvider.singleSignOnServices.contains(ret.httpRedirect)
		ret.httpRedirect.location.uri == "http://identityProvider.test.com/SAML2/Redirect/SSO"
		
		identityProvider.artifactResolutionServices.size() == 1
		
		identityProvider.artifactResolutionServices.contains(ret.soapArtifact)
		ret.soapArtifact.location.uri == "http://identityProvider.test.com/SAML2/SOAP/ArtifactResolution"	
		
		attributeAuthority.organization == null
		attributeAuthority.entityDescriptor == null
		attributeAuthority.collaborator == identityProvider

	}
	
	def "Updating an existing identity provider with valid changed content succeeds"() {
		setup:
		def organization = Organization.build()
		def entityDescriptor = EntityDescriptor.build(organization:organization)
		def idp = IDPSSODescriptor.build(entityDescriptor:entityDescriptor, active:false, wantAuthnRequestsSigned:false)
		params.id = idp.id
		params.idp = [displayName:"new displayName", description:"new description", scope:"test.com", status:'true', wantauthnrequestssigned:true]
		
		when:
		def (updated, identityProvider_) = IDPSSODescriptorService.update(params)
		
		then:
		updated
		
		identityProvider_.displayName == "new displayName"
		identityProvider_.description == "new description"
		identityProvider_.active
		identityProvider_.wantAuthnRequestsSigned
	}
	
	def "Updating an existing identity provider with invalid changed content fails"() {
		setup:
		def organization = Organization.build()
		def entityDescriptor = EntityDescriptor.build(organization:organization)
		def idp = IDPSSODescriptor.build(entityDescriptor:entityDescriptor, active:false, wantAuthnRequestsSigned:false)
		params.id = idp.id
		params.idp = [displayName:"", description:"new description", status:'true', wantauthnrequestssigned:false]
		
		when:
		def (updated, identityProvider_) = IDPSSODescriptorService.update(params)
		
		then:
		!updated
		
		identityProvider_.displayName == ""
		identityProvider_.description == "new description"
		identityProvider_.active
		!identityProvider_.wantAuthnRequestsSigned
	}

}