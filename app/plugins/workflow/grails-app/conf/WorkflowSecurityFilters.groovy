
public class WorkflowSecurityFilters {

  def filters = {
    authn(uri:"/workflow/**") {
      before = {
        accessControl { true }
      }
    }

    workflow(uri:"/workflow/**") {
      before = {
        log.info("secfilter: ALLOWED - [$subject.id]$subject.principal|${request.remoteAddr}|$params.controller/$params.action")
      }
    }
  }

}