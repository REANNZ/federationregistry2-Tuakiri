package fedreg.workflow

import grails.plugins.nimble.core.UserBase

class Process {

	String name
	String description
	String definition
	
	boolean active
	
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
		
		tasks(validator: {val, obj ->
			obj.validateTasks()
		})
	}
	
	public String toString() {
		"process:[id:$id, name:$name, version:$version]"
	}
	
	def validateTasks = {
		boolean f = false
		
		// Ensure all processes have at least 1 task
		if(tasks == null || tasks.size() == 0) {
			return ['process.validation.tasks.minimum', name]
		}
		
		for(v in tasks) {
			// Ensure all dependencies reference valid tasks
			for(dep in v.dependencies) {
				def task = tasks.find { t -> t.name.equals(dep) }
				if(!task) {
					return ['process.validation.tasks.dependencies.invalid.reference', name, dep]
				}
			}
			
			// Ensure all outcome start+terminate reference valid tasks
			for(out in v.outcomes.values()) {
				for (s in out.start) {
					def task = tasks.find { t -> t.name.equals(s) }
					if(!task) {
						return ['process.validation.tasks.outcomes.invalid.start.reference', name, s]
					}
				}
				for (s in out.terminate) {
					def task = tasks.find { t -> t.name.equals(s) }
					if(!task) {
						return ['process.validation.tasks.outcomes.invalid.terminate.reference', name, s]
					}
				}
			}
			
			// Ensure all rejections start+terminate reference valid tasks
			for(rej in v.rejections.values()) {
				for (s in rej.start) {
					def task = tasks.find { t -> t.name.equals(s) }
					if(!task) {
						return ['process.validation.tasks.rejections.invalid.start.reference', name, s]
					}
				}
				for (s in rej.terminate) {
					def task = tasks.find { t -> t.name.equals(s) }
					if(!task) {
						return ['process.validation.tasks.rejections.invalid.terminate.reference', name, s]
					}
				}
			}
			
			if(!f)
				f = v.finishOnThisTask
		}
		
		// Ensure all processes have at least 1 finish task
		if(!f)
			return ['process.validation.no.finish.task', name]
		
		true
	}
}
