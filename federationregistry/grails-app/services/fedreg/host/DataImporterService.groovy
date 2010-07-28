package fedreg.host

import org.springframework.beans.factory.InitializingBean
import groovy.sql.Sql

import fedreg.core.*

class DataImporterService implements InitializingBean {

    boolean transactional = true

	def grailsApplication
	def sql
	def cryptoService

	// This entire service is a temporary piece of code to assist with a slow orderly migration from a PHP based resource registry to a new generation Grails version
	// It is anticipated that over time all data being sourced from the RR will be created in this app and this service and all methods will be totally discontinued
	
	void afterPropertiesSet()
    {
		def oldrr = grailsApplication.config.fedreg.oldrr		
        sql = Sql.newInstance(oldrr.connection, oldrr.user, oldrr.password, oldrr.driver)	
    }

	def populate(def request) {
		importCACertificates()
		importOrganizations()
		importContacts()
		importAttributes()
		importEntities()
		importIDPSSODescriptors()
		importAttributeAuthorityDescriptors()
		importSPSSODescriptors()
		
		if(request)
		{
			def dataLoadRecord = new DataLoadRecord(invoker:authenticatedUser?:null, remoteAddr: request.getRemoteAddr(), remoteHost: request.getRemoteHost(), userAgent:request.getHeader("User-Agent"))
			dataLoadRecord.save()
		}
	}
	
	def initialPopulate() {		
		// Attribute Scopes (Non standard)	
		def fedScope = new AttributeScope(name:'Federation').save()
		def localScope = new AttributeScope(name:'Local').save()
		def mandatoryCategory = new AttributeCategory(name:'Mandatory').save()
		def recommendedCategory = new AttributeCategory(name:'Recommended').save()
		def optionalCategory = new AttributeCategory(name:'Optional').save()
		
		// Overall SAML2 protocol support, all SAML 2.0 compliant RoleDescriptors need this.
		def samlNamespace = new SamlURI(type:SamlURIType.ProtocolSupport, uri:'urn:oasis:names:tc:SAML:2.0:protocol').save()
		
		// Bindings
		def httpRedirect = new SamlURI(type:SamlURIType.ProtocolBinding, uri:'urn:oasis:names:tc:SAML:2.0:bindings:HTTP-Redirect', description:'').save()
		def httpPost = new SamlURI(type:SamlURIType.ProtocolBinding, uri:'urn:oasis:names:tc:SAML:2.0:bindings:HTTP-POST', description:'').save()
		def httpArtifact = new SamlURI(type:SamlURIType.ProtocolBinding, uri:'urn:oasis:names:tc:SAML:2.0:bindings:HTTP-Artifact', description:'').save()
		def httpPostSimple = new SamlURI(type:SamlURIType.ProtocolBinding, uri:'urn:oasis:names:tc:SAML:2.0:bindings:HTTP-POST-SimpleSign', description:'').save()
		def paos = new SamlURI(type:SamlURIType.ProtocolBinding, uri:'urn:oasis:names:tc:SAML:2.0:bindings:PAOS', description:'').save()
		def soap = new SamlURI(type:SamlURIType.ProtocolBinding, uri:'urn:oasis:names:tc:SAML:2.0:bindings:SOAP', description:'').save()
		def shibAuthn = new SamlURI(type:SamlURIType.ProtocolBinding, uri:'urn:mace:shibboleth:1.0:profiles:AuthnRequest', description:'').save()
		def httpPost1 = new SamlURI(type:SamlURIType.ProtocolBinding, uri:'urn:oasis:names:tc:SAML:1.0:profiles:browser-post', description:'').save()
		def httpArtifact1 = new SamlURI(type:SamlURIType.ProtocolBinding, uri:'urn:oasis:names:tc:SAML:1.0:profiles:artifact-01', description:'').save()
		def soap1 = new SamlURI(type:SamlURIType.ProtocolBinding, uri:'urn:oasis:names:tc:SAML:1.0:bindings:SOAP-binding', description:'').save()
		
		// NameIDFormats
		def unspec = new SamlURI(type:SamlURIType.NameIdentifierFormat, uri:'urn:oasis:names:tc:SAML:1.1:nameid-format:unspecified', description:'The interpretation of the content of the element is left to individual implementations.').save()
		def email = new SamlURI(type:SamlURIType.NameIdentifierFormat, uri:'urn:oasis:names:tc:SAML:1.1:nameid-format:emailAddress', description:'Indicates that the content of the element is in the form of an email address, specifically addr-spec as defined in IETF RFC 2822').save()
		def x509 = new SamlURI(type:SamlURIType.NameIdentifierFormat, uri:'urn:oasis:names:tc:SAML:1.1:nameid-format:X509SubjectName', description:'Indicates that the content of the element is in the form specified for the contents of the <ds:X509SubjectName> element in the XML Signature Recommendation [XMLSig].').save()
		def windows = new SamlURI(type:SamlURIType.NameIdentifierFormat, uri:'urn:oasis:names:tc:SAML:1.1:nameid-format:WindowsDomainQualifiedName', description:'Indicates that the content of the element is a Windows domain qualified name. A Windows domain qualified user name is a string of the form "DomainName\\UserName"').save()
		def kerberos = new SamlURI(type:SamlURIType.NameIdentifierFormat, uri:'urn:oasis:names:tc:SAML:2.0:nameid-format:kerberos', description:'Indicates that the content of the element is in the form of a Kerberos principal name using the format name[/instance]@REALM').save()
		def entity = new SamlURI(type:SamlURIType.NameIdentifierFormat, uri:'urn:oasis:names:tc:SAML:2.0:nameid-format:entity', description:'Indicates that the content of the element is the identifier of an entity that provides SAML-based services or is a participant in SAML profiles.').save()
		def pers = new SamlURI(type:SamlURIType.NameIdentifierFormat, uri:'urn:oasis:names:tc:SAML:2.0:nameid-format:persistent', description:'Indicates that the content of the element is a persistent opaque identifier for a principal that is specific to an identity provider and a service provider or affiliation of service providers.').save()
		def trans = new SamlURI(type:SamlURIType.NameIdentifierFormat, uri:'urn:oasis:names:tc:SAML:2.0:nameid-format:transient', description:'Indicates that the content of the element is an identifier with transient semantics and SHOULD be treated as an opaque and temporary value by the relying party.').save()
		def shibNameID = new SamlURI(type:SamlURIType.NameIdentifierFormat, uri:'urn:mace:shibboleth:1.0:nameIdentifier', description:'').save()
		
		// Contact Types
		def tech = new ContactType(name:'technical', displayName:'Technical', description: 'Technical contacts').save()
		def mark = new ContactType(name:'marketing', displayName:'Marketing', description: 'Marketting contacts').save()
		def bill = new ContactType(name:'billing', displayName:'Billing', description: 'Billing contacts').save()
		def supp = new ContactType(name:'support', displayName:'Support', description: 'Support contacts').save()
		def admin = new ContactType(name:'administrative', displayName:'Administrative', description: 'Administrative contacts').save()
		def other = new ContactType(name:'other', displayName:'Other', description: 'Other contacts').save()
		
	}

	def dumpData() {
		log.debug("Executing dump data process")
		User.list().each { it.entityDescriptor = null; it.save(); }
		SPSSODescriptor.list().each { it.delete(); }
		AttributeAuthorityDescriptor.list().each { it.delete(); }
		IDPSSODescriptor.list().each { it.delete(); }
		EntityDescriptor.list().each { it.delete(); }
		Contact.list().each { it.delete(); }
		Attribute.list().each { it.delete(); }
		Organization.list().each { it.delete(); }
		OrganizationType.list().each { it.delete(); }
		CAKeyInfo.list().each { it.delete(); }
	}
	
	def importCACertificates() {
		sql.eachRow("select * from certData where objectType='issuingca'", {
			def data = "-----BEGIN CERTIFICATE-----\n${it.certData}\n-----END CERTIFICATE-----"
			def caCert = new CACertificate(data:data)
			def caKeyInfo = new CAKeyInfo(certificate:caCert)
			caKeyInfo.save()
		})
	}

	def importOrganizations() {
		sql.eachRow("select * from homeOrgTypes",
		{
			def orgType = new OrganizationType(name:it.homeOrgType, displayName:it.homeOrgType, description:it.descriptiveName)
			orgType.save()
		})
		
		sql.eachRow("select * from homeOrgs",
		{
			def orgType = OrganizationType.findByName(it.homeOrgType)
			def org = new Organization(name:it.homeOrgName, displayName:it.homeOrgName, lang:it.mainLanguage, url: new UrlURI(uri:it.errorURL ?:'http://aaf.edu.au/support'), primary: orgType)
			org.save()
		})
		
		sql.eachRow("select * from resources",
		{
			// If organization does not yet exist it must only be providing services in which case we mark as being of Type 'other'
			def org = Organization.findByName(it.homeOrg)
			if(!org) {
				log.debug "Loading organization which is only providing services ${it.homeOrg}"
				def orgType = OrganizationType.findByName('others')
				org = new Organization(name:it.homeOrg, displayName:it.homeOrg, lang:it.mainLanguage, url: new UrlURI(uri:it.errorURL ?:'http://aaf.edu.au/support'), primary: orgType)
				org.save()
			}
		})
	}

    def importAttributes() {
		sql.eachRow("select * from attributes", 
		{
			def scope = AttributeScope.findByName(it.scope)
			def category = AttributeCategory.findByName(it.status)
			def attr = new Attribute(id: it.attributeID, oid: it.attributeOID, name: it.attributeURN, friendlyName: it.attributeFullName, headerName: it.headerName, 
										alias:it.alias, description:it.description, scope: scope, category: category)
								
			def existingAttr = Attribute.get(attr.id)
			if(!existingAttr) {
				attr.save()
				if(attr.hasErrors()) {
					log.warn "Unable to import attribute identified by oid: $attr.oid and name $attr.name"
					attr.errors.each {
						log.warn it
					}
				}
			}
			else {
				// Update local version	to keep exisiting RR and our stuff in sync				
				attr.properties.each { k,v-> if (!k in ['class','id']) existingAttr."${k}" = v }
				existingAttr.save()
				if(existingAttr.hasErrors()) {
					log.warn "Unable to update values for attribute identified by oid: $existingAttr.oid and name $existingAttr.name"
					existingAttr.errors.each {
						log.warn it
					}
				}
			}
		})
    }

	def importContacts() {
		//Import contacts associated with 'homeOrgs'
		sql.eachRow("select email, contactName, homeOrgName from contacts LEFT JOIN homeOrgs ON contacts.objectID=homeOrgs.homeOrgID ORDER BY (email)",
		{
			def givenName, surname
			
			if(it.contactName.contains(" ")) {
				def names = it.contactName.split(" ")
				givenName = names[0]
				surname = names[1]
			}
			else {
				givenName = "N/A"
				surname = it.contactName
			}

			// Doesn't already exist (RR data is crapppppy!!) so create
			if(!MailURI.findByUri(it.email)) {
				def organization
				if(it.homeOrgName)
					organization = Organization.findByName(it.homeOrgName)
					
				def contact = new Contact(organization:organization, userLink:false, givenName:givenName, surname:surname, email:new MailURI(uri:it.email))
								
				contact.save(flush:true)
				if(contact.hasErrors()) {
					contact.errors.each { err -> log.error err}
				}
			}
			else {
				// Check if this is duplicate entry for this user and if so does this entry link them to an organization?
				def contact = Contact.findByEmail(MailURI.findByUri(it.email))
				if(!contact.organization) {
					if(it.homeOrgName) {
						def organization = Organization.findByName(it.homeOrgName)
						contact.organization = organization
						contact.save()
						if(contact.hasErrors()) {
							contact.errors.each { err -> log.error err}
						}
					}
				}
			}
		})
	}

	def importEntities() {
		log.debug "Importing entities from upstream resource registry"
		sql.eachRow("select * from homeOrgs",
		{
			def org = Organization.findByName(it.homeOrgName)
			def entity = new EntityDescriptor(entityID:it.entityID, organization:org, active:it.approved)
			entity.save()
			if(entity.hasErrors()) {
				entity.errors.each {log.error it}
			}
			log.debug "Imported entity ${entity.entityID}"
			
			// Create ContactPerson instances and link entities to contacts
			sql.eachRow("select email, contactType from contacts INNER JOIN homeOrgs ON contacts.objectID=homeOrgs.homeOrgID WHERE homeOrgs.entityID=${entity.entityID}",
			{
				linkContact(it, entity)
			})
			entity.save()
		})
		
		sql.eachRow("select * from resources",
		{
			def org = Organization.findByName(it.homeOrg)
			def entity = new EntityDescriptor(entityID:it.providerID, organization:org, active:it.visible).save()
			if(entity.hasErrors()) {
				entity.errors.each {log.error it}
			}
			log.debug "Imported entity ${entity.entityID}"
			
			// Create ContactPerson instances and link entities to contacts
			sql.eachRow("select email, contactType from contacts INNER JOIN resources ON contacts.objectID=resources.resourceID WHERE resources.providerID=${entity.entityID}",
			{
				linkContact(it, entity)
			})
			entity.save()
		})
	}
	
	def linkContact(def row, def ent) {
		def type
		def contact = MailURI.findByUri(row.email)?.contact
		if(contact) {
			type = ContactType.findByName(row.contactType)
			if(!type)
				type = ContactType.findByName('other')
			def contactPerson = new ContactPerson(contact:contact, entity:ent, type:type)
			ent.addToContacts(contactPerson)
			log.debug "Linked contact ${contactPerson?.contact?.givenName} ${contactPerson?.contact?.surname} to entity ${ent.entityID}"
		}
	}

	def importIDPSSODescriptors() {
		def samlNamespace = SamlURI.findByUri('urn:oasis:names:tc:SAML:2.0:protocol')
		def shibNameID = SamlURI.findByUri('urn:mace:shibboleth:1.0:nameIdentifier')
		def trans = SamlURI.findByUri('urn:oasis:names:tc:SAML:2.0:nameid-format:transient')
		
		sql.eachRow("select * from homeOrgs",
		{			
			def entity = EntityDescriptor.findWhere(entityID:it.entityID)
			def idp = new IDPSSODescriptor(active:true, entityDescriptor:entity, organization:entity.organization)
			idp.addToNameIDFormats(trans)	// RR hard codes everything to only advertise NameIDForm of transient so we need to do the same, in FR this is modifable and DB driven
			idp.addToProtocolSupportEnumerations(samlNamespace)
			
			sql.eachRow("select * from objectDescriptions where objectID=${it.homeOrgID} and objectType='homeOrg'",
			{
				idp.displayName = it.descriptiveName?:"N/A"
				idp.description = it.description?:"N/A"
			})
			
			sql.eachRow("select * from serviceLocations where objectID=${it.homeOrgID} and serviceType='SingleSignOnService'",
			{
				def binding = SamlURI.findByUri(it.serviceBinding)
				if(binding) {
					def location = new UrlURI(uri:it.serviceLocation)
					def ssoService = new SingleSignOnService(binding: binding, location: location, active:true)
					idp.addToSingleSignOnServices(ssoService)
				}
				else 
					log.warn ("No SamlURI binding for uri ${it.serviceBinding} exists, not importing, not importing SingleSignOnService")
			})
			def index = 0
			sql.eachRow("select * from serviceLocations where objectID=${it.homeOrgID} and serviceType='ArtifactResolutionService'",
			{
				def binding = SamlURI.findByUri(it.serviceBinding)
				if(binding) {
					def location = new UrlURI(uri:it.serviceLocation)
					def artServ = new ArtifactResolutionService(binding: binding, location: location, isDefault: it.defaultLocation, active:true, endpointIndex:index++)
					idp.addToArtifactResolutionServices(artServ)
				}
				else
					log.warn ("No SamlURI binding for uri ${it.serviceBinding} exists, not importing ArtifactResolutionService")
			})
			sql.eachRow("select attributeOID from attributes INNER JOIN homeOrgAttributes ON attributes.attributeID=homeOrgAttributes.attributeID where homeOrgAttributes.homeOrgID=${it.homeOrgID};",
			{
				def attr = Attribute.findByOid(it.attributeOID)
				if(attr)
					idp.addToAttributes(attr)
			})
			
			// RR doesn't store any flag to indicate that AA publishes encyption type in MD so we won't create an enc key.....
			importCrypto(it.homeOrgID, idp, false)
			
			entity.addToIdpDescriptors(idp)
			entity.save()
			if(entity.hasErrors()) {
				entity.errors.each {log.error it}
			}
			else
				log.debug "Added new IDPSSODescriptor to Entity ${entity.entityID}"
		})	
	}
	
	def importAttributeAuthorityDescriptors() {
		def samlNamespace = SamlURI.findByUri('urn:oasis:names:tc:SAML:2.0:protocol')
		def shibNameID = SamlURI.findByUri('urn:mace:shibboleth:1.0:nameIdentifier')
		def trans = SamlURI.findByUri('urn:oasis:names:tc:SAML:2.0:nameid-format:transient')
		
		sql.eachRow("select * from homeOrgs",
		{			
			def entity = EntityDescriptor.findWhere(entityID:it.entityID)
			def idp = entity.idpDescriptors.toList().get(0)	// We know entities in RR space are closely linked to both an IDP and AA descriptor
			def aa = new AttributeAuthorityDescriptor(active:true, entityDescriptor:entity, organization:entity.organization, displayName:idp.displayName, description:idp.description)
			aa.save()
			if(aa.hasErrors()) {
				aa.errors.each {log.error it}
			}
			aa.addToProtocolSupportEnumerations(samlNamespace)
			
			sql.eachRow("select * from serviceLocations where objectID=${it.homeOrgID} and serviceType='AttributeService'",
			{
				def binding = SamlURI.findByUri(it.serviceBinding)
				if(binding) {
					def location = new UrlURI(uri:it.serviceLocation)
					def attrService = new AttributeService(binding: binding, location: location, active:true)
					aa.addToAttributeServices(attrService)
				}
				else 
					log.warn ("No SamlURI binding for uri ${it.serviceBinding} exists, not importing, not importing AttributeService")
			})
			sql.eachRow("select attributeOID from attributes INNER JOIN homeOrgAttributes ON attributes.attributeID=homeOrgAttributes.attributeID where homeOrgAttributes.homeOrgID=${it.homeOrgID};",
			{
				def attr = Attribute.findByOid(it.attributeOID)
				if(attr)
					aa.addToAttributes(attr)
			})
			
			// RR doesn't store any flag to indicate that AA publishes encyption type in MD so we won't create an enc key.....
			importCrypto(it.homeOrgID, aa, false)
			
			entity.addToAttributeAuthorityDescriptors(aa)
			entity.save()
			if(entity.hasErrors()) {
				entity.errors.each {log.error it}
			}
			else
				log.debug "Added new AttributeAuthorityDescriptor to Entity ${entity.entityID}"
				
			aa.collaborator = idp
			aa.save()
			
			idp.collaborator = aa
			idp.save()
			
		})
	}

	def importSPSSODescriptors() {
		def samlNamespace = SamlURI.findByUri('urn:oasis:names:tc:SAML:2.0:protocol')
		def shibNameID = SamlURI.findByUri('urn:mace:shibboleth:1.0:nameIdentifier')
		def trans = SamlURI.findByUri('urn:oasis:names:tc:SAML:2.0:nameid-format:transient')
		
		sql.eachRow("select * from resources",
		{
			// RR hardcodes NameIDFormats so we need to figure out which version of SAML is supported(or even both) and manually add NameIDFormats
			boolean saml1 = false
			boolean saml2 = false
			
			def entity = EntityDescriptor.findWhere(entityID:it.providerID)
			def sd = new ServiceDescription()
			def sp = new SPSSODescriptor(active:true, entityDescriptor:entity, organization:entity.organization, visible:it.visible, serviceDescription:sd)
			sp.addToProtocolSupportEnumerations(samlNamespace)
			
			sql.eachRow("select * from objectDescriptions where objectID=${it.resourceID} and objectType='resource'",
			{
				sp.displayName = it.descriptiveName
				sp.description = it.description
			})

			sql.eachRow("select * from serviceLocations where objectID=${it.resourceID} and serviceType='SingleLogoutService'",
			{
				def binding = SamlURI.findByUri(it.serviceBinding)
				if(binding) {
					def location = new UrlURI(uri:it.serviceLocation)
					def sls = new SingleLogoutService(binding: binding, location: location, active:true)
					sp.addToSingleLogoutServices(sls)
					
					if(it.serviceBinding.contains('SAML:2.0'))
						saml2 = true
					if(it.serviceBinding.contains('SAML:1.0'))
						saml1 = true
				}
				else 
					log.warn ("No SamlURI binding for uri ${it.serviceBinding} exists, not importing, not importing SingleLogoutService")
			})
			def index = 0
			sql.eachRow("select * from serviceLocations where objectID=${it.resourceID} and serviceType='AssertionConsumerService'",
			{
				def binding = SamlURI.findByUri(it.serviceBinding)
				if(binding) {
					def location = new UrlURI(uri:it.serviceLocation)
					def acs = new AssertionConsumerService(binding: binding, location: location, isDefault: it.defaultLocation, active:true, endpointIndex:index++)
					sp.addToAssertionConsumerServices(acs)
					
					if(it.serviceBinding.contains('SAML:2.0'))
						saml2 = true
					if(it.serviceBinding.contains('SAML:1.0'))
						saml1 = true
				}
				else 
					log.warn ("No SamlURI binding for uri ${it.serviceBinding} exists, not importing, not importing AssertionConsumerService")
			})
			sql.eachRow("select * from serviceLocations where objectID=${it.resourceID} and serviceType='ManageNameIDService'",
			{
				def binding = SamlURI.findByUri(it.serviceBinding)
				if(binding) {
					def location = new UrlURI(uri:it.serviceLocation)
					def mnids = new ManageNameIDService(binding: binding, location: location, active:true)
					sp.addToManageNameIDServices(mnids)
					
					if(it.serviceBinding.contains('SAML:2.0'))
						saml2 = true
					if(it.serviceBinding.contains('SAML:1.0'))
						saml1 = true
				}
				else 
					log.warn ("No SamlURI binding for uri ${it.serviceBinding} exists, not importing, not importing ManageNameIDService")
			})
			
			if(saml2)
				sp.addToNameIDFormats(trans)
			if(saml1)
				sp.addToNameIDFormats(shibNameID)
			
			// RR has no real concept of multiple AttributeConsumingServices so everything is just index 0 and true for isDefault...
			// Additionally current metadata just grabs data from objectDescription for serviceName and serviceDescription ... fun.
			def acs = new AttributeConsumingService(index:0, isDefault:true)
			sql.eachRow("select * from objectDescriptions where objectID=${it.resourceID} and objectType='resource'",
			{
				acs.lang = it.language
				acs.addToServiceNames(it.descriptiveName)
				acs.addToServiceDescriptions(it.description)
			})
			
			sql.eachRow("select attributeUse.attributeUseType, attributes.attributeURN from attributeUse INNER JOIN attributes ON attributes.attributeID=attributeUse.attributeID where attributeUse.resourceID=${it.resourceID}",
			{
				def required = it.attributeUseType.equals("required")	// change to boolean for sanity..
				def attr = Attribute.findByName(it.attributeURN)
				def reqAttr = new RequestedAttribute(isRequired:required, attribute:attr)
				acs.addToRequestedAttributes(reqAttr)
			})
			sp.addToAttributeConsumingServices(acs)
			
			// RR doesn't store any flag to indicate that SP publishes encyption type in MD but it does for every SP.....
			importCrypto(it.resourceID, sp, true)
			
			entity.addToSpDescriptors(sp)
			entity.save()
			if(entity.hasErrors()) {
				entity.errors.each {log.error it}
			}
			else
				log.debug "Added new SPSSODescriptor to Entity ${entity.entityID}"
		})
	}
	
	def importCrypto(def id, def descriptor, def enc) {
		sql.eachRow("select * from certData where objectID=${id}",
		{
			try {
			def data = "-----BEGIN CERTIFICATE-----\n${it.certData}\n-----END CERTIFICATE-----"
			//log.debug "Importing certificate data\n${data}"
			def cert = new Certificate(data:data)	
			cert.expiryDate = cryptoService.expiryDate(cert)
			cert.issuer = cryptoService.issuer(cert)
			cert.subject = cryptoService.subject(cert)
			
			def keyInfo = new KeyInfo(certificate:cert)
			def keyDescriptor = new KeyDescriptor(keyInfo:keyInfo, keyType:KeyTypes.signing, roleDescriptor:descriptor)
			
			descriptor.addToKeyDescriptors(keyDescriptor)
			
			if(enc){
				def certEnc = new Certificate(data:data)
				certEnc.expiryDate = cryptoService.expiryDate(certEnc)
				certEnc.issuer = cryptoService.issuer(certEnc)
				certEnc.subject = cryptoService.subject(certEnc)
				
				def keyInfoEnc = new KeyInfo(certificate:certEnc)
				def keyDescriptorEnc = new KeyDescriptor(keyInfo:keyInfoEnc, keyType:KeyTypes.encryption, roleDescriptor:descriptor)
				descriptor.addToKeyDescriptors(keyDescriptorEnc)
			}
			}
			catch(Exception e) {
				log.error "Error importing crypto for descriptor ${descriptor.displayName}"
			}
		})
	}

}
