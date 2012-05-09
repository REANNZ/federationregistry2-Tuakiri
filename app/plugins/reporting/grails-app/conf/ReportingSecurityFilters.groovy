
import org.apache.shiro.SecurityUtils

import aaf.fr.foundation.*

class ReportingSecurityFilters {

  def filters = {

    authn(uri:"/reporting/**") {
      before = {
        accessControl { true }
      }
      after = {
        log.info("secfilter:[$subject.id]$subject.principal|${request.remoteAddr}|$params.controller/$params.action${params.type == 'csv' ? '|CSV export':''}")
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
   if(SecurityUtils.subject.isPermitted("federation:management:reporting")) {
      log.info("Allowing access for $subject to undertake $params.controller/$params.action")
      return true
    }
   else {
      log.warn("Denying access for $subject to undertake $params.controller/$params.action incorrect permissions")
      return false
    }
  }

  private idpGuard(def params) {
    // Allows the basic user interface to be loaded
    if(params.action in ['sessions', 'utilization', 'demand', 'connections']) {
      log.info("Allowing $subject to load base IdP reporting UI")
      return true
    }

    if(!params.idpID) {
      log.warn "idpID was not present"
      return false
    }
    
    def idp = IDPSSODescriptor.get(params.idpID)
    if (!idp) {
      log.error "No IdP for $params.idpID exists"
      return false
    }

   if(SecurityUtils.subject.isPermitted("federation:management:reporting") || SecurityUtils.subject.isPermitted("federation:management:descriptor:${idp.id}:reporting")) {
      log.info("Allowing access for $subject to undertake $params.controller/$params.action on $idp")
      return true
    }
   else {
      log.warn("Denying access for $subject to undertake $params.controller/$params.action on $idp incorrect permissions")
      return false
    }
  }

  private spGuard(def params) {
    // Allows the basic user interface to be loaded
    if(params.action in ['sessions', 'utilization', 'demand', 'connections']) {
      log.info("Allowing $subject to load base SP reporting UI")
      return true
    }

    if(!params.spID) {
      log.warn "spID was not present"
      return false
    }
    
    def sp = SPSSODescriptor.get(params.spID)
    if (!sp) {
      log.error "No SP for $params.spID exists"
      return false
    }

   if(SecurityUtils.subject.isPermitted("federation:management:reporting") || SecurityUtils.subject.isPermitted("federation:management:descriptor:${sp.id}:reporting")) {
      log.info("Allowing access for $subject to undertake $params.controller/$params.action on $sp")
      return true
    }
   else {
      log.warn("Denying access for $subject to undertake $params.controller/$params.action on $sp incorrect permissions")
      return false
    }
  }
}