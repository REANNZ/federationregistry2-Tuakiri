package aaf.fr.foundation

/**
 * @author Bradley Beddoes
 */
class DiscoveryResponseService extends IndexedEndpoint  {
	static auditable = true

	static belongsTo = [descriptor: SSODescriptor]

	public String toString() {	"discoveryresponseservice:[id:$id, location: $location]" }
	
	public boolean functioning() {
		( active && approved && descriptor.functioning() )
	}

}
