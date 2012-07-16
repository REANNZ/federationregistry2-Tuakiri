package aaf.fr.foundation

class ErronousStateException extends RuntimeException {
	
	ErronousStateException() { super() }

	ErronousStateException(String message) { super(message) } 

	ErronousStateException(String message, Throwable cause) { super(message, cause) }

	ErronousStateException(Throwable cause) { super(cause) }
	
}