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
						approvers: String,
						execute: String
	]
	
    static belongsTo = [ process: Process ]
	
	static constraints = {
		description(nullable:false, blank:false)
		execute( validator: { val, obj ->
			obj.validateExecution()
		})
		approvers( validator: { val, obj ->
			!obj.ensureMinimalDirectives() ? ['task.validation.directives.approvers.invalid', name] : true
		})
		approverGroups( validator: { val, obj ->
			!obj.ensureMinimalDirectives() ? ['task.validation.directives.approvers.invalid', name] : true
		})
		approverRoles( validator: { val, obj ->
			!obj.ensureMinimalDirectives() ? ['task.validation.directives.approvers.invalid', name] : true
		})
		outcomes( validator: {val, obj ->
			obj.validateOutcomes()
		})
		rejections( validator: {val, obj ->
			obj.validateRejections()
		})
	}
	
	def ensureMinimalDirectives = {
		if (!finishOnThisTask) {
			if( (approvers.size() == 0) && (approverGroups.size() == 0) && (approverRoles.size() == 0) && (execute.size() == 0) )
				return false
		}
		true
	}
	
	def needsApproval = {
		(approvers.size() > 0) || (approverGroups.size() > 0) || (approverRoles.size() > 0)
	}
	
	def executes = {
		execute.size() > 0
	}
	
	def validateRejections = {
		if(needsApproval()) {
			if(rejections.size() == 0)
				return ['task.validation.approval.rejections.invalid.count', name]
		}
		true
	}
	
	def validateOutcomes = {
		if(!finishOnThisTask) {
			if(needsApproval() && !executes()) {
				if(outcomes.size() != 1)
					return ['task.validation.outcomes.approvalonly.invalid.count', name]
			}
			else {
				if(outcomes.size() == 0) {
					return ['task.validation.outcomes.invalid.count', name]
				}
			}
		}
		else
			if( outcomes?.size() != 0)
				return ['task.validation.outcomes.finishtask.invalid.count', name]
		
		// Outcomes meet spec
		return true
	}
	
	def validateExecution = {
		if(ensureMinimalDirectives()) {
			if(execute == null || execute.keySet().size() == 0) {
				log.debug('Overall directives satisified, no execute directives to validate')
				return true
			}
			else {
				if(execute.containsKey('service')) {
					log.debug('Execute directive located containing a service definition, validating method is defined')
					if(execute.keySet().size() != 2 || !(execute.containsKey('method'))) {
						return ['task.validation.execute.service.invalid.definition', name]
					}
				}
				else {
					if(execute.containsKey('controller')) {
						log.debug('Execute directive located containing a controller definition, validating action (and optional ID) is defined')
						if(execute.keySet().size() == 2) {
							if(!execute.containsKey('action')) {
								return ['task.validation.execute.controller.invalid.definition', name]
							}
						}
						else {
							if(!(execute.keySet().size() == 3 && execute.containsKey('action') && execute.containsKey('id')) )
								return ['task.validation.execute.controller.invalid.definition', name]
						}
					}
					else {
						log.error('Execute directive located that does not specify service or controller')
						return ['task.validation.execute.invalid.definition', name]
					}
				}
			}
		}
		else
			return ['task.validation.invalid.directive.set', name]
			
		// Task meets spec
		true
	}
}
