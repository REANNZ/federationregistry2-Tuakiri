package aaf.fr.foundation

import org.apache.shiro.SecurityUtils
import grails.plugins.federatedgrails.Role

/**
 * Provides EntityDescriptor views.
 *
 * @author Bradley Beddoes
 */
class EntityDescriptorController {
  static defaultAction = "list"
  def allowedMethods = [save: 'POST', update: 'PUT', archive: 'DELETE', delete:'DELETE', unarchive:'PUT', archive:'PUT', migrateOrg:'PUT']
    
  def entityDescriptorService
  
  def list = {
    [entityList: EntityDescriptor.findAllWhere(archived:false), entityTotal: EntityDescriptor.count()]
  }
  
  def listarchived = {
    [entityList: EntityDescriptor.findAllWhere(archived:true), entityTotal: EntityDescriptor.count()]
  }

  def show = {
    if(!params.id) {
      log.warn "EntityDescriptor ID was not present"
      flash.type="error"
      flash.message = message(code: 'controllers.fr.generic.namevalue.missing')
      redirect(action: "list")
      return
    }
    
    def entity = EntityDescriptor.get(params.id)
    if (!entity) {
      flash.type="error"
      flash.message = message(code: 'aaf.fr.foundation.entitydescriptor.nonexistant')
      redirect(action: "list")
      return
    }
    def adminRole = Role.findByName("descriptor-${entity.id}-administrators")

    def organizations = Organization.list().findAll { it.functioning() }

    [entity: entity, contactTypes:ContactType.list(), administrators:adminRole?.subjects, organizations:organizations]
  }
  
  def create = {
    def entityDescriptor = new EntityDescriptor()
    [entity: entityDescriptor, organizationList: Organization.list()]
  }
  
  def save = {
    def (created, entityDescriptor) = entityDescriptorService.create(params)
  
    if(created) {
      log.info "$subject created $entityDescriptor"
      redirect (action: "show", id: entityDescriptor.id)
    } else {
      flash.type="error"
      flash.message = message(code: 'aaf.fr.foundation.entitydescriptor.save.validation.error')
      
      log.info "$subject failed attempting to create $entityDescriptor"
      render (view:'create', model:[entity:entityDescriptor, organizationList: Organization.list()])
    }
  }
  
  def update = {
    if(!params.id) {
      log.warn "EntityDescriptor ID was not present"
      flash.type="error"
      flash.message = message(code: 'controllers.fr.generic.namevalue.missing')
      redirect(action: "list")
      return
    }
    
    def entityDescriptor_ = EntityDescriptor.get(params.id)
    if (!entityDescriptor_) {
      log.error "EntityDescriptor for id ${params.id} does not exist"
      flash.type="error"
      flash.message = message(code: 'aaf.fr.foundation.entitydescriptor.nonexistant')
      redirect(action: "list")
      return
    }
    
    if(SecurityUtils.subject.isPermitted("federation:management:descriptor:${entityDescriptor_.id}:update")) {
      def (updated, entityDescriptor) = entityDescriptorService.update(params)
      if(updated) {
        log.info "$subject updated $entityDescriptor"
        redirect (action: "show", id: entityDescriptor.id)
      } else {
        log.info "$subject failed when attempting update on $entityDescriptor"
        entityDescriptor.errors.each {
          log.debug it
        }
        flash.type="error"
        flash.message = message(code: 'aaf.fr.foundation.entitydescriptor.save.validation.error')
        redirect (action: "show", id: entityDescriptor.id)
        return
      }
    }
    else {
      log.warn("Attempt to update $entityDescriptor_ by $subject was denied, incorrect permission set")
      response.sendError(403)
    }
  }
  
  def archive = {
    if(!params.id) {
      log.warn "EntityDescriptor ID was not present"
      flash.type="error"
      flash.message = message(code: 'controllers.fr.generic.namevalue.missing')
      redirect(action: "list")
      return
    }
    
    def entityDescriptor = EntityDescriptor.get(params.id)
    if (!entityDescriptor) {
      flash.type="error"
      flash.message = message(code: 'aaf.fr.foundation.entitydescriptor.nonexistant')
      redirect(action: "list")
      return
    }
    
    if(!(entityDescriptor.holdsIDPOnly() || entityDescriptor.holdsSPOnly())) {
      flash.type="error"
      flash.message = message(code: 'templates.fr.entitydescriptor.nonstandard')
      redirect(action: "show", id:entityDescriptor.id)
      return
    }
    
    if(SecurityUtils.subject.isPermitted("federation:management:advanced")) {
      entityDescriptorService.archive(entityDescriptor.id)
      log.info "$subject archived $entityDescriptor"

      flash.type="success"
      flash.message = message(code: 'templates.fr.entitydescriptor.archived')
      log.info "$subject archived $entityDescriptor"
      
      redirect (action: "show", id: entityDescriptor.id)
    }
    else {
      log.warn("Attempt to delete $entityDescriptor by $subject was denied, incorrect permission set")
      response.sendError(403)
    }
  }

  def unarchive = {
    if(!params.id) {
      log.warn "EntityDescriptor ID was not present"
      flash.type="error"
      flash.message = message(code: 'controllers.fr.generic.namevalue.missing')
      redirect(action: "list") 
      return
    }
    
    def entityDescriptor = EntityDescriptor.get(params.id)
    if (!entityDescriptor) {
      flash.type="error"
      flash.message = message(code: 'aaf.fr.foundation.entitydescriptor.nonexistant')
      redirect(action: "list")
      return
    }
    
    if(!(entityDescriptor.holdsIDPOnly() || entityDescriptor.holdsSPOnly())) {
      flash.type="error"
      flash.message = message(code: 'templates.fr.entitydescriptor.nonstandard')
      redirect(action: "show", id:entityDescriptor.id)
      return
    }
    
    if(SecurityUtils.subject.isPermitted("federation:management:advanced")) {
      entityDescriptorService.unarchive(entityDescriptor.id)
      log.info "$subject unarchive $entityDescriptor"

      flash.type="success"
      flash.message = message(code: 'templates.fr.entitydescriptor.unarchived')
      log.info "$subject unarchived $entityDescriptor"
      
      redirect (action: "show", id: entityDescriptor.id)
    }
    else {
      log.warn("Attempt to delete $entityDescriptor by $subject was denied, incorrect permission set")
      response.sendError(403)
    }
  }

  def delete = {
    if(!params.id) {
      log.warn "EntityDescriptor ID was not present"
      flash.type="error"
      flash.message = message(code: 'controllers.fr.generic.namevalue.missing')
      redirect(action: "list")
      return
    }
    
    def entityDescriptor = EntityDescriptor.get(params.id)
    if (!entityDescriptor) {
      flash.type="error"
      flash.message = message(code: 'aaf.fr.foundation.entitydescriptor.nonexistant')
      redirect(action: "list")
      return
    }
    
    if(SecurityUtils.subject.isPermitted("federation:management:advanced")) {
      entityDescriptorService.delete(entityDescriptor.id)
      log.info "$subject deleted $entityDescriptor"

      flash.type="success"
      flash.message = message(code: 'templates.fr.entitydescriptor.deleted')
      log.info "$subject deleted $entityDescriptor"
      
      redirect (action: "list")
    }
    else {
      log.warn("Attempt to delete $entityDescriptor by $subject was denied, incorrect permission set")
      response.sendError(403)
    }
  }

  def migrate = {
    if(!params.id) {
      log.warn "EntityDescriptor ID was not present"
      flash.type="error"
      flash.message = message(code: 'controllers.fr.generic.namevalue.missing')
      redirect(action: "list")
      return
    }
    
    def entityDescriptor = EntityDescriptor.get(params.id)
    if (!entityDescriptor) {
      flash.type="error"
      flash.message = message(code: 'aaf.fr.foundation.entitydescriptor.nonexistant')
      redirect(action: "list")
      return
    }

    if(!params.newOrgId) {
      log.warn "New Organization ID was not present"
      flash.type="error"
      flash.message = message(code: 'controllers.fr.generic.namevalue.missing')
      redirect (action: "show", id: entityDescriptor.id)
      return
    }
    
    def organization = Organization.get(params.newOrgId)
    if (!organization) {
      flash.type="error"
      flash.message = message(code: 'domains.fr.foundation.organization.nonexistant')
      redirect (action: "show", id: entityDescriptor.id)
      return
    }
    
    if(SecurityUtils.subject.isPermitted("federation:management:advanced")) {
      entityDescriptorService.migrateOrganization(entityDescriptor.id, organization.id)
      log.info "$subject migrated $entityDescriptor to $organization"

      flash.type="success"
      flash.message = message(code: 'templates.fr.entitydescriptor.migrated')
      log.info "$subject migrated $entityDescriptor to $organization"
      
      redirect (action: "show", id: entityDescriptor.id)
    }
    else {
      log.warn("Attempt to migrate $entityDescriptor to $organization by $subject was denied, incorrect permission set")
      response.sendError(403)
    }
  }

}
