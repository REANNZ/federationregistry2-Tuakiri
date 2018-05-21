package aaf.fr.foundation

import static org.apache.commons.lang.StringUtils.*

/**
 * Provides methods for managing Endpoint instances.
 *
 * @author Bradley Beddoes
 */
class EndpointService {
  static transactional = true
  
  def grailsApplication

  def create(def descriptor, def endpointClass, def endpointType, def binding, def loc, def index, def active) {
    def endpoint = grailsApplication.classLoader.loadClass(endpointClass).newInstance(descriptor:descriptor, binding: binding, location: loc, active:active ? true:false, approved:true)
    if(index)
      endpoint.index = index
    descriptor."addTo${capitalize(endpointType)}"(endpoint)
    determineDescriptorProtocolSupport(descriptor)

    descriptor.save()
    if(descriptor.hasErrors() || endpoint.hasErrors()) {
      descriptor.errors?.each {
        log.error it
      }
      endpoint.errors?.each {
        log.error it
      }
      throw new ErronousStateException("Unable to save when creating ${endpoint} for ${descriptor}")
    }
    
    log.info "$subject created $endpoint for $descriptor of type $endpointType at $loc"
  }
  
  def update(def endpoint, def binding, def location, def index) {
    endpoint.binding = binding
    endpoint.location = location
    
    if(index)
      endpoint.index = index

    def descriptor = endpoint.descriptor
    determineDescriptorProtocolSupport(descriptor)
    
    descriptor.save()
    if(descriptor.hasErrors()) {
      descriptor.errors.each {
        log.error it
      }
      throw new ErronousStateException("Unable to save when updating ${endpoint}")
    }
    
    log.info "$subject updated $endpoint"
  }
  
  def makeDefault(def endpoint, def endpointType) {
    def descriptor = endpoint.descriptor
    descriptor."$endpointType".each {
      if(it.isDefault) {
        it.isDefault = false
        it.save()
        if(it.hasErrors()) {
          it.errors.each {log.error}
          throw new ErronousStateException("Unable to save when setting default for ${endpoint} for ${descriptor}")
        }
      }
    }
    endpoint.isDefault = true
    endpoint.save()
    
    log.info "$subject set $endpoint as default"
  }
  
  def toggle(def endpoint) {
    log.info "Toggling state of ${endpoint}"
    
    endpoint.active = !endpoint.active

    determineDescriptorProtocolSupport(endpoint.descriptor)

    endpoint.save()
    if(endpoint.hasErrors()) {
      endpoint.errors.each {
        log.error it
      }
      throw new ErronousStateException("Unable to save when toggling active state for ${endpoint}")
    }
    
    log.info "$subject toggled $endpoint state"
  }
  
  def delete(def endpoint, def endpointType) {
    def descriptor = endpoint.descriptor
    log.info "Deleting ${endpoint} with binding ${endpoint.binding} from ${descriptor}"
    
    endpoint.delete()
    descriptor."removeFrom${capitalize(endpointType)}"(endpoint)

    determineDescriptorProtocolSupport(descriptor)

    if(!descriptor.save()) {
      descriptor.errors.each {
        log.error it
      }
      throw new ErronousStateException("Unable to save descriptor when deleting ${endpoint}")
    }
    
    log.info "$subject deleted $endpoint from $descriptor"
  }
  
  def determineDescriptorProtocolSupport(descriptor) {
    descriptor.protocolSupportEnumerations.clear()
    
    if(descriptor.instanceOf(IDPSSODescriptor))
      determineIDPSSODescriptorProtocolSupport(descriptor)
    if(descriptor.instanceOf(AttributeAuthorityDescriptor))
      determineAttributeAuthorityProtocolSupport(descriptor)
    if(descriptor.instanceOf(SPSSODescriptor))
      determineSPSSODescriptorProtocolSupport(descriptor)
  }
  
  def determineSPSSODescriptorProtocolSupport(sp) {
    sp.assertionConsumerServices?.findAll{ it.active }.each {
      determineProtocolSupport(it.binding, sp)
    }
    sp.manageNameIDServices?.findAll{ it.active }.each {
      determineProtocolSupport(it.binding, sp)
    }
    sp.singleLogoutServices?.findAll{ it.active }.each {
      determineProtocolSupport(it.binding, sp)
    }
    sp.artifactResolutionServices?.findAll{ it.active }.each {
      determineProtocolSupport(it.binding, sp)
    }
    
    log.debug "Determined current SP protocol support of ${sp.protocolSupportEnumerations}"
  }
  
  def determineIDPSSODescriptorProtocolSupport(idp) {
    idp.singleSignOnServices?.findAll{ it.active }.each {
      determineProtocolSupport(it.binding, idp)
    }
    idp.artifactResolutionServices?.findAll{ it.active }.each {
      determineProtocolSupport(it.binding, idp)
    }
    idp.singleLogoutServices?.findAll{ it.active }.each {
      determineProtocolSupport(it.binding, idp)
    }
    idp.assertionIDRequestServices?.findAll{ it.active }.each {
      determineProtocolSupport(it.binding, idp)
    }
    idp.nameIDMappingServices?.findAll{ it.active }.each {
      determineProtocolSupport(it.binding, idp)
    }
    idp.manageNameIDServices?.findAll{ it.active }.each {
      determineProtocolSupport(it.binding, idp)
    }

    if(idp.collaborator) {
      determineAttributeAuthorityProtocolSupport(idp.collaborator)
    }
    
    log.debug "Determined current IDP protocol support of ${idp.protocolSupportEnumerations}"
  }
  
  def determineAttributeAuthorityProtocolSupport(def aa) {
    aa.attributeServices.findAll{ it.active }?.each {
      determineProtocolSupport(it.binding, aa)
    }
    aa.assertionIDRequestServices.findAll{ it.active }?.each {
      determineProtocolSupport(it.binding, aa)
    }
    
    log.debug "Determined current AA protocol support of ${aa.protocolSupportEnumerations}"
  }
  
  def determineProtocolSupport(binding, descriptor) {
    def saml2Namespace = SamlURI.findWhere(uri:'urn:oasis:names:tc:SAML:2.0:protocol')
    def saml1Namespace = SamlURI.findWhere(uri:'urn:oasis:names:tc:SAML:1.1:protocol')
    def shibboleth1Namespace = SamlURI.findWhere(uri:'urn:mace:shibboleth:1.0')
    
    log.debug "Determining protocol support for ${descriptor} based on ${binding}"

    if(binding.uri.contains('urn:oasis:names:tc:SAML:2.0') && !descriptor.protocolSupportEnumerations?.contains(saml2Namespace)) {
      log.debug "Adding support to $descriptor for $saml2Namespace"
      descriptor.addToProtocolSupportEnumerations(saml2Namespace)
    }

    if(binding.uri.contains('urn:oasis:names:tc:SAML:1.0') && !descriptor.protocolSupportEnumerations?.contains(saml1Namespace)) {
      log.debug "Adding support to $descriptor for $saml1Namespace"
      descriptor.addToProtocolSupportEnumerations(saml1Namespace)
    }

    if(binding.uri.contains('urn:mace:shibboleth:1.0') && !descriptor.protocolSupportEnumerations?.contains(shibboleth1Namespace)) {
      log.debug "Adding support to $descriptor for $shibboleth1Namespace"
      descriptor.addToProtocolSupportEnumerations(shibboleth1Namespace)
      if(!descriptor.protocolSupportEnumerations.contains(saml1Namespace)) {
        log.debug "Adding support to $descriptor for $saml1Namespace due to need to support $shibboleth1Namespace"
        descriptor.addToProtocolSupportEnumerations(saml1Namespace)
      }
    }
  }
}
