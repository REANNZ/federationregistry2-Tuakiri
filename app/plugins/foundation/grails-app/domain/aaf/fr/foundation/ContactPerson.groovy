package aaf.fr.foundation

/**
 * @author Bradley Beddoes
 */
class ContactPerson  {
	static auditable = true

	Contact contact
	ContactType type
	String extensions
	
	Date dateCreated
	Date lastUpdated

  	static belongsTo = [descriptor:RoleDescriptor, entity:EntityDescriptor, organization:Organization]

	static constraints = {
		descriptor(nullable:true)
        entity(nullable:true)
		organization(nullable:true)
		extensions(nullable:true)
		dateCreated(nullable:true)
		lastUpdated(nullable:true)
	}
	
	public String toString() {	"contactperson:[id:$id, contact: $contact, descriptor:$descriptor, entity:$entity]" }
}
