import fedreg.core.*
/*
Organization, Entity Descriptor and IDP/AA or SP removal script
This will only work for Organizations that only have Entity Descriptors who have either an IDP/AA child or single SP child. Other non standard configurations need manual intervention

Usage: Simply supply the ID of the organization you wish to delete as the value for id below and press execute.
ID: The value identified in the SHOW URL for this organization e.g: /federationregistry/membership/organization/show/<id>

Bradley Beddoes
22/12/2010
*/

// The organization to delete. Triple check!!
id = 

// ---------------------------
// NO CHANGES BELOW THIS POINT
// ---------------------------

organizationService = ctx.getBean("organizationService")

def org = Organization.get(id)
if(org) {
  organizationService.delete(id)

  org = Organization.get(id)
  if(org) {
    println "Organization found but delete failed review FR logs on disk for reasoning"
    false
  }
  else {
    println "Deleted Organization successfully"
    true
  }
}
else {
  println "No such Organization"
  false
}