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
		if(!endpoint.responseLocation)
			builder."$type"(Binding: endpoint.binding.uri, Location:endpoint.location.uri)
		else
			builder."$type"(Binding: endpoint.binding.uri, Location:endpoint.location.uri, ResponseLocation: endpoint.responseLocation.uri)
	}
	
	def indexedEndpoint(builder, type, endpoint, index) { 
		if(!endpoint.responseLocation)
			builder."$type"(Binding: endpoint.binding.uri, Location:endpoint.location.uri, index:index)
		else
			builder."$type"(Binding: endpoint.binding.uri, Location:endpoint.location.uri, ResponseLocation: endpoint.responseLocation.uri, index:index)
	}
	
	def idpSSODescriptor(builder, idpSSODescriptor) {
		builder.IDPSSODescriptor(protocolSupportEnumeration: idpSSODescriptor.protocolSupportEnumerations.collect({it.uri}).join(' ')) {
			idpSSODescriptor.keyDescriptors.sort{it.keyType}.each{keyDescriptor(builder, it)}
			organization(builder, idpSSODescriptor.organization)
			idpSSODescriptor.contacts.each{contactPerson(builder, it)}
			
			// SSODescriptorType
		}
	}
	
}