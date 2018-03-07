package aaf.fr.foundation

/**
 * @author Bradley Beddoes
 */
class ArtifactResolutionService extends IndexedEndpoint  {
	static auditable = true

	static belongsTo = [descriptor: SSODescriptor]

	public String toString() {	"artifactresolutionservice:[id:$id, location: $location]" }
	
	public boolean functioning() {
		( active && approved && descriptor.functioning() )
	}

}
