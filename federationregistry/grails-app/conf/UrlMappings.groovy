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
		controller = "entityDescriptor"
	}
	
	"/membership/identityprovider/$action?/$id?"{
		controller = "IDPSSODescriptor"
	}
	
	"/membership/serviceprovider/$action?/$id?"{
		controller = "SPSSODescriptor"
	}
	
	"/membership/contacts/$action?/$id?"{
		controller = "contacts"
	}
	
	"/membership/backend/contact/$action?/$id?"{
		controller = "descriptorContact"
	}
	
	"/membership/backend/roleDescriptorCrypto/$action?/$id?"{
		controller = "roleDescriptorCrypto"
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
	
	"/membership/backend/attributeconumingservice/$action?/$id?"{
		controller = "attributeConsumingService"
	}
	
	"/registration/$action?/$id?"{
		controller = "bootstrap"
	}
	
	"/coreutils/$action?/$id?"{
		controller = "coreUtilities"
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
	
	"/bootstrap/$action?/$id?" {
		controller = "initialBootstrap"
	}
	
	"/greenmail/$action?/$id?"{
		controller = "greenmail"
	}
	
    "/"(view:"/index")
	  "500"(view:'/error')
	}
}
