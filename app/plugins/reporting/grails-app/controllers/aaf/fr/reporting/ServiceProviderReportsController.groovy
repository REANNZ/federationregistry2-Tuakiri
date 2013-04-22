package aaf.fr.reporting

import java.text.SimpleDateFormat;

import grails.converters.JSON
import grails.plugins.federatedgrails.Role
import org.apache.shiro.SecurityUtils

import aaf.fr.foundation.*
import aaf.fr.reporting.ReportingHelpers

/**
 * Provides Service Provider reporting
 *
 * @author Bradley Beddoes
 */
class ServiceProviderReportsController {

  def sessions = {[spList:SPSSODescriptor.listOrderByDisplayName()]}
  def utilization = {[spList:SPSSODescriptor.listOrderByDisplayName()]}
  def demand = {[spList:SPSSODescriptor.listOrderByDisplayName()]}
  def connections = {[spList:SPSSODescriptor.listOrderByDisplayName()]}

  def reportsessions = {
    def startDate, endDate
    (startDate, endDate) = setupDates(params)

    def results = [
      title: g.message(encodeAs:"HTML", code:'label.detailedspsessionsreport'),
      categories: [],
      startdate: [
          day: startDate.get(Calendar.DAY_OF_MONTH),
          month: startDate.get(Calendar.MONTH),
          year: startDate.get(Calendar.YEAR)
      ],
      axis: [
        y: g.message(encodeAs:"HTML", code:'label.sessions')
      ],
      series: [
        overall: [
          name: g.message(encodeAs:"HTML", code:'label.totalsessions')
        ],
      ]
    ]

    def queryParams = [:]
    queryParams.startDate = startDate.time
    queryParams.endDate = endDate.time
    queryParams.spID = params.spID as Long

    def knownDailyTotals = WayfAccessRecord.executeQuery("select count(*), dateCreated from aaf.fr.reporting.WayfAccessRecord where spID = :spID and dateCreated between :startDate and :endDate and robot = false group by year(dateCreated), month(dateCreated), day(dateCreated) order by year(dateCreated), month(dateCreated), day(dateCreated)", queryParams)
    results.series.overall.count = populateDaily(knownDailyTotals, startDate, endDate)

    if(params.type == 'csv') {
      response.setHeader("Content-disposition", "attachment; filename=detailedspsessions.csv")
      response.contentType = "application/vnd.ms-excel"

      def httpout = response.outputStream
      httpout << "Report:, SP ${SPSSODescriptor.get(params.spID).displayName} Sessions\n"
      httpout << "Period:, ${startDate.time}, ${endDate.time}\n\n"
      httpout << "date,sessions\n"
      def curDate = startDate.time
      results.series.overall.count.each {
        httpout << "${curDate},$it\n"
        curDate++
      }
      httpout << "\n"
      httpout.flush()
      httpout.close()
      return
    }

    render results as JSON
  }

  def reportidputilization = {
    def startDate, endDate
    (startDate, endDate) = setupDates(params)

    def results = [
      title: g.message(encodeAs:"HTML", code:'label.detailedsputilizationreport'),
      categories: [],
      startdate: [
          day: startDate.get(Calendar.DAY_OF_MONTH),
          month: startDate.get(Calendar.MONTH),
          year: startDate.get(Calendar.YEAR)
      ],
      axis: [
        y: g.message(encodeAs:"HTML", code:'label.sessions')
      ],
      series: [],
    ]

    def queryParams = [:]
    queryParams.startDate = startDate.time
    queryParams.endDate = endDate.time
    queryParams.spID = params.spID as Long
    List sessionTotals = WayfAccessRecord.executeQuery("select idpID, count(*) from aaf.fr.reporting.WayfAccessRecord where spID = :spID and dateCreated between :startDate and :endDate and robot = false group by idpID", queryParams)

    def requestedIDP = params.get('activeidp') as List
    def idpList = IDPSSODescriptor.listOrderByDisplayName()
    idpList.each { idp ->
      if(idp.functioning()) {
        def sessionTotal // For some reason SessionTotals.find is totally broken in Grails 2.1.4
        for(List st : sessionTotals) {
          if(st.get(0) == idp.id) {
            sessionTotal = st.toArray()
            break
          }
        }

        if(sessionTotal && sessionTotal[1] > 0) {
          def series = [:]
          series.id = idp.id
          series.name = idp.displayName
          
          series.count = sessionTotal[1]
          series.excluded = (!requestedIDP || requestedIDP?.contains(idp.id.toString())) ? false:true
          results.series.add(series)
        }
      }
    }
    results.series.sort { -it.count }

    if(params.type == 'csv') {
      response.setHeader("Content-disposition", "attachment; filename=detailedserviceutilization.csv")
      response.contentType = "application/vnd.ms-excel"

      def httpout = response.outputStream
      httpout << "Report:, Detailed ${SPSSODescriptor.get(params.spID).displayName} Service Utilisation\n"
      httpout << "Period:, ${startDate.time}, ${endDate.time}\n\n"
      httpout << "id,name,sessions\n"
      results.series.each {
        if(!it.excluded)
          httpout << "${it.id},${it.name.replace(',','')},${it.count}\n"
      }
      httpout.flush()
      httpout.close()
      return
    }

    render results as JSON
  }

  def reportdemand = {
    def startDate, endDate
    (startDate, endDate) = setupDates(params)

    def results = [
      title: g.message(encodeAs:"HTML", code:'label.detailedspdemandreport'),
      categories: [],
      startdate: [
          day: startDate.get(Calendar.DAY_OF_MONTH),
          month: startDate.get(Calendar.MONTH),
          year: startDate.get(Calendar.YEAR)
      ],
      axis: [
        y: g.message(encodeAs:"HTML", code:'label.sessions')
      ],
      series: [
      ]
    ]

    def queryParams = [:]
    queryParams.startDate = startDate.time
    queryParams.endDate = endDate.time
    queryParams.spID = params.spID as Long

    def totals = WayfAccessRecord.executeQuery("select hour(dateCreated), count(*) from aaf.fr.reporting.WayfAccessRecord where spID = :spID and dateCreated between :startDate and :endDate and robot = false group by hour(dateCreated) order by hour(dateCreated)", queryParams)
    results.series = totals

    if(params.type == 'csv') {
      response.setHeader("Content-disposition", "attachment; filename=detaileddemand.csv")
      response.contentType = "application/vnd.ms-excel"

      def httpout = response.outputStream
      httpout << "Report:, SP ${SPSSODescriptor.get(params.spID).displayName} Demand\n"
      httpout << "Period:, ${startDate.time}, ${endDate.time}\n\n"
      httpout << "hour,sessions\n"
      results.series.each {
        httpout << "${it[0]},${it[1]}\n"
      }
      httpout.flush()
      httpout.close()
      return
    }

    render results as JSON
  }
  
  def reportconnectivity = {
    // This code survived the cut of protovis because everyone loved it so much in mgmt etc - :( - Uggh.
    // So we maintain protovis for this function only.
    def startDate, endDate
    (startDate, endDate) = setupDates(params)

    if(!params.spID) {
      log.warn "Sp was not present"
      render message(code: 'controllers.fr.generic.namevalue.missing')
      response.setStatus(500)
      return
    }
    
    def sp = SPSSODescriptor.get(params.spID)
    if (!sp) {
      render message(code: 'aaf.fr.foundation.spssoroledescriptor.nonexistant')
      response.setStatus(500)
      return
    }
    if(true || SecurityUtils.subject.isPermitted("federation:management:descriptor:${sp.id}:reporting") || SecurityUtils.subject.isPermitted("federation:management:reporting")) {
      
      def queryParams = [:]
      queryParams.startDate = startDate.time
      queryParams.endDate = endDate.time
      queryParams.spID = params.spID as Long
    
      def target = 1
      def results = [:]
      def services = []
      def nodes = []
      def links = []
    
      results.nodes = nodes
      results.links = links
    
      def totalSessions = WayfAccessRecord.executeQuery("select count(*) as count from WayfAccessRecord where spid = :spID and dateCreated between :startDate and :endDate", queryParams)
      if(totalSessions && totalSessions[0] > 0) {
        results.populated = true
      
        def val = 0
        def spNode = [:]
        spNode.nodeName = sp.displayName
        spNode.group = 1
        nodes.add(spNode)
        
        def sessions = WayfAccessRecord.executeQuery("select count(*), idpID from WayfAccessRecord where spID = :spID and dateCreated between :startDate and :endDate and robot = false group by idpID", queryParams)
        if(sessions) {
          sessions.each { s ->
            def idp = IDPSSODescriptor.get(s[1])
            if(idp) {
              def service = [:]
              service.id = idp.id
              service.name = idp.displayName
              services.add(service)
              service.rendered = true
    
              def node = [:]
              node.nodeName = idp.displayName
              node.group = 2
              nodes.add(node)

              def link = [:]
              link.source = 0
              def value = ((s[0] / totalSessions[0]) * 20)    /* 0 - 20 instead of 0 - 1, makes graph look nicer */
              link.value = value
              link.target = target++

              links.add(link)
            }
          }
        }
        results.services = services.sort{it.get('name').toLowerCase()}
      } else {
        results.populated = false
      }
      render results as JSON
    }
    else {
      log.warn("Attempt to query connections json for $sp by $authenticatedUser was denied, incorrect permission set")
      render message(code: 'help.fr.unauthorized')
      response.setStatus(403)
    }
  }

  private def setupDates(params) {
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd")
    Calendar startDate = formatter.parse(params.startDate).toCalendar()
    Calendar endDate = new GregorianCalendar()
    endDate.setTimeInMillis((formatter.parse(params.endDate) + 1).time - 1)

    [startDate, endDate]    
  }

  // Populates missing zero values so every day has content for zooming
  private def populateDaily(knownDailyTotals, startDate, endDate) {
    def allDailyTotals = []
    def activeDates = [:]
    knownDailyTotals.each {dailyTotal ->
        activeDates.put(dailyTotal[1].clearTime(), dailyTotal[0])
    }

    (startDate.time..endDate.time).each { today ->
      if(activeDates.containsKey(today)) {
        allDailyTotals.add(activeDates[today])
      }
      else {
        allDailyTotals.add(0)
      }
    }
    allDailyTotals
  }
}
