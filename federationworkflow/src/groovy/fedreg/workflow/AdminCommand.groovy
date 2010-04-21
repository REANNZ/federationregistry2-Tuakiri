package fedreg.workflow

import fedreg.workflow.ProcessStatus

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
