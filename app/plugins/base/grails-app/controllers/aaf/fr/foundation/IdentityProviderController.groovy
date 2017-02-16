package aaf.fr.foundation

import org.apache.shiro.SecurityUtils
import grails.plugins.federatedgrails.Role

/**
 * Provides Identity Provider views.
 *
 * @author Bradley Beddoes
 */
class IdentityProviderController {
  def defaultAction = "list"
  def allowedMethods = [save: 'POST', update: 'PUT', delete: 'DELETE']

  def IdentityProviderService
  
  def list = {
    [identityProviderList: IDPSSODescriptor.findAllWhere(archived:false), identityProviderTotal: IDPSSODescriptor.count()]
  }
  
  def listarchived = {
    [identityProviderList: IDPSSODescriptor.findAllWhere(archived:true), identityProviderTotal: IDPSSODescriptor.count()]
  }

  def show = {
    if(!params.id) {
      log.warn "IDPSSODescriptor ID was not present"
      flash.type="error"
      flash.message = message(code: 'controllers.fr.generic.namevalue.missing')
      redirect(action: "list")
      return
    }
    
    def identityProvider = IDPSSODescriptor.get(params.id)
    if (!identityProvider) {
      flash.type="error"
      flash.message = message(code: 'aaf.fr.foundation.idpssoroledescriptor.nonexistant')
      redirect(action: "list")
      return
    }
    
    def adminRole = Role.findByName("descriptor-${identityProvider.id}-administrators")
    def reportRole = Role.findByName("descriptor-${identityProvider.id}-report-administrators")
    def subjects = aaf.fr.identity.Subject.list()
    [identityProvider: identityProvider, contactTypes:ContactType.list(), administrators:adminRole?.subjects, reportAdministrators:reportRole?.subjects, subjects:subjects]
  }
  
  def create = {
    def identityProvider = new IDPSSODescriptor()
    def c = AttributeBase.createCriteria()
    def attributeList = c.list {
      eq("adminRestricted", false)
      order("category", "asc")
      order("name", "asc")
    }
    [identityProvider: identityProvider, organizationList: Organization.findAllWhere(active:true, approved:true), attributeList: attributeList, nameIDFormatList: SamlURI.findAllWhere(type:SamlURIType.NameIdentifierFormat)]
  }
  
  def save = {
    withForm {
      def (created, ret) = IdentityProviderService.create(params)
    
      if(created) {
        log.info "$subject created ${ret.identityProvider}"
        redirect (action: "show", id: ret.identityProvider.id)
      }
      else {
        flash.type="error"
        flash.message = message(code: 'aaf.fr.foundation.idpssoroledescriptor.save.validation.error')
        def c = AttributeBase.createCriteria()
        def attributeList = c.list {
          eq("adminRestricted", false)
          order("category", "asc")
          order("name", "asc")
        }
        render (view:'create', model:ret + [organizationList: Organization.findAllWhere(active:true, approved:true), attributeList: attributeList, nameIDFormatList: SamlURI.findAllWhere(type:SamlURIType.NameIdentifierFormat)])
      }
    }.invalidToken {
      log.warn("Attempt to create identity provider was denied, incorrect form token")
      response.sendError(403)
    }
  }
  
  def update = {
    if(!params.id) {
      log.warn "IDPSSODescriptor ID was not present"
      flash.type="error"
      flash.message = message(code: 'controllers.fr.generic.namevalue.missing')
      redirect(action: "list")
      return
    }
    
    def identityProvider_ = IDPSSODescriptor.get(params.id)
    if (!identityProvider_) {
      log.warn "IDPSSODescriptor identified by ID ${params.id} was not found"
      flash.type="error"
      flash.message = message(code: 'aaf.fr.foundation.identityprovider.nonexistant')
      redirect(action: "list")
      return
    }
    
    if(SecurityUtils.subject.isPermitted("federation:management:descriptor:${identityProvider_.id}:update")) {
      def (updated, identityProvider) = IdentityProviderService.update(params)
      if(updated) {
        log.info "$subject updated $identityProvider"
        redirect (action: "show", id: identityProvider.id)
      } else {
        log.info "$subject failed when attempting update on $identityProvider"
        identityProvider.errors.each {
          log.debug it
        }
        flash.type="error"
        flash.message = message(code: 'aaf.fr.foundation.identityprovider.update.validation.error')
        redirect (action: "show", id: identityProvider.id)
      }
    }
    else {
      log.warn("Attempt to update $identityProvider_ by $subject was denied, incorrect permission set")
      response.sendError(403)
    }
  }
}
