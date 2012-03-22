class AdminUrlMappings {

  static mappings = {
    "/administration/console/$action?/$id?"{
      controller = "adminConsole"
    }
  
    "/console/$action?/$id?"{
      controller = "console"
    }
  }

}
