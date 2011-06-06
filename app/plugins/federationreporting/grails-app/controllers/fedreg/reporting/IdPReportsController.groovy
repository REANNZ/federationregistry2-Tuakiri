package fedreg.reporting

import org.apache.shiro.SecurityUtils

import fedreg.core.*
import grails.plugins.nimble.core.Role
import grails.plugins.nimble.core.LoginRecord

import grails.converters.JSON

/**
 * Provides IdP reporting data
 *
 * @author Bradley Beddoes
 */
class IdPReportsController {

	def view = {
		def idpList = IDPSSODescriptor.listOrderByDisplayName()
		[idpList:idpList]
	}
	
	def loginsjson = {

		if(!params.id) {
			log.warn "IdP was not present"
			render message(code: 'fedreg.controllers.namevalue.missing')
			response.setStatus(500)
			return
		}
		def idp = IDPSSODescriptor.get(params.id)
		if (!idp) {
			render message(code: 'fedreg.core.idpssoroledescriptor.nonexistant')
			response.setStatus(500)
			return
		}
			
		if(SecurityUtils.subject.isPermitted("descriptor:${idp.id}:reporting") || SecurityUtils.subject.isPermitted("federation:reporting")) {
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
			results.title = "${g.message(code:'fedreg.templates.reports.identityprovider.logins.title', args:[idp.displayName])} ${day ? day + ' /':''} ${month ? month + ' /':''} $year"
			results.xaxis = g.message(code:'label.hour')
			results.yaxis = g.message(code:'label.logins')
			
			def query = new StringBuilder("select new map(count(*) as c, hour(date_created) as t) from WayfAccessRecord where idpid = :idpid and year(dateCreated) = :year")
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
		
			query << " ${robots(robot)} group by hour(date_created)"
			
			def loginCounts = WayfAccessRecord.executeQuery(query.toString(), queryParams)
			def (loginTotals, max) = ReportingHelpers.populateTotals(0..23, loginCounts, 0)
			results.max = max
			results.totals = loginTotals
		
			render results as JSON
		}
		else {
			log.warn("Attempt to query logins json for $idp by $authenticatedUser was denied, incorrect permission set")
			render message(code: 'fedreg.help.unauthorized')
			response.setStatus(403)
		}
	}
	
	def sessionsjson = {
		if(!params.id) {
			log.warn "IdP was not present"
			render message(code: 'fedreg.controllers.namevalue.missing')
			response.setStatus(500)
			return
		}
		
		def idp = IDPSSODescriptor.get(params.id)
		if (!idp) {
			render message(code: 'fedreg.core.idpssoroledescriptor.nonexistant')
			response.setStatus(500)
			return
		}
		if(SecurityUtils.subject.isPermitted("descriptor:${idp.id}:reporting")  || SecurityUtils.subject.isPermitted("federation:reporting")) {
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

			def results = [:]
			results.title = "${g.message(code:'fedreg.templates.reports.identityprovider.sessions.title', args:[idp.displayName])} ${day ? day + ' /':''} ${month ? month + ' /':''} $year"		
			results.yaxis = g.message(code:'label.sessions')
			
			def query, queryParams = [:], limit = 0
			queryParams.idpID = idp.id
			queryParams.year = year
			if(day) {
				query = "select new map(count(*) as c, hour(dateCreated) as t) from WayfAccessRecord where idpID = :idpID and year(dateCreated) = :year and month(dateCreated) = :month and day(dateCreated) = :day ${robots(robot)} group by hour(dateCreated)"
				queryParams.month = month
				queryParams.day = day
				results.xaxis = g.message(code:'label.hour')
			} else {
				if(month) {
					def cal = new GregorianCalendar(year,month - 1, 1)
					def currentCal = new GregorianCalendar()
					def currentYear = (cal.get(Calendar.YEAR) == currentCal.get(Calendar.YEAR))
					def currentMonth = (cal.get(Calendar.MONTH) == currentCal.get(Calendar.MONTH))
					if(currentYear && currentMonth) {
						limit = currentCal.get(Calendar.DAY_OF_MONTH)
					}
					
					query = "select new map(count(*) as c, day(dateCreated) as t) from WayfAccessRecord where idpID = :idpID and year(dateCreated) = :year and month(dateCreated) = :month ${robots(robot)} group by day(dateCreated)"
					queryParams.month = month
					results.xaxis = g.message(code:'label.day')
				} else {
					def cal = new GregorianCalendar(year, 0, 1)
					def currentCal = new GregorianCalendar()
					def currentYear = (cal.get(Calendar.YEAR) == currentCal.get(Calendar.YEAR))
					if(currentYear) {
						limit = currentCal.get(Calendar.MONTH) + 1
					}
					
					query = "select new map(count(*) as c, month(dateCreated) as t) from WayfAccessRecord where idpID = :idpID and year(dateCreated) = :year ${robots(robot)} group by month(dateCreated)"	
					results.xaxis = g.message(code:'label.month')
				}
			}

			def sessionCounts = WayfAccessRecord.executeQuery(query.toString(), queryParams)
			def (sessionTotals, max) = ReportingHelpers.populateTotals(year, month, day, sessionCounts, limit)
			results.max = max
			results.totals = sessionTotals
		
			render results as JSON
		}
		else {
			log.warn("Attempt to query totals json for $idp by $authenticatedUser was denied, incorrect permission set")
			render message(code: 'fedreg.help.unauthorized')
			response.setStatus(403)
		}
	}
	
	def totalsjson = {
		if(!params.id) {
			log.warn "IdP was not present"
			render message(code: 'fedreg.controllers.namevalue.missing')
			response.setStatus(500)
			return
		}
		
		def idp = IDPSSODescriptor.get(params.id)
		if (!idp) {
			render message(code: 'fedreg.core.idpssoroledescriptor.nonexistant')
			response.setStatus(500)
			return
		}
		if(SecurityUtils.subject.isPermitted("descriptor:${idp.id}:reporting")  || SecurityUtils.subject.isPermitted("federation:reporting")) {
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
		
			def count = 0, maxSessions = 0, totalSessions = 0
			def results = [:]
			def services = []
			def values = []
			def valueLabels = []
		
			results.title = "${g.message(code:'fedreg.templates.reports.identityprovider.totals.title', args:[idp.displayName])} ${day ? day + ' /':''} ${month ? month + ' /':''} $year"
	
			// We remove any SP with a -1 id as this indicates the SP could not be determined at record creation time
			def query = new StringBuilder("select count(*), spID from WayfAccessRecord where spID != -1 and idpID = :idpid and year(dateCreated) = :year")
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
		
			query << " ${robots(robot)} group by spID order by count(spID) desc"
		
			def serviceSessions = WayfAccessRecord.executeQuery(query.toString(), queryParams)
			if(serviceSessions) {
				results.populated = true
				serviceSessions.each { ss ->
					def sp = SPSSODescriptor.get(ss[1])
					if(sp) {
						def service = [:]
						service.name = sp.displayName
						service.id = sp.id
						service.count = ss[0]
						services.add(service)
						totalSessions = totalSessions + service.count
		
						if((activeSP == null || activeSP.contains(sp.id.toString())) && (!min || service.count >= min) && (!max || service.count <= max)) {
							service.rendered = true
							values.add(service.count)
							valueLabels.add(sp.displayName)
				
							if(maxSessions < service.count)
								maxSessions = service.count
							count++
						}
						else
							service.rendered = false
					}
				}

				results.services = services.sort{it.get('name').toLowerCase()}
				results.maxsessions = maxSessions
				results.totalsessions = totalSessions
				results.servicecount = count
				results.values = values
				results.valuelabels = valueLabels			
			} else { results.populated = false }
		
			render results as JSON
		}
		else {
			log.warn("Attempt to query totals json for $idp by $authenticatedUser was denied, incorrect permission set")
			render message(code: 'fedreg.help.unauthorized')
			response.setStatus(403)
		}
	}
	
	def connectivityjson = {
		if(!params.id) {
			log.warn "IdP was not present"
			render message(code: 'fedreg.controllers.namevalue.missing')
			response.setStatus(500)
			return
		}
		
		def idp = IDPSSODescriptor.get(params.id)
		if (!idp) {
			render message(code: 'fedreg.core.idpssoroledescriptor.nonexistant')
			response.setStatus(500)
			return
		}
		if(SecurityUtils.subject.isPermitted("descriptor:${idp.id}:reporting") || SecurityUtils.subject.isPermitted("federation:reporting")) {
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
		
			def target = 1
			def results = [:]
			def services = []
			def nodes = []
			def links = []
		
			results.nodes = nodes
			results.links = links
			results.title = "${g.message(code:'fedreg.templates.reports.identityprovider.connectivity.title', args:[idp.displayName])} ${day ? day + ' /':''} ${month ? month + ' /':''} $year"

			def totalQuery = new StringBuilder("select count(*) as count from WayfAccessRecord where idpid = :idpid and year(dateCreated) = :year ${robots(robot)}")
			def totalParams = [:]
			totalParams.idpid = idp.id
			totalParams.year = year

			if(month) {
				totalQuery << " and month(dateCreated) = :month"
				totalParams.month = month
			}
			if(day) {
				totalQuery << " and day(dateCreated) = :day"
				totalParams.day = day
			}
			if(activeSP) {
				totalQuery << " and spid in (:activeSP)"
				totalParams.activeSP = activeSP
			}
		
			def totalSessions = WayfAccessRecord.executeQuery(totalQuery.toString(), totalParams)
			if(totalSessions && totalSessions[0] > 0) {
				results.populated = true
			
				def val = 0
				def idpNode = [:]
				idpNode.nodeName = idp.displayName
				idpNode.group = 1
				nodes.add(idpNode)
				
				// We remove any SP with a -1 id as this indicates the SP could not be determined at record creation time
				def query = new StringBuilder("select count(*), spID from WayfAccessRecord where spID != -1 and idpID = :idpid and year(dateCreated) = :year")
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
				query << " ${robots(robot)} group by spID"
				
				def sessions = WayfAccessRecord.executeQuery(query.toString(), queryParams)
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
								def value = ((s[0] / totalSessions[0]) * 20)		/* 0 - 20 instead of 0 - 1, makes graph look nicer */
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
		else {
			log.warn("Attempt to query connections json for $idp by $authenticatedUser was denied, incorrect permission set")
			render message(code: 'fedreg.help.unauthorized')
			response.setStatus(403)
		}
	}
	
	def robots(def robot) {
		robot ? '':'and robot = false'
	}
}
