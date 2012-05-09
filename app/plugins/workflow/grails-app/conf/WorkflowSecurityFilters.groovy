
public class WorkflowSecurityFilters {

  def filters = {
    workflow(uri: "/workflow/**") {
      before = {
        accessControl { true }
      }
      after = {
        log.info("secfilter:[$subject.id]$subject.principal|${request.remoteAddr}|$params.controller/$params.action")
      }
    }
  }

}