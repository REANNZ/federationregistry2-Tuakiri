import fedreg.core.*

/*
FR HOTFIX
This is only required for 1.0.2 -> 1.1

This script parses all ACS endpoints looking for SAML2 Artifact. A bug in SP registration set the binding to SOAP
and this was hence being rendered in metadata. After executing this script all ACS SAML 2 Artifact endpoints
will correctly be associated with the HTTP-Artifact binding.

Bradley Beddoes
7/1/2011
*/

def sps = SPSSODescriptor.list()
def invalidBinding = SamlURI.findByUri(SamlConstants.soap)
def validBinding = SamlURI.findByUri(SamlConstants.httpArtifact)

sps.each { sp ->

  sp.assertionConsumerServices.sort{it.id}.eachWithIndex { acs, i ->
    if(acs.location.uri.endsWith('/Shibboleth.sso/SAML2/Artifact') && acs.binding.id == invalidBinding.id) {
      println "Updating $acs to use $validBinding" 
      acs.binding = validBinding
      acs.save()
      if(acs.hasErrors()) {
        println "Error updating $acs for $sp" 
      }
    }
  }

}