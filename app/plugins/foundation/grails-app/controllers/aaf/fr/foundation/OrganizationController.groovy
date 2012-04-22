package aaf.fr.foundation

import org.apache.shiro.SecurityUtils
import grails.plugins.federatedgrails.Role

import aaf.fr.identity.Subject

/**
 * Provides Organization views.
 *
 * @author Bradley Beddoes
 */
class OrganizationController {
  static defaultAction = "index"
  def allowedMethods = [save: 'POST', update: 'PUT', delete:'DELETE', unarchive:'PUT', archive:'PUT']
    
  def organizationService
  def roleService

  def list = {
    [organizationList: Organization.findAllWhere(archived:false), organizationTotal: Organization.count()]
  }
  
  def listarchived = {
    [organizationList: Organization.findAllWhere(archived:true), organizationTotal: Organization.count()]
  }

  def show = {
    if(!params.id) {
      log.warn "Organization ID was not present"
      flash.type="error"
      flash.message = message(code: 'controllers.fr.generic.namevalue.missing')
      redirect(action: "list")
      return
    }
    
    def organization = Organization.get(params.id)
    if (organization) {
      def entities = EntityDescriptor.findAllWhere(organization:organization)
      def adminRole = Role.findByName("organization-${organization.id}-administrators")
      def identityproviders = []
      def serviceproviders = []
            
      entities.each { e ->
        e.idpDescriptors.each { idp -> identityproviders.add(idp)}
        e.spDescriptors.each { sp -> serviceproviders.add(sp) }
      }
      
      def subjects = aaf.fr.identity.Subject.list()
      [organization: organization, registrations:organization.buildStatistics(), entities:entities, identityproviders:identityproviders, serviceproviders:serviceproviders, administrators:adminRole?.subjects, subjects:subjects, contactTypes:ContactType.list(), organizationTypes: OrganizationType.list()]
    }
    else {
      flash.type="error"
      flash.message = message(code: 'aaf.fr.foundation.organization.nonexistant')
      redirect(action: "list")
    }
  }
  
  def create = {
    def organization = new Organization()
    [organization:organization, organizationTypes: OrganizationType.list()]
  }
  
  def save = {
    def (created, organization, contact) = organizationService.create(params)
  
    if(created) {
      log.info "$subject created $organization"
      redirect (action: "show", id: organization.id)
    } else {
      log.info "$subject failed to create $organization"
      
      flash.type="error"
      flash.message = message(code: 'aaf.fr.foundation.organization.save.validation.error')
      render (view:'create', model:[organization:organization, contact:contact, organizationTypes: OrganizationType.list()])
    }
  }
  
  def update = {
    if(!params.id) {
      log.warn "Organization ID was not present"
      flash.type="error"
      flash.message = message(code: 'controllers.fr.generic.namevalue.missing')
      redirect(action: "list")
    }
    
    def organization_ = Organization.get(params.id)
    if (!organization_) {
      flash.type="error"
      flash.message = message(code: 'aaf.fr.foundation.organization.nonexistant')
      redirect(action: "list")
    }
    
    if(SecurityUtils.subject.isPermitted("organization:${organization_.id}:update")) {
      def (updated, organization) = organizationService.update(params)
      if(updated) {
        log.info "$subject updated $organization"
        redirect (action: "show", id: organization.id)
      }
      else {
        log.info "$subject failed to update $organization"
        organization.errors.each {
          log.debug it
        }
        flash.type="error"
        flash.message = message(code: 'aaf.fr.foundation.organization.save.validation.error')
        redirect (action: "show", id: organization.id)
      }
    }
    else {
      log.warn("Attempt to update $organization_ by $subject was denied, incorrect permission set")
      response.sendError(403)
    }
  }

  def archive = {
    if(!params.id) {
      log.warn "Organization ID was not present"
      flash.type="error"
      flash.message = message(code: 'controllers.fr.generic.namevalue.missing')
      redirect(action: "list")
    }
    
    def organization = Organization.get(params.id)
    if (!organization) {
      flash.type="error"
      flash.message = message(code: 'aaf.fr.foundation.organization.nonexistant')
      redirect(action: "list")
    }
    
    if(SecurityUtils.subject.isPermitted("federation:management:advanced")) {
      organizationService.archive(organization.id)
      log.info "$subject archive $organization"

      flash.type="success"
      flash.message = message(code: 'templates.fr.organization.archived')

      redirect (action: "show", id: organization.id)
    }
    else {
      log.warn("Attempt to archive $organization by $subject was denied, incorrect permission set")
      response.sendError(403)
    }
  }

  def unarchive = {
    if(!params.id) {
      log.warn "Organization ID was not present"
      flash.type="error"
      flash.message = message(code: 'controllers.fr.generic.namevalue.missing')
      redirect(action: "list")
    }
    
    def organization = Organization.get(params.id)
    if (!organization) {
      flash.type="error"
      flash.message = message(code: 'aaf.fr.foundation.organization.nonexistant')
      redirect(action: "list")
    }
    
    if(SecurityUtils.subject.isPermitted("federation:management:advanced")) {
      organizationService.unarchive(organization.id)
      log.info "$subject unarchived $organization"

      flash.type="success"
      flash.message = message(code: 'templates.fr.organization.unarchived')

      redirect (action: "show", id: organization.id)
    }
    else {
      log.warn("Attempt to archive $organization by $subject was denied, incorrect permission set")
      response.sendError(403)
    }
  }

  def delete = {
    if(!params.id) {
      log.warn "Organization ID was not present"
      flash.type="error"
      flash.message = message(code: 'controllers.fr.generic.namevalue.missing')
      redirect(action: "list")
    }
    
    def organization = Organization.get(params.id)
    if (!organization) {
      flash.type="error"
      flash.message = message(code: 'aaf.fr.foundation.organization.nonexistant')
      redirect(action: "list")
    }
    
    if(SecurityUtils.subject.isPermitted("federation:management:advanced")) {
      organizationService.delete(organization.id)

      log.info "$subject deleted $organization"
      flash.type="success"
      flash.message = message(code: 'templates.fr.organization.deleted')

      redirect (action: "list")
    }
    else {
      log.warn("Attempt to archive $organization by $subject was denied, incorrect permission set")
      response.sendError(403)
    }
  }

  def grantFullAdministration = {
    def organization = Organization.get(params.id)
    if (!organization) {
      log.error "No organization exists for ${params.id}"
      response.sendError(500)
      return
    } 
    
    def subj = Subject.get(params.subjectID)
    if (!subj) {
      flash.type="error"
      flash.message = message(code: 'aaf.fr.foundation.subject.nonexistant', default:"The subject identified does not exist")
      redirect(action: "show", id:organization.id, fragment:"tab-admins")
      return
    }
    
    if(SecurityUtils.subject.isPermitted("federation:management:organization:${organization.id}:administrators")) {
      def adminRole = Role.findByName("organization-${organization.id}-administrators")
      
      if(adminRole) {
        roleService.addMember(subj, adminRole)
      
        log.info "$subject successfully added $subj to $adminRole"
        redirect(action: "show", id:organization.id, fragment:"tab-admins")
      } else {
        log.warn("Attempt to grant administrative control for $organization to $subj by $subject was denied. Administrative role doesn't exist")
        response.sendError(403)
      }
    }
    else {
      log.warn("Attempt to assign complete administrative control for $organization to $subj by $subject was denied, incorrect permission set")
      response.sendError(403)
    }
  }

  def revokeFullAdministration = {
    def organization = Organization.get(params.id)
    if (!organization) {
      log.error "No organization exists for ${params.id}"
      response.sendError(500)
      return
    } 
   
    def subj = Subject.get(params.subjectID)
    if (!subj) {
      flash.type="error"
      flash.message = message(code: 'aaf.fr.foundation.user.nonexistant', default:"The subject identified does not exist")
      redirect(action: "show", id:organization.id, fragment:"tab-admins")
      return
    }
    
    if(SecurityUtils.subject.isPermitted("federation:management:organization:${organization.id}:administrators")) {
      if(subj == subject) {
        flash.type="error"
        flash.message = message(code: 'controller.organizationadministration.selfedit', default:"Admins can't remove their own privileged access.")
        redirect(action: "show", id:organization.id, fragment:"tab-admins")
        return
      }
      
      def adminRole = Role.findByName("organization-${organization.id}-administrators")
      roleService.deleteMember(subj, adminRole)
      
      log.info "$subject successfully removed $subj from $adminRole"
      redirect(action: "show", id:organization.id, fragment:"tab-admins")
    }
    else {
      log.warn("Attempt to remove complete administrative control for $organization from $subj by $subject was denied, incorrect permission set")
      response.sendError(403)
    }
  }

}
