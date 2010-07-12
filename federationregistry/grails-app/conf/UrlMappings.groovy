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
		controller = "IDPSSODescriptor"
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
	
	"/workflow/process/$action?/$id?" {
			controller = "workflowProcess"
	}
	
	"/workflow/scripting/$action?/$id?" {
			controller = "workflowScript"
	}
	
	"/workflow/$action?/$id?" {
			controller = "workflowInstance"
	}
	
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
	
	"/codeconsole/$action?/$id?"{
		controller = "code"
	}
	
	"/console/$action?/$id?"{
		controller = "console"
	}
	
    "/"(view:"/index")
	  "500"(view:'/error')
	}
}
