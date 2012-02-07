
import aaf.fr.foundation.AttributeCategory
import aaf.fr.foundation.AttributeBase
import aaf.fr.foundation.SamlURI

/*
 Initial attribute definition and population for Federation Registry
*/

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

println "completed aafAttributePopulation.groovy"	