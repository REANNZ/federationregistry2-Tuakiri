
public class AdminSecurityFilters {

  def grailsApplication

  def filters = {

    authn(uri:"/administration/**") {
      before = {
        accessControl { true }
      }
    }

    administration(uri: "/administration/**") {
      before = {
        if(!accessControl { permission("federation:globaladministrator") }) {
          log.info("secfilter: DENIED - [${subject.id}]${subject.principal}|${request.remoteAddr}|$params.controller/$params.action")
          response.sendError(403)
          return
        }
        log.info("secfilter: ALLOWED - [$subject.id]$subject.principal|${request.remoteAddr}|$params.controller/$params.action")
      }
    }

    console(controller:"console") {
      before = {
        if(grailsApplication.config.aaf.fr.bootstrap) {
          log.info("secfilter: ALERT - bootstrap|${request.remoteAddr}|$params.controller/$params.action")
          return  
        }
        else if (!accessControl { permission("federation:globaladministrator") }) {
          log.info("secfilter: DENIED - [${subject.id}]${subject.principal}|${request.remoteAddr}|$params.controller/$params.action")
          response.sendError(404) // Deliberately not 403.
          return
        }
        log.info("secfilter: ALLOWED - [$subject.id]$subject.principal|${request.remoteAddr}|$params.controller/$params.action")
      }
    }
  }

}