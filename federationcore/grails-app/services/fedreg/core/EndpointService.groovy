package fedreg.core

import static org.apache.commons.lang.StringUtils.*

class EndpointService {
	
	def grailsApplication

	def create(def descriptor, def endpointClass, def endpointType, def binding, def loc) {
		log.info "Creating endpoint for ${descriptor} of type ${endpointType} at location ${loc}"
		
		def location = new UrlURI(uri:loc)
		def endpoint = grailsApplication.classLoader.loadClass(endpointClass).newInstance(descriptor:descriptor, binding: binding, location: location, active:true, approved:true)
		descriptor."addTo${capitalize(endpointType)}"(endpoint)
		
		def saml2Namespace = SamlURI.findWhere(uri:'urn:oasis:names:tc:SAML:2.0:protocol')
		def saml1Namespace = SamlURI.findWhere(uri:'urn:oasis:names:tc:SAML:1.1:protocol')
		def shibboleth1Namespace = SamlURI.findWhere(uri:'urn:mace:shibboleth:1.0')
		
		if(binding.uri.contains('urn:oasis:names:tc:SAML:2.0') && !descriptor.protocolSupportEnumerations.contains(saml2Namespace))
			descriptor.addToProtocolSupportEnumerations(saml2Namespace)
			
		if(binding.uri.contains('urn:oasis:names:tc:SAML:1.0') && !descriptor.protocolSupportEnumerations.contains(saml1Namespace)) {
			descriptor.addToProtocolSupportEnumerations(saml1Namespace)
		}
			
		if(binding.uri.contains('urn:mace:shibboleth:1.0') && !descriptor.protocolSupportEnumerations.contains(shibboleth1Namespace)) {
			descriptor.addToProtocolSupportEnumerations(shibboleth1Namespace)
			if(!descriptor.protocolSupportEnumerations.contains(saml1Namespace))
				descriptor.addToProtocolSupportEnumerations(saml1Namespace)
		}

		descriptor.save()
		if(descriptor.hasErrors() || endpoint.hasErrors()) {
			descriptor.errors?.each {
				log.error it
			}
			endpoint.errors?.each {
				log.error it
			}
			throw new RuntimeException("Unable to save when creating ${endpoint} for ${descriptor}")
		}
	}
	
	def makeDefault(def endpoint, def endpointType) {
		def descriptor = endpoint.descriptor
		descriptor."$endpointType".each {
			if(it.isDefault) {
				it.isDefault = false
				it.save()
				if(it.hasErrors()) {
					it.errors.each {log.error}
					throw new RuntimeException("Unable to save when setting default for ${endpoint} for ${descriptor}")
				}
			}
		}
		endpoint.isDefault = true
		endpoint.save()
	}
	
	def toggle(def endpoint) {
		log.info "Toggling state of ${endpoint}"
		
		endpoint.active = !endpoint.active
		endpoint.save()
		if(endpoint.hasErrors()) {
			endpoint.errors.each {
				log.error it
			}
			throw new RuntimeException("Unable to save when toggling active state for ${endpoint}")
		}
	}
	
	def delete(def endpoint, def endpointType) {
		log.info "Deleting ${endpoint}"
		
		def descriptor = endpoint.descriptor
		descriptor."removeFrom${capitalize(endpointType)}"(endpoint)
		descriptor.save()
		
		if(descriptor.hasErrors()) {
			descriptor.errors.each {
				log.error it
			}
			throw new RuntimeException("Unable to save when removing ${endpoint} from ${descriptor}")
		}
		
		endpoint.delete()
	}
}