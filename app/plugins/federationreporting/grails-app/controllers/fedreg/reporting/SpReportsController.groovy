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
			def year, month, day, min, max
			year = params.int('year')
			if(!year) {
				def cal = Calendar.instance
				year = cal.get(Calendar.YEAR)
			}
			month = params.int('month')
			if(month)
				day = params.int('day')

			def maxLogins = 0
			def results = [:]
			def logins = []
			results.logins = logins

			results.title = "${g.message(code:'fedreg.templates.reports.serviceprovider.logins.title', args:[sp.displayName])} ${day ? day + ' /':''} ${month ? month + ' /':''} $year"
		
			def loginsQuery = new StringBuilder("select count(*) as count, hour(date_created) as hour from WayfAccessRecord where spid = :spid and year(dateCreated) = :year")
			def loginsParams = [:]
			loginsParams.spid = sp.id
			loginsParams.year = year

			if(month) {
				loginsQuery << " and month(dateCreated) = :month"
				loginsParams.month = month
			}
			if(day) {
				loginsQuery << " and day(dateCreated) = :day"
				loginsParams.day = day
			}
		
			loginsQuery << " group by hour(date_created)"
		
			def totalLogins = WayfAccessRecord.executeQuery(loginsQuery.toString(), loginsParams)
			if(totalLogins.size() == 0)
				results.populated = false
			else
				results.populated = true

			totalLogins.each {
				def login = [:]
				login.count = it[0]
				login.hour = it[1]
				
				if(maxLogins < login.count)
					maxLogins = login.count
				
				logins.add(login)
			}
			results.maxlogins = maxLogins
		
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
			def year, month, day
		
			year = params.int('year')
			if(!year) {
				def cal = Calendar.instance
				year = cal.get(Calendar.YEAR)
			}
			month = params.int('month')
			if(month)
				day = params.int('day')
		
			def count = 0, maxLogins = 0, totalLogins
			def results = [:]
			def sessions = []
		
			results.sessions = sessions
			results.title = "${g.message(code:'fedreg.templates.reports.serviceprovider.sessions.title', args:[sp.displayName])} ${day ? day + ' /':''} ${month ? month + ' /':''} $year"
			
			def sessionsQuery
			def sessionsParams = [:]
			if(day) {
				sessionsQuery = "select count(*), hour(dateCreated) from WayfAccessRecord where spID = :spID and year(dateCreated) = :year and month(dateCreated) = :month and day(dateCreated) = :day group by hour(dateCreated)"
				sessionsParams.spID = sp.id
				sessionsParams.year = year
				sessionsParams.month = month
				sessionsParams.day = day
			} else {
				if(month) {
					sessionsQuery = "select count(*), day(dateCreated) from WayfAccessRecord where spID = :spID and year(dateCreated) = :year and month(dateCreated) = :month group by day(dateCreated)"
					sessionsParams.spID = sp.id
					sessionsParams.year = year
					sessionsParams.month = month
				} else {
					sessionsQuery = "select count(*), month(dateCreated) from WayfAccessRecord where spID = :spID and year(dateCreated) = :year group by month(dateCreated)"
					sessionsParams.spID = sp.id
					sessionsParams.year = year
				}
			}

			def sessionCount = WayfAccessRecord.executeQuery(sessionsQuery, sessionsParams)
			sessionCount.each { s ->
				def session = [:]
				session.count = s[0]
				
				if(maxLogins < session.count)
					maxLogins = session.count
				
				session.date = s[1]
				
				sessions.add(session)
			}

			if(sessionCount.size() > 0)
				results.populated = true
			else
				results.populated = false
				
			results.maxlogins = maxLogins
		
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
	
			def sessionQuery = new StringBuilder("select count(*), idpID from WayfAccessRecord where spID = :spid and year(dateCreated) = :year")
			def queryParams = [:]
			queryParams.spid = sp.id
			queryParams.year = year
		
			if(month) {
				sessionQuery << " and month(dateCreated) = :month"
				queryParams.month = month
			}
			if(day) {
				sessionQuery << " and day(dateCreated) = :day"
				queryParams.day = day
			}
		
			sessionQuery << " group by idpID order by count(idpID) desc"
		
			def sessions = WayfAccessRecord.executeQuery(sessionQuery.toString(), queryParams)
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
		
			if(count > 0)
				results.populated = true
			else
				results.populated = false
		
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

			def totalQuery = new StringBuilder("select count(*) as count from WayfAccessRecord where spid = :spid and year(dateCreated) = :year")
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
		
			def totalLogins = WayfAccessRecord.executeQuery(totalQuery.toString(), totalParams)
			
			if(totalLogins[0] > 0) {
				results.populated = true
			
				def val = 0
				if(sp) {
					def node = [:]
					node.nodeName = sp.displayName
					node.group = 2
					nodes.add(node)
				}
					// We remove any SP with a -1 id as this indicates the SP could not be determined at record creation time
					def loginQuery = new StringBuilder("select count(*), idpID from WayfAccessRecord where spID != -1 and spID = :spid and year(dateCreated) = :year")
					def loginParams = [:]
					loginParams.spid = sp.id
					loginParams.year = year
				
					if(month) {
						loginQuery << " and month(dateCreated) = :month"
						loginParams.month = month
					}
					if(day) {
						loginQuery << " and day(dateCreated) = :day"
						loginParams.day = day
					}
					loginQuery << " group by idpID"
					
					def logins = WayfAccessRecord.executeQuery(loginQuery.toString(), loginParams)
					logins.each { login ->
						def idp = IDPSSODescriptor.get(login[1])
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
								def value = ((login[0] / totalLogins[0]) * 20)		// 0 - 20 instead of 0 - 1, makes graph look nicer
								link.value = value
								link.target = target++

								links.add(link)
							}
							else
								provider.rendered = false
						}
					}

			} else {
				results.populated = false
			}
			
			results.providers = providers.sort{it.get('name').toLowerCase()}
			render results as JSON
		}
		else {
			log.warn("Attempt to query connections json for $sp by $authenticatedUser was denied, incorrect permission set")
			render message(code: 'fedreg.help.unauthorized')
			response.setStatus(403)
		}
	}
	
}
