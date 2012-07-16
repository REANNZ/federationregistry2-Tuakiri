class ApiUrlMappings {

  static mappings = {
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
  }
}
