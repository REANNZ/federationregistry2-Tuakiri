package fedreg.core

import org.apache.shiro.SecurityUtils

class SPSSODescriptorController {
	static allowedMethods = [save: "POST", update: "POST", delete: "POST"]
	static defaultAction = "list"
	
	def SPSSODescriptorService
	
	def list = {
		params.max = Math.min(params.max ? params.max.toInteger() : 20, 100)
		[serviceProviderList: SPSSODescriptor.list(params), serviceProviderTotal: SPSSODescriptor.count()]
	}
	
	def show = {
		if(!params.id) {
			log.warn "SPSSODescriptor ID was not present"
			flash.type="error"
			flash.message = message(code: 'fedreg.controllers.namevalue.missing')
			redirect(action: "list")
			return
		}
		
		def serviceProvider = SPSSODescriptor.get(params.id)
		if (!serviceProvider) {
			flash.type="error"
			flash.message = message(code: 'fedreg.core.spssoroledescriptor.nonexistant')
			redirect(action: "list")
			return
		}
		
		def attributes = AttributeBase.list()
		def specAttr = AttributeBase.findAllWhere(specificationRequired:true)

		[serviceProvider: serviceProvider, contactTypes:ContactType.list(), availableAttributes:attributes, specificationAttributes: specAttr]
	}
	
	def create = {
		if(SecurityUtils.subject.isPermitted("spssodescriptor:create")) {
			def serviceProvider = new SPSSODescriptor()
			[serviceProvider: serviceProvider, organizationList: Organization.findAllWhere(active:true, approved:true), attributeList: AttributeBase.list(), nameIDFormatList: SamlURI.findAllWhere(type:SamlURIType.NameIdentifierFormat)]
		}
		else {
			log.warn("Attempt to create service provider by $authenticatedUser was denied, incorrect permission set")
			response.sendError(403)
		}
	}
	
	def save = {
		if(SecurityUtils.subject.isPermitted("organization:${params.organization.id}:components:spssodescriptor:create")) {
			def (created, organization, entityDescriptor, serviceProvider, httpPostACS, soapArtifactACS, sloArtifact, sloRedirect, sloSOAP, sloPost, organizationList, attributeList, nameIDFormatList, contact) = SPSSODescriptorService.create(params)
		
			if(created)
				redirect (action: "show", id: serviceProvider.id)
			else {
				flash.type="error"
				flash.message = message(code: 'fedreg.core.spssoroledescriptor.save.validation.error')
				render (view:'create', model:[organization:organization, entityDescriptor:entityDescriptor, serviceProvider:serviceProvider, httpPostACS:httpPostACS, soapArtifactACS:soapArtifactACS, contact:contact,
											sloArtifact:sloArtifact, sloRedirect:sloRedirect, sloSOAP:sloSOAP, sloPost:sloPost, organizationList:organizationList, attributeList:attributeList, nameIDFormatList:nameIDFormatList])
			}
		}
		else {
			log.warn("Attempt to save service provider by $authenticatedUser was denied, incorrect permission set")
			response.sendError(403)
		}
	}
	
	def edit = {
		if(!params.id) {
			log.warn "SPSSODescriptor ID was not present"
			flash.type="error"
			flash.message = message(code: 'fedreg.controllers.namevalue.missing')
			redirect(action: "list")
			return
		}
		
		def serviceProvider = SPSSODescriptor.get(params.id)
		if (!serviceProvider) {
			flash.type="error"
			flash.message = message(code: 'fedreg.core.spssoroledescriptor.nonexistant')
			redirect(action: "list")
			return
		}	
		
		if(SecurityUtils.subject.isPermitted("descriptor:${serviceProvider.id}:update")) {
			[serviceProvider: serviceProvider]	
		}
		else {
			log.warn("Attempt to edit $serviceProvider by $authenticatedUser was denied, incorrect permission set")
			response.sendError(403)
		}	
	}
	
	def update = {
		if(!params.id) {
			log.warn "SPSSODescriptor ID was not present"
			flash.type="error"
			flash.message = message(code: 'fedreg.controllers.namevalue.missing')
			redirect(action: "list")
			return
		}
		
		def serviceProvider_ = SPSSODescriptor.get(params.id)
		if (!serviceProvider_) {
			flash.type="error"
			flash.message = message(code: 'fedreg.core.spssoroledescriptor.nonexistant')
			redirect(action: "list")
			return
		}
		if(SecurityUtils.subject.isPermitted("descriptor:${serviceProvider_.id}:update")) {
			def (updated, serviceProvider) = SPSSODescriptorService.update(params)
			if(updated)
				redirect (action: "show", id: serviceProvider.id)
			else {
				flash.type="error"
				flash.message = message(code: 'fedreg.core.spssoroledescriptor.update.validation.error')
				render (view:'edit', model:[serviceProvider:serviceProvider])
			}
		}
		else {
			log.warn("Attempt to update $serviceProvider by $authenticatedUser was denied, incorrect permission set")
			response.sendError(403)
		}
	}
}