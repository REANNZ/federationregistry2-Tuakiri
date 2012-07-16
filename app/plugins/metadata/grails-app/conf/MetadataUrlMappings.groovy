class MetadataUrlMappings {

  static mappings = {
    "/metadata/$action?/$id?"{
      controller = "metadata"
    }

    "/wayf/$action"{
      controller = "wayf"
    }
  
    "/attributefilter/$action?/$id?"{
      controller = "attributeFilter"
    }
  }
  
}
