package aaf.fr.app

import aaf.fr.foundation.*
import aaf.fr.workflow.*
import org.apache.shiro.SecurityUtils

/**
 * Provides the dashboard view for FR users.
 *
 * @author Bradley Beddoes
 */
class DashboardController {
  
  def workflowTaskService

  def index = {

    def submittedTasks = []
    if(subject.contact.id) {
      def pi = ProcessInstance.executeQuery("from aaf.fr.workflow.ProcessInstance as pi where pi.status = 'INPROGRESS' and pi.params['creator']=?", [subject.contact.id])
      pi.each { instance ->
        def currentTask = instance.taskInstances.findAll{it.status == TaskStatus.APPROVALREQUIRED}
        submittedTasks.add(currentTask[0])
        println currentTask[0].potentialApprovers
      }
    }

    def organizations = []
    def identityProviders = []
    def serviceProviders = []
    
    def orgCount = Organization.list().findAll{it.functioning()}.size()
    def idpCount = IDPSSODescriptor.list().findAll{it.functioning()}.size()
    def spCount = SPSSODescriptor.list().findAll{it.functioning()}.size()
    
    // Find all additional orgs this user can admin
    def orgs = Organization.list()
    orgs.each {
      if(it.functioning() && SecurityUtils.subject.hasRole("organization-${it.id}-administrators"))
        organizations.add(it)
    }
    
    def idps = IDPSSODescriptor.list()
    idps.each {
      if(it.functioning() && SecurityUtils.subject.hasRole("descriptor-${it.id}-administrators"))
        identityProviders.add(it)
    }
    
    def sps = SPSSODescriptor.list()
    sps.each {
      if(it.functioning() && SecurityUtils.subject.hasRole("descriptor-${it.id}-administrators"))
        serviceProviders.add(it)
    }

    def tasks = workflowTaskService.retrieveTasksAwaitingApproval(subject)
    
    [orgCount:orgCount, idpCount:idpCount, spCount:spCount, organizations: organizations, tasks:tasks, submittedTasks:submittedTasks, identityProviders:identityProviders, serviceProviders:serviceProviders, subject:subject]
  }

}