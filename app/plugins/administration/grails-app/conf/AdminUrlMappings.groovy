class AdminUrlMappings {

  static mappings = {
    "/administration/attributes/$action?/$id?"{
      controller = "attributeBase"
    }

    "/administration/monitortype/$action?/$id?"{
      controller = "monitorType"
    }

    "/administration/organisationtype/$action?/$id?"{
      controller = "organizationType"
    }

    "/administration/cakeyinfo/$action?/$id?"{
      controller = "CAKeyInfo"
    }

    "/administration/console/$action?/$id?"{
      controller = "adminConsole"
    }

    "/administration/samluri/$action?/$id?"{
      controller = "samlURI" 
    }
  
    "/console/$action?/$id?"{
      controller = "console"
    }
  }

}
