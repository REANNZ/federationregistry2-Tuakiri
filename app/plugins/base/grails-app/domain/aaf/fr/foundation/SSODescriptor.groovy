package aaf.fr.foundation

/**
 * @author Bradley Beddoes
 */
abstract class SSODescriptor extends RoleDescriptor  {
	static auditable = true

  	static hasMany = [
		  nameIDFormats: SamlURI,
		  artifactResolutionServices: ArtifactResolutionService,
		  singleLogoutServices: SingleLogoutService,
		  manageNameIDServices: ManageNameIDService
  	]

  	static mapping = {
		tablePerHierarchy false
  	}

  	static constraints = {
		nameIDFormats(nullable: true)
		artifactResolutionServices(nullable: true)
		singleLogoutServices(nullable: true)
		manageNameIDServices(nullable: true)
  	}

	public String toString() {	"ssodescriptor:[id:$id, displayName: $displayName]" }

}
