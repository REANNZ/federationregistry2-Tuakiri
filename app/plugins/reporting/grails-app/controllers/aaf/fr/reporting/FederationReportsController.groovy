package aaf.fr.reporting

import java.text.SimpleDateFormat;

import grails.converters.JSON
import grails.converters.XML
import grails.plugins.federatedgrails.Role
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


  def summaryregistrations = {
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
      
      count = Organization.executeQuery("select count(*) as count from aaf.fr.foundation.Organization where year(dateCreated) = ?", [year])[0]
      results.series.org.counts.add(count);
      tot = tot + count

      count = IDPSSODescriptor.executeQuery("select count(*) from aaf.fr.foundation.IDPSSODescriptor where year(dateCreated) = ?", [year])[0]
      results.series.idp.counts.add(count);
      
      tot = tot + count

      count = SPSSODescriptor.executeQuery("select count(*) from aaf.fr.foundation.SPSSODescriptor where year(dateCreated) = ?", [year])[0]
      results.series.sp.counts.add(count);
      tot = tot + count

      avg = tot/3
      results.series.summary.total.add(tot)
      results.series.summary.avg.add(avg)
    }

    render results as JSON
  }

  def detailedregistrations = {
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd")
    Date startDate = formatter.parse(params.startDate)
    Date endDate = formatter.parse(params.endDate)

    def results
    use(groovy.time.TimeCategory) {
      results = [
        title: g.message(code:'label.detailedregistrationsreport'),
        axis: [
          x: g.message(code:'label.years'),
          y: g.message(code:'label.totals')
        ],
        startdate: [
            day: startDate.day,
            month: startDate.month,
            year: startDate.year + 1900
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
    knownDailyTotals = Organization.executeQuery("select count(*), dateCreated, id, displayName from aaf.fr.foundation.Organization where dateCreated between ? and ? group by year(dateCreated), month(dateCreated), day(dateCreated) order by year(dateCreated), month(dateCreated), day(dateCreated)", [startDate, endDate])
    results.series.org.counts = populateDaily(knownDailyTotals, startDate, endDate)
    knownDailyTotals.each { daily ->
      def o = [:]
      o.id = daily[2]
      o.displayName = daily[3]
      o.dateCreated = daily[1].format("yyyy-MM-dd")
      o.url = g.createLink(absolute: true, controller:'organization', action:'show', id:o.id)
      results.detail.org.add(o)
    }
    results.detail.org.sort{it.dateCreated}

    knownDailyTotals = IDPSSODescriptor.executeQuery("select count(*), dateCreated, id, displayName from aaf.fr.foundation.IDPSSODescriptor where dateCreated between ? and ? group by year(dateCreated), month(dateCreated), day(dateCreated) order by year(dateCreated), month(dateCreated), day(dateCreated)", [startDate, endDate])
    results.series.idp.counts = populateDaily(knownDailyTotals, startDate, endDate)
    knownDailyTotals.each { daily ->
      def idp = [:]
      idp.id = daily[2]
      idp.displayName = daily[3]
      idp.dateCreated = daily[1].format("yyyy-MM-dd")
      idp.url = g.createLink(absolute: true, controller:'identityProvider', action:'show', id:idp.id)
      results.detail.idp.add(idp)
    }
    results.detail.idp.sort{it.dateCreated}

    knownDailyTotals = SPSSODescriptor.executeQuery("select count(*), dateCreated, id, displayName from aaf.fr.foundation.SPSSODescriptor where dateCreated between ? and ? group by year(dateCreated), month(dateCreated), day(dateCreated) order by year(dateCreated), month(dateCreated), day(dateCreated)", [startDate, endDate])
    results.series.sp.counts = populateDaily(knownDailyTotals, startDate, endDate)
    knownDailyTotals.each { daily ->
      def sp = [:]
      sp.id = daily[2]
      sp.displayName = daily[3]
      sp.dateCreated = daily[1].format("yyyy-MM-dd")
      sp.url = g.createLink(absolute: true, controller:'serviceProvider', action:'show', id:sp.id)
      results.detail.sp.add(sp)
    }
    results.detail.sp.sort{it.dateCreated}

    if(params.type == 'csv') {
      response.setHeader("Content-disposition", "attachment; filename=detailedregistrations.csv")
      response.contentType = "application/vnd.ms-excel"

      def httpout = response.outputStream
      httpout << "Report:, Detailed Registrations\n"
      httpout << "Period:, ${startDate}, ${endDate}\n\n"

      httpout << "Organizations\n"
      httpout << "date,count\n"
      def curDate = startDate
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
      curDate = startDate
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
      curDate = startDate
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

  def summarysessions = {
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
      
      count = WayfAccessRecord.executeQuery("select count(*) as count from aaf.fr.reporting.WayfAccessRecord where year(dateCreated) = ? and robot = false", [year])[0]
      results.series.count.add(count);
    }

    render results as JSON
  }

  def detailedsessions = {
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd")
    Date startDate = formatter.parse(params.startDate)
    Date endDate = formatter.parse(params.endDate)

    def results = [
      title: g.message(code:'label.detailedwaysessionsreport'),
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

    def knownDailyTotals = WayfAccessRecord.executeQuery("select count(*), dateCreated from aaf.fr.reporting.WayfAccessRecord where dateCreated between ? and ? and robot = false group by year(dateCreated), month(dateCreated), day(dateCreated) order by year(dateCreated), month(dateCreated), day(dateCreated)", [startDate, endDate])
    results.series.overall.count = populateDaily(knownDailyTotals, startDate, endDate)

    if(params.type == 'csv') {
      response.setHeader("Content-disposition", "attachment; filename=detailedsessions.csv")
      response.contentType = "application/vnd.ms-excel"

      def httpout = response.outputStream
      httpout << "Report:, DS Sessions\n"
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

  def detaileddemand = {
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd")
    Date startDate = formatter.parse(params.startDate)
    Date endDate = formatter.parse(params.endDate)

    def results = [
      title: g.message(code:'label.detaileddemandreport'),
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

    def totals = WayfAccessRecord.executeQuery("select hour(dateCreated), count(*) from aaf.fr.reporting.WayfAccessRecord where dateCreated between ? and ? and robot = false group by hour(dateCreated) order by hour(dateCreated)", [startDate, endDate])
    results.series = totals

    if(params.type == 'csv') {
      response.setHeader("Content-disposition", "attachment; filename=detaileddemand.csv")
      response.contentType = "application/vnd.ms-excel"

      def httpout = response.outputStream
      httpout << "Report:, Detailed Demand\n"
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

  def detaileddsutilization = {
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd")
    Date startDate = formatter.parse(params.startDate)
    Date endDate = formatter.parse(params.endDate)

    def results = [
      title: g.message(code:'label.detailedwayfnodesessionsreport'),
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
      totals: []
    ]

    def dsNodes = WayfAccessRecord.executeQuery("select distinct(dsHost) from aaf.fr.reporting.WayfAccessRecord");
    dsNodes.each { node ->
      def series = [:]
      series.name = node
      def knownDailyTotals = WayfAccessRecord.executeQuery("select count(*), dateCreated from aaf.fr.reporting.WayfAccessRecord where dsHost='${node}' and dateCreated between ? and ? and robot = false group by year(dateCreated), month(dateCreated), day(dateCreated) order by year(dateCreated), month(dateCreated), day(dateCreated)", [startDate, endDate])
      series.counts = populateDaily(knownDailyTotals, startDate, endDate)
      results.series.add(series)

      def totals = [:]
      totals.name = node
      totals.count = WayfAccessRecord.executeQuery("select count(*) from aaf.fr.reporting.WayfAccessRecord where dsHost='${node}' and dateCreated between ? and ? and robot = false", [startDate, endDate])[0]
      results.totals.add(totals)
    }

    if(params.type == 'csv') {
      response.setHeader("Content-disposition", "attachment; filename=detaileddsutilization.csv")
      response.contentType = "application/vnd.ms-excel"

      def httpout = response.outputStream
      httpout << "Report:, DS Utilization\n"
      httpout << "Period:, ${startDate}, ${endDate}\n\n"
      httpout << "node,date,sessions\n"
      results.series.each {
        httpout << "${it.name}\n"
        def curDate = startDate
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

  def summarysubscribergrowth = {
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
      
      count = Organization.executeQuery("select count(*) as count from aaf.fr.foundation.Organization where year(dateCreated) = ?", [year])[0]
      o = o + count
      results.series.org.counts.add(o)

      count = IDPSSODescriptor.executeQuery("select count(*) from aaf.fr.foundation.IDPSSODescriptor where year(dateCreated) = ?", [year])[0]
      idp = idp + count
      results.series.idp.counts.add(idp)

      count = SPSSODescriptor.executeQuery("select count(*) from aaf.fr.foundation.SPSSODescriptor where year(dateCreated) = ?", [year])[0]
      sp = sp + count
      results.series.sp.counts.add(sp)
    }

    render results as JSON
  }

  def detailedsubscribergrowth = {
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd")
    Date startDate = formatter.parse(params.startDate)
    Date endDate = formatter.parse(params.endDate)

    def results
    use(groovy.time.TimeCategory) {
      results = [
        title: g.message(code:'label.detailedsubscribergrowthreport'),
        axis: [
          x: g.message(code:'label.years'),
          y: g.message(code:'label.totals')
        ],
        startdate: [
            day: startDate.day,
            month: startDate.month,
            year: startDate.year + 1900
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

    def knownDailyTotals
    knownDailyTotals = Organization.executeQuery("select count(*), dateCreated from aaf.fr.foundation.Organization where dateCreated between ? and ? group by year(dateCreated), month(dateCreated), day(dateCreated) order by year(dateCreated), month(dateCreated), day(dateCreated)", [startDate, endDate])
    results.series.org.counts = populateDailyCompound(knownDailyTotals, startDate, endDate)

    knownDailyTotals = IDPSSODescriptor.executeQuery("select count(*), dateCreated from aaf.fr.foundation.IDPSSODescriptor where dateCreated between ? and ? group by year(dateCreated), month(dateCreated), day(dateCreated) order by year(dateCreated), month(dateCreated), day(dateCreated)", [startDate, endDate])
    results.series.idp.counts = populateDailyCompound(knownDailyTotals, startDate, endDate)
    
    knownDailyTotals = SPSSODescriptor.executeQuery("select count(*), dateCreated from aaf.fr.foundation.SPSSODescriptor where dateCreated between ? and ? group by year(dateCreated), month(dateCreated), day(dateCreated) order by year(dateCreated), month(dateCreated), day(dateCreated)", [startDate, endDate])
    results.series.sp.counts = populateDailyCompound(knownDailyTotals, startDate, endDate)

    if(params.type == 'csv') {
      response.setHeader("Content-disposition", "attachment; filename=detailedsubscribergrowth.csv")
      response.contentType = "application/vnd.ms-excel"

      def httpout = response.outputStream
      httpout << "Report:, Detailed Subscriber Growth\n"
      httpout << "Period:, ${startDate}, ${endDate}\n"

      httpout << "Organizations\n"
      httpout << "date,count\n"
      def curDate = startDate
      results.series.org.counts.each {
        httpout << "${curDate},$it\n"
        curDate++
      }
      httpout << "\n"

      httpout << "Identity Providers\n"
      httpout << "date,count\n"
      curDate = startDate
      results.series.idp.counts.each {
        httpout << "${curDate},$it\n"
        curDate++
      }
      httpout << "\n"

      httpout << "Service Providers\n"
      httpout << "date,count\n"
      curDate = startDate
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

  def detailedserviceutilization = {
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd")
    Date startDate = formatter.parse(params.startDate)
    Date endDate = formatter.parse(params.endDate)

    def results = [
      title: g.message(code:'label.detailedserviceutilizationreport'),
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

    def requestedSP = params.get('activesp') as List
    def spList = SPSSODescriptor.listOrderByDisplayName()
    spList.each { sp ->
        def series = [:]
        series.id = sp.id
        series.name = sp.displayName
        def sessionTotal = WayfAccessRecord.executeQuery("select count(*) from aaf.fr.reporting.WayfAccessRecord where spID=${sp.id} and dateCreated between ? and ? and robot = false", [startDate, endDate])
        series.count = sessionTotal[0]
        series.excluded = (!requestedSP || requestedSP?.contains(sp.id.toString())) ? false:true
        results.series.add(series)
    }
    results.series.sort { -it.count }

    if(params.type == 'csv') {
      response.setHeader("Content-disposition", "attachment; filename=detailedserviceutilization.csv")
      response.contentType = "application/vnd.ms-excel"

      def httpout = response.outputStream
      httpout << "Report:, Detailed Service Utilisation\n"
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

  def detailedidputilization = {
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd")
    Date startDate = formatter.parse(params.startDate)
    Date endDate = formatter.parse(params.endDate)

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

    def requestedIdP = params.get('activeidp') as List
    def idPList = IDPSSODescriptor.listOrderByDisplayName()
    idPList.each { idp ->
        def series = [:]
        series.id = idp.id
        series.name = idp.displayName
        def sessionTotal = WayfAccessRecord.executeQuery("select count(*) from aaf.fr.reporting.WayfAccessRecord where idpID=${idp.id} and dateCreated between ? and ? and robot = false", [startDate, endDate])
        series.count = sessionTotal[0]
        series.excluded = (!requestedIdP || requestedIdP?.contains(idp.id.toString())) ? false:true
        results.series.add(series)
    }
    results.series.sort { -it.count }

    if(params.type == 'csv') {
      response.setHeader("Content-disposition", "attachment; filename=detailedidputilization.csv")
      response.contentType = "application/vnd.ms-excel"

      def httpout = response.outputStream
      httpout << "Report:, Detailed IdP Utilisation\n"
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

  // Populates missing total values so every day has content for zooming
  private def populateDailyCompound(knownDailyTotals, startDate, endDate) {
    def allDailyTotals = []
    def activeDates = [:]
    knownDailyTotals.each {dailyTotal ->
        activeDates.put(dailyTotal[1].clearTime(), dailyTotal[0])
    }

    def total = 0
    (startDate..endDate).each { today ->
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
