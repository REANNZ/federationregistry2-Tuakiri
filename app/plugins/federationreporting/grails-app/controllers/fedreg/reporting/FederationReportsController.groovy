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
	def sessiontotals = {}
	def logins = {}
	
	def totalsjson = {
		if(SecurityUtils.subject.isPermitted("federation:reporting")) {
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
		
			def count = 0, maxLogins = 0, totalLogins = 0
			def results = [:]
			def services = []
			def bars = []
			def barLabels = []
		
			results.bars = bars
			results.barlabels = barLabels
			results.title = "${g.message(code:'fedreg.view.reporting.federation.services.period.title')} ${day ? day + ' /':''} ${month ? month + ' /':''} $year"
	
			def loginQuery = "select count(*), spID from WayfAccessRecord where year(dateCreated) = :year"
			def loginParams = [:]
			loginParams.year = year
		
			if(month) {
				loginQuery = loginQuery + " and month(dateCreated) = :month"
				loginParams.month = month
			}
			if(day) {
				loginQuery = loginQuery + " and day(dateCreated) = :day"
				loginParams.day = day
			}
		
			loginQuery = loginQuery + " group by spID order by count(spID) desc"
		
			def logins = WayfAccessRecord.executeQuery(loginQuery, loginParams)
			logins.each { login ->
				
				def sp = SPSSODescriptor.get(login[1])
				if(sp) {
					totalLogins = totalLogins + login[0]
					
					def service = [:]
					service.name = sp.displayName
					service.id = sp.id
					service.count = login[0]
					services.add(service)
		
					if((activeSP == null || activeSP.contains(sp.id.toString())) && (!min || login[0] >= min) && (!max || login[0] <= max)) {
						service.rendered = true
						bars.add(login[0])
						barLabels.add(sp.displayName)
				
						if(maxLogins < login[0])
							maxLogins = login[0]
						count++
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
			results.maxlogins = maxLogins
			results.servicecount = count
			results.totallogins = totalLogins
		
			if(count > 0)
				results.populated = true
			else
				results.populated = false
		
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

			results.title = "${g.message(code:'fedreg.views.reporting.federation.logins.report.title')} ${day ? day + ' /':''} ${month ? month + ' /':''} $year"
	
			def loginsQuery = "select count(*) as count, hour(date_created) as hour from WayfAccessRecord where year(dateCreated) = :year"
			def loginsParams = [:]
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
			log.warn("Attempt to query logins json for federation by $authenticatedUser was denied, incorrect permission set")
			render message(code: 'fedreg.help.unauthorized')
			response.setStatus(403)
		}
	}

}
