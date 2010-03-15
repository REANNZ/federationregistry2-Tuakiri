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
			//def dataLoadRecord = new DataLoadRecord(invoker:authenticatedUser?:null, remoteAddr: request.getRemoteAddr(), remoteHost: request.getRemoteHost(), userAgent:request.getHeader("User-Agent"))
			//dataLoadRecord.save()
		}
	}
	
	def initialPopulate() {
		
		// Attribute Scopes (Non standard)	
		def fedScope = new AttributeScope(name:'Federation').save()
		def localScope = new AttributeScope(name:'Local').save()
		def mandatoryCategory = new AttributeCategory(name:'Mandatory').save()
		def recommendedCategory = new AttributeCategory(name:'Recommended').save()
		def optionalCategory = new AttributeCategory(name:'Optional').save()
		
		// Bindings
		def httpRedirect = new SamlURI(type:SamlURIType.ProtocolBinding, uri:'urn:oasis:names:tc:SAML:2.0:bindings:HTTP-Redirect').save()
		def httpPost = new SamlURI(type:SamlURIType.ProtocolBinding, uri:'urn:oasis:names:tc:SAML:2.0:bindings:HTTP-POST').save()
		def httpArtifact = new SamlURI(type:SamlURIType.ProtocolBinding, uri:'urn:oasis:names:tc:SAML:2.0:bindings:HTTP-Artifact').save()
		def httpPostSimple = new SamlURI(type:SamlURIType.ProtocolBinding, uri:'urn:oasis:names:tc:SAML:2.0:bindings:HTTP-POST-SimpleSign').save()
		def paos = new SamlURI(type:SamlURIType.ProtocolBinding, uri:'urn:oasis:names:tc:SAML:2.0:bindings:PAOS').save()
		def soap = new SamlURI(type:SamlURIType.ProtocolBinding, uri:'urn:oasis:names:tc:SAML:2.0:bindings:SOAP').save()
		def shibAuthn = new SamlURI(type:SamlURIType.ProtocolBinding, uri:'urn:mace:shibboleth:1.0:profiles:AuthnRequest').save()
		def httpPost1 = new SamlURI(type:SamlURIType.ProtocolBinding, uri:'urn:oasis:names:tc:SAML:1.0:profiles:browser-post').save()
		def httpArtifact1 = new SamlURI(type:SamlURIType.ProtocolBinding, uri:'urn:oasis:names:tc:SAML:1.0:profiles:artifact-01').save()
		def soap1 = new SamlURI(type:SamlURIType.ProtocolBinding, uri:'urn:oasis:names:tc:SAML:1.0:bindings:SOAP-binding').save()
		
		// NameIDFormats
		def unspec = new SamlURI(type:SamlURIType.NameIdentifierFormat, uri:'urn:oasis:names:tc:SAML:1.1:nameid-format:unspecified').save()
		def email = new SamlURI(type:SamlURIType.NameIdentifierFormat, uri:'urn:oasis:names:tc:SAML:1.1:nameid-format:emailAddress').save()
		def x509 = new SamlURI(type:SamlURIType.NameIdentifierFormat, uri:'urn:oasis:names:tc:SAML:1.1:nameid-format:X509SubjectName').save()
		def windows = new SamlURI(type:SamlURIType.NameIdentifierFormat, uri:'urn:oasis:names:tc:SAML:1.1:nameid-format:WindowsDomainQualifiedName').save()
		def kerberos = new SamlURI(type:SamlURIType.NameIdentifierFormat, uri:'urn:oasis:names:tc:SAML:2.0:nameid-format:kerberos').save()
		def entity = new SamlURI(type:SamlURIType.NameIdentifierFormat, uri:'urn:oasis:names:tc:SAML:2.0:nameid-format:entity').save()
		def pers = new SamlURI(type:SamlURIType.NameIdentifierFormat, uri:'urn:oasis:names:tc:SAML:2.0:nameid-format:persistent').save()
		def trans = new SamlURI(type:SamlURIType.NameIdentifierFormat, uri:'urn:oasis:names:tc:SAML:2.0:nameid-format:transient').save()
		def shibNameID = new SamlURI(type:SamlURIType.NameIdentifierFormat, uri:'urn:mace:shibboleth:1.0:nameIdentifier').save()
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
			def caCert = new CACertificate(data:it.certData)
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
				log.debug "Loading organization which is only providing services, ${it.homeOrg}"
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
		sql.eachRow("select email, contactName from contacts ORDER BY (email)",
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
			def email = it.email
			
			def c = Contact.createCriteria()
			def results = c.list {
				and {
					emailAddresses {
						and {
							eq("uri", email)
						}
					}
				}
			}
			// Doesn't already exist (RR data is crapppppy!!) so create.
			if(results.size() == 0) {
				def contact = new Contact(userLink:false, givenName:givenName, surname:surname)
								.addToEmailAddresses(new MailURI(uri:email))
								
				contact.save(flush:true)
				if(contact.hasErrors()) {
					contact.errors.each { err -> log.error err}
				}
			}
		})
	}

	def importEntities() {
		log.debug "Importing entities from upstream resource registry"
		sql.eachRow("select * from homeOrgs",
		{
			def org = Organization.findByName(it.homeOrgName)
			def entity = new EntityDescriptor(entityID:it.entityID, organization:org, active:it.approved).save()
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
		def email = row.email
		def c = Contact.createCriteria()
		def contact = c.get {
			and {
				emailAddresses {
					and {
						eq("uri", email)
					}
				}
			}
		}
		def type
		switch(row.contactType) {
			case 'technical': type = ContactType.technical; break;
			case 'support': type = ContactType.support; break;
			case 'administrative': type = ContactType.administrative; break;
			case 'billing': type = ContactType.billing; break;
			default: type = ContactType.other; break;
		}
		def contactPerson = new ContactPerson(contact:contact, entity:ent, type:type)
		ent.addToContacts(contactPerson)
		log.debug "Linked contact ${contactPerson?.contact?.givenName} ${contactPerson?.contact?.surname} to entity ${ent.entityID}"
	}

	def importIDPSSODescriptors() {
		def shibNameID = SamlURI.findByUri('urn:mace:shibboleth:1.0:nameIdentifier')
		def trans = SamlURI.findByUri('urn:oasis:names:tc:SAML:2.0:nameid-format:transient')
		
		sql.eachRow("select * from homeOrgs",
		{			
			def entity = EntityDescriptor.findWhere(entityID:it.entityID)
			def idp = new IDPSSODescriptor(entityDescriptor:entity, organization:entity.organization)
			idp.addToNameIDFormats(trans)	// RR hard codes everything to only advertise NameIDForm of transient so we need to do the same, in FR this is modifable and DB driven
			
			sql.eachRow("select * from objectDescriptions where objectID=${it.homeOrgID} and objectType='homeOrg'",
			{
				idp.displayName = it.descriptiveName
				idp.description = it.description
			})
			
			sql.eachRow("select * from serviceLocations where objectID=${it.homeOrgID} and serviceType='SingleSignOnService'",
			{
				def binding = SamlURI.findByUri(it.serviceBinding)
				if(binding) {
					def location = new UrlURI(uri:it.serviceLocation)
					def ssoService = new SingleSignOnService(binding: binding, location: location)
					idp.addToSingleSignOnServices(ssoService)
				}
				else 
					log.warn ("No SamlURI binding for uri ${it.serviceBinding} exists, not importing, not importing SingleSignOnService")
			})
			sql.eachRow("select * from serviceLocations where objectID=${it.homeOrgID} and serviceType='ArtifactResolutionService'",
			{
				def binding = SamlURI.findByUri(it.serviceBinding)
				if(binding) {
					def location = new UrlURI(uri:it.serviceLocation)
					def artServ = new ArtifactResolutionService(binding: binding, location: location, isDefault: it.defaultLocation)
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
			log.debug "Added new IDPSSODescriptor to Entity ${entity.entityID}"
		})	
	}
	
	def importAttributeAuthorityDescriptors() {
		def shibNameID = SamlURI.findByUri('urn:mace:shibboleth:1.0:nameIdentifier')
		def trans = SamlURI.findByUri('urn:oasis:names:tc:SAML:2.0:nameid-format:transient')
		
		sql.eachRow("select * from homeOrgs",
		{			
			def entity = EntityDescriptor.findWhere(entityID:it.entityID)
			def aa = new AttributeAuthorityDescriptor(entityDescriptor:entity, organization:entity.organization).save()
			
			sql.eachRow("select * from serviceLocations where objectID=${it.homeOrgID} and serviceType='AttributeService'",
			{
				def binding = SamlURI.findByUri(it.serviceBinding)
				if(binding) {
					def location = new UrlURI(uri:it.serviceLocation)
					def attrService = new AttributeService(binding: binding, location: location)
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
			log.debug "Added new AttributeAuthorityDescriptor to Entity ${entity.entityID}"
		})
	}

	def importSPSSODescriptors() {
		def shibNameID = SamlURI.findByUri('urn:mace:shibboleth:1.0:nameIdentifier')
		def trans = SamlURI.findByUri('urn:oasis:names:tc:SAML:2.0:nameid-format:transient')
		
		sql.eachRow("select * from resources",
		{
			// RR hardcodes NameIDFormats so we need to figure out which version of SAML is supported(or even both) and manually add NameIDFormats
			boolean saml1 = false
			boolean saml2 = false
			
			def entity = EntityDescriptor.findWhere(entityID:it.providerID)
			def sd = new ServiceDescription()
			def sp = new SPSSODescriptor(entityDescriptor:entity, organization:entity.organization, visible:it.visible, serviceDescription:sd)
			
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
					def sls = new SingleLogoutService(binding: binding, location: location)
					sp.addToSingleLogoutServices(sls)
					
					if(it.serviceBinding.contains('SAML:2.0'))
						saml2 = true
					if(it.serviceBinding.contains('SAML:1.0'))
						saml1 = true
				}
				else 
					log.warn ("No SamlURI binding for uri ${it.serviceBinding} exists, not importing, not importing SingleLogoutService")
			})
			sql.eachRow("select * from serviceLocations where objectID=${it.resourceID} and serviceType='AssertionConsumerService'",
			{
				def binding = SamlURI.findByUri(it.serviceBinding)
				if(binding) {
					def location = new UrlURI(uri:it.serviceLocation)
					def acs = new AssertionConsumerService(binding: binding, location: location, isDefault: it.defaultLocation)
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
					def mnids = new ManageNameIDService(binding: binding, location: location)
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
			log.debug "Added new SPSSODescriptor to Entity ${entity.entityID}"
		})
	}
	
	def importCrypto(def id, def descriptor, def enc) {
		sql.eachRow("select * from certData where objectID=${id}",
		{
			def cert = new Certificate(data:it.certData)
			def keyInfo = new KeyInfo(certificate:cert)
			def keyDescriptor = new KeyDescriptor(keyInfo:keyInfo, keyType:KeyTypes.signing, roleDescriptor:descriptor)
			
			descriptor.addToKeyDescriptors(keyDescriptor)
			
			if(enc){
				def certEnc = new Certificate(data:it.certData)
				def keyInfoEnc = new KeyInfo(certificate:certEnc)
				def keyDescriptorEnc = new KeyDescriptor(keyInfo:keyInfoEnc, keyType:KeyTypes.encryption)
				descriptor.addToKeyDescriptors(keyDescriptorEnc)
			}
		})
	}

}
