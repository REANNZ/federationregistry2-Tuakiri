package aaf.fr.foundation

import grails.test.spock.*

class EntityDescriptorSpec extends IntegrationSpec {

	def "Ensure functioning operates as expected"() {
		when:
		def org = new Organization(active:true, approved:true)
		def ed = new EntityDescriptor(organization:org, active:true, approved:true)
		
		then:
		ed.functioning()
	}
	
	def "Ensure functioning fails when not active as expected"() {
		when:
		def org = new Organization(active:true, approved:true)
		def ed = new EntityDescriptor(organization:org, active:false, approved:true)
		
		then:
		!ed.functioning()
	}
	
	def "Ensure functioning fails when unapproved as expected"() {
		when:
		def org = new Organization(active:true, approved:true)
		def ed = new EntityDescriptor(organization:org, active:true, approved:false)
		
		then:
		!ed.functioning()
	}
	
	def "Ensure functioning fails when not active and unapproved as expected"() {
		when:
		def org = new Organization(active:true, approved:true)
		def ed = new EntityDescriptor(organization:org, active:false, approved:false)
		
		then:
		!ed.functioning()
	}
	
	def "Ensure functioning fails locally fine but parent org not functioning"() {
		when:
		def org = new Organization(active:true, approved:false)
		def ed = new EntityDescriptor(organization:org, active:true, approved:false)
		
		then:
		!ed.functioning()
	}
	
	def "Ensure EntityDescriptor not empty with a samlValid IDP child"() {
		when:
		def o = Organization.build(active:true, approved:true)
		def ed = EntityDescriptor.build(active:true, approved:true, organization:o)
		def idp = new IDPSSODescriptor(active:true, approved:true, entityDescriptor:ed)
		def httpPost = new SamlURI(type:SamlURIType.ProtocolBinding, uri:'urn:oasis:names:tc:SAML:2.0:bindings:HTTP-POST', description:'')
		def sso = new SingleSignOnService(descriptor:idp,active:true, approved:true, binding:httpPost, location:"https://test.example.com/sso/POST")
		idp.addToSingleSignOnServices(sso)
		ed.addToIdpDescriptors(idp)

		then:
		!ed.empty()
	}

	def "Ensure EntityDescriptor empty with an IDP child not samlValid"() {
		when:
		def o = Organization.build(active:true, approved:true)
		def ed = EntityDescriptor.build(active:true, approved:true, organization:o)
		def idp = new IDPSSODescriptor(active:true, approved:true, entityDescriptor:ed)
		ed.addToIdpDescriptors(idp)

		then:
		ed.empty()
	}

	def "Ensure EntityDescriptor not empty with a samlValid SP child"() {
		when:
		def o = Organization.build(active:true, approved:true)
		def ed = EntityDescriptor.build(active:true, approved:true, organization:o)
		def sp = new SPSSODescriptor(active:true, approved:true, entityDescriptor:ed)
		def httpArtifact = new SamlURI(type:SamlURIType.ProtocolBinding, uri:'urn:oasis:names:tc:SAML:2.0:bindings:HTTP-Artifact', description:'')
		def acs = new AssertionConsumerService(index:300, active:true, approved:true, binding:httpArtifact, location:"https://test.example.com/acs/ART")
		sp.addToAssertionConsumerServices(acs)
		ed.addToSpDescriptors(sp)

		then:
		!ed.empty()
	}

	def "Ensure EntityDescriptor empty with an SP child not samlValid"() {
		when:
		def o = Organization.build(active:true, approved:true)
		def ed = EntityDescriptor.build(active:true, approved:true, organization:o)
		def sp = new SPSSODescriptor(active:true, approved:true, entityDescriptor:ed)
		ed.addToSpDescriptors(sp)

		then:
		ed.empty()
	}

	def "Ensure EntityDescriptor not empty with a samlValid AA child"() {
		when:
		def o = Organization.build(active:true, approved:true)
		def ed = EntityDescriptor.build(active:true, approved:true, organization:o)
		def aa = new AttributeAuthorityDescriptor(active:true, approved:true, entityDescriptor:ed)

		def soap = new SamlURI(type:SamlURIType.ProtocolBinding, uri:'urn:oasis:names:tc:SAML:2.0:bindings:SOAP', description:'')
		def attrService = new AttributeService(active:true, approved:true, location:'https://idp.example.org:8443/idp/profile/SAML2/SOAP/AttributeQuery', binding:soap)
		aa.addToAttributeServices(attrService)

		ed.addToAttributeAuthorityDescriptors(aa)

		then:
		!ed.empty()
	}

	def "Ensure EntityDescriptor empty with an AA child not samlValid"() {
		when:
		def o = Organization.build(active:true, approved:true)
		def ed = EntityDescriptor.build(active:true, approved:true, organization:o)
		def aa = new AttributeAuthorityDescriptor(active:true, approved:true, entityDescriptor:ed)

		ed.addToAttributeAuthorityDescriptors(aa)

		then:
		ed.empty()
	}

	def "Ensure EntityDescriptor not empty with samlValid IDP and AA child"() {
		when:
		def o = Organization.build(active:true, approved:true)
		def ed = EntityDescriptor.build(active:true, approved:true, organization:o)
		def idp = new IDPSSODescriptor(active:true, approved:true, entityDescriptor:ed)
		def httpPost = new SamlURI(type:SamlURIType.ProtocolBinding, uri:'urn:oasis:names:tc:SAML:2.0:bindings:HTTP-POST', description:'')
		def sso = new SingleSignOnService(descriptor:idp,active:true, approved:true, binding:httpPost, location:"https://test.example.com/sso/POST")
		idp.addToSingleSignOnServices(sso)
		ed.addToIdpDescriptors(idp)

		def aa = new AttributeAuthorityDescriptor(active:true, approved:true, entityDescriptor:ed)
		def soap = new SamlURI(type:SamlURIType.ProtocolBinding, uri:'urn:oasis:names:tc:SAML:2.0:bindings:SOAP', description:'')
		def attrService = new AttributeService(active:true, approved:true, location:'https://idp.example.org:8443/idp/profile/SAML2/SOAP/AttributeQuery', binding:soap)
		aa.addToAttributeServices(attrService)
		ed.addToAttributeAuthorityDescriptors(aa)

		then:
		!ed.empty()
	}

	def "Ensure EntityDescriptor empty when no child nodes"() {
		when:
		def ed = new EntityDescriptor()
		
		then:
		ed.empty()
	}

    def "Ensure EntityDescriptor empty no functioning child nodes"() {
                when:
        def ed = new EntityDescriptor(active:true, approved:true)
        def idp = new IDPSSODescriptor(archived:true)
        ed.addToIdpDescriptors(idp)
        def sp = new SPSSODescriptor(active:false, approved:false)
        ed.addToSpDescriptors(sp)
        
        then:
        ed.empty()
    }

}
