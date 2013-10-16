package aaf.fr.wayf

import aaf.fr.foundation.*

/**
 * Integrates with Switch based WAYF by providing configuration for selectable categories etc
 *
 * @author Bradley Beddoes
*/
class WayfController {
  
  def grailsApplication

  def generateconfiguration = {
    log.info "Generating new Wayf Configuration file for requestor located at $request.remoteAddr / $request.remoteHost"

    def organizationTypes = [] as List    
    def identityProviders = IDPSSODescriptor.list().findAll { idp -> idp.functioning() && !idp.attributeAuthorityOnly }.sort { idp -> idp.displayName }
    
    def ssoPostEndpoints = [:]

    def otList = []
    
    OrganizationType.findAllWhere(discoveryServiceCategory:true)?.each { orgType ->
      def ot = [:]
      ot.name = orgType.name
      ot.description = orgType.description
      ot.idpList = []
      identityProviders.findAll { it.organization.primary.name == ot.name }.each { idp ->
        def ssoEndpoint = false
        idp.singleSignOnServices.each { ep ->
          if (ep.binding.uri == 'urn:mace:shibboleth:1.0:profiles:AuthnRequest') {
            ot.idpList.add([entityID:idp.entityDescriptor.entityID, displayName:idp.displayName.replace("'",""), location:ep.location])
            ssoEndpoint = true
          }
        }
        if(!ssoEndpoint) {
          idp.singleSignOnServices.each { ep ->
            if (ep.binding.uri == 'urn:oasis:names:tc:SAML:2.0:bindings:HTTP-POST')
              ot.idpList.add([entityID:idp.entityDescriptor.entityID, displayName:idp.displayName.replace("'",""),  location:ep.location])
          }
        }  
      }

      if(ot.idpList.size() > 0)
        otList.add(ot)
    }
    
    render view: "generateconfiguration", model:[organizationTypes:otList], contentType:"text/plain", encoding:"UTF-8"
  }

}
