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
	
	"/bootstrap" {
			controller = "dataManagement"
			action = "bootstrap"
	}
	
	"/datamanagement/$action?/$id?"{
		controller = "dataManagement"
	}
	
    "/"(view:"/index")
	  "500"(view:'/error')
	}
}
