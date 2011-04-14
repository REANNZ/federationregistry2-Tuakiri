package fedreg.reporting

import org.apache.shiro.SecurityUtils

import fedreg.host.User
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
	
	def connectivityjson = {
		if(!params.id) {
			log.warn "IdP was not present"
			render message(code: 'fedreg.controllers.namevalue.missing')
			response.setStatus(500)
			return
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
		
		def idp = IDPSSODescriptor.get(params.id)
		if (!idp) {
			render message(code: 'fedreg.core.idpssoroledescriptor.nonexistant')
			response.setStatus(500)
			return
		}
		
		def activeSP = params.activesp as List
		
		def target = 1, totalLogins
		def results = [:]
		def services = []
		def nodes = []
		def links = []
		
		results.nodes = nodes
		results.links = links
		results.services = services
		
		results.title = "${g.message(code:'fedreg.templates.reports.identityprovider.connectivity.title', args:[idp.displayName])} ${day ? day + ' /':''} ${month ? month + ' /':''} $year"

		def totalQuery = "select count(*) as count from WayfAccessRecord where idpid = :idpid and year(dateCreated) = :year"
		def totalParams = [:]
		totalParams.idpid = idp.id
		totalParams.year = year

		if(month) {
			totalQuery = totalQuery + " and month(dateCreated) = :month"
			totalParams.month = month
		}
		if(day) {
			totalQuery = totalQuery + " and day(dateCreated) = :day"
			totalParams.day = day
		}
		if(activeSP) {
			totalQuery = totalQuery + " and spid in (:activeSP)"
			totalParams.activeSP = activeSP
		}
		
		totalLogins = WayfAccessRecord.executeQuery(totalQuery, totalParams)
			
		if(totalLogins[0] > 0) {
			def val = 0
			if(idp) {
				def node = [:]
				node.nodeName = idp.displayName
				node.group = 1
				nodes.add(node)
			}
	
			def sps = SPSSODescriptor.listOrderByDisplayName()
			sps.each { sp ->
				
				def loginQuery = "select count(*) as count from WayfAccessRecord where idpID = :idpid and spID = :spid and year(dateCreated) = :year"
				def loginParams = [:]
				loginParams.idpid = idp.id
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
				
				def loginCount = WayfAccessRecord.executeQuery(loginQuery, loginParams)
			
				if(loginCount[0] > 0) {
					def service = [:]
					service.name = sp.displayName
					service.id = sp.id
					services.add(service)
				
					if(activeSP == null || activeSP.contains(sp.id.toString())) {
						service.rendered = true
				
						def node = [:]
						node.nodeName = sp.displayName
						node.group = 2
						nodes.add(node)
	
						def link = [:]
						link.source = 0
						def value = ((loginCount[0] / totalLogins[0]) * 20)		/* 0 - 20 instead of 0 - 1, makes graph look nicer*/
						link.value = value
						link.target = target++

						links.add(link)
					}
					else
						service.rendered = false
				}
			}
		}
		render results as JSON
	}
}