package fedreg.workflow

import grails.plugins.nimble.core.UserBase
import grails.plugins.nimble.core.Role
import grails.plugins.nimble.core.Group

class Task {

    String name
	String description
	boolean automated = true
	
	Map execute = [:]
   	Map outcomes = [:]
	Map rejections = [:]
	
	List approverRoles = []
	List approverGroups = []
	List approvers = []
   	
   	boolean finishOnThisTask = false

   	static hasMany = [	instances: TaskInstance, 
						rejections: TaskRejection,
						outcomes: TaskOutcome,
						dependencies: String,
						approverRoles: String,
						approverGroups: String,
						approvers: String
	]
	
    static belongsTo = [ process: Process ]
	
	static constraints = {

	}
	
}
