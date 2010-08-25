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
		soap = new SamlURI(type:SamlURIType.ProtocolBinding, uri:'urn:oasis:names:tc:SAML:2.0:bindings:SOAP', description:'').save()
		httpRedirect = new SamlURI(type:SamlURIType.ProtocolBinding, uri:'urn:oasis:names:tc:SAML:2.0:bindings:HTTP-Redirect', description:'').save()
		httpPost = new SamlURI(type:SamlURIType.ProtocolBinding, uri:'urn:oasis:names:tc:SAML:2.0:bindings:HTTP-POST', description:'').save()
		httpArtifact = new SamlURI(type:SamlURIType.ProtocolBinding, uri:'urn:oasis:names:tc:SAML:2.0:bindings:HTTP-Artifact', description:'').save()
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
		def sso = SingleSignOnService.build(active:true, approved:true, binding:httpPost, location:location).save()
		def result = loadResult('testvalidendpointgeneration')
		
		when:
		metadataGenerationService.endpoint(builder, "SingleSignOnService", sso)
		
		then:
		def xml = writer.toString()
		xml == result
	}
	
	def "Test valid endpoint generation with response"() {
		setup:
		setupBindings()
		def location = new UrlURI(uri:"https://test.example.com/SSO")
		def responseLocation = new UrlURI(uri:"https://test.example.com/response")
		def sso = SingleSignOnService.build(active:true, approved:true, binding:httpPost, location:location, responseLocation:responseLocation).save()
		def result = loadResult('testvalidendpointgenerationresponse')
		
		when:
		metadataGenerationService.endpoint(builder, "SingleSignOnService", sso)
		
		then:
		def xml = writer.toString()
		xml == result
	}
	
	def "Test endpoint generation when endpoint inactive"() {
		setup:
		setupBindings()
		def location = new UrlURI(uri:"https://test.example.com/SSO")
		def sso = SingleSignOnService.build(active:false, approved:true, binding:httpPost, location:location).save()
		
		when:
		metadataGenerationService.endpoint(builder, "SingleSignOnService", sso)
		
		then:
		def xml = writer.toString()
		xml == ""
	}
	
	def "Test endpoint generation when endpoint not approved"() {
		setup:
		setupBindings()
		def location = new UrlURI(uri:"https://test.example.com/SSO")
		def sso = SingleSignOnService.build(active:true, approved:false, binding:httpPost, location:location).save()
		
		when:
		metadataGenerationService.endpoint(builder, "SingleSignOnService", sso)
		
		then:
		def xml = writer.toString()
		xml == ""
	}
	
	def "Test valid indexed endpoint generation"() {
		setup:
		setupBindings()
		def location = new UrlURI(uri:"https://test.example.com/artifact")
		def ars = ArtifactResolutionService.build(active:true, approved:true, binding:httpPost, location:location, endpointIndex:1).save()
		def result = loadResult('testvalidindexedendpointgeneration')
		
		when:
		metadataGenerationService.indexedEndpoint(builder, "ArtifactResolutionService", ars)
		
		then:
		def xml = writer.toString()
		xml == result
	}
	
	def "Test valid indexed endpoint generation with response"() {
		setup:
		setupBindings()
		def location = new UrlURI(uri:"https://test.example.com/artifact")
		def responseLocation = new UrlURI(uri:"https://test.example.com/response")
		def ars = ArtifactResolutionService.build(active:true, approved:true, binding:httpPost, location:location, responseLocation:responseLocation, endpointIndex:1).save()
		def result = loadResult('testvalidindexedendpointgenerationresponse')
		
		when:
		metadataGenerationService.indexedEndpoint(builder, "ArtifactResolutionService", ars)
		
		then:
		def xml = writer.toString()
		xml == result
	}
	
	def "Test indexed endpoint generation when not active"() {
		setup:
		setupBindings()
		def location = new UrlURI(uri:"https://test.example.com/artifact")
		def ars = ArtifactResolutionService.build(active:false, approved:true, binding:httpPost, location:location, endpointIndex:1).save()
		
		when:
		metadataGenerationService.indexedEndpoint(builder, "ArtifactResolutionService", ars)
		
		then:
		def xml = writer.toString()
		xml == ""
	}
	
	def "Test indexed endpoint generation when not approved"() {
		setup:
		setupBindings()
		def location = new UrlURI(uri:"https://test.example.com/artifact")
		def ars = ArtifactResolutionService.build(active:true, approved:false, binding:httpPost, location:location, endpointIndex:1).save()
		
		when:
		metadataGenerationService.indexedEndpoint(builder, "ArtifactResolutionService", ars)
		
		then:
		def xml = writer.toString()
		xml == ""
	}
	
	def "Test valid organization generation"() {
		setup:
		def organization = Organization.build(name:"Test Organization", displayName:"Test Organization Display", lang:"en", url: new UrlURI(uri:"http://example.com")).save()
		def result = loadResult('testvalidorganization')
		
		when:
		metadataGenerationService.organization(builder, organization)
		
		then:
		def xml = writer.toString()
		xml == result
	}
	
	def "Test valid contact person generation"() {
		setup:
		def email = new MailURI(uri:"test@example.com").save()
		def home = new TelNumURI(uri:"(07) 1111 1111").save()
		def work = new TelNumURI(uri:"(567) 222 22222").save()
		def mobile = new TelNumURI(uri:"0413 867 208").save()
		def contact = Contact.build(givenName:"Test", surname:"User", email:email, homePhone:home, workPhone:work, mobilePhone:mobile).save()
		def admin = ContactType.build(name:"administrative").save()
		def contactPerson = ContactPerson.build(contact:contact, type:admin).save()
		def result = loadResult('testvalidcontact')
		
		when:
		metadataGenerationService.contactPerson(builder, contactPerson)
		
		then:
		def xml = writer.toString()
		xml == result
	}
	
	def "Test valid contact person generation with only mobile phone"() {
		setup:
		def email = new MailURI(uri:"test@example.com").save()
		def mobile = new TelNumURI(uri:"0413 867 208").save()
		def contact = Contact.build(givenName:"Test", surname:"User", email:email, mobilePhone:mobile).save()
		def admin = ContactType.build(name:"administrative").save()
		def contactPerson = ContactPerson.build(contact:contact, type:admin).save()
		def result = loadResult('testvalidcontactonlymobile')
		
		when:
		metadataGenerationService.contactPerson(builder, contactPerson)
		
		then:
		def xml = writer.toString()
		xml == result
	}

	def "Test valid IDPSSODescriptor generation"() {
		setup:
		setupBindings()
		def saml2Prot = SamlURI.build(uri:'urn:oasis:names:tc:SAML:2.0:protocol').save()
		def saml1Prot = SamlURI.build(uri:'urn:oasis:names:tc:SAML:1.1:protocol urn:mace:shibboleth:1.0').save()
		def protocolSupportEnumerations = [saml1Prot, saml2Prot]
		
		def organization = Organization.build(name:"Test Organization", displayName:"Test Organization Display", lang:"en", url: new UrlURI(uri:"http://example.com")).save()
		def email = new MailURI(uri:"test@example.com").save()
		def home = new TelNumURI(uri:"(07) 1111 1111").save()
		def work = new TelNumURI(uri:"(567) 222 22222").save()
		def mobile = new TelNumURI(uri:"0413 867 208").save()
		def contact = Contact.build(givenName:"Test", surname:"User", email:email, homePhone:home, workPhone:work, mobilePhone:mobile).save()
		def admin = ContactType.build(name:"administrative").save()
		def contactPerson = ContactPerson.build(contact:contact, type:admin).save()
		
		def certificate = cryptoService.createCertificate(loadPK())
		def keyInfo = new KeyInfo(keyName:"key1", certificate:certificate)
		def encryptionMethod = new EncryptionMethod(algorithm:"http://www.w3.org/2001/04/xmlenc#tripledes-cbc")
		def keyDescriptor = new KeyDescriptor(keyInfo:keyInfo, encryptionMethod:encryptionMethod, keyType:KeyTypes.encryption)
		
		def certificate2 = cryptoService.createCertificate(loadPK())
		def keyInfo2 = new KeyInfo(keyName:"key2", certificate:certificate)
		def keyDescriptor2 = new KeyDescriptor(keyInfo:keyInfo2, keyType:KeyTypes.signing)
		
		def ars = new ArtifactResolutionService(active:true, approved:true, endpointIndex:1, isDefault:true, binding:soap, location:new UrlURI(uri:"https://test.example.com/ars/artifact"))
		def ars2 = new ArtifactResolutionService(active:true, approved:true, endpointIndex:1, isDefault:false, binding:soap, location:new UrlURI(uri:"https://test.example.com/ars/artifact2"))
		
		def slo = new SingleLogoutService(active:true, approved:true, binding:httpPost, location:new UrlURI(uri:"https://test.example.com/slo/POST"))
		def mnid = new ManageNameIDService(active:true, approved:true, binding:httpRedirect, location:new UrlURI(uri:"https://test.example.com/mnid/REDIRECT"))
		def nidf = new SamlURI(uri:"supported:nameid:format:urn")
		
		def idp = IDPSSODescriptor.build(protocolSupportEnumerations:protocolSupportEnumerations, organization:organization)
		idp.addToKeyDescriptors(keyDescriptor)
		idp.addToKeyDescriptors(keyDescriptor2)
		idp.addToContacts(contactPerson)
		
		idp.addToArtifactResolutionServices(ars)
		idp.addToArtifactResolutionServices(ars2)
		idp.addToSingleLogoutServices(slo)
		idp.addToManageNameIDServices(mnid)
		idp.addToNameIDFormats(nidf)
		
		
		idp.save()
		
		def result = loadResult('testvalididpssodescriptor')
		
		when:
		metadataGenerationService.idpSSODescriptor(builder, idp)
		
		then:
		def xml = writer.toString()
		println xml
		xml == result
	}
	
	
	
	
}