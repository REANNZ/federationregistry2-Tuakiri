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

			def results = [:]
			def values = []
			def labels = []
			results.values = values
			results.labels = labels

			results.title = "${g.message(code:'fedreg.templates.reports.serviceprovider.logins.title', args:[sp.displayName])} ${day ? day + ' /':''} ${month ? month + ' /':''} $year"
		
			def loginsQuery = "select count(*) as count, hour(date_created) as hour from WayfAccessRecord where spid = :spid and year(dateCreated) = :year"
			def loginsParams = [:]
			loginsParams.spid = sp.id
			loginsParams.year = year

			if(month) {
				loginsQuery = loginsQuery + " and month(dateCreated) = :month"
				loginsParams.month = month
			}
			if(day) {
				loginsQuery = loginsQuery + " and day(dateCreated) = :day"
				loginsParams.day = day
			}
		
			loginsQuery = loginsQuery + " group by hour(date_created)"
		
			def totalLogins = WayfAccessRecord.executeQuery(loginsQuery, loginsParams)
			if(totalLogins.size() == 0)
				results.populated = false
			else
				results.populated = true

			totalLogins.each {
				def val = [:]
				values.add( it[0] )
				labels.add( it[1] )
			}
		
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
				
			def activeIDP = params.activesp as List
		
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
		
			def count = 0, maxLogins = 0, totalLogins
			def results = [:]
			def providers = []
			def bars = []
			def barLabels = []
		
			results.bars = bars
			results.barlabels = barLabels
			results.title = "${g.message(code:'fedreg.templates.reports.serviceprovider.totals.title', args:[sp.displayName])} ${day ? day + ' /':''} ${month ? month + ' /':''} $year"
	
			def loginQuery = "select count(*), idpID from WayfAccessRecord where spID = :spid and year(dateCreated) = :year"
			def loginParams = [:]
			loginParams.spid = sp.id
			loginParams.year = year
		
			if(month) {
				loginQuery = loginQuery + " and month(dateCreated) = :month"
				loginParams.month = month
			}
			if(day) {
				loginQuery = loginQuery + " and day(dateCreated) = :day"
				loginParams.day = day
			}
		
			loginQuery = loginQuery + " group by idpID"
		
			def logins = WayfAccessRecord.executeQuery(loginQuery, loginParams)
			logins.each { login ->
				def idp = IDPSSODescriptor.get(login[1])
				if(idp) {
					def provider = [:]
					provider.name = idp.displayName
					provider.id = idp.id
					providers.add(provider)
		
					if((activeIDP == null || activeIDP.contains(idp.id.toString())) && (!min || login[0] >= min) && (!max || login[0] <= max)) {
						provider.rendered = true
						bars.add(login[0])
						barLabels.add(idp.displayName)
				
						if(maxLogins < login[0])
							maxLogins = login[0]
						count++
					}
					else
						provider.rendered = false
				}
			}

			results.providers = providers.sort{it.get('name').toLowerCase()}
			results.maxlogins = maxLogins
			results.providercount = count
		
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
		
			def target = 1, totalLogins
			def results = [:]
			def providers = []
			def nodes = []
			def links = []
		
			results.nodes = nodes
			results.links = links
			results.title = "${g.message(code:'fedreg.templates.reports.serviceprovider.connectivity.title', args:[sp.displayName])} ${day ? day + ' /':''} ${month ? month + ' /':''} $year"

			def totalQuery = "select count(*) as count from WayfAccessRecord where spid = :spid and year(dateCreated) = :year"
			def totalParams = [:]
			totalParams.spid = sp.id
			totalParams.year = year

			if(month) {
				totalQuery = totalQuery + " and month(dateCreated) = :month"
				totalParams.month = month
			}
			if(day) {
				totalQuery = totalQuery + " and day(dateCreated) = :day"
				totalParams.day = day
			}
			if(activeIDP) {
				totalQuery = totalQuery + " and idpid in (:activeIDP)"
				totalParams.activeIDP = activeIDP
			}
		
			totalLogins = WayfAccessRecord.executeQuery(totalQuery, totalParams)
			
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
					def loginQuery = "select count(*), idpID from WayfAccessRecord where spID != -1 and spID = :spid and year(dateCreated) = :year"
					def loginParams = [:]
					loginParams.spid = sp.id
					loginParams.year = year
				
					if(month) {
						loginQuery = loginQuery + " and month(dateCreated) = :month"
						loginParams.month = month
					}
					if(day) {
						loginQuery = loginQuery + " and day(dateCreated) = :day"
						loginParams.day = day
					}
					loginQuery = loginQuery + " group by idpID"
					
					def logins = WayfAccessRecord.executeQuery(loginQuery, loginParams)
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
