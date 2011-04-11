package fedreg.reporting

import org.apache.shiro.SecurityUtils

import java.math.MathContext
import java.math.RoundingMode
import static java.math.RoundingMode.*

import fedreg.host.User
import fedreg.core.*
import grails.plugins.nimble.core.Role
import grails.plugins.nimble.core.LoginRecord

import grails.converters.JSON

/**
 * Provides ServiceCategory views.
 *
 * @author Bradley Beddoes
 */
class InternalReportsController {
	
	def arc = {
		if(!params.id) {
			log.warn "IdP was not present"
			flash.type="error"
			flash.message = message(code: 'fedreg.controllers.namevalue.missing')
			redirect(action: "list")
			return
		}
		
		def year = params.int('year')
		if(!year) {
			def cal = Calendar.instance
			year = cal.get(Calendar.YEAR)
		}
		def month = params.int('month')
		
		def idp = IDPSSODescriptor.get(params.id)
		if (!idp) {
			flash.type="error"
			flash.message = message(code: 'fedreg.core.idpssoroledescriptor.nonexistant')
			redirect(action: "list")
			return
		}
		
		[idp:params.id, year:year, month:month]
	}
	
	def arcjson = {
		if(!params.id) {
			log.warn "IdP was not present"
			render message(code: 'fedreg.controllers.namevalue.missing')
			response.setStatus(500)
			return
		}
		
		def year = params.int('year')
		if(!year) {
			def cal = Calendar.instance
			year = cal.get(Calendar.YEAR)
		}
		def month = params.int('month')
		
		def idp = IDPSSODescriptor.get(params.id)
		if (!idp) {
			render message(code: 'fedreg.core.idpssoroledescriptor.nonexistant')
			response.setStatus(500)
			return
		}
		
		def target = 0, totalLogins
		def results = [:]
		def nodes = []
		def links = []
		
		results.nodes = nodes
		results.links = links
		if(month)
			results.title = "Connections between Identity Provider ${idp.displayName} and federation services for $month / $year"
		else
			results.title = "Connections between Identity Provider ${idp.displayName} and federation services during $year"

		if(month)
			totalLogins = WayfAccessRecord.executeQuery("select count(*) as count from WayfAccessRecord where idp_entityid = :id and year(dateCreated) = :year and month(dateCreated) = :month ", [id:idp.entityDescriptor.id, year:year, month:month])
		else
			totalLogins = WayfAccessRecord.executeQuery("select count(*) as count from WayfAccessRecord where idp_entityid = :id and year(dateCreated) = :year", [id:idp.entityDescriptor.id, year:year])
			
		def val = 0
		if(idp) {
			def node = [:]
			node.nodeName = idp.displayName
			node.group = 1
			nodes.add(node)
		}
		
		def mcu = new MathContext( 1, UP )
	
		def sps = SPSSODescriptor.list()
		sps.each { sp ->
			def loginCount
			
			if(month)
				loginCount = WayfAccessRecord.executeQuery("select count(*) as count from WayfAccessRecord where idp_entityid = :idp_id and sp_entityid = :sp_id and year(dateCreated) = :year and month(dateCreated) = :month ", [ idp_id:idp.entityDescriptor.id, sp_id:sp.entityDescriptor.id, year:year, month:month ])
			else
				loginCount = WayfAccessRecord.executeQuery("select count(*) as count from WayfAccessRecord where idp_entityid = :idp_id and sp_entityid = :sp_id and year(dateCreated) = :year", [ idp_id:idp.entityDescriptor.id, sp_id:sp.entityDescriptor.id, year:year ])
				
			if(loginCount[0] > 0) {
				def node = [:]
				node.nodeName = sp.displayName
				node.group = 2
				nodes.add(node)
				
				def link = [:]
				link.source = 0
				def value = (loginCount[0] / totalLogins[0]) * 20		// 0 - 10 instead of 0 - 1, makes graph look nicer
				link.value = value
				link.target = target++
			
				links.add(link)
			}
		}

		render results as JSON
	}
	
	def login = {
		
	}
	
	def dailylogins = {
		if(!params.year) {
			log.warn "Year was not present"
			render message(code: 'fedreg.controllers.namevalue.missing')
			response.setStatus(500)
			return
		}
		if(!params.month) {
			log.warn "Month was not present"
			render message(code: 'fedreg.controllers.namevalue.missing')
			response.setStatus(500)
			return
		}
		
		def results = []
		
		def days = LoginRecord.executeQuery("select new map(cast(lr.dateCreated as date) as date, count(*) as count) from LoginRecord lr where year(lr.dateCreated) = :year and month(lr.dateCreated) = :month group by day(lr.dateCreated) order by lr.dateCreated",
										 [year:params.int('year'), month:params.int('month')])
		days.each { d ->
				def data = [:]
				data.date = d.date.time
				data.count = d.count
								
				def sessionData = []
				def sessions = LoginRecord.executeQuery("select new map(lr.owner.id as userID, lr.owner.profile.fullName as name, lr.dateCreated as date) from LoginRecord lr where cast(lr.dateCreated as date) = :date order by lr.dateCreated", [date:d.date])
				sessions.each { s ->
					def session = [:]
					session.fullName = s.name
					session.time = s.date.time
					session.userID = s.userID
					sessionData.add(session)
				}
				data.sessions = sessionData
				
				results.add(data)
		}		
		render results as JSON
	}
	
}