package aaf.fr.foundation

/**
 * @author Bradley Beddoes
 */
class NameIDMappingService extends Endpoint  {
	static auditable = true

	static belongsTo = [descriptor: IDPSSODescriptor]
	public String toString() {	"nameidmappingservice:[id:$id, location: $location]" }
	
	public boolean functioning() {
		( active && approved && descriptor.functioning() )
	}
}
