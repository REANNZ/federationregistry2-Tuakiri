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
	def idpssoDescriptorService
	def params
	
	def setup () {
		savedMetaClasses = [:]
		
		SpecHelpers.registerMetaClass(WorkflowProcessService, savedMetaClasses)
		workflowProcessService.metaClass = WorkflowProcessService.metaClass
		
		idpssoDescriptorService = new IDPSSODescriptorService(cryptoService:cryptoService, workflowProcessService:workflowProcessService, entityDescriptorService:entityDescriptorService)
		def user = UserBase.build()
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

		def saml2Prot = SamlURI.build(uri:'urn:oasis:names:tc:SAML:2.0:protocol').save()
		def organization = Organization.build().save()
		def attr1 = AttributeBase.build(name: "attr1").save()
		def attr2 = AttributeBase.build(name: "attr2").save()
		def nameID1 = SamlURI.build().save()
		def nameID2 = SamlURI.build().save()
		def pk = loadPK()
		def ct = ContactType.build().save()

		params.organization = [id: organization.id]
		params.active = true
		params.entity = [identifier:"http://identityProvider.test.com"]
		params.idp = [displayName:"test name", description:"test desc", attributes:[(attr1.id):'on', (attr2.id):'on'], nameidformats:[(nameID1.id):'on', (nameID2.id):'on'], crypto:[sig: true, sigdata:pk, enc:true, encdata:pk], post:[uri:"http://identityProvider.test.com/SAML2/POST/SSO"], 
								redirect:[uri:"http://identityProvider.test.com/SAML2/Redirect/SSO"], artifact:[uri:"http://identityProvider.test.com/SAML2/SOAP/ArtifactResolution"]]
		params.aa = [create: true, displayName:"test name", description:"test desc", crypto:[sig: true, sigdata:pk, enc:true, encdata:pk], attributeservice:[uri:"http://identityProvider.test.com/SAML2/SOAP/AttributeQuery"], attributes:[(attr1.id):'on', (attr2.id):'on']]
		params.contact = [givenName:"Bradley", surname:"Beddoes", email:"bradleybeddoes@intient.com", type:ct.name]
		
		def wfProcessName, wfDescription, wfPriority, wfParams
		
		when:
		WorkflowProcessService.metaClass.initiate =  { String processName, String instanceDescription, ProcessPriority priority, Map params ->
			wfProcessName = processName
			wfDescription = instanceDescription
			wfPriority = priority
			wfParams = params
			[:]
		}
		WorkflowProcessService.metaClass.run = { def processInstance -> }
		def (created, org, entityDescriptor_, identityProvider, attributeAuthority, httpPost, httpRedirect, soapArtifact, organizationList, attributeList, nameIDFormatList, contact_) = idpssoDescriptorService.create(params)
		
		then:
		created
		
		Organization.count() == 1
		SamlURI.count() == 7
		EntityDescriptor.count() == 1
		IDPSSODescriptor.count() == 1
		AttributeAuthorityDescriptor.count() == 1
		
		def entityDescriptor = EntityDescriptor.list().get(0)
		entityDescriptor.entityID == "http://identityProvider.test.com"
		
		def idp = IDPSSODescriptor.list().get(0)
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
		identityProvider.contacts.toList().get(0).contact.givenName == "Bradley"
		identityProvider.contacts.toList().get(0).contact.surname == "Beddoes"
		identityProvider.contacts.toList().get(0).contact.email.uri == "bradleybeddoes@intient.com"
		
		def aa = AttributeAuthorityDescriptor.list().get(0)
		attributeAuthority.organization == organization
		attributeAuthority.entityDescriptor == entityDescriptor
		attributeAuthority.collaborator == identityProvider
		identityProvider.collaborator == attributeAuthority

		
		attributeAuthority.attributes != null
		attributeAuthority.attributes.size() == 2
		
		wfProcessName == "idpssodescriptor_create"
		
		wfPriority == ProcessPriority.MEDIUM
		wfParams.size() == 4
		wfParams.identityProvider == identityProvider.id
		wfParams.organization == organization.name
	}
	
	def "Create succeeds when valid initial IDPSSODescriptor and AttributeAuthorityDescriptor data are provided (without existing EntityDescriptor but with existing contact)"() {
		setup:
		setupBindings()
		setupCrypto()

		def saml2Prot = SamlURI.build(uri:'urn:oasis:names:tc:SAML:2.0:protocol').save()
		def organization = Organization.build().save()
		def attr1 = AttributeBase.build(name: "attr1").save()
		def attr2 = AttributeBase.build(name: "attr2").save()
		def nameID1 = SamlURI.build().save()
		def nameID2 = SamlURI.build().save()
		def pk = loadPK()
		def contact = Contact.build(organization: organization).save()
		def ct = ContactType.build().save()

		params.organization = [id: organization.id]
		params.active = true
		params.entity = [identifier:"http://identityProvider.test.com"]
		params.idp = [displayName:"test name", description:"test desc", attributes:[(attr1.id):'on', (attr2.id):'on'], nameidformats:[(nameID1.id):'on', (nameID2.id):'on'], crypto:[sig: true, sigdata:pk, enc:true, encdata:pk], post:[uri:"http://identityProvider.test.com/SAML2/POST/SSO"], 
								redirect:[uri:"http://identityProvider.test.com/SAML2/Redirect/SSO"], artifact:[uri:"http://identityProvider.test.com/SAML2/SOAP/ArtifactResolution"]]
		params.aa = [create: true, displayName:"test name", description:"test desc", crypto:[sig: true, sigdata:pk, enc:true, encdata:pk], attributeservice:[uri:"http://identityProvider.test.com/SAML2/SOAP/AttributeQuery"], attributes:[(attr1.id):'on', (attr2.id):'on']]
		params.contact = [id: contact.id, type: ct.name]
		
		def wfProcessName, wfDescription, wfPriority, wfParams
		
		when:
		WorkflowProcessService.metaClass.initiate =  { String processName, String instanceDescription, ProcessPriority priority, Map params ->
			wfProcessName = processName
			wfDescription = instanceDescription
			wfPriority = priority
			wfParams = params
			[:]
		}
		WorkflowProcessService.metaClass.run = { def processInstance -> }
		def (created, org, entityDescriptor_, identityProvider, attributeAuthority, httpPost, httpRedirect, soapArtifact, organizationList, attributeList, nameIDFormatList, contact_) = idpssoDescriptorService.create(params)
		
		then:
		created
		
		Organization.count() == 1
		SamlURI.count() == 7
		EntityDescriptor.count() == 1
		IDPSSODescriptor.count() == 1
		AttributeAuthorityDescriptor.count() == 1
		
		def entityDescriptor = EntityDescriptor.list().get(0)
		entityDescriptor.entityID == "http://identityProvider.test.com"
		
		def idp = IDPSSODescriptor.list().get(0)
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
		
		def aa = AttributeAuthorityDescriptor.list().get(0)
		attributeAuthority.organization == organization
		attributeAuthority.entityDescriptor == entityDescriptor
		attributeAuthority.collaborator == identityProvider
		identityProvider.collaborator == attributeAuthority

		
		attributeAuthority.attributes != null
		attributeAuthority.attributes.size() == 2
		
		wfProcessName == "idpssodescriptor_create"
		
		wfPriority == ProcessPriority.MEDIUM
		wfParams.size() == 4
		wfParams.creator == contact.id
		wfParams.identityProvider == identityProvider.id
		wfParams.organization == organization.name
	}
	
	def "Create succeeds when valid initial IDPSSODescriptor and AttributeAuthorityDescriptor data are provided (with existing EntityDescriptor)"() {
		setup:
		setupBindings()
		setupCrypto()

		def saml2Prot = SamlURI.build(uri:'urn:oasis:names:tc:SAML:2.0:protocol').save()
		def organization = Organization.build().save()
		def entityDescriptor = EntityDescriptor.build(organization:organization).save()
		def attr1 = AttributeBase.build(name: "attr1").save()
		def attr2 = AttributeBase.build(name: "attr2").save()
		def pk = loadPK()
		def contact = Contact.build(organization: organization).save()
		def ct = ContactType.build().save()

		params.organization = [id: organization.id]
		params.active = true
		params.entity = [id: entityDescriptor.id]
		params.idp = [displayName:"test name", description:"test desc", attributes:[(attr1.id):'on', (attr2.id):'on'], crypto:[sig: true, sigdata:pk, enc:true, encdata:pk], post:[uri:"http://identityProvider.test.com/SAML2/POST/SSO"], 
								redirect:[uri:"http://identityProvider.test.com/SAML2/Redirect/SSO"], artifact:[uri:"http://identityProvider.test.com/SAML2/SOAP/ArtifactResolution"]]
		params.aa = [create: true, displayName:"test name", description:"test desc", crypto:[sig: true, sigdata:pk, enc:true, encdata:pk], attributeservice:[uri:"http://identityProvider.test.com/SAML2/SOAP/AttributeQuery"], attributes:[(attr1.id):'on', (attr2.id):'on']]
		params.contact = [id: contact.id, type: ct.name]
		
		def wfProcessName, wfDescription, wfPriority, wfParams
		
		when:
		WorkflowProcessService.metaClass.initiate =  { String processName, String instanceDescription, ProcessPriority priority, Map params ->
			wfProcessName = processName
			wfDescription = instanceDescription
			wfPriority = priority
			wfParams = params
			[:]
		}
		WorkflowProcessService.metaClass.run = { def processInstance -> }
		def (created, org, entityDescriptor_, identityProvider, attributeAuthority, httpPost, httpRedirect, soapArtifact, organizationList, attributeList, nameIDFormatList, contact_) = idpssoDescriptorService.create(params)
		
		then:
		created
		
		Organization.count() == 1
		SamlURI.count() == 5
		EntityDescriptor.count() == 1
		IDPSSODescriptor.count() == 1
		AttributeAuthorityDescriptor.count() == 1

		def idp = IDPSSODescriptor.list().get(0)
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
		
		def aa = AttributeAuthorityDescriptor.list().get(0)
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
		wfParams.size() == 4
		wfParams.creator == contact.id
		wfParams.identityProvider == identityProvider.id
		wfParams.organization == organization.name
	}
	
	def "Create fails when IDPSSODescriptor fails constraints even though a valid AttributeAuthorityDescriptor is provided"() {
		setup:
		setupBindings()
		setupCrypto()

		def saml2Prot = SamlURI.build(uri:'urn:oasis:names:tc:SAML:2.0:protocol').save()
		def organization = Organization.build().save()
		def entityDescriptor = EntityDescriptor.build(organization:organization).save()
		def attr1 = Attribute.build().save()
		def attr2 = Attribute.build().save()
		def pk = loadPK()
		def contact = Contact.build(organization: organization).save()
		def ct = ContactType.build().save()

		params.organization = [id: organization.id]
		params.active = true
		params.entity = [id: entityDescriptor.id]
		params.idp = [description:"test desc", crypto:[sig: true, sigdata:pk, enc:true, encdata:pk], post:[uri:"http://identityProvider.test.com/SAML2/POST/SSO"], 
								redirect:[uri:"http://identityProvider.test.com/SAML2/Redirect/SSO"], artifact:[uri:"http://identityProvider.test.com/SAML2/SOAP/ArtifactResolution"]]
		params.aa = [create: true, displayName:"test name", description:"test desc", crypto:[sig: true, sigdata:pk, enc:true, encdata:pk], attributeservice:[uri:"http://identityProvider.test.com/SAML2/SOAP/AttributeQuery"], attributes:[1, 2]]
		params.contact = [id: contact.id, type: ct.name]
		
		when:
		def (created, org, entityDescriptor_, identityProvider, attributeAuthority, httpPost, httpRedirect, soapArtifact, organizationList, attributeList, nameIDFormatList, contact_) = idpssoDescriptorService.create(params)
		
		then:
		!created
		
		Organization.count() == 1
		SamlURI.count() == 5
		EntityDescriptor.count() == 1
		IDPSSODescriptor.count() == 0
		AttributeAuthorityDescriptor.count() == 0

		identityProvider.organization == organization
		identityProvider.entityDescriptor == entityDescriptor
		identityProvider.entityDescriptor.organization == organization
		
		identityProvider.displayName == null
		identityProvider.description == "test desc"
		identityProvider.keyDescriptors.size() == 2
		identityProvider.keyDescriptors.toList().get(0).keyInfo.certificate.data == pk
		identityProvider.keyDescriptors.toList().get(1).keyInfo.certificate.data == pk
		
		identityProvider.singleSignOnServices.size() == 2
		
		identityProvider.singleSignOnServices.contains(httpPost)
		httpPost.location.uri == "http://identityProvider.test.com/SAML2/POST/SSO"
		
		
		identityProvider.singleSignOnServices.contains(httpRedirect)
		httpRedirect.location.uri == "http://identityProvider.test.com/SAML2/Redirect/SSO"
		
		identityProvider.artifactResolutionServices.size() == 1
		
		identityProvider.artifactResolutionServices.contains(soapArtifact)
		soapArtifact.location.uri == "http://identityProvider.test.com/SAML2/SOAP/ArtifactResolution"
		
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

		def saml2Prot = SamlURI.build(uri:'urn:oasis:names:tc:SAML:2.0:protocol').save()
		def organization = Organization.build().save()
		def entityDescriptor = EntityDescriptor.build(organization:organization).save()
		def attr1 = Attribute.build().save()
		def attr2 = Attribute.build().save()
		def pk = loadPK()
		def contact = Contact.build(organization: organization).save()
		def ct = ContactType.build().save()

		params.organization = [id: organization.id]
		params.active = true
		params.entity = [id: entityDescriptor.id]
		params.idp = [displayName: "test name", description:"test desc", crypto:[sig: true, sigdata:pk, enc:true, encdata:pk], post:[uri:"/SAML2/POST/SSO"], 
								redirect:[uri:"http://identityProvider.test.com/SAML2/Redirect/SSO"], artifact:[uri:"http://identityProvider.test.com/SAML2/SOAP/ArtifactResolution"]]
		params.aa = [create: true, displayName:"test name", description:"test desc", crypto:[sig: true, sigdata:pk, enc:true, encdata:pk], attributeservice:[uri:"http://identityProvider.test.com/SAML2/SOAP/AttributeQuery"], attributes:[1, 2]]
		params.contact = [id: contact.id, type: ct.name]
		
		when:
		def (created, org, entityDescriptor_, identityProvider, attributeAuthority, httpPost, httpRedirect, soapArtifact, organizationList, attributeList, nameIDFormatList, contact_) = idpssoDescriptorService.create(params)
		
		then:
		!created
		
		println "ID IS -- " + identityProvider.id
		
		Organization.count() == 1
		SamlURI.count() == 5
		EntityDescriptor.count() == 1
		IDPSSODescriptor.count() == 0
		AttributeAuthorityDescriptor.count() == 0

		identityProvider.organization == organization
		identityProvider.entityDescriptor == entityDescriptor
		identityProvider.entityDescriptor.organization == organization
		
		identityProvider.displayName == "test name"
		identityProvider.description == "test desc"
		identityProvider.keyDescriptors.size() == 2
		identityProvider.keyDescriptors.toList().get(0).keyInfo.certificate.data == pk
		identityProvider.keyDescriptors.toList().get(1).keyInfo.certificate.data == pk
		
		identityProvider.singleSignOnServices.size() == 2
		
		identityProvider.singleSignOnServices.contains(httpPost)
		httpPost.location.uri == "/SAML2/POST/SSO"
		httpPost.errors.getErrorCount() == 1
		httpPost.errors.getFieldError('location.uri').code == 'url.invalid'
		
		
		identityProvider.singleSignOnServices.contains(httpRedirect)
		httpRedirect.location.uri == "http://identityProvider.test.com/SAML2/Redirect/SSO"
		
		identityProvider.artifactResolutionServices.size() == 1
		
		identityProvider.artifactResolutionServices.contains(soapArtifact)
		soapArtifact.location.uri == "http://identityProvider.test.com/SAML2/SOAP/ArtifactResolution"
		
		
		attributeAuthority.organization == organization
		attributeAuthority.entityDescriptor == entityDescriptor
		attributeAuthority.collaborator == identityProvider

	}
	
	def "Create fails when IDPSSODescriptor post endpoint not supplied"() {
		setup:
		setupBindings()
		setupCrypto()

		def saml2Prot = SamlURI.build(uri:'urn:oasis:names:tc:SAML:2.0:protocol').save()
		def organization = Organization.build().save()
		def entityDescriptor = EntityDescriptor.build(organization:organization).save()
		def attr1 = Attribute.build().save()
		def attr2 = Attribute.build().save()
		def pk = loadPK()
		def contact = Contact.build(organization: organization).save()
		def ct = ContactType.build().save()

		params.organization = [id: organization.id]
		params.active = true
		params.entity = [id: entityDescriptor.id]
		params.idp = [displayName: "test name", description:"test desc", crypto:[sig: true, sigdata:pk, enc:true, encdata:pk], 
								redirect:[uri:"http://identityProvider.test.com/SAML2/Redirect/SSO"], artifact:[uri:"http://identityProvider.test.com/SAML2/SOAP/ArtifactResolution"]]
		params.aa = [create: true, displayName:"test name", description:"test desc", crypto:[sig: true, sigdata:pk, enc:true, encdata:pk], attributeservice:[uri:"http://identityProvider.test.com/SAML2/SOAP/AttributeQuery"], attributes:[1, 2]]
		params.contact = [id: contact.id, type: ct.name]
		
		when:
		def (created, org, entityDescriptor_, identityProvider, attributeAuthority, httpPost, httpRedirect, soapArtifact, organizationList, attributeList, nameIDFormatList, contact_) = idpssoDescriptorService.create(params)
		
		then:
		!created
		
		Organization.count() == 1
		SamlURI.count() == 5
		EntityDescriptor.count() == 1
		IDPSSODescriptor.count() == 0
		AttributeAuthorityDescriptor.count() == 0

		
		identityProvider.organization == organization
		identityProvider.entityDescriptor == entityDescriptor
		identityProvider.entityDescriptor.organization == organization
		
		identityProvider.displayName == "test name"
		identityProvider.description == "test desc"
		identityProvider.keyDescriptors.size() == 2
		identityProvider.keyDescriptors.toList().get(0).keyInfo.certificate.data == pk
		identityProvider.keyDescriptors.toList().get(1).keyInfo.certificate.data == pk
		
		identityProvider.singleSignOnServices.size() == 2
		
		identityProvider.singleSignOnServices.contains(httpPost)
		httpPost.location.uri == null
		httpPost.errors.getErrorCount() == 1
		httpPost.errors.getFieldError('location.uri').code == 'nullable'
		identityProvider.errors.getErrorCount() == 1
		identityProvider.errors.getFieldError('singleSignOnServices.location.uri').code == 'nullable'
		
		
		identityProvider.singleSignOnServices.contains(httpRedirect)
		httpRedirect.location.uri == "http://identityProvider.test.com/SAML2/Redirect/SSO"
		
		identityProvider.artifactResolutionServices.size() == 1
		
		identityProvider.artifactResolutionServices.contains(soapArtifact)
		soapArtifact.location.uri == "http://identityProvider.test.com/SAML2/SOAP/ArtifactResolution"
		
		
		attributeAuthority.organization == organization
		attributeAuthority.entityDescriptor == entityDescriptor
		attributeAuthority.collaborator == identityProvider

	}
	
	def "Create fails when IDPSSODescriptor redirect endpoint fails constraints"() {
		setup:
		setupBindings()
		setupCrypto()

		def saml2Prot = SamlURI.build(uri:'urn:oasis:names:tc:SAML:2.0:protocol').save()
		def organization = Organization.build().save()
		def entityDescriptor = EntityDescriptor.build(organization:organization).save()
		def attr1 = Attribute.build().save()
		def attr2 = Attribute.build().save()
		def pk = loadPK()
		def contact = Contact.build(organization: organization).save()
		def ct = ContactType.build().save()

		params.organization = [id: organization.id]
		params.active = true
		params.entity = [id: entityDescriptor.id]
		params.idp = [displayName: "test name", description:"test desc", crypto:[sig: true, sigdata:pk, enc:true, encdata:pk], post:[uri:"http://identityProvider.test.com/SAML2/POST/SSO"], 
								redirect:[uri:"asdfasdasdf"], artifact:[uri:"http://identityProvider.test.com/SAML2/SOAP/ArtifactResolution"]]
		params.aa = [create: true, displayName:"test name", description:"test desc", crypto:[sig: true, sigdata:pk, enc:true, encdata:pk], attributeservice:[uri:"http://identityProvider.test.com/SAML2/SOAP/AttributeQuery"], attributes:[1, 2]]
		params.contact = [id: contact.id, type: ct.name]
		
		when:
		def (created, org, entityDescriptor_, identityProvider, attributeAuthority, httpPost, httpRedirect, soapArtifact, organizationList, attributeList, nameIDFormatList, contact_) = idpssoDescriptorService.create(params)
		
		then:
		!created
		
		Organization.count() == 1
		SamlURI.count() == 5
		EntityDescriptor.count() == 1
		IDPSSODescriptor.count() == 0
		AttributeAuthorityDescriptor.count() == 0

		
		identityProvider.organization == organization
		identityProvider.entityDescriptor == entityDescriptor
		identityProvider.entityDescriptor.organization == organization
		
		identityProvider.displayName == "test name"
		identityProvider.description == "test desc"
		identityProvider.keyDescriptors.size() == 2
		identityProvider.keyDescriptors.toList().get(0).keyInfo.certificate.data == pk
		identityProvider.keyDescriptors.toList().get(1).keyInfo.certificate.data == pk
		
		identityProvider.singleSignOnServices.size() == 2
		
		identityProvider.singleSignOnServices.contains(httpPost)
		httpPost.location.uri == "http://identityProvider.test.com/SAML2/POST/SSO"

		
		identityProvider.singleSignOnServices.contains(httpRedirect)
		httpRedirect.location.uri == "asdfasdasdf"
		httpRedirect.errors.getErrorCount() == 1
		httpRedirect.errors.getFieldError('location.uri').code == 'url.invalid'
		
		identityProvider.artifactResolutionServices.size() == 1
		
		identityProvider.artifactResolutionServices.contains(soapArtifact)
		soapArtifact.location.uri == "http://identityProvider.test.com/SAML2/SOAP/ArtifactResolution"
		
		
		attributeAuthority.organization == organization
		attributeAuthority.entityDescriptor == entityDescriptor
		attributeAuthority.collaborator == identityProvider

	}
	
	def "Create fails when IDPSSODescriptor redirect endpoint not supplied"() {
		setup:
		setupBindings()
		setupCrypto()

		def saml2Prot = SamlURI.build(uri:'urn:oasis:names:tc:SAML:2.0:protocol').save()
		def organization = Organization.build().save()
		def entityDescriptor = EntityDescriptor.build(organization:organization).save()
		def attr1 = Attribute.build().save()
		def attr2 = Attribute.build().save()
		def pk = loadPK()
		def contact = Contact.build(organization: organization).save()
		def ct = ContactType.build().save()

		params.organization = [id: organization.id]
		params.active = true
		params.entity = [id: entityDescriptor.id]
		params.idp = [displayName: "test name", description:"test desc", crypto:[sig: true, sigdata:pk, enc:true, encdata:pk], post:[uri:"http://identityProvider.test.com/SAML2/POST/SSO"],
								artifact:[uri:"http://identityProvider.test.com/SAML2/SOAP/ArtifactResolution"]]
		params.aa = [create: true, displayName:"test name", description:"test desc", crypto:[sig: true, sigdata:pk, enc:true, encdata:pk], attributeservice:[uri:"http://identityProvider.test.com/SAML2/SOAP/AttributeQuery"], attributes:[1, 2]]
		params.contact = [id: contact.id, type: ct.name]
		
		when:
		def (created, org, entityDescriptor_, identityProvider, attributeAuthority, httpPost, httpRedirect, soapArtifact, organizationList, attributeList, nameIDFormatList, contact_) = idpssoDescriptorService.create(params)
		
		then:
		!created
		
		Organization.count() == 1
		SamlURI.count() == 5
		EntityDescriptor.count() == 1
		IDPSSODescriptor.count() == 0
		AttributeAuthorityDescriptor.count() == 0

		
		identityProvider.organization == organization
		identityProvider.entityDescriptor == entityDescriptor
		identityProvider.entityDescriptor.organization == organization
		
		identityProvider.displayName == "test name"
		identityProvider.description == "test desc"
		identityProvider.keyDescriptors.size() == 2
		identityProvider.keyDescriptors.toList().get(0).keyInfo.certificate.data == pk
		identityProvider.keyDescriptors.toList().get(1).keyInfo.certificate.data == pk
		
		identityProvider.singleSignOnServices.size() == 2
		
		identityProvider.singleSignOnServices.contains(httpPost)
		httpPost.location.uri == "http://identityProvider.test.com/SAML2/POST/SSO"

		
		identityProvider.singleSignOnServices.contains(httpRedirect)
		httpRedirect.location.uri == null
		httpRedirect.errors.getErrorCount() == 1
		httpRedirect.errors.getFieldError('location.uri').code == 'nullable'
		identityProvider.errors.getErrorCount() == 1
		identityProvider.errors.getFieldError('singleSignOnServices.location.uri').code == 'nullable'
		
		identityProvider.artifactResolutionServices.size() == 1
		
		identityProvider.artifactResolutionServices.contains(soapArtifact)
		soapArtifact.location.uri == "http://identityProvider.test.com/SAML2/SOAP/ArtifactResolution"
		
		
		attributeAuthority.organization == organization
		attributeAuthority.entityDescriptor == entityDescriptor
		attributeAuthority.collaborator == identityProvider

	}
	
	def "Create fails when IDPSSODescriptor artifact endpoint fails constraints"() {
		setup:
		setupBindings()
		setupCrypto()

		def saml2Prot = SamlURI.build(uri:'urn:oasis:names:tc:SAML:2.0:protocol').save()
		def organization = Organization.build().save()
		def entityDescriptor = EntityDescriptor.build(organization:organization).save()
		def attr1 = Attribute.build().save()
		def attr2 = Attribute.build().save()
		def pk = loadPK()
		def contact = Contact.build(organization: organization).save()
		def ct = ContactType.build().save()

		params.organization = [id: organization.id]
		params.active = true
		params.entity = [id: entityDescriptor.id]
		params.idp = [displayName: "test name", description:"test desc", crypto:[sig: true, sigdata:pk, enc:true, encdata:pk], post:[uri:"http://identityProvider.test.com/SAML2/POST/SSO"], 
								redirect:[uri:"http://identityProvider.test.com/SAML2/Redirect/SSO"], artifact:[uri:"/SAML2/SOAP/ArtifactResolution"]]
		params.aa = [create: true, displayName:"test name", description:"test desc", crypto:[sig: true, sigdata:pk, enc:true, encdata:pk], attributeservice:[uri:"http://identityProvider.test.com/SAML2/SOAP/AttributeQuery"], attributes:[1, 2]]
		params.contact = [id: contact.id, type: ct.name]
		
		when:
		def (created, org, entityDescriptor_, identityProvider, attributeAuthority, httpPost, httpRedirect, soapArtifact, organizationList, attributeList, nameIDFormatList, contact_) = idpssoDescriptorService.create(params)
		
		then:
		!created
		
		Organization.count() == 1
		SamlURI.count() == 5
		EntityDescriptor.count() == 1
		IDPSSODescriptor.count() == 0
		AttributeAuthorityDescriptor.count() == 0

		
		identityProvider.organization == organization
		identityProvider.entityDescriptor == entityDescriptor
		identityProvider.entityDescriptor.organization == organization
		
		identityProvider.displayName == "test name"
		identityProvider.description == "test desc"
		identityProvider.keyDescriptors.size() == 2
		identityProvider.keyDescriptors.toList().get(0).keyInfo.certificate.data == pk
		identityProvider.keyDescriptors.toList().get(1).keyInfo.certificate.data == pk
		
		identityProvider.singleSignOnServices.size() == 2
		
		identityProvider.singleSignOnServices.contains(httpPost)
		httpPost.location.uri == "http://identityProvider.test.com/SAML2/POST/SSO"

		
		identityProvider.singleSignOnServices.contains(httpRedirect)
		httpRedirect.location.uri == "http://identityProvider.test.com/SAML2/Redirect/SSO"
		
		identityProvider.artifactResolutionServices.size() == 1
		
		identityProvider.artifactResolutionServices.contains(soapArtifact)
		soapArtifact.location.uri == "/SAML2/SOAP/ArtifactResolution"
		soapArtifact.errors.getErrorCount() == 1
		soapArtifact.errors.getFieldError('location.uri').code == 'url.invalid'
		
		
		attributeAuthority.organization == organization
		attributeAuthority.entityDescriptor == entityDescriptor
		attributeAuthority.collaborator == identityProvider

	}
	
	def "Create fails when IDPSSODescriptor artifact endpoint not supplied"() {
		setup:
		setupBindings()
		setupCrypto()

		def saml2Prot = SamlURI.build(uri:'urn:oasis:names:tc:SAML:2.0:protocol').save()
		def organization = Organization.build().save()
		def entityDescriptor = EntityDescriptor.build(organization:organization).save()
		def attr1 = Attribute.build().save()
		def attr2 = Attribute.build().save()
		def pk = loadPK()
		def contact = Contact.build(organization: organization).save()
		def ct = ContactType.build().save()

		params.organization = [id: organization.id]
		params.active = true
		params.entity = [id: entityDescriptor.id]
		params.idp = [displayName: "test name", description:"test desc", crypto:[sig: true, sigdata:pk, enc:true, encdata:pk], post:[uri:"http://identityProvider.test.com/SAML2/POST/SSO"],
								redirect:[uri:"http://identityProvider.test.com/SAML2/Redirect/SSO"]]
		params.aa = [create: true, displayName:"test name", description:"test desc", crypto:[sig: true, sigdata:pk, enc:true, encdata:pk], attributeservice:[uri:"http://identityProvider.test.com/SAML2/SOAP/AttributeQuery"], attributes:[1, 2]]
		params.contact = [id: contact.id, type: ct.name]
		
		when:
		def (created, org, entityDescriptor_, identityProvider, attributeAuthority, httpPost, httpRedirect, soapArtifact, organizationList, attributeList, nameIDFormatList, contact_) = idpssoDescriptorService.create(params)
		
		then:
		!created
		
		Organization.count() == 1
		SamlURI.count() == 5
		EntityDescriptor.count() == 1
		IDPSSODescriptor.count() == 0
		AttributeAuthorityDescriptor.count() == 0

		
		identityProvider.organization == organization
		identityProvider.entityDescriptor == entityDescriptor
		identityProvider.entityDescriptor.organization == organization
		
		identityProvider.displayName == "test name"
		identityProvider.description == "test desc"
		identityProvider.keyDescriptors.size() == 2
		identityProvider.keyDescriptors.toList().get(0).keyInfo.certificate.data == pk
		identityProvider.keyDescriptors.toList().get(1).keyInfo.certificate.data == pk
		
		identityProvider.singleSignOnServices.size() == 2
		
		identityProvider.singleSignOnServices.contains(httpPost)
		httpPost.location.uri == "http://identityProvider.test.com/SAML2/POST/SSO"

		
		identityProvider.singleSignOnServices.contains(httpRedirect)
		httpRedirect.location.uri == "http://identityProvider.test.com/SAML2/Redirect/SSO"
		
		identityProvider.artifactResolutionServices.size() == 1
		
		identityProvider.artifactResolutionServices.contains(soapArtifact)
		soapArtifact.location.uri == null
		soapArtifact.errors.getErrorCount() == 1
		soapArtifact.errors.getFieldError('location.uri').code == 'nullable'
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

		def saml2Prot = SamlURI.build(uri:'urn:oasis:names:tc:SAML:2.0:protocol').save()
		def organization = Organization.build().save()
		def entityDescriptor = EntityDescriptor.build(organization:organization).save()
		def attr1 = Attribute.build().save()
		def attr2 = Attribute.build().save()
		def pk = loadPK()
		def contact = Contact.build(organization: organization).save()
		def ct = ContactType.build().save()

		params.organization = [id: organization.id]
		params.active = true
		params.entity = [id: entityDescriptor.id]
		params.idp = [displayName: "test name", description:"test desc",  post:[uri:"http://identityProvider.test.com/SAML2/POST/SSO"],
								redirect:[uri:"http://identityProvider.test.com/SAML2/Redirect/SSO"], artifact:[uri:"http://identityProvider.test.com/SAML2/SOAP/ArtifactResolution"]]
		params.aa = [create: true, displayName:"test name", description:"test desc", crypto:[sig: true, sigdata:pk, enc:true, encdata:pk], attributeservice:[uri:"http://identityProvider.test.com/SAML2/SOAP/AttributeQuery"], attributes:[1, 2]]
		params.contact = [id: contact.id, type: ct.name]
		
		def wfProcessName, wfDescription, wfPriority, wfParams
		
		when:
		WorkflowProcessService.metaClass.initiate =  { String processName, String instanceDescription, ProcessPriority priority, Map params ->
			wfProcessName = processName
			wfDescription = instanceDescription
			wfPriority = priority
			wfParams = params
			[:]
		}
		WorkflowProcessService.metaClass.run = { def processInstance -> }
		def (created, org, entityDescriptor_, identityProvider, attributeAuthority, httpPost, httpRedirect, soapArtifact, organizationList, attributeList, nameIDFormatList, contact_) = idpssoDescriptorService.create(params)
		
		then:
		created
		
		Organization.count() == 1
		SamlURI.count() == 5
		EntityDescriptor.count() == 1
		IDPSSODescriptor.count() == 1
		AttributeAuthorityDescriptor.count() == 1

		def idp = IDPSSODescriptor.list().get(0)
		def aa = AttributeAuthorityDescriptor.list().get(0)
		
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
		wfParams.size() == 4
		wfParams.creator == contact.id
		wfParams.identityProvider == identityProvider.id
		wfParams.organization == organization.name
	}
	
	def "Create succeeds when AttributeAuthorityDescriptor not required"() {
		setup:
		setupBindings()
		setupCrypto()

		def saml2Prot = SamlURI.build(uri:'urn:oasis:names:tc:SAML:2.0:protocol').save()
		def organization = Organization.build().save()
		def entityDescriptor = EntityDescriptor.build(organization:organization).save()
		def attr1 = Attribute.build().save()
		def attr2 = Attribute.build().save()
		def pk = loadPK()
		def contact = Contact.build(organization: organization).save()
		def ct = ContactType.build().save()

		params.organization = [id: organization.id]
		params.active = true
		params.entity = [id: entityDescriptor.id]
		params.idp = [displayName:"test name", description:"test desc", crypto:[sig: true, sigdata:pk, enc:true, encdata:pk], post:[uri:"http://identityProvider.test.com/SAML2/POST/SSO"], 
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
			[:]
		}
		WorkflowProcessService.metaClass.run = { def processInstance -> }
		def (created, org, entityDescriptor_, identityProvider, attributeAuthority, httpPost, httpRedirect, soapArtifact, organizationList, attributeList, nameIDFormatList, contact_) = idpssoDescriptorService.create(params)
		
		then:
		created
		
		Organization.count() == 1
		SamlURI.count() == 5
		EntityDescriptor.count() == 1
		IDPSSODescriptor.count() == 1
		AttributeAuthorityDescriptor.count() == 0

		def idp = IDPSSODescriptor.list().get(0)
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
		wfParams.size() == 4
		wfParams.creator == contact.id
		wfParams.identityProvider == identityProvider.id
		wfParams.organization == organization.name
	}
	
	def "Create succeeds when AttributeAuthorityDescriptor data not presented"() {
		setup:
		setupBindings()
		setupCrypto()

		def saml2Prot = SamlURI.build(uri:'urn:oasis:names:tc:SAML:2.0:protocol').save()
		def organization = Organization.build().save()
		def entityDescriptor = EntityDescriptor.build(organization:organization).save()
		def attr1 = Attribute.build().save()
		def attr2 = Attribute.build().save()
		def pk = loadPK()
		def contact = Contact.build(organization: organization).save()
		def ct = ContactType.build().save()

		params.organization = [id: organization.id]
		params.active = true
		params.entity = [id: entityDescriptor.id]
		params.idp = [displayName:"test name", description:"test desc", crypto:[sig: true, sigdata:pk, enc:true, encdata:pk], post:[uri:"http://identityProvider.test.com/SAML2/POST/SSO"], 
								redirect:[uri:"http://identityProvider.test.com/SAML2/Redirect/SSO"], artifact:[uri:"http://identityProvider.test.com/SAML2/SOAP/ArtifactResolution"]]
		params.contact = [id: contact.id, type: ct.name]
		
		def wfProcessName, wfDescription, wfPriority, wfParams
		
		when:
		WorkflowProcessService.metaClass.initiate =  { String processName, String instanceDescription, ProcessPriority priority, Map params ->
			wfProcessName = processName
			wfDescription = instanceDescription
			wfPriority = priority
			wfParams = params
			[:]
		}
		WorkflowProcessService.metaClass.run = { def processInstance -> }
		def (created, org, entityDescriptor_, identityProvider, attributeAuthority, httpPost, httpRedirect, soapArtifact, organizationList, attributeList, nameIDFormatList, contact_) = idpssoDescriptorService.create(params)
		
		then:
		created
		
		Organization.count() == 1
		SamlURI.count() == 5
		EntityDescriptor.count() == 1
		IDPSSODescriptor.count() == 1
		AttributeAuthorityDescriptor.count() == 0

		def idp = IDPSSODescriptor.list().get(0)
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
		wfParams.size() == 4
		wfParams.creator == contact.id
		wfParams.identityProvider == identityProvider.id
		wfParams.organization == organization.name
	}
	
	def "Create fails when AttributeAuthorityDescriptor fails to meet constraints"() {
		setup:
		setupBindings()
		setupCrypto()

		def saml2Prot = SamlURI.build(uri:'urn:oasis:names:tc:SAML:2.0:protocol').save()
		def organization = Organization.build().save()
		def entityDescriptor = EntityDescriptor.build(organization:organization).save()
		def attr1 = Attribute.build().save()
		def attr2 = Attribute.build().save()
		def pk = loadPK()
		def contact = Contact.build(organization: organization).save()
		def ct = ContactType.build().save()

		params.organization = [id: organization.id]
		params.active = true
		params.entity = [id: entityDescriptor.id]
		params.idp = [displayName:"test name", description:"test desc", crypto:[sig: true, sigdata:pk, enc:true, encdata:pk], post:[uri:"http://identityProvider.test.com/SAML2/POST/SSO"], 
								redirect:[uri:"http://identityProvider.test.com/SAML2/Redirect/SSO"], artifact:[uri:"http://identityProvider.test.com/SAML2/SOAP/ArtifactResolution"]]
		params.aa = [create: true, description:"test desc", crypto:[sig: true, sigdata:pk, enc:true, encdata:pk], attributeservice:[uri:"http://identityProvider.test.com/SAML2/SOAP/AttributeQuery"], attributes:[1, 2]]
		params.contact = [id: contact.id, type: ct.name]
		
		when:
		def (created, org, entityDescriptor_, identityProvider, attributeAuthority, httpPost, httpRedirect, soapArtifact, organizationList, attributeList, nameIDFormatList, contact_) = idpssoDescriptorService.create(params)
		
		then:
		!created
		
		Organization.count() == 1
		SamlURI.count() == 5
		EntityDescriptor.count() == 1
		IDPSSODescriptor.count() == 0
		AttributeAuthorityDescriptor.count() == 0

		
		identityProvider.organization == organization
		identityProvider.entityDescriptor == entityDescriptor
		identityProvider.entityDescriptor.organization == organization
		
		identityProvider.displayName == "test name"
		identityProvider.description == "test desc"
		identityProvider.keyDescriptors.size() == 2
		identityProvider.keyDescriptors.toList().get(0).keyInfo.certificate.data == pk
		identityProvider.keyDescriptors.toList().get(1).keyInfo.certificate.data == pk
		
		identityProvider.singleSignOnServices.size() == 2
		
		identityProvider.singleSignOnServices.contains(httpPost)
		httpPost.location.uri == "http://identityProvider.test.com/SAML2/POST/SSO"

		
		identityProvider.singleSignOnServices.contains(httpRedirect)
		httpRedirect.location.uri == "http://identityProvider.test.com/SAML2/Redirect/SSO"
		
		identityProvider.artifactResolutionServices.size() == 1
		
		identityProvider.artifactResolutionServices.contains(soapArtifact)
		soapArtifact.location.uri == "http://identityProvider.test.com/SAML2/SOAP/ArtifactResolution"
		
		
		attributeAuthority.displayName == null
	}
	
	def "Create fails when invalid existing EnityDescriptor provided"() {
		setup:
		setupBindings()
		setupCrypto()

		def saml2Prot = SamlURI.build(uri:'urn:oasis:names:tc:SAML:2.0:protocol').save()
		def organization = Organization.build().save()
		def entityDescriptor = EntityDescriptor.build(organization:organization).save()
		def attr1 = Attribute.build().save()
		def attr2 = Attribute.build().save()
		def pk = loadPK()
		def contact = Contact.build(organization: organization).save()
		def ct = ContactType.build().save()

		params.organization = [id: organization.id]
		params.active = true
		params.entity = [id: entityDescriptor.id + 5]
		params.idp = [displayName:"test name", description:"test desc", crypto:[sig: true, sigdata:pk, enc:true, encdata:pk], post:[uri:"http://identityProvider.test.com/SAML2/POST/SSO"], 
								redirect:[uri:"http://identityProvider.test.com/SAML2/Redirect/SSO"], artifact:[uri:"http://identityProvider.test.com/SAML2/SOAP/ArtifactResolution"]]
		params.aa = [create: true, displayName:"test name", description:"test desc", crypto:[sig: true, sigdata:pk, enc:true, encdata:pk], attributeservice:[uri:"http://identityProvider.test.com/SAML2/SOAP/AttributeQuery"], attributes:[1, 2]]
		params.contact = [id: contact.id, type: ct.name]
		
		when:
		def (created, org, entityDescriptor_, identityProvider, attributeAuthority, httpPost, httpRedirect, soapArtifact, organizationList, attributeList, nameIDFormatList, contact_) = idpssoDescriptorService.create(params)
		
		then:
		!created
		
		Organization.count() == 1
		SamlURI.count() == 5
		EntityDescriptor.count() == 1
		IDPSSODescriptor.count() == 0
		AttributeAuthorityDescriptor.count() == 0

		IDPSSODescriptor.count() == 0
		AttributeAuthorityDescriptor.count() == 0

		
		identityProvider.organization == organization
		identityProvider.entityDescriptor == null
		
		identityProvider.displayName == "test name"
		identityProvider.description == "test desc"
		identityProvider.keyDescriptors.size() == 2
		identityProvider.keyDescriptors.toList().get(0).keyInfo.certificate.data == pk
		identityProvider.keyDescriptors.toList().get(1).keyInfo.certificate.data == pk
		
		identityProvider.singleSignOnServices.size() == 2
		
		identityProvider.singleSignOnServices.contains(httpPost)
		httpPost.location.uri == "http://identityProvider.test.com/SAML2/POST/SSO"

		
		identityProvider.singleSignOnServices.contains(httpRedirect)
		httpRedirect.location.uri == "http://identityProvider.test.com/SAML2/Redirect/SSO"
		
		identityProvider.artifactResolutionServices.size() == 1
		
		identityProvider.artifactResolutionServices.contains(soapArtifact)
		soapArtifact.location.uri == "http://identityProvider.test.com/SAML2/SOAP/ArtifactResolution"
		
		
		attributeAuthority.organization == organization
		attributeAuthority.entityDescriptor == null
		attributeAuthority.collaborator == identityProvider

	}
	
	def "Create fails when EnityDescriptor does not meet constraints"() {
		setup:
		setupBindings()
		setupCrypto()

		def saml2Prot = SamlURI.build(uri:'urn:oasis:names:tc:SAML:2.0:protocol').save()
		def organization = Organization.build().save()
		def attr1 = Attribute.build().save()
		def attr2 = Attribute.build().save()
		def pk = loadPK()
		def contact = Contact.build(organization: organization).save()
		def ct = ContactType.build().save()

		params.organization = [id: organization.id]
		params.active = true
		params.entity = [identifier:""]
		params.idp = [displayName:"test name", description:"test desc", crypto:[sig: true, sigdata:pk, enc:true, encdata:pk], post:[uri:"http://identityProvider.test.com/SAML2/POST/SSO"], 
								redirect:[uri:"http://identityProvider.test.com/SAML2/Redirect/SSO"], artifact:[uri:"http://identityProvider.test.com/SAML2/SOAP/ArtifactResolution"]]
		params.aa = [create: true, displayName:"test name", description:"test desc", crypto:[sig: true, sigdata:pk, enc:true, encdata:pk], attributeservice:[uri:"http://identityProvider.test.com/SAML2/SOAP/AttributeQuery"], attributes:[1, 2]]
		params.contact = [id: contact.id, type: ct.name]
		
		when:
		def (created, org, entityDescriptor_, identityProvider, attributeAuthority, httpPost, httpRedirect, soapArtifact, organizationList, attributeList, nameIDFormatList, contact_) = idpssoDescriptorService.create(params)
		
		then:
		!created
		
		Organization.count() == 1
		SamlURI.count() == 5
		EntityDescriptor.count() == 0
		IDPSSODescriptor.count() == 0
		AttributeAuthorityDescriptor.count() == 0

		IDPSSODescriptor.count() == 0
		AttributeAuthorityDescriptor.count() == 0

		
		identityProvider.organization == organization
		identityProvider.entityDescriptor == null
		
		identityProvider.displayName == "test name"
		identityProvider.description == "test desc"
		identityProvider.keyDescriptors.size() == 2
		identityProvider.keyDescriptors.toList().get(0).keyInfo.certificate.data == pk
		identityProvider.keyDescriptors.toList().get(1).keyInfo.certificate.data == pk
		
		identityProvider.singleSignOnServices.size() == 2
		
		identityProvider.singleSignOnServices.contains(httpPost)
		httpPost.location.uri == "http://identityProvider.test.com/SAML2/POST/SSO"

		
		identityProvider.singleSignOnServices.contains(httpRedirect)
		httpRedirect.location.uri == "http://identityProvider.test.com/SAML2/Redirect/SSO"
		
		identityProvider.artifactResolutionServices.size() == 1
		
		identityProvider.artifactResolutionServices.contains(soapArtifact)
		soapArtifact.location.uri == "http://identityProvider.test.com/SAML2/SOAP/ArtifactResolution"
		
		
		attributeAuthority.organization == organization
		attributeAuthority.entityDescriptor == null
		attributeAuthority.collaborator == identityProvider

	}
	
	def "Create fails when invalid Organization provided"() {
		setup:
		setupBindings()
		setupCrypto()

		def saml2Prot = SamlURI.build(uri:'urn:oasis:names:tc:SAML:2.0:protocol').save()
		def organization = Organization.build().save()
		def attr1 = Attribute.build().save()
		def attr2 = Attribute.build().save()
		def pk = loadPK()
		def contact = Contact.build(organization: organization).save()
		def ct = ContactType.build().save()

		params.organization = [id: organization.id + 1]
		params.active = true
		params.entity = [identifier: "http://test.example.com"]
		params.idp = [displayName:"test name", description:"test desc", crypto:[sig: true, sigdata:pk, enc:true, encdata:pk], post:[uri:"http://identityProvider.test.com/SAML2/POST/SSO"], 
								redirect:[uri:"http://identityProvider.test.com/SAML2/Redirect/SSO"], artifact:[uri:"http://identityProvider.test.com/SAML2/SOAP/ArtifactResolution"]]
		params.aa = [create: true, displayName:"test name", description:"test desc", crypto:[sig: true, sigdata:pk, enc:true, encdata:pk], attributeservice:[uri:"http://identityProvider.test.com/SAML2/SOAP/AttributeQuery"], attributes:[1, 2]]
		params.contact = [id: contact.id, type: ct.name]
		
		when:
		def (created, org, entityDescriptor_, identityProvider, attributeAuthority, httpPost, httpRedirect, soapArtifact, organizationList, attributeList, nameIDFormatList, contact_) = idpssoDescriptorService.create(params)
		
		then:
		!created
		
		Organization.count() == 1
		SamlURI.count() == 5
		EntityDescriptor.count() == 0
		IDPSSODescriptor.count() == 0
		AttributeAuthorityDescriptor.count() == 0

		IDPSSODescriptor.count() == 0
		AttributeAuthorityDescriptor.count() == 0

		
		identityProvider.organization == null
		identityProvider.entityDescriptor == null
		
		identityProvider.displayName == "test name"
		identityProvider.description == "test desc"
		identityProvider.keyDescriptors.size() == 2
		identityProvider.keyDescriptors.toList().get(0).keyInfo.certificate.data == pk
		identityProvider.keyDescriptors.toList().get(1).keyInfo.certificate.data == pk
		
		identityProvider.singleSignOnServices.size() == 2
		
		identityProvider.singleSignOnServices.contains(httpPost)
		httpPost.location.uri == "http://identityProvider.test.com/SAML2/POST/SSO"

		
		identityProvider.singleSignOnServices.contains(httpRedirect)
		httpRedirect.location.uri == "http://identityProvider.test.com/SAML2/Redirect/SSO"
		
		identityProvider.artifactResolutionServices.size() == 1
		
		identityProvider.artifactResolutionServices.contains(soapArtifact)
		soapArtifact.location.uri == "http://identityProvider.test.com/SAML2/SOAP/ArtifactResolution"
		
		
		attributeAuthority.organization == null
		attributeAuthority.entityDescriptor == null
		attributeAuthority.collaborator == identityProvider

	}
	
	def "Updating an existing identity provider with valid changed content succeeds"() {
		setup:
		def organization = Organization.build().save()
		def entityDescriptor = EntityDescriptor.build(organization:organization).save()
		def idp = IDPSSODescriptor.build(entityDescriptor:entityDescriptor, active:false, wantAuthnRequestsSigned:false).save()
		params.id = idp.id
		params.idp = [displayName:"new displayName", description:"new description", status:'true', wantauthnrequestssigned:'true']
		
		when:
		def (updated, identityProvider_) = idpssoDescriptorService.update(params)
		
		then:
		updated
		
		identityProvider_.displayName == "new displayName"
		identityProvider_.description == "new description"
		identityProvider_.active
		identityProvider_.wantAuthnRequestsSigned
	}
	
	def "Updating an existing identity provider with invalid changed content fails"() {
		setup:
		def organization = Organization.build().save()
		def entityDescriptor = EntityDescriptor.build(organization:organization).save()
		def idp = IDPSSODescriptor.build(entityDescriptor:entityDescriptor, active:false, wantAuthnRequestsSigned:false).save()
		params.id = idp.id
		params.idp = [displayName:"", description:"new description", status:'true', wantauthnrequestssigned:'true']
		
		when:
		def (updated, identityProvider_) = idpssoDescriptorService.update(params)
		
		then:
		!updated
		
		identityProvider_.displayName == ""
		identityProvider_.description == "new description"
		identityProvider_.active
		identityProvider_.wantAuthnRequestsSigned
	}
	
}