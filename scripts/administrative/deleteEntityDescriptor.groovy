import fedreg.core.*
/*
Entity Descriptor and IDP/AA or SP removal script
This will only work for Entity Descriptors who have either an IDP/AA child or single SP child. Other non standard configurations need manual intervention

Usage: Simply supply the ID of the entity descriptor you wish to delete as the value for id below and press execute.
ID: The value identified in the SHOW URL for this Entity Descriptor e.g: /federationregistry/membership/entity/show/<id>

Bradley Beddoes
21/12/2010
*/

// The entityID to delete. Triple check!!
id = 

// ---------------------------
// NO CHANGES BELOW THIS POINT
// ---------------------------

entityDescriptorService = ctx.getBean("entityDescriptorService")

def entity = EntityDescriptor.get(id)
if(entity) {
  entityDescriptorService.delete(id)

  entity = EntityDescriptor.get(id)
  if(entity) {
    println "Entity Descriptor found but delete failed review FR logs on disk for reasoning"
    false
  }
  else {
    println "Deleted Entity Descriptor successfully"
    true
  }
}
else {
  println "No such Entity Descriptor"
  false
}