import grails.plugins.nimble.core.*
import fedreg.core.*

/*
Perform initial population of Organization and IDP for FR deployers who
are not moving across from an existing Resource Registry instance or who are developing locally and wish to start from a clean platform.

Expects that samlBase.groovy has already been executed (and aafAttributePopulation.groovy or equivalent if relevant) as part of boostrap

Requires that a pre existing trsut relationship exists between the IDP and SP protecting Federation Registry (or you're using local development mode)

You should provide the hostname of your existing IDP below and ensure that the generated entityID will match what your IDP will assert during authentication.

When complete and you're able to login you should use the web interface to add certificates, attribute support and NameID support as well as administrators for the IDP.
Following this you should create a service provider using the web interface to represent the service provider protecting this FR instance.

Once these stages are complete FR should be generating minimal but valid metadata to start expanding your federation with. We recommend signing the generated Metadata
document with the Internet2 XMLSec tool before distributing to remote endpoints.

Bradley Beddoes
14/12/10
*/

roleService = ctx.getBean('roleService')
permissionService = ctx.getBean('permissionService')

def lang = 'en'

def federationName = 'aaf.edu.au'

def otName = "university"
def otDisplayName = "Australian Universities"
def otDescription = "Organization Type that is associated with all Australian Universities"

def orgName = 'dropbear.edu.au'
def orgDisplayName = 'Dropbear University'
def orgURL = 'http://www.dropbear.edu.au'

def contactName = 'Mick'
def contactSurname = 'Dundee'
def contactEmail = 'thatsaknife@dropbear.edu.au'

def hostname = 'idp.dropbear.edu.au'
def scope = 'dropbear.edu.au'
def displayName = 'Drop Bear IDP'
def description = 'Identity Provider for Drop Bear university'

//-----------
// Shouldn't be any need for hacking after this point for most usage scenarios
//-----------

// Default PING monitor
def mt = new MonitorType(name:'ping', description:'Ping check of associated endpoint to ensure availability')
mt.save()​​​​​​​​​​​​​​​​​​​​​​​​​​​​

// Entities Descriptor
def eds = new EntitiesDescriptor(name:federationName)
def savedEDS = eds.save()
if(!savedEDS) {
	eds.errors.each { println it }
	return
}

// Organization Type
def ot = new OrganizationType(name:otName, displayName:otDisplayName, description:otDescription)
def savedOT = ot.save()
if(!savedOT) {
	ot.errors.each { println it }
	return	
}

// Organization
def organization = new Organization(approved:true, active:true, name:orgName, displayName:orgDisplayName, url: new UrlURI(uri:orgURL), primary:savedOT, lang:lang)
def savedOrg = organization.save()
if(!savedOrg) {
	organization.errors.each { println it }
	return	
}

// Organization administrative role and overall permission
def oRole = roleService.createRole("organization-${savedOrg.id}-administrators", "Global administrators for $savedOrg", false)

def oPermission = new LevelPermission()
oPermission.populate("organization", "${savedOrg.id}", "*", null, null, null)
oPermission.managed = false
permissionService.createPermission(oPermission, oRole)

// Contact
def contact = new Contact(givenName: contactName, surname: contactSurname, email: new MailURI(uri:contactEmail))
contact.organization = savedOrg
def savedContact = contact.save()
if(!savedContact) {
	contact.errors.each { println it }
	return	
}

// Entity Descriptor
def entityDescriptor = new EntityDescriptor(approved:true, active: true, entityID:"https://$hostname/idp/shibboleth", organization: savedOrg)
def entContactPerson = new ContactPerson(contact:savedContact, type:ContactType.findByName('administrative'))
entityDescriptor.addToContacts(entContactPerson)

// IDP
def saml2Namespace = SamlURI.findByUri('urn:oasis:names:tc:SAML:2.0:protocol')
def identityProvider = new IDPSSODescriptor(approved:true, active:true, displayName:displayName , description:description, scope:scope, organization: savedOrg)
identityProvider.addToProtocolSupportEnumerations(saml2Namespace)

def idpContactPerson = new ContactPerson(contact:savedContact, type:ContactType.findByName('administrative'))
identityProvider.addToContacts(idpContactPerson)

def postBinding = SamlURI.findByUri(SamlConstants.httpPost)
def postLocation = new UrlURI(uri:"https://$hostname/idp/profile/SAML2/POST/SSO")
def httpPost = new SingleSignOnService(approved: true, binding: postBinding, location:postLocation, active:true)
identityProvider.addToSingleSignOnServices(httpPost)

def redirectBinding = SamlURI.findByUri(SamlConstants.httpRedirect)
def redirectLocation = new UrlURI(uri:"https://$hostname/idp/profile/SAML2/Redirect/SSO")
def httpRedirect = new SingleSignOnService(approved: true, binding: redirectBinding, location:redirectLocation, active:true)
identityProvider.addToSingleSignOnServices(httpRedirect)

def artifactBinding = SamlURI.findByUri(SamlConstants.soap)
def artifactLocation = new UrlURI(uri:"https://$hostname/idp/profile/SAML2/SOAP/ArtifactResolution")
def soapArtifact = new ArtifactResolutionService(approved: true, binding: artifactBinding, location:artifactLocation, active:true, isDefault:true)
identityProvider.addToArtifactResolutionServices(soapArtifact)

attributeAuthority = new AttributeAuthorityDescriptor(approved:true, active:true, displayName:displayName, description:description, scope: scope, collaborator: identityProvider, organization:organization)
attributeAuthority.addToProtocolSupportEnumerations(saml2Namespace)
identityProvider.collaborator = attributeAuthority
  
def attributeServiceBinding = SamlURI.findByUri('urn:oasis:names:tc:SAML:2.0:bindings:SOAP')
def attributeServiceLocation = new UrlURI(uri:"https://$hostname/idp/profile/SAML2/SOAP/AttributeQuery")
soapAttributeService = new AttributeService(approved: true, binding: attributeServiceBinding, location:attributeServiceLocation, active:true)
attributeAuthority.addToAttributeServices(soapAttributeService)

identityProvider.entityDescriptor = entityDescriptor
entityDescriptor.addToIdpDescriptors(identityProvider)

attributeAuthority.entityDescriptor = entityDescriptor
entityDescriptor.addToAttributeAuthorityDescriptors(attributeAuthority)

def savedED = entityDescriptor.save()
if(!savedED) {
	entityDescriptor.errors.each {println it}
	return
}

println identityProvider

// Create ED access control role
def edRole = roleService.createRole("descriptor-${savedED.id}-administrators", "Global administrators for ${savedED}", false)

LevelPermission edPermission = new LevelPermission()
edPermission.populate("descriptor", "${savedED.id}", "*", null, null, null)
edPermission.managed = false
permissionService.createPermission(edPermission, edRole)

// Create IDP access control role
def idpRole = roleService.createRole("descriptor-${identityProvider.id}-administrators", "Global administrators for $identityProvider", false)

// In our model the IDP role has permissions to edit the IDP and the AA
// Manage IDP
def idpPermission = new LevelPermission()
idpPermission.populate("descriptor", "${identityProvider.id}", "*", null, null, null)
idpPermission.managed = false
permissionService.createPermission(idpPermission, idpRole)

// Manage collaborating AA
def aaPermission = new LevelPermission()       
aaPermission.populate("descriptor", "${identityProvider.collaborator.id}", "*", null, null, null)
aaPermission.managed = false
permissionService.createPermission(aaPermission, idpRole)


​


​
​