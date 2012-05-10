
public class AdminSecurityFilters {

  def grailsApplication

  def filters = {

    console(controller:"console") {
      before = {
        if(grailsApplication.config.aaf.fr.bootstrap) {
          log.info("secfilter-alert:bootstrap|${request.remoteAddr}|$params.controller/$params.action")
          return  
        }
        else if (!accessControl { permission("federation:globaladministrator") }) {
          log.info("secfilter-alert:[${subject?.id}]${subject?.principal}|${request.remoteAddr}|$params.controller/$params.action")
          response.sendError(404) // Deliberately not 403.
          return
        }
      }
      after = {
        log.info("secfilter:[$subject?.id]$subject?.principal|${request.remoteAddr}|$params.controller/$params.action")
      }
    }
    administration(uri: "/administration/**") {
      before = {
        if(!accessControl { permission("federation:globaladministrator") }) {
          log.info("secfilter-alert:[${subject?.id}]${subject?.principal}|${request.remoteAddr}|$params.controller/$params.action")
          response.sendError(403)
          return
        }
      }
      after = {
        log.info("secfilter:[$subject?.id]$subject?.principal|${request.remoteAddr}|$params.controller/$params.action")
      }
    }

  }

}