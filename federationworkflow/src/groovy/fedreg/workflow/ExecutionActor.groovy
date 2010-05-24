package fedreg.workflow

import groovyx.gpars.actor.*

import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.SessionHolder;

class ExecutionActor extends DynamicDispatchActor {
	
	def processService
	def taskService
	
	/*
	Actors allow for a messaging-based concurrency model, built from independent active objects that exchange messages and 
	have no mutable shared state. Actors can help developers avoid issues like deadlocks, live-locks or starvation, so typical 
	for shared memory, while leveraging the multi-core nature of today's hardware. A nice wrap-up of the key concepts behind 
	actors was written recently by Ruben Vermeersch. Actors guarantee that always at most one thread processes the actor's 
	body at a time and also under the covers the memory gets synchronized each time a thread gets assigned to an actor so 
	the actor's state can be safely modified by code in the body without any other extra (synchronization or locking) effort . 
	Ideally actor's code should never be invoked directly from outside so all the code of the actor class can only be executed 
	by the thread handling the last received message and so all the actor's code is implicitly thread-safe . If any of the 
	actor's methods is allowed to be called by other objects directly, the thread-safety guarantee for the actor's 
	code and state are no longer valid .
	*/
	
	/* 
	There is explicitly no checking going on here for ensuring the correct values are sent to the actor.
	If we can't get it right internally what hope do we have??
	*/
	
	void onMessage(List message) {
		
		def action = message.get(0)
		
		println action
		
		switch(action) {
			case ExecutionAction.INITIATE:

					println 'here'
					def processInstanceID = message.get(1)
					def taskID = message.get(2)
				
					taskService.initiate(processInstanceID, taskID)
				
				stop()
				break
			case ExecutionAction.APPROVALREQUIRED:
				def taskInstanceID = message.get(1)
				taskService.requestApproval(taskInstanceID)
				break
			case ExecutionAction.APPROVALREJECT:
				def taskInstanceID = message.get(1)
				def rejection = message.get(2)
				def reasoning = message.get(3)
				taskService.reject(taskInstanceID, rejection, reasoning)
				break
			case ExecutionAction.EXECUTE:
				def taskInstanceID = message.get(1)
				taskService.execute(taskInstanceID)
				break
			case ExecutionAction.FINALIZE:
				def taskInstanceID = message.get(1)
				def outcome = message.get(2)
				def reasoning = message.get(3)
				taskService.finalize(taskInstanceID, outcome, reasoning)
				break
			default:
				throw new RuntimeException("${action} --- ExecutionActor default.. WTF.. TODO")
		}
		
    }
	
}

public enum ExecutionAction {
	INITIATE,
	APPROVALREQUIRED,
	APPROVALREQUESTED,
	APPROVALREJECT,
	EXECUTE,
	FINALIZE
}