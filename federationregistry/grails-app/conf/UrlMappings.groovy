class UrlMappings {
    static mappings = {
	
	"/compliance/identityprovider/attributes/$action?/$id?"{
		controller = "IDPSSODescriptorAttributeCompliance"
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
	
	"/membership/backend/organizationadministration/$action?/$id?"{
		controller = "organizationAdministration"
	}
	
	"/membership/backend/descriptoradministration/$action?/$id?"{
		controller = "descriptorAdministration"
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
	
	"/membership/backend/monitors/$action?/$id?"{
		controller = "roleDescriptorMonitor"
	}
	
	"/confirmadministrator/$action?/$id?"{
		controller = "invitation"
	}
	
	"/registration/$action?/$id?"{
		controller = "bootstrap"
	}
	
	"/metadata/$action"{
		controller = "metadata"
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
