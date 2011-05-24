/*
Removes endpoint entries associated with a Shibboleth 1.3 IdP and instigates correct endpoint format for Shibboleth 2.x IdP

Author: Bradley Beddoes
24/5/2011
*/

import fedreg.core.*

def commit = false

def id = 491
def host = "https://shib1.its.rmit.edu.au"

// No changes below this point

def identityProvider = IDPSSODescriptor.get(id)
if(!identityProvider) {
  println "IDP not located"
  return
}

def attributeAuthority = identityProvider.collaborator
if(!attributeAuthority) {
  println "Collaborating AA not located"
  return  
}

identityProvider.singleSignOnServices.toList().each {
  if(commit) {
    println "Deleting $it"
    it.delete()
    identityProvider.removeFromSingleSignOnServices(it)
  } else {
    println "Non commit - would have deleted $it"
  }
}

identityProvider.nameIDMappingServices.toList().each {
  if(commit) {
    println "Deleting $it"
    it.delete()
    identityProvider.removeFromNameIDMappingServices(it)
  } else {
    println "Non commit - would have deleted $it"
  }
}

identityProvider.assertionIDRequestServices.toList().each {
  if(commit) {
    println "Deleting $it"
    it.delete()
    identityProvider.removeFromAssertionIDRequestServices(it)
  } else {
    println "Non commit - would have deleted $it"
  }
}

identityProvider.artifactResolutionServices.toList().each {
  if(commit) {
    println "Deleting $it"
    it.delete()
    identityProvider.removeFromArtifactResolutionServices(it)
  } else {
    println "Non commit - would have deleted $it"
  }
}

identityProvider.singleLogoutServices.toList().each {
  if(commit) {
    println "Deleting $it"
    it.delete()
    identityProvider.removeFromSingleLogoutServices(it)
  } else {
    println "Non commit - would have deleted $it"
  }
}

identityProvider.manageNameIDServices.toList().each {
  if(commit) {
    println "Deleting $it"
    it.delete()
    identityProvider.removeFromManageNameIDServices(it)
  } else {
    println "Non commit - would have deleted $it"
  }
}

attributeAuthority.attributeServices.toList().each {
  if(commit) {
    println "Deleting $it"
    it.delete()
    attributeAuthority.removeFromAttributeServices(it)
  } else {
    println "Non commit - would have deleted $it"
  }
}

def saml2Namespace = SamlURI.findByUri('urn:oasis:names:tc:SAML:2.0:protocol')
identityProvider.addToProtocolSupportEnumerations(saml2Namespace)  

def postBinding = SamlURI.findByUri(SamlConstants.httpPost)
def postLocation = new UrlURI(uri: "$host/idp/profile/SAML2/POST/SSO")
def httpPost = new SingleSignOnService(descriptor:identityProvider, approved: true, binding: postBinding, location:postLocation, active:true)
identityProvider.addToSingleSignOnServices(httpPost)

def redirectBinding = SamlURI.findByUri(SamlConstants.httpRedirect)
def redirectLocation = new UrlURI(uri: "$host/idp/profile/SAML2/Redirect/SSO")
def httpRedirect = new SingleSignOnService(descriptor:identityProvider, approved: true, binding: redirectBinding, location:redirectLocation, active:true)
identityProvider.addToSingleSignOnServices(httpRedirect)

def artifactBinding = SamlURI.findByUri(SamlConstants.soap)
def artifactLocation = new UrlURI(uri: "$host:8443/idp/profile/SAML2/SOAP/ArtifactResolution")
def soapArtifact = new ArtifactResolutionService(descriptor:identityProvider, approved: true, binding: artifactBinding, location:artifactLocation, index:2, active:true, isDefault:true)
identityProvider.addToArtifactResolutionServices(soapArtifact)

attributeAuthority.addToProtocolSupportEnumerations(saml2Namespace)

def attributeServiceBinding = SamlURI.findByUri('urn:oasis:names:tc:SAML:2.0:bindings:SOAP')
def attributeServiceLocation = new UrlURI(uri: "$host:8443/idp/profile/SAML2/SOAP/AttributeQuery")
soapAttributeService = new AttributeService(descriptor:attributeAuthority, approved: true, binding: attributeServiceBinding, location:attributeServiceLocation, active:true)
attributeAuthority.addToAttributeServices(soapAttributeService)
  
if(commit) {
  println "Commiting new endpoints to $identityProvider"
  if(!identityProvider.save()) {
    println "Unable to save $identityProvider"
    identityProvider.errors.each { print it }
  }
} else {
  println "Non commit - would have added new endpoints to $identityProvider"
}