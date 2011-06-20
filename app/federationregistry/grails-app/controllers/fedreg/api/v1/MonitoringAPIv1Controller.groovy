package fedreg.api.v1

import fedreg.core.*
import grails.converters.JSON

class MonitoringAPIv1Controller {
	
	def list = {		
		def results = [:]
		def descriptors, type
		
		switch(params.type) {
			case "identityproviders": 	descriptors = IDPSSODescriptor.list()
										type = 'identityproviders'
										listMonitorSources(type, descriptors, results)
									 	break
			case "serviceproviders":  	descriptors = SPSSODescriptor.list()
										type = 'serviceproviders'
										listMonitorSources(type, descriptors, results)
										break
			default: 					descriptors = IDPSSODescriptor.list()
										type = 'identityproviders'
										listMonitorSources(type, descriptors, results)
										descriptors = SPSSODescriptor.list()
										type = 'serviceproviders'
										listMonitorSources(type, descriptors, results)										
		}
		
		def response = [monitors:[results]]
		render response as JSON
	}
	
	def show = {
		if(!params.type) {
			log.warn "Type was not present"
			render message(code: 'fedreg.controllers.namevalue.missing')
			response.sendError(400)
			return
		}
		
		if(!params.id) {
			log.warn "descriptor id was not present"
			render message(code: 'fedreg.controllers.namevalue.missing')
			response.sendError(400)
			return
		}
		
		def results = []
		def descriptor
		
		switch(params.type) {
			case "identityproviders": 	descriptor = IDPSSODescriptor.get(params.id)
									 	break
			case "serviceproviders":  	descriptor = SPSSODescriptor.get(params.id)
										break
		}
		
		if(!descriptor) {
			log.warn "Descriptor Id and/or type was invalid"
			render message(code: 'fedreg.controllers.namevalue.missing')
			response.sendError(400)
			return
		}
		
		def result = [:]
		result.id = descriptor.id
		result.name = descriptor.displayName
		result.monitors = []
	
		descriptor.monitors.each { m ->	
			def monitor = [:]				
			monitor.type = m.type.name
			monitor.endpoint = m.url
			monitor.interval = m.checkPeriod
			monitor.enabled = m.enabled
		
			result.monitors.add(monitor)
		}
	
		results.add(result)
		
		def response = ["${params.type.substring(0, params.type.size() - 1)}":results]	// dumps plural from naming
		render response as JSON
	}
	
	private void listMonitorSources(type, descriptors, r) {
		
		def results = []
		descriptors.each { desc ->
			if(desc.monitors) {
				def result = [:]
				result.id = desc.id
				result.name = desc.displayName
				result.format = "json"
				result.link = g.createLink(controller: 'monitoringAPIv1', id: desc.id, params:[type:type], absolute: true)
				
				results.add(result)
			}
		}
		
		r."$type" = results
	}
}