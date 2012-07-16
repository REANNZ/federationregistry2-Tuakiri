
public class MetadataSecurityFilters {

  def filters = {

    authn(controller: "metadata", action:"(view|viewall|entity)") {
      before = {
        accessControl { true }
      }
    }

    metadata(controller: "metadata", action:"(view|viewall|entity)") {
      before = {
        log.info("secfilter: ALLOWED - [$subject.id]$subject.principal|${request.remoteAddr}|$params.controller/$params.action")
      }
    }

    metadatapublic(controller: "metadata", action:"(currentminimal|currentminimalnoext|current|all)") {
      before = {
        log.info("secfilter: PUBLIC - unauthenticated|${request.remoteAddr}|$params.controller/$params.action")
      }
    }

    attributefilter(controller:"attributeFilter") {
      before = {
        log.info("secfilter: PUBLIC - unauthenticated|${request.remoteAddr}|$params.controller/$params.action")
      }  
    }

    wayf(controller:"wayf") {
      before = {
        log.info("secfilter: PUBLIC - unauthenticated|${request.remoteAddr}|$params.controller/$params.action")
      }  
    }

  }

}