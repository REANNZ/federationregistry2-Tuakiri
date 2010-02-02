class UrlMappings {
    static mappings = {
      "/$controller/$action?/$id?"{
	      constraints {
			 // apply constraints here
		  }
	  }
	
	"/compliance/attributescope/identityprovider/$action?/$id?"{
		controller = "attributeScope"
	}
	
	"/compliance/identityprovider/attributes/$action?/$id?"{
		controller = "idpAttributeCompliance"
	}
	
	"/compliance/attributes/release/$action?/$id?"{
		controller = "attributeRelease"
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
