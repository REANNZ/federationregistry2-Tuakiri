package aaf.fr.foundation

import org.apache.commons.lang.builder.EqualsBuilder
import org.apache.commons.lang.builder.HashCodeBuilder

import grails.plugins.federatedgrails.Role

/**
 * @author Bradley Beddoes
 */
class IDPSSODescriptor extends SSODescriptor  {
	static auditable = true

	String scope

	AttributeAuthorityDescriptor collaborator

	boolean wantAuthnRequestsSigned = false
	boolean autoAcceptServices = false
  boolean attributeAuthorityOnly = false

	List singleSignOnServices

	static belongsTo = [entityDescriptor:EntityDescriptor]

	static hasMany = [
		singleSignOnServices: SingleSignOnService,
		nameIDMappingServices: NameIDMappingService,
		assertionIDRequestServices: AssertionIDRequestService,
		attributeProfiles: SamlURI,
		attributes: Attribute
	]

 	static constraints = {
		scope(nullable: false)
		collaborator(nullable: true)
		singleSignOnServices(minSize: 1)
		nameIDMappingServices(nullable: true)
		assertionIDRequestServices(nullable: true)
		attributeProfiles(nullable: true)
		attributes(nullable: true)
	}

	public String toString() {	"idpssodescriptor:[id:$id, displayName: $displayName]" }

	public boolean functioning() {
		( !archived && active && approved && entityDescriptor.functioning() )
	}

	public List sortedAttributes() {
		def c = Attribute.createCriteria()
		def attributeList = c.list {
			eq("idpSSODescriptor", this)
			createAlias("base","_base")
			order("_base.category", "asc")
			order("_base.name", "asc")
		}
	}

	public boolean equals(Object obj) {
		if( this.is(obj) ) return true
		if ( obj == null ) return false
		if ( !obj.instanceOf(IDPSSODescriptor) ) return false

		IDPSSODescriptor rhs = (IDPSSODescriptor) obj
		return new EqualsBuilder()
			.append(this.id, rhs.id)
			.append(this.scope, rhs.scope)
			.append(this.displayName, rhs.displayName)
			.append(this.organization, rhs.organization)
			.isEquals()
	}

	public int hashCode() {
		// hard-coded, randomly chosen, non-zero, odd number different for each class
		return new HashCodeBuilder(21, 123).
		toHashCode();
	}

	def structureAsJson() {
		def adminRole = Role.findByName("descriptor-${this.id}-administrators")

		def json = new groovy.json.JsonBuilder()
		json {
		  fr_id this.id
		  display_name this.displayName
		  description this.description ?: ''
		  organization { id this.organization.id
		  							 name this.organization.displayName }

			contacts this.contacts.collect { [id: it.id, type: [id: it.type.id, name: it.type.name]] }
			monitors this.monitors.collect { [id: it.id, type: [id: it.type.id, name: it.type.name], url: it.url, node: it.node ?: '', enabled: it.enabled, check_period: it.checkPeriod] }

		  active this.active
		  archived this.archived
		  approved this.approved
		  functioning this.functioning()
		  utilises_attribute_filters autoAcceptServices
		  date_created this.dateCreated
		  last_updated this.lastUpdated
		  administrators adminRole?.subjects.collect { [id: it.id, principal: it.sharedToken] }
			saml {
				entity entityDescriptor.entityID
				attribute_authority_descriptor collaborator?.id
				scope scope
				authnrequests_signed wantAuthnRequestsSigned
				single_sign_on_services singleSignOnServices.collect { [id: it.id, location: it.location, binding: [id: it.binding.id, uri: it.binding.uri], functioning: it.functioning() ]}
				name_id_mapping_services nameIDMappingServices.collect { [id: it.id, location: it.location, binding: [id: it.binding.id, uri: it.binding.uri], functioning: it.functioning() ]}
				assertion_id_request_services assertionIDRequestServices.collect { [id: it.id, location: it.location, binding: [id: it.binding.id, uri: it.binding.uri], functioning: it.functioning() ]}
				attribute_profiles attributeProfiles.collect { [id:it.id, uri: it.uri]}
				attributes attributes.collect { [id:it.base.id, name: it.base.name, specification: it.base.specificationRequired,
																				 values: it.values.collect { [value: it.value, approved: it.approved] }
																			] }
				sso_descriptor super.structureAsJson()
			}
		}
		json.content
	}
}
