package aaf.fr.workflow

class ErronousWorkflowStateException extends RuntimeException {
	
	ErronousWorkflowStateException() { super() }

	ErronousWorkflowStateException(String message) { super(message) } 

	ErronousWorkflowStateException(String message, Throwable cause) { super(message, cause) }

	ErronousWorkflowStateException(Throwable cause) { super(cause) }
	
}