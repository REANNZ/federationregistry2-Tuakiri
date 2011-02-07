// SAML Base (createSAMLBase.groovy)
// Overall SAML2 protocol support, all SAML 2.0 compliant RoleDescriptors need this.
def saml2Namespace = new SamlURI(type:SamlURIType.ProtocolSupport, uri:'urn:oasis:names:tc:SAML:2.0:protocol').save()
def saml1Namespace = new SamlURI(type:SamlURIType.ProtocolSupport, uri:'urn:oasis:names:tc:SAML:1.1:protocol').save()
def shibboleth1Namespace = new SamlURI(type:SamlURIType.ProtocolSupport, uri:'urn:mace:shibboleth:1.0').save()

// Bindings
def httpRedirect = new SamlURI(type:SamlURIType.ProtocolBinding, uri:'urn:oasis:names:tc:SAML:2.0:bindings:HTTP-Redirect', description:'').save()
def httpPost = new SamlURI(type:SamlURIType.ProtocolBinding, uri:'urn:oasis:names:tc:SAML:2.0:bindings:HTTP-POST', description:'').save()
def httpArtifact = new SamlURI(type:SamlURIType.ProtocolBinding, uri:'urn:oasis:names:tc:SAML:2.0:bindings:HTTP-Artifact', description:'').save()
def httpPostSimple = new SamlURI(type:SamlURIType.ProtocolBinding, uri:'urn:oasis:names:tc:SAML:2.0:bindings:HTTP-POST-SimpleSign', description:'').save()
def paos = new SamlURI(type:SamlURIType.ProtocolBinding, uri:'urn:oasis:names:tc:SAML:2.0:bindings:PAOS', description:'').save()
def soap = new SamlURI(type:SamlURIType.ProtocolBinding, uri:'urn:oasis:names:tc:SAML:2.0:bindings:SOAP', description:'').save()
def shibAuthn = new SamlURI(type:SamlURIType.ProtocolBinding, uri:'urn:mace:shibboleth:1.0:profiles:AuthnRequest', description:'').save()
def httpPost1 = new SamlURI(type:SamlURIType.ProtocolBinding, uri:'urn:oasis:names:tc:SAML:1.0:profiles:browser-post', description:'').save()
def httpArtifact1 = new SamlURI(type:SamlURIType.ProtocolBinding, uri:'urn:oasis:names:tc:SAML:1.0:profiles:artifact-01', description:'').save()
def soap1 = new SamlURI(type:SamlURIType.ProtocolBinding, uri:'urn:oasis:names:tc:SAML:1.0:bindings:SOAP-binding', description:'').save()
def drs = new SamlURI(type:SamlURIType.ProtocolBinding, uri:'urn:oasis:names:tc:SAML:profiles:SSO:idp-discovery-protocol', description:'').save()

// NameIDFormats
def unspec = new SamlURI(type:SamlURIType.NameIdentifierFormat, uri:'urn:oasis:names:tc:SAML:1.1:nameid-format:unspecified', description:'The interpretation of the content of the element is left to individual implementations.').save()
def email = new SamlURI(type:SamlURIType.NameIdentifierFormat, uri:'urn:oasis:names:tc:SAML:1.1:nameid-format:emailAddress', description:'Indicates that the content of the element is in the form of an email address, specifically addr-spec as defined in IETF RFC 2822').save()
def x509 = new SamlURI(type:SamlURIType.NameIdentifierFormat, uri:'urn:oasis:names:tc:SAML:1.1:nameid-format:X509SubjectName', description:'Indicates that the content of the element is in the form specified for the contents of the <ds:X509SubjectName> element in the XML Signature Recommendation [XMLSig].').save()
def windows = new SamlURI(type:SamlURIType.NameIdentifierFormat, uri:'urn:oasis:names:tc:SAML:1.1:nameid-format:WindowsDomainQualifiedName', description:'Indicates that the content of the element is a Windows domain qualified name. A Windows domain qualified user name is a string of the form "DomainName\\UserName"').save()
def kerberos = new SamlURI(type:SamlURIType.NameIdentifierFormat, uri:'urn:oasis:names:tc:SAML:2.0:nameid-format:kerberos', description:'Indicates that the content of the element is in the form of a Kerberos principal name using the format name[/instance]@REALM').save()
def entity = new SamlURI(type:SamlURIType.NameIdentifierFormat, uri:'urn:oasis:names:tc:SAML:2.0:nameid-format:entity', description:'Indicates that the content of the element is the identifier of an entity that provides SAML-based services or is a participant in SAML profiles.').save()
def pers = new SamlURI(type:SamlURIType.NameIdentifierFormat, uri:'urn:oasis:names:tc:SAML:2.0:nameid-format:persistent', description:'Indicates that the content of the element is a persistent opaque identifier for a principal that is specific to an identity provider and a service provider or affiliation of service providers.').save()
def trans = new SamlURI(type:SamlURIType.NameIdentifierFormat, uri:'urn:oasis:names:tc:SAML:2.0:nameid-format:transient', description:'Indicates that the content of the element is an identifier with transient semantics and SHOULD be treated as an opaque and temporary value by the relying party.').save()
def shibNameID = new SamlURI(type:SamlURIType.NameIdentifierFormat, uri:'urn:mace:shibboleth:1.0:nameIdentifier', description:'').save()

// AttributeNameFormats
def attrUnspec = new SamlURI(type:SamlURIType.AttributeNameFormat, uri:'urn:oasis:names:tc:SAML:2.0:attrname-format:unspecified', description:'').save()
def attrUri = new SamlURI(type:SamlURIType.AttributeNameFormat, uri:'urn:oasis:names:tc:SAML:2.0:attrname-format:uri', description:'').save()
def attrBasic = new SamlURI(type:SamlURIType.AttributeNameFormat, uri:'urn:oasis:names:tc:SAML:2.0:attrname-format:basic', description:'').save()

// Contact Types
def tech = new ContactType(name:'technical', displayName:'Technical', description: 'Technical contacts').save()
def mark = new ContactType(name:'marketing', displayName:'Marketing', description: 'Marketting contacts').save()
def bill = new ContactType(name:'billing', displayName:'Billing', description: 'Billing contacts').save()
def supp = new ContactType(name:'support', displayName:'Support', description: 'Support contacts').save()
def admin = new ContactType(name:'administrative', displayName:'Administrative', description: 'Administrative contacts').save()
def other = new ContactType(name:'other', displayName:'Other', description: 'Other contacts').save()
