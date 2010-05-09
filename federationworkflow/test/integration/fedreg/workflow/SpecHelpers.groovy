package fedreg.workflow

import grails.plugin.spock.*
import org.apache.shiro.subject.Subject
import org.apache.shiro.util.ThreadContext
import org.apache.shiro.SecurityUtils

import grails.plugins.nimble.core.UserBase
import grails.plugins.nimble.core.ProfileBase

class SpecHelpers {
	
	static def setupShiroEnv() {
		def subject = [ getPrincipal: { "1" }, isAuthenticated: { true } ] as Subject
		ThreadContext.put( ThreadContext.SECURITY_MANAGER_KEY, [ getSubject: { subject } ] as SecurityManager )
		SecurityUtils.metaClass.static.getSubject = { subject }
	}
}