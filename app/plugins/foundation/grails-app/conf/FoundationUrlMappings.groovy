class FoundationUrlMappings {

  static mappings = {
    "/bootstrap/$action?/$id?" {
      controller = "initialBootstrap"
    }

    "/coreutils/$action?/$id?"{
      controller = "coreUtilities"
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
  }
  
}
