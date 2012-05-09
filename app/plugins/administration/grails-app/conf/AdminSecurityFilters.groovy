
public class AdminSecurityFilters {

  def filters = {

    administration(uri: "/administration/**") {
      before = {
        if(!accessControl { permission("federation:globaladministrator") }) {
          log.info("secfilter-alert:[${subject?.id}]${subject?.principal}|${request.remoteAddr}|$params.controller/$params.action")
          response.sendError(403)
          return
        }
      }
      after = {
        log.info("secfilter:[$subject.id]$subject.principal|${request.remoteAddr}|$params.controller/$params.action")
      }
    }

  }

}