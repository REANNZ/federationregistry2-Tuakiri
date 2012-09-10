package aaf.fr.foundation

/**
 * @author Bradley Beddoes
 */
class SamlURI  {
	static auditable = true
	
	SamlURIType type
  String uri
  String description
	
	public String toString() {	"samluri:[id:$id, uri: $uri]" }

  static constraints = {
    description(nullable: true)
  }
}

public enum SamlURIType {
	AttributeNameFormat, AttributeProfile, NameIdentifierFormat, ProtocolBinding, ProtocolSupport
}
