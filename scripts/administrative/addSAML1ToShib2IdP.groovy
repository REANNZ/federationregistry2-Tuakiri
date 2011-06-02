/*
Adds SAML 1 endpoints to Shibboleth IdP in FR (not provided by default as of FR 1.2)

Author: Bradley Beddoes
24/5/2011
*/

import fedreg.core.*

def commit = false

def id =	// ID from IdP view
def host =	// e.g https://node.idp.uni.edu.au

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

def saml1Namespace = SamlURI.findByUri('urn:oasis:names:tc:SAML:1.1:protocol')
def shibNamespace = SamlURI.findByUri('urn:mace:shibboleth:1.0 ')

if(!identityProvider.protocolSupportEnumerations.contains(saml1Namespace))
	identityProvider.addToProtocolSupportEnumerations(saml1Namespace)
	
if(!identityProvider.protocolSupportEnumerations.contains(shibNamespace))
	identityProvider.addToProtocolSupportEnumerations(shibNamespace)  

def postBinding = SamlURI.findByUri(SamlConstants.shibAuthn)
def postLocation = new UrlURI(uri: "$host/idp/profile/Shibboleth/SSO")
def httpPost = new SingleSignOnService(descriptor:identityProvider, approved: true, binding: postBinding, location:postLocation, active:true)
identityProvider.addToSingleSignOnServices(httpPost)

def artifactBinding = SamlURI.findByUri(SamlConstants.soap1)
def artifactLocation = new UrlURI(uri: "$host:8443/idp/profile/SAML1/SOAP/ArtifactResolution")
def soapArtifact = new ArtifactResolutionService(descriptor:identityProvider, approved: true, binding: artifactBinding, location:artifactLocation, index:1, active:true, isDefault:true)
identityProvider.addToArtifactResolutionServices(soapArtifact)

if(!attributeAuthority.protocolSupportEnumerations.contains(saml1Namespace))
	attributeAuthority.addToProtocolSupportEnumerations(saml1Namespace)
	
if(!attributeAuthority.protocolSupportEnumerations.contains(shibNamespace))
	attributeAuthority.addToProtocolSupportEnumerations(shibNamespace)

def attributeServiceBinding = SamlURI.findByUri(SamlConstants.soap1)
def attributeServiceLocation = new UrlURI(uri: "$host:8443/idp/profile/SAML1/SOAP/AttributeQuery")
soapAttributeService = new AttributeService(descriptor:attributeAuthority, approved: true, binding: attributeServiceBinding, location:attributeServiceLocation, active:true)
attributeAuthority.addToAttributeServices(soapAttributeService)
  
if(commit) {
  println "Commiting new SAML 1 endpoints to $identityProvider"
  if(!identityProvider.save()) {
    println "Unable to save $identityProvider"
    identityProvider.errors.each { print it }
  }
} else {
  println "Non commit - would have added new SAML 1 endpoints to $identityProvider"
}