package org.grails.plugins.console

import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import grails.util.GrailsUtil
import org.springframework.web.context.request.RequestContextHolder

/**
 * session-scope service that is responsible to bind variables for every request. The following variables are
 * available in any script :
 *  - application
 *  - context
 *  - session
 *  - request
 *  - shell
 */
class ConsoleService implements ApplicationContextAware {
  static Logger logger = LoggerFactory.getLogger(ConsoleService.class)
  static transactional = false, scope = "session"
  ApplicationContext applicationContext
  def grailsApplication
  def shell

  def createShell() {
    shell = new GroovyShell(grailsApplication.classLoader, new Binding(
            'application': grailsApplication,
            'context': applicationContext, 'ctx': applicationContext, 
            'session': RequestContextHolder.requestAttributes.request.session))
        shell.'shell' = shell
  }

  def bindShell() {
    if (!shell) shell = createShell()
    shell.'request' = RequestContextHolder.requestAttributes.request
  }

  def eval(code) {
    if (logger.isTraceEnabled()) logger.trace("eval() - code: $code, shell: $shell")
    bindShell()
    def result = [out: new StringWriter()]
    shell.'out' = new PrintWriter(result.'out')
    try {
      result.'returnValue' = shell.evaluate(code)
    } catch (t) {
      result.'exception' = t; result.'stacktrace' = new StringWriter()
      GrailsUtil.printSanitizedStackTrace(t, new PrintWriter(result.'stacktrace'))
      logger.error "Exception occured while evaluating console script: $t", t
    }
    if (logger.isDebugEnabled()) logger.debug("eval() - code: $code, return: $result")
    return result;
  }

}
