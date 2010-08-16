package fedreg.core

import fedreg.workflow.ProcessPriority

class BootstrapController {
	
	def IDPSSODescriptorService
	def SPSSODescriptorService
	def organizationService
	
	static allowedMethods = [saveIDP: "POST", update: "POST", delete: "POST"]
	
	def idp = {
		def identityProvider = new IDPSSODescriptor()
		[identityProvider: identityProvider, organizationList: Organization.list(), attributeList: Attribute.list(), nameIDFormatList: SamlURI.findAllWhere(type:SamlURIType.NameIdentifierFormat)]
	}
	
	def idpregistered = {
		if(!params.id) {
			log.warn "IDPSSODescriptor ID was not present"
			flash.type="error"
			flash.message = message(code: 'fedreg.controllers.namevalue.missing')
			redirect(action: "list")
			return
		}
		
		def identityProvider = IDPSSODescriptor.get(params.id)
		if (!identityProvider) {
			flash.type="error"
			flash.message = message(code: 'fedreg.core.idpssoroledescriptor.nonexistant')
			redirect( "/" )
			return
		}

		[identityProvider: identityProvider]
	}
	
	def saveidp = {
		def (created, organization, entityDescriptor, identityProvider, attributeAuthority, httpPost, httpRedirect, soapArtifact, organizationList, attributeList, nameIDFormatList, contact) = IDPSSODescriptorService.create(params)
		
		if(created)
			redirect (action: "idpregistered", id: identityProvider.id)
		else
			render (view:'idp', model:[organization:organization, entityDescriptor:entityDescriptor, identityProvider:identityProvider, attributeAuthority:attributeAuthority, httpPost:httpPost, httpRedirect:httpRedirect, 
			soapArtifact:soapArtifact, organizationList:organizationList, attributeList:attributeList, nameIDFormatList:nameIDFormatList, contact:contact])
	}
	
	def sp = {
		def serviceProvider = new SPSSODescriptor()
		[serviceProvider: serviceProvider, organizationList: Organization.list(), attributeList: AttributeBase.list(), nameIDFormatList: SamlURI.findAllWhere(type:SamlURIType.NameIdentifierFormat)]
	}
	
	def savesp = {
		def (created, organization, entityDescriptor, serviceProvider, httpPostACS, soapArtifactACS, sloArtifact, sloRedirect, sloSOAP, sloPost, organizationList, attributeList, nameIDFormatList, contact) = SPSSODescriptorService.create(params)
		
		if(created)
			redirect (action: "spregistered", id: serviceProvider.id)
		else {
			flash.type="error"
			flash.message = message(code: 'fedreg.core.spssoroledescriptor.save.validation.error')
			render (view:'sp', model:[organization:organization, entityDescriptor:entityDescriptor, serviceProvider:serviceProvider, httpPostACS:httpPostACS, soapArtifactACS:soapArtifactACS, contact:contact,
										sloArtifact:sloArtifact, sloRedirect:sloRedirect, sloSOAP:sloSOAP, sloPost:sloPost, organizationList:organizationList, attributeList:attributeList, nameIDFormatList:nameIDFormatList])
		}
	}
	
	def spregistered = {
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
			redirect( "/" )
			return
		}

		[serviceProvider: serviceProvider]
	}
	
	def organization = {
		def organization = new Organization()
		[organization:organization, organizationTypes: OrganizationType.list()]
	}
	
	def organizationregistered = {
		if(!params.id) {
			log.warn "Organization ID was not present"
			flash.type="error"
			flash.message = message(code: 'fedreg.controllers.namevalue.missing')
			redirect(action: "list")
			return
		}
		
		def organization = Organization.get(params.id)
		if (!organization) {
			flash.type="error"
			flash.message = message(code: 'fedreg.core.organization.nonexistant')
			redirect(action: "list")
			return
		}

		[organization: organization]
	}
	
	def saveorganization = {
		def (created, organization, contact) = organizationService.create(params)
		
		if(created)
			redirect (action: "organizationregistered", id: organization.id)
		else
			render (view:'organization', model:[organization:organization, contact:contact])
	}
	
}