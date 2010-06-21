package org.grails.plugins.console

import grails.converters.JSON
import grails.util.GrailsUtil
import org.springframework.context.ApplicationContextAware
import org.springframework.context.ApplicationContext
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class ConsoleController {
  static Logger logger = LoggerFactory.getLogger(ConsoleService.class)
  def consoleService

  def index = {
    render(view:params.'dev'?'dev':'index')
  }

  def execute = {
    long startTime = System.currentTimeMillis()
    def code = params.code
    def out = new StringWriter()

    def script = null, result = null, output

    try {                              
      session['_grails_console_last_code_'] = code
      result = consoleService.eval(code)
      if (!result.'exception') {       
        out << '<div class="output">'
        out << result.'out'?.encodeAsConsoleOutput()
        out << '</div>'
        out.println '<div class="script-result">' 
        out.println """<span class="exec-time">${System.currentTimeMillis() - startTime} ms</span>"""
        out.println """<span class="exec-res">${result.'returnValue'?.inspect()?.encodeAsConsoleOutput()}</span>"""
        out.println '</div>'
        jsonHeader([success: true] as JSON)
      } else {
        jsonHeader([success: false, runtimeError: true] as JSON)
        out << '<div class="output">'
        out << result.'out'?.encodeAsConsoleOutput()
        out << '</div>'
        out.println '<div class="stacktrace">'
        out.println "Exception ${result.'exception'}"
        out.println "${result.'stacktrace'.encodeAsConsoleOutput()}"
        out.println '</div>'
      }

    } catch (Throwable t) {
      logger.error(t.message, t)
      jsonHeader([success: false, compilationError: true] as JSON)
      out << output?.encodeAsHTML()
      out.println("Compilation Error: ")
      out.println '<div class="stacktrace">'
      GrailsUtil.printSanitizedStackTrace(t, new PrintWriter(out))
      out.println '</div>'
    }
    
    render text: out.toString()
  }
}
