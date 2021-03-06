package aaf.fr.foundation

/**
 * @author Bradley Beddoes
 */
class SingleLogoutService extends Endpoint  {
	static auditable = true

	static belongsTo = [descriptor: SSODescriptor]
	
	public String toString() {	"singlelogoutservice:[id:$id, location: $location]" }
	
	public boolean functioning() {
		( active && approved && descriptor.functioning() )
	}

}
