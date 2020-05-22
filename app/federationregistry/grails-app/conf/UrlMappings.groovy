class UrlMappings {

  static mappings = {        
    "/"{
      controller = "welcome"
    }

    "/dashboard/$action?/$id?"{
      controller = "dashboard"
    }

    "/bootstrap/$action?/$id?" {
      controller = "initialBootstrap"
    }

    "403"(controller:'error', action:'notPermitted')
    "404"(controller:'error', action:'notFound')
    "500"(view:'/500')

    // Development only.
    "/greenmail/$action?/$id?"{
      controller = "greenmail"
    }
  }

}
