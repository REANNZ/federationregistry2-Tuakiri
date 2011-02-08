import fedreg.core.*

// After running importRRContent.groovy this script will run through all IDP/SP and assign index values to endpoints that are of type IndexedEndpoint

def idps = IDPSSODescriptor.list()
def sps = SPSSODescriptor.list()

idps.each { idp ->
  idp.artifactResolutionServices.eachWithIndex { endpoint, i ->
    endpoint.index = i+1
    endpoint.save()
  }
}
  
sps.each { sp ->
  sp.artifactResolutionServices.eachWithIndex { endpoint, i ->
    endpoint.index = i+1
    endpoint.save()
  }  
  sp.assertionConsumerServices.eachWithIndex { endpoint, i ->
    endpoint.index = i+1
    endpoint.save()
  }
  sp.discoveryResponseServices.eachWithIndex { endpoint, i ->
    endpoint.index = i+1
    endpoint.save()
  }
}