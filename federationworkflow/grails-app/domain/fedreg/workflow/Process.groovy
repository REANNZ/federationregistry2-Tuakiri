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
			def result = true
			val.each {
				it.dependencies.each { dep ->
					def task = val.find { t -> t.name.equals(dep)}
					if(!task) {
						result = false
					}
				}
			}
			if(val.size() < 1) {
				result false
			}
			result
		})
	}
}
