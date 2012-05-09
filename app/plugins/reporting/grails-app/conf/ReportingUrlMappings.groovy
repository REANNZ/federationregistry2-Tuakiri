class ReportingUrlMappings {

  static mappings = {
    
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

  }

}
