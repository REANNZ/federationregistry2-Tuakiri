package fedreg.workflow.engine

import fedreg.workflow.engine.ProcessStatus

class AdminCommand {
    
    String processId
    String processName
    String initiatedBy
    ProcessStatus status
    Date startDateInitiated
    Date endDateInitiated
    Date startDateRequired
    Date endDateRequired
    Date startDateCompleted
    Date endDateCompleted

}
