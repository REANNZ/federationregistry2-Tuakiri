import grails.plugins.nimble.core.AdminsService

public class WorkflowEngineSecurityFilters extends grails.plugins.nimble.security.NimbleFilterBase {

    def filters = {

        // Content requiring users to be authenticated
        secure(controller: "processManager") {
            before = {
                accessControl {
                    true
                }
            }
        }
        
        administration(controller: "processManager", action: "(processDefinition|processDefinitions)") {
            before = {
                accessControl {
                    role(AdminsService.ADMIN_ROLE)
                }
            }
        }
    }
}
