
	import groovy.sql.Sql
	
	import grails.plugins.nimble.InstanceGenerator
	import grails.plugins.nimble.core.*
	
	import fedreg.host.*
	import fedreg.core.*

	/*
	 This script assists with bootstrap and provides migration from an existing PHP based resource registry.
	 Unlike the rest of Federation Registry I haven't built any extensive test suites to go along with this script. Your schema may be slightly different or have various hacks applied.
	 I highly reccomend extensive tests in your respective development and test environments before letting loose on production.
	 This should be run from the provided FR online Groovy console to be able to access various Grails features.
	*/
	cryptoService = ctx.getBean("cryptoService")
	userService = ctx.getBean("userService")
	
    sql = Sql.newInstance("jdbc:mysql://localhost:3306/resourceregistry", "rr", "password", "com.mysql.jdbc.Driver")

	importCACertificates()
	importOrganizations()
	importContacts()
	importUsers()
	importEntities()
	importIDPSSODescriptors()
	importAttributeAuthorityDescriptors()
	importSPSSODescriptors()
	
	def importCACertificates() {
		sql.eachRow("select * from certData where objectType='issuingca'", {
			def data = "-----BEGIN CERTIFICATE-----\n${it.certData}\n-----END CERTIFICATE-----"
			def caCert = new CACertificate(data:data)
			def caKeyInfo = new CAKeyInfo(certificate:caCert)
			caKeyInfo.save()
			if(caKeyInfo.hasErrors()) {
				println "Error importing CA"
				caKeyInfo.errors.each {println it}
			}
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
			def org = new Organization(active:true, approved:true, name:it.homeOrgName, displayName:it.homeOrgName, lang:it.mainLanguage, url: new UrlURI(uri:it.errorURL ?:'http://aaf.edu.au/support'), primary: orgType)
			org.save()
		})
		
		sql.eachRow("select * from resources",
		{
			// If organization does not yet exist it must only be providing services in which case we mark as being of Type 'other'
			def org = Organization.findByName(it.homeOrg)
			if(!org) {
				def orgType = OrganizationType.findByName('others')
				org = new Organization(name:it.homeOrg, displayName:it.homeOrg, lang:it.mainLanguage, url: new UrlURI(uri:it.errorURL ?:'http://aaf.edu.au/support'), primary: orgType)
				org.save()
			}
		})
	}

	def importContacts() {
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

			// Doesn't already exist so create
			if(!MailURI.findByUri(it.email)) {
				def organization
				if(it.homeOrgName)
					organization = Organization.findByName(it.homeOrgName)
					
				def contact = new Contact(organization:organization, userLink:false, givenName:givenName, surname:surname, email:new MailURI(uri:it.email))
								
				contact.save(flush:true)
				if(contact.hasErrors()) {
					contact.errors.each { err -> println err}
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
							contact.errors.each { err -> println err}
						}
					}
				}
			}
		})
	}
	
	def importUsers() {
		def shibbolethFederationProvider = FederationProvider.findByUid(ShibbolethService.federationProviderUid)
		
		sql.eachRow("select uniqueID, givenName, surname, eMail, homeOrgName from users ORDER BY (email)",
		{
			if(it.uniqueID) {
				def organization = Organization.findByName(it.homeOrgName)
				if(organization) {
					UserBase newUser = InstanceGenerator.user()
					newUser.username = it.uniqueID
					newUser.enabled = true
					newUser.external = true
					newUser.federated = true
					newUser.federationProvider = shibbolethFederationProvider

					newUser.profile = InstanceGenerator.profile()
					newUser.profile.owner = newUser
					newUser.profile.fullName = "${it.givenName} ${it.surname}"
					newUser.profile.email = (it.email == "") ? null : it.email
		
					user = userService.createUser(newUser)
					if (user.hasErrors()) {
						println "Error creating user account from Shibboleth credentials for ${it.uniqueID}"
					}
		
					def contact = MailURI.findByUri(newUser.profile.email)?.contact
					if(!contact) {
						contact = new Contact(givenName:it.givenName, surname:it.surname, email:new MailURI(uri:it.email), userLink:true, userID: user.id, organization:organization )
						if(!contact.save()) {
							println "Unable to create Contact to link with incoming user" 
						}
					}
				
					user.contact = contact
					if(!user.save()) {
						println "Unable to create Contact link with $user" 
					}
					
					println "Imported user $user"
				}
				else
					println "ERROR IMPORTING USER NO ORGANIZATION ${it.homeOrgName}"
			}
		})
	}

	def importEntities() {
		println "Importing entities from upstream resource registry"
		sql.eachRow("select * from homeOrgs",
		{
			def org = Organization.findByName(it.homeOrgName)
			def entity = new EntityDescriptor(entityID:it.entityID, organization:org, active:true, approved:it.approved)
			entity.save()
			if(entity.hasErrors()) {
				entity.errors.each {println it}
			}
			println "Imported entity ${entity.entityID}"
			
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
		}
	}

	def importIDPSSODescriptors() {
		def samlNamespace = SamlURI.findByUri('urn:oasis:names:tc:SAML:2.0:protocol')
		def shibNameID = SamlURI.findByUri('urn:mace:shibboleth:1.0:nameIdentifier')
		def trans = SamlURI.findByUri('urn:oasis:names:tc:SAML:2.0:nameid-format:transient')
		
		sql.eachRow("select * from homeOrgs",
		{			
			def entity = EntityDescriptor.findWhere(entityID:it.entityID)
			def idp = new IDPSSODescriptor(active:true, approved:true, entityDescriptor:entity, organization:entity.organization)
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
					def ssoService = new SingleSignOnService(binding: binding, location: location, active:true, approved:true)
					idp.addToSingleSignOnServices(ssoService)
				}
				else 
					println("No SamlURI binding for uri ${it.serviceBinding} exists, not importing, not importing SingleSignOnService")
			})
			def index = 0
			sql.eachRow("select * from serviceLocations where objectID=${it.homeOrgID} and serviceType='ArtifactResolutionService'",
			{
				def binding = SamlURI.findByUri(it.serviceBinding)
				if(binding) {
					def location = new UrlURI(uri:it.serviceLocation)
					def artServ = new ArtifactResolutionService(binding: binding, location: location, isDefault: it.defaultLocation, active:true, approved:true, endpointIndex:index++)
					idp.addToArtifactResolutionServices(artServ)
				}
				else
					println("No SamlURI binding for uri ${it.serviceBinding} exists, not importing ArtifactResolutionService")
			})
			// RR doesn't store any flag to indicate that IDP publishes encyption type in MD so we won't create an enc key.....
			importCrypto(it.homeOrgID, idp, false)
			
			entity.addToIdpDescriptors(idp)
			entity.save()
			if(entity.hasErrors()) {
				entity.errors.each {println it}
			}
			else
				println "Added new IDPSSODescriptor to Entity ${entity.entityID}"
				
			sql.eachRow("select attributeOID from attributes INNER JOIN homeOrgAttributes ON attributes.attributeID=homeOrgAttributes.attributeID where homeOrgAttributes.homeOrgID=${it.homeOrgID};",
			{
				def base = AttributeBase.findByOid(it.attributeOID)
				if(base) {
					def attr = new Attribute(base: base, idpSSODescriptor:idp)
					idp.addToAttributes(attr)
				}
			})
			idp.save()
			
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
			def aa = new AttributeAuthorityDescriptor(active:true, approved:true, entityDescriptor:entity, organization:entity.organization, displayName:idp.displayName, description:idp.description)
			aa.addToProtocolSupportEnumerations(samlNamespace)
			aa.save()
			if(aa.hasErrors()) {
				aa.errors.each {println it}
			}
			aa.addToProtocolSupportEnumerations(samlNamespace)
			
			sql.eachRow("select * from serviceLocations where objectID=${it.homeOrgID} and serviceType='AttributeService'",
			{
				def binding = SamlURI.findByUri(it.serviceBinding)
				if(binding) {
					def location = new UrlURI(uri:it.serviceLocation)
					def attrService = new AttributeService(binding: binding, location: location, active:true, approved:true)
					aa.addToAttributeServices(attrService)
				}
				else 
					println("No SamlURI binding for uri ${it.serviceBinding} exists, not importing, not importing AttributeService")
			})
			// RR doesn't store any flag to indicate that AA publishes encyption type in MD so we won't create an enc key.....
			importCrypto(it.homeOrgID, aa, false)
			
			entity.addToAttributeAuthorityDescriptors(aa)
			entity.save()
			if(entity.hasErrors()) {
				entity.errors.each {println it}
			}
			else
				println "Added new AttributeAuthorityDescriptor to Entity ${entity.entityID}"
				
			aa.collaborator = idp
			aa.save()
			
			idp.collaborator = aa
			idp.save()
			
			sql.eachRow("select attributeOID from attributes INNER JOIN homeOrgAttributes ON attributes.attributeID=homeOrgAttributes.attributeID where homeOrgAttributes.homeOrgID=${it.homeOrgID};",
			{
				def base = AttributeBase.findByOid(it.attributeOID)
				if(base) {
					def attr = new Attribute(base: base, attributeAuthorityDescriptor:aa)
					aa.addToAttributes(attr)
				}
			})
			aa.save()
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
			def sp = new SPSSODescriptor(active:true, approved:true, entityDescriptor:entity, organization:entity.organization, visible:it.visible, serviceDescription:sd)
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
					def sls = new SingleLogoutService(binding: binding, location: location, active:true, approved:true)
					sp.addToSingleLogoutServices(sls)
					
					if(it.serviceBinding.contains('SAML:2.0'))
						saml2 = true
					if(it.serviceBinding.contains('SAML:1.0'))
						saml1 = true
				}
				else 
					println("No SamlURI binding for uri ${it.serviceBinding} exists, not importing, not importing SingleLogoutService")
			})
			def index = 0
			sql.eachRow("select * from serviceLocations where objectID=${it.resourceID} and serviceType='AssertionConsumerService'",
			{
				def binding = SamlURI.findByUri(it.serviceBinding)
				if(binding) {
					def location = new UrlURI(uri:it.serviceLocation)
					def acs = new AssertionConsumerService(binding: binding, location: location, isDefault: it.defaultLocation, active:true, approved:true, endpointIndex:index++)
					sp.addToAssertionConsumerServices(acs)
					
					if(it.serviceBinding.contains('SAML:2.0'))
						saml2 = true
					if(it.serviceBinding.contains('SAML:1.0'))
						saml1 = true
				}
				else 
					println("No SamlURI binding for uri ${it.serviceBinding} exists, not importing, not importing AssertionConsumerService")
			})
			sql.eachRow("select * from serviceLocations where objectID=${it.resourceID} and serviceType='ManageNameIDService'",
			{
				def binding = SamlURI.findByUri(it.serviceBinding)
				if(binding) {
					def location = new UrlURI(uri:it.serviceLocation)
					def mnids = new ManageNameIDService(binding: binding, location: location, active:true, approved:true)
					sp.addToManageNameIDServices(mnids)
					
					if(it.serviceBinding.contains('SAML:2.0'))
						saml2 = true
					if(it.serviceBinding.contains('SAML:1.0'))
						saml1 = true
				}
				else 
					println("No SamlURI binding for uri ${it.serviceBinding} exists, not importing, not importing ManageNameIDService")
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
				def base = AttributeBase.findByName(it.attributeURN)
				if(base) {
					def required = it.attributeUseType.equals("required")	// change to boolean for sanity..
					def reqAttr = new RequestedAttribute(isRequired:required, reasoning:'autoimport', base:base, approved:true, attributeConsumingService:acs)
					acs.addToRequestedAttributes(reqAttr)
					reqAttr.validate()
					if(reqAttr.hasErrors()) {
						println "Error importing attribute support for $sp"
						reqAttr.errors.each {println it}
					}
				}
			})
			sp.addToAttributeConsumingServices(acs)
			
			// RR doesn't store any flag to indicate that SP publishes encyption type in MD but it does for every SP.....
			importCrypto(it.resourceID, sp, true)
			
			entity.addToSpDescriptors(sp)
			entity.save()
			if(entity.hasErrors()) {
				entity.errors.each {println it}
			}
			else
				println "Added new SPSSODescriptor to Entity ${entity.entityID}"
		})
	}
	
	def importCrypto(def id, def descriptor, def enc) {
		sql.eachRow("select * from certData where objectID=${id}",
		{
			try {
			def data = "-----BEGIN CERTIFICATE-----\n${it.certData}\n-----END CERTIFICATE-----"
			//println "Importing certificate data\n${data}"
			def cert = cryptoService.createCertificate(data)	
			def keyInfo = new KeyInfo(certificate:cert)
			def keyDescriptor = new KeyDescriptor(keyInfo:keyInfo, keyType:KeyTypes.signing, roleDescriptor:descriptor)
			keyDescriptor.validate()
			if(keyDescriptor.hasErrors()){
				println "Error import crypto for $descriptor"
				keyDescriptor.errors.each { println it}
			}
			
			
			descriptor.addToKeyDescriptors(keyDescriptor)
			
			if(enc){
				def certEnc = cryptoService.createCertificate(data)	
				def keyInfoEnc = new KeyInfo(certificate:certEnc)
				def keyDescriptorEnc = new KeyDescriptor(keyInfo:keyInfoEnc, keyType:KeyTypes.encryption, roleDescriptor:descriptor)
				keyDescriptorEnc.validate()
				if(keyDescriptorEnc.hasErrors()){
					println "Error import crypto for $descriptor"
					keyDescriptorEnc.errors.each { println it}
				}
				
				descriptor.addToKeyDescriptors(keyDescriptorEnc)
			}
			}
			catch(Exception e) {
				println "Error importing crypto for descriptor ${descriptor.displayName}"
				println e.getMessage()
			}
		})
	}
