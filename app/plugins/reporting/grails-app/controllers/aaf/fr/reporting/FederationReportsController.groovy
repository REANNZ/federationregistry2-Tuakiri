package aaf.fr.reporting

import java.text.SimpleDateFormat;

import grails.converters.JSON
import grails.converters.XML
import grails.plugins.federatedgrails.Role
import grails.plugins.federatedgrails.SessionRecord
import org.apache.shiro.SecurityUtils

import aaf.fr.foundation.*

/**
 * Provides Federation wide reporting data
 *
 * @author Bradley Beddoes
 */
class FederationReportsController {

  def summary = {}
  def growth = {}
  def registrations = {}
  def sessions = {}
  def idputilization = {}
  def serviceutilization = {}
  def demand = {} 
  def dsutilization = {}
  def exportonly = { [organizations:Organization.list()]}


  def reportsummaryregistrations = {
    def currentCal = new GregorianCalendar()

    def minYear = (Organization.executeQuery("select min(year(dateCreated)) from aaf.fr.foundation.Organization")[0] ?: 2010) - 1
    def maxYear = currentCal.get(Calendar.YEAR)

    def results = [
      title: g.message(code:'label.summaryregistrationsreport'),      
      axis: [
        x: g.message(code:'label.years'),
        y: g.message(code:'label.registrations')
      ],
      categories: [],
      series: [
        org: [
          name: g.message(code:'label.organization'),
          counts: []
        ],
        idp: [
          name: g.message(code:'label.identityprovider'),
          counts: []
        ],
        sp: [
          name: g.message(code:'label.serviceprovider'),
          counts: []
        ],
        summary: [
          name: g.message(code:'label.average'),
          avg: [],
          total: []
        ]
      ]
    ]

    (minYear..maxYear).each { year ->
      def count = 0
      def tot = 0 
      int avg = 0

      results.categories.add(year)
      
      count = Organization.executeQuery("select count(*) as count from aaf.fr.foundation.Organization where year(dateCreated) = ?", [year], [readOnly:true, cache:true])[0]
      results.series.org.counts.add(count);
      tot = tot + count

      count = IDPSSODescriptor.executeQuery("select count(*) from aaf.fr.foundation.IDPSSODescriptor where year(dateCreated) = ?", [year], [readOnly:true, cache:true])[0]
      results.series.idp.counts.add(count);
      
      tot = tot + count

      count = SPSSODescriptor.executeQuery("select count(*) from aaf.fr.foundation.SPSSODescriptor where year(dateCreated) = ?", [year], [readOnly:true, cache:true])[0]
      results.series.sp.counts.add(count);
      tot = tot + count

      avg = tot/3
      results.series.summary.total.add(tot)
      results.series.summary.avg.add(avg)
    }

    render results as JSON
  }

  def reportregistrations = {
    def startDate, endDate
    (startDate, endDate) = setupDates(params)

    def results
    use(groovy.time.TimeCategory) {
      results = [
        title: g.message(code:'label.detailedregistrationsreport'),
        axis: [
          x: g.message(code:'label.years'),
          y: g.message(code:'label.totals')
        ],
        startdate: [
            day: startDate.get(Calendar.DAY_OF_MONTH),
            month: startDate.get(Calendar.MONTH),
            year: startDate.get(Calendar.YEAR)
        ],
        series: [
          org: [
            name: g.message(code:'label.organization'),
            counts: []
          ],
          idp: [
            name: g.message(code:'label.identityprovider'),
            counts: []
          ],
          sp: [
            name: g.message(code:'label.serviceprovider'),
            counts: []
          ]
        ],
        detail: [
          org: [
          ],
          idp: [
          ],
          sp:[
          ]
        ]
      ]
    }

    def knownDailyTotals
    knownDailyTotals = Organization.executeQuery("select count(*), dateCreated from aaf.fr.foundation.Organization where dateCreated between ? and ? group by year(dateCreated), month(dateCreated), day(dateCreated) order by year(dateCreated), month(dateCreated), day(dateCreated)", [startDate.time, endDate.time])
    results.series.org.counts = populateDaily(knownDailyTotals, startDate, endDate)
    
    knownDailyTotals.each { daily ->
      def organizations = Organization.executeQuery("select id, displayName, dateCreated from aaf.fr.foundation.Organization where date(dateCreated) = ?", daily[1])
      organizations.each { org ->
        def o = [:]
        o.id = org[0]
        o.displayName = org[1]
        o.dateCreated = org[2].format( 'yyyy-MM-dd' )
        o.url = g.createLink(absolute: true, controller:'organization', action:'show', id:o.id)
        results.detail.org.add(o)
      }
    }
    results.detail.org.sort{it.dateCreated}

    knownDailyTotals = IDPSSODescriptor.executeQuery("select count(*), dateCreated from aaf.fr.foundation.IDPSSODescriptor where dateCreated between ? and ? group by year(dateCreated), month(dateCreated), day(dateCreated) order by year(dateCreated), month(dateCreated), day(dateCreated)", [startDate.time, endDate.time])
    results.series.idp.counts = populateDaily(knownDailyTotals, startDate, endDate)
    knownDailyTotals.each { daily ->
      def idps = IDPSSODescriptor.executeQuery("select id, displayName, dateCreated from aaf.fr.foundation.IDPSSODescriptor where date(dateCreated) = ?", daily[1])
      idps.each { idp ->
        def i = [:]
        i.id = idp[0]
        i.displayName = idp[1]
        i.dateCreated = idp[2].format( 'yyyy-MM-dd' )
        i.url = g.createLink(absolute: true, controller:'organization', action:'show', id:i.id)
        results.detail.idp.add(i)
      }
    }
    results.detail.idp.sort{it.dateCreated}

    knownDailyTotals = SPSSODescriptor.executeQuery("select count(*), dateCreated from aaf.fr.foundation.SPSSODescriptor where dateCreated between ? and ? group by year(dateCreated), month(dateCreated), day(dateCreated) order by year(dateCreated), month(dateCreated), day(dateCreated)", [startDate.time, endDate.time])
    results.series.sp.counts = populateDaily(knownDailyTotals, startDate, endDate)
    knownDailyTotals.each { daily ->
      def sps = SPSSODescriptor.executeQuery("select id, displayName, dateCreated from aaf.fr.foundation.SPSSODescriptor where date(dateCreated) = ?", daily[1])
      sps.each { sp ->
        def s = [:]
        s.id = sp[0]
        s.displayName = sp[1]
        s.dateCreated = sp[2].format( 'yyyy-MM-dd' )
        s.url = g.createLink(absolute: true, controller:'organization', action:'show', id:s.id)
        results.detail.sp.add(s)
      }
    }
    results.detail.sp.sort{it.dateCreated}

    if(params.type == 'csv') {
      response.setHeader("Content-disposition", "attachment; filename=detailedregistrations.csv")
      response.contentType = "application/vnd.ms-excel"

      def httpout = response.outputStream
      httpout << "Report:, Detailed Registrations\n"
      httpout << "Period:, ${startDate.time}, ${endDate.time}\n\n"

      httpout << "Organizations\n"
      httpout << "date,count\n"
      def curDate = startDate.time
      results.series.org.counts.each {
        httpout << "${curDate},$it\n"
        curDate++
      }
      httpout << "\n"
      httpout << "id,name,created,url\n"
      results.detail.org.each {
        httpout << "${it.id},${it.displayName},${it.dateCreated},${it.url}\n"
      }
      httpout << "\n\n"

      httpout << "Identity Providers\n"
      httpout << "date,count\n"
      curDate = startDate.time
      results.series.idp.counts.each {
        httpout << "${curDate},$it\n" 
        curDate++
      }
      httpout << "\n"
      httpout << "id,name,created,url\n"
      results.detail.idp.each {
        httpout << "${it.id},${it.displayName},${it.dateCreated},${it.url}\n"
      }
      httpout << "\n\n"

      httpout << "Service Providers\n"
      httpout << "date,count\n"
      curDate = startDate.time
      results.series.sp.counts.each {
        httpout << "${curDate},$it\n"
        curDate++
      }
      httpout << "\n"
      httpout << "id,name,created,url\n"
      results.detail.sp.each {
        httpout << "${it.id},${it.displayName},${it.dateCreated},${it.url}\n"
      }
      httpout << "\n\n"

      httpout.flush()
      httpout.close()
      return
    }

    render results as JSON
  }

  def reportsummarysessions = {
    def currentCal = new GregorianCalendar()

    def minYear = (Organization.executeQuery("select min(year(dateCreated)) from aaf.fr.foundation.Organization")[0] ?: 2010) - 1
    def maxYear = currentCal.get(Calendar.YEAR)

    def results = [
      title: g.message(code:'label.summarywaysessionsreport'),
      categories: [],
      axis: [
        x: g.message(code:'label.years'),
        y: g.message(code:'label.sessions')
      ],
      series: [
        count: []
      ]
    ]

    (minYear..maxYear).each { year ->
      def count = 0

      results.categories.add(year)
      results.series.name = g.message(code:'label.sessions')
      
      count = WayfAccessRecord.executeQuery("select count(*) as count from aaf.fr.reporting.WayfAccessRecord where year(dateCreated) = ? and robot = false", [year], [readOnly:true, cache:true])[0]
      results.series.count.add(count);
    }

    render results as JSON
  }

  def reportsessions = {
    def startDate, endDate
    (startDate, endDate) = setupDates(params)

    def results = [
      title: g.message(code:'label.detailedwaysessionsreport'),
      categories: [],
      startdate: [
          day: startDate.get(Calendar.DAY_OF_MONTH),
          month: startDate.get(Calendar.MONTH),
          year: startDate.get(Calendar.YEAR)
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

    def knownDailyTotals = WayfAccessRecord.executeQuery("select count(*), dateCreated from aaf.fr.reporting.WayfAccessRecord where dateCreated between ? and ? and robot = false group by year(dateCreated), month(dateCreated), day(dateCreated) order by year(dateCreated), month(dateCreated), day(dateCreated)", [startDate.time, endDate.time])
    results.series.overall.count = populateDaily(knownDailyTotals, startDate, endDate)

    if(params.type == 'csv') {
      response.setHeader("Content-disposition", "attachment; filename=detailedsessions.csv")
      response.contentType = "application/vnd.ms-excel"

      def httpout = response.outputStream
      httpout << "Report:, DS Sessions\n"
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

  def reportdemand = {
    def startDate, endDate
    (startDate, endDate) = setupDates(params)

    def results = [
      title: g.message(code:'label.detaileddemandreport'),
      categories: [],
      startdate: [
          day: startDate.get(Calendar.DAY_OF_MONTH),
          month: startDate.get(Calendar.MONTH),
          year: startDate.get(Calendar.YEAR)
      ],
      axis: [
        y: g.message(code:'label.sessions')
      ],
      series: [
      ]
    ]

    def totals = WayfAccessRecord.executeQuery("select hour(dateCreated), count(*) from aaf.fr.reporting.WayfAccessRecord where dateCreated between ? and ? and robot = false group by hour(dateCreated) order by hour(dateCreated)", [startDate.time, endDate.time])
    results.series = totals

    if(params.type == 'csv') {
      response.setHeader("Content-disposition", "attachment; filename=detaileddemand.csv")
      response.contentType = "application/vnd.ms-excel"

      def httpout = response.outputStream
      httpout << "Report:, Detailed Demand\n"
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

  def reportdsutilization = {
    def startDate, endDate
    (startDate, endDate) = setupDates(params)

    def results = [
      title: g.message(code:'label.detailedwayfnodesessionsreport'),
      categories: [],
      startdate: [
          day: startDate.get(Calendar.DAY_OF_MONTH),
          month: startDate.get(Calendar.MONTH),
          year: startDate.get(Calendar.YEAR)
      ],
      axis: [
        y: g.message(code:'label.sessions')
      ],
      series: [],
      totals: []
    ]

    def dsNodes = WayfAccessRecord.executeQuery("select distinct(dsHost) from aaf.fr.reporting.WayfAccessRecord");
    dsNodes.each { node ->
      def series = [:]
      series.name = node
      def knownDailyTotals = WayfAccessRecord.executeQuery("select count(*), dateCreated from aaf.fr.reporting.WayfAccessRecord where dsHost=? and dateCreated between ? and ? and robot = false group by year(dateCreated), month(dateCreated), day(dateCreated) order by year(dateCreated), month(dateCreated), day(dateCreated)", [node, startDate.time, endDate.time])
      series.counts = populateDaily(knownDailyTotals, startDate, endDate)
      results.series.add(series)

      def totals = [:]
      totals.name = node
      totals.count = WayfAccessRecord.executeQuery("select count(*) from aaf.fr.reporting.WayfAccessRecord where dsHost=? and dateCreated between ? and ? and robot = false", [node, startDate.time, endDate.time])[0]
      results.totals.add(totals)
    }

    if(params.type == 'csv') {
      response.setHeader("Content-disposition", "attachment; filename=detaileddsutilization.csv")
      response.contentType = "application/vnd.ms-excel"

      def httpout = response.outputStream
      httpout << "Report:, DS Utilization\n"
      httpout << "Period:, ${startDate.time}, ${endDate.time}\n\n"
      httpout << "node,date,sessions\n"
      results.series.each {
        httpout << "${it.name}\n"
        def curDate = startDate.time
        it.counts.each {
          httpout << ",${curDate},$it\n"
          curDate++
        }
        httpout << "\n"
      }
      httpout.flush()
      httpout.close()
      return
    }
    
    render results as JSON
  }

  def reportsummarysubscribergrowth = {
    def currentCal = new GregorianCalendar()

    def minYear = (Organization.executeQuery("select min(year(dateCreated)) from aaf.fr.foundation.Organization")[0] ?: 2010) - 1
    def maxYear = currentCal.get(Calendar.YEAR)

    def results = [
      title: g.message(code:'label.summarysubscribergrowthreport'),
      categories: [],
      axis: [
        x: g.message(code:'label.years'),
        y: g.message(code:'label.totals')
      ],
      series: [
        org: [
          name: g.message(code:'label.organization'),
          counts: []
        ],
        idp: [
          name: g.message(code:'label.identityprovider'),
          counts: []
        ],
        sp: [
          name: g.message(code:'label.serviceprovider'),
          counts: []
        ]
      ]
    ]

    def o = 0
    def idp = 0
    def sp = 0
    def count = 0
    (minYear..maxYear).each { year ->
      results.categories.add(year)
      
      count = Organization.executeQuery("select count(*) as count from aaf.fr.foundation.Organization where year(dateCreated) = ?", [year], [readOnly:true, cache:true])[0]
      o = o + count
      results.series.org.counts.add(o)

      count = IDPSSODescriptor.executeQuery("select count(*) from aaf.fr.foundation.IDPSSODescriptor where year(dateCreated) = ?", [year], [readOnly:true, cache:true])[0]
      idp = idp + count
      results.series.idp.counts.add(idp)

      count = SPSSODescriptor.executeQuery("select count(*) from aaf.fr.foundation.SPSSODescriptor where year(dateCreated) = ?", [year], [readOnly:true, cache:true])[0]
      sp = sp + count
      results.series.sp.counts.add(sp)
    }

    render results as JSON
  }

  def reportsubscribergrowth = {
    def startDate, endDate
    (startDate, endDate) = setupDates(params)

    def results
    use(groovy.time.TimeCategory) {
      results = [
        title: g.message(code:'label.detailedsubscribergrowthreport'),
        axis: [
          x: g.message(code:'label.years'),
          y: g.message(code:'label.totals')
        ],
        startdate: [
            day: startDate.get(Calendar.DAY_OF_MONTH),
            month: startDate.get(Calendar.MONTH),
            year: startDate.get(Calendar.YEAR)
        ],
        series: [
          org: [
            name: g.message(code:'label.organization'),
            counts: []
          ],
          idp: [
            name: g.message(code:'label.identityprovider'),
            counts: []
          ],
          sp: [
            name: g.message(code:'label.serviceprovider'),
            counts: []
          ]
        ]
      ]
    }

    def knownDailyTotals, previousCount
    previousCount = Organization.executeQuery("select count(*) from aaf.fr.foundation.Organization where dateCreated < ?", [startDate.time])
    knownDailyTotals = Organization.executeQuery("select count(*), dateCreated from aaf.fr.foundation.Organization where dateCreated between ? and ? group by year(dateCreated), month(dateCreated), day(dateCreated) order by year(dateCreated), month(dateCreated), day(dateCreated)", [startDate.time, endDate.time])
    results.series.org.counts = populateDailyCompound(previousCount[0], knownDailyTotals, startDate, endDate)

    previousCount = IDPSSODescriptor.executeQuery("select count(*) from aaf.fr.foundation.IDPSSODescriptor where dateCreated < ?", [startDate.time])
    knownDailyTotals = IDPSSODescriptor.executeQuery("select count(*), dateCreated from aaf.fr.foundation.IDPSSODescriptor where dateCreated between ? and ? group by year(dateCreated), month(dateCreated), day(dateCreated) order by year(dateCreated), month(dateCreated), day(dateCreated)", [startDate.time, endDate.time])
    results.series.idp.counts = populateDailyCompound(previousCount[0], knownDailyTotals, startDate, endDate)
    
    previousCount = SPSSODescriptor.executeQuery("select count(*) from aaf.fr.foundation.SPSSODescriptor where dateCreated < ?", [startDate.time])
    knownDailyTotals = SPSSODescriptor.executeQuery("select count(*), dateCreated from aaf.fr.foundation.SPSSODescriptor where dateCreated between ? and ? group by year(dateCreated), month(dateCreated), day(dateCreated) order by year(dateCreated), month(dateCreated), day(dateCreated)", [startDate.time, endDate.time])
    results.series.sp.counts = populateDailyCompound(previousCount[0], knownDailyTotals, startDate, endDate)

    if(params.type == 'csv') {
      response.setHeader("Content-disposition", "attachment; filename=detailedsubscribergrowth.csv")
      response.contentType = "application/vnd.ms-excel"

      def httpout = response.outputStream
      httpout << "Report:, Detailed Subscriber Growth\n"
      httpout << "Period:, ${startDate.time}, ${endDate.time}\n"

      httpout << "Organizations\n"
      httpout << "date,count\n"
      def curDate = startDate.time
      results.series.org.counts.each {
        httpout << "${curDate},$it\n"
        curDate++
      }
      httpout << "\n"

      httpout << "Identity Providers\n"
      httpout << "date,count\n"
      curDate = startDate.time
      results.series.idp.counts.each {
        httpout << "${curDate},$it\n"
        curDate++
      }
      httpout << "\n"

      httpout << "Service Providers\n"
      httpout << "date,count\n"
      curDate = startDate.time
      results.series.sp.counts.each {
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
    def startDate, endDate
    (startDate, endDate) = setupDates(params)

    def results = [
      title: g.message(code:'label.detailedserviceutilizationreport'),
      categories: [],
      startdate: [
          day: startDate.get(Calendar.DAY_OF_MONTH),
          month: startDate.get(Calendar.MONTH),
          year: startDate.get(Calendar.YEAR)
      ],
      axis: [
        y: g.message(code:'label.sessions')
      ],
      series: [],
    ]

    def requestedSP = params.get('activesp') as List
    def spList = SPSSODescriptor.list()
    spList.each { sp ->
        def sessionTotal = WayfAccessRecord.executeQuery("select count(*) from aaf.fr.reporting.WayfAccessRecord where spID=? and dateCreated between ? and ? and robot = false", [sp.id, startDate.time, endDate.time])
        if(sessionTotal[0] > 0) {
          def series = [:]
          series.id = sp.id
          series.name = sp.displayName
          
          series.count = sessionTotal[0]
          series.excluded = (!requestedSP || requestedSP?.contains(sp.id.toString())) ? false:true
          results.series.add(series)
        }
    }
    results.series.sort { -it.count }

    if(params.type == 'csv') {
      response.setHeader("Content-disposition", "attachment; filename=detailedserviceutilization.csv")
      response.contentType = "application/vnd.ms-excel"

      def httpout = response.outputStream
      httpout << "Report:, Detailed Service Utilisation\n"
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

  def reportidputilization = {
    def startDate, endDate
    (startDate, endDate) = setupDates(params)

    def results = [
      title: g.message(code:'label.detailedidputilizationreport'),
      categories: [],
      startdate: [
          day: startDate.get(Calendar.DAY_OF_MONTH),
          month: startDate.get(Calendar.MONTH),
          year: startDate.get(Calendar.YEAR)
      ],
      axis: [
        y: g.message(code:'label.sessions')
      ],
      series: [],
    ]

    def requestedIdP = params.get('activeidp') as List
    def idPList = IDPSSODescriptor.list()
    idPList.each { idp ->
        
        def sessionTotal = WayfAccessRecord.executeQuery("select count(*) from aaf.fr.reporting.WayfAccessRecord where idpID=? and dateCreated between ? and ? and robot = false", [idp.id, startDate.time, endDate.time])
        if(sessionTotal[0] > 0) {
          def series = [:]
          series.id = idp.id
          series.name = idp.displayName
          series.count = sessionTotal[0]
          series.excluded = (!requestedIdP || requestedIdP?.contains(idp.id.toString())) ? false:true
          results.series.add(series)
        }
    }
    results.series.sort { -it.count }

    if(params.type == 'csv') {
      response.setHeader("Content-disposition", "attachment; filename=detailedidputilization.csv")
      response.contentType = "application/vnd.ms-excel"

      def httpout = response.outputStream
      httpout << "Report:, Detailed IdP Utilisation\n"
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

  // Provides data for AAF 'box' reports per executive requirements.
  def reportserviceutilizationbreakdown = {
    def startDate, endDate
    (startDate, endDate) = setupDates(params)

    response.setHeader("Content-disposition", "attachment; filename=serviceutilizationbreakdown.csv")
    response.contentType = "application/vnd.ms-excel"
    def httpout = response.outputStream
    httpout << "Report:, Detailed Service Utilisation with IdP Breakdown\n"
    httpout << "Period:, ${startDate.time}, ${endDate.time}\n\n"

    def queryParams = [:]
    queryParams.startDate = startDate.time
    queryParams.endDate = endDate.time
    queryParams.robot = false
    queryParams.orgID = params.excludeorg as Long
    def querySessionTotal = "select count(*) from WayfAccessRecord war, RoleDescriptor rd where war.spID = rd.id and rd.organization.id != :orgID and war.dateCreated between :startDate and :endDate and war.robot = :robot"
    def sessionTotal = WayfAccessRecord.executeQuery(querySessionTotal, queryParams)[0]
    httpout << "Total Sessions in Period:, ${sessionTotal}\n\n"

    def query = new StringBuilder("select count(*), war.spID from WayfAccessRecord war, RoleDescriptor rd where war.spID = rd.id and rd.organization.id != :orgID and war.dateCreated between :startDate and :endDate and war.robot = :robot group by war.spID order by count(war.spID) desc")
    def spSessions = WayfAccessRecord.executeQuery(query.toString(), queryParams)

    httpout << "Session details for active services\n\n"
    spSessions.each { s ->
      def sp = SPSSODescriptor.get(s[1])
      if(sp && !excludeTestUAT(sp.displayName, params.excludetestuat))  {
        def sessions = s[0]
        httpout <<  "Service,${sp.displayName}\n"
        httpout <<  ",Owner,${sp.organization.name.replace(',','')},${sp.organization.displayName.replace(',','')}\n"
        httpout <<  ",Sessions,${sessions}\n"
        httpout <<  ",% Federated Sessions,${(sessions/sessionTotal) * 100}%\n\n"
        httpout <<  ",Top $params.idpcount sources\n,,,Sessions,% Service Sessions\n"

        def queryIdP = new StringBuilder("select count(*), idpID from WayfAccessRecord where spID = :spid and dateCreated between :startDate and :endDate and robot = :robot group by idpID order by count(idpID) desc")
        def queryIdPParams = [:]
        queryIdPParams.spid = sp.id
        queryIdPParams.startDate = startDate.time
        queryIdPParams.endDate = endDate.time
        queryIdPParams.robot = false

        def idpSessions = WayfAccessRecord.executeQuery(queryIdP.toString(), queryIdPParams, [max:params.idpcount])
        def remainingSessions = sessions
        idpSessions.each { idps ->
          def idp = IDPSSODescriptor.get(idps[1])
          if(idp) {
            def idpSessionCount = idps[0]
            remainingSessions = remainingSessions - idpSessionCount
            httpout << ",,${idp.displayName.replace(',','')},${idpSessionCount},${(idpSessionCount/sessions) * 100}%\n"
          }
        }
        httpout << ",,Other,${remainingSessions},${(remainingSessions/sessions) * 100}%\n\n\n"
      }
    }

    httpout.flush()
    httpout.close()
  }

  def reportinternalsessions = {
    def startDate, endDate
    (startDate, endDate) = setupDates(params)

    def results = [
      title: g.message(code:'label.detailedfrsessionsreport', default:"Federation Registry Sessions"),
      categories: [],
      startdate: [
          day: startDate.get(Calendar.DAY_OF_MONTH),
          month: startDate.get(Calendar.MONTH),
          year: startDate.get(Calendar.YEAR)
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

    def knownDailyTotals = WayfAccessRecord.executeQuery("select count(*), dateCreated from SessionRecord where dateCreated between ? and ? group by year(dateCreated), month(dateCreated), day(dateCreated) order by year(dateCreated), month(dateCreated), day(dateCreated)", [startDate.time, endDate.time])
    results.series.overall.count = populateDaily(knownDailyTotals, startDate, endDate)

    render results as JSON
  }

  private def setupDates(params) {
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd")
    Calendar startDate = formatter.parse(params.startDate).toCalendar()
    Calendar endDate = new GregorianCalendar()
    endDate.setTimeInMillis((formatter.parse(params.endDate) + 1).time - 1)

    [startDate, endDate]    
  }

  private def excludeTestUAT(name, exclude) {
    if(name.toLowerCase().contains('test') || name.toLowerCase().contains('tst') || name.toLowerCase().contains('dev') || name.toLowerCase().contains('uat'))
      exclude
    else
      false
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

  // Populates missing total values so every day has content for zooming
  private def populateDailyCompound(previousCount, knownDailyTotals, startDate, endDate) {

    def allDailyTotals = []
    def activeDates = [:]
    knownDailyTotals.each {dailyTotal ->
        activeDates.put(dailyTotal[1].clearTime(), dailyTotal[0])
    }

    def total = previousCount
    (startDate.time..endDate.time).each { today ->
      if(activeDates.containsKey(today)) {
        total = total + activeDates[today]
        allDailyTotals.add(total)
      }
      else {
        allDailyTotals.add(total)
      }
    }
    allDailyTotals
  }
}
