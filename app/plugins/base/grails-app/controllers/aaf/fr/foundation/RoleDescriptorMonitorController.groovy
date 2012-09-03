package aaf.fr.foundation

import org.apache.shiro.SecurityUtils

/**
 * Provides monitoring management views for Descriptors.
 *
 * @author Bradley Beddoes
 */
class RoleDescriptorMonitorController {
	def allowedMethods = [create:'POST', delete:'DELETE']
	
	def list = {
		def roleDescriptor = RoleDescriptor.get(params.id)
		if (!roleDescriptor) {
			flash.type="error"
			flash.message = message(code: 'aaf.fr.foundation.roledescriptor.nonexistant')
			response.sendError(500)
			return
		}
		
		render template: '/templates/monitor/list', contextPath: pluginContextPath, model:[roleDescriptor:roleDescriptor]
	}
	
	def create = {
		def roleDescriptor = RoleDescriptor.get(params.id)
		if (!roleDescriptor) {
			flash.type="error"
			flash.message = message(code: 'aaf.fr.foundation.roledescriptor.nonexistant')
			response.sendError(500)
			return
		}
		
		def monitorType = MonitorType.get(params.type)
		if (!roleDescriptor) {
			flash.type="error"
			flash.message = message(code: 'aaf.fr.foundation.monitortype.nonexistant')
			response.sendError(500)
			return
		}
		
		if(SecurityUtils.subject.isPermitted("federation:management:descriptor:${roleDescriptor.id}:monitor:add")) {
			def serviceMonitor = new ServiceMonitor(type:monitorType, url:params.url, interval:params.interval, node:params.node)
			roleDescriptor.addToMonitors(serviceMonitor)
			if(!roleDescriptor.save()) {
				log.info "$subject was unable to add $serviceMonitor to $roleDescriptor"
				roleDescriptor.errors.each {
					log.error it
				}

				render message(code: 'aaf.fr.foundation.monitor.create.error')
				response.setStatus(500)
				return
			}
			
			log.info "$subject added $monitorType at ${params.url} to $roleDescriptor"
			render message(code: 'aaf.fr.foundation.monitor.create.success')
		} else {
			log.warn("Attempt to add monitor to $roleDescriptor by $subject was denied, incorrect permission set")
			response.sendError(403)
		}
	}
	
	def delete = {
		def serviceMonitor = ServiceMonitor.get(params.id)
		if (!serviceMonitor) {
			flash.type="error"
			flash.message = message(code: 'aaf.fr.foundation.servicemonitor.nonexistant')
			response.sendError(500)
			return
		}
		if(SecurityUtils.subject.isPermitted("federation:management:descriptor:${serviceMonitor.roleDescriptor.id}:monitor:delete")) {
			serviceMonitor.delete()
			log.info "$subject delete $serviceMonitor from ${serviceMonitor.roleDescriptor}"
			render message(code: 'aaf.fr.foundation.monitor.delete.success')
		} else {
			log.warn("Attempt to delete monitor from ${serviceMonitor.roleDescriptor} by $subject was denied, incorrect permission set")
			response.sendError(403)
		}
	}

}