package fedreg.workflow.engine

import fedreg.workflow.engine.ProcessInstance

class ProcessMessage {

    Date date
    String postedBy
    String message
    
    static belongsTo = [parentProcess: ProcessInstance]
    
}
