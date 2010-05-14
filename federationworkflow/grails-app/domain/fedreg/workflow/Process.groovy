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
			def finish = false
			
			// Ensure all processes have at least 1 task
			if(!val || val.size() == 0) {
				return false
			}
			
			for(v in val) {
				// Ensure all dependencies reference valid tasks
				for(dep in v.dependencies) {
					def task = val.find { t -> t.name.equals(dep) }
					if(!task) {
						return false
					}
				}
				
				// Ensure all rejections start+terminate reference valid tasks
				for(rej in v.rejections.values()) {
					for (s in rej.start) {
						def task = val.find { t -> t.name.equals(s) }
						if(!task) {
							return false
						}
					}
					for (s in rej.terminate) {
						def task = val.find { t -> t.name.equals(s) }
						if(!task) {
							return false
						}
					}
				}
				
				// Ensure all outcome start+terminate reference valid tasks
				for(out in v.outcomes.values()) {
					for (s in out.start) {
						def task = val.find { t -> t.name.equals(s) }
						if(!task) {
							return false
						}
					}
					for (s in out.terminate) {
						def task = val.find { t -> t.name.equals(s) }
						if(!task) {
							return false
						}
					}
				}
				
				if(!finish) {
					finish = v.finishOnThisTask
				}
			}
			
			// Ensure all processes have at least 1 finish task
			if(!finish)
				return false
			
			true
		})
	}
}
