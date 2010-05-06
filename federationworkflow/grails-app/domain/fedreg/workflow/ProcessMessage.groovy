package fedreg.workflow

import grails.plugins.nimble.core.UserBase

class ProcessMessage {

	UserBase creator
    String message
	Date dateCreated
	
	TaskInstance references
    
    static belongsTo = [processInstance: ProcessInstance]

	static constraints = {
		references(nullable:true)
		dateCreated(nullable:true)
	}
    
}
