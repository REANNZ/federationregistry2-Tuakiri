package aaf.fr.app

import aaf.fr.foundation.*
import org.apache.shiro.SecurityUtils

/**
 * Provides the dashboard view for FR users.
 *
 * @author Bradley Beddoes
 */
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
		
		def tasks = workflowTaskService.retrieveTasksAwaitingApproval(subject)
		
		//organizations.add(subject?.entityDescriptor?.organization)
		
		// Find all additional orgs this user can admin
		def orgs = Organization.list()
		orgs.each {
			if(!it.archived && SecurityUtils.subject.hasRole("organization-${it.id}-administrators"))
				if(it.id != subject?.entityDescriptor?.organization?.id)
					organizations.add(it)
		}
		
		def idps = IDPSSODescriptor.list()
		idps.each {
			if(!it.archived && SecurityUtils.subject.hasRole("descriptor-${it.id}-administrators"))
				identityProviders.add(it)
		}
		
		def sps = SPSSODescriptor.list()
		sps.each {
			if(!it.archived && SecurityUtils.subject.hasRole("descriptor-${it.id}-administrators"))
				serviceProviders.add(it)
		}
		
		[orgCount:orgCount, idpCount:idpCount, spCount:spCount, organizations: organizations, endpointCount:endpointCount, certCounts:certCounts, tasks:tasks, identityProviders:identityProviders, serviceProviders:serviceProviders, subject:subject]
	}

}