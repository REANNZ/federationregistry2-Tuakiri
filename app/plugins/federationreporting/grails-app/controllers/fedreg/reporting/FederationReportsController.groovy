package fedreg.reporting

import org.apache.shiro.SecurityUtils

import fedreg.core.*
import grails.plugins.nimble.core.Role
import grails.plugins.nimble.core.LoginRecord

import grails.converters.JSON

/**
 * Provides Federation wide reporting data
 *
 * @author Bradley Beddoes
 */
class FederationReportsController {

	def summary = {}
	def subscribers = {}
	def registrations = {}
	def sessions = {}
	def sessiontotals = {}
	def logins = {}
	def connectivity = {}
	
	def summaryjson = {
		if(SecurityUtils.subject.isPermitted("federation:reporting")) {
			def results = [:]
			def robot = params.robot ? params.robot.toBoolean() : false
			
			// Key object creations
			def minYear = WayfAccessRecord.executeQuery("select min(year(dateCreated)) from WayfAccessRecord")[0] 
			def maxYear = WayfAccessRecord.executeQuery("select max(year(dateCreated)) from WayfAccessRecord")[0] 
			
			if(minYear && maxYear) {
				def sessionValues=[], orgValues =[], idpValues = [], spValues = [], yearLabels = []
				def sessionMax = 0, orgMax = 0, idpMax = 0, spMax = 0
				(minYear..maxYear).each { year ->
					yearLabels.add(year)
					
					// Sessions per year
					def sessionValue = [:]	
					sessionValue.x = year
					def sessionQuery = "select count(*) from fedreg.reporting.WayfAccessRecord where year(dateCreated) = ? ${ReportingHelpers.robots(robot)}"
					def sessionCount = WayfAccessRecord.executeQuery(sessionQuery, [year])[0]
					if(sessionCount) {
						sessionValue.y = sessionCount
						if(sessionMax < sessionCount)
							sessionMax = sessionCount
					} else { sessionValue.y = 0 }
					sessionValues.add(sessionValue)
				
					// Organization creations per year	
					def orgValue = [:]	
					orgValue.x = year
					def orgCount = Organization.executeQuery("select count(*) from fedreg.core.Organization where year(dateCreated) = ?", [year])[0]
					if(orgCount) {
						orgValue.y = orgCount
						if(orgMax < orgCount)
							orgMax = orgCount
					} else { orgValue.y = 0 }
					orgValues.add(orgValue)	
					
					// IdP creations per year
					def idpValue = [:]	
					idpValue.x = year
					def idpCount = IDPSSODescriptor.executeQuery("select count(*) from fedreg.core.IDPSSODescriptor where year(dateCreated) = ?", [year])[0]
					if(idpCount) {
						idpValue.y = idpCount
						if(idpMax < idpCount)
							idpMax = idpCount
					} else { idpValue.y = 0 }
					idpValues.add(idpValue)
					
					// SP creations per year
					def spValue = [:]	
					spValue.x = year
					def spCount = SPSSODescriptor.executeQuery("select count(*) from fedreg.core.SPSSODescriptor where year(dateCreated) = ?", [year])[0]
					if(spCount) {
						spValue.y = spCount
						if(spMax < spCount)
							spMax = spCount
					} else { spValue.y = 0 }
					spValues.add(spValue)
				}
				
				results.sessionvalues = sessionValues
				results.sessionmax = sessionMax
				results.orgvalues = orgValues
				results.orgmax = orgMax
				results.idpvalues = idpValues
				results.idpmax = idpMax
				results.spvalues = spValues
				results.spmax = spMax
				results.yearlabels = yearLabels
			}
			
			render results as JSON
		}
		else {
			log.warn("Attempt to query summary json for federation by $authenticatedUser was denied, incorrect permission set")
			render message(code: 'fedreg.help.unauthorized')
			response.setStatus(403)
		}
	}
	
	def registrationsjson = {
		if(SecurityUtils.subject.isPermitted("federation:reporting")) {
			if(!params.type || !['org', 'idp', 'sp'].contains(params.type)) {
				log.warn "Registration type was not present"
				render message(code: 'fedreg.controllers.namevalue.missing')
				response.setStatus(500)
				return
			}
			
			def i18n, controller, className
			switch(params.type) {
				case 'org': i18n = 'organization'
							controller = 'organization'
							className = "fedreg.core.Organization"
							break
				case 'idp': i18n = 'identityprovider'
							controller = 'IDPSSODescriptor'
							className = "fedreg.core.IDPSSODescriptor"
							break
				case 'sp': i18n = 'serviceprovider'
							controller = 'SPSSODescriptor'
							className = "fedreg.core.SPSSODescriptor"
							break
			}
			
			def year, month, day		
			year = params.int('year')
			if(!year) {
				def cal = Calendar.instance
				year = cal.get(Calendar.YEAR)
			}
			month = params.int('month')
			if(month)
				day = params.int('day')
		
			def results = [:]		
			def title = "fedreg.view.reporting.federation.registrations.${i18n}.period.title"
			results.title = "${g.message(code:"${title}")} ${day ? day + ' /':''} ${month ? month + ' /':''} $year"
			results.yaxis = g.message(code:'label.registrations')
			
			def query, queryTotal, queryParams = [:]
			queryParams.year = year
			if(day) {
				query = "select new map(dateCreated as date, displayName as name, id as id) from ${className} where year(dateCreated) = :year and month(dateCreated) = :month and day(dateCreated) = :day order by dateCreated"
				queryTotal = "select new map(count(*) as c, hour(dateCreated) as t) from ${className} where year(dateCreated) = :year and month(dateCreated) = :month and day(dateCreated) = :day group by hour(dateCreated)"
				queryParams.month = month
				queryParams.day = day
				results.xaxis = g.message(code:'label.hour')
			} else {
				if(month) {
					query = "select new map(dateCreated as date, displayName as name, id as id) from ${className} where year(dateCreated) = :year and month(dateCreated) = :month order by dateCreated"
					queryTotal = "select new map(count(*) as c, day(dateCreated) as t) from ${className} where year(dateCreated) = :year and month(dateCreated) = :month group by day(dateCreated)"
					queryParams.month = month
					results.xaxis = g.message(code:'label.day')
				} else {
					query = "select new map(dateCreated as date, displayName as name, id as id) from ${className} where year(dateCreated) = :year order by dateCreated"
					queryTotal = "select new map(count(*) as c, month(dateCreated) as t) from ${className} where year(dateCreated) = :year group by month(dateCreated)"
					results.xaxis = g.message(code:'label.month')
				}
			}

			
			def registrationCounts = Organization.executeQuery(queryTotal, queryParams)
			def (registrationTotals, max) = ReportingHelpers.populateTotals(year, month, day, registrationCounts)
			results.max = max
			results.totals = registrationTotals
			
			results.registrations = []
			def registrations = Organization.executeQuery(query, queryParams)
			registrations.each { r ->
				def registration = [:]
				registration.date = r.date.format("dd/MM/yyyy HH:mm")
				registration.name = r.name
				registration.id = r.id
				registration.manage = g.createLink(controller:controller, action:'show', id:registration.id)
				
				results.registrations.add(registration)
			}
			render results as JSON
		}
		else {
			log.warn("Attempt to query registration json for federation by $authenticatedUser was denied, incorrect permission set")
			render message(code: 'fedreg.help.unauthorized')
			response.setStatus(403)
		}
	}
	
	def subscribersjson = {
		if(SecurityUtils.subject.isPermitted("federation:reporting")) {
			if(!params.type || !['org', 'idp', 'sp'].contains(params.type)) {
				log.warn "Registration type was not present"
				render message(code: 'fedreg.controllers.namevalue.missing')
				response.setStatus(500)
				return
			}
			
			def i18n, controller, className
			switch(params.type) {
				case 'org': i18n = 'organization'
							controller = 'organization'
							className = "fedreg.core.Organization"
							break
				case 'idp': i18n = 'identityprovider'
							controller = 'IDPSSODescriptor'
							className = "fedreg.core.IDPSSODescriptor"
							break
				case 'sp': i18n = 'serviceprovider'
							controller = 'SPSSODescriptor'
							className = "fedreg.core.SPSSODescriptor"
							break
			}
			
			def year, month, day		
			year = params.int('year')
			if(year) {
				month = params.int('month')
			}
		
			def results = [:]		
			def title = "fedreg.view.reporting.federation.subscribers.${i18n}.period.title"
			results.title = "${g.message(code:"${title}")} ${day ? day + ' /':''} ${month ? month + ' /':''} ${year ?:''}"
			results.yaxis = g.message(code:'label.subscribers')
			
			def queryBaseLine, queryTotal, queryParams = [:], baseCount = 0
			
			if(month) {
				def cal = new GregorianCalendar(year, month, 1) 
				queryParams.year = year
				queryBaseLine = "select count(*) from ${className} where dateCreated < :cal"
				queryTotal = "select new map(count(*) as c, day(dateCreated) as t) from ${className} where year(dateCreated) = :year and month(dateCreated) = :month group by day(dateCreated)"
				queryParams.month = month
				results.xaxis = g.message(code:'label.day')
				
				def subscriberBaseCount = Organization.executeQuery(queryBaseLine, [cal:cal.getTime()])
				baseCount = subscriberBaseCount ? subscriberBaseCount[0] : 0
				
				def subscriberCounts = Organization.executeQuery(queryTotal, queryParams)
				def (subscriberTotals, max) = ReportingHelpers.populateCumulativeTotals(year, month, null, baseCount, subscriberCounts)
				results.max = max
				results.totals = subscriberTotals
				
			} else if(year) {
				queryParams.year = year
				queryBaseLine = "select count(*) from ${className} where year(dateCreated) < :year"
				queryTotal = "select new map(count(*) as c, month(dateCreated) as t) from ${className} where year(dateCreated) = :year group by month(dateCreated)"
				results.xaxis = g.message(code:'label.month')
				
				def subscriberBaseCount = Organization.executeQuery(queryBaseLine, queryParams)
				baseCount = subscriberBaseCount ? subscriberBaseCount[0] : 0
				
				def subscriberCounts = Organization.executeQuery(queryTotal, queryParams)
				def (subscriberTotals, max) = ReportingHelpers.populateCumulativeTotals(year, null, null, baseCount, subscriberCounts)
				results.max = max
				results.totals = subscriberTotals
				
			} else {
				queryTotal = "select new map(count(*) as c, year(dateCreated) as t) from ${className} group by year(dateCreated)"
				results.xaxis = g.message(code:'label.year')
				baseCount = 0
				
				def subscriberCounts = Organization.executeQuery(queryTotal, queryParams)
				def (subscriberTotals, max) = ReportingHelpers.populateCumulativeTotals( 0, subscriberCounts)
				results.max = max
				results.totals = subscriberTotals
			}

			render results as JSON
		}
		else {
			log.warn("Attempt to query registration json for federation by $authenticatedUser was denied, incorrect permission set")
			render message(code: 'fedreg.help.unauthorized')
			response.setStatus(403)
		}
	}
	
	def sessionsjson = {
		if(SecurityUtils.subject.isPermitted("federation:reporting")) {
			def robot = params.robot ? params.robot.toBoolean() : false
			def year, month, day
		
			year = params.int('year')
			if(!year) {
				def cal = Calendar.instance
				year = cal.get(Calendar.YEAR)
			}
			month = params.int('month')
			if(month)
				day = params.int('day')
		
			def results = [:]
			results.title = "${g.message(code:'fedreg.view.reporting.federation.sessions.period.title')} ${day ? day + ' /':''} ${month ? month + ' /':''} $year"
			results.yaxis = g.message(code:'label.sessions')
			
			def query
			def queryParams = [:]
			queryParams.year = year
			if(day) {
				query = "select new map(count(*) as c, hour(dateCreated) as t) from WayfAccessRecord where year(dateCreated) = :year and month(dateCreated) = :month and day(dateCreated) = :day ${ReportingHelpers.robots(robot)} group by hour(dateCreated)"
				queryParams.month = month
				queryParams.day = day
				results.xaxis = g.message(code:'label.hour')
			} else {
				if(month) {
					query = "select new map(count(*) as c, day(dateCreated) as t) from WayfAccessRecord where year(dateCreated) = :year and month(dateCreated) = :month ${ReportingHelpers.robots(robot)} group by day(dateCreated)"
					queryParams.month = month
					results.xaxis = g.message(code:'label.day')
				} else {
					query = "select new map(count(*) as c, month(dateCreated) as t) from WayfAccessRecord where year(dateCreated) = :year ${ReportingHelpers.robots(robot)} group by month(dateCreated)"
					results.xaxis = g.message(code:'label.month')
				}
			}

			def sessionCounts = WayfAccessRecord.executeQuery(query, queryParams)
			def (sessionTotals, max) = ReportingHelpers.populateTotals(year, month, day, sessionCounts)
			results.max = max
			results.totals = sessionTotals
		
			render results as JSON
		}
		else {
			log.warn("Attempt to query session json for federation by $authenticatedUser was denied, incorrect permission set")
			render message(code: 'fedreg.help.unauthorized')
			response.setStatus(403)
		}
	}
	
	def totalsjson = {
		if(SecurityUtils.subject.isPermitted("federation:reporting")) {
			def robot = params.robot ? params.robot.toBoolean() : false
			def year, month, day, min, max
		
			year = params.int('year')
			if(!year) {
				def cal = Calendar.instance
				year = cal.get(Calendar.YEAR)
			}
			month = params.int('month')
			if(month)
				day = params.int('day')
		
			min = params.int('min')	
			max = params.int('max')
			
			def activeSP = params.activesp as List
		
			def maxSessions = 0, totalSessions = 0
			def results = [:]
			def services = []
			def values = []
			def valueLabels = []
		
			results.values = values
			results.valuelabels = valueLabels
			results.title = "${g.message(code:'fedreg.view.reporting.federation.sessiontotals.period.title')} ${day ? day + ' /':''} ${month ? month + ' /':''} $year"
	
			def query = new StringBuilder("select count(*), spID from WayfAccessRecord where year(dateCreated) = :year")
			def queryParams = [:]
			queryParams.year = year
		
			if(month) {
				query << " and month(dateCreated) = :month"
				queryParams.month = month
			}
			if(day) {
				query << " and day(dateCreated) = :day"
				queryParams.day = day
			}
		
			query << " ${ReportingHelpers.robots(robot)} group by spID order by count(spID) desc"
		
			def sessions = WayfAccessRecord.executeQuery(query.toString(), queryParams)
			if(sessions) {
				sessions.each { s ->
					def sp = SPSSODescriptor.get(s[1])
					if(sp) {
						def service = [:]
						service.name = sp.displayName
						service.id = sp.id
						service.count = s[0]
						services.add(service)
					
						totalSessions = totalSessions + service.count
		
						if((activeSP == null || activeSP.contains(sp.id.toString())) && (!min || service.count >= min) && (!max || service.count <= max)) {
							service.rendered = true
							values.add(service.count)
							valueLabels.add(sp.displayName)
				
							if(maxSessions < service.count)
								maxSessions = service.count
						}
						else
							service.rendered = false
					}
				}

				if(services.size() > 10) {
					results.toptenservices = services.sort{it.count}.reverse()[0..9].sort{it.displayName}
					results.remainingservices = services.sort{it.count}.reverse()[10..(services.size() - 1)].sort{it.displayName}
				} else {
					results.toptenservices = services.sort{it.displayName}
				}
				results.maxsessions = maxSessions
				results.totalsessions = totalSessions
		
				results.populated = true
			} else {
				results.populated = false
			}
		
			render results as JSON
		}
		else {
			log.warn("Attempt to query totals json across the federation by $authenticatedUser was denied, incorrect permission set")
			render message(code: 'fedreg.help.unauthorized')
			response.setStatus(403)
		}
	}
	
	def loginsjson = {
		if(SecurityUtils.subject.isPermitted("federation:reporting")) {
			def robot = params.robot ? params.robot.toBoolean() : false
			def year, month, day
			year = params.int('year')
			if(!year) {
				def cal = Calendar.instance
				year = cal.get(Calendar.YEAR)
			}
			month = params.int('month')
			if(month)
				day = params.int('day')

			def results = [:]
			results.xaxis = g.message(code:'label.hour')
			results.yaxis = g.message(code:'label.sessions')

			results.title = "${g.message(code:'fedreg.views.reporting.federation.logins.report.title')} ${day ? day + ' /':''} ${month ? month + ' /':''} $year"
	
			def query = new StringBuilder("select new map(count(*) as c, hour(date_created) as t) from WayfAccessRecord where year(dateCreated) = :year")
			def queryParams = [:]
			queryParams.year = year

			if(month) {
				query << " and month(dateCreated) = :month"
				queryParams.month = month
			}
			if(day) {
				query << " and day(dateCreated) = :day"
				queryParams.day = day
			}
	
			query << " ${ReportingHelpers.robots(robot)} group by hour(date_created)"
	
			def loginCounts = WayfAccessRecord.executeQuery(query.toString(), queryParams)
			def (loginTotals, max) = ReportingHelpers.populateTotals(0..23, loginCounts)
			results.max = max
			results.totals = loginTotals
			
			render results as JSON
		}
		else {
			log.warn("Attempt to query logins json for federation by $authenticatedUser was denied, incorrect permission set")
			render message(code: 'fedreg.help.unauthorized')
			response.setStatus(403)
		}
	}
	
	def connectivityjson = {
		
		if(SecurityUtils.subject.isPermitted("federation:reporting")) {
			def robot = params.robot ? params.robot.toBoolean() : false
			def year, month, day
		
			year = params.int('year')
			if(!year) {
				def cal = Calendar.instance
				year = cal.get(Calendar.YEAR)
			}
			month = params.int('month')
			if(month)
				day = params.int('day')
		
			def activeSP = params.activesp as List
		
			def target
			def results = [:]
			def services = []
			def nodes = []
			def links = []
			def idpMap = [:]
			def spMap = [:]
		
			results.nodes = nodes
			results.links = links
			results.title = "${g.message(code:'fedreg.templates.reports.federation.connectivity.title')} ${day ? day + ' /':''} ${month ? month + ' /':''} $year"
			results.populated = false
			results.idpcount = IDPSSODescriptor.count()
			results.spcount = SPSSODescriptor.count()
			
			IDPSSODescriptor.list().sort{it.displayName}.each { idp ->

				def query = new StringBuilder("select count(*) as count from WayfAccessRecord where idpid = :idpid and year(dateCreated) = :year ${ReportingHelpers.robots(robot)}")
				def queryParams = [:]
				queryParams.idpid = idp.id
				queryParams.year = year

				if(month) {
					query << " and month(dateCreated) = :month"
					queryParams.month = month
				}
				if(day) {
					query << " and day(dateCreated) = :day"
					queryParams.day = day
				}
				if(activeSP) {
					query << " and spid in (:activeSP)"
					queryParams.activeSP = activeSP
				}
				query << " ${ReportingHelpers.robots(robot)}"
		
				def sessions = WayfAccessRecord.executeQuery(query.toString(), queryParams)
				if(sessions && sessions[0] > 0) {
					def val = 0
					def idpNode = [:]
					idpNode.nodeName = idp.displayName
					idpNode.group = 1
					nodes.add(idpNode)
					idpMap.put(idp.id.toLong(), nodes.size() - 1)
				}
			}
			
			SPSSODescriptor.list().sort{it.displayName}.each { sp ->
				
				def query = new StringBuilder("select count(*) as count from WayfAccessRecord where spid = :spid and year(dateCreated) = :year ${ReportingHelpers.robots(robot)}")
				def queryParams = [:]
				queryParams.spid = sp.id
				queryParams.year = year

				if(month) {
					query << " and month(dateCreated) = :month"
					queryParams.month = month
				}
				if(day) {
					query << " and day(dateCreated) = :day"
					queryParams.day = day
				}
				if(activeSP) {
					query << " and spid in (:activeSP)"
					queryParams.activeSP = activeSP
				}
				query << " ${ReportingHelpers.robots(robot)}"
		
				def sessions = WayfAccessRecord.executeQuery(query.toString(), queryParams)
				if(sessions && sessions[0] > 0) {
					def val = 0
					def spNode = [:]
					spNode.nodeName = sp.displayName
					spNode.group = 2
					nodes.add(spNode)
					spMap.put(sp.id.toLong(), nodes.size() - 1)
					
					def service = [:]
					service.id = sp.id
					service.name = sp.displayName
					services.add(service)
				}
			
			}
			
			idpMap.keySet().each { idp ->
				// We remove any SP with a -1 id as this indicates the SP could not be determined at record creation time
				def query = new StringBuilder("select count(*), spID from WayfAccessRecord where spID != -1 and idpID = :idpid and year(dateCreated) = :year")
				def queryParams = [:]
				queryParams.idpid = idp
				queryParams.year = year
		
				if(month) {
					query << " and month(dateCreated) = :month"
					queryParams.month = month
				}
				if(day) {
					query << " and day(dateCreated) = :day"
					queryParams.day = day
				}
				query << " ${ReportingHelpers.robots(robot)} group by spID"
			
				def sessions = WayfAccessRecord.executeQuery(query.toString(), queryParams)
				if(sessions) {
					results.populated = true
					sessions.each { s ->
						def sp = SPSSODescriptor.get(s[1])
						if(sp) {

							if(activeSP == null || activeSP.contains(sp.id.toString())) {
								def link = [:]
								link.source = idpMap.get(idp)
								def value = ( s[0] / 10 )
								link.value = value
								link.target = spMap.get(sp.id)

								links.add(link)
							}
						}
					}
				}
				results.services = services.sort{it.get('name').toLowerCase()}
			}
			render results as JSON
		}
		else {
			log.warn("Attempt to query connections json for federation by $authenticatedUser was denied, incorrect permission set")
			render message(code: 'fedreg.help.unauthorized')
			response.setStatus(403)
		}
	}

}
