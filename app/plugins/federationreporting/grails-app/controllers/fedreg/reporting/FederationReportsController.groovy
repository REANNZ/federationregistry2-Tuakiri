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
	
	def logins = {
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
