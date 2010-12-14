import fedreg.core.*

def idps = IDPSSODescriptor.list()
def sps = SPSSODescriptor.list()

println "\nTest IDP in production space (identified with 'test' in name)"
idps.each {
  if(it.displayName.toLowerCase().contains('test'))
    println "IDP: $it.displayName - Organization: $it.organization.name"
}

println "\n\nTest SP in production space (identified with 'test' in name)"
sps.each {
  if(it.displayName.toLowerCase().contains('test'))
    println "SP: $it.displayName - Organization: $it.organization.name"
}

println "\n\nDev IDP in production space (identified with 'dev' in name)"
idps.each {
  if(it.displayName.toLowerCase().contains('dev'))
    println "IDP: $it.displayName - Organization: $it.organization.name"
}

println "\n\nDev SP in production space (identified with 'dev' in name)"
sps.each {
  if(it.displayName.toLowerCase().contains('dev'))
    println "SP: $it.displayName - Organization: $it.organization.name"
}
​