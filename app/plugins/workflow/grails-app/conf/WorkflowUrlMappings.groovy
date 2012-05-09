class WorkflowUrlMappings {

  static mappings = {
    "/workflow/process/$action?/$id?" {
      controller = "workflowProcess"
    }
  
    "/workflow/scripting/$action?/$id?" {
      controller = "workflowScript"
    }
  
    "/workflow/approval/$action/$id?" {
      controller = "workflowApproval"
    }
  }

}
