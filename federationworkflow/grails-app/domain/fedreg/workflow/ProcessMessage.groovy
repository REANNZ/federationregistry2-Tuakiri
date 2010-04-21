package fedreg.workflow

import fedreg.workflow.ProcessInstance

class ProcessMessage {

    Date date
    String postedBy
    String message
    
    static belongsTo = [parentProcess: ProcessInstance]
    
}
