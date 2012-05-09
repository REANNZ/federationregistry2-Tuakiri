package aaf.fr.foundation

import org.apache.shiro.SecurityUtils
import grails.plugins.federatedgrails.Role
import grails.plugins.federatedgrails.Permission

import aaf.fr.identity.Subject

/**
 * Provides administration management views for Descriptors.
 *
 * @author Bradley Beddoes
 */
class DescriptorAdministrationController {
  def allowedMethods = [grantFullAdministrationToken:'POST', grantReportAdministration: 'POST', revokeReportAdministration: 'DELETE', grantFullAdministration: 'POST', revokeFullAdministration: 'DELETE']
  
  def roleService
  def permissionService
  def invitationService

  def grantReportAdministration = {
    def descriptor = Descriptor.get(params.id)
    if (!descriptor) {
      log.error "No descriptor exists for ${params.id}"
      response.sendError(500)
      return
    } 
    
    def controller = determineController(descriptor) 
    def subj = Subject.get(params.subjectID)
    if (!subj) {
      flash.type="error"
      flash.message = message(code: 'aaf.fr.foundation.subject.nonexistant', default:"The subject identified does not exist")
      redirect(controller: controller, action: "show", id:descriptor.id, fragment:"tab-admins")
      return
    }
    
    if(SecurityUtils.subject.isPermitted("federation:management:descriptor:${descriptor.id}:manage:administrators")) {
      def adminRole = Role.findByName("descriptor-${descriptor.id}-report-administrators")
      
      if(!adminRole) {
        adminRole = roleService.createRole("descriptor-${descriptor.id}-report-administrators", "Report viewers for descriptor ${descriptor.id}", false)

        def permission = new Permission()
        permission.type = Permission.defaultPerm
        permission.target = "federation:managementment:descriptor:${descriptor.id}:reporting"
        permission.owner = adminRole
        
        permissionService.createPermission(permission, adminRole)
      }

      roleService.addMember(subj, adminRole)
      log.info "$subject successfully added $subj to $adminRole"
      redirect(controller: controller, action: "show", id:descriptor.id, fragment:"tab-admins")
    }
    else {
      log.warn("Attempt to assign complete administrative control for $descriptor to $subj by $subject was denied, incorrect permission set")
      response.sendError(403)
    }
  }
  
  def grantFullAdministration = {
    def descriptor = Descriptor.get(params.id)
    if (!descriptor) {
      log.error "No descriptor exists for ${params.id}"
      response.sendError(500)
      return
    } 
    
    def controller = determineController(descriptor) 
    def subj = Subject.get(params.subjectID)
    if (!subj) {
      flash.type="error"
      flash.message = message(code: 'aaf.fr.foundation.subject.nonexistant', default:"The subject identified does not exist")
      redirect(controller: controller, action: "show", id:descriptor.id, fragment:"tab-admins")
      return
    }
    
    if(SecurityUtils.subject.isPermitted("federation:management:descriptor:${descriptor.id}:manage:administrators")) {
      def adminRole = Role.findByName("descriptor-${descriptor.id}-administrators")
      
      if(adminRole) {
        roleService.addMember(subj, adminRole)
      
        log.info "$subject successfully added $subj to $adminRole"
        redirect(controller: controller, action: "show", id:descriptor.id, fragment:"tab-admins")
      } else {
        log.warn("Attempt to grant administrative control for $descriptor to $subj by $subject was denied. Administrative role doesn't exist")
        response.sendError(403)
      }
    }
    else {
      log.warn("Attempt to assign complete administrative control for $descriptor to $subj by $subject was denied, incorrect permission set")
      response.sendError(403)
    }
  }

  def grantFullAdministrationToken = {
    def descriptor = Descriptor.get(params.id)
    if (!descriptor) {
      log.error "No descriptor exists for ${params.id}"
      response.sendError(500)
      return
    } 

    if(!params.token) {
      log.error "No token supplied in request"
      response.sendError(500)
      return
    }
    
    def controller = determineController(descriptor) 
    
    def invite = invitationService.claim(params.token, descriptor.id)
    if(invite) {
      log.info "$subject successfully claimed $invite"
      flash.type="success"
      flash.message="Token was applied successfully" 
      redirect(controller: controller, action: "show", id:descriptor.id, fragment:"tab-admins")
    } else {
      log.info "$subject failed in claiming invite code $params.token"
      flash.type="error"
      flash.message="Code was not applied, it has been used before, is associated with a different provider or an internal error occured. Please verify correctness of entered code. Please contact support if you continue to have issues." 
      redirect(controller: controller, action: "show", id:descriptor.id, fragment:"tab-admins")
    }
  }
  
  def revokeReportAdministration = {
    def descriptor = Descriptor.get(params.id)
    if (!descriptor) {
      log.error "No descriptor exists for ${params.id}"
      response.sendError(500)
      return
    }

    def controller = determineController(descriptor)    
    def subj = Subject.get(params.subjectID)
    if (!subj) {
      flash.type="error"
      flash.message = message(code: 'aaf.fr.foundation.user.nonexistant', default:"The subject identified does not exist")
      redirect(controller: controller, action: "show", id:descriptor.id, fragment:"tab-admins")
      return
    }
    
    if(SecurityUtils.subject.isPermitted("federation:management:descriptor:${descriptor.id}:manage:administrators")) {      
      def adminRole = Role.findByName("descriptor-${descriptor.id}-report-administrators")
      roleService.deleteMember(subj, adminRole)
      
      log.info "$subject successfully removed $subj from $adminRole"
      redirect(controller: controller, action: "show", id:descriptor.id, fragment:"tab-admins")
    }
    else {
      log.warn("Attempt to remove complete administrative control for $descriptor from $subj by $subject was denied, incorrect permission set")
      response.sendError(403)
    }
  }

  def revokeFullAdministration = {
    def descriptor = Descriptor.get(params.id)
    if (!descriptor) {
      log.error "No descriptor exists for ${params.id}"
      response.sendError(500)
      return
    }

    def controller = determineController(descriptor)    
    def subj = Subject.get(params.subjectID)
    if (!subj) {
      flash.type="error"
      flash.message = message(code: 'aaf.fr.foundation.user.nonexistant', default:"The subject identified does not exist")
      redirect(controller: controller, action: "show", id:descriptor.id, fragment:"tab-admins")
      return
    }
    
    if(SecurityUtils.subject.isPermitted("federation:management:descriptor:${descriptor.id}:manage:administrators")) {
      if(subj == subject) {
        flash.type="error"
        flash.message = message(code: 'controller.descriptoradministration.selfedit', default:"Admins can't remove their own privileged access.")
        redirect(controller: controller, action: "show", id:descriptor.id, fragment:"tab-admins")
        return
      }
      
      def adminRole = Role.findByName("descriptor-${descriptor.id}-administrators")
      roleService.deleteMember(subj, adminRole)
      
      log.info "$subject successfully removed $subj from $adminRole"
      redirect(controller: controller, action: "show", id:descriptor.id, fragment:"tab-admins")
    }
    else {
      log.warn("Attempt to remove complete administrative control for $descriptor from $subj by $subject was denied, incorrect permission set")
      response.sendError(403)
    }
  }

  private String determineController(def descriptor) {
    def controller
    switch(descriptor.class.simpleName) {
      case "IDPSSODescriptor" : controller = 'identityProvider'
                                break
      case "SPSSODescriptor" :  controller = 'serviceProvider'
                                break                         
    }
    controller
  }
}