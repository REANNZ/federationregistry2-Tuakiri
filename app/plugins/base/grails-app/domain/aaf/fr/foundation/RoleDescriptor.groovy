package aaf.fr.foundation

/**
 * While not marked explicitly due to GORM issues RoleDescriptor is considered 'Abstract'
 *
 * @author Bradley Beddoes
 */
abstract class RoleDescriptor extends Descriptor {
	static auditable = true

	def cryptoService

	Organization organization
	String errorURL

	String displayName
	String description
	String extensions

	boolean active
	boolean archived = false
	boolean approved = false
	boolean reporting = true

	Date dateCreated
	Date lastUpdated

	static hasMany = [
		contacts: ContactPerson,
		protocolSupportEnumerations: SamlURI,
		keyDescriptors: KeyDescriptor,
		monitors: ServiceMonitor
	]

	static mapping = {
		tablePerHierarchy false
		sort "displayName"
	}

	static constraints = {
		displayName(nullable:false, blank:false)
		description(nullable:false, blank: false, maxSize:2000)

		organization(nullable: false)
		extensions(nullable: true, maxSize:2000)
		errorURL(nullable:true)
		protocolSupportEnumerations(nullable: false, minSize:1)
		contacts(nullable: true)
		keyDescriptors(nullable: true)
		dateCreated(nullable:true)
		lastUpdated(nullable:true)
	}

	public String toString() { "roledescriptor:[id:$id, displayName: $displayName]" }

	// RoleDescriptor is considered abstract but can't be marked as such due to GORM issues
	// This method should be overlaoded by all subclasses
	public boolean functioning() {
		false
	}

	def structureAsJson() {
    def json = new groovy.json.JsonBuilder()
    json {
    	protocol_support_enumerations protocolSupportEnumerations.collect{ [id: it.id, uri: it.uri]}
    	key_descriptors keyDescriptors.collect {
    										[  id: it.id, type: it.keyType, disabled: it.disabled,
	    										 encryption_method: [ algorithm: it.encryptionMethod?.algorithm ?: '',
	    										 										  key_size: it.encryptionMethod?.keySize ?: '',
	    										 										  oae_params: it.encryptionMethod?.oaeParams ?: ''
	    										 ],
	    										 key_info: [ name: it.keyInfo.keyName ?: '',
	    															   certificate:[ expires: it.keyInfo.certificate.expiryDate,
																								    subject: it.keyInfo.certificate.subject,
																								    issuer: it.keyInfo.certificate.issuer,
																								    data: it.keyInfo.certificate.data
																			  ]
													 ],
    									  ]
    									}
      error_url errorURL
      extensions extensions ?:''
    }

    json.content
  }
}
