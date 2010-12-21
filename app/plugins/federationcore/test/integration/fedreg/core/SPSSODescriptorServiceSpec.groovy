package fedreg.core

import grails.plugin.spock.*

import fedreg.core.*
import fedreg.workflow.*
import grails.plugins.nimble.core.*

class SPSSODescriptorServiceSpec extends IntegrationSpec {
	
	def cryptoService
	def savedMetaClasses
	def workflowProcessService
	def spssoDescriptorService
	def params
	
	def setup () {
		savedMetaClasses = [:]
		
		SpecHelpers.registerMetaClass(WorkflowProcessService, savedMetaClasses)
		workflowProcessService.metaClass = WorkflowProcessService.metaClass
		
		spssoDescriptorService = new SPSSODescriptorService(cryptoService:cryptoService, workflowProcessService:workflowProcessService)
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
	
	def "Create succeeds when valid initial SPSSODescriptor data is provided (without existing EntityDescriptor or contact)"() {
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
		params.entity = [identifier:"https://service.test.com"]
		params.sp = [displayName:"test service name", description:"test desc", attributes:[(attr1.id):[requested: 'on', reasoning:'reason for request', required:'on'], (attr2.id):[requested: 'on', reasoning:'reason for request2', required:'off']], nameidformats:[(nameID1.id):'on', (nameID2.id):'on'], crypto:[sig: true, sigdata:pk, enc:true, encdata:pk],
					  acs:[ post:[uri: 'https://service.test.com/Shibboleth.sso/SAML2/POST'], artifact:[uri: 'https://service.test.com/Shibboleth.sso/SAML2/Artifact'] ], 
					  slo:[post:[uri: 'https://service.test.com/Shibboleth.sso/SLO/Post'], soap:[uri: 'https://service.test.com/Shibboleth.sso/SLO/SOAP'], redirect:[uri: 'https://service.test.com/Shibboleth.sso/SLO/Redirect'], artifact:[uri: 'https://service.test.com/Shibboleth.sso/SLO/Artifact'] ] ]
								
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
		def (created, organization_, entityDescriptor_, serviceProvider_, httpPostACS_, soapArtifactACS_, sloArtifact_, sloRedirect_, sloSOAP_, sloPost_, organizationList_, attributeList_, nameIDFormatList_, contact_) = spssoDescriptorService.create(params)
		
		then:
		Organization.count() == 1
		EntityDescriptor.count() == 1
		SPSSODescriptor.count() == 1
		
		serviceProvider_.displayName == "test service name"
		serviceProvider_.description == "test desc"
		serviceProvider_.protocolSupportEnumerations.size() == 1
		serviceProvider_.nameIDFormats.size() == 2
		serviceProvider_.attributeConsumingServices.size() == 1
		def acs = serviceProvider_.attributeConsumingServices.toList().get(0)
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
		serviceProvider_.assertionConsumerServices.size() == 2
		serviceProvider_.singleLogoutServices.size() == 4
		serviceProvider_.contacts.size() == 1
		
		wfProcessName == "spssodescriptor_create"
		wfPriority == ProcessPriority.MEDIUM
		wfParams.size() == 3
		wfParams.serviceProvider == "${serviceProvider_.id}"
		wfParams.organization == organization_.name
		
		httpPostACS_.location.uri == "https://service.test.com/Shibboleth.sso/SAML2/POST"
		soapArtifactACS_.location.uri == "https://service.test.com/Shibboleth.sso/SAML2/Artifact"
		sloArtifact_.location.uri ==  "https://service.test.com/Shibboleth.sso/SLO/Artifact"
		sloRedirect_.location.uri ==  "https://service.test.com/Shibboleth.sso/SLO/Redirect"
		sloSOAP_.location.uri ==  "https://service.test.com/Shibboleth.sso/SLO/SOAP"
		sloPost_.location.uri == "https://service.test.com/Shibboleth.sso/SLO/Post"
	}
	
	def "Create succeeds when valid initial SPSSODescriptor data is provided (with existing EntityDescriptor and contact)"() {
		setup:
		setupBindings()
		setupCrypto()

		def saml2Prot = SamlURI.build(uri:'urn:oasis:names:tc:SAML:2.0:protocol').save()
		def organization = Organization.build().save()
		def ed = EntityDescriptor.build(organization: organization).save()
		def attr1 = AttributeBase.build(name: "attr1").save()
		def attr2 = AttributeBase.build(name: "attr2").save()
		def nameID1 = SamlURI.build().save()
		def nameID2 = SamlURI.build().save()
		def pk = loadPK()
		def ct = ContactType.build().save()
		def contact = Contact.build().save()

		params.organization = [id: organization.id]
		params.active = true
		params.entity = [id: ed.id]
		params.sp = [displayName:"test service name", description:"test desc", attributes:[(attr1.id):[requested: 'on', reasoning:'reason for request', required:'on'], (attr2.id):[requested: 'on', reasoning:'reason for request2', required:'off']], nameidformats:[(nameID1.id):'on', (nameID2.id):'on'], crypto:[sig: true, sigdata:pk, enc:true, encdata:pk],
					  acs:[ post:[uri: 'https://service.test.com/Shibboleth.sso/SAML2/POST'], artifact:[uri: 'https://service.test.com/Shibboleth.sso/SAML2/Artifact'] ], 
					  slo:[post:[uri: 'https://service.test.com/Shibboleth.sso/SLO/Post'], soap:[uri: 'https://service.test.com/Shibboleth.sso/SLO/SOAP'], redirect:[uri: 'https://service.test.com/Shibboleth.sso/SLO/Redirect'], artifact:[uri: 'https://service.test.com/Shibboleth.sso/SLO/Artifact'] ] ]
								
		params.contact = [id:contact.id, type:ct.name]
		
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
		def (created, organization_, entityDescriptor_, serviceProvider_, httpPostACS_, soapArtifactACS_, sloArtifact_, sloRedirect_, sloSOAP_, sloPost_, organizationList_, attributeList_, nameIDFormatList_, contact_) = spssoDescriptorService.create(params)
		
		then:
		Organization.count() == 1
		EntityDescriptor.count() == 1
		Contact.count() == 1
		SPSSODescriptor.count() == 1
		
		serviceProvider_.displayName == "test service name"
		serviceProvider_.description == "test desc"
		serviceProvider_.protocolSupportEnumerations.size() == 1
		serviceProvider_.nameIDFormats.size() == 2
		serviceProvider_.attributeConsumingServices.size() == 1
		def acs = serviceProvider_.attributeConsumingServices.toList().get(0)
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
		serviceProvider_.assertionConsumerServices.size() == 2
		serviceProvider_.singleLogoutServices.size() == 4
		serviceProvider_.contacts.size() == 1
		
		wfProcessName == "spssodescriptor_create"
		wfPriority == ProcessPriority.MEDIUM
		wfParams.size() == 3
		wfParams.serviceProvider == "${serviceProvider_.id}"
		wfParams.organization == organization_.name
		
		httpPostACS_.location.uri == "https://service.test.com/Shibboleth.sso/SAML2/POST"
		soapArtifactACS_.location.uri == "https://service.test.com/Shibboleth.sso/SAML2/Artifact"
		sloArtifact_.location.uri ==  "https://service.test.com/Shibboleth.sso/SLO/Artifact"
		sloRedirect_.location.uri ==  "https://service.test.com/Shibboleth.sso/SLO/Redirect"
		sloSOAP_.location.uri ==  "https://service.test.com/Shibboleth.sso/SLO/SOAP"
		sloPost_.location.uri == "https://service.test.com/Shibboleth.sso/SLO/Post"
	}
	
	def "Create succeeds when valid initial SPSSODescriptor data is provided even though not all known SLO endpoints are supplied"() {
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
		params.entity = [identifier:"https://service.test.com"]
		params.sp = [displayName:"test service name", description:"test desc", attributes:[(attr1.id):[requested: 'on', reasoning:'reason for request', required:'on'], (attr2.id):[requested: 'on', reasoning:'reason for request2', required:'off']], nameidformats:[(nameID1.id):'on', (nameID2.id):'on'], crypto:[sig: true, sigdata:pk, enc:true, encdata:pk],
					  acs:[ post:[uri: 'https://service.test.com/Shibboleth.sso/SAML2/POST'], artifact:[uri: 'https://service.test.com/Shibboleth.sso/SAML2/Artifact'] ], 
					  slo:[post:[uri: 'https://service.test.com/Shibboleth.sso/SLO/Post'], redirect:[uri: 'https://service.test.com/Shibboleth.sso/SLO/Redirect'] ] ]
								
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
		def (created, organization_, entityDescriptor_, serviceProvider_, httpPostACS_, soapArtifactACS_, sloArtifact_, sloRedirect_, sloSOAP_, sloPost_, organizationList_, attributeList_, nameIDFormatList_, contact_) = spssoDescriptorService.create(params)
		
		then:
		Organization.count() == 1
		EntityDescriptor.count() == 1
		SPSSODescriptor.count() == 1
		
		serviceProvider_.displayName == "test service name"
		serviceProvider_.description == "test desc"
		serviceProvider_.protocolSupportEnumerations.size() == 1
		serviceProvider_.nameIDFormats.size() == 2
		serviceProvider_.attributeConsumingServices.size() == 1
		def acs = serviceProvider_.attributeConsumingServices.toList().get(0)
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
		serviceProvider_.assertionConsumerServices.size() == 2
		serviceProvider_.singleLogoutServices.size() == 2
		serviceProvider_.contacts.size() == 1
		
		wfProcessName == "spssodescriptor_create"
		wfPriority == ProcessPriority.MEDIUM
		wfParams.size() == 3
		[true, [:]]
		wfParams.organization == organization_.name
		
		httpPostACS_.location.uri == "https://service.test.com/Shibboleth.sso/SAML2/POST"
		soapArtifactACS_.location.uri == "https://service.test.com/Shibboleth.sso/SAML2/Artifact"
		sloArtifact_ == null
		sloRedirect_.location.uri ==  "https://service.test.com/Shibboleth.sso/SLO/Redirect"
		sloSOAP_ == null
		sloPost_.location.uri == "https://service.test.com/Shibboleth.sso/SLO/Post"
	}
	
	def "Create fails if both required assertion consumer endpoint types aren't supplied"() {
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
		params.entity = [identifier:"https://service.test.com"]
		params.sp = [displayName:"test service name", description:"test desc", attributes:[(attr1.id):[requested: 'on', reasoning:'reason for request', required:'on'], (attr2.id):[requested: 'on', reasoning:'reason for request2', required:'off']], nameidformats:[(nameID1.id):'on', (nameID2.id):'on'], crypto:[sig: true, sigdata:pk, enc:true, encdata:pk],
					  acs:[ artifact:[uri: 'https://service.test.com/Shibboleth.sso/SAML2/Artifact'] ], 
					  slo:[post:[uri: 'https://service.test.com/Shibboleth.sso/SLO/Post'], soap:[uri: 'https://service.test.com/Shibboleth.sso/SLO/SOAP'], redirect:[uri: 'https://service.test.com/Shibboleth.sso/SLO/Redirect'], artifact:[uri: 'https://service.test.com/Shibboleth.sso/SLO/Artifact'] ] ]
								
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
		def (created, organization_, entityDescriptor_, serviceProvider_, httpPostACS_, soapArtifactACS_, sloArtifact_, sloRedirect_, sloSOAP_, sloPost_, organizationList_, attributeList_, nameIDFormatList_, contact_) = spssoDescriptorService.create(params)
		
		then:
		Organization.count() == 1
		EntityDescriptor.count() == 1
		SPSSODescriptor.count() == 0
		
		serviceProvider_.displayName == "test service name"
		serviceProvider_.description == "test desc"
		serviceProvider_.protocolSupportEnumerations.size() == 1
		serviceProvider_.nameIDFormats.size() == 2
		serviceProvider_.attributeConsumingServices.size() == 1
		def acs = serviceProvider_.attributeConsumingServices.toList().get(0)
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
		serviceProvider_.assertionConsumerServices.size() == 2
		serviceProvider_.singleLogoutServices.size() == 4
		serviceProvider_.contacts.size() == 1
		
		httpPostACS_.hasErrors() == true
		wfProcessName == null
		
		httpPostACS_.location.uri == null
		soapArtifactACS_.location.uri == "https://service.test.com/Shibboleth.sso/SAML2/Artifact"
		sloArtifact_.location.uri ==  "https://service.test.com/Shibboleth.sso/SLO/Artifact"
		sloRedirect_.location.uri ==  "https://service.test.com/Shibboleth.sso/SLO/Redirect"
		sloSOAP_.location.uri ==  "https://service.test.com/Shibboleth.sso/SLO/SOAP"
		sloPost_.location.uri == "https://service.test.com/Shibboleth.sso/SLO/Post"
	}
	
	def "Create fails when reasoning for attribute request isn't supplied"() {
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
		params.entity = [identifier:"https://service.test.com"]
		params.sp = [displayName:"test service name", description:"test desc", attributes:[(attr1.id):[requested: 'on', required:'on'], (attr2.id):[requested: 'on', reasoning:'', required:'off']], nameidformats:[(nameID1.id):'on', (nameID2.id):'on'], crypto:[sig: true, sigdata:pk, enc:true, encdata:pk],
					  acs:[ post:[uri: 'https://service.test.com/Shibboleth.sso/SAML2/POST'], artifact:[uri: 'https://service.test.com/Shibboleth.sso/SAML2/Artifact'] ], 
					  slo:[post:[uri: 'https://service.test.com/Shibboleth.sso/SLO/Post'], soap:[uri: 'https://service.test.com/Shibboleth.sso/SLO/SOAP'], redirect:[uri: 'https://service.test.com/Shibboleth.sso/SLO/Redirect'], artifact:[uri: 'https://service.test.com/Shibboleth.sso/SLO/Artifact'] ] ]
								
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
		def (created, organization_, entityDescriptor_, serviceProvider_, httpPostACS_, soapArtifactACS_, sloArtifact_, sloRedirect_, sloSOAP_, sloPost_, organizationList_, attributeList_, nameIDFormatList_, contact_) = spssoDescriptorService.create(params)
		
		then:
		Organization.count() == 1
		EntityDescriptor.count() == 1
		SPSSODescriptor.count() == 0
		
		serviceProvider_.displayName == "test service name"
		serviceProvider_.description == "test desc"
		serviceProvider_.protocolSupportEnumerations.size() == 1
		serviceProvider_.nameIDFormats.size() == 2
		serviceProvider_.attributeConsumingServices.size() == 1
		def acs = serviceProvider_.attributeConsumingServices.toList().get(0)
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
		serviceProvider_.assertionConsumerServices.size() == 2
		serviceProvider_.singleLogoutServices.size() == 4
		serviceProvider_.contacts.size() == 1
		
		wfProcessName == null
		
		httpPostACS_.location.uri == "https://service.test.com/Shibboleth.sso/SAML2/POST"
		soapArtifactACS_.location.uri == "https://service.test.com/Shibboleth.sso/SAML2/Artifact"
		sloArtifact_.location.uri ==  "https://service.test.com/Shibboleth.sso/SLO/Artifact"
		sloRedirect_.location.uri ==  "https://service.test.com/Shibboleth.sso/SLO/Redirect"
		sloSOAP_.location.uri ==  "https://service.test.com/Shibboleth.sso/SLO/SOAP"
		sloPost_.location.uri == "https://service.test.com/Shibboleth.sso/SLO/Post"
	}
	
	def "Create fails when valid contact details aren't provided"() {
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
		params.entity = [identifier:"https://service.test.com"]
		params.sp = [displayName:"test service name", description:"test desc", attributes:[(attr1.id):[requested: 'on', reasoning:'reason for request', required:'on'], (attr2.id):[requested: 'on', reasoning:'reason for request2', required:'off']], nameidformats:[(nameID1.id):'on', (nameID2.id):'on'], crypto:[sig: true, sigdata:pk, enc:true, encdata:pk],
					  acs:[ post:[uri: 'https://service.test.com/Shibboleth.sso/SAML2/POST'], artifact:[uri: 'https://service.test.com/Shibboleth.sso/SAML2/Artifact'] ], 
					  slo:[post:[uri: 'https://service.test.com/Shibboleth.sso/SLO/Post'], soap:[uri: 'https://service.test.com/Shibboleth.sso/SLO/SOAP'], redirect:[uri: 'https://service.test.com/Shibboleth.sso/SLO/Redirect'], artifact:[uri: 'https://service.test.com/Shibboleth.sso/SLO/Artifact'] ] ]
		
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
		def (created, organization_, entityDescriptor_, serviceProvider_, httpPostACS_, soapArtifactACS_, sloArtifact_, sloRedirect_, sloSOAP_, sloPost_, organizationList_, attributeList_, nameIDFormatList_, contact_) = spssoDescriptorService.create(params)
		
		then:
		Organization.count() == 1
		EntityDescriptor.count() == 0
		SPSSODescriptor.count() == 0
		
		serviceProvider_.displayName == "test service name"
		serviceProvider_.description == "test desc"
		serviceProvider_.protocolSupportEnumerations.size() == 1
		serviceProvider_.nameIDFormats.size() == 2
		serviceProvider_.attributeConsumingServices.size() == 1
		def acs = serviceProvider_.attributeConsumingServices.toList().get(0)
		acs.requestedAttributes.size() == 2
		serviceProvider_.assertionConsumerServices.size() == 2
		serviceProvider_.singleLogoutServices.size() == 4
		serviceProvider_.contacts.size() == 1
		contact_.hasErrors() == true
		
		wfProcessName == null
		
		httpPostACS_.location.uri == "https://service.test.com/Shibboleth.sso/SAML2/POST"
		soapArtifactACS_.location.uri == "https://service.test.com/Shibboleth.sso/SAML2/Artifact"
		sloArtifact_.location.uri ==  "https://service.test.com/Shibboleth.sso/SLO/Artifact"
		sloRedirect_.location.uri ==  "https://service.test.com/Shibboleth.sso/SLO/Redirect"
		sloSOAP_.location.uri ==  "https://service.test.com/Shibboleth.sso/SLO/SOAP"
		sloPost_.location.uri == "https://service.test.com/Shibboleth.sso/SLO/Post"
	}
	
	def "Create fails when invalid description"() {
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
		params.entity = [identifier:"https://service.test.com"]
		params.sp = [displayName:"test service name", attributes:[(attr1.id):[requested: 'on', reasoning:'reason for request', required:'on'], (attr2.id):[requested: 'on', reasoning:'reason for request2', required:'off']], nameidformats:[(nameID1.id):'on', (nameID2.id):'on'], crypto:[sig: true, sigdata:pk, enc:true, encdata:pk],
					  acs:[ post:[uri: 'https://service.test.com/Shibboleth.sso/SAML2/POST'], artifact:[uri: 'https://service.test.com/Shibboleth.sso/SAML2/Artifact'] ], 
					  slo:[post:[uri: 'https://service.test.com/Shibboleth.sso/SLO/Post'], soap:[uri: 'https://service.test.com/Shibboleth.sso/SLO/SOAP'], redirect:[uri: 'https://service.test.com/Shibboleth.sso/SLO/Redirect'], artifact:[uri: 'https://service.test.com/Shibboleth.sso/SLO/Artifact'] ] ]
								
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
		def (created, organization_, entityDescriptor_, serviceProvider_, httpPostACS_, soapArtifactACS_, sloArtifact_, sloRedirect_, sloSOAP_, sloPost_, organizationList_, attributeList_, nameIDFormatList_, contact_) = spssoDescriptorService.create(params)
		
		then:
		Organization.count() == 1
		EntityDescriptor.count() == 1
		SPSSODescriptor.count() == 0
		
		serviceProvider_.displayName == "test service name"
		serviceProvider_.description == null
		serviceProvider_.hasErrors() == true
		serviceProvider_.protocolSupportEnumerations.size() == 1
		serviceProvider_.nameIDFormats.size() == 2
		serviceProvider_.attributeConsumingServices.size() == 1
		def acs = serviceProvider_.attributeConsumingServices.toList().get(0)
		acs.requestedAttributes.size() == 2
		serviceProvider_.assertionConsumerServices.size() == 2
		serviceProvider_.singleLogoutServices.size() == 4
		serviceProvider_.contacts.size() == 1
		
		wfProcessName == null
		
		httpPostACS_.location.uri == "https://service.test.com/Shibboleth.sso/SAML2/POST"
		soapArtifactACS_.location.uri == "https://service.test.com/Shibboleth.sso/SAML2/Artifact"
		sloArtifact_.location.uri ==  "https://service.test.com/Shibboleth.sso/SLO/Artifact"
		sloRedirect_.location.uri ==  "https://service.test.com/Shibboleth.sso/SLO/Redirect"
		sloSOAP_.location.uri ==  "https://service.test.com/Shibboleth.sso/SLO/SOAP"
		sloPost_.location.uri == "https://service.test.com/Shibboleth.sso/SLO/Post"
	}
	
	def "Create fails when invalid display name"() {
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
		params.entity = [identifier:"https://service.test.com"]
		params.sp = [description:"test desc", attributes:[(attr1.id):[requested: 'on', reasoning:'reason for request', required:'on'], (attr2.id):[requested: 'on', reasoning:'reason for request2', required:'off']], nameidformats:[(nameID1.id):'on', (nameID2.id):'on'], crypto:[sig: true, sigdata:pk, enc:true, encdata:pk],
					  acs:[ post:[uri: 'https://service.test.com/Shibboleth.sso/SAML2/POST'], artifact:[uri: 'https://service.test.com/Shibboleth.sso/SAML2/Artifact'] ], 
					  slo:[post:[uri: 'https://service.test.com/Shibboleth.sso/SLO/Post'], soap:[uri: 'https://service.test.com/Shibboleth.sso/SLO/SOAP'], redirect:[uri: 'https://service.test.com/Shibboleth.sso/SLO/Redirect'], artifact:[uri: 'https://service.test.com/Shibboleth.sso/SLO/Artifact'] ] ]
								
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
		def (created, organization_, entityDescriptor_, serviceProvider_, httpPostACS_, soapArtifactACS_, sloArtifact_, sloRedirect_, sloSOAP_, sloPost_, organizationList_, attributeList_, nameIDFormatList_, contact_) = spssoDescriptorService.create(params)
		
		then:
		Organization.count() == 1
		EntityDescriptor.count() == 1
		SPSSODescriptor.count() == 0
		
		serviceProvider_.displayName == null
		serviceProvider_.hasErrors() == true
		serviceProvider_.description == "test desc"
		serviceProvider_.protocolSupportEnumerations.size() == 1
		serviceProvider_.nameIDFormats.size() == 2
		serviceProvider_.attributeConsumingServices.size() == 1
		def acs = serviceProvider_.attributeConsumingServices.toList().get(0)
		acs.requestedAttributes.size() == 2
		serviceProvider_.assertionConsumerServices.size() == 2
		serviceProvider_.singleLogoutServices.size() == 4
		serviceProvider_.contacts.size() == 1
		
		wfProcessName == null
		
		httpPostACS_.location.uri == "https://service.test.com/Shibboleth.sso/SAML2/POST"
		soapArtifactACS_.location.uri == "https://service.test.com/Shibboleth.sso/SAML2/Artifact"
		sloArtifact_.location.uri ==  "https://service.test.com/Shibboleth.sso/SLO/Artifact"
		sloRedirect_.location.uri ==  "https://service.test.com/Shibboleth.sso/SLO/Redirect"
		sloSOAP_.location.uri ==  "https://service.test.com/Shibboleth.sso/SLO/SOAP"
		sloPost_.location.uri == "https://service.test.com/Shibboleth.sso/SLO/Post"
	}
	
	def "Attempt to update non existant service provider fails"() {
		setup:
		params.id = 1
		
		when: 
		def (updated, serviceProvider_) = spssoDescriptorService.update(params)
		
		then:
		!updated
		serviceProvider_ == null
	}
	
	def "Updating an existing service provider with valid changed content succeeds"() {
		setup:
		def orgType = new OrganizationType(name:"test", displayName:"test")
		def organization = Organization.build(name:"test org", primary: orgType).save()
		def ed = EntityDescriptor.build(organization: organization).save()
		def sd = ServiceDescription.build(connectURL: "http://connecturl.com", furtherInfo:"this is further info")
		def sp = SPSSODescriptor.build(entityDescriptor:ed, serviceDescription:sd).save()
		params.id = sp.id
		params.sp = [displayName:"new displayName", description:"new description"]
		params.sp.servicedescription = [connecturl: "http://newconnecturl.com", furtherinfo:"this is new further info"]
		
		when:
		def (updated, serviceProvider_) = spssoDescriptorService.update(params)
		
		then:
		updated
		serviceProvider_.displayName == "new displayName"
		serviceProvider_.description == "new description"
		serviceProvider_.serviceDescription.connectURL == "http://newconnecturl.com"
		serviceProvider_.serviceDescription.furtherInfo == "this is new further info"
	}

	def "Updating an existing service provider with invalid changed content fails"() {
		setup:
		def orgType = new OrganizationType(name:"test", displayName:"test")
		def organization = Organization.build(name:"test org", primary: orgType).save()
		def ed = EntityDescriptor.build(organization: organization).save()
		def sd = ServiceDescription.build(connectURL: "http://connecturl.com", furtherInfo:"this is further info")
		def sp = SPSSODescriptor.build(entityDescriptor:ed, serviceDescription:sd).save()
		params.id = sp.id
		params.sp = [displayName:"", description:"new description"]
		params.sp.servicedescription = [connecturl: "http://newconnecturl.com", furtherinfo:"this is new further info"]
		
		when:
		def (updated, serviceProvider_) = spssoDescriptorService.update(params)
		
		then:
		!updated
		serviceProvider_.hasErrors()
		serviceProvider_.displayName == ""
		serviceProvider_.description == "new description"
		serviceProvider_.serviceDescription.connectURL == "http://newconnecturl.com"
		serviceProvider_.serviceDescription.furtherInfo == "this is new further info"
	}
}