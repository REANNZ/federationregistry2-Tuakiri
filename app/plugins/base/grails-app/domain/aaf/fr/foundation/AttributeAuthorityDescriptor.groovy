package aaf.fr.foundation

import grails.plugins.federatedgrails.Role

/**
 * @author Bradley Beddoes
 */
class AttributeAuthorityDescriptor extends RoleDescriptor {
	static auditable = true

	IDPSSODescriptor collaborator    // This links the AA with an IDP that collaborates with it to provide authentication assertion services
	String scope

	static belongsTo = [entityDescriptor:EntityDescriptor]

	static hasMany = [
		  attributeServices: AttributeService,
		  assertionIDRequestServices: AssertionIDRequestService,
		  nameIDFormats: SamlURI,
		  attributeProfiles: String,
		  attributes: Attribute
	]

	static constraints = {
		scope(nullable:false)
		collaborator(nullable:true)
    attributeServices(nullable: true)
		assertionIDRequestServices(nullable: true)
		nameIDFormats(nullable: true)
		attributeProfiles(nullable: true)
		attributes(nullable: true)
	}

	public String toString() {	"attributeauthoritydescriptor:[id:$id]" }

	public boolean functioning() {
		if(collaborator)
			( attributeServices?.findAll{it.exposed()}?.size() > 0 && !archived && active && approved && collaborator.functioning() && entityDescriptor.functioning() )
		else
			( attributeServices?.findAll{it.exposed()}?.size() > 0 && !archived && active && approved && entityDescriptor.functioning() )
	}

	public boolean samlSchemaValid() {
		// Missing mandatory endpoints indicates an incomplete AttributeAuthorityDescriptor not valid according to the SAML schema
		attributeServices.any { it.functioning() }
	}

	def structureAsJson() {
		def adminRole = Role.findByName("descriptor-${this.id}-administrators")

		def json = new groovy.json.JsonBuilder()
		json {
		  id this.id
		  display_name this.displayName
		  description this.description ?: ''
		  organization { id this.organization.id
		  							 name this.organization.displayName }

		  if(!collaborator)	{
				contacts this.contacts.collect { [id: it.id, type: [id: it.type.id, name: it.type.name]] }
				monitors this.monitors.collect { [id: it.id, type: [id: it.type.id, name: it.type.name], url: it.url, node: it.node ?: '', enabled: it.enabled, check_period: it.checkPeriod] }
		  }

			active this.active
			archived this.archived
			approved this.approved
			functioning this.functioning()
			created_at dateCreated
			updated_at lastUpdated
			administrators adminRole?.subjects.collect { [id: it.id, principal: it.sharedToken] }

			saml {
				entity {
					id entityDescriptor.id
					entity_id entityDescriptor.entityID
				}
				if(collaborator) {
					extract_metadata_from_idp_sso_descriptor true
					attribute_services attributeServices.collect { [id: it.id, location: it.location, binding: [id: it.binding.id, uri: it.binding.uri], functioning: it.functioning() ]}
					idp_sso_descriptor collaborator?.id
				} else {
					extract_metadata_from_idp_sso_descriptor false
					scope scope
					attribute_services attributeServices.collect { [id: it.id, location: it.location, binding: [id: it.binding.id, uri: it.binding.uri], functioning: it.functioning() ]}
					assertion_id_request_services assertionIDRequestServices.collect { [id: it.id, location: it.location, binding: [id: it.binding.id, uri: it.binding.uri], functioning: it.functioning() ]}
					name_id_formats nameIDFormats.collect{ [id: it.id, uri: it.uri]}
					attribute_profiles attributeProfiles.collect { [id:it.id, uri: it.uri]}
					attributes attributes.collect { [id:it.base.id, name: it.base.name, specification: it.base.specificationRequired,
																					 values: it.values.collect { [value: it.value, approved: it.approved] }
																				] }
					role_descriptor super.structureAsJson()
				}
			}
		}
	}
}
