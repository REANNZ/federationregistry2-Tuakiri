import fedreg.core.*

/*
FR HOTFIX
This is only required for 1.x -> 1.1.3

Due to invalid configuration for Shibboleth 2.x IDP all IDP/AA created using FR prior to 1.1.3 did not append :8443 to ArtifactResolutionService and AttributeService URI.

This script corrects the URI values for created IDP/AA by appending the correct 8443 port designation.

Bradley Beddoes
9/3/2011
*/

def commit = false

def ars = ArtifactResolutionService.list()
def acs = AttributeService.list()

def desc = []

if(!commit)
 	println "SCRIPT RUNNING IN NON COMMIT MODE, UPDATES NOT PERMANENT\n"

ars.each {
  if(!it.location.uri.contains("8443") && it.location.uri.contains("idp/profile")) {
    if(!desc.contains(it)) desc.add(it.descriptor)
    def invalid = it.location.uri
    def corrected = it.location.uri.replace('/idp/profile', ":8443/idp/profile")
    println "Set invalid ARS URI $invalid to valid URI $corrected"
	if(commit) {
		it.location.uri = corrected
		it.save()
		
		if(it.hasErrors()) { 
			println "Error attempting to update $it"
			it.errors.each { err -> println err }
		}
	}
  }
}
  
acs.each {
  if(!it.location.uri.contains("8443") && it.location.uri.contains("idp/profile")) {
    if(!desc.contains(it)) desc.add(it.descriptor)
    def invalid = it.location.uri
    def corrected = it.location.uri.replace('/idp/profile', ":8443/idp/profile")
    println "Set invalid ACS URI $invalid to valid URI $corrected"
	if(commit) {
		it.location.uri = corrected
		it.save()
		
		if(it.hasErrors()) { 
			println "Error attempting to update $it"
			it.errors.each { err -> println err }
		}
	}
  }
}

if(commit)
	println "\nDescriptors that were corrected by this script:" 
else
 	println "\nDescriptors that would be corrected by commiting this script:" 

desc.each {
    println "[$it.id] $it.displayName - $it.dateCreated"
} 
  
println "\n\nNumber of descriptors in error: $desc.size"  
  
true