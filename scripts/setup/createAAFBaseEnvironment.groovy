import aaf.fr.foundation.*

/*
DON'T RUN THIS UNTIL frFullSetup.groovy has been executed. (You may need to compile this by running "groovy buildFRFullSetup.groovy" from the command line)

Perform initial population of Organization and IDP for FR deployers who wish to start from a clean platform. Alternatively you may wish to create scripts to import your full set of existing IdP/SP.

Once executed you can login and start creating Organisations, Identity Providers and Service Providers.
*/

roleService = ctx.getBean('roleService')
permissionService = ctx.getBean('permissionService')

def lang = 'en'
def federationName = 'aaf.edu.au'
def federationOrgTypes = [
  [name:'university', displayName:'university', description:'Australian University', discoverServiceCategory:'true'],
  [name:'hospital', displayName:'hospital', description:'Hospital', discoverServiceCategory:'true'],
  [name:'library', displayName:'library', description:'Library', discoverServiceCategory:'true'],
  [name:'vho', displayName:'vho', description:'Virtual Home Organization', discoverServiceCategory:'true'],
  [name:'others', displayName:'others', description:'Others', discoverServiceCategory:'true'],
  [name:'marcs', displayName:'marcs', description:'Members of ARCS (MARCS)', discoverServiceCategory:'true'],
  [name:'nzuniversity', displayName:'nzuniversity', description:'New Zealand University', discoverServiceCategory:'true'],
  [name:'Standalone AA', displayName:'Standalone AA', description:'Standalone AA only', discoverServiceCategory:'false']
] as List

//-----------
// Shouldn't be any need for hacking after this point for most usage scenarios
//-----------

// Default Service Category
def sc = new ServiceCategory(name:'General', description:'Default category that suits majority of federation provided services')
sc.save()​

// Default PING monitor
def ping = new MonitorType(name:'ping', description:'Ping check of associated endpoint to ensure availability')
ping.save()​​​​​​​​​​​​​​​​​​​​​​​​​​​​

// Default HTTP monitor
def http = new MonitorType(name:'ping', description:'Ping check of associated endpoint to ensure availability')
http.save()​​​​​​​​​​​​​​​​​​​​​​​​​​​​

// Entities Descriptor
def eds = new EntitiesDescriptor(name:federationName)
def savedEDS = eds.save()
if(!savedEDS) {
	eds.errors.each { println it }
	return
}

// AAF Organization Types
federationOrgTypes.each {
  def ot = new OrganizationType(it)
  savedOT = ot.save()
  if(!savedOT) {
    ot.errors.each { println it }
    return  
  }
}

println "completed createAAFBaseEnvironment.groovy"



​


​
​