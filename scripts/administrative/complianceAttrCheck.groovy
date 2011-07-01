import fedreg.core.*
import fedreg.compliance.*

// ID of IdP to interrogate
def id = 
    
def idp = IDPSSODescriptor.get(id)
if (!idp) {
    println "no such IDP"
    return
}
    
println "\nIdentity Provider: ${idp.displayName} ( $idp.id )"
println "Report generated from federation registry at ${new Date().format('dd/MM/yyyy HH:mm')}"

def categorySupport = []
def categories = AttributeCategory.list()
categories.each {
   def total = AttributeBase.countByCategory(it)
   def supported = idp.attributes.findAll{att -> att.base.category == it }
   def currentStatus = new CategorySupportStatus(totalCount:total, supportedCount:supported.size(), available:AttributeBase.findAllByCategory(it), supported:supported, name:it.name)
   categorySupport.add(currentStatus)
}
   
categorySupport.each { cs ->
   println "\nCategory name: ${cs.name}"
    println "Supported: ${cs.supportedCount} / ${cs.totalCount}"
    println "\n--Supported Attributes--"
    cs.supported.sort{it.id}.each { println "${it.base.friendlyName} ( ${it.base.name} )"}
    
    println "\n--Unsupported Attributes--"
    def uns = false
    cs.available.each { attr -> if(!cs.supported.find{it.base == attr}) {uns = true; println "${attr.friendlyName} ( ${attr.name} )"}}
    if(!uns)
        println "N/A"
    println "\n---------------"
}