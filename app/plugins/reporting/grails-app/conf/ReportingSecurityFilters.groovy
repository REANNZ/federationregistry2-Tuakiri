
import org.apache.shiro.SecurityUtils

import aaf.fr.foundation.*

class ReportingSecurityFilters {

  def filters = {

    authn(controller: "(federationReports|complianceReports|identityProviderReports|serviceProviderReports)") {
      before = {
        accessControl { true }
      }
      after = {
        log.info("Completed authenticated access for $subject to $params.controller/$params.action ${params.type == 'csv' ? 'exporting CSV':''}")
      }
    }

    federationReportRestriction(controller: 'federationReports', action:'*', actionExclude: 'reportsummary*') {
      before = {
        def allow = federationGuard(params)
        if(!allow) {
          response.sendError(403)
          return false
        }
      }  
    }

    idpReportRestriction(controller: 'identityProviderReports') {
      before = {
        def allow = idpGuard(params)
        if(!allow) {
          response.sendError(403)
          return false
        }
      }
    }

    spReportRestriction(controller: 'serviceProviderReports') {
      before = {
        def allow = spGuard(params)
        if(!allow) {
          response.sendError(403)
          return false
        }
      }
    }

  }

  private federationGuard(def params) {
   if(SecurityUtils.subject.isPermitted("federation:reporting")) {
      log.info("Allowing access for $subject to undertake $params.controller/$params.action")
      return true
    }
   else {
      log.warn("Denying access for $subject to undertake $params.controller/$params.action incorrect permissions")
      return false
    }
  }

  private idpGuard(def params) {
    if(!params.idpID) {
      log.warn "idpID was not present"
      return false
    }
    
    def idp = IDPSSODescriptor.get(params.idpID)
    if (!idp) {
      log.error "No idp for $params.idpID exists"
      return false
    }

   if(SecurityUtils.subject.isPermitted("federation:management:descriptor:${idp.id}:reporting") || SecurityUtils.subject.isPermitted("federation:reporting")) {
      log.info("Allowing access for $subject to undertake $params.controller/$params.action on $idp")
      return true
    }
   else {
      log.warn("Denying access for $subject to undertake $params.controller/$params.action on $idp incorrect permissions")
      return false
    }
  }

  private spGuard(def params) {
    if(!params.spID) {
      log.warn "spID was not present"
      return false
    }
    
    def sp = SPSSODescriptor.get(params.spID)
    if (!sp) {
      log.error "No idp for $params.spID exists"
    }

   if(SecurityUtils.subject.isPermitted("federation:management:descriptor:${sp.id}:reporting") || SecurityUtils.subject.isPermitted("federation:reporting")) {
      log.info("Allowing access for $subject to undertake $params.controller/$params.action on $sp")
      return true
    }
   else {
      log.warn("Denying access for $subject to undertake $params.controller/$params.action on $sp incorrect permissions")
      return false
    }
  }
}