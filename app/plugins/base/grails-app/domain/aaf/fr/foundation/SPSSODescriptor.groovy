package aaf.fr.foundation

import org.apache.commons.lang.builder.EqualsBuilder
import org.apache.commons.lang.builder.HashCodeBuilder

import grails.plugins.federatedgrails.Role

/**
 * @author Bradley Beddoes
 */
class SPSSODescriptor extends SSODescriptor {
	static auditable = true

 	boolean authnRequestsSigned
 	boolean wantAssertionsSigned
  boolean forceAttributesInFilter = false

	ServiceDescription serviceDescription

 	static belongsTo = [entityDescriptor:EntityDescriptor]

	static hasMany = [
		assertionConsumerServices: AssertionConsumerService,
		attributeConsumingServices: AttributeConsumingService,
		discoveryResponseServices: DiscoveryResponseService,
		serviceCategories: ServiceCategory
	]

	static constraints = {
		attributeConsumingServices(nullable: true)
		serviceDescription(nullable:false)
 	}

	public String toString() {	"spssodescriptor:[id:$id, displayName: $displayName]" }

	public boolean functioning() {
		( !archived && active && approved && entityDescriptor.functioning() )
	}

	public boolean equals(Object obj) {
		if( this.is(obj) ) return true
		if ( obj == null ) return false
		if ( !obj.instanceOf(SPSSODescriptor) ) return false

		SPSSODescriptor rhs = (SPSSODescriptor) obj
		return new EqualsBuilder()
			.append(this.id, rhs.id)
			.append(this.displayName, rhs.displayName)
			.append(this.organization, rhs.organization)
			.isEquals()
	}

	public int hashCode() {
		// hard-coded, randomly chosen, non-zero, odd number different for each class
		return new HashCodeBuilder(43, 129).
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

		  information {
		  	catalogue_publish serviceDescription.publish
		  	public_url serviceDescription.connectURL ?: ''
		  	logo_url serviceDescription.logoURL ?: ''
		  	further_info serviceDescription.furtherInfo ?: ''
		  	provides serviceDescription.provides ?: ''
		  	benefits serviceDescription.benefits ?: ''
		  	audience serviceDescription.audience ?: ''
		  	restrictions serviceDescription.restrictions ?: ''
		  	accessing serviceDescription.accessing ?: ''
		  	support serviceDescription.support ?: ''
		  	maintenance serviceDescription.maintenance ?: ''
		  }

		  categories serviceCategories.collect { [id: it.id, name: it.name] }

		  contacts this.contacts.collect { [id: it.id, type: [id: it.type.id, name: it.type.name]] }
		  monitors this.monitors.collect { [id: it.id, type: [id: it.type.id, name: it.type.name], url: it.url, node: it.node ?: '', enabled: it.enabled, check_period: it.checkPeriod] }

		  active this.active
		  archived this.archived
		  approved this.approved
		  functioning this.functioning()
		  reporting this.reporting
		  date_created this.dateCreated
		  last_updated this.lastUpdated
		  administrators adminRole?.subjects.collect { [id: it.id, principal: it.sharedToken] }
			saml {
				entity {
					id entityDescriptor.id
					entity_id entityDescriptor.entityID
				}
				authnrequests_signed authnRequestsSigned
				assertions_signed wantAssertionsSigned
				always_render_attributes_in_filter forceAttributesInFilter
				attribute_consuming_services attributeConsumingServices.collect { [id: it.id, is_default: it.isDefault, approved: it.approved,
																			names: it.serviceNames,
																			descriptions: it.serviceDescriptions,
																			attributes: it.requestedAttributes.collect { [id:it.base.id, name: it.base.name, specification: it.base.specificationRequired,
																									reason: it.reasoning, is_required: it.isRequired,  approved: it.approved,
																									values: it.values.collect { [value: it.value, approved: it.approved] }
																									] }
																			] }
				assertion_consumer_services assertionConsumerServices.collect { [id: it.id, location: it.location, binding: [id: it.binding.id, uri: it.binding.uri], functioning: it.functioning(), index: it.index, is_default: it.isDefault ]}
      	discovery_response_services discoveryResponseServices.collect { [id: it.id, location: it.location, binding: [id: it.binding.id, uri: it.binding.uri], functioning: it.functioning(), index: it.index, is_default: it.isDefault ]}
				sso_descriptor super.structureAsJson()
			}
		}

		json.content
	}
}
