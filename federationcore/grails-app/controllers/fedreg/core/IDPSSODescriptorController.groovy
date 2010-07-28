package fedreg.core

class IDPSSODescriptorController {

	static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

	def defaultAction = "list"
	
	def IDPSSODescriptorService

	def list = {
		params.max = Math.min(params.max ? params.max.toInteger() : 20, 100)
		[identityProviderList: IDPSSODescriptor.list(params), identityProviderTotal: IDPSSODescriptor.count()]
	}

	def show = {
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

		[identityProvider: identityProvider, contactTypes:ContactType.list()]
	}
	
	def create = {
		def identityProvider = new IDPSSODescriptor()
		[identityProvider: identityProvider, organizationList: Organization.list(), attributeList: Attribute.list(), nameIDFormatList: SamlURI.findAllWhere(type:SamlURIType.NameIdentifierFormat)]
	}
	
	def save = {
		def (created, organization, entityDescriptor, identityProvider, attributeAuthority, httpPost, httpRedirect, soapArtifact, organizationList, attributeList, nameIDFormatList, contact) = IDPSSODescriptorService.create(params)
		
		if(created)
			redirect (action: "show", id: identityProvider.id)
		else
			render (view:'create', model:[organization:organization, entityDescriptor:entityDescriptor, identityProvider:identityProvider, attributeAuthority:attributeAuthority, httpPost:httpPost, httpRedirect:httpRedirect, 
			soapArtifact:soapArtifact, organizationList:organizationList, attributeList:attributeList, nameIDFormatList:nameIDFormatList, contact:contact])
	}
}
