package aaf.fr.foundation

import org.codehaus.groovy.grails.web.servlet.mvc.SynchronizerTokensHolder

import grails.plugin.spock.*
import aaf.fr.workflow.*
import aaf.fr.identity.Subject

class IdentityProviderControllerSpec extends IntegrationSpec {
  
  def controller, idpssoDescriptorService, user
  
  def cleanup() {
    
  }
  
  def setup () {
    idpssoDescriptorService = new IdentityProviderService()
    
    controller = new IdentityProviderController(IdentityProviderService:idpssoDescriptorService)
    
    user = Subject.build()
    SpecHelpers.setupShiroEnv(user)
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
    model.identityProviderList.size() >= 25
  }
  
  def "Show with no ID"() {   
    when:
    controller.show()
    
    then:
    controller.flash.type == "error"
    controller.flash.message == "controllers.fr.generic.namevalue.missing"
    controller.response.redirectedUrl == "/membership/identityprovider/list"
  }
  
  def "Show with invalid IDPSSODescriptor ID"() {
    setup:
    controller.params.id = 200000000
    
    when:
    controller.show()
    
    then:
    controller.flash.type == "error"
    controller.flash.message == "aaf.fr.foundation.idpssoroledescriptor.nonexistant"
    controller.response.redirectedUrl == "/membership/identityprovider/list"
  }
  
  def "Validate create"() {
    setup:
    (1..10).each {
      Organization.build(active:true, approved:true).save()
    }
    
    (1..11).each { i ->
      AttributeBase.build(name: "attr$i").save()
    }
    
    (1..12).each { i ->
      SamlURI.build(type:SamlURIType.NameIdentifierFormat).save()
    }
    
    when:
    def model = controller.create()

    then:
    println model
    model.identityProvider != null
    model.identityProvider instanceof IDPSSODescriptor
    model.organizationList.size() > 0
    model.attributeList.size() > 0
    model.nameIDFormatList.size() > 0
  }
  
  def "Validate successful save"() {
    setup:
    def params = [:]
    def organization = Organization.build().save()
    def entityDescriptor = EntityDescriptor.build(organization:organization).save()
    def identityProvider = IDPSSODescriptor.build(entityDescriptor:entityDescriptor).save()
    def attributeAuthority = AttributeAuthorityDescriptor.build(entityDescriptor:entityDescriptor).save()
    def httpPost = SamlURI.build().save()
    def httpRedirect = SamlURI.build().save()
    def soapArtifact = SamlURI.build().save()
    def organizationList = [organization]
    def attributeList = [Attribute.build().save()]
    def nameIDFormatList = [SamlURI.build().save()]
    def contact = Contact.build().save()

    def token = SynchronizerTokensHolder.store(controller.session)
    controller.params[SynchronizerTokensHolder.TOKEN_URI] = "/identityProvider/save"
    controller.params[SynchronizerTokensHolder.TOKEN_KEY] = token.generateToken(controller.params[SynchronizerTokensHolder.TOKEN_URI])
    
    when:
    idpssoDescriptorService.metaClass.create = { def p -> 
      return [true, [organization:organization, entityDescriptor:entityDescriptor, identityProvider:identityProvider, attributeAuthority:attributeAuthority, httpPost:httpPost, httpRedirect:httpRedirect, soapArtifact:soapArtifact, organizationList:organizationList, attributeList:attributeList, nameIDFormatList:nameIDFormatList, contact:contact]]
    } 
    def model = controller.save()
    
    then:
    controller.response.redirectedUrl == "/membership/identityprovider/show/${identityProvider.id}" 
  }
  
  def "Validate failed save"() {
    setup:
    def params = [:]
    def organization = Organization.build().save()
    def entityDescriptor = EntityDescriptor.build(organization:organization).save()
    def identityProvider = IDPSSODescriptor.build(entityDescriptor:entityDescriptor).save()
    def attributeAuthority = AttributeAuthorityDescriptor.build(entityDescriptor:entityDescriptor).save()
    def httpPost = SamlURI.build().save()
    def httpRedirect = SamlURI.build().save()
    def soapArtifact = SamlURI.build().save()
    def organizationList = [organization]
    def attributeList = [Attribute.build().save()]
    def nameIDFormatList = [SamlURI.build().save()]
    def contact = Contact.build().save()

    def token = SynchronizerTokensHolder.store(controller.session)
    controller.params[SynchronizerTokensHolder.TOKEN_URI] = "/identityProvider/save"
    controller.params[SynchronizerTokensHolder.TOKEN_KEY] = token.generateToken(controller.params[SynchronizerTokensHolder.TOKEN_URI])
    
    when:
    idpssoDescriptorService.metaClass.create = { def p -> 
      return [false, [organization:organization, entityDescriptor:entityDescriptor, identityProvider:identityProvider, attributeAuthority:attributeAuthority, httpPost:httpPost, httpRedirect:httpRedirect, soapArtifact:soapArtifact, organizationList:organizationList, attributeList:attributeList, nameIDFormatList:nameIDFormatList, contact:contact]]
    } 
    def model = controller.save()
    
    then:
    organization == controller.modelAndView.model.organization  
    entityDescriptor == controller.modelAndView.model.entityDescriptor  
    identityProvider == controller.modelAndView.model.identityProvider
    attributeAuthority == controller.modelAndView.model.attributeAuthority
    
    controller.flash.type == "error"
    controller.flash.message == "aaf.fr.foundation.idpssoroledescriptor.save.validation.error"  
  }
  
  def "Validate successful update"() {
    setup:
    def organization = Organization.build().save()
    def entityDescriptor = EntityDescriptor.build(organization:organization).save()
    def identityProvider = IDPSSODescriptor.build(entityDescriptor:entityDescriptor).save()
    
    controller.params.id = identityProvider.id
    user.permissions.add("federation:management:descriptor:${identityProvider.id}:update")
    idpssoDescriptorService.metaClass.update = { def p -> 
      return [true, identityProvider]
    } 

    when:
    controller.update()
    
    then:
    controller.response.redirectedUrl == "/membership/identityprovider/show/${identityProvider.id}" 
  }

  def "Validate update with incorrect perms"() {
    setup:
    def organization = Organization.build().save()
    def entityDescriptor = EntityDescriptor.build(organization:organization).save()
    def identityProvider = IDPSSODescriptor.build(entityDescriptor:entityDescriptor).save()
    
    controller.params.id = identityProvider.id
    user.permissions.add("federation:management:descriptor:-1:update")

    when:
    controller.update()
    
    then:
    controller.response.status == 403 
  }
  
  def "Validate failed update"() {
    setup:
    def organization = Organization.build().save()
    def entityDescriptor = EntityDescriptor.build(organization:organization).save()
    def identityProvider = IDPSSODescriptor.build(entityDescriptor:entityDescriptor).save()
    
    controller.params.id = identityProvider.id
    user.permissions.add("federation:management:descriptor:${identityProvider.id}:update")
    idpssoDescriptorService.metaClass.update = { def p -> 
      return [false, identityProvider]
    } 

    when:
    def model = controller.update()
    
    then:
    controller.flash.type == "error"
    controller.flash.message == "aaf.fr.foundation.identityprovider.update.validation.error"  
  }
}
