import fedreg.core.*
import fedreg.compliance.*
    
IDPSSODescriptor.list().sort{it.displayName}.each { idp ->
    
    if(idp.active && idp.approved && !idp.archived) {
    	println "\nOrganization: ${idp.entityDescriptor.organization.displayName} ( ID: ${idp.entityDescriptor.organization.id} )"
    	println "Identity Provider: ${idp.displayName} ( ID: $idp.id )"
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
    	   println "\n\nCategory: ${cs.name}"
    	    println "Supported: ${cs.supportedCount} / ${cs.totalCount} ( ${((cs.supportedCount / cs.totalCount) * 100)}% )"
    	    println "\n--Supported Attributes--"
    	    cs.supported.sort{it.id}.each { println "${it.base.friendlyName} ( ${it.base.name} )"}
        
    	    println "\n--Unsupported Attributes--"
    	    def uns = false
    	    cs.available.each { attr -> if(!cs.supported.find{it.base == attr}) {uns = true; println "${attr.friendlyName} ( ${attr.name} )"}}
    	    if(!uns)
    	        println "N/A"
    	}
    	
    	println "\n================================"
    }
    
}