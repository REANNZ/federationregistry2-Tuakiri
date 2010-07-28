package fedreg.core

import grails.plugin.spock.*

import fedreg.core.*
import fedreg.workflow.*
import grails.plugins.nimble.core.*

class IDPSSODescriptorControllerSpec extends IntegrationSpec {
	
	def controller
	def cryptoService
	def savedMetaClasses
	def workflowProcessService
	def IDPSSODescriptorService
	
	def setup () {
		savedMetaClasses = [:]
		
		SpecHelpers.registerMetaClass(WorkflowProcessService, savedMetaClasses)
		workflowProcessService.metaClass = WorkflowProcessService.metaClass
		
		controller = new IDPSSODescriptorController(IDPSSODescriptorService:IDPSSODescriptorService/*cryptoService: cryptoService, workflowProcessService: workflowProcessService*/)
		def user = UserBase.build()
		SpecHelpers.setupShiroEnv(user)
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
	
	def "Validate list"() {
		setup:
		setupCrypto()
		def pk = loadPK()
		
		(1..25).each { i ->
			def ed = EntityDescriptor.build(entityID:"http://idp.test.com/$i")
			def idp = IDPSSODescriptor.build(entityDescriptor: ed)
			idp.save()
		}
		
		when:
		def model = controller.list()

		then:
		model.identityProviderList.size() == 20
	}
	
	def "Validate list with max set"() {
		setup:
		(1..25).each { i ->
			def ed = EntityDescriptor.build(entityID:"http://idp.test.com/$i")
			def idp = IDPSSODescriptor.build(entityDescriptor: ed)
			idp.save()
		}
		controller.params.max = 10
		
		when:
		def model = controller.list()

		then:
		model.identityProviderList.size() == 10
		model.identityProviderList.get(0) == IDPSSODescriptor.list().get(0)
		model.identityProviderList.get(9) == IDPSSODescriptor.list().get(9)
	}
	
	def "Validate list with max and offset set"() {
		setup:
		(1..25).each { i->
			def ed = EntityDescriptor.build(entityID:"http://idp.test.com/$i")
			def idp = IDPSSODescriptor.build(entityDescriptor: ed)
			idp.save()
		}
		controller.params.max = 10
		controller.params.offset = 5
		
		when:
		def model = controller.list()

		then:
		model.identityProviderList.size() == 10
		model.identityProviderList.get(0) == IDPSSODescriptor.list().get(5)
		model.identityProviderList.get(9) == IDPSSODescriptor.list().get(14)
	}
	
	def "Show with no ID"() {		
		when:
		controller.show()
		
		then:
		controller.flash.type == "error"
		controller.flash.message == "fedreg.controllers.namevalue.missing"
		controller.response.redirectedUrl == "/IDPSSODescriptor/list"
	}
	
	def "Show with invalid IDPSSODescriptor ID"() {
		setup:
		controller.params.id = 2
			
		when:
		controller.show()
		
		then:
		controller.flash.type == "error"
		controller.flash.message == "fedreg.core.idpssoroledescriptor.nonexistant"
		controller.response.redirectedUrl == "/IDPSSODescriptor/list"
	}
	
	def "Validate create"() {
		setup:
		(1..10).each {
			Organization.build().save()
		}
		
		(1..11).each { i ->
			Attribute.build(name: "attr$i").save()
		}
		
		(1..12).each { i ->
			SamlURI.build(type:SamlURIType.NameIdentifierFormat).save()
		}
		
		when:
		def model = controller.create()

		then:
		model.identityProvider != null
		model.identityProvider instanceof IDPSSODescriptor
		model.organizationList.size() == 10
		model.attributeList.size() == 11
		model.nameIDFormatList.size() == 12
	}
	
	def "Save succeeds when valid initial IDPSSODescriptor and AttributeAuthorityDescriptor data are provided (without existing EntityDescriptor or contact)"() {
		setup:
		setupBindings()
		setupCrypto()

		def organization = Organization.build().save()
		def attr1 = Attribute.build(name: "attr1").save()
		def attr2 = Attribute.build(name: "attr2").save()
		def nameID1 = SamlURI.build().save()
		def nameID2 = SamlURI.build().save()
		def pk = loadPK()
		def ct = ContactType.build().save()

		controller.params.organization = [id: organization.id]
		controller.params.active = true
		controller.params.entity = [identifier:"http://idp.test.com"]
		controller.params.idp = [displayName:"test name", description:"test desc", attributes:[(attr1.id):'on', (attr2.id):'on'], nameidformats:[(nameID1.id):'on', (nameID2.id):'on'], crypto:[sig: true, sigdata:pk, enc:true, encdata:pk], post:[uri:"http://idp.test.com/SAML2/POST/SSO"], 
								redirect:[uri:"http://idp.test.com/SAML2/Redirect/SSO"], artifact:[uri:"http://idp.test.com/SAML2/SOAP/ArtifactResolution"]]
		controller.params.aa = [create: true, displayName:"test name", description:"test desc", crypto:[sig: true, sigdata:pk, enc:true, encdata:pk], attributeservice:[uri:"http://idp.test.com/SAML2/SOAP/AttributeQuery"], attributes:[(attr1.id):'on', (attr2.id):'on']]
		controller.params.contact = [givenName:"Bradley", surname:"Beddoes", email:"bradleybeddoes@intient.com", type:ct.name]
		
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
		def model = controller.save()
		
		then:
		Organization.count() == 1
		SamlURI.count() == 6
		EntityDescriptor.count() == 1
		IDPSSODescriptor.count() == 1
		AttributeAuthorityDescriptor.count() == 1
		
		def entityDescriptor = EntityDescriptor.list().get(0)
		entityDescriptor.entityID == "http://idp.test.com"
		
		def idp = IDPSSODescriptor.list().get(0)
		idp.organization == organization
		idp.entityDescriptor == entityDescriptor
		idp.entityDescriptor.organization == organization
		
		idp.displayName == "test name"
		idp.description == "test desc"
		idp.keyDescriptors.size() == 2
		idp.keyDescriptors.toList().get(0).keyInfo.certificate.data == pk
		idp.keyDescriptors.toList().get(1).keyInfo.certificate.data == pk
		
		idp.singleSignOnServices.size() == 2
		def sso1 = idp.singleSignOnServices.toList().get(0)
		def sso2 = idp.singleSignOnServices.toList().get(1)
		if(sso1.binding == SamlURI.findByUri(SamlConstants.httpPost)) {
			sso1.location.uri == "http://idp.test.com/SAML2/POST/SSO"
			sso2.location.uri == "http://idp.test.com/SAML2/Redirect/SSO"
		}
		else {
			sso1.location.uri == "http://idp.test.com/SAML2/Redirect/SSO"
			sso2.location.uri == "http://idp.test.com/SAML2/POST/SSO"
		}
				
		idp.artifactResolutionServices.size() == 1
		idp.artifactResolutionServices.toList().get(0).location.uri == "http://idp.test.com/SAML2/SOAP/ArtifactResolution"
		
		idp.attributes != null
		idp.attributes.size() == 2
		
		idp.nameIDFormats != null
		idp.nameIDFormats.size() == 2
		
		idp.contacts.size() == 1
		idp.contacts.toList().get(0).type.name == ct.name
		idp.contacts.toList().get(0).contact.givenName == "Bradley"
		idp.contacts.toList().get(0).contact.surname == "Beddoes"
		idp.contacts.toList().get(0).contact.email.uri == "bradleybeddoes@intient.com"
		
		def aa = AttributeAuthorityDescriptor.list().get(0)
		aa.organization == organization
		aa.entityDescriptor == entityDescriptor
		aa.collaborator == idp
		idp.collaborator == aa
		aa.keyDescriptors.size() == 2
		aa.keyDescriptors.toList().get(0).keyInfo.certificate.data == pk
		aa.keyDescriptors.toList().get(1).keyInfo.certificate.data == pk
		aa.attributeServices.toList().get(0).location.uri == "http://idp.test.com/SAML2/SOAP/AttributeQuery"
		
		aa.attributes != null
		aa.attributes.size() == 2
		
		wfProcessName == "idpssodescriptor_create"
		wfDescription == "Approval for creation of IDPSSODescriptor test name"
		wfPriority == ProcessPriority.MEDIUM
		wfParams.size() == 4
		wfParams.creator == controller.authenticatedUser.id
		wfParams.identityProvider == idp.id
		wfParams.organization == organization.name
		
		controller.response.redirectedUrl == "/IDPSSODescriptor/show/${idp.id}"
	}
	
	def "Save succeeds when valid initial IDPSSODescriptor and AttributeAuthorityDescriptor data are provided (without existing EntityDescriptor but with existing contact)"() {
		setup:
		setupBindings()
		setupCrypto()

		def organization = Organization.build().save()
		def attr1 = Attribute.build(name: "attr1").save()
		def attr2 = Attribute.build(name: "attr2").save()
		def nameID1 = SamlURI.build().save()
		def nameID2 = SamlURI.build().save()
		def pk = loadPK()
		def contact = Contact.build(organization: organization).save()
		def ct = ContactType.build().save()

		controller.params.organization = [id: organization.id]
		controller.params.active = true
		controller.params.entity = [identifier:"http://idp.test.com"]
		controller.params.idp = [displayName:"test name", description:"test desc", attributes:[(attr1.id):'on', (attr2.id):'on'], nameidformats:[(nameID1.id):'on', (nameID2.id):'on'], crypto:[sig: true, sigdata:pk, enc:true, encdata:pk], post:[uri:"http://idp.test.com/SAML2/POST/SSO"], 
								redirect:[uri:"http://idp.test.com/SAML2/Redirect/SSO"], artifact:[uri:"http://idp.test.com/SAML2/SOAP/ArtifactResolution"]]
		controller.params.aa = [create: true, displayName:"test name", description:"test desc", crypto:[sig: true, sigdata:pk, enc:true, encdata:pk], attributeservice:[uri:"http://idp.test.com/SAML2/SOAP/AttributeQuery"], attributes:[(attr1.id):'on', (attr2.id):'on']]
		controller.params.contact = [id: contact.id, type: ct.name]
		
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
		def model = controller.save()
		
		then:
		Organization.count() == 1
		SamlURI.count() == 6
		EntityDescriptor.count() == 1
		IDPSSODescriptor.count() == 1
		AttributeAuthorityDescriptor.count() == 1
		
		def entityDescriptor = EntityDescriptor.list().get(0)
		entityDescriptor.entityID == "http://idp.test.com"
		
		def idp = IDPSSODescriptor.list().get(0)
		idp.organization == organization
		idp.entityDescriptor == entityDescriptor
		idp.entityDescriptor.organization == organization
		
		idp.displayName == "test name"
		idp.description == "test desc"
		idp.keyDescriptors.size() == 2
		idp.keyDescriptors.toList().get(0).keyInfo.certificate.data == pk
		idp.keyDescriptors.toList().get(1).keyInfo.certificate.data == pk
		
		idp.singleSignOnServices.size() == 2
		def sso1 = idp.singleSignOnServices.toList().get(0)
		def sso2 = idp.singleSignOnServices.toList().get(1)
		if(sso1.binding == SamlURI.findByUri(SamlConstants.httpPost)) {
			sso1.location.uri == "http://idp.test.com/SAML2/POST/SSO"
			sso2.location.uri == "http://idp.test.com/SAML2/Redirect/SSO"
		}
		else {
			sso1.location.uri == "http://idp.test.com/SAML2/Redirect/SSO"
			sso2.location.uri == "http://idp.test.com/SAML2/POST/SSO"
		}
				
		idp.artifactResolutionServices.size() == 1
		idp.artifactResolutionServices.toList().get(0).location.uri == "http://idp.test.com/SAML2/SOAP/ArtifactResolution"
		
		idp.attributes != null
		idp.attributes.size() == 2
		
		idp.nameIDFormats != null
		idp.nameIDFormats.size() == 2
		
		idp.contacts.size() == 1
		idp.contacts.toList().get(0).type.name == ct.name
		idp.contacts.toList().get(0).contact.givenName == contact.givenName
		idp.contacts.toList().get(0).contact.surname == contact.surname
		idp.contacts.toList().get(0).contact.email.uri == contact.email.uri
		
		def aa = AttributeAuthorityDescriptor.list().get(0)
		aa.organization == organization
		aa.entityDescriptor == entityDescriptor
		aa.collaborator == idp
		idp.collaborator == aa
		aa.keyDescriptors.size() == 2
		aa.keyDescriptors.toList().get(0).keyInfo.certificate.data == pk
		aa.keyDescriptors.toList().get(1).keyInfo.certificate.data == pk
		aa.attributeServices.toList().get(0).location.uri == "http://idp.test.com/SAML2/SOAP/AttributeQuery"
		
		aa.attributes != null
		aa.attributes.size() == 2
		
		wfProcessName == "idpssodescriptor_create"
		wfDescription == "Approval for creation of IDPSSODescriptor test name"
		wfPriority == ProcessPriority.MEDIUM
		wfParams.size() == 4
		wfParams.creator == controller.authenticatedUser.id
		wfParams.identityProvider == idp.id
		wfParams.organization == organization.name
		
		controller.response.redirectedUrl == "/IDPSSODescriptor/show/${idp.id}"
	}
	
	def "Save succeeds when valid initial IDPSSODescriptor and AttributeAuthorityDescriptor data are provided (with existing EntityDescriptor)"() {
		setup:
		setupBindings()
		setupCrypto()

		def organization = Organization.build().save()
		def entityDescriptor = EntityDescriptor.build(organization:organization).save()
		def attr1 = Attribute.build(name: "attr1").save()
		def attr2 = Attribute.build(name: "attr2").save()
		def pk = loadPK()
		def contact = Contact.build(organization: organization).save()
		def ct = ContactType.build().save()

		controller.params.organization = [id: organization.id]
		controller.params.active = true
		controller.params.entity = [id: entityDescriptor.id]
		controller.params.idp = [displayName:"test name", description:"test desc", attributes:[(attr1.id):'on', (attr2.id):'on'], crypto:[sig: true, sigdata:pk, enc:true, encdata:pk], post:[uri:"http://idp.test.com/SAML2/POST/SSO"], 
								redirect:[uri:"http://idp.test.com/SAML2/Redirect/SSO"], artifact:[uri:"http://idp.test.com/SAML2/SOAP/ArtifactResolution"]]
		controller.params.aa = [create: true, displayName:"test name", description:"test desc", crypto:[sig: true, sigdata:pk, enc:true, encdata:pk], attributeservice:[uri:"http://idp.test.com/SAML2/SOAP/AttributeQuery"], attributes:[(attr1.id):'on', (attr2.id):'on']]
		controller.params.contact = [id: contact.id, type: ct.name]
		
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
		def model = controller.save()
		
		then:
		Organization.count() == 1
		SamlURI.count() == 4
		EntityDescriptor.count() == 1
		IDPSSODescriptor.count() == 1
		AttributeAuthorityDescriptor.count() == 1

		def idp = IDPSSODescriptor.list().get(0)
		idp.organization == organization
		idp.entityDescriptor == entityDescriptor
		idp.entityDescriptor.organization == organization
		
		idp.displayName == "test name"
		idp.description == "test desc"
		idp.active 
		!idp.approved
		idp.keyDescriptors.size() == 2
		idp.keyDescriptors.toList().get(0).keyInfo.certificate.data == pk
		idp.keyDescriptors.toList().get(1).keyInfo.certificate.data == pk
		
		idp.singleSignOnServices.size() == 2
		def sso1 = idp.singleSignOnServices.toList().get(0)
		def sso2 = idp.singleSignOnServices.toList().get(1)
		if(sso1.binding == SamlURI.findByUri(SamlConstants.httpPost)) {
			sso1.location.uri == "http://idp.test.com/SAML2/POST/SSO"
			sso2.location.uri == "http://idp.test.com/SAML2/Redirect/SSO"
		}
		else {
			sso1.location.uri == "http://idp.test.com/SAML2/Redirect/SSO"
			sso2.location.uri == "http://idp.test.com/SAML2/POST/SSO"
		}
				
		idp.artifactResolutionServices.size() == 1
		idp.artifactResolutionServices.toList().get(0).location.uri == "http://idp.test.com/SAML2/SOAP/ArtifactResolution"
		
		idp.attributes != null
		idp.attributes.size() == 2
		
		idp.contacts.size() == 1
		idp.contacts.toList().get(0).type.name == ct.name
		idp.contacts.toList().get(0).contact.givenName == contact.givenName
		idp.contacts.toList().get(0).contact.surname == contact.surname
		idp.contacts.toList().get(0).contact.email.uri == contact.email.uri
		
		def aa = AttributeAuthorityDescriptor.list().get(0)
		aa.organization == organization
		aa.entityDescriptor == entityDescriptor
		aa.collaborator == idp
		idp.collaborator == aa
		aa.active 
		!aa.approved
		aa.keyDescriptors.size() == 2
		aa.keyDescriptors.toList().get(0).keyInfo.certificate.data == pk
		aa.keyDescriptors.toList().get(1).keyInfo.certificate.data == pk
		aa.attributeServices.toList().get(0).location.uri == "http://idp.test.com/SAML2/SOAP/AttributeQuery"
		
		aa.attributes != null
		aa.attributes.size() == 2
		
		wfProcessName == "idpssodescriptor_create"
		wfDescription == "Approval for creation of IDPSSODescriptor test name"
		wfPriority == ProcessPriority.MEDIUM
		wfParams.size() == 4
		wfParams.creator == controller.authenticatedUser.id
		wfParams.identityProvider == idp.id
		wfParams.organization == organization.name
		
		controller.response.redirectedUrl == "/IDPSSODescriptor/show/${idp.id}"
	}
	
	def "Save fails when IDPSSODescriptor fails constraints even though a valid AttributeAuthorityDescriptor is provided"() {
		setup:
		setupBindings()
		setupCrypto()

		def organization = Organization.build().save()
		def entityDescriptor = EntityDescriptor.build(organization:organization).save()
		def attr1 = Attribute.build().save()
		def attr2 = Attribute.build().save()
		def pk = loadPK()
		def contact = Contact.build(organization: organization).save()
		def ct = ContactType.build().save()

		controller.params.organization = [id: organization.id]
		controller.params.active = true
		controller.params.entity = [id: entityDescriptor.id]
		controller.params.idp = [description:"test desc", crypto:[sig: true, sigdata:pk, enc:true, encdata:pk], post:[uri:"http://idp.test.com/SAML2/POST/SSO"], 
								redirect:[uri:"http://idp.test.com/SAML2/Redirect/SSO"], artifact:[uri:"http://idp.test.com/SAML2/SOAP/ArtifactResolution"]]
		controller.params.aa = [create: true, displayName:"test name", description:"test desc", crypto:[sig: true, sigdata:pk, enc:true, encdata:pk], attributeservice:[uri:"http://idp.test.com/SAML2/SOAP/AttributeQuery"], attributes:[1, 2]]
		controller.params.contact = [id: contact.id, type: ct.name]
		
		when:
		def model = controller.save()
		
		then:
		Organization.count() == 1
		SamlURI.count() == 4
		EntityDescriptor.count() == 1
		IDPSSODescriptor.count() == 0
		AttributeAuthorityDescriptor.count() == 0

		def idp = controller.modelAndView.model.identityProvider
		idp.organization == organization
		idp.entityDescriptor == entityDescriptor
		idp.entityDescriptor.organization == organization
		
		idp.displayName == null
		idp.description == "test desc"
		idp.keyDescriptors.size() == 2
		idp.keyDescriptors.toList().get(0).keyInfo.certificate.data == pk
		idp.keyDescriptors.toList().get(1).keyInfo.certificate.data == pk
		
		idp.singleSignOnServices.size() == 2
		def httpPost = controller.modelAndView.model.httpPost
		idp.singleSignOnServices.contains(httpPost)
		httpPost.location.uri == "http://idp.test.com/SAML2/POST/SSO"
		
		def httpRedirect = controller.modelAndView.model.httpRedirect
		idp.singleSignOnServices.contains(httpRedirect)
		httpRedirect.location.uri == "http://idp.test.com/SAML2/Redirect/SSO"
		
		idp.artifactResolutionServices.size() == 1
		def soapArtifact = controller.modelAndView.model.soapArtifact
		idp.artifactResolutionServices.contains(soapArtifact)
		soapArtifact.location.uri == "http://idp.test.com/SAML2/SOAP/ArtifactResolution"
		
		idp.contacts.size() == 1
		idp.contacts.toList().get(0).id == null
		
		def aa = controller.modelAndView.model.attributeAuthority
		aa.organization == organization
		aa.entityDescriptor == entityDescriptor
		aa.collaborator == idp
		aa.keyDescriptors.size() == 2
		aa.keyDescriptors.toList().get(0).keyInfo.certificate.data == pk
		aa.keyDescriptors.toList().get(1).keyInfo.certificate.data == pk
		aa.attributeServices.toList().get(0).location.uri == "http://idp.test.com/SAML2/SOAP/AttributeQuery"
		
		controller.modelAndView.model.identityProvider.errors.getFieldError('displayName').code == 'nullable'
		controller.flash.type = "error"
		controller.flash.message = "fedreg.core.idpssodescriptor.save.validation.error.identityprovider"
	}
	
	def "Save fails when IDPSSODescriptor post endpoint fails constraints"() {
		setup:
		setupBindings()
		setupCrypto()

		def organization = Organization.build().save()
		def entityDescriptor = EntityDescriptor.build(organization:organization).save()
		def attr1 = Attribute.build().save()
		def attr2 = Attribute.build().save()
		def pk = loadPK()
		def contact = Contact.build(organization: organization).save()
		def ct = ContactType.build().save()

		controller.params.organization = [id: organization.id]
		controller.params.active = true
		controller.params.entity = [id: entityDescriptor.id]
		controller.params.idp = [displayName: "test name", description:"test desc", crypto:[sig: true, sigdata:pk, enc:true, encdata:pk], post:[uri:"/SAML2/POST/SSO"], 
								redirect:[uri:"http://idp.test.com/SAML2/Redirect/SSO"], artifact:[uri:"http://idp.test.com/SAML2/SOAP/ArtifactResolution"]]
		controller.params.aa = [create: true, displayName:"test name", description:"test desc", crypto:[sig: true, sigdata:pk, enc:true, encdata:pk], attributeservice:[uri:"http://idp.test.com/SAML2/SOAP/AttributeQuery"], attributes:[1, 2]]
		controller.params.contact = [id: contact.id, type: ct.name]
		
		when:
		def model = controller.save()
		
		then:
		Organization.count() == 1
		SamlURI.count() == 4
		EntityDescriptor.count() == 1
		IDPSSODescriptor.count() == 0
		AttributeAuthorityDescriptor.count() == 0

		def idp = controller.modelAndView.model.identityProvider
		idp.organization == organization
		idp.entityDescriptor == entityDescriptor
		idp.entityDescriptor.organization == organization
		
		idp.displayName == "test name"
		idp.description == "test desc"
		idp.keyDescriptors.size() == 2
		idp.keyDescriptors.toList().get(0).keyInfo.certificate.data == pk
		idp.keyDescriptors.toList().get(1).keyInfo.certificate.data == pk
		
		idp.singleSignOnServices.size() == 2
		def httpPost = controller.modelAndView.model.httpPost
		idp.singleSignOnServices.contains(httpPost)
		httpPost.location.uri == "/SAML2/POST/SSO"
		httpPost.errors.getErrorCount() == 1
		httpPost.errors.getFieldError('location.uri').code == 'url.invalid'
		
		def httpRedirect = controller.modelAndView.model.httpRedirect
		idp.singleSignOnServices.contains(httpRedirect)
		httpRedirect.location.uri == "http://idp.test.com/SAML2/Redirect/SSO"
		
		idp.artifactResolutionServices.size() == 1
		def soapArtifact = controller.modelAndView.model.soapArtifact
		idp.artifactResolutionServices.contains(soapArtifact)
		soapArtifact.location.uri == "http://idp.test.com/SAML2/SOAP/ArtifactResolution"
		
		def aa = controller.modelAndView.model.attributeAuthority
		aa.organization == organization
		aa.entityDescriptor == entityDescriptor
		aa.collaborator == idp
		aa.keyDescriptors.size() == 2
		aa.keyDescriptors.toList().get(0).keyInfo.certificate.data == pk
		aa.keyDescriptors.toList().get(1).keyInfo.certificate.data == pk
		aa.attributeServices.toList().get(0).location.uri == "http://idp.test.com/SAML2/SOAP/AttributeQuery"
		
		controller.flash.type = "error"
		controller.flash.message = "fedreg.core.idpssodescriptor.save.validation.error.identityprovider"
	}
	
	def "Save fails when IDPSSODescriptor post endpoint not supplied"() {
		setup:
		setupBindings()
		setupCrypto()

		def organization = Organization.build().save()
		def entityDescriptor = EntityDescriptor.build(organization:organization).save()
		def attr1 = Attribute.build().save()
		def attr2 = Attribute.build().save()
		def pk = loadPK()
		def contact = Contact.build(organization: organization).save()
		def ct = ContactType.build().save()

		controller.params.organization = [id: organization.id]
		controller.params.active = true
		controller.params.entity = [id: entityDescriptor.id]
		controller.params.idp = [displayName: "test name", description:"test desc", crypto:[sig: true, sigdata:pk, enc:true, encdata:pk], 
								redirect:[uri:"http://idp.test.com/SAML2/Redirect/SSO"], artifact:[uri:"http://idp.test.com/SAML2/SOAP/ArtifactResolution"]]
		controller.params.aa = [create: true, displayName:"test name", description:"test desc", crypto:[sig: true, sigdata:pk, enc:true, encdata:pk], attributeservice:[uri:"http://idp.test.com/SAML2/SOAP/AttributeQuery"], attributes:[1, 2]]
		controller.params.contact = [id: contact.id, type: ct.name]
		
		when:
		def model = controller.save()
		
		then:
		Organization.count() == 1
		SamlURI.count() == 4
		EntityDescriptor.count() == 1
		IDPSSODescriptor.count() == 0
		AttributeAuthorityDescriptor.count() == 0

		def idp = controller.modelAndView.model.identityProvider
		idp.organization == organization
		idp.entityDescriptor == entityDescriptor
		idp.entityDescriptor.organization == organization
		
		idp.displayName == "test name"
		idp.description == "test desc"
		idp.keyDescriptors.size() == 2
		idp.keyDescriptors.toList().get(0).keyInfo.certificate.data == pk
		idp.keyDescriptors.toList().get(1).keyInfo.certificate.data == pk
		
		idp.singleSignOnServices.size() == 2
		def httpPost = controller.modelAndView.model.httpPost
		idp.singleSignOnServices.contains(httpPost)
		httpPost.location.uri == null
		httpPost.errors.getErrorCount() == 1
		httpPost.errors.getFieldError('location.uri').code == 'nullable'
		idp.errors.getErrorCount() == 1
		idp.errors.getFieldError('singleSignOnServices.location.uri').code == 'nullable'
		
		def httpRedirect = controller.modelAndView.model.httpRedirect
		idp.singleSignOnServices.contains(httpRedirect)
		httpRedirect.location.uri == "http://idp.test.com/SAML2/Redirect/SSO"
		
		idp.artifactResolutionServices.size() == 1
		def soapArtifact = controller.modelAndView.model.soapArtifact
		idp.artifactResolutionServices.contains(soapArtifact)
		soapArtifact.location.uri == "http://idp.test.com/SAML2/SOAP/ArtifactResolution"
		
		def aa = controller.modelAndView.model.attributeAuthority
		aa.organization == organization
		aa.entityDescriptor == entityDescriptor
		aa.collaborator == idp
		aa.keyDescriptors.size() == 2
		aa.keyDescriptors.toList().get(0).keyInfo.certificate.data == pk
		aa.keyDescriptors.toList().get(1).keyInfo.certificate.data == pk
		aa.attributeServices.toList().get(0).location.uri == "http://idp.test.com/SAML2/SOAP/AttributeQuery"
		
		controller.flash.type = "error"
		controller.flash.message = "fedreg.core.idpssodescriptor.save.validation.error.identityprovider"
	}
	
	def "Save fails when IDPSSODescriptor redirect endpoint fails constraints"() {
		setup:
		setupBindings()
		setupCrypto()

		def organization = Organization.build().save()
		def entityDescriptor = EntityDescriptor.build(organization:organization).save()
		def attr1 = Attribute.build().save()
		def attr2 = Attribute.build().save()
		def pk = loadPK()
		def contact = Contact.build(organization: organization).save()
		def ct = ContactType.build().save()

		controller.params.organization = [id: organization.id]
		controller.params.active = true
		controller.params.entity = [id: entityDescriptor.id]
		controller.params.idp = [displayName: "test name", description:"test desc", crypto:[sig: true, sigdata:pk, enc:true, encdata:pk], post:[uri:"http://idp.test.com/SAML2/POST/SSO"], 
								redirect:[uri:"asdfasdasdf"], artifact:[uri:"http://idp.test.com/SAML2/SOAP/ArtifactResolution"]]
		controller.params.aa = [create: true, displayName:"test name", description:"test desc", crypto:[sig: true, sigdata:pk, enc:true, encdata:pk], attributeservice:[uri:"http://idp.test.com/SAML2/SOAP/AttributeQuery"], attributes:[1, 2]]
		controller.params.contact = [id: contact.id, type: ct.name]
		
		when:
		def model = controller.save()
		
		then:
		Organization.count() == 1
		SamlURI.count() == 4
		EntityDescriptor.count() == 1
		IDPSSODescriptor.count() == 0
		AttributeAuthorityDescriptor.count() == 0

		def idp = controller.modelAndView.model.identityProvider
		idp.organization == organization
		idp.entityDescriptor == entityDescriptor
		idp.entityDescriptor.organization == organization
		
		idp.displayName == "test name"
		idp.description == "test desc"
		idp.keyDescriptors.size() == 2
		idp.keyDescriptors.toList().get(0).keyInfo.certificate.data == pk
		idp.keyDescriptors.toList().get(1).keyInfo.certificate.data == pk
		
		idp.singleSignOnServices.size() == 2
		def httpPost = controller.modelAndView.model.httpPost
		idp.singleSignOnServices.contains(httpPost)
		httpPost.location.uri == "http://idp.test.com/SAML2/POST/SSO"

		def httpRedirect = controller.modelAndView.model.httpRedirect
		idp.singleSignOnServices.contains(httpRedirect)
		httpRedirect.location.uri == "asdfasdasdf"
		httpRedirect.errors.getErrorCount() == 1
		httpRedirect.errors.getFieldError('location.uri').code == 'url.invalid'
		
		idp.artifactResolutionServices.size() == 1
		def soapArtifact = controller.modelAndView.model.soapArtifact
		idp.artifactResolutionServices.contains(soapArtifact)
		soapArtifact.location.uri == "http://idp.test.com/SAML2/SOAP/ArtifactResolution"
		
		def aa = controller.modelAndView.model.attributeAuthority
		aa.organization == organization
		aa.entityDescriptor == entityDescriptor
		aa.collaborator == idp
		aa.keyDescriptors.size() == 2
		aa.keyDescriptors.toList().get(0).keyInfo.certificate.data == pk
		aa.keyDescriptors.toList().get(1).keyInfo.certificate.data == pk
		aa.attributeServices.toList().get(0).location.uri == "http://idp.test.com/SAML2/SOAP/AttributeQuery"
		
		controller.flash.type = "error"
		controller.flash.message = "fedreg.core.idpssodescriptor.save.validation.error.identityprovider"
	}
	
	def "Save fails when IDPSSODescriptor redirect endpoint not supplied"() {
		setup:
		setupBindings()
		setupCrypto()

		def organization = Organization.build().save()
		def entityDescriptor = EntityDescriptor.build(organization:organization).save()
		def attr1 = Attribute.build().save()
		def attr2 = Attribute.build().save()
		def pk = loadPK()
		def contact = Contact.build(organization: organization).save()
		def ct = ContactType.build().save()

		controller.params.organization = [id: organization.id]
		controller.params.active = true
		controller.params.entity = [id: entityDescriptor.id]
		controller.params.idp = [displayName: "test name", description:"test desc", crypto:[sig: true, sigdata:pk, enc:true, encdata:pk], post:[uri:"http://idp.test.com/SAML2/POST/SSO"],
								artifact:[uri:"http://idp.test.com/SAML2/SOAP/ArtifactResolution"]]
		controller.params.aa = [create: true, displayName:"test name", description:"test desc", crypto:[sig: true, sigdata:pk, enc:true, encdata:pk], attributeservice:[uri:"http://idp.test.com/SAML2/SOAP/AttributeQuery"], attributes:[1, 2]]
		controller.params.contact = [id: contact.id, type: ct.name]
		
		when:
		def model = controller.save()
		
		then:
		Organization.count() == 1
		SamlURI.count() == 4
		EntityDescriptor.count() == 1
		IDPSSODescriptor.count() == 0
		AttributeAuthorityDescriptor.count() == 0

		def idp = controller.modelAndView.model.identityProvider
		idp.organization == organization
		idp.entityDescriptor == entityDescriptor
		idp.entityDescriptor.organization == organization
		
		idp.displayName == "test name"
		idp.description == "test desc"
		idp.keyDescriptors.size() == 2
		idp.keyDescriptors.toList().get(0).keyInfo.certificate.data == pk
		idp.keyDescriptors.toList().get(1).keyInfo.certificate.data == pk
		
		idp.singleSignOnServices.size() == 2
		def httpPost = controller.modelAndView.model.httpPost
		idp.singleSignOnServices.contains(httpPost)
		httpPost.location.uri == "http://idp.test.com/SAML2/POST/SSO"

		def httpRedirect = controller.modelAndView.model.httpRedirect
		idp.singleSignOnServices.contains(httpRedirect)
		httpRedirect.location.uri == null
		httpRedirect.errors.getErrorCount() == 1
		httpRedirect.errors.getFieldError('location.uri').code == 'nullable'
		idp.errors.getErrorCount() == 1
		idp.errors.getFieldError('singleSignOnServices.location.uri').code == 'nullable'
		
		idp.artifactResolutionServices.size() == 1
		def soapArtifact = controller.modelAndView.model.soapArtifact
		idp.artifactResolutionServices.contains(soapArtifact)
		soapArtifact.location.uri == "http://idp.test.com/SAML2/SOAP/ArtifactResolution"
		
		def aa = controller.modelAndView.model.attributeAuthority
		aa.organization == organization
		aa.entityDescriptor == entityDescriptor
		aa.collaborator == idp
		aa.keyDescriptors.size() == 2
		aa.keyDescriptors.toList().get(0).keyInfo.certificate.data == pk
		aa.keyDescriptors.toList().get(1).keyInfo.certificate.data == pk
		aa.attributeServices.toList().get(0).location.uri == "http://idp.test.com/SAML2/SOAP/AttributeQuery"
		
		controller.flash.type = "error"
		controller.flash.message = "fedreg.core.idpssodescriptor.save.validation.error.identityprovider"
	}
	
	def "Save fails when IDPSSODescriptor artifact endpoint fails constraints"() {
		setup:
		setupBindings()
		setupCrypto()

		def organization = Organization.build().save()
		def entityDescriptor = EntityDescriptor.build(organization:organization).save()
		def attr1 = Attribute.build().save()
		def attr2 = Attribute.build().save()
		def pk = loadPK()
		def contact = Contact.build(organization: organization).save()
		def ct = ContactType.build().save()

		controller.params.organization = [id: organization.id]
		controller.params.active = true
		controller.params.entity = [id: entityDescriptor.id]
		controller.params.idp = [displayName: "test name", description:"test desc", crypto:[sig: true, sigdata:pk, enc:true, encdata:pk], post:[uri:"http://idp.test.com/SAML2/POST/SSO"], 
								redirect:[uri:"http://idp.test.com/SAML2/Redirect/SSO"], artifact:[uri:"/SAML2/SOAP/ArtifactResolution"]]
		controller.params.aa = [create: true, displayName:"test name", description:"test desc", crypto:[sig: true, sigdata:pk, enc:true, encdata:pk], attributeservice:[uri:"http://idp.test.com/SAML2/SOAP/AttributeQuery"], attributes:[1, 2]]
		controller.params.contact = [id: contact.id, type: ct.name]
		
		when:
		def model = controller.save()
		
		then:
		Organization.count() == 1
		SamlURI.count() == 4
		EntityDescriptor.count() == 1
		IDPSSODescriptor.count() == 0
		AttributeAuthorityDescriptor.count() == 0

		def idp = controller.modelAndView.model.identityProvider
		idp.organization == organization
		idp.entityDescriptor == entityDescriptor
		idp.entityDescriptor.organization == organization
		
		idp.displayName == "test name"
		idp.description == "test desc"
		idp.keyDescriptors.size() == 2
		idp.keyDescriptors.toList().get(0).keyInfo.certificate.data == pk
		idp.keyDescriptors.toList().get(1).keyInfo.certificate.data == pk
		
		idp.singleSignOnServices.size() == 2
		def httpPost = controller.modelAndView.model.httpPost
		idp.singleSignOnServices.contains(httpPost)
		httpPost.location.uri == "http://idp.test.com/SAML2/POST/SSO"

		def httpRedirect = controller.modelAndView.model.httpRedirect
		idp.singleSignOnServices.contains(httpRedirect)
		httpRedirect.location.uri == "http://idp.test.com/SAML2/Redirect/SSO"
		
		idp.artifactResolutionServices.size() == 1
		def soapArtifact = controller.modelAndView.model.soapArtifact
		idp.artifactResolutionServices.contains(soapArtifact)
		soapArtifact.location.uri == "/SAML2/SOAP/ArtifactResolution"
		soapArtifact.errors.getErrorCount() == 1
		soapArtifact.errors.getFieldError('location.uri').code == 'url.invalid'
		
		def aa = controller.modelAndView.model.attributeAuthority
		aa.organization == organization
		aa.entityDescriptor == entityDescriptor
		aa.collaborator == idp
		aa.keyDescriptors.size() == 2
		aa.keyDescriptors.toList().get(0).keyInfo.certificate.data == pk
		aa.keyDescriptors.toList().get(1).keyInfo.certificate.data == pk
		aa.attributeServices.toList().get(0).location.uri == "http://idp.test.com/SAML2/SOAP/AttributeQuery"
		
		controller.flash.type = "error"
		controller.flash.message = "fedreg.core.idpssodescriptor.save.validation.error.identityprovider"
	}
	
	def "Save fails when IDPSSODescriptor artifact endpoint not supplied"() {
		setup:
		setupBindings()
		setupCrypto()

		def organization = Organization.build().save()
		def entityDescriptor = EntityDescriptor.build(organization:organization).save()
		def attr1 = Attribute.build().save()
		def attr2 = Attribute.build().save()
		def pk = loadPK()
		def contact = Contact.build(organization: organization).save()
		def ct = ContactType.build().save()

		controller.params.organization = [id: organization.id]
		controller.params.active = true
		controller.params.entity = [id: entityDescriptor.id]
		controller.params.idp = [displayName: "test name", description:"test desc", crypto:[sig: true, sigdata:pk, enc:true, encdata:pk], post:[uri:"http://idp.test.com/SAML2/POST/SSO"],
								redirect:[uri:"http://idp.test.com/SAML2/Redirect/SSO"]]
		controller.params.aa = [create: true, displayName:"test name", description:"test desc", crypto:[sig: true, sigdata:pk, enc:true, encdata:pk], attributeservice:[uri:"http://idp.test.com/SAML2/SOAP/AttributeQuery"], attributes:[1, 2]]
		controller.params.contact = [id: contact.id, type: ct.name]
		
		when:
		def model = controller.save()
		
		then:
		Organization.count() == 1
		SamlURI.count() == 4
		EntityDescriptor.count() == 1
		IDPSSODescriptor.count() == 0
		AttributeAuthorityDescriptor.count() == 0

		def idp = controller.modelAndView.model.identityProvider
		idp.organization == organization
		idp.entityDescriptor == entityDescriptor
		idp.entityDescriptor.organization == organization
		
		idp.displayName == "test name"
		idp.description == "test desc"
		idp.keyDescriptors.size() == 2
		idp.keyDescriptors.toList().get(0).keyInfo.certificate.data == pk
		idp.keyDescriptors.toList().get(1).keyInfo.certificate.data == pk
		
		idp.singleSignOnServices.size() == 2
		def httpPost = controller.modelAndView.model.httpPost
		idp.singleSignOnServices.contains(httpPost)
		httpPost.location.uri == "http://idp.test.com/SAML2/POST/SSO"

		def httpRedirect = controller.modelAndView.model.httpRedirect
		idp.singleSignOnServices.contains(httpRedirect)
		httpRedirect.location.uri == "http://idp.test.com/SAML2/Redirect/SSO"
		
		idp.artifactResolutionServices.size() == 1
		def soapArtifact = controller.modelAndView.model.soapArtifact
		idp.artifactResolutionServices.contains(soapArtifact)
		soapArtifact.location.uri == null
		soapArtifact.errors.getErrorCount() == 1
		soapArtifact.errors.getFieldError('location.uri').code == 'nullable'
		idp.errors.getErrorCount() == 1
		idp.errors.getFieldError('artifactResolutionServices.location.uri').code == 'nullable'
		
		def aa = controller.modelAndView.model.attributeAuthority
		aa.organization == organization
		aa.entityDescriptor == entityDescriptor
		aa.collaborator == idp
		aa.keyDescriptors.size() == 2
		aa.keyDescriptors.toList().get(0).keyInfo.certificate.data == pk
		aa.keyDescriptors.toList().get(1).keyInfo.certificate.data == pk
		aa.attributeServices.toList().get(0).location.uri == "http://idp.test.com/SAML2/SOAP/AttributeQuery"
		
		controller.flash.type = "error"
		controller.flash.message = "fedreg.core.idpssodescriptor.save.validation.error.identityprovider"
	}
	
	def "Save succeeds when IDPSSODescriptor crypto not supplied"() {
		setup:
		setupBindings()
		setupCrypto()

		def organization = Organization.build().save()
		def entityDescriptor = EntityDescriptor.build(organization:organization).save()
		def attr1 = Attribute.build().save()
		def attr2 = Attribute.build().save()
		def pk = loadPK()
		def contact = Contact.build(organization: organization).save()
		def ct = ContactType.build().save()

		controller.params.organization = [id: organization.id]
		controller.params.active = true
		controller.params.entity = [id: entityDescriptor.id]
		controller.params.idp = [displayName: "test name", description:"test desc",  post:[uri:"http://idp.test.com/SAML2/POST/SSO"],
								redirect:[uri:"http://idp.test.com/SAML2/Redirect/SSO"], artifact:[uri:"http://idp.test.com/SAML2/SOAP/ArtifactResolution"]]
		controller.params.aa = [create: true, displayName:"test name", description:"test desc", crypto:[sig: true, sigdata:pk, enc:true, encdata:pk], attributeservice:[uri:"http://idp.test.com/SAML2/SOAP/AttributeQuery"], attributes:[1, 2]]
		controller.params.contact = [id: contact.id, type: ct.name]
		
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
		def model = controller.save()
		
		then:
		Organization.count() == 1
		SamlURI.count() == 4
		EntityDescriptor.count() == 1
		IDPSSODescriptor.count() == 1
		AttributeAuthorityDescriptor.count() == 1

		def idp = IDPSSODescriptor.list().get(0)
		def aa = AttributeAuthorityDescriptor.list().get(0)
		
		idp.organization == organization
		idp.entityDescriptor == entityDescriptor
		idp.entityDescriptor.organization == organization
		idp.collaborator == aa
		
		idp.displayName == "test name"
		idp.description == "test desc"
		idp.keyDescriptors == null
		idp.singleSignOnServices.size() == 2
		def sso1 = idp.singleSignOnServices.toList().get(0)
		def sso2 = idp.singleSignOnServices.toList().get(1)
		if(sso1.binding == SamlURI.findByUri(SamlConstants.httpPost)) {
			sso1.location.uri == "http://idp.test.com/SAML2/POST/SSO"
			sso2.location.uri == "http://idp.test.com/SAML2/Redirect/SSO"
		}
		else {
			sso1.location.uri == "http://idp.test.com/SAML2/Redirect/SSO"
			sso2.location.uri == "http://idp.test.com/SAML2/POST/SSO"
		}
				
		idp.artifactResolutionServices.size() == 1
		idp.artifactResolutionServices.toList().get(0).location.uri == "http://idp.test.com/SAML2/SOAP/ArtifactResolution"
		
		wfProcessName == "idpssodescriptor_create"
		wfDescription == "Approval for creation of IDPSSODescriptor test name"
		wfPriority == ProcessPriority.MEDIUM
		wfParams.size() == 4
		wfParams.creator == controller.authenticatedUser.id
		wfParams.identityProvider == idp.id
		wfParams.organization == organization.name
		
		controller.response.redirectedUrl == "/IDPSSODescriptor/show/${idp.id}"
	}
	
	def "Save succeeds when AttributeAuthorityDescriptor not required"() {
		setup:
		setupBindings()
		setupCrypto()

		def organization = Organization.build().save()
		def entityDescriptor = EntityDescriptor.build(organization:organization).save()
		def attr1 = Attribute.build().save()
		def attr2 = Attribute.build().save()
		def pk = loadPK()
		def contact = Contact.build(organization: organization).save()
		def ct = ContactType.build().save()

		controller.params.organization = [id: organization.id]
		controller.params.active = true
		controller.params.entity = [id: entityDescriptor.id]
		controller.params.idp = [displayName:"test name", description:"test desc", crypto:[sig: true, sigdata:pk, enc:true, encdata:pk], post:[uri:"http://idp.test.com/SAML2/POST/SSO"], 
								redirect:[uri:"http://idp.test.com/SAML2/Redirect/SSO"], artifact:[uri:"http://idp.test.com/SAML2/SOAP/ArtifactResolution"]]
		controller.params.aa = [create: false]
		controller.params.contact = [id: contact.id, type: ct.name]
		
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
		def model = controller.save()
		
		then:
		Organization.count() == 1
		SamlURI.count() == 4
		EntityDescriptor.count() == 1
		IDPSSODescriptor.count() == 1
		AttributeAuthorityDescriptor.count() == 0

		def idp = IDPSSODescriptor.list().get(0)
		idp.organization == organization
		idp.entityDescriptor == entityDescriptor
		idp.entityDescriptor.organization == organization
		idp.collaborator == null
		
		idp.displayName == "test name"
		idp.description == "test desc"
		idp.keyDescriptors.size() == 2
		idp.singleSignOnServices.size() == 2
		def sso1 = idp.singleSignOnServices.toList().get(0)
		def sso2 = idp.singleSignOnServices.toList().get(1)
		if(sso1.binding == SamlURI.findByUri(SamlConstants.httpPost)) {
			sso1.location.uri == "http://idp.test.com/SAML2/POST/SSO"
			sso2.location.uri == "http://idp.test.com/SAML2/Redirect/SSO"
		}
		else {
			sso1.location.uri == "http://idp.test.com/SAML2/Redirect/SSO"
			sso2.location.uri == "http://idp.test.com/SAML2/POST/SSO"
		}
				
		idp.artifactResolutionServices.size() == 1
		idp.artifactResolutionServices.toList().get(0).location.uri == "http://idp.test.com/SAML2/SOAP/ArtifactResolution"
		
		wfProcessName == "idpssodescriptor_create"
		wfDescription == "Approval for creation of IDPSSODescriptor test name"
		wfPriority == ProcessPriority.MEDIUM
		wfParams.size() == 4
		wfParams.creator == controller.authenticatedUser.id
		wfParams.identityProvider == idp.id
		wfParams.organization == organization.name
		
		controller.response.redirectedUrl == "/IDPSSODescriptor/show/${idp.id}"
	}
	
	def "Save succeeds when AttributeAuthorityDescriptor data not presented"() {
		setup:
		setupBindings()
		setupCrypto()

		def organization = Organization.build().save()
		def entityDescriptor = EntityDescriptor.build(organization:organization).save()
		def attr1 = Attribute.build().save()
		def attr2 = Attribute.build().save()
		def pk = loadPK()
		def contact = Contact.build(organization: organization).save()
		def ct = ContactType.build().save()

		controller.params.organization = [id: organization.id]
		controller.params.active = true
		controller.params.entity = [id: entityDescriptor.id]
		controller.params.idp = [displayName:"test name", description:"test desc", crypto:[sig: true, sigdata:pk, enc:true, encdata:pk], post:[uri:"http://idp.test.com/SAML2/POST/SSO"], 
								redirect:[uri:"http://idp.test.com/SAML2/Redirect/SSO"], artifact:[uri:"http://idp.test.com/SAML2/SOAP/ArtifactResolution"]]
		controller.params.contact = [id: contact.id, type: ct.name]
		
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
		def model = controller.save()
		
		then:
		Organization.count() == 1
		SamlURI.count() == 4
		EntityDescriptor.count() == 1
		IDPSSODescriptor.count() == 1
		AttributeAuthorityDescriptor.count() == 0

		def idp = IDPSSODescriptor.list().get(0)
		idp.organization == organization
		idp.entityDescriptor == entityDescriptor
		idp.entityDescriptor.organization == organization
		idp.collaborator == null
		
		idp.displayName == "test name"
		idp.description == "test desc"
		idp.keyDescriptors.size() == 2
		idp.singleSignOnServices.size() == 2
		def sso1 = idp.singleSignOnServices.toList().get(0)
		def sso2 = idp.singleSignOnServices.toList().get(1)
		if(sso1.binding == SamlURI.findByUri(SamlConstants.httpPost)) {
			sso1.location.uri == "http://idp.test.com/SAML2/POST/SSO"
			sso2.location.uri == "http://idp.test.com/SAML2/Redirect/SSO"
		}
		else {
			sso1.location.uri == "http://idp.test.com/SAML2/Redirect/SSO"
			sso2.location.uri == "http://idp.test.com/SAML2/POST/SSO"
		}
				
		idp.artifactResolutionServices.size() == 1
		idp.artifactResolutionServices.toList().get(0).location.uri == "http://idp.test.com/SAML2/SOAP/ArtifactResolution"
		
		wfProcessName == "idpssodescriptor_create"
		wfDescription == "Approval for creation of IDPSSODescriptor test name"
		wfPriority == ProcessPriority.MEDIUM
		wfParams.size() == 4
		wfParams.creator == controller.authenticatedUser.id
		wfParams.identityProvider == idp.id
		wfParams.organization == organization.name
		
		controller.response.redirectedUrl == "/IDPSSODescriptor/show/${idp.id}"
	}
	
	def "Save fails when AttributeAuthorityDescriptor fails to meet constraints"() {
		setup:
		setupBindings()
		setupCrypto()

		def organization = Organization.build().save()
		def entityDescriptor = EntityDescriptor.build(organization:organization).save()
		def attr1 = Attribute.build().save()
		def attr2 = Attribute.build().save()
		def pk = loadPK()
		def contact = Contact.build(organization: organization).save()
		def ct = ContactType.build().save()

		controller.params.organization = [id: organization.id]
		controller.params.active = true
		controller.params.entity = [id: entityDescriptor.id]
		controller.params.idp = [displayName:"test name", description:"test desc", crypto:[sig: true, sigdata:pk, enc:true, encdata:pk], post:[uri:"http://idp.test.com/SAML2/POST/SSO"], 
								redirect:[uri:"http://idp.test.com/SAML2/Redirect/SSO"], artifact:[uri:"http://idp.test.com/SAML2/SOAP/ArtifactResolution"]]
		controller.params.aa = [create: true, description:"test desc", crypto:[sig: true, sigdata:pk, enc:true, encdata:pk], attributeservice:[uri:"http://idp.test.com/SAML2/SOAP/AttributeQuery"], attributes:[1, 2]]
		controller.params.contact = [id: contact.id, type: ct.name]
		
		when:
		def model = controller.save()
		
		then:
		Organization.count() == 1
		SamlURI.count() == 4
		EntityDescriptor.count() == 1
		IDPSSODescriptor.count() == 0
		AttributeAuthorityDescriptor.count() == 0

		def idp = controller.modelAndView.model.identityProvider
		idp.organization == organization
		idp.entityDescriptor == entityDescriptor
		idp.entityDescriptor.organization == organization
		
		idp.displayName == "test name"
		idp.description == "test desc"
		idp.keyDescriptors.size() == 2
		idp.keyDescriptors.toList().get(0).keyInfo.certificate.data == pk
		idp.keyDescriptors.toList().get(1).keyInfo.certificate.data == pk
		
		idp.singleSignOnServices.size() == 2
		def httpPost = controller.modelAndView.model.httpPost
		idp.singleSignOnServices.contains(httpPost)
		httpPost.location.uri == "http://idp.test.com/SAML2/POST/SSO"

		def httpRedirect = controller.modelAndView.model.httpRedirect
		idp.singleSignOnServices.contains(httpRedirect)
		httpRedirect.location.uri == "http://idp.test.com/SAML2/Redirect/SSO"
		
		idp.artifactResolutionServices.size() == 1
		def soapArtifact = controller.modelAndView.model.soapArtifact
		idp.artifactResolutionServices.contains(soapArtifact)
		soapArtifact.location.uri == "http://idp.test.com/SAML2/SOAP/ArtifactResolution"
		
		def aa = controller.modelAndView.model.attributeAuthority
		aa.displayName == null
		
		controller.flash.type = "error"
		controller.flash.message = "fedreg.core.idpssodescriptor.save.validation.error.attributeauthority"
	}
	
	def "Save fails when invalid existing EnityDescriptor provided"() {
		setup:
		setupBindings()
		setupCrypto()

		def organization = Organization.build().save()
		def entityDescriptor = EntityDescriptor.build(organization:organization).save()
		def attr1 = Attribute.build().save()
		def attr2 = Attribute.build().save()
		def pk = loadPK()
		def contact = Contact.build(organization: organization).save()
		def ct = ContactType.build().save()

		controller.params.organization = [id: organization.id]
		controller.params.active = true
		controller.params.entity = [id: entityDescriptor.id + 5]
		controller.params.idp = [displayName:"test name", description:"test desc", crypto:[sig: true, sigdata:pk, enc:true, encdata:pk], post:[uri:"http://idp.test.com/SAML2/POST/SSO"], 
								redirect:[uri:"http://idp.test.com/SAML2/Redirect/SSO"], artifact:[uri:"http://idp.test.com/SAML2/SOAP/ArtifactResolution"]]
		controller.params.aa = [create: true, displayName:"test name", description:"test desc", crypto:[sig: true, sigdata:pk, enc:true, encdata:pk], attributeservice:[uri:"http://idp.test.com/SAML2/SOAP/AttributeQuery"], attributes:[1, 2]]
		controller.params.contact = [id: contact.id, type: ct.name]
		
		when:
		def model = controller.save()
		
		then:
		Organization.count() == 1
		SamlURI.count() == 4
		EntityDescriptor.count() == 1
		IDPSSODescriptor.count() == 0
		AttributeAuthorityDescriptor.count() == 0

		IDPSSODescriptor.count() == 0
		AttributeAuthorityDescriptor.count() == 0

		def idp = controller.modelAndView.model.identityProvider
		idp.organization == organization
		idp.entityDescriptor == null
		
		idp.displayName == "test name"
		idp.description == "test desc"
		idp.keyDescriptors.size() == 2
		idp.keyDescriptors.toList().get(0).keyInfo.certificate.data == pk
		idp.keyDescriptors.toList().get(1).keyInfo.certificate.data == pk
		
		idp.singleSignOnServices.size() == 2
		def httpPost = controller.modelAndView.model.httpPost
		idp.singleSignOnServices.contains(httpPost)
		httpPost.location.uri == "http://idp.test.com/SAML2/POST/SSO"

		def httpRedirect = controller.modelAndView.model.httpRedirect
		idp.singleSignOnServices.contains(httpRedirect)
		httpRedirect.location.uri == "http://idp.test.com/SAML2/Redirect/SSO"
		
		idp.artifactResolutionServices.size() == 1
		def soapArtifact = controller.modelAndView.model.soapArtifact
		idp.artifactResolutionServices.contains(soapArtifact)
		soapArtifact.location.uri == "http://idp.test.com/SAML2/SOAP/ArtifactResolution"
		
		def aa = controller.modelAndView.model.attributeAuthority
		aa.organization == organization
		aa.entityDescriptor == null
		aa.collaborator == idp
		aa.keyDescriptors.size() == 2
		aa.keyDescriptors.toList().get(0).keyInfo.certificate.data == pk
		aa.keyDescriptors.toList().get(1).keyInfo.certificate.data == pk
		aa.attributeServices.toList().get(0).location.uri == "http://idp.test.com/SAML2/SOAP/AttributeQuery"
		
		controller.modelAndView.model.entityDescriptor.errors.getErrorCount() == 1
		
		controller.flash.type = "error"
		controller.flash.message = "fedreg.core.idpssodescriptor.save.validation.error.entitydescriptor"
	}
	
	def "Save fails when EnityDescriptor does not meet constraints"() {
		setup:
		setupBindings()
		setupCrypto()

		def organization = Organization.build().save()
		def attr1 = Attribute.build().save()
		def attr2 = Attribute.build().save()
		def pk = loadPK()
		def contact = Contact.build(organization: organization).save()
		def ct = ContactType.build().save()

		controller.params.organization = [id: organization.id]
		controller.params.active = true
		controller.params.entity = [identifier:""]
		controller.params.idp = [displayName:"test name", description:"test desc", crypto:[sig: true, sigdata:pk, enc:true, encdata:pk], post:[uri:"http://idp.test.com/SAML2/POST/SSO"], 
								redirect:[uri:"http://idp.test.com/SAML2/Redirect/SSO"], artifact:[uri:"http://idp.test.com/SAML2/SOAP/ArtifactResolution"]]
		controller.params.aa = [create: true, displayName:"test name", description:"test desc", crypto:[sig: true, sigdata:pk, enc:true, encdata:pk], attributeservice:[uri:"http://idp.test.com/SAML2/SOAP/AttributeQuery"], attributes:[1, 2]]
		controller.params.contact = [id: contact.id, type: ct.name]
		
		when:
		def model = controller.save()
		
		then:
		Organization.count() == 1
		SamlURI.count() == 4
		EntityDescriptor.count() == 0
		IDPSSODescriptor.count() == 0
		AttributeAuthorityDescriptor.count() == 0

		IDPSSODescriptor.count() == 0
		AttributeAuthorityDescriptor.count() == 0

		def idp = controller.modelAndView.model.identityProvider
		idp.organization == organization
		idp.entityDescriptor == null
		
		idp.displayName == "test name"
		idp.description == "test desc"
		idp.keyDescriptors.size() == 2
		idp.keyDescriptors.toList().get(0).keyInfo.certificate.data == pk
		idp.keyDescriptors.toList().get(1).keyInfo.certificate.data == pk
		
		idp.singleSignOnServices.size() == 2
		def httpPost = controller.modelAndView.model.httpPost
		idp.singleSignOnServices.contains(httpPost)
		httpPost.location.uri == "http://idp.test.com/SAML2/POST/SSO"

		def httpRedirect = controller.modelAndView.model.httpRedirect
		idp.singleSignOnServices.contains(httpRedirect)
		httpRedirect.location.uri == "http://idp.test.com/SAML2/Redirect/SSO"
		
		idp.artifactResolutionServices.size() == 1
		def soapArtifact = controller.modelAndView.model.soapArtifact
		idp.artifactResolutionServices.contains(soapArtifact)
		soapArtifact.location.uri == "http://idp.test.com/SAML2/SOAP/ArtifactResolution"
		
		def aa = controller.modelAndView.model.attributeAuthority
		aa.organization == organization
		aa.entityDescriptor == null
		aa.collaborator == idp
		aa.keyDescriptors.size() == 2
		aa.keyDescriptors.toList().get(0).keyInfo.certificate.data == pk
		aa.keyDescriptors.toList().get(1).keyInfo.certificate.data == pk
		aa.attributeServices.toList().get(0).location.uri == "http://idp.test.com/SAML2/SOAP/AttributeQuery"
		
		controller.modelAndView.model.entityDescriptor.errors.getErrorCount() == 1
		controller.modelAndView.model.entityDescriptor.errors.getFieldError('entityID').code == 'blank'
		
		controller.flash.type = "error"
		controller.flash.message = "fedreg.core.idpssodescriptor.save.validation.error.entitydescriptor"
	}
	
	def "Save fails when invalid Organization provided"() {
		setup:
		setupBindings()
		setupCrypto()

		def organization = Organization.build().save()
		def attr1 = Attribute.build().save()
		def attr2 = Attribute.build().save()
		def pk = loadPK()
		def contact = Contact.build(organization: organization).save()
		def ct = ContactType.build().save()

		controller.params.organization = [id: organization.id + 1]
		controller.params.active = true
		controller.params.entity = [identifier: "http://test.example.com"]
		controller.params.idp = [displayName:"test name", description:"test desc", crypto:[sig: true, sigdata:pk, enc:true, encdata:pk], post:[uri:"http://idp.test.com/SAML2/POST/SSO"], 
								redirect:[uri:"http://idp.test.com/SAML2/Redirect/SSO"], artifact:[uri:"http://idp.test.com/SAML2/SOAP/ArtifactResolution"]]
		controller.params.aa = [create: true, displayName:"test name", description:"test desc", crypto:[sig: true, sigdata:pk, enc:true, encdata:pk], attributeservice:[uri:"http://idp.test.com/SAML2/SOAP/AttributeQuery"], attributes:[1, 2]]
		controller.params.contact = [id: contact.id, type: ct.name]
		
		when:
		def model = controller.save()
		
		then:
		Organization.count() == 1
		SamlURI.count() == 4
		EntityDescriptor.count() == 0
		IDPSSODescriptor.count() == 0
		AttributeAuthorityDescriptor.count() == 0

		IDPSSODescriptor.count() == 0
		AttributeAuthorityDescriptor.count() == 0

		def idp = controller.modelAndView.model.identityProvider
		idp.organization == null
		idp.entityDescriptor == null
		
		idp.displayName == "test name"
		idp.description == "test desc"
		idp.keyDescriptors.size() == 2
		idp.keyDescriptors.toList().get(0).keyInfo.certificate.data == pk
		idp.keyDescriptors.toList().get(1).keyInfo.certificate.data == pk
		
		idp.singleSignOnServices.size() == 2
		def httpPost = controller.modelAndView.model.httpPost
		idp.singleSignOnServices.contains(httpPost)
		httpPost.location.uri == "http://idp.test.com/SAML2/POST/SSO"

		def httpRedirect = controller.modelAndView.model.httpRedirect
		idp.singleSignOnServices.contains(httpRedirect)
		httpRedirect.location.uri == "http://idp.test.com/SAML2/Redirect/SSO"
		
		idp.artifactResolutionServices.size() == 1
		def soapArtifact = controller.modelAndView.model.soapArtifact
		idp.artifactResolutionServices.contains(soapArtifact)
		soapArtifact.location.uri == "http://idp.test.com/SAML2/SOAP/ArtifactResolution"
		
		def aa = controller.modelAndView.model.attributeAuthority
		aa.organization == null
		aa.entityDescriptor == null
		aa.collaborator == idp
		aa.keyDescriptors.size() == 2
		aa.keyDescriptors.toList().get(0).keyInfo.certificate.data == pk
		aa.keyDescriptors.toList().get(1).keyInfo.certificate.data == pk
		aa.attributeServices.toList().get(0).location.uri == "http://idp.test.com/SAML2/SOAP/AttributeQuery"
		
		controller.flash.type = "error"
		controller.flash.message = "fedreg.core.idpssodescriptor.save.validation.error.organization"
	}
	
}