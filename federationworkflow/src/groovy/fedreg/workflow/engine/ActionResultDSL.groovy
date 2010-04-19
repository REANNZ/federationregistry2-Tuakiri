package fedreg.workflow.engine

class ActionResultDSL {

	def actionResultDefinition

	ActionResultDSL(ActionResultDefinition actionResultDefinition) {
		this.actionResultDefinition = actionResultDefinition
	}

	def to(String name) {
		this.to([name])
	}

	def to(List list) {
		actionResultDefinition.nextTasks = list
		//actionResultDefinition.save()
	}

	def toAndCancel(String next, String cancel) {
	    this.toAndCancel([next], [cancel])
	}
	
	def toAndCancel(String name, List list) {
	    this.toAndCancel([name], list)
	}
	
	def toAndCancel(List list, String name) {
	    this.toAndCancel(list, [name])
	}
	
	def toAndCancel(List nextList, List cancelList) {
	    actionResultDefinition.nextTasks = nextList
	    actionResultDefinition.cancelTasks = cancelList
	}

}
