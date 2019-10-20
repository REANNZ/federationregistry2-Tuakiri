package aaf.fr.foundation

/**
 * @author Bradley Beddoes
 */
class EntityDescriptor extends Descriptor  {
	static auditable = true

	// Organization is the top teir in our model. All entities must belong to an organization definition
	Organization organization

	String entityID
	String extensions

	boolean active
	boolean archived = false
	boolean approved

	Date dateCreated
	Date lastUpdated

	static hasMany = [
		  idpDescriptors: IDPSSODescriptor,
		  spDescriptors: SPSSODescriptor,
		  attributeAuthorityDescriptors: AttributeAuthorityDescriptor,
		  pdpDescriptors: PDPDescriptor,
		  additionalMetadataLocations: AdditionalMetadataLocation,
		  contacts: ContactPerson
	]

	static constraints = {
		organization(nullable:false)
		entityID(nullable:false, blank:false, unique:true)
		idpDescriptors(nullable: true)
		spDescriptors(nullable: true)
		attributeAuthorityDescriptors(nullable: true)
		pdpDescriptors(nullable: true)
		contacts(nullable: true)
		dateCreated(nullable:true)
		lastUpdated(nullable:true)
    extensions(nullable: true, maxSize:2000)
  }

	static mapping = {
		tablePerHierarchy false
	}

	public String toString() {	"entitydescriptor:[id:$id, entityID: $entityID]" }

	public boolean holdsIDPOnly() {
		( idpDescriptors?.size() == 1 && (attributeAuthorityDescriptors?.size() == 0 || attributeAuthorityDescriptors?.size() == 1) && spDescriptors?.size() == 0 && pdpDescriptors?.size() == 0 )
	}

	public boolean holdsSPOnly() {
		( spDescriptors?.size() == 1 && idpDescriptors?.size() == 0 && attributeAuthorityDescriptors?.size() == 0 && pdpDescriptors?.size() == 0 )
	}

	public boolean functioning() {
		( !archived && active && approved && organization.functioning() )
	}

	public boolean empty() {
		def empty = true
		// No children or no children functioning indicates an empty ED
		if(empty && idpDescriptors) {
		 	idpDescriptors.each {
				if(it.functioning() && it.samlSchemaValid())
					empty = false
			}
		}
		if(empty && attributeAuthorityDescriptors) {
		 	attributeAuthorityDescriptors.each {
				if(it.functioning() && it.samlSchemaValid())
					empty = false
			}
		}
		if(empty && spDescriptors) {
		 	spDescriptors.each {
				if(it.functioning() && it.samlSchemaValid())
					empty = false
			}
		}
		if(empty && pdpDescriptors) {
		 	pdpDescriptors.each {
				if(it.functioning() && it.samlSchemaValid())
					empty = false
			}
		}

		empty
	}

	def structureAsJson() {
		def json = new groovy.json.JsonBuilder()
		json {
		  id this.id
		  entity_id entityID
		  organization { id this.organization.id
		  							 name this.organization.displayName }
		  contacts this.contacts.collect { [id: it.id, type: [id: it.type.id, name: it.type.name]] }?.sort{it.id}

		  active active
		  archived archived
		  approved approved
		  functioning functioning()
		  created_at dateCreated
		  updated_at lastUpdated
		  saml {
		  	idp_sso_descriptor_only this.holdsIDPOnly()
		  	sp_sso_descriptor_only this.holdsSPOnly()
			  empty this.empty()
		  	extensions extensions ?: ''
		  	identity_providers idpDescriptors.collect { [id: it.id, functioning: it.functioning()] }?.sort{it.id}
		  	service_providers spDescriptors.collect { [id: it.id, functioning: it.functioning()] }?.sort{it.id}
		  	attribute_authorities attributeAuthorityDescriptors.collect { [id: it.id, functioning: it.functioning()] }?.sort{it.id}
		  	additional_metadata_locations additionalMetadataLocations.collect{ [uri: it.uri, namespace: it.namespace]}?.sort{it.uri}
		  }
		}
	}
}
