
public class SecurityFilters {

  def grailsApplication

  def filters = {

    welcome(controller: "welcome") {
      after = {
        log.info("secfilter: PUBLIC - unauthenticated|${request.remoteAddr}|$params.controller")
      }
    }

    dashboard(controller: "dashboard") {
      before = {
        accessControl { true }
      }
      after = {
        log.info("secfilter: ALLOWED - [$subject.id]$subject.principal|${request.remoteAddr}|$params.controller/$params.action")
      }
    }

    bootstrap(controller: "initialBootstrap") {
      before = {
        if( !grailsApplication.config.aaf.fr.bootstrap ) {
          log.info("secfilter: DENIED - [$subject?.id]$subject?.principal|${request.remoteAddr}|$params.controller")
          response.sendError(404) // Deliberately not 403.
          return
        }
        log.info("secfilter: ALERT - unauthenticated|${request.remoteAddr}|$params.controller/$params.action")
      }
    }
  }

}
