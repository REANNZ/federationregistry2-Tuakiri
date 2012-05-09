
public class FoundationSecurityFilters {

  def filters = {

    membership(uri: "/membership/**") {
      before = {
        accessControl { true }
      }
      after = {
        log.info("secfilter:[$subject.id]$subject.principal|${request.remoteAddr}|$params.controller/$params.action")
      }
    }

    registration(uri: "/registration/**") {
      after = {
        log.info("secfilter:unauthenticated|${request.remoteAddr}|$params.controller/$params.action")
      }
    }

  }

}