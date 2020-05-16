import org.apache.shiro.SecurityUtils

public class AdminSecurityFilters {

  def grailsApplication

  def filters = {

    authn(uri:"/administration/**") {
      before = {
        accessControl { true }
      }
    }

    administration(controller: "adminDashboard|subject|role|attributeBase|attributeCategory|monitorType|organizationType|contactType|adminServiceCategory|CAKeyInfo|samlURI|adminConsole") {
      before = {
        if (!SecurityUtils.subject.isPermitted("federation:globaladministrator")) {
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
        else if (!SecurityUtils.subject.isPermitted("federation:globaladministrator")) {
          log.info("secfilter: DENIED - [${subject?.id}]${subject?.principal}|${request.remoteAddr}|$params.controller/$params.action")
          response.sendError(404) // Deliberately not 403.
          return
        }
        log.info("secfilter: ALLOWED - [$subject.id]$subject.principal|${request.remoteAddr}|$params.controller/$params.action")
      }
    }
  }

}
