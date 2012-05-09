
public class SecurityFilters {

  def grailsApplication

  def filters = {

    all(uri:"/**") {
      before = {
        log.info "request:${request.requestURI}|${request.remoteAddr}"
      }
    }

    welcome(controller: "welcome") {
      after = {
        log.info("secfilter:unauthenticated|${request.remoteAddr}|$params.controller")
      }
    }

    dashboard(controller: "dashboard") {
      before = {
        accessControl { true }
      }
      after = {
        log.info("secfilter:[$subject.id]$subject.principal|${request.remoteAddr}|$params.controller/$params.action")
      }
    }

    bootstrap(controller: "initialBootstrap") {
      before = {
        if( !grailsApplication.config.aaf.fr.bootstrap ) {
          log.info("secfilter-alert:[$subject.id]$subject.principal|${request.remoteAddr}|$params.controller")
          response.sendError(403)
          return
        }
      }
      after = {
        log.info("secfilter:unauthenticated|${request.remoteAddr}|$params.controller")
      }
    }

  }

}
