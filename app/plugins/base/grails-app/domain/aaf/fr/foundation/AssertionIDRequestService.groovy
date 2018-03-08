package aaf.fr.foundation

/**
 * @author Bradley Beddoes
 */
class AssertionIDRequestService extends Endpoint  {
	static auditable = true

	static belongsTo = [descriptor:RoleDescriptor]
	
	public String toString() {	"assertionidrequestservice:[id:$id, location: $location]" }
	
	public boolean functioning() {
		( active && approved && descriptor.functioning() )
	}

}
