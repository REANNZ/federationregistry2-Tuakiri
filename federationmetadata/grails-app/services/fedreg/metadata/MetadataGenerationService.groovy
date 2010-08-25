package fedreg.metadata

import groovy.xml.MarkupBuilder

class MetadataGenerationService {
	
	def localizedName(builder, type, lang, content) {
		builder."$type"('xml:lang':lang, content)
	}

	def localizedUri(builder, type, lang, content) {
		builder."$type"('xml:lang':lang, content)
	}
	
	def organization(builder, organization) {
		builder.Organization() {
			localizedName(builder, "OrganizationName", organization.lang, organization.name)
			localizedName(builder, "OrganizationDisplayName", organization.lang, organization.displayName)
			localizedUri(builder, "OrganizationURL", organization.lang, organization.url.uri)
		}
	}
	
	def contactPerson(builder, contactPerson) {
		builder.ContactPerson(contactType:contactPerson.type.name) {
			if(contactPerson.contact.organization)
				Company(contactPerson.contact.organization.displayName)
			GivenName(contactPerson.contact.givenName)
			SurName(contactPerson.contact.surname)
			EmailAddress(contactPerson.contact.email.uri)
			if(contactPerson.contact.workPhone)
				TelephoneNumber(contactPerson.contact.workPhone.uri)
			if(contactPerson.contact.homePhone)
				TelephoneNumber(contactPerson.contact.homePhone.uri)
			if(contactPerson.contact.mobilePhone)
				TelephoneNumber(contactPerson.contact.mobilePhone.uri)
		}
	}
	
	def keyDescriptor(builder, keyDescriptor) {
		builder.KeyDescriptor(use: keyDescriptor.keyType) {
			'ds:KeyInfo'('xmlns:ds':'http://www.w3.org/2000/09/xmldsig#') {
				if(keyDescriptor.keyInfo.keyName)
					'ds:KeyName'(keyDescriptor.keyInfo.keyName)
				'ds:X509Data'() {
					def data = keyDescriptor.keyInfo.certificate.data
					if(data.startsWith('-----BEGIN CERTIFICATE-----'))
						data = data.replace('-----BEGIN CERTIFICATE-----', '')
					if(data.endsWith('-----END CERTIFICATE-----'))
						data = data.replace('-----END CERTIFICATE-----', '')
					'ds:X509Certificate'(data)
				}
			}
			if(keyDescriptor.encryptionMethod) {
				EncryptionMethod(Algorithm:keyDescriptor.encryptionMethod.algorithm) {
					if(keyDescriptor.encryptionMethod.keySize)
						'xenc:KeySize'('xmlns:xenc':'http://www.w3.org/2001/04/xmlenc#', keyDescriptor.encryptionMethod.keySize)
					if(keyDescriptor.encryptionMethod.oaeParams)
					'xenc:OAEPparams'('xmlns:xenc':'http://www.w3.org/2001/04/xmlenc#', keyDescriptor.encryptionMethod.oaeParams)
				}
			}
		}
	}
		
	def endpoint(builder, type, endpoint) {
		if(endpoint.active && endpoint.approved) {
			if(!endpoint.responseLocation)
				builder."$type"(Binding: endpoint.binding.uri, Location:endpoint.location.uri)
			else
				builder."$type"(Binding: endpoint.binding.uri, Location:endpoint.location.uri, ResponseLocation: endpoint.responseLocation.uri)
		}
	}
	
	def indexedEndpoint(builder, type, endpoint, index) { 
		if(endpoint.active && endpoint.approved) {
			if(!endpoint.responseLocation)
				builder."$type"(Binding: endpoint.binding.uri, Location:endpoint.location.uri, index:index, isDefault:endpoint.isDefault)
			else
				builder."$type"(Binding: endpoint.binding.uri, Location:endpoint.location.uri, ResponseLocation: endpoint.responseLocation.uri, index:index, isDefault:endpoint.isDefault)
		}
	}
	
	def samlURI(builder, type, uri) {
		builder."$type"(uri.uri)
	}
	
	def attribute(builder, attr) {
		builder.'saml:Attribute'(Name: attr.base.name, NameFormat:attr.base.nameFormat?.uri, FriendlyName:attr.base.friendlyName) {
			attr.values?.sort{it?.value}.each {
				'saml:AttributeValue'(it.value)
			}
		}
	}
	
	def requestedAttribute(builder, attr) {
		if(attr.approved) {
			builder.RequestedAttribute(Name: attr.base.name, NameFormat:attr.base.nameFormat?.uri, FriendlyName:attr.base.friendlyName, isRequired:attr.isRequired) {
				attr.values?.sort{it?.value}.each {
					'saml:AttributeValue'(it.value)
				}
			}
		}
	}
	
	def attributeConsumingService(builder, acs, index) {
		builder.AttributeConsumingService(index:index, isDefault:acs.isDefault) {
			acs.serviceNames?.sort{it}.each {
				localizedName(builder, "ServiceName", acs.lang, it)
			}
			acs.serviceDescriptions?.sort{it}.each {
				localizedName(builder, "ServiceDescription", acs.lang, it)
			}
			acs.requestedAttributes?.sort{it.base.name}.each{ attr -> requestedAttribute(builder, attr)}
		}
	}
	
	def roleDescriptor(builder, roleDescriptor) {
		roleDescriptor.keyDescriptors?.sort{it.keyType}.each{keyDescriptor(builder, it)}		
		organization(builder, roleDescriptor.organization)
		roleDescriptor.contacts?.sort{it.contact.email.uri}.each{cp -> contactPerson(builder, cp)}
	}
	
	def ssoDescriptor(builder, ssoDescriptor) {
		ssoDescriptor.artifactResolutionServices?.sort{it.location.uri}.eachWithIndex{ars, i -> indexedEndpoint(builder, "ArtifactResolutionService", ars, i+1)}
		ssoDescriptor.singleLogoutServices?.sort{it.location.uri}.each{sls -> endpoint(builder, "SingleLogoutService", sls)}
		ssoDescriptor.manageNameIDServices?.sort{it.location.uri}.each{mnids -> endpoint(builder, "ManageNameIDService", mnids)}
		ssoDescriptor.nameIDFormats?.sort{it.location.uri}.each{nidf -> samlURI(builder, "NameIDFormat", nidf)}
	}
	
	def idpSSODescriptor(builder, idpSSODescriptor) {
		builder.IDPSSODescriptor(protocolSupportEnumeration: idpSSODescriptor.protocolSupportEnumerations.sort{it.uri}.collect({it.uri}).join(' '), WantAuthnRequestsSigned:idpSSODescriptor.wantAuthnRequestsSigned) {
			roleDescriptor(builder, idpSSODescriptor)
			ssoDescriptor(builder, idpSSODescriptor)
			
			idpSSODescriptor.singleSignOnServices.sort{it.location.uri}.each{ sso -> endpoint(builder, "SingleSignOnService", sso) }
			idpSSODescriptor.nameIDMappingServices.sort{it.location.uri}.each{ nidms -> endpoint(builder, "NameIDMappingService", nidms) }
			idpSSODescriptor.assertionIDRequestServices.sort{it.location.uri}.each{ aidrs -> endpoint(builder, "AssertionIDRequestService", aidrs) }
			idpSSODescriptor.attributeProfiles?.sort{it.location.uri}.each{ ap -> samlURI(builder, "AttributeProfile", ap) }
			idpSSODescriptor.attributes.sort{it.base.name}.each{ attr -> attribute(builder, attr)}
		}
	}
	
	def spSSODescriptor(builder, spSSODescriptor) {
		builder.SPSSODescriptor(protocolSupportEnumeration: spSSODescriptor.protocolSupportEnumerations.sort{it.uri}.collect({it.uri}).join(' '), AuthnRequestsSigned:spSSODescriptor.authnRequestsSigned, WantAssertionsSigned:spSSODescriptor.wantAssertionsSigned) {
			roleDescriptor(builder, spSSODescriptor)
			ssoDescriptor(builder, spSSODescriptor)
			
			spSSODescriptor.assertionConsumerServices.sort{it.location.uri}.eachWithIndex{ ars, i -> indexedEndpoint(builder, "AssertionConsumerService", ars, i+1) }
			spSSODescriptor.attributeConsumingServices.sort{it.id}.eachWithIndex{ acs, i -> attributeConsumingService(builder, acs, i) }
		}
	}
	
	def attributeAuthorityDescriptor(builder, aaDescriptor) {
		builder.AttributeAuthorityDescriptor(protocolSupportEnumeration: aaDescriptor.protocolSupportEnumerations.sort{it.uri}.collect({it.uri}).join(' ')) {
			if(aaDescriptor.collaborator) {
				// We don't currently provide direct AA manipulation to reduce general end user complexity.
				// So where a collaborative relationship exists we use all common data from the IDP to render the AA
				roleDescriptor(builder, aaDescriptor.collaborator)	
				aaDescriptor.attributeServices?.sort{it.location.uri}.each{ attrserv -> endpoint(builder, "AttributeService", attrserv) }
				aaDescriptor.collaborator.assertionIDRequestServices?.sort{it.location.uri}.each{ aidrs -> endpoint(builder, "AssertionIDRequestService", aidrs) }
				aaDescriptor.collaborator.nameIDFormats?.sort{it.location.uri}.each{ nidf -> samlURI(builder, "NameIDFormat", nidf) }
				aaDescriptor.collaborator.attributeProfiles?.sort{it.location.uri}.each{ ap -> samlURI(builder, "AttributeProfile", ap) }
				aaDescriptor.collaborator.attributes?.sort{it.base.name}.each{ attr -> attribute(builder, attr) }
			}
			else {
				roleDescriptor(builder, aaDescriptor)	
				aaDescriptor.attributeServices?.sort{it.location.uri}.each{ attrserv -> endpoint(builder, "AttributeService", attrserv) }
				aaDescriptor.assertionIDRequestServices?.sort{it.location.uri}.each{ aidrs -> endpoint(builder, "AssertionIDRequestService", aidrs) }
				aaDescriptor.nameIDFormats?.sort{it.location.uri}.each{ nidf -> samlURI(builder, "NameIDFormat", nidf) }
				aaDescriptor.attributeProfiles?.sort{it.location.uri}.each{ ap -> samlURI(builder, "AttributeProfile", ap) }
				aaDescriptor.attributes?.sort{it.base.name}.each{ attr -> attribute(builder, attr) }
			}
		}
	}
	
}