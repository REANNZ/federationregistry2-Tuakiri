class UrlMappings {
    static mappings = {
	
	"/compliance/attributescope/identityprovider/$action?/$id?"{
		controller = "attributeScope"
	}
	
	"/compliance/identityprovider/attributes/$action?/$id?"{
		controller = "idpAttributeCompliance"
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
	
	"/membership/entity/$action?/$id?"{
		controller = "entity"
	}
	
	"/membership/identityprovider/$action?/$id?"{
		controller = "identityProvider"
	}
	
	"/membership/contacts/$action?/$id?"{
		controller = "contacts"
	}
	
	"/membership/backend/contact/$action?/$id?"{
		controller = "descriptorContact"
	}
	
	"/membership/backend/keydescriptor/$action?/$id?"{
		controller = "descriptorKeyDescriptor"
	}
	
	"/membership/backend/endpoint/$action?/$id?"{
		controller = "descriptorEndpoint"
	}
	
	"/membership/backend/nameidformat/$action?/$id?"{
		controller = "descriptorNameIDFormat"
	}
	
	"/membership/backend/attribute/$action?/$id?"{
		controller = "descriptorAttribute"
	} 
	
	"/workflow/$action?/$id?"{
		controller = "workflowManager"
	}
	
	"/workflow/instances/$id?" (controller: 'workflowManager', action: 'processInstances')
    "/workflow/instances/search/$id?" (controller: 'workflowManager', action: 'dateSearch')
    "/workflow/processes/$id?" (controller: 'workflowManager', action: 'processDefinitions')
    "/workflow/processes/create/$id?" (controller: 'workflowManager', action: 'newProcess')
    "/workflow/processes/definition/$id?" (controller: 'workflowManager', action: 'processDefinition')
	
	"/bootstrap" {
			controller = "dataManagement"
			action = "bootstrap"
	}
	
	"/datamanagement/$action?/$id?"{
		controller = "dataManagement"
	}
	
	"/monitor/$action?/$id?"{
		controller = "monitor"
	}
	
	
    "/"(view:"/index")
	  "500"(view:'/error')
	}
}
