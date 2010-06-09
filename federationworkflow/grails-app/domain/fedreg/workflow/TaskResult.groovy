package fedreg.workflow

class TaskResult {

    String name
    List initiates = []
    List cancels = []

    static hasMany = [initiates: Task, cancels: Task]
    static belongsTo = [task:Task]

	public String toString() {
		"taskresult:[id:$id, name:$name, initiates:$start, cancels:$terminate]"
	}

}
