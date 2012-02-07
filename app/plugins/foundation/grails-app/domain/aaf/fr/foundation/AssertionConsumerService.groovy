package aaf.fr.foundation

/**
 * @author Bradley Beddoes
 */
class AssertionConsumerService extends IndexedEndpoint  {
	static auditable = true

 	static belongsTo = [descriptor: SPSSODescriptor]

	public String toString() {	"assertionconsumerservice:[id:$id, location: $location]" }
	
	public boolean functioning() {
		( active && approved && descriptor.functioning() )
	}

}