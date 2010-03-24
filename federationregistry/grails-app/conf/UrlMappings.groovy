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
