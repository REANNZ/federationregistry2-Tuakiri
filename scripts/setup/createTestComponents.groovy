import aaf.fr.foundation.*

/*
Bootstrap - Optional

This will create an example organisation, idp and sp in the FR database and enable the development/example accounts to login.

For production deployments this script serves as a useful basis to create your initial environment.
*/

// Create an initial Organisation and Identity Provider suitable for use with the developement login functionality
cryptoService = ctx.getBean('cryptoService')

// Contact Creation
def ct = 'technical'
def contact = new Contact(givenName: 'Fred', surname: 'Bloggs', email: 'fredbloggs@one.edu.au')
contact.save(flush:true)
if(contact.hasErrors()) {
  contact.errors.each { println it }
}

// Organisation Creation
def organization = new Organization(approved:true, active:true, name:'one.edu.au', displayName:'Organisation One', lang: 'en', url: 'http://www.one.edu.au', primary:OrganizationType.get(1))

def contactPerson = new ContactPerson(contact:contact, type:ContactType.findByName(ct))
organization.addToContacts(contactPerson) 

if(!organization.validate() || contact.hasErrors()) {
  organization?.errors.each { println it }
  return [ false, organization, contact ]
}

// Force flush as we need identifiers
if(!organization.save(flush:true)) {
  organization?.errors.each { println it }
  throw new ErronousStateException("Unable to save when creating $organization")
}

if(contact.organization == null)
  contact.organization = organization

// Force flush as we need identifiers
if(!contact.save(flush:true)) {
  contact?.errors.each { println it }
  throw new ErronousStateException("Unable to save when creating ${contact}")
}

def saml2Namespace = SamlURI.findByUri('urn:oasis:names:tc:SAML:2.0:protocol')
def nameid = SamlURI.findWhere(uri:'urn:oasis:names:tc:SAML:2.0:nameid-format:transient')

// Identity Provider Creation
def certData = """-----BEGIN CERTIFICATE-----
MIIDYzCCAkugAwIBAgIJANl+C/wPoWXCMA0GCSqGSIb3DQEBBQUAMEgxCzAJBgNV
BAYTAkFVMRMwEQYDVQQIDApTb21lLVN0YXRlMQswCQYDVQQKDAJGUjEXMBUGA1UE
AwwOaWRwLm9uZS5lZHUuYXUwHhcNMTEwMTI3MDQwODAxWhcNMTIwMTI3MDQwODAx
WjBIMQswCQYDVQQGEwJBVTETMBEGA1UECAwKU29tZS1TdGF0ZTELMAkGA1UECgwC
RlIxFzAVBgNVBAMMDmlkcC5vbmUuZWR1LmF1MIIBIjANBgkqhkiG9w0BAQEFAAOC
AQ8AMIIBCgKCAQEAnWq4BTqKflXf+mDYHgMiIGS6XsjjPRLWLWAXqIJ24TzCIt7x
sI0Ho/c95ambwSnKRL7bLc0u2ZL0GTZ4dizRDdpu3b1HxK9opNJ1owGHibqfhWKr
b4IkAcnYIEjrlSzeYIWNBpqjKOG8EyO8TjxQFkFLiSPE2UxXZzu6jC5Ql9qkWtWD
Z9M1d0ecDOIaz15O/nUFqUa9Y+mX3bG1eMFAx5v1x+neI/HNRA/4PdfbPZLVB2Ah
viUNHc3n11uhfLyGUh7TNmGKOtCe65CyWEfDcXpzhWYAxNcux2/hqOIUgN6u2iKb
Pc5M4KGjDJZgsHr2ETkPJ26nCIIi+Xpjol93XQIDAQABo1AwTjAdBgNVHQ4EFgQU
6GEI0/urtOKi28w/yrY10xUPDjkwHwYDVR0jBBgwFoAU6GEI0/urtOKi28w/yrY1
0xUPDjkwDAYDVR0TBAUwAwEB/zANBgkqhkiG9w0BAQUFAAOCAQEATfVC6KUcZ+Ab
W++I8qPAuwkeLGS7qXbSR0QdzqOR8G+abyPId8lc3Z/bOCL7Gbkcx50W6MFD3RAT
vfvJCQ32nf8aNgW+lF6HZ6/JWKkLrXwoBrAOCYQHooFbrHubug241RZNnMA//+pC
94PhB5C1zfW6/UbpXb02OA8VTjcTQUe6yqfm/bqJEQMxGwF6qrr6H+BYwce+9fUM
Vk8bqShxnYJ6cMM0oduSk8JOQYJ5h1IM4NVvn66Sg6xsLkLWeDeuyhGineBL2XMQ
rqAMIEkOFmoE7s06RJeOwO/BoGKvx1gKO/8cMQqBd7rRth/sLPpX5Uhra/w2vJBG
xHYeaB80rA==
-----END CERTIFICATE-----"""

def entityDescriptor = new EntityDescriptor(approved:true, active:true, entityID: 'https://idp.one.edu.au/idp/shibboleth', organization: organization)

def identityProvider = new IDPSSODescriptor(approved:true, active:true, displayName: 'One University', description: 'The IdP for One University staff and students', scope: 'one.edu.au', organization: organization)
identityProvider.addToProtocolSupportEnumerations(saml2Namespace)
identityProvider.addToNameIDFormats(nameid)

def attr = AttributeBase.findWhere(name:'eduPersonTargetedID')
identityProvider.addToAttributes(new Attribute(base:attr))

def idpContactPerson = new ContactPerson(contact:contact, type:ContactType.findByName(ct))
identityProvider.addToContacts(idpContactPerson)  

def postBinding = SamlURI.findByUri(SamlConstants.httpPost)
def postLocation = 'https://idp.one.edu.au/idp/profile/SAML2/POST/SSO'
def httpPost = new SingleSignOnService(approved: true, binding: postBinding, location:postLocation, active:true)
identityProvider.addToSingleSignOnServices(httpPost)

def redirectBinding = SamlURI.findByUri(SamlConstants.httpRedirect)
def redirectLocation = 'https://idp.one.edu.au/idp/profile/SAML2/Redirect/SSO'
def httpRedirect = new SingleSignOnService(approved: true, binding: redirectBinding, location:redirectLocation, active:true)
identityProvider.addToSingleSignOnServices(httpRedirect)

def artifactBinding = SamlURI.findByUri(SamlConstants.soap)
def artifactLocation = 'https://idp.one.edu.au:8443/idp/profile/SAML2/SOAP/ArtifactResolution'
def soapArtifact = new ArtifactResolutionService(approved: true, binding: artifactBinding, location:artifactLocation, index:2, active:true, isDefault:true)
identityProvider.addToArtifactResolutionServices(soapArtifact)

def cert = cryptoService.createCertificate(certData)
cryptoService.validateCertificate(cert)
def keyInfo = new KeyInfo(certificate: cert)
def keyDescriptor = new KeyDescriptor(keyInfo:keyInfo, keyType:KeyTypes.signing, encryptionMethod:null)
keyDescriptor.roleDescriptor = identityProvider
identityProvider.addToKeyDescriptors(keyDescriptor)

def attributeAuthority, soapAttributeService
attributeAuthority = new AttributeAuthorityDescriptor(approved:true, active:true, displayName: 'One University', description: 'The IdP for One University staff and students', scope: 'one.edu.au', organization:organization)
attributeAuthority.addToProtocolSupportEnumerations(saml2Namespace)

def attributeServiceBinding = SamlURI.findByUri('urn:oasis:names:tc:SAML:2.0:bindings:SOAP')
def attributeServiceLocation = 'https://idp.one.edu.au:8443/idp/profile/SAML2/SOAP/AttributeQuery'
soapAttributeService = new AttributeService(approved: true, binding: attributeServiceBinding, location:attributeServiceLocation, active:true)
attributeAuthority.addToAttributeServices(soapAttributeService)

entityDescriptor.addToIdpDescriptors(identityProvider)
entityDescriptor.addToAttributeAuthorityDescriptors(attributeAuthority)

identityProvider.validate()
attributeAuthority.validate()

if(!entityDescriptor.validate() || contact.hasErrors()) {
  entityDescriptor.errors.each {println it}
  throw new ErronousStateException("Unable to validate when creating EntityDescriptor")
}
    
if(!entityDescriptor.save(flush:true)) {
  entityDescriptor.errors.each {println it}
  throw new ErronousStateException("Unable to save when creating EntityDescriptor")
}

identityProvider.collaborator = attributeAuthority
attributeAuthority.collaborator = identityProvider

if(!entityDescriptor.save()) {
  entityDescriptor.errors.each {println it}
  throw new ErronousStateException("Unable to save when creating EntityDescriptor having linked IdP and AA")
}

// Service Provider Creation
entityDescriptor = null
certData = """-----BEGIN CERTIFICATE-----
MIIDYTCCAkmgAwIBAgIJAMZruy388SCHMA0GCSqGSIb3DQEBBQUAMEcxCzAJBgNV
BAYTAkFVMRMwEQYDVQQIDApTb21lLVN0YXRlMQswCQYDVQQKDAJGUjEWMBQGA1UE
AwwNc3Aub25lLmVkdS5hdTAeFw0xMTAxMjcwNDA5MDhaFw0xMjAxMjcwNDA5MDha
MEcxCzAJBgNVBAYTAkFVMRMwEQYDVQQIDApTb21lLVN0YXRlMQswCQYDVQQKDAJG
UjEWMBQGA1UEAwwNc3Aub25lLmVkdS5hdTCCASIwDQYJKoZIhvcNAQEBBQADggEP
ADCCAQoCggEBAO1Zg1w7L5lyRY3gBBkY3i6fGBYU9Tk8Lkk6/oalijhiOCoeMu5L
NeMeegwf+9K3PzfFf0iX6IaZkYAk1g1TEe9LQ7xDWh1n1XD/OOmz3kAkn/g+ewXa
9+mOWvB6n6c5hBRD5FQ9HeMLUiRFeFUD+IO9VSWHlj1Emaeg9NIfgz6mP6NBVp7M
oEP6cDog3YikTTDJRhOJFJX42B/Fh2x7QNrUXAvAfjaqfPecSPQ9eYHRB5fwq/yY
IKAWjeA4tK1XWvi/+3ok0RtgAfAEB/OdMwXHwxzbx71pI98Xb3lPdrq1kp8fhPic
DaQWKKtpxVd4/3QyEOHz1Qtv8dEiEquiZ0UCAwEAAaNQME4wHQYDVR0OBBYEFFrA
1krjBPJbChZCPKuMDvekGNoUMB8GA1UdIwQYMBaAFFrA1krjBPJbChZCPKuMDvek
GNoUMAwGA1UdEwQFMAMBAf8wDQYJKoZIhvcNAQEFBQADggEBAJaZ7b4dwYBsXvUw
mW8ypW8koO+gLvb034uZrSpsnpG29yIdCZkE5iZYHM2y7/Pf/9/hQCk3vAvfF+7P
lvY1g0uCr1/BJKl3Sc5UQKb8zXDuAKRRxX93k9wbPECLjhjd8RlXAjD27vftE7xo
aK7cd66zIIzKIrVgDW0yEjzqHv1YT4WyROk6OGvSGBgZ/M+xL6bu1Y4z96XNsdk1
/kiDU3yknOMNBpf2KHyNqVP/oCfB3+97kgtgUXDPkj/ENeycR+6oFrNoXlfoYIHJ
E15T10XASSbUa8JnXXFqlNSJoUzyz1FDyyxQT9YF9uSXL6PJeyeHMI3/hCUxfuJS
khkY/9I=
-----END CERTIFICATE-----"""

entityDescriptor = new EntityDescriptor(approved:true, active:true, entityID: 'https://sp.one.edu.au/shibboleth', organization: organization)
def serviceProvider = new SPSSODescriptor(approved:true, active:true, displayName: 'Test SP', description: 'Test SP for One University', organization: organization, authnRequestsSigned:false, wantAssertionsSigned:false)
serviceProvider.addToProtocolSupportEnumerations(saml2Namespace)
serviceProvider.addToNameIDFormats(nameid)  

def acs = new AttributeConsumingService(approved:true, lang:'en')
acs.addToServiceNames('Test SP')
serviceProvider.addToAttributeConsumingServices(acs)

def spContactPerson = new ContactPerson(contact:contact, type:ContactType.findByName(ct))
serviceProvider.addToContacts(spContactPerson)

postBinding = SamlURI.findByUri(SamlConstants.httpPost)
postLocation = 'https://sp.one.edu.au/Shibboleth.sso/SAML2/POST'
def httpPostACS = new AssertionConsumerService(approved: true, binding: postBinding, location:postLocation, 
  index:1, active:true, isDefault:true)
serviceProvider.addToAssertionConsumerServices(httpPostACS)
httpPostACS.validate()

artifactBinding = SamlURI.findByUri(SamlConstants.httpArtifact)
artifactLocation = 'https://sp.one.edu.au/Shibboleth.sso/SAML2/Artifact'
def httpArtifactACS = new AssertionConsumerService(approved: true, binding: artifactBinding, location:artifactLocation, 
  index:3, active:true, isDefault:false)
serviceProvider.addToAssertionConsumerServices(httpArtifactACS)

def drsBinding = SamlURI.findByUri(SamlConstants.drs)
def drsLocation = 'https://sp.one.edu.au/Shibboleth.sso/DS'
discoveryResponseService = new DiscoveryResponseService(approved: true, binding: drsBinding, location:drsLocation, active:true, isDefault:true)
serviceProvider.addToDiscoveryResponseServices(discoveryResponseService)

cert = cryptoService.createCertificate(certData)
cryptoService.validateCertificate(cert)
keyInfo = new KeyInfo(certificate: cert)
keyDescriptor = new KeyDescriptor(keyInfo:keyInfo, keyType:KeyTypes.signing, encryptionMethod:null)
keyDescriptor.roleDescriptor = serviceProvider
serviceProvider.addToKeyDescriptors(keyDescriptor)

def certEnc = cryptoService.createCertificate(certData)
cryptoService.validateCertificate(certEnc)
def keyInfoEnc = new KeyInfo(certificate:certEnc)
def keyDescriptorEnc = new KeyDescriptor(keyInfo:keyInfoEnc, keyType:KeyTypes.encryption, encryptionMethod:null)
keyDescriptorEnc.roleDescriptor = serviceProvider
serviceProvider.addToKeyDescriptors(keyDescriptorEnc)

def serviceDescription = new ServiceDescription(connectURL: 'https://sp.one.edu.au', descriptor: serviceProvider)
serviceProvider.serviceDescription = serviceDescription

entityDescriptor.addToSpDescriptors(serviceProvider)
serviceProvider.validate()

if(!entityDescriptor.validate() || contact.hasErrors()) {
  entityDescriptor.errors.each {print it}
  throw new ErronousStateException("Unable to validating when creating SP EntityDescriptor")
}

// Force flush as we need identifiers
if(!entityDescriptor.save(flush:true)) {
  entityDescriptor.errors.each {print it}
  throw new ErronousStateException("Unable to save when creating SP EntityDescriptor")
}

println "Completed creation of testing Organisation, Identity Provider and Service Provider"
true
