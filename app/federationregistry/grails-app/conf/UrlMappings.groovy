class UrlMappings {
    static mappings = {
    // REST APIs
    "/api/v1/attributefilters"(controller:"attributeFiltersAPIv1"){
      action = [GET:"list"]
    }
    
    "/api/v1/attributefilters/$id"(controller:"attributeFiltersAPIv1"){
      action = [GET:"show"]
    }
    
    "/api/v1/metadata"(controller:"metadataAPIv1"){
      action = [GET: "list"]
    }
    
    "/api/v1/metadata/$type/$id?"(controller:"metadataAPIv1"){
      action = [GET: "show"]
    }

    "/api/v1/organizations"(controller:"organizationAPIv1"){ 
      action = [GET: "list"]
    }

    "/api/v1/organizations/$id"(controller:"organizationAPIv1"){
      action = [GET: "show"]
    }
    
    "/api/v1/identityproviders"(controller:"identityProvidersAPIv1"){
      action = [GET: "list"]
    }
    
    "/api/v1/identityproviders/$id"(controller:"identityProvidersAPIv1"){
      action = [GET: "show"]
    }
    
    "/api/v1/serviceproviders"(controller:"serviceProvidersAPIv1"){
      action = [GET: "list"]
    }
    
    "/api/v1/serviceproviders/$id"(controller:"serviceProvidersAPIv1"){
      action = [GET: "show"]
    }
    
    "/api/v1/servicecategories"(controller:"serviceCategoriesAPIv1"){
      action = [GET: "list"]
    }
    
    "/api/v1/servicecategories/$id"(controller:"serviceCategoriesAPIv1"){
      action = [GET: "show"]
    }
    
    "/api/v1/monitoring/$type?"(controller:"monitoringAPIv1"){
      action = [GET: "list"]
    }
    
    "/api/v1/monitoring/$type/$id"(controller:"monitoringAPIv1"){
      action = [GET: "show"]
    }
        
    // Application
    "/dashboard/$action?/$id?"{
      controller = "dashboard"
    }
    
    "/reporting/federation/$action/$id?" {
      controller = "federationReports"
    }

    "/reporting/compliance/$action/$id?" {
      controller = "complianceReports" 
    }
    
    "/reporting/identityprovider/$action/$id?" {
      controller = "identityProviderReports"
    }

    "/reporting/serviceprovider/$action/$id?" {
      controller = "serviceProviderReports"
    }
  
    "/compliance/identityprovider/attributes/$action?/$id?"{
      controller = "identityProviderAttributeCompliance"
    }
  
    "/compliance/attributes/release/$action?/$id?"{
      controller = "attributeRelease"
    }
  
    "/compliance/cautilization/$action?/$id?"{
      controller = "certifyingAuthorityUsage"
    }
  
    "/membership/organization/$action?/$id?"{
      controller = "organization"
    }
  
    "/membership/entitydescriptor/$action?/$id?"{
      controller = "entityDescriptor"
    }
  
    "/membership/identityprovider/$action?/$id?"{
      controller = "identityProvider"
    }
  
    "/membership/serviceprovider/$action?/$id?"{
      controller = "serviceProvider"
    }
  
    "/membership/contacts/$action?/$id?"{
      controller = "contacts"
    }
  
    "/membership/backend/organizationadministration/$action?/$id?"{
      controller = "organizationAdministration"
    }
  
    "/membership/backend/descriptoradministration/$action?/$id?"{
      controller = "descriptorAdministration"
    }
  
    "/membership/backend/contact/$action?/$id?"{
      controller = "descriptorContact"
    }
    
    "/membership/backend/orgcontact/$action?/$id?"{
      controller = "organizationContact"
    }
  
    "/membership/backend/roledescriptorcrypto/$action?/$id?"{
      controller = "roleDescriptorCrypto"
    }
  
    "/membership/backend/endpoint/$action?/$id?"{
      controller = "descriptorEndpoint"
    }
    
    "/membership/backend/servicecategories/$action?/$id?"{
      controller = "serviceCategory"
    }
  
    "/membership/backend/nameidformat/$action?/$id?"{
      controller = "descriptorNameIDFormat"
    }
  
    "/membership/backend/attribute/$action?/$id?"{
      controller = "descriptorAttribute"
    }
  
    "/membership/backend/attributeconumingservice/$action?/$id?"{
      controller = "attributeConsumingService"
    }
  
    "/membership/backend/monitors/$action?/$id?"{
      controller = "roleDescriptorMonitor"
    }
  
    "/confirmadministrator/$action?/$id?"{
      controller = "invitation"
    }
    
    "/servicecategories/json/$id" {
      controller = "serviceCategory"
      action = "json"
    }
  
    "/registration/$action?/$id?"{
      controller = "bootstrap"
    }
  
    "/metadata/$action?/$id?"{
      controller = "metadata"
    }

    "/wayf/$action"{
      controller = "wayf"
    }
  
    "/attributefilter/$action?/$id?"{
      controller = "attributeFilter"
    }
  
    "/wayf/$action"{
      controller = "wayf"
    }
  
    "/coreutils/$action?/$id?"{
      controller = "coreUtilities"
    }
  
    "/workflow/process/$action?/$id?" {
      controller = "workflowProcess"
    }
  
    "/workflow/scripting/$action?/$id?" {
      controller = "workflowScript"
    }
  
    "/workflow/approval/$action/$id?" {
      controller = "workflowApproval"
    }
  
    "/datamanagement/$action?/$id?"{
      controller = "dataManagement"
    }
  
    "/monitor/$action?/$id?"{
      controller = "monitor"
    }
  
    "/bootstrap/$action?/$id?" {
      controller = "initialBootstrap"
    }
  
    "/greenmail/$action?/$id?"{
      controller = "greenmail"
    }

    "/auth/$action?/$id?"{
      controller = "auth"
    }
  
    "/"{
      controller = "welcome"
    }

    "403"(view:'/403')
    "404"(view:'/404')
    "500"(view:'/500')

  }
}
