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
	
      "/"(view:"/index")
	  "500"(view:'/error')
	}
}
