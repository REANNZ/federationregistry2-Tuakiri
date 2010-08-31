package fedreg.core

import static org.apache.commons.lang.StringUtils.*

class EndpointService {
	
	def grailsApplication

	def create(def descriptor, def endpointClass, def endpointType, def binding, def loc) {
		log.info "Creating endpoint for ${descriptor} of type ${endpointType} at location ${loc}"
		
		def location = new UrlURI(uri:loc)
		def endpoint = grailsApplication.classLoader.loadClass(endpointClass).newInstance(descriptor:descriptor, binding: binding, location: location, active:true)
		descriptor."addTo${capitalize(endpointType)}"(endpoint)

		descriptor.save()
		if(descriptor.hasErrors() || endpoint.hasErrors()) {
			descriptor.errors?.each {
				log.error it
			}
			endpoint.errors?.each {
				log.error it
			}
			// throw RTE
		}
	}
	
	def toggle(def endpoint) {
		log.info "Toggling state of ${endpoint}"
		
		endpoint.active = !endpoint.active
		endpoint.save()
		if(endpoint.hasErrors()) {
			endpoint.errors.each {
				log.error it
			}
			// throw RTE
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
			// throw RTE
		}
		
		endpoint.delete()
	}
}