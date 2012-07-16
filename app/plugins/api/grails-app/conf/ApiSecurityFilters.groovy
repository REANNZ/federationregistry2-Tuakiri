
public class ApiSecurityFilters {

  def grailsApplication

  def filters = {

    api(uri: "/api/**") {
      before = {
        log.info("secfilter: PUBLICAPI - unauthenticated|${request.remoteAddr}|$params.controller")
      }
    }
  }

}
