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
	
      "/"(view:"/index")
	  "500"(view:'/error')
	}
}
