class UrlMappings {
    static mappings = {
      "/$controller/$action?/$id?"{
	      constraints {
			 // apply constraints here
		  }
	  }
	
	"/attributescope/$action?/$id?"{
		controller = "attributeScope"
	}
	
	"/attributecompliance/$action?/$id?"{
		controller = "attributeCompliance"
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
