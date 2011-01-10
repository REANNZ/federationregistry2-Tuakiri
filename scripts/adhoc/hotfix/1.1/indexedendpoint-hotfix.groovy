import fedreg.core.*

/*
FR HOTFIX

REQUIRES DATABASE MIGRATION - indexedendpoint-hotfix.sql

A colum with the name 'samlmd_index' with all values set to 0 should be created for the table
indexed_endpoint before executing this script. This is only required for 1.0.2 -> 1.1

This script simply mimicks what current MD generation does by assigning an every increasing value to endpoint index. From this point forward all
indexedendpoints will be explicitly set by administrators and rendered as such in metadata.

Bradley Beddoes
7/1/2011
*/

def sps = SPSSODescriptor.list()
def idps = IDPSSODescriptor.list()

sps.each { sp ->

  sp.artifactResolutionServices.sort{it.id}.eachWithIndex { ars, i ->
    ars.index = i+1
    ars.save()
	if(ars.hasErrors()) {
        println "Error updating $ars for $sp" 
      }
  }
  sp.assertionConsumerServices.sort{it.id}.eachWithIndex { acs, i ->
    acs.index = i+1
    acs.save()
	if(acs.hasErrors()) {
        println "Error updating $acs for $sp" 
      }
  }
  sp.discoveryResponseServices.sort{it.id}.eachWithIndex { drs, i ->
    drs.index = i+1
    drs.save()
	if(drs.hasErrors()) {
        println "Error updating $drs for $sp" 
      }
  }

}
  
idps.each { idp ->
  idp.artifactResolutionServices.sort{it.id}.eachWithIndex { ars, i ->
    ars.index = i+1
    ars.save()
	if(ars.hasErrors()) {
        println "Error updating $ars for $idp" 
      }
  }
}