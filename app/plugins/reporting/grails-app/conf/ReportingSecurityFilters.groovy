
import org.apache.shiro.SecurityUtils

import aaf.fr.foundation.*

class ReportingSecurityFilters {

  def filters = {

    authn(uri:"/reporting/**") {
      before = {
        accessControl { true }
      }
    }

    compliance(controller: 'complianceReports') {
      before = {
        log.info("secfilter: ALLOWED - [$subject.id]$subject.principal|${request.remoteAddr}|$params.controller/$params.action${params.type == 'csv' ? '|CSV export':''}")
      }
    }

    federationSummaryReport(controller: 'federationReports', action:'reportsummary*') {
      before = {
        log.info("secfilter: ALLOWED - [$subject.id]$subject.principal|${request.remoteAddr}|$params.controller/$params.action${params.type == 'csv' ? '|CSV export':''}")
      }  
    }

    federationReportRestriction(controller: 'federationReports', action:'*', actionExclude: 'reportsummary*') {
      before = {
        def allow = federationGuard(params)
        if(!allow) {
          log.info("secfilter: DENIED - [$subject.id]$subject.principal|${request.remoteAddr}|$params.controller/$params.action${params.type == 'csv' ? '|CSV export':''}")
          response.sendError(403)
          return false
        }
        log.info("secfilter: ALLOWED - [$subject.id]$subject.principal|${request.remoteAddr}|$params.controller/$params.action${params.type == 'csv' ? '|CSV export':''}")
      }  
    }

    idpReportRestriction(controller: 'identityProviderReports') {
      before = {
        def allow = idpGuard(params)
        if(!allow) {
          log.info("secfilter: DENIED - [$subject.id]$subject.principal|${request.remoteAddr}|$params.controller/$params.action${params.type == 'csv' ? '|CSV export':''}")
          response.sendError(403)
          return false
        }
        log.info("secfilter: ALLOWED - [$subject.id]$subject.principal|${request.remoteAddr}|$params.controller/$params.action${params.type == 'csv' ? '|CSV export':''}")
      }
    }

    spReportRestriction(controller: 'serviceProviderReports') {
      before = {
        def allow = spGuard(params)
        if(!allow) {
          log.info("secfilter: DENIED - [$subject.id]$subject.principal|${request.remoteAddr}|$params.controller/$params.action${params.type == 'csv' ? '|CSV export':''}")
          response.sendError(403)
          return false
        }
        log.info("secfilter: ALLOWED - [$subject.id]$subject.principal|${request.remoteAddr}|$params.controller/$params.action${params.type == 'csv' ? '|CSV export':''}")
      }
    }

  }

  private federationGuard(def params) {
    SecurityUtils.subject.isPermitted("federation:management:reporting")
  }

  private idpGuard(def params) {
    // Allows the basic user interface to be loaded
    if(params.action in ['sessions', 'utilization', 'demand', 'connections']) {
      return true
    }

    if(!params.idpID) {
      return false
    }
    
    def idp = IDPSSODescriptor.get(params.idpID)
    if (!idp) {
      return false
    }

    (SecurityUtils.subject.isPermitted("federation:management:reporting") || SecurityUtils.subject.isPermitted("federation:management:descriptor:${idp.id}:reporting"))
  }

  private spGuard(def params) {
    // Allows the basic user interface to be loaded
    if(params.action in ['sessions', 'utilization', 'demand', 'connections']) {
      return true
    }

    if(!params.spID) {
      return false
    }
    
    def sp = SPSSODescriptor.get(params.spID)
    if (!sp) {
      return false
    }

    (SecurityUtils.subject.isPermitted("federation:management:reporting") || SecurityUtils.subject.isPermitted("federation:management:descriptor:${sp.id}:reporting"))
  }
}