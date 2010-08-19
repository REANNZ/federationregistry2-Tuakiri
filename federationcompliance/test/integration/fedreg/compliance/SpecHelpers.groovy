package fedreg.compliance

import org.codehaus.groovy.runtime.InvokerHelper

import grails.plugin.spock.*
import org.apache.shiro.subject.Subject
import org.apache.shiro.util.ThreadContext
import org.apache.shiro.SecurityUtils

import grails.plugins.nimble.core.UserBase
import grails.plugins.nimble.core.ProfileBase

public class SpecHelpers {
	
	static void setupShiroEnv(def user) {
		def subject = [ getPrincipal: { user.id }, isAuthenticated: { true } ] as Subject
		ThreadContext.put( ThreadContext.SECURITY_MANAGER_KEY, [ getSubject: { subject } ] as SecurityManager )
		SecurityUtils.metaClass.static.getSubject = { subject }
	}
	
	/**
	* Use this method when you plan to perform some meta-programming
	* on a class within a specification
	*/
	static void registerMetaClass(Class clazz, Map savedMetaClasses) {
		// If the class has already been registered, then there's nothing to do.
		if (savedMetaClasses.containsKey(clazz)) return

		// Save the class's current meta class.
		savedMetaClasses[clazz] = clazz.metaClass

		// Create a new EMC for the class and attach it.
		def emc = new ExpandoMetaClass(clazz, true, true)
		emc.initialize()
		InvokerHelper.metaRegistry.setMetaClass(clazz, emc)
	}
	
	/**
	* Use this method in cleanup when you plan to perform some meta-programming
	* on a class. It ensures that any modifications you make will be
	* cleared at the end of the test.
	*/
	static void resetMetaClasses(Map savedMetaClasses) {
		savedMetaClasses.each {clazz, metaClass ->
			GroovySystem.metaClassRegistry.removeMetaClass(clazz)
			GroovySystem.metaClassRegistry.setMetaClass(clazz, metaClass)
		}
	}

}