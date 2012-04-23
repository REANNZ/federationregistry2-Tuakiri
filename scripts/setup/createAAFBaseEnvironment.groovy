import aaf.fr.foundation.*

roleService = ctx.getBean('roleService')
permissionService = ctx.getBean('permissionService')

def lang = 'en'
def federationName = 'aaf.edu.au'
def federationOrgTypes = [
  [name:'university', displayName:'university', description:'Australian University', discoverServiceCategory:'true'],
  [name:'hospital', displayName:'hospital', description:'Hospital', discoverServiceCategory:'true'],
  [name:'library', displayName:'library', description:'Library', discoverServiceCategory:'true'],
  [name:'vho', displayName:'vho', description:'Virtual Home Organization', discoverServiceCategory:'true'],
  [name:'others', displayName:'others', description:'Others', discoverServiceCategory:'true'],
  [name:'eresearch', displayName:'eresearch', description:'eResearch Organisations', discoverServiceCategory:'true'],
  [name:'nzuniversity', displayName:'nzuniversity', description:'New Zealand University', discoverServiceCategory:'true'],
  [name:'Standalone AA', displayName:'Standalone AA', description:'Standalone AA only', discoverServiceCategory:'false']
] as List

def federationMonitors = [
  [name:'Ping', description:'Used to test the reach-ability of a host on the network and to measure the round-trip time for messages sent from the monitoring server to a destination server.'],
  [name:'HTTP', description:'Tests the HTTP service on the specified host. It can test normal (http) and secure (https) servers.'],
  [name:'SSL Certificate', description:'Tests the number of days a certificate has to be valid.'],
  [name:'Time Sync', description:'Test the time on the server is in-sync with time from a known and trusted time source.'],
  [name:'Shib - Basic', description:'Tests the status of the shibboleth server (IdP and SP) using the status end point.'],
  [name:'Shib - Advanced', description:'Tests the status of the shibboleth server and checks the version numbers of the software components reported in the status response.'],
  [name:'Port Security Check', description:'Scans for open ports and reports any that have not in the list of ports expected to be open.']
] as List

// Entities Descriptor
def eds = new EntitiesDescriptor(name:federationName)
def savedEDS = eds.save()
if(!savedEDS) {
	eds.errors.each { println it }
	return
}

// AAF supported organisation types
federationOrgTypes.each {
  def ot = new OrganizationType(it)
  savedOT = ot.save()
  if(!savedOT) {
    ot.errors.each { println it }
    return  
  }
}

// AAF supported attributes
def coreCategory = new AttributeCategory(name:'Core')
coreCategory.save()
def optionalCategory = new AttributeCategory(name:'Optional')
optionalCategory.save()

def attrUri = SamlURI.findWhere(uri:'urn:oasis:names:tc:SAML:2.0:attrname-format:uri')

def o = new AttributeBase(oid:'2.5.4.10', nameFormat: attrUri, legacyName:'urn:mace:dir:attribute-def:o'  , name:'organizationName', description:'Standard name of the top-level organization (institution) with which the user is associated.', category:coreCategory, specificationRequired:false).save()
def ou =  new AttributeBase(oid:'2.5.4.11', nameFormat: attrUri, legacyName:'urn:mace:dir:attribute-def:ou', name:'organizationalUnit', description:'Organizational Unit currently used for faculty membership of staff', category:optionalCategory, specificationRequired:false).save()
def postalAddress =  new AttributeBase(oid:'2.5.4.16', nameFormat: attrUri, legacyName:'urn:mace:dir:attribute-def:postalAddress', name:'postalAddress', description:'Business postal address: Campus or office address', category:optionalCategory, specificationRequired:false).save()

def auEduPersonSharedToken =  new AttributeBase(oid:'1.3.6.1.4.1.27856.1.2.5', nameFormat: attrUri, legacyName:'urn:oid:1.3.6.1.4.1.27856.1.2.5', name:'auEduPersonSharedToken', description:'A unique identifier enabling federation spanning services such as Grid and Repositories', category:coreCategory, specificationRequired:false).save()
def auEduPersonAffiliation =  new AttributeBase(oid:'1.3.6.1.4.1.27856.1.2.1', nameFormat: attrUri, legacyName:'urn:oid:1.3.6.1.4.1.27856.1.2.1', name:'auEduPersonAffiliation', description:'Specifies a persons relationship to the institution in broad categories but with a finer-grained set of permissible values than eduPersonAffiliation.', category:optionalCategory, specificationRequired:false).save()
def auEduPersonLegalName =  new AttributeBase(oid:'1.3.6.1.4.1.27856.1.2.2', nameFormat: attrUri, legacyName:'urn:oid:1.3.6.1.4.1.27856.1.2.2', name:'auEduPersonLegalName', description:'The users legal name, as per their passport, birth certificate, or other legal document', category:optionalCategory, specificationRequired:false).save()

def eduPersonTargetedID =  new AttributeBase(oid:'1.3.6.1.4.1.5923.1.1.1.10', nameFormat: attrUri, legacyName:'urn:mace:dir:attribute-def:eduPersonTargetedID', name:'eduPersonTargetedID', description:'A persistent, non-reassigned, privacy-preserving identifier for a principal shared between a pair of coordinating entities', category:coreCategory, specificationRequired:false).save()
def eduPersonAffiliation =  new AttributeBase(oid:'1.3.6.1.4.1.5923.1.1.1.1', nameFormat: attrUri, legacyName:'urn:mace:dir:attribute-def:eduPersonAffiliation', name:'eduPersonAffiliation', description:'Specifies the persons relationship(s) to the institution in broad categories such as student, faculty, staff, alum, etc.', category:coreCategory, specificationRequired:false).save()
def eduPersonEntitlement =  new AttributeBase(oid:'1.3.6.1.4.1.5923.1.1.1.7', nameFormat: attrUri, legacyName:'urn:mace:dir:attribute-def:eduPersonEntitlement', name:'eduPersonEntitlement', description:'Member of: URI (either URL or URN) that indicates a set of rights to specific resources based on an agreement across the releavant community', category:coreCategory, specificationRequired:true).save()
def eduPersonPrincipalName =  new AttributeBase(oid:'1.3.6.1.4.1.5923.1.1.1.6', nameFormat: attrUri, legacyName:'urn:mace:dir:attribute-def:eduPersonPrincipalName', name:'eduPersonPrincipalName', description:'eduPerson per Internet2 and EDUCAUSE', category:optionalCategory, specificationRequired:false).save()
def eduPersonAssurance =  new AttributeBase(oid:'1.3.6.1.4.1.5923.1.1.1.11', nameFormat: attrUri, legacyName:'urn:mace:dir:attribute-def:eduPersonAssurance', name:'eduPersonAssurance', description:'This attribute represents identity assurance profiles (IAPs), which are the set of standards that are met by an identity assertion, based on the Identity Providers identity management processes, type of auth credential used, binding strength,  etc.', category:coreCategory, specificationRequired:false).save()
def eduPersonScopedAffiliation =  new AttributeBase(oid:'1.3.6.1.4.1.5923.1.1.1.9', nameFormat: attrUri, legacyName:'urn:mace:dir:attribute-def:eduPersonScopedAffiliation', name:'eduPersonScopedAffiliation', description:'This attribute enables an organisation to assert its relationship with the user.', category:coreCategory, specificationRequired:false).save()
def eduPersonPrimaryAffiliation =  new AttributeBase(oid:'1.3.6.1.4.1.5923.1.1.1.5', nameFormat: attrUri, legacyName:'urn:mace:dir:attribute-def:eduPersonPrimaryAffiliation', name:'eduPersonPrimaryAffiliation', description:'Specifies the persons PRIMARY relationship to the institution in broad categories such as student, faculty, staff, alum, etc.', category:optionalCategory, specificationRequired:false).save()

def cn =  new AttributeBase(oid:'2.5.4.3', nameFormat: attrUri, legacyName:'urn:mace:dir:attribute-def:cn', name:'commonName', description:'An individuals common name, typically their full name. This attribute should not be used in transactions where it is desirable to maintain user anonymity.', category:coreCategory, specificationRequired:false).save()
def sn =  new AttributeBase(oid:'2.5.4.4', nameFormat: attrUri, legacyName:'urn:mace:dir:attribute-def:sn', name:'surname', description:'Surname or family name', category:optionalCategory, specificationRequired:false).save()
def givenName =  new AttributeBase(oid:'2.5.4.42', nameFormat: attrUri, legacyName:'urn:mace:dir:attribute-def:givenName', name:'givenName', description:'Given name of a person', category:optionalCategory, specificationRequired:false).save()
def displayName =  new AttributeBase(oid:'2.16.840.1.113730.3.1.241', nameFormat: attrUri, legacyName:'urn:mace:dir:attribute-def:displayName', name:'displayName', description:'Preferred name of a person to be used when displaying entries. This attribute should not be used in transactions where it is desirable to maintain user anonymity.', category:coreCategory, specificationRequired:false).save()

def mail =  new AttributeBase(oid:'0.9.2342.19200300.100.1.3', nameFormat: attrUri, legacyName:'urn:mace:dir:attribute-def:mail', name:'email', description:'Preferred address for e-mail to be sent to this person', category:coreCategory, specificationRequired:false).save()
def telephoneNumber =  new AttributeBase(oid:'2.5.4.20', nameFormat: attrUri, legacyName:'urn:mace:dir:attribute-def:telephoneNumber', name:'telephoneNumber', description:'Office or campus phone number of the individual', category:optionalCategory, specificationRequired:false).save()
def mobile =  new AttributeBase(oid:'0.9.2342.19200300.100.1.41', nameFormat: attrUri, legacyName:'urn:mace:dir:attribute-def:mobile', name:'mobileNumber', description:'Mobile phone number', category:optionalCategory, specificationRequired:false).save()

def homeOrganization =  new AttributeBase(oid:'1.3.6.1.4.1.25178.1.2.9', nameFormat: attrUri, legacyName:'urn:oid:1.3.6.1.4.1.25178.1.2.9', name:'homeOrganization', description:'Users Home Organization', category:optionalCategory, specificationRequired:false).save()
def homeOrganizationType =  new AttributeBase(oid:'1.3.6.1.4.1.25178.1.2.10', nameFormat: attrUri, legacyName:'urn:oid:1.3.6.1.4.1.25178.1.2.10', name:'homeOrganizationType', description:'Type of Organization the user belongs too', category:optionalCategory, specificationRequired:false).save()

// Default Service Category
def sc = new ServiceCategory(name:'General', description:'Default category that suits majority of federation provided services')
sc.save()​

// AAF supported Monitors
federationMonitors.each {
  def mon = new MonitorType(it)
  saveMon = mon.save()
  if(!saveMon) {
    mon.errors.each { println it }
    return  
  }
}

println "completed createAAFBaseEnvironment.groovy"



​


​
​