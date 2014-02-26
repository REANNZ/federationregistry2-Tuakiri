package aaf.fr.reporting

import java.text.SimpleDateFormat;

import grails.converters.JSON
import grails.converters.XML
import grails.plugins.federatedgrails.Role
import org.apache.shiro.SecurityUtils

import aaf.fr.foundation.*

/**
 * Provides Federation wide compliance reporting
 *
 * @author Bradley Beddoes
 */
class ComplianceReportsController {
  def cryptoService

  def attributesupport = {}
  def detailedattributesupport = {}
  def idpprovidingattribute = {}
  def compatability = {
    [idpList: IDPSSODescriptor.findAllWhere(active:true), spList:SPSSODescriptor.findAllWhere(active:true)]
  }

  def reportattributeavailability = {

    def results = [
      title: g.message(encodeAs:"HTML", code:'label.idpsummaryattributesupport'),      
      axis: [
        x: g.message(encodeAs:"HTML", code:'label.identityprovider'),
        y: g.message(encodeAs:"HTML", code:'label.supported')
      ],
      categories: [],
      series: [:],
      attributes: [:]
    ]

    AttributeBase.list().each {
      results.attributes.put(it.id, it.name)
    }

    def idpList = IDPSSODescriptor.findAllWhere(active:true).sort{it.displayName.toLowerCase()}
    def categories = AttributeCategory.list()

    idpList.each { i ->     
      results.categories.add([name:i.displayName, automatedRelease:i.autoAcceptServices, url:g.createLink(controller:'identityProvider', action:'show', id:i.id)])
      
      categories.each { c ->
        def attributes = AttributeBase.findAllByCategory(c)
        def catTotal = attributes.size()
        if(!results.series."$c.name") {
          results.series."$c.name" = [:]
          results.series."$c.name".data = []
          results.series."$c.name".max = catTotal
        }

        def supported = i.attributes.findAll{a -> a.base.category == c}.collect{it.base}
        def unsupported = attributes - supported

        results.series."$c.name".data.add([y:supported.size(), color: supported.size() == catTotal ? '#00A201':(c.name == 'Core' ? '#D40B13':'#F47700'), supported:supported.collect{it.id}, unsupported:unsupported.collect{it.id}])
      }
    }
    
    if(params.type == 'csv') {
      response.setHeader("Content-disposition", "attachment; filename=detailedattributesupport.csv")
      response.contentType = "application/vnd.ms-excel"

      def httpout = response.outputStream
      httpout << "Report:, Detailed IdP Attribute Support\n\n"

      results.categories.eachWithIndex { idp, i ->
        httpout << "${idp.name}\n"
        results.series.each { s ->
          httpout << ",${s.key}\n"
          httpout << ",,Supported (${s.value.data[i].supported.size()})\n"
          s.value.data[i].supported.each { attr ->
            httpout << ",,,${results.attributes[attr]}\n"
          }
          httpout << "\n,,Unsupported (${s.value.data[i].unsupported.size()})\n"
          s.value.data[i].unsupported.each { attr ->
            httpout << ",,,${results.attributes[attr]}\n"
          }
          httpout << "\n"
        }
        httpout << "\n"
      }
      httpout.flush()
      httpout.close()
      return
    }

    render results as JSON
  }

  def reportattributecompatibility = {
    def results = [
      title: g.message(encodeAs:"HTML", code:'label.attributecompatibility'),
      minimumRequirements: true, 
      idp:[:],
      sp:[:],     
      series: [
        required:[],
        optional:[]
      ]
    ]

    def identityProvider = IDPSSODescriptor.get(params.idpID)
    if(!identityProvider) {
      log.debug("No IDP matching ID ${params.idp} exists")
      response.sendError(500)
      return  
    }

    def serviceProvider = SPSSODescriptor.get(params.spID)
    if(!serviceProvider) {
      log.debug("No SP matching ID ${params.sp} exists")
      response.sendError(500)
      return  
    }

    results.idp.name = identityProvider.displayName
    results.idp.automatedRelease = identityProvider.autoAcceptServices
    results.idp.url = g.createLink(controller:'identityProvider', action:'show', id:identityProvider.id)

    results.sp.name = serviceProvider.displayName
    results.sp.url = g.createLink(controller:'serviceProvider', action:'show', id:serviceProvider.id)

    // Collate all required attributes across ACS instances defined for this SP
    serviceProvider.attributeConsumingServices.each { acs ->
      acs.requestedAttributes.sort{it.base.name}.each { attr ->
        def supported = identityProvider.attributes.find {it.base == attr.base} != null
        if(attr.isRequired) {
          results.series.required.add([attr.base.name, supported])

          if( !supported )
            results.minimumRequirements = false
        } else {
          results.series.optional.add([attr.base.name, supported])
        }
      }
    }

    render results as JSON
  }

  def reportprovidingattribute = {
    def results = [
      supported: [],
      unsupported: []
    ]

    def attribute = AttributeBase.get(params.attrid)
    results.attribute = "$attribute.name ($attribute.oid)"

    IDPSSODescriptor.findAllWhere(active:true).sort{it.displayName}.each { i->
      if( i.attributes.collect{it.base}.contains(attribute) ) {
        results.supported.add([name:i.displayName, automatedRelease:i.autoAcceptServices, url:g.createLink(controller:'identityProvider', action:'show', id:i.id)])
      } else {
        results.unsupported.add([name:i.displayName, automatedRelease:i.autoAcceptServices, url:g.createLink(controller:'identityProvider', action:'show', id:i.id)])
      }
    }

    render results as JSON
  }

  def causage = {
    def ca = [:]
    Certificate.list().each { cert ->
      try {
        def subject = cryptoService.subject(cert)
        def issuer = cryptoService.issuer(cert)
        
        // External CA only
        if(issuer != subject && !cert.keyInfo.keyDescriptor.disabled ){
          def members = ca.get(issuer)
          if(members) {
            if(!members.contains(cert.keyInfo.keyDescriptor.roleDescriptor.entityDescriptor))
              members.add(cert.keyInfo.keyDescriptor.roleDescriptor.entityDescriptor)
          }
          else {
            ca.put(issuer,[cert.keyInfo.keyDescriptor.roleDescriptor.entityDescriptor])
          }
        }
      } catch (Exception e) {
        log.error "$cert was considered invalid $cert when generating causage report and should be revisited - $e"
      }
    }

    [causage:ca.sort{-it.value.size()}]
  }
}
