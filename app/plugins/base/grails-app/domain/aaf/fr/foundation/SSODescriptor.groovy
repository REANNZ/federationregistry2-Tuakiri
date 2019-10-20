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

  def structureAsJson() {
    def json = new groovy.json.JsonBuilder()
    json {
      role_descriptor super.structureAsJson()
      name_id_formats nameIDFormats.collect { [id:it.id, uri: it.uri]}?.sort{it.id}
      artifact_resolution_services artifactResolutionServices.collect { [id: it.id, location: it.location, binding: [id: it.binding.id, uri: it.binding.uri], functioning: it.functioning(), index: it.index, is_default: it.isDefault ]}?.sort{it.id}
      single_logout_services singleLogoutServices.collect { [id: it.id, location: it.location, binding: [id: it.binding.id, uri: it.binding.uri], functioning: it.functioning() ]}?.sort{it.id}
      manage_nameid_services manageNameIDServices.collect { [id: it.id, location: it.location, binding: [id: it.binding.id, uri: it.binding.uri], functioning: it.functioning() ]}?.sort{it.id}
    }

    json.content
  }
}
