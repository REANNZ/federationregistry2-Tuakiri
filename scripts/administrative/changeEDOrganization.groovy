import fedreg.core.*
/*
Entity Descriptor (and all child IdP, AA, SP, PDP etc) organization ownership change script

Usage: Simply supply the ID of the organization you wish to make the new owner and the entityID of the entity descriptor to modify. ALL IdP/SP/AA/PdP belonging to this ED will also have their organization ownership pointers updated)

ID: The value identified in the SHOW URL for the new owning organization e.g: /federationregistry/membership/organization/show/<id>
entityID: The entityID shown in metadata for the entity descriptor to operate on

Bradley Beddoes
7/3/2010
*/

// The organization ID to make the new owner. Triple check!!
id = 

// The entityDescriptor to modify organization ownership for
entityID = ""

// ---------------------------
// NO CHANGES BELOW THIS POINT
// ---------------------------


def organization = Organization.get(id)
def entity = EntityDescriptor.findByEntityID(entityID)
if(organization && entity) {
	
	entity.organization = organization
	entity.idpDescriptors.each {
		it.organization = organization
	}
	entity.idpDescriptors.each {
		it.organization = organization
	}
	entity.spDescriptors.each {
		it.organization = organization
	}
	entity.attributeAuthorityDescriptors.each {
		it.organization = organization
	}
	entity.pdpDescriptors.each {
		it.organization = organization
	}
	
	entity.save()
	
	if(entity.hasErrors()) {
		entity.errors.each { println it }
		false
	}
	else {
		println "$entity and all children updated to belong to $organization"
		true
	}
}
else {
	println "No such Organization or Entity Descriptor"
	false
}