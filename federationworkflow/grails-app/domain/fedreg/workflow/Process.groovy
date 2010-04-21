package fedreg.workflow

import grails.plugins.nimble.core.UserBase

class Process {

	String name
	String description
	String definition
	
	int version
	int processVersion
	
	UserBase creator
	UserBase lastEditor
	
	Date dateCreated
	Date lastUpdated
	
	List tasks
	
	static hasMany = [tasks: Task, instances: ProcessInstance]
	
	static mapping = {
		definition type: "text"
	}

	
	static constraints = {
		name(nullable: false)
		description(nullable: false)
		processVersion(min: 1)

		definition(nullable: false)
		creator(nullable: false)
		lastEditor(nullable: true)
	}
	/*
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
	*/
}
