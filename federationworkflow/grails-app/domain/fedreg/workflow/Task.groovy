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
	List dependenices = []
   	
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
		description(nullable:false, blank:false)
		execute( validator: { val, obj ->
			obj.with {
				if(ensureMinimalDirectives()) {
					if(execute == null || execute.keySet().size() == 0) {
						log.debug('Overall directives satisified, no execute directives to validate')
						return true
					}
					else {
						if(execute.containsKey('service')) {
							log.debug('Execute directive located containing a service definition, validating method is defined')
							return execute.keySet().size() == 2 && execute.containsKey('method')
						}
						else {
							if(execute.containsKey('controller')) {
								log.debug('Execute directive located containing a controller definition, validating action (and optional ID) is defined')
								if(execute.keySet().size() == 2) 
									return execute.containsKey('action')
								else 
									return execute.keySet().size() == 3 && execute.containsKey('action') && execute.containsKey('id')
							}
							else {
								log.error('Execute directive located that does not specify service or controller')
								return false	// specifies neither service or controller
							}
						}
					}
				}
				else
					return false
			}
		})
		approvers( validator: { val, obj ->
			obj.ensureMinimalDirectives()
		})
		approverGroups( validator: { val, obj ->
			obj.ensureMinimalDirectives()
		})
		approverRoles( validator: { val, obj ->
			obj.ensureMinimalDirectives()
		})
		outcomes( validator: {val, obj ->
			obj.with {
				if(!finishOnThisTask) {
					if(needsApproval() && !executes())
						outcomes.size() == 1
					else
						outcomes.size() > 0
				}
				else
					outcomes == null || outcomes.size() == 0
			}
		})
		rejections( validator: {val, obj ->
			obj.with {
				if(needsApproval()) {
					rejections.size() > 0
				}
				else
					true
			}
		})
	}
	
	def ensureMinimalDirectives = {
		if (!finishOnThisTask) {
			!((approvers.size() == 0) && (approverGroups.size() == 0) && (approverRoles.size() == 0) && (execute.size() == 0))
		}
		else
			true
	}
	
	def needsApproval = {
		(approvers.size() > 0) || (approverGroups.size() > 0) || (approverRoles.size() > 0)
	}
	
	def executes = {
		execute.size() > 0
	}
}
