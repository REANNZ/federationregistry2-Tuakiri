package fedreg.workflow

class ProcessService {
	
	def create(def definition) {
		Binding binding = new Binding()
		binding.process = { name, closure ->
			this.interpret(name, definition, closure)
		}
		
		def script = new GroovyShell().parse(definition)
		script.binding = binding
		script.run()
	}
	
	def interpret(String name, String definition, Closure closure) {
		def processes = Process.findAllByName(name)
		int version = 1
		if (processes) {
			version = processes.size() + 1
		}

		def process = new Process(name: name, processVersion: version, creator: authenticatedUser, definition: definition)
		
		closure.delegate = new ProcessDelegate(process)
		closure()
		
		process.save()
		if(process.hasErrors()) {
			process.errors.each {
				log.error it
			}
		}
	}
	
}