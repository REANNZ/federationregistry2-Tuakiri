package aaf.fr.foundation

import grails.plugin.spock.*

class EndpointSpec extends IntegrationSpec {
  def soap

  def setup() {
    soap = new SamlURI(type:SamlURIType.ProtocolBinding, uri:'urn:oasis:names:tc:SAML:2.0:bindings:SOAP', description:'')
  }

  def "Ensure exposed operates"() {
    when:
    def org = new Organization(active:true, approved:true)
    def ed = new EntityDescriptor(organization:org, active:true, approved:true)
    def aa = new AttributeAuthorityDescriptor(organization:org, entityDescriptor:ed, active:true, approved:true)
    def ep = new AttributeService(descriptor:aa, active:true, approved:true, location:'https://idp.example.org:8443/idp/profile/SAML2/SOAP/AttributeQuery', binding:soap)

    then:
    ep.exposed()
  }

  def "Ensure exposed fails when unapproved"() {
    when:
    def org = new Organization(active:true, approved:true)
    def ed = new EntityDescriptor(organization:org, active:true, approved:true)
    def aa = new AttributeAuthorityDescriptor(organization:org, entityDescriptor:ed, active:true, approved:true)
    def ep = new AttributeService(descriptor:aa, active:true, approved:false, location:'https://idp.example.org:8443/idp/profile/SAML2/SOAP/AttributeQuery', binding:soap)

    then:
    !ep.exposed()
  }

  def "Ensure exposed fails when not active and unapproved"() {
    when:
    def org = new Organization(active:true, approved:true)
    def ed = new EntityDescriptor(organization:org, active:true, approved:true)
    def aa = new AttributeAuthorityDescriptor(organization:org, entityDescriptor:ed, active:true, approved:true)
    def ep = new AttributeService(descriptor:aa, active:false, approved:false, location:'https://idp.example.org:8443/idp/profile/SAML2/SOAP/AttributeQuery', binding:soap)

    then:
    !ep.exposed()
  }

}

