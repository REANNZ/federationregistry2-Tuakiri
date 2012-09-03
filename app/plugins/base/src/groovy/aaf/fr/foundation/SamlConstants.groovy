package aaf.fr.foundation

/**
 * @author Bradley Beddoes
 */
class SamlConstants {

	static def samlNamespace = 'urn:oasis:names:tc:SAML:2.0:protocol'
	
	// Bindings
	static def httpRedirect = 'urn:oasis:names:tc:SAML:2.0:bindings:HTTP-Redirect'
	static def httpPost = 'urn:oasis:names:tc:SAML:2.0:bindings:HTTP-POST'
	static def httpArtifact = 'urn:oasis:names:tc:SAML:2.0:bindings:HTTP-Artifact'
	static def httpPostSimple = 'urn:oasis:names:tc:SAML:2.0:bindings:HTTP-POST-SimpleSign'
	static def paos = 'urn:oasis:names:tc:SAML:2.0:bindings:PAOS'
	static def soap = 'urn:oasis:names:tc:SAML:2.0:bindings:SOAP'
	static def shibAuthn = 'urn:mace:shibboleth:1.0:profiles:AuthnRequest'
	static def httpPost1 = 'urn:oasis:names:tc:SAML:1.0:profiles:browser-post'
	static def httpArtifact1 = 'urn:oasis:names:tc:SAML:1.0:profiles:artifact-01'
	static def soap1 = 'urn:oasis:names:tc:SAML:1.0:bindings:SOAP-binding'
	static def drs = 'urn:oasis:names:tc:SAML:profiles:SSO:idp-discovery-protocol'
	
	// NameIDFormats
	static def unspec = 'urn:oasis:names:tc:SAML:1.1:nameid-format:unspecified'
	static def email = 'urn:oasis:names:tc:SAML:1.1:nameid-format:emailAddress'
	static def x509 = 'urn:oasis:names:tc:SAML:1.1:nameid-format:X509SubjectName'
	static def windows = 'urn:oasis:names:tc:SAML:1.1:nameid-format:WindowsDomainQualifiedName'
	static def kerberos = 'urn:oasis:names:tc:SAML:2.0:nameid-format:kerberos'
	static def entity = 'urn:oasis:names:tc:SAML:2.0:nameid-format:entity'
	static def pers = 'urn:oasis:names:tc:SAML:2.0:nameid-format:persistent'
	static def trans = 'urn:oasis:names:tc:SAML:2.0:nameid-format:transient'
	static def shibNameID = 'urn:mace:shibboleth:1.0:nameIdentifier'
	
}