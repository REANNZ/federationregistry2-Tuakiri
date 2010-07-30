package fedreg.core

import fedreg.workflow.ProcessPriority

class BootstrapController {
	
	static allowedMethods = [saveIDP: "POST", update: "POST", delete: "POST"]
	
	def IDPSSODescriptorService
	
	def show = {
		
	}
	
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
			redirect(action: "list")
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
		
		def identityProvider = IDPSSODescriptor.get(params.id)
		if (!identityProvider) {
			flash.type="error"
			flash.message = message(code: 'fedreg.core.organization.nonexistant')
			redirect(action: "list")
			return
		}

		[organization: organization]
	}
	
	def saveorganization = {
		def (created, organization, contact) = OrganizationService.create(params)
		
		if(created)
			redirect (action: "organizationregistered", id: organization.id)
		else
			render (view:'organization', model:[organization:organization, contact:contact])
	}
	
}