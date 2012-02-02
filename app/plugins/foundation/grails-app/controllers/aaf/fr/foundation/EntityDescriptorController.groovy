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
  def allowedMethods = [save: 'POST', update: 'PUT', archive: 'DELETE']
    
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
      flash.message = message(code: 'fedreg.controllers.namevalue.missing')
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
    println entity 
    def adminRole = Role.findByName("descriptor-${entity.id}-administrators")
    [entity: entity, contactTypes:ContactType.list(), administrators:adminRole?.subjects]
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
  
  def edit = {
    if(!params.id) {
      log.warn "EntityDescriptor ID was not present"
      flash.type="error"
      flash.message = message(code: 'fedreg.controllers.namevalue.missing')
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
    
    if(SecurityUtils.subject.isPermitted("descriptor:${entityDescriptor.id}:update")) {
      [entity: entityDescriptor]
    }
    else {
      log.warn("Attempt to edit $entityDescriptor by $subject was denied, incorrect permission set")
      response.sendError(403)
    }   
  }
  
  def update = {
    if(!params.id) {
      log.warn "EntityDescriptor ID was not present"
      flash.type="error"
      flash.message = message(code: 'fedreg.controllers.namevalue.missing')
      redirect(action: "list")
      return
    }
    
    def entityDescriptor_ = EntityDescriptor.get(params.id)
    if (!entityDescriptor_) {
      flash.type="error"
      flash.message = message(code: 'aaf.fr.foundation.entitydescriptor.nonexistant')
      redirect(action: "list")
      return
    }
    
    if(SecurityUtils.subject.isPermitted("descriptor:${entityDescriptor_.id}:update")) {
      def (updated, entityDescriptor) = entityDescriptorService.update(params)
      if(updated) {
        log.info "$subject updated $entityDescriptor"
        redirect (controller: "entityDescriptor", action: "show", id: entityDescriptor.id)
      } else {
        flash.type="error"
        flash.message = message(code: 'aaf.fr.foundation.entitydescriptor.update.validation.error')
        log.info "$subject failed attempt to update $entityDescriptor"
        render (view:'edit', model:[entity:entityDescriptor])
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
      flash.message = message(code: 'fedreg.controllers.namevalue.missing')
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
      flash.message = message(code: 'aaf.fr.foundation.entitydescriptor.nonstandard')
      redirect(action: "list")
      return
    }
    
    if(SecurityUtils.subject.isPermitted("descriptor:${entityDescriptor.id}:archive")) {
      entityDescriptorService.archive(entityDescriptor.id)
      
      flash.type="success"
      flash.message = message(code: 'aaf.fr.foundation.entitydescriptor.archived')
      log.info "$subject deleted $entityDescriptor"
      
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
      flash.message = message(code: 'fedreg.controllers.namevalue.missing')
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
    
    if(SecurityUtils.subject.isPermitted("descriptor:${entityDescriptor.id}:delete")) {
      entityDescriptorService.delete(entityDescriptor.id)
      
      flash.type="success"
      flash.message = message(code: 'aaf.fr.foundation.entitydescriptor.deleted')
      log.info "$subject deleted $entityDescriptor"
      
      redirect (action: "list")
    }
    else {
      log.warn("Attempt to delete $entityDescriptor by $subject was denied, incorrect permission set")
      response.sendError(403)
    }
  }

}
