package aaf.fr.api.v1

import aaf.fr.foundation.*
import grails.converters.JSON

class MonitoringAPIv1Controller {
	
	def list = {		
		def results = [:]
		def descriptors, type
		
		switch(params.type) {
			case "identityproviders": 	descriptors = IDPSSODescriptor.list().sort{it.id}
										type = 'identityproviders'
										listMonitorSources(type, descriptors, results)
									 	break
			case "serviceproviders":  	descriptors = SPSSODescriptor.list().sort{it.id}
										type = 'serviceproviders'
										listMonitorSources(type, descriptors, results)
										break
			default: 			descriptors = IDPSSODescriptor.list().sort{it.id}
										type = 'identityproviders'
										listMonitorSources(type, descriptors, results)
										descriptors = SPSSODescriptor.list().sort{it.id}
										type = 'serviceproviders'
										listMonitorSources(type, descriptors, results)										
		}
		
		def response = [monitors:[results]]
		render response as JSON
	}
	
	def show = {
		if(!params.type) {
			log.warn "Type was not present"
			render message(code: 'controllers.fr.generic.namevalue.missing')
			response.sendError(400)
			return
		}
		
		if(!params.id) {
			log.warn "descriptor id was not present"
			render message(code: 'controllers.fr.generic.namevalue.missing')
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
			log.warn "Descriptor ID and/or type was invalid"
			render message(code: 'controllers.fr.generic.namevalue.missing')
			response.sendError(400)
			return
		}
		
		def result = [:]
		result.id = descriptor.id
		result.name = descriptor.displayName

		result.monitors = []
		descriptor.monitors.sort{it.id}.each { m ->	
			def monitor = [:]				
			monitor.type = m.type.name
			monitor.endpoint = m.url
			monitor.interval = m.checkPeriod
			monitor.enabled = m.enabled
      monitor.node = m.node
		
			result.monitors.add(monitor)
		}

    result.organization = [:]
    result.organization.name = descriptor.organization.name
    result.organization.link = g.createLink(controller: 'organizationAPIv1', id: descriptor.organization.id, absolute:true)

    result.contacts = [:]
    result.contacts.administrative = []
    descriptor.contacts.findAll{ cp -> cp.type.name.equalsIgnoreCase('administrative') }.sort{it.id}.each { cp ->
      def admin = [:]
      admin.givenName = cp.contact.givenName
      admin.surname = cp.contact.surname 
      admin.email = cp.contact.email?.uri
      admin.mobilePhone = cp.contact.mobilePhone?.uri
      result.contacts.administrative.add(admin)
    }

    result.contacts.technical = []
    descriptor.contacts.findAll{ cp -> cp.type.name.equalsIgnoreCase('technical') }.sort{it.id}.each { cp ->
      def tech = [:]
      tech.givenName = cp.contact.givenName
      tech.surname = cp.contact.surname 
      tech.email = cp.contact.email?.uri
      tech.mobilePhone = cp.contact.mobilePhone?.uri
      result.contacts.technical.add(tech)
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
        result.link = g.createLink(controller: 'monitoringAPIv1', id: desc.id, params:[type:type], absolute: true)
        result.organization = [:]
        result.organization.name = desc.organization.name
        result.organization.link = g.createLink(controller: 'organizationAPIv1', id: desc.organization.id, absolute:true)
				result.format = "json"
				results.add(result)
			}
		}
		
		r."$type" = results
	}
}