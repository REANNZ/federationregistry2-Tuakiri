package fedreg.host

import fedreg.core.*
import org.apache.shiro.SecurityUtils

class DashboardController {
	
	def workflowTaskService

	def index = {
		def organizations = []
		def identityProviders = []
		def serviceProviders = []
		
		def orgCount = Organization.countByActive(true)
		def idpCount = IDPSSODescriptor.countByActive(true)
		def spCount = SPSSODescriptor.countByActive(true)
		def endpointCount = Endpoint.countByActive(true)
		def certCounts = KeyDescriptor.countByDisabled(false)
		
		def tasks = workflowTaskService.retrieveTasksAwaitingApproval(authenticatedUser)
		
		organizations.add(authenticatedUser?.entityDescriptor?.organization)
		
		// Find all additional orgs this user can admin
		def orgs = Organization.list()
		orgs.each {
			if(SecurityUtils.subject.hasRole("organization-${it.id}-administrators"))
				if(it.id != authenticatedUser?.entityDescriptor?.organization?.id)
					organizations.add(it)
		}
		
		def idps = IDPSSODescriptor.list()
		idps.each {
			if(SecurityUtils.subject.hasRole("descriptor-${it.id}-administrators"))
				identityProviders.add(it)
		}
		
		def sps = SPSSODescriptor.list()
		sps.each {
			if(SecurityUtils.subject.hasRole("descriptor-${it.id}-administrators"))
				serviceProviders.add(it)
		}
		
		[orgCount:orgCount, idpCount:idpCount, spCount:spCount, organizations: organizations, endpointCount:endpointCount, certCounts:certCounts, tasks:tasks, identityProviders:identityProviders, serviceProviders:serviceProviders, authenticatedUser:authenticatedUser]
	}

}