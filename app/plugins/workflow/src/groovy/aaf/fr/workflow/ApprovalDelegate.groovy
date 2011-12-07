package aaf.fr.workflow

class ApprovalDelegate {

	def task
	
	ApprovalDelegate(Task task, Map approvers) {
		this.task = task
		
		// Process roles
		if(approvers.get('roles')) {
			task.approverRoles.addAll(approvers.get('roles'))
		}
		
		if(approvers.get('role')) {
			task.approverRoles.add(approvers.get('role'))
		}
		
		// Process groups
		if(approvers.get('groups')) {
			task.approverGroups.addAll(approvers.get('groups'))
		}
		
		if(approvers.get('group')) {
			task.approverGroups.add(approvers.get('group'))
		}
		
		// Process users
		if(approvers.get('users')) {
			task.approvers.addAll(approvers.get('users'))
		}
		
		if(approvers.get('user')) {
			task.addToApprovers(approvers.get('user'))
		}
	}
	
	def reject(Map map, Closure closure) {
		def name = map.name
		def description = map.description
		def taskRejection = new TaskRejection(name: name, description: description, task:task)
		closure.delegate = new RejectDelegate(taskRejection)
		closure()
		
		task.rejections.put(name, taskRejection)
	}
}