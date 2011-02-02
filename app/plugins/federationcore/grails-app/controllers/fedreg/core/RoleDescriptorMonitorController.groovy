package fedreg.core

import org.apache.shiro.SecurityUtils

class RoleDescriptorMonitorController {
	def allowedMethods = [create:'POST', delete:'DELETE']
	
	def list = {
		def roleDescriptor = RoleDescriptor.get(params.id)
		if (!roleDescriptor) {
			flash.type="error"
			flash.message = message(code: 'fedreg.core.roledescriptor.nonexistant')
			response.sendError(500)
			return
		}
		
		render template: '/templates/monitor/list', contextPath: pluginContextPath, model:[roleDescriptor:roleDescriptor]
	}
	
	def create = {
		def roleDescriptor = RoleDescriptor.get(params.id)
		if (!roleDescriptor) {
			flash.type="error"
			flash.message = message(code: 'fedreg.core.roledescriptor.nonexistant')
			response.sendError(500)
			return
		}
		
		def monitorType = MonitorType.get(params.type)
		if (!roleDescriptor) {
			flash.type="error"
			flash.message = message(code: 'fedreg.core.monitortype.nonexistant')
			response.sendError(500)
			return
		}
		
		if(SecurityUtils.subject.isPermitted("descriptor:${roleDescriptor.id}:monitor:add")) {
			def serviceMonitor = new ServiceMonitor(type:monitorType, url:params.url, interval:params.interval)
			roleDescriptor.addToMonitors(serviceMonitor)
			if(!roleDescriptor.save()) {
				log.info "$authenticatedUser was unable to add $serviceMonitor to $roleDescriptor"
				roleDescriptor.errors.each {
					log.error it
				}

				render message(code: 'fedreg.core.monitor.create.error')
				response.setStatus(500)
				return
			}
			
			log.info "$authenticatedUser added $monitorType at ${params.url} to $roleDescriptor"
			render message(code: 'fedreg.core.monitor.create.success')
		} else {
			log.warn("Attempt to add monitor to $roleDescriptor by $authenticatedUser was denied, incorrect permission set")
			response.sendError(403)
		}
	}
	
	def delete = {
		def serviceMonitor = ServiceMonitor.get(params.id)
		if (!serviceMonitor) {
			flash.type="error"
			flash.message = message(code: 'fedreg.core.servicemonitor.nonexistant')
			response.sendError(500)
			return
		}
		if(SecurityUtils.subject.isPermitted("descriptor:${serviceMonitor.roleDescriptor.id}:monitor:delete")) {
			serviceMonitor.delete()
			log.info "$authenticatedUser delete $serviceMonitor from ${serviceMonitor.roleDescriptor}"
			render message(code: 'fedreg.core.monitor.delete.success')
		} else {
			log.warn("Attempt to delete monitor from ${serviceMonitor.roleDescriptor} by $authenticatedUser was denied, incorrect permission set")
			response.sendError(403)
		}
	}

}