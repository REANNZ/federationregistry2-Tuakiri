package fedreg.workflow

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
		def taskRejection = new TaskRejection(name: map.name, description: map.description, task:task)
		closure.delegate = new RejectDelegate(taskRejection)
		closure()
		
		task.rejections.put(map.name, taskRejection)
	}
}