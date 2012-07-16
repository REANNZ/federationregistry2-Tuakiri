
public class FoundationSecurityFilters {

  def filters = {

    authn(uri:"/membership/**") {
      before = {
        accessControl { true }
      }
    }

    membership(uri: "/membership/**") {
      before = {
        log.info("secfilter: ALLOWED - [$subject.id]$subject.principal|${request.remoteAddr}|$params.controller/$params.action")
      }
    }

    registration(uri: "/registration/**") {
      before = {
        log.info("secfilter: PUBLIC - unauthenticated|${request.remoteAddr}|$params.controller/$params.action")
      }
    }

  }

}