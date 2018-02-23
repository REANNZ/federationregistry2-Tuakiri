package aaf.fr.foundation

/**
 * @author Bradley Beddoes
 */
class SingleSignOnService extends Endpoint {
	static auditable = true

	static belongsTo = [descriptor: IDPSSODescriptor]

	public String toString() {	"singlesignonservice:[id:$id, location: $location]" }
	
	public boolean functioning() {
		( active && approved && descriptor.functioning() )
	}

	public boolean selfFunctioning() {
		( active && approved )
	}

}
