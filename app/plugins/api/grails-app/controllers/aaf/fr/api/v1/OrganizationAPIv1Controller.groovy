package aaf.fr.api.v1

import grails.converters.JSON
import groovy.xml.MarkupBuilder

import aaf.fr.foundation.*

class OrganizationAPIv1Controller {
  
  def list = {  
    def results = []

    def organizations = Organization.list().sort{it.id}   
    organizations.each { org ->
      def result = [:]
      result.id = org.id
      result.name = org.name
      result.functioning = org.functioning()
      result.archived = org.archived
      result.link = g.createLink(controller: 'organizationAPIv1', id: org.id, absolute: true)
      result.format = "json"
      results.add(result)
    }
    
    def response = [organizations:results]
    render response as JSON   
  }
  
  def show = {    
    if(!params.id) {
      log.warn "Organization ID was not present"
      render message(code: 'controllers.fr.generic.namevalue.missing')
      response.sendError(400)
      return
    }
    
    def org = Organization.get(params.id)
    if (!org) {
      log.warn "Organization does not exist for ${params.id}"
      render message(code: 'controllers.fr.generic.namevalue.missing')
      response.sendError(400)
      return
    }
    
    def result = [:]
    result.id = org.id
    result.name = org.name
    result.displayName = org.displayName
    result.description = org.description
    result.logoURL = org.logoURL
    result.lang = org.lang
    result.primary = org.primary.name

    result.secondarytypes = []
    org.types.each { type ->
      result.secondarytypes.add(type.name)
    }

    result.identityproviders = []
    IDPSSODescriptor.findAllWhere(organization:org).sort{it.id}.each { idp ->
      def r = [:]
      r.id = idp.id
      r.name = idp.displayName
      r.link = g.createLink(controller: 'identityProvidersAPIv1', id: idp.id, absolute: true)
      result.identityproviders.add(r)
    }

    result.serviceproviders = []
    SPSSODescriptor.findAllWhere(organization:org).sort{it.id}.each { sp ->
      def r = [:]
      r.id = sp.id
      r.name = sp.displayName
      r.link = g.createLink(controller: 'serviceProvidersAPIv1', id: sp.id, absolute: true)
      result.serviceproviders.add(r)
    }

    result.url = org.url
    result.functioning = org.functioning()
    result.archived = org.archived
    result.format = "json"

    def response = [organization:result]
    render response as JSON 
  }
}