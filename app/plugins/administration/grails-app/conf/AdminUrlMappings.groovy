class AdminUrlMappings {

  static mappings = {
    "/administration/dashboard"{
      controller="adminDashboard"
      action="index"
    }

    "/administration/subjects/$action?/$id?"{
      controller = "subject"
    }

    "/administration/roles/$action?/$id?"{
      controller = "role"
    }

    "/administration/attributes/$action?/$id?"{
      controller = "attributeBase"
    }

    "/administration/attributecategory/$action?/$id?"{
      controller = "attributeCategory"
    }

    "/administration/monitortype/$action?/$id?"{
      controller = "monitorType"
    }

    "/administration/organisationtype/$action?/$id?"{
      controller = "organizationType"
    }

    "/administration/contacttype/$action?/$id?"{
      controller = "contactType"
    }

    "/administration/servicecategory/$action?/$id?"{
      controller = "adminServiceCategory"
    }

    "/administration/cakeyinfo/$action?/$id?"{
      controller = "CAKeyInfo"
    }

   "/administration/samluri/$action?/$id?"{
      controller = "samlURI"
    }

    "/administration/adminconsole/$action?/$id?"{
      controller = "adminConsole"
    }

    "/internal/console/$action?/$id?"{
      controller = "console"
    }

  }

}
