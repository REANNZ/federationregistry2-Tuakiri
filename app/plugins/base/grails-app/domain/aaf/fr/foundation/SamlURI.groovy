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

  def structureAsJson() {
    def json = new groovy.json.JsonBuilder()
    json {
      id id
      type type
      uri uri
      description description ?: ''
    }
  }

}

public enum SamlURIType {
	AttributeNameFormat, AttributeProfile, NameIdentifierFormat, ProtocolBinding, ProtocolSupport
}
