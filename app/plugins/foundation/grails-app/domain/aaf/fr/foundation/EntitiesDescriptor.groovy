package aaf.fr.foundation

/**
 * @author Bradley Beddoes
 */
class EntitiesDescriptor {
	static auditable = true

	String name
	String extensions

	Date dateCreated
	Date lastUpdated

	static hasMany = [
		entityDescriptors: EntityDescriptor,
		entitiesDescriptors: EntitiesDescriptor,
	]

	static constraints = {
		entityDescriptors(nullable: true)
		entitiesDescriptors(nullable: true)
		name(nullable: true)
		extensions(nullable: true)
		dateCreated(nullable:true)
		lastUpdated(nullable:true)
	}
	
	public String toString() { "entitiesdescriptor:[id:$id, name: $name]" }
}
