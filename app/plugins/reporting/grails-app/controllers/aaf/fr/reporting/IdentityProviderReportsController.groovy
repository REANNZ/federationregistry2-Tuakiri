package aaf.fr.reporting

import java.text.SimpleDateFormat;

import grails.converters.JSON
import grails.plugins.federatedgrails.Role
import org.apache.shiro.SecurityUtils

import aaf.fr.foundation.*
import aaf.fr.reporting.ReportingHelpers

/**
 * Provides IdP reporting data
 *
 * @author Bradley Beddoes
 */
class IdentityProviderReportsController {

  def sessions = {[idpList:IDPSSODescriptor.listOrderByDisplayName()]}
  def utilization = {[idpList:IDPSSODescriptor.listOrderByDisplayName()]}
  def demand = {[idpList:IDPSSODescriptor.listOrderByDisplayName()]}
  def connections = {[idpList:IDPSSODescriptor.listOrderByDisplayName()]}

  def reportsessions = {
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd")
    Date startDate = formatter.parse(params.startDate)
    Date endDate = formatter.parse(params.endDate) + 1

    def results = [
      title: g.message(code:'label.detailedidpsessionsreport'),
      categories: [],
      startdate: [
          day: startDate.day,
          month: startDate.month,
          year: startDate.year + 1900
      ],
      axis: [
        y: g.message(code:'label.sessions')
      ],
      series: [
        overall: [
          name: g.message(code:'label.totalsessions')
        ],
      ]
    ]

    def queryParams = [:]
    queryParams.startDate = startDate
    queryParams.endDate = endDate
    queryParams.idpID = params.idpID as Long

    def knownDailyTotals = WayfAccessRecord.executeQuery("select count(*), dateCreated from aaf.fr.reporting.WayfAccessRecord where idpID = :idpID and dateCreated between :startDate and :endDate and robot = false group by year(dateCreated), month(dateCreated), day(dateCreated) order by year(dateCreated), month(dateCreated), day(dateCreated)", queryParams)
    results.series.overall.count = populateDaily(knownDailyTotals, startDate, endDate)

    if(params.type == 'csv') {
      response.setHeader("Content-disposition", "attachment; filename=detailedidpsessions.csv")
      response.contentType = "application/vnd.ms-excel"

      def httpout = response.outputStream
      httpout << "Report:, IDP ${IDPSSODescriptor.get(params.idpID).displayName} Sessions\n"
      httpout << "Period:, ${startDate}, ${endDate}\n\n"
      httpout << "date,sessions\n"
      def curDate = startDate
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

  def reportserviceutilization = {
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd")
    Date startDate = formatter.parse(params.startDate)
    Date endDate = formatter.parse(params.endDate) + 1

    def results = [
      title: g.message(code:'label.detailedidputilizationreport'),
      categories: [],
      startdate: [
          day: startDate.day,
          month: startDate.month,
          year: startDate.year + 1900
      ],
      axis: [
        y: g.message(code:'label.sessions')
      ],
      series: [],
    ]

    def queryParams = [:]
    queryParams.startDate = startDate
    queryParams.endDate = endDate
    queryParams.idpID = params.idpID as Long
    def sessionTotals = WayfAccessRecord.executeQuery("select spID, count(*) from aaf.fr.reporting.WayfAccessRecord where idpID = :idpID and dateCreated between :startDate and :endDate and robot = false group by spID", queryParams)

    def requestedSP = params.get('activesp') as List
    def spList = SPSSODescriptor.listOrderByDisplayName()
    spList.each { sp ->
      if(sp.functioning()) {
        def sessionTotal = sessionTotals.find{it[0] == sp.id}
        if(sessionTotal && sessionTotal[1] > 0) {
          def series = [:]
          series.id = sp.id
          series.name = sp.displayName
          
          series.count = sessionTotal[1]
          series.excluded = (!requestedSP || requestedSP?.contains(sp.id.toString())) ? false:true
          results.series.add(series)
        }
      }
    }
    results.series.sort { -it.count }

    if(params.type == 'csv') {
      response.setHeader("Content-disposition", "attachment; filename=detailedserviceutilization.csv")
      response.contentType = "application/vnd.ms-excel"

      def httpout = response.outputStream
      httpout << "Report:, Detailed ${IDPSSODescriptor.get(params.idpID).displayName} Service Utilisation\n"
      httpout << "Period:, ${startDate}, ${endDate}\n\n"
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
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd")
    Date startDate = formatter.parse(params.startDate)
    Date endDate = formatter.parse(params.endDate) + 1

    def results = [
      title: g.message(code:'label.detailedidpdemandreport'),
      categories: [],
      startdate: [
          day: startDate.day,
          month: startDate.month,
          year: startDate.year + 1900
      ],
      axis: [
        y: g.message(code:'label.sessions')
      ],
      series: [
      ]
    ]

    def queryParams = [:]
    queryParams.startDate = startDate
    queryParams.endDate = endDate
    queryParams.idpID = params.idpID as Long

    def totals = WayfAccessRecord.executeQuery("select hour(dateCreated), count(*) from aaf.fr.reporting.WayfAccessRecord where idpID = :idpID and dateCreated between :startDate and :endDate and robot = false group by hour(dateCreated) order by hour(dateCreated)", queryParams)
    results.series = totals

    if(params.type == 'csv') {
      response.setHeader("Content-disposition", "attachment; filename=detaileddemand.csv")
      response.contentType = "application/vnd.ms-excel"

      def httpout = response.outputStream
      httpout << "Report:, IDP ${IDPSSODescriptor.get(params.idpID).displayName} Demand\n"
      httpout << "Period:, ${startDate}, ${endDate}\n\n"
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

    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd")
    Date startDate = formatter.parse(params.startDate)
    Date endDate = formatter.parse(params.endDate) + 1
    
    // Interceptor approved so we know it exists.
    def idp = IDPSSODescriptor.get(params.idpID)
      
      def queryParams = [:]
      queryParams.startDate = startDate
      queryParams.endDate = endDate
      queryParams.idpID = params.idpID as Long
    
      def activeSP = params.activesp as List
    
      def target = 1
      def results = [:]
      def services = []
      def nodes = []
      def links = []
    
      results.nodes = nodes
      results.links = links
    
      def totalSessions = WayfAccessRecord.executeQuery("select count(*) as count from WayfAccessRecord where idpid = :idpID and dateCreated between :startDate and :endDate", queryParams)
      if(totalSessions && totalSessions[0] > 0) {
        results.populated = true
      
        def val = 0
        def idpNode = [:]
        idpNode.nodeName = idp.displayName
        idpNode.group = 1
        nodes.add(idpNode)
        
        def sessions = WayfAccessRecord.executeQuery("select count(*), spID from WayfAccessRecord where idpID = :idpID and dateCreated between :startDate and :endDate and robot = false group by spID", queryParams)
        if(sessions) {
          sessions.each { s ->
            def sp = SPSSODescriptor.get(s[1])
            if(sp) {
              def service = [:]
              service.id = sp.id
              service.name = sp.displayName
              services.add(service)
      
              if(activeSP == null || activeSP.contains(sp.id.toString())) {
                service.rendered = true
      
                def node = [:]
                node.nodeName = sp.displayName
                node.group = 2
                nodes.add(node)

                def link = [:]
                link.source = 0
                def value = ((s[0] / totalSessions[0]) * 20)    /* 0 - 20 instead of 0 - 1, makes graph look nicer */
                link.value = value
                link.target = target++

                links.add(link)
              }
              else
                service.rendered = false
            }
          }
        }
        results.services = services.sort{it.get('name').toLowerCase()}
      } else {
        results.populated = false
      }
      render results as JSON
    
  }

  // Populates missing zero values so every day has content for zooming
  private def populateDaily(knownDailyTotals, startDate, endDate) {
    def allDailyTotals = []
    def activeDates = [:]
    knownDailyTotals.each {dailyTotal ->
        activeDates.put(dailyTotal[1].clearTime(), dailyTotal[0])
    }

    (startDate..endDate).each { today ->
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
