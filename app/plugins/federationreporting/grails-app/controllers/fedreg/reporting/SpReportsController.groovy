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
class SpReportsController {

	def view = {
		def spList = SPSSODescriptor.listOrderByDisplayName()
		[spList:spList]
	}
	
	def loginsjson = {
		if(!params.id) {
			log.warn "SP was not present"
			render message(code: 'fedreg.controllers.namevalue.missing')
			response.setStatus(500)
			return
		}
		def sp = SPSSODescriptor.get(params.id)
		if (!sp) {
			render message(code: 'fedreg.core.spssoroledescriptor.nonexistant')
			response.setStatus(500)
			return
		}
			
		if(SecurityUtils.subject.isPermitted("descriptor:${sp.id}:reporting") || SecurityUtils.subject.isPermitted("federation:reporting")) {
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
			results.title = "${g.message(code:'fedreg.templates.reports.serviceprovider.logins.title', args:[sp.displayName])} ${day ? day + ' /':''} ${month ? month + ' /':''} $year"
			results.xaxis = g.message(code:'label.hour')
			results.yaxis = g.message(code:'label.logins')
			
			def query = new StringBuilder("select new map(count(*) as c, hour(date_created) as t) from WayfAccessRecord where spid = :spid and year(dateCreated) = :year")
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
		
			query << " ${robots(robot)} group by hour(date_created)"
		
			def loginCounts = WayfAccessRecord.executeQuery(query.toString(), queryParams)
			def (loginTotals, max) = ReportingHelpers.populateTotals(0..23, loginCounts)
			results.max = max
			results.totals = loginTotals
		
			render results as JSON
		}
		else {
			log.warn("Attempt to query logins json for $sp by $authenticatedUser was denied, incorrect permission set")
			render message(code: 'fedreg.help.unauthorized')
			response.setStatus(403)
		}
	}
	
	def sessionsjson = {
		if(!params.id) {
			log.warn "SP was not present"
			render message(code: 'fedreg.controllers.namevalue.missing')
			response.setStatus(500)
			return
		}
		
		def sp = SPSSODescriptor.get(params.id)
		if (!sp) {
			render message(code: 'fedreg.core.spssoroledescriptor.nonexistant')
			response.setStatus(500)
			return
		}
		if(SecurityUtils.subject.isPermitted("descriptor:${sp.id}:reporting")  || SecurityUtils.subject.isPermitted("federation:reporting")) {
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
			results.title = "${g.message(code:'fedreg.templates.reports.serviceprovider.sessions.title', args:[sp.displayName])} ${day ? day + ' /':''} ${month ? month + ' /':''} $year"
			results.yaxis = g.message(code:'label.sessions')
			
			def query
			def queryParams = [:]
			queryParams.spID = sp.id
			queryParams.year = year
					
			if(day) {
				query = "select new map(count(*) as c, hour(dateCreated) as t) from WayfAccessRecord where spID = :spID and year(dateCreated) = :year and month(dateCreated) = :month and day(dateCreated) = :day ${robots(robot)} group by hour(dateCreated)"
				queryParams.month = month
				queryParams.day = day
				results.xaxis = g.message(code:'label.hour')
			} else {
				if(month) {
					query = "select new map(count(*) as c, day(dateCreated) as t) from WayfAccessRecord where spID = :spID and year(dateCreated) = :year and month(dateCreated) = :month ${robots(robot)} group by day(dateCreated)"
					queryParams.month = month
					results.xaxis = g.message(code:'label.day')
				} else {
					query = "select new map(count(*) as c, month(dateCreated) as t) from WayfAccessRecord where spID = :spID and year(dateCreated) = :year ${robots(robot)} group by month(dateCreated)"
					results.xaxis = g.message(code:'label.month')
				}
			}

			def sessionCounts = WayfAccessRecord.executeQuery(query.toString(), queryParams)
			def (sessionTotals, max) = ReportingHelpers.populateTotals(0..23, sessionCounts)
			results.max = max
			results.totals = sessionTotals
				
			render results as JSON
		}
		else {
			log.warn("Attempt to query totals json for $sp by $authenticatedUser was denied, incorrect permission set")
			render message(code: 'fedreg.help.unauthorized')
			response.setStatus(403)
		}
	}
	
	def totalsjson = {
		if(!params.id) {
			log.warn "SP was not present"
			render message(code: 'fedreg.controllers.namevalue.missing')
			response.setStatus(500)
			return
		}
		
		def sp = SPSSODescriptor.get(params.id)
		if (!sp) {
			render message(code: 'fedreg.core.spssoroledescriptor.nonexistant')
			response.setStatus(500)
			return
		}
		if(SecurityUtils.subject.isPermitted("descriptor:${sp.id}:reporting")  || SecurityUtils.subject.isPermitted("federation:reporting")) {
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
			
			def activeIDP = params.activeidp as List
		
			def count = 0, maxSessions = 0, totalSessions = 0
			def results = [:]
			def providers = []
			def values = []
			def valueLabels = []
		
			results.values = values
			results.valuelabels = valueLabels
			results.title = "${g.message(code:'fedreg.templates.reports.serviceprovider.totals.title', args:[sp.displayName])} ${day ? day + ' /':''} ${month ? month + ' /':''} $year"
	
			def query = new StringBuilder("select count(*), idpID from WayfAccessRecord where spID = :spid and year(dateCreated) = :year")
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
		
			query << " ${robots(robot)} group by idpID order by count(idpID) desc"
		
			def sessions = WayfAccessRecord.executeQuery(query.toString(), queryParams)
			if(sessions) {
				results.populated = true
				sessions.each { s ->
					def idp = IDPSSODescriptor.get(s[1])
					if(idp) {
						def provider = [:]
						provider.name = idp.displayName
						provider.id = idp.id
						provider.count = s[0]
						providers.add(provider)
					
						totalSessions = totalSessions + provider.count
		
						if((activeIDP == null || activeIDP.contains(idp.id.toString())) && (!min || provider.count >= min) && (!max || provider.count <= max)) {
							provider.rendered = true
							values.add(provider.count)
							valueLabels.add(idp.displayName)
				
							if(maxSessions < provider.count)
								maxSessions = provider.count
							count++
						}
						else
							provider.rendered = false
					}
				}

				results.providers = providers.sort{it.get('name').toLowerCase()}
				results.maxsessions = maxSessions
				results.totalsessions = totalSessions
			} else {results.populated = false}

			render results as JSON
		}
		else {
			log.warn("Attempt to query totals json for $sp by $authenticatedUser was denied, incorrect permission set")
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
		
		def sp = SPSSODescriptor.get(params.id)
		if (!sp) {
			render message(code: 'fedreg.core.spssoroledescriptor.nonexistant')
			response.setStatus(500)
			return
		}
		if(SecurityUtils.subject.isPermitted("descriptor:${sp.id}:reporting:connections") || SecurityUtils.subject.isPermitted("federation:reporting")) {
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
		
			def activeIDP = params.activeidp as List
		
			def target = 1
			def results = [:]
			def providers = []
			def nodes = []
			def links = []
		
			results.nodes = nodes
			results.links = links
			results.title = "${g.message(code:'fedreg.templates.reports.serviceprovider.connectivity.title', args:[sp.displayName])} ${day ? day + ' /':''} ${month ? month + ' /':''} $year"

			def totalQuery = new StringBuilder("select count(*) as count from WayfAccessRecord where spid = :spid and year(dateCreated) = :year ${robots(robot)}")
			def totalParams = [:]
			totalParams.spid = sp.id
			totalParams.year = year

			if(month) {
				totalQuery << " and month(dateCreated) = :month"
				totalParams.month = month
			}
			if(day) {
				totalQuery << " and day(dateCreated) = :day"
				totalParams.day = day
			}
			if(activeIDP) {
				totalQuery << " and idpid in (:activeIDP)"
				totalParams.activeIDP = activeIDP
			}
		
			def totalSessions = WayfAccessRecord.executeQuery(totalQuery.toString(), totalParams)
			
			if(totalSessions && totalSessions[0] > 0) {
				results.populated = true
			
				def val = 0
				def spNode = [:]
				spNode.nodeName = sp.displayName
				spNode.group = 2
				nodes.add(spNode)
				
				// We remove any SP with a -1 id as this indicates the SP could not be determined at record creation time
				def query = new StringBuilder("select count(*), idpID from WayfAccessRecord where spID != -1 and spID = :spid and year(dateCreated) = :year")
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
				query << " ${robots(robot)} group by idpID"
				
				def sessions = WayfAccessRecord.executeQuery(query.toString(), queryParams)
				if(sessions) {
					sessions.each { s ->
						def idp = IDPSSODescriptor.get(s[1])
						if(idp) {
							def provider = [:]
							provider.id = idp.id
							provider.name = idp.displayName
							providers.add(provider)
			
							if(activeIDP == null || activeIDP.contains(idp.id.toString())) {
								provider.rendered = true
			
								def node = [:]
								node.nodeName = idp.displayName
								node.group = 1
								nodes.add(node)

								def link = [:]
								link.source = 0
								def value = ((s[0] / totalSessions[0]) * 20)		// 0 - 20 instead of 0 - 1, makes graph look nicer
								link.value = value
								link.target = target++

								links.add(link)
							}
							else
								provider.rendered = false
						}
					}
				}
				results.providers = providers.sort{it.get('name').toLowerCase()}
			} else { results.populated = false }
			
			
			render results as JSON
		}
		else {
			log.warn("Attempt to query connections json for $sp by $authenticatedUser was denied, incorrect permission set")
			render message(code: 'fedreg.help.unauthorized')
			response.setStatus(403)
		}
	}
	
	def robots(def robot) {
		robot ? '':'and robot = false'
	}	
}
