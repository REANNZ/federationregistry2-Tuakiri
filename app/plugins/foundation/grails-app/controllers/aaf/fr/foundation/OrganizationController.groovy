package aaf.fr.foundation

import org.apache.shiro.SecurityUtils
import grails.plugins.federatedgrails.Role

/**
 * Provides Organization views.
 *
 * @author Bradley Beddoes
 */
class OrganizationController {
  static defaultAction = "index"
  def allowedMethods = [save: 'POST', update: 'PUT']
    
  def organizationService

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
      flash.message = message(code: 'fedreg.controllers.namevalue.missing')
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
      
      [organization: organization, registrations:organization.buildStatistics(), entities:entities, identityproviders:identityproviders, serviceproviders:serviceproviders, administrators:adminRole?.users, contactTypes:ContactType.list(), organizationTypes: OrganizationType.list()]
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
      flash.message = message(code: 'fedreg.controllers.namevalue.missing')
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
      flash.message = message(code: 'fedreg.controllers.namevalue.missing')
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
      flash.message = message(code: 'fedreg.templates.organization.archived')

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
      flash.message = message(code: 'fedreg.controllers.namevalue.missing')
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
      flash.message = message(code: 'fedreg.templates.organization.unarchived')

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
      flash.message = message(code: 'fedreg.controllers.namevalue.missing')
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
      flash.message = message(code: 'fedreg.templates.organization.deleted')

      redirect (action: "list")
    }
    else {
      log.warn("Attempt to archive $organization by $subject was denied, incorrect permission set")
      response.sendError(403)
    }
  }

}
