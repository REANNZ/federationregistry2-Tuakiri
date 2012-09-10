package aaf.fr.foundation

/**
 * @author Bradley Beddoes
 */
class AuthzService extends Endpoint  {
	static auditable = true

	static belongsTo = [ descriptor:PDPDescriptor ]

	public String toString() {	"authzservice:[id:$id, location: $location]" }
	
	public boolean functioning() {
		( active && approved && descriptor.functioning() )
	}
}