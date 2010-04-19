package fedreg.workflow.engine

class ProcessDefinition {

	String name
	String description
	int version // GORM versioning
	int processVersion
	String dsl
	String firstTask
	String uploadedBy
	
	Date dateCreated
	Date lastUpdated

	static hasMany = [tasks: TaskDefinition, instances: ProcessInstance]
	
	static constraints = {
		name(nullable: false)
		description(nullable: true)
		processVersion(min: 1)
		firstTask(nullable: true)
		dsl(nullable: false)
		uploadedBy(nullable: true)
		instances(nullable: true)

		tasks(validator: { val, obj ->
           if (!val.find { it.finishOnThisTask == true }) {
		        obj.errors.rejectValue('tasks', 'finish', [obj.name] as Object[], "The process must have one or more finishing tasks")
   				return false
   		   }
		   
		   return true
		    
		}, nullable: false)
	}
	

  	def getStartTask() {
		return tasks.find { it.name == firstTask}
	}

	def getTask(String name) {
		return tasks.find { it.name == name }
	}

    String toString() {
		return "id="+id+",name="+name+",version="+version+",processVersion="+processVersion+",uploadedBy="+uploadedBy+",tasks="+tasks
	}
}
