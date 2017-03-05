package aaf.fr.foundation

import org.apache.shiro.SecurityUtils
import grails.plugins.federatedgrails.Role

/**
 * Provides service provider views.
 *
 * @author Bradley Beddoes
 */
class ServiceProviderController {
  static defaultAction = "list"
  def allowedMethods = [save: 'POST', update: 'PUT', delete: 'DELETE']
  
  def ServiceProviderService
  
  def list = {
    [serviceProviderList: SPSSODescriptor.findAllWhere(archived:false), serviceProviderTotal: SPSSODescriptor.count()]
  }
  
  def listarchived = {
    [serviceProviderList: SPSSODescriptor.findAllWhere(archived:true), serviceProviderTotal: SPSSODescriptor.count()]
  }
  
  def show = {
    if(!params.id) {
      log.warn "SPSSODescriptor ID was not present"
      flash.type="error"
      flash.message = message(code: 'controllers.fr.generic.namevalue.missing')
      redirect(action: "list")
      return
    }
    
    def serviceProvider = SPSSODescriptor.get(params.id)
    if (!serviceProvider) {
      flash.type="error"
      flash.message = message(code: 'aaf.fr.foundation.spssoroledescriptor.nonexistant')
      redirect(action: "list")
      return
    }
    
    def attributes, specAttr
    if(SecurityUtils.subject.isPermitted("federation:management:modify:restricted:attributes")) {
      attributes = AttributeBase.list()
      specAttr = AttributeBase.findAllWhere(specificationRequired:true)
    } else {
      attributes = AttributeBase.findAllWhere(adminRestricted:false)
      specAttr = AttributeBase.findAllWhere(adminRestricted:false, specificationRequired:true)  
    }
    
    def adminRole = Role.findByName("descriptor-${serviceProvider.id}-administrators")
    def reportRole = Role.findByName("descriptor-${serviceProvider.id}-report-administrators")
    def subjects = aaf.fr.identity.Subject.list()
    [serviceProvider: serviceProvider, contactTypes:ContactType.list(), availableAttributes:attributes, specificationAttributes: specAttr, administrators:adminRole?.subjects, reportAdministrators:reportRole?.subjects, subjects:subjects]
  }
  
  def create = {
    def serviceProvider = new SPSSODescriptor()
    def c = AttributeBase.createCriteria()
    def attributeList = c.list {
      eq("adminRestricted", false)
      order("category", "asc")
      order("name", "asc")
    }
    [serviceProvider:serviceProvider, organizationList: Organization.findAllWhere(active:true, approved:true), attributeList: attributeList, nameIDFormatList: SamlURI.findAllWhere(type:SamlURIType.NameIdentifierFormat)]
  }
  
  def save = {
    withForm {
      def (created, ret) = ServiceProviderService.create(params)
    
      if(created) {
        log.info "$subject created ${ret.serviceProvider}"
        redirect (action: "show", id: ret.serviceProvider.id)
      }
      else {
        flash.type="error"
        flash.message = message(code: 'aaf.fr.foundation.spssoroledescriptor.save.validation.error')
        
        log.info "$subject failed attempting to create ${ret.serviceProvider}"
        
        def c = AttributeBase.createCriteria()
        def attributeList = c.list {
          eq("adminRestricted", false)
          order("category", "asc")
          order("name", "asc")
        }
        render (view:'create', model: ret + [organizationList: Organization.findAllWhere(active:true, approved:true), attributeList: attributeList, nameIDFormatList: SamlURI.findAllWhere(type:SamlURIType.NameIdentifierFormat)])
      }
    }.invalidToken {
      log.warn("Attempt to create service provider was denied, incorrect form token")
      response.sendError(403)
    }
  }
  
  def update = {
    if(!params.id) {
      log.warn "SPSSODescriptor ID was not present"
      flash.type="error"
      flash.message = message(code: 'controllers.fr.generic.namevalue.missing')
      redirect(action: "list")
      return
    }
    
    def serviceProvider_ = SPSSODescriptor.get(params.id)
    if (!serviceProvider_) {
      log.error "SPSSODescriptor for id ${params.id} does not exist"
      flash.type="error"
      flash.message = message(code: 'aaf.fr.foundation.spssoroledescriptor.nonexistant')
      redirect(action: "list")
      return
    }

    if(SecurityUtils.subject.isPermitted("federation:management:descriptor:${serviceProvider_.id}:update")) {
      def (updated, serviceProvider) = ServiceProviderService.update(params)
      if(updated) {
        log.info "$subject updated $serviceProvider"
        redirect (action: "show", id: serviceProvider.id)
      } else {        
        log.info "$subject failed when attempting update on $serviceProvider"
        serviceProvider.errors.each {
          log.debug it
        }
        flash.type="error"
        flash.message = message(code: 'aaf.fr.foundation.spssoroledescriptor.update.validation.error')
        redirect (action: "show", id: serviceProvider_.id)
        return
      }
    }
    else {
      log.warn("Attempt to update $serviceProvider_ by $subject was denied, incorrect permission set")
      response.sendError(403)
    }
  } 
}
