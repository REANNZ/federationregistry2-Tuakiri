package fedreg.metadata

import grails.plugin.spock.*
import groovy.xml.MarkupBuilder

import fedreg.core.*

class MetadataGenerationServiceSpec extends IntegrationSpec {
	def metadataGenerationService
	def cryptoService
	
	def writer
	def builder
	
	def soap
	def httpRedirect
	def httpPost
	def httpArtifact
	
	def cleanup() {
	}
	
	def setup () {
		writer = new StringWriter()
		builder = new MarkupBuilder(writer)
	}
	
	def setupBindings() {
		soap = new SamlURI(type:SamlURIType.ProtocolBinding, uri:'urn:oasis:names:tc:SAML:2.0:bindings:SOAP', description:'')
		httpRedirect = new SamlURI(type:SamlURIType.ProtocolBinding, uri:'urn:oasis:names:tc:SAML:2.0:bindings:HTTP-Redirect', description:'')
		httpPost = new SamlURI(type:SamlURIType.ProtocolBinding, uri:'urn:oasis:names:tc:SAML:2.0:bindings:HTTP-POST', description:'')
		httpArtifact = new SamlURI(type:SamlURIType.ProtocolBinding, uri:'urn:oasis:names:tc:SAML:2.0:bindings:HTTP-Artifact', description:'')
	}
	
	def loadResult(file) {
		new File("./test/data/metadatagenerationservicespec/${file}.xml").text
	}
	
	def String loadPK() {
		new File('./test/data/selfsigned.pem').text
	}
	
	def "Test valid endpoint generation"() {
		setup:
		setupBindings()
		def location = new UrlURI(uri:"https://test.example.com/SSO")
		def sso = SingleSignOnService.build(active:true, approved:true, binding:httpPost, location:location)
		def result = loadResult('testvalidendpointgeneration')
		
		when:
		metadataGenerationService.endpoint(builder, false, "SingleSignOnService", sso)
		
		then:
		def xml = writer.toString()
		xml == result
	}
	
	def "Test valid endpoint generation with response"() {
		setup:
		setupBindings()
		def location = new UrlURI(uri:"https://test.example.com/SSO")
		def responseLocation = new UrlURI(uri:"https://test.example.com/response")
		def sso = SingleSignOnService.build(active:true, approved:true, binding:httpPost, location:location, responseLocation:responseLocation)
		def result = loadResult('testvalidendpointgenerationresponse')
		
		when:
		metadataGenerationService.endpoint(builder, false, "SingleSignOnService", sso)
		
		then:
		def xml = writer.toString()
		xml == result
	}
	
	def "Test endpoint generation when endpoint inactive"() {
		setup:
		setupBindings()
		def location = new UrlURI(uri:"https://test.example.com/SSO")
		def sso = SingleSignOnService.build(active:false, approved:true, binding:httpPost, location:location)
		
		when:
		metadataGenerationService.endpoint(builder, false, "SingleSignOnService", sso)
		
		then:
		def xml = writer.toString()
		xml == ""
	}
	
	def "Test endpoint generation when endpoint not approved"() {
		setup:
		setupBindings()
		def location = new UrlURI(uri:"https://test.example.com/SSO")
		def sso = SingleSignOnService.build(active:true, approved:false, binding:httpPost, location:location)
		
		when:
		metadataGenerationService.endpoint(builder, false, "SingleSignOnService", sso)
		
		then:
		def xml = writer.toString()
		xml == ""
	}
	
	def "Test valid indexed endpoint generation"() {
		setup:
		setupBindings()
		def location = new UrlURI(uri:"https://test.example.com/artifact")
		def ars = ArtifactResolutionService.build(active:true, approved:true, binding:httpPost, location:location)
		def result = loadResult('testvalidindexedendpointgeneration')
		
		when:
		metadataGenerationService.indexedEndpoint(builder, false, "ArtifactResolutionService", ars, 1)
		
		then:
		def xml = writer.toString()
		xml == result
	}
	
	def "Test valid indexed endpoint generation with response"() {
		setup:
		setupBindings()
		def location = new UrlURI(uri:"https://test.example.com/artifact")
		def responseLocation = new UrlURI(uri:"https://test.example.com/response")
		def ars = ArtifactResolutionService.build(active:true, approved:true, binding:httpPost, location:location, responseLocation:responseLocation)
		def result = loadResult('testvalidindexedendpointgenerationresponse')
		
		when:
		metadataGenerationService.indexedEndpoint(builder, false, "ArtifactResolutionService", ars, 1)
		
		then:
		def xml = writer.toString()
		xml == result
	}
	
	def "Test indexed endpoint generation when not active"() {
		setup:
		setupBindings()
		def location = new UrlURI(uri:"https://test.example.com/artifact")
		def ars = ArtifactResolutionService.build(active:false, approved:true, binding:httpPost, location:location)
		
		when:
		metadataGenerationService.indexedEndpoint(builder, false, "ArtifactResolutionService", ars, 1)
		
		then:
		def xml = writer.toString()
		xml == ""
	}
	
	def "Test indexed endpoint generation when not approved"() {
		setup:
		setupBindings()
		def location = new UrlURI(uri:"https://test.example.com/artifact")
		def ars = ArtifactResolutionService.build(active:true, approved:false, binding:httpPost, location:location)
		
		when:
		metadataGenerationService.indexedEndpoint(builder, false, "ArtifactResolutionService", ars, 1)
		
		then:
		def xml = writer.toString()
		xml == ""
	}
	
	def "Test valid organization generation"() {
		setup:
		def organization = Organization.build(name:"Test Organization", displayName:"Test Organization Display", lang:"en", url: new UrlURI(uri:"http://example.com"))
		def result = loadResult('testvalidorganization')
		
		when:
		metadataGenerationService.organization(builder, organization)
		
		then:
		def xml = writer.toString()
		xml == result
	}
	
	def "Test valid contact person generation"() {
		setup:
		def email = new MailURI(uri:"test@example.com")
		def home = new TelNumURI(uri:"(07) 1111 1111")
		def work = new TelNumURI(uri:"(567) 222 22222")
		def mobile = new TelNumURI(uri:"0413 867 208")
		def contact = Contact.build(givenName:"Test", surname:"User", email:email, homePhone:home, workPhone:work, mobilePhone:mobile)
		def admin = ContactType.build(name:"administrative")
		def contactPerson = ContactPerson.build(contact:contact, type:admin)
		def result = loadResult('testvalidcontact')
		
		when:
		metadataGenerationService.contactPerson(builder, contactPerson)
		
		then:
		def xml = writer.toString()
		xml == result
	}
	
	def "Test valid contact person generation with only mobile phone"() {
		setup:
		def email = new MailURI(uri:"test@example.com")
		def mobile = new TelNumURI(uri:"0413 867 208")
		def contact = Contact.build(givenName:"Test", surname:"User", email:email, mobilePhone:mobile)
		def admin = ContactType.build(name:"administrative")
		def contactPerson = ContactPerson.build(contact:contact, type:admin)
		def result = loadResult('testvalidcontactonlymobile')
		
		when:
		metadataGenerationService.contactPerson(builder, contactPerson)
		
		then:
		def xml = writer.toString()
		xml == result
	}
	
	def "Test valid EntitiesDescriptor generation"() {
		setup:
		def organization = Organization.build(active:true, approved:true, name:"Test Organization", displayName:"Test Organization Display", lang:"en", url: new UrlURI(uri:"http://example.com"))
		def email = new MailURI(uri:"test@example.com")
		def home = new TelNumURI(uri:"(07) 1111 1111")
		def work = new TelNumURI(uri:"(567) 222 22222")
		def mobile = new TelNumURI(uri:"0413 867 208")
		def contact = Contact.build(givenName:"Test", surname:"User", email:email, homePhone:home, workPhone:work, mobilePhone:mobile)
		def admin = ContactType.build(name:"administrative")
		def contactPerson = ContactPerson.build(contact:contact, type:admin)
		
		def entitiesDescriptor = new EntitiesDescriptor(name:"some.test.name")
		(1..2).each { i ->
			def entityDescriptor = EntityDescriptor.build(organization:organization, entityID:"https://test.example.com/myuniqueID$i", active:true, approved:true)
			entityDescriptor.addToContacts(contactPerson)
			entityDescriptor.addToIdpDescriptors(IDPSSODescriptor.build(entityDescriptor:entityDescriptor, organization:organization, active:true, approved:true))
			entityDescriptor.addToSpDescriptors(SPSSODescriptor.build(entityDescriptor:entityDescriptor, organization:organization, active:true, approved:true))
			entityDescriptor.addToAttributeAuthorityDescriptors(AttributeAuthorityDescriptor.build(entityDescriptor:entityDescriptor, organization:organization, active:true, approved:true))
			
			entitiesDescriptor.addToEntityDescriptors(entityDescriptor)
		}
		
		def keyInfo = new CAKeyInfo(certificate:new CACertificate(data:loadPK()))
		def certificate2 = cryptoService.createCertificate(loadPK())
		def keyInfo2 = new CAKeyInfo(certificate:new CACertificate(data:loadPK()))
		def certificateAuthorities = []
		certificateAuthorities.add(keyInfo)
		certificateAuthorities.add(keyInfo2)
		
		def validUntil = new GregorianCalendar(2009, Calendar.JULY, 22)
		def cacheExpires = new GregorianCalendar(2009, Calendar.JULY, 06)
			
		def result = loadResult('testvalidentitiesdescriptor')
		
		
		when:
		metadataGenerationService.entitiesDescriptor(builder, false, entitiesDescriptor, validUntil.getTime(), cacheExpires.getTime(), certificateAuthorities)
		
		then:
		def xml = writer.toString()
		xml == result
	}
	
	def "Test valid EntitiesDescriptor generation with embedded entitiesdescriptors"() {
		setup:
		def organization = Organization.build(active:true, approved:true, name:"Test Organization", displayName:"Test Organization Display", lang:"en", url: new UrlURI(uri:"http://example.com"))
		def email = new MailURI(uri:"test@example.com")
		def home = new TelNumURI(uri:"(07) 1111 1111")
		def work = new TelNumURI(uri:"(567) 222 22222")
		def mobile = new TelNumURI(uri:"0413 867 208")
		def contact = Contact.build(givenName:"Test", surname:"User", email:email, homePhone:home, workPhone:work, mobilePhone:mobile)
		def admin = ContactType.build(name:"administrative")
		def contactPerson = ContactPerson.build(contact:contact, type:admin)
		
		def entitiesDescriptor = new EntitiesDescriptor(name:"some.test.name")
		def entitiesDescriptor1 = new EntitiesDescriptor()
		entitiesDescriptor.addToEntitiesDescriptors(entitiesDescriptor1)
		(1..2).each { i ->
			def entityDescriptor = EntityDescriptor.build(organization:organization, entityID:"https://test.example.com/myuniqueID$i", active:true, approved:true)
			entityDescriptor.addToContacts(contactPerson)
			entityDescriptor.addToIdpDescriptors(IDPSSODescriptor.build(entityDescriptor:entityDescriptor, organization:organization, active:true, approved:true))
			entityDescriptor.addToSpDescriptors(SPSSODescriptor.build(entityDescriptor:entityDescriptor, organization:organization, active:true, approved:true))
			entityDescriptor.addToAttributeAuthorityDescriptors(AttributeAuthorityDescriptor.build(entityDescriptor:entityDescriptor, organization:organization, active:true, approved:true))
			
			entitiesDescriptor1.addToEntityDescriptors(entityDescriptor)
		}
		
		def keyInfo = new CAKeyInfo(certificate:new CACertificate(data:loadPK()))
		def certificate2 = cryptoService.createCertificate(loadPK())
		def keyInfo2 = new CAKeyInfo(certificate:new CACertificate(data:loadPK()))
		def certificateAuthorities = []
		certificateAuthorities.add(keyInfo)
		certificateAuthorities.add(keyInfo2)
		
		def validUntil = new GregorianCalendar(2009, Calendar.JULY, 22)
		def cacheExpires = new GregorianCalendar(2009, Calendar.JULY, 06)
			
		def result = loadResult('testvalidentitiesdescriptorembedded')
		
		
		when:
		metadataGenerationService.entitiesDescriptor(builder, false, entitiesDescriptor, validUntil.getTime(), cacheExpires.getTime(), certificateAuthorities)
		
		then:
		def xml = writer.toString()
		xml == result
	}
	
	def "Test inactive EntityDescriptor generation"() {
		setup:
		def ed = EntityDescriptor.build(active:false)
		
		when:
		metadataGenerationService.entityDescriptor(builder, false, ed)
		
		then:
		def xml = writer.toString()
		xml == ""
	}
	
	def "Test not approved EntityDescriptor generation"() {
		setup:
		def ed = EntityDescriptor.build(approved:false)
		
		when:
		metadataGenerationService.entityDescriptor(builder, false, ed)
		
		then:
		def xml = writer.toString()
		xml == ""
	}
	
	def "Test valid EntityDescriptor generation"() {
		setup:
		def organization = Organization.build(active:true, approved:true, name:"Test Organization", displayName:"Test Organization Display", lang:"en", url: new UrlURI(uri:"http://example.com"))
		def email = new MailURI(uri:"test@example.com")
		def home = new TelNumURI(uri:"(07) 1111 1111")
		def work = new TelNumURI(uri:"(567) 222 22222")
		def mobile = new TelNumURI(uri:"0413 867 208")
		def contact = Contact.build(givenName:"Test", surname:"User", email:email, homePhone:home, workPhone:work, mobilePhone:mobile)
		def admin = ContactType.build(name:"administrative")
		def contactPerson = ContactPerson.build(contact:contact, type:admin)
		
		def entityDescriptor = EntityDescriptor.build(organization:organization, entityID:"https://test.example.com/myuniqueID", active:true, approved:true)
		entityDescriptor.addToContacts(contactPerson)
		entityDescriptor.addToIdpDescriptors(IDPSSODescriptor.build(entityDescriptor:entityDescriptor, organization:organization, active:true, approved:true))
		entityDescriptor.addToIdpDescriptors(IDPSSODescriptor.build(entityDescriptor:entityDescriptor, organization:organization, active:true, approved:true))
		entityDescriptor.addToSpDescriptors(SPSSODescriptor.build(entityDescriptor:entityDescriptor, organization:organization, active:true, approved:true))
		entityDescriptor.addToSpDescriptors(SPSSODescriptor.build(entityDescriptor:entityDescriptor, organization:organization, active:true, approved:true))
		entityDescriptor.addToSpDescriptors(SPSSODescriptor.build(entityDescriptor:entityDescriptor, organization:organization, active:true, approved:true))
		entityDescriptor.addToAttributeAuthorityDescriptors(AttributeAuthorityDescriptor.build(entityDescriptor:entityDescriptor, organization:organization, active:true, approved:true))
		
		def result = loadResult('testvalidentitydescriptor')
		
		when:
		metadataGenerationService.entityDescriptor(builder, false, entityDescriptor)
		
		then:
		def xml = writer.toString()
		xml == result
	}
	
	def "Test inactive IDPSSODescriptor generation"() {
		setup:
		def idp = IDPSSODescriptor.build(active:false)
		
		when:
		metadataGenerationService.idpSSODescriptor(builder, false, idp)
		
		then:
		def xml = writer.toString()
		xml == ""
	}
	
	def "Test not approved IDPSSODescriptor generation"() {
		setup:
		def idp = IDPSSODescriptor.build(approved:false)
		
		when:
		metadataGenerationService.idpSSODescriptor(builder, false, idp)
		
		then:
		def xml = writer.toString()
		xml == ""
	}

	def "Test valid IDPSSODescriptor generation"() {
		setup:
		setupBindings()
		def saml2Prot = SamlURI.build(uri:'urn:oasis:names:tc:SAML:2.0:protocol')
		def saml1Prot = SamlURI.build(uri:'urn:oasis:names:tc:SAML:1.1:protocol urn:mace:shibboleth:1.0')
		def protocolSupportEnumerations = [saml1Prot, saml2Prot]
		
		def organization = Organization.build(name:"Test Organization", displayName:"Test Organization Display", lang:"en", url: new UrlURI(uri:"http://example.com"))
		def email = new MailURI(uri:"test@example.com")
		def home = new TelNumURI(uri:"(07) 1111 1111")
		def work = new TelNumURI(uri:"(567) 222 22222")
		def mobile = new TelNumURI(uri:"0413 867 208")
		def contact = Contact.build(givenName:"Test", surname:"User", email:email, homePhone:home, workPhone:work, mobilePhone:mobile)
		def admin = ContactType.build(name:"administrative")
		def contactPerson = ContactPerson.build(contact:contact, type:admin)
		
		def certificate = cryptoService.createCertificate(loadPK())
		def keyInfo = new KeyInfo(keyName:"key1", certificate:certificate)
		def encryptionMethod = new EncryptionMethod(algorithm:"http://www.w3.org/2001/04/xmlenc#tripledes-cbc")
		def keyDescriptor = new KeyDescriptor(keyInfo:keyInfo, encryptionMethod:encryptionMethod, keyType:KeyTypes.encryption)
		
		def certificate2 = cryptoService.createCertificate(loadPK())
		def keyInfo2 = new KeyInfo(keyName:"key2", certificate:certificate)
		def keyDescriptor2 = new KeyDescriptor(keyInfo:keyInfo2, keyType:KeyTypes.signing)
		
		def ars = new ArtifactResolutionService(active:true, approved:true, isDefault:true, binding:soap, location:new UrlURI(uri:"https://test.example.com/ars/artifact"))
		def ars2 = new ArtifactResolutionService(active:true, approved:true, isDefault:false, binding:soap, location:new UrlURI(uri:"https://test.example.com/ars/artifact2"))
		
		def slo = new SingleLogoutService(active:true, approved:true, binding:httpPost, location:new UrlURI(uri:"https://test.example.com/slo/POST"))
		def mnid = new ManageNameIDService(active:true, approved:true, binding:httpRedirect, location:new UrlURI(uri:"https://test.example.com/mnid/REDIRECT"))
		def nidf = new SamlURI(uri:"supported:nameid:format:urn")
		
		def sso = new SingleSignOnService(active:true, approved:true, binding:httpPost, location:new UrlURI(uri:"https://test.example.com/sso/POST"))
		def nidms = new NameIDMappingService(active:true, approved:true, binding:httpRedirect, location:new UrlURI(uri:"https://test.example.com/nameidmappingserivce/REDIRECT"))
		def aidrs = new AssertionIDRequestService(active:true, approved:true, binding:httpRedirect, location:new UrlURI(uri:"https://test.example.com/assertionidrequestservice/REDIRECT"))
		
		def base1 = new AttributeBase(name:'test attr', nameFormat:new SamlURI(uri:'test:attr:format'), friendlyName:'test attr friendly')
		def base2 = new AttributeBase(name:'test attr2', friendlyName:'test attr friendly 2')
		def base3 = new AttributeBase(name:'test attr3', nameFormat:new SamlURI(uri:'test:attr:format'))
		
		def attr1 = new Attribute(base:base1)
		attr1.addToValues(new AttributeValue(value:'val1'))
		attr1.addToValues(new AttributeValue(value:'val2'))
		
		def attr2 = new Attribute(base:base2)
		def attr3 = new Attribute(base:base3)
		
		def idp = IDPSSODescriptor.build(protocolSupportEnumerations:protocolSupportEnumerations, organization:organization, approved:true, active:true)
		idp.addToKeyDescriptors(keyDescriptor)
		idp.addToKeyDescriptors(keyDescriptor2)
		idp.addToContacts(contactPerson)
		
		idp.addToArtifactResolutionServices(ars)
		idp.addToArtifactResolutionServices(ars2)
		idp.addToSingleLogoutServices(slo)
		idp.addToManageNameIDServices(mnid)
		idp.addToNameIDMappingServices(nidms)
		idp.addToAssertionIDRequestServices(aidrs)
		idp.addToNameIDFormats(nidf)
		
		idp.addToSingleSignOnServices(sso)
		idp.addToAttributes(attr1)
		idp.addToAttributes(attr2)
		idp.addToAttributes(attr3)
		
		def result = loadResult('testvalididpssodescriptor')
		
		when:
		metadataGenerationService.idpSSODescriptor(builder, false, idp)
		
		then:
		def xml = writer.toString()
		xml == result
	}
	
	def "Test inactive SPSSODescriptor generation"() {
		setup:
		def sp = SPSSODescriptor.build(active:false)
		
		when:
		metadataGenerationService.spSSODescriptor(builder, false, sp)
		
		then:
		def xml = writer.toString()
		xml == ""
	}
	
	def "Test not approved SPSSODescriptor generation"() {
		setup:
		def sp = SPSSODescriptor.build(approved:false)
		
		when:
		metadataGenerationService.spSSODescriptor(builder, false, sp)
		
		then:
		def xml = writer.toString()
		xml == ""
	}
	
	def "Test valid SPSSODescriptor generation"() {
		setup:
		setupBindings()
		def saml2Prot = SamlURI.build(uri:'urn:oasis:names:tc:SAML:2.0:protocol')
		def saml1Prot = SamlURI.build(uri:'urn:oasis:names:tc:SAML:1.1:protocol urn:mace:shibboleth:1.0')
		def protocolSupportEnumerations = [saml1Prot, saml2Prot]
		
		def organization = Organization.build(name:"Test Organization", displayName:"Test Organization Display", lang:"en", url: new UrlURI(uri:"http://example.com"))
		def email = new MailURI(uri:"test@example.com")
		def home = new TelNumURI(uri:"(07) 1111 1111")
		def work = new TelNumURI(uri:"(567) 222 22222")
		def mobile = new TelNumURI(uri:"0413 867 208")
		def contact = Contact.build(givenName:"Test", surname:"User", email:email, homePhone:home, workPhone:work, mobilePhone:mobile)
		def admin = ContactType.build(name:"administrative")
		def contactPerson = ContactPerson.build(contact:contact, type:admin)
		
		def certificate = cryptoService.createCertificate(loadPK())
		def keyInfo = new KeyInfo(keyName:"key1", certificate:certificate)
		def encryptionMethod = new EncryptionMethod(algorithm:"http://www.w3.org/2001/04/xmlenc#tripledes-cbc")
		def keyDescriptor = new KeyDescriptor(keyInfo:keyInfo, encryptionMethod:encryptionMethod, keyType:KeyTypes.encryption)
		
		def certificate2 = cryptoService.createCertificate(loadPK())
		def keyInfo2 = new KeyInfo(keyName:"key2", certificate:certificate)
		def keyDescriptor2 = new KeyDescriptor(keyInfo:keyInfo2, keyType:KeyTypes.signing)
		
		def ars = new ArtifactResolutionService(active:true, approved:true, isDefault:true, binding:soap, location:new UrlURI(uri:"https://test.example.com/ars/artifact"))
		def ars2 = new ArtifactResolutionService(active:true, approved:true, isDefault:false, binding:soap, location:new UrlURI(uri:"https://test.example.com/ars/artifact2"))
		
		def slo = new SingleLogoutService(active:true, approved:true, binding:httpPost, location:new UrlURI(uri:"https://test.example.com/slo/POST"))
		def mnid = new ManageNameIDService(active:true, approved:true, binding:httpRedirect, location:new UrlURI(uri:"https://test.example.com/mnid/REDIRECT"))
		def nidf = new SamlURI(uri:"supported:nameid:format:urn")
		
		def acs = new AssertionConsumerService(active:true, approved:true, binding:soap, location:new UrlURI(uri:"https://test.example.com/acs/SOAP"))
		
		def base1 = new AttributeBase(name:'test attr', nameFormat:new SamlURI(uri:'test:attr:format'), friendlyName:'test attr friendly')
		def base2 = new AttributeBase(name:'test attr2', friendlyName:'test attr friendly 2')
		def base3 = new AttributeBase(name:'test attr3', nameFormat:new SamlURI(uri:'test:attr:format'))
		def base4 = new AttributeBase(name:'test attr3', nameFormat:new SamlURI(uri:'test:attr:format'))
		
		def attr1 = new RequestedAttribute(base:base1, isRequired:true, approved:true, reasoning:"valid test case")
		attr1.addToValues(new AttributeValue(value:'val1'))
		attr1.addToValues(new AttributeValue(value:'val2'))
		
		def attr2 = new RequestedAttribute(base:base2, isRequired:true, approved:true, reasoning:"valid test case")
		def attr3 = new RequestedAttribute(base:base3, isRequired:false, approved:true, reasoning:"valid test case")
		def attr4 = new RequestedAttribute(base:base4, isRequired:false, approved:false, reasoning:"valid test case")
		
		def attrService = new AttributeConsumingService(lang:'en')
		attrService.addToServiceNames("Test Name 1")
		attrService.addToServiceNames("Test Name 2")
		attrService.addToServiceDescriptions("This is a great description")
		attrService.addToRequestedAttributes(attr1)
		attrService.addToRequestedAttributes(attr2)
		attrService.addToRequestedAttributes(attr3)
		attrService.addToRequestedAttributes(attr4)
		
		def sp = SPSSODescriptor.build(protocolSupportEnumerations:protocolSupportEnumerations, organization:organization, approved:true, active:true)
		sp.addToKeyDescriptors(keyDescriptor)
		sp.addToKeyDescriptors(keyDescriptor2)
		sp.addToContacts(contactPerson)
		
		sp.addToArtifactResolutionServices(ars)
		sp.addToArtifactResolutionServices(ars2)
		sp.addToSingleLogoutServices(slo)
		sp.addToManageNameIDServices(mnid)
		sp.addToNameIDFormats(nidf)
		
		sp.addToAssertionConsumerServices(acs)
		sp.addToAttributeConsumingServices(attrService)
		
		def result = loadResult('testvalidspssodescriptor')
		
		when:
		metadataGenerationService.spSSODescriptor(builder, false, sp)
		
		then:
		def xml = writer.toString()
		xml == result
	}
	
	def "Test inactive AttributeAuthorityDescriptor generation"() {
		setup:
		def aa = AttributeAuthorityDescriptor.build(active:false)
		
		when:
		metadataGenerationService.attributeAuthorityDescriptor(builder, false, aa)
		
		then:
		def xml = writer.toString()
		xml == ""
	}
	
	def "Test not approved AttributeAuthorityDescriptor generation"() {
		setup:
		def aa = AttributeAuthorityDescriptor.build(approved:false)
		
		when:
		metadataGenerationService.attributeAuthorityDescriptor(builder, false, aa)
		
		then:
		def xml = writer.toString()
		xml == ""
	}
	
	def "Test valid AttributeAuthorityDescriptor creation when collaborating with IDP"() {
		setup:
		setupBindings()
		def saml2Prot = SamlURI.build(uri:'urn:oasis:names:tc:SAML:2.0:protocol')
		def saml1Prot = SamlURI.build(uri:'urn:oasis:names:tc:SAML:1.1:protocol urn:mace:shibboleth:1.0')
		def protocolSupportEnumerations = [saml1Prot, saml2Prot]

		def organization = Organization.build(name:"Test Organization", displayName:"Test Organization Display", lang:"en", url: new UrlURI(uri:"http://example.com"))
		def email = new MailURI(uri:"test@example.com")
		def home = new TelNumURI(uri:"(07) 1111 1111")
		def work = new TelNumURI(uri:"(567) 222 22222")
		def mobile = new TelNumURI(uri:"0413 867 208")
		def contact = Contact.build(givenName:"Test", surname:"User", email:email, homePhone:home, workPhone:work, mobilePhone:mobile)
		def admin = ContactType.build(name:"administrative")
		def contactPerson = ContactPerson.build(contact:contact, type:admin)

		def certificate = cryptoService.createCertificate(loadPK())
		def keyInfo = new KeyInfo(keyName:"key1", certificate:certificate)
		def encryptionMethod = new EncryptionMethod(algorithm:"http://www.w3.org/2001/04/xmlenc#tripledes-cbc")
		def keyDescriptor = new KeyDescriptor(keyInfo:keyInfo, encryptionMethod:encryptionMethod, keyType:KeyTypes.encryption)

		def certificate2 = cryptoService.createCertificate(loadPK())
		def keyInfo2 = new KeyInfo(keyName:"key2", certificate:certificate)
		def keyDescriptor2 = new KeyDescriptor(keyInfo:keyInfo2, keyType:KeyTypes.signing)
		
		def nidf = new SamlURI(uri:"supported:nameid:format:urn")
		def aidrs = new AssertionIDRequestService(active:true, approved:true, binding:httpRedirect, location:new UrlURI(uri:"https://test.example.com/assertionidrequestservice/REDIRECT"))
		
		def base1 = new AttributeBase(name:'test attr', nameFormat:new SamlURI(uri:'test:attr:format'), friendlyName:'test attr friendly')
		def base2 = new AttributeBase(name:'test attr2', friendlyName:'test attr friendly 2')
		def base3 = new AttributeBase(name:'test attr3', nameFormat:new SamlURI(uri:'test:attr:format'))
		
		def attr1 = new Attribute(base:base1)
		attr1.addToValues(new AttributeValue(value:'val1'))
		attr1.addToValues(new AttributeValue(value:'val2'))
		
		def attr2 = new Attribute(base:base2)
		def attr3 = new Attribute(base:base3)
		
		def ed = EntityDescriptor.build(entityID:"https://test.com", organization:organization)
		
		def idp = IDPSSODescriptor.build(protocolSupportEnumerations:protocolSupportEnumerations, entityDescriptor:ed, organization:organization, approved:true, active:true)
		idp.addToKeyDescriptors(keyDescriptor)
		idp.addToKeyDescriptors(keyDescriptor2)
		idp.addToContacts(contactPerson)
		
		idp.addToNameIDFormats(nidf)
		idp.addToAssertionIDRequestServices(aidrs)
		
		idp.addToAttributes(attr1)
		idp.addToAttributes(attr2)
		idp.addToAttributes(attr3)
		
		def aa = AttributeAuthorityDescriptor.build(protocolSupportEnumerations:protocolSupportEnumerations, entityDescriptor:ed, organization:organization, approved:true, active:true)
		aa.collaborator = idp
		aa
		
		idp.collaborator = aa
		idp
		
		def result = loadResult('testvalidaadescriptor')
		
		when:
		metadataGenerationService.attributeAuthorityDescriptor(builder, false, aa)
		
		then:
		def xml = writer.toString()
		xml == result
	}
	
	def "Test valid AttributeAuthorityDescriptor creation when not collaborating with IDP"() {
		setup:
		setupBindings()
		def saml2Prot = SamlURI.build(uri:'urn:oasis:names:tc:SAML:2.0:protocol')
		def saml1Prot = SamlURI.build(uri:'urn:oasis:names:tc:SAML:1.1:protocol urn:mace:shibboleth:1.0')
		def protocolSupportEnumerations = [saml1Prot, saml2Prot]

		def organization = Organization.build(name:"Test Organization", displayName:"Test Organization Display", lang:"en", url: new UrlURI(uri:"http://example.com"))
		def email = new MailURI(uri:"test@example.com")
		def home = new TelNumURI(uri:"(07) 1111 1111")
		def work = new TelNumURI(uri:"(567) 222 22222")
		def mobile = new TelNumURI(uri:"0413 867 208")
		def contact = Contact.build(givenName:"Test", surname:"User", email:email, homePhone:home, workPhone:work, mobilePhone:mobile)
		def admin = ContactType.build(name:"administrative")
		def contactPerson = ContactPerson.build(contact:contact, type:admin)

		def certificate = cryptoService.createCertificate(loadPK())
		def keyInfo = new KeyInfo(keyName:"key1", certificate:certificate)
		def encryptionMethod = new EncryptionMethod(algorithm:"http://www.w3.org/2001/04/xmlenc#tripledes-cbc")
		def keyDescriptor = new KeyDescriptor(keyInfo:keyInfo, encryptionMethod:encryptionMethod, keyType:KeyTypes.encryption)

		def certificate2 = cryptoService.createCertificate(loadPK())
		def keyInfo2 = new KeyInfo(keyName:"key2", certificate:certificate)
		def keyDescriptor2 = new KeyDescriptor(keyInfo:keyInfo2, keyType:KeyTypes.signing)
		
		def nidf = new SamlURI(uri:"supported:nameid:format:urn")
		def aidrs = new AssertionIDRequestService(active:true, approved:true, binding:httpRedirect, location:new UrlURI(uri:"https://test.example.com/assertionidrequestservice/REDIRECT"))
		
		def base1 = new AttributeBase(name:'test attr', nameFormat:new SamlURI(uri:'test:attr:format'), friendlyName:'test attr friendly')
		def base2 = new AttributeBase(name:'test attr2', friendlyName:'test attr friendly 2')
		def base3 = new AttributeBase(name:'test attr3', nameFormat:new SamlURI(uri:'test:attr:format'))
		
		def attr1 = new Attribute(base:base1)
		attr1.addToValues(new AttributeValue(value:'val1'))
		attr1.addToValues(new AttributeValue(value:'val2'))
		
		def attr2 = new Attribute(base:base2)
		def attr3 = new Attribute(base:base3)
		
		def aa = AttributeAuthorityDescriptor.build(protocolSupportEnumerations:protocolSupportEnumerations, organization:organization, approved:true, active:true)
		aa.addToKeyDescriptors(keyDescriptor)
		aa.addToKeyDescriptors(keyDescriptor2)
		aa.addToContacts(contactPerson)
		
		aa.addToNameIDFormats(nidf)
		aa.addToAssertionIDRequestServices(aidrs)
		
		aa.addToAttributes(attr1)
		aa.addToAttributes(attr2)
		aa.addToAttributes(attr3)
		
		def result = loadResult('testvalidaadescriptor')	// deliberate both paths should give same outcome
		
		when:
		metadataGenerationService.attributeAuthorityDescriptor(builder, false, aa)
		
		then:
		def xml = writer.toString()
		xml == result
	}
}