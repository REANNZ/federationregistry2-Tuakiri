
public class MetadataSecurityFilters {

  def filters = {

    metadata(controller: "metadata", action:"(view|viewall|entity)") {
      before = {
        accessControl { true }
      }
      after = {
        log.info("secfilter:[$subject.id]$subject.principal|${request.remoteAddr}|$params.controller/$params.action")
      }
    }

    metadatapublic(controller: "metadata", action:"(currentminimal|currentminimalnoext|current|all)") {
      after = {
        log.info("secfilter:unauthenticated|${request.remoteAddr}|$params.controller/$params.action")
      }
    }

    attributefilter(controller:"attributeFilter") {
      after = {
        log.info("secfilter:unauthenticated|${request.remoteAddr}|$params.controller/$params.action")
      }  
    }

    wayf(controller:"wayf") {
      after = {
        log.info("secfilter:unauthenticated|${request.remoteAddr}|$params.controller/$params.action")
      }  
    }

  }

}