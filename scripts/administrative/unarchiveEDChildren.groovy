import fedreg.core.*

/*
Unarchive an entity descriptor and ALL CHILDREN. Does not activate components, manual activation will be required

Bradley Beddoes
6/6/2011
*/

// The entity descriptor to unarchive by ID
def id = 

eds = ctx.getBean('entityDescriptorService')
eds.unarchive(id)