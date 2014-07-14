package aaf.fr.foundation

import grails.plugins.federatedgrails.Role

/**
 * @author Bradley Beddoes
 */
class Organization  {	// Also called a participant in AAF land
	static auditable = true

	String name
	String displayName
	String description
	String logoURL
	String lang
	String extensions

	boolean active
	boolean archived = false
	boolean approved = false

	String url

	OrganizationType primary

	Date dateCreated
	Date lastUpdated

	static hasMany = [
		contacts: ContactPerson,
		types : OrganizationType,
		suspensions: OrganizationType,	// Won't render in DS/WAYF listing
		sponsors: Organization,
		affiliates: Organization,
		entityDescriptors: EntityDescriptor
	]

	static mapping = {
		autoImport false
		sort "name"
	}

	static constraints = {
		name(nullable: false, blank: false, unique:true)
		displayName(nullable: false, blank: false)
		description(nullable:true, blank: false, maxSize:2000)
		logoURL(nullable:true)
		lang(nullable: false, blank: false)
		url(nullable: false, blank: false, url: true)
		extensions(nullable: true, blank: true)
		types(nullable:true)
		dateCreated(nullable:true)
		lastUpdated(nullable:true)
		entityDescriptors(nullable:true)
	}

	public String toString() {	"organization:[id:$id, name: $name, displayName: $displayName]" }

	public boolean functioning() {
		( !archived && active && approved )
	}

	public Map buildStatistics() {
		def edTotal = this.entityDescriptors?.size()
		def activeEntityDescriptors = EntityDescriptor.countByOrganizationAndActive(this, true)
		def inactiveEntityDescriptors = EntityDescriptor.countByOrganizationAndActive(this, false)
		def approvedEntityDescriptors = EntityDescriptor.countByOrganizationAndApproved(this, true)
		def unapprovedEntityDescriptors = EntityDescriptor.countByOrganizationAndApproved(this, false)

		def entityDescriptors = [total:edTotal, activeEntityDescriptors:activeEntityDescriptors, inactiveEntityDescriptors:inactiveEntityDescriptors, approvedEntityDescriptors:approvedEntityDescriptors, unapprovedEntityDescriptors:unapprovedEntityDescriptors]

		def idpTotal = IDPSSODescriptor.countByOrganization(this)
		def activeIDPSSODescriptors = IDPSSODescriptor.countByOrganizationAndActive(this, true)
		def inactiveIDPSSODescriptors = IDPSSODescriptor.countByOrganizationAndActive(this, false)
		def approvedIDPSSODescriptors = IDPSSODescriptor.countByOrganizationAndApproved(this, true)
		def unapprovedIDPSSODescriptors = IDPSSODescriptor.countByOrganizationAndApproved(this, false)

		def idpSSODescriptors = [total:idpTotal, activeIDPSSODescriptors:activeIDPSSODescriptors, inactiveIDPSSODescriptors:inactiveIDPSSODescriptors, approvedIDPSSODescriptors:approvedIDPSSODescriptors, unapprovedIDPSSODescriptors:unapprovedIDPSSODescriptors]

		def spTotal = SPSSODescriptor.countByOrganization(this)
		def activeSPSSODescriptors = SPSSODescriptor.countByOrganizationAndActive(this, true)
		def inactiveSPSSODescriptors = SPSSODescriptor.countByOrganizationAndActive(this, false)
		def approvedSPSSODescriptors = SPSSODescriptor.countByOrganizationAndApproved(this, true)
		def unapprovedSPSSODescriptors = SPSSODescriptor.countByOrganizationAndApproved(this, false)

		def spSSODescriptors = [total:spTotal, activeSPSSODescriptors:activeSPSSODescriptors, inactiveSPSSODescriptors:inactiveSPSSODescriptors, approvedSPSSODescriptors:approvedSPSSODescriptors, unapprovedSPSSODescriptors:unapprovedSPSSODescriptors]

		[entityDescriptors:entityDescriptors, idpSSODescriptors:idpSSODescriptors, spSSODescriptors:spSSODescriptors]
	}

	def structureAsJson() {
		def adminRole = Role.findByName("organization-${this.id}-administrators")

		def entityDescriptors = EntityDescriptor.findAllWhere(organization: this)
		def identityProviders = IDPSSODescriptor.findAllWhere(organization: this)
		def serviceProviders = SPSSODescriptor.findAllWhere(organization: this)
		def attributeAuthorities = AttributeAuthorityDescriptor.findAllWhere(organization: this)

		def json = new groovy.json.JsonBuilder()
		json {
		  id this.id
		  domain this.name
		  display_name this.displayName
		  description this.description ?: ''
		  url this.url
		  logo_url this.logoURL ?: ''
		  lang this.lang
		  contacts this.contacts.collect { [id: it.id, type: [id: it.type.id, name: it.type.name]] }
		  active this.active
		  archived this.archived
		  approved this.approved
		  functioning this.functioning()
		  types {
		    primary this.primary.id
		    secondary this.types.collect { it.id }
		    suspensions this.suspensions.collect { it.id }
		  }
		  sponsors this.sponsors.collect { it.id }
		  affiliates this.affiliates.collect { it.id }
		  saml {
		  	entity_descriptors entityDescriptors.collect { [id: it.id, entity_id: it.entityID, functioning: it.functioning()]}
		    identity_providers identityProviders.collect { [id: it.id, entity: [id: it.entityDescriptor.id, entity_id: it.entityDescriptor.entityID], functioning: it.functioning()] }
		    service_providers serviceProviders.collect { [id: it.id, entity: [id: it.entityDescriptor.id, entity_id: it.entityDescriptor.entityID], functioning: it.functioning()] }
		    attribute_authorities attributeAuthorities.collect { [id: it.id, entity: [id: it.entityDescriptor.id, entity_id: it.entityDescriptor.entityID], functioning: it.functioning()] }
		    extensions this.extensions ?: ''
		  }
		  created_at dateCreated
		  updated_at lastUpdated
		  administrators adminRole?.subjects.collect { [id: it.id, principal: it.sharedToken] }
		}
		json.content
	}
}
