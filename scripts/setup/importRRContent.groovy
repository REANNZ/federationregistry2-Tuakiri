
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
	roleService = ctx.getBean("roleService")
	permissionService = ctx.getBean("permissionService")
	grailsApplication = ctx.getBean('grailsApplication')
	
    sql = Sql.newInstance("jdbc:mysql://localhost:3306/resourceregistry", "rr", "password", "com.mysql.jdbc.Driver")

	importCACertificates()
	importOrganizations()
	importContacts()
	importUsers()
	assignOrganizationAdministrators()
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
			def orgType = new OrganizationType(name:it.homeOrgType, displayName:it.homeOrgType, description:it.descriptiveName, discoveryServiceCategory:true)
			orgType.save()
		})
		
		sql.eachRow("select * from homeOrgs",
		{
				def org = Organization.findByName(it.homeOrgName)
				if(!org) {
					def orgType = OrganizationType.findByName(it.homeOrgType)
					org = new Organization(active:true, approved:true, name:it.homeOrgName, lang:it.mainLanguage, url: new UrlURI(uri:it.errorURL ?:'http://aaf.edu.au/support'), primary: orgType)
					def homeOrgName = it.homeOrgName
					sql.eachRow("select * from objectDescriptions where objectID=${it.homeOrgID} and objectType='homeOrg'",
					{
						org.displayName = it.descriptiveName?:homeOrgName
						org.description = it.description?:"N/A"
					})
					
					org.save()
				}
		})
		
		sql.eachRow("select * from resources",
		{
			// If organization does not yet exist it must only be providing services in which case we mark as being of Type 'other'
			def org = Organization.findByName(it.homeOrg)
			if(!org) {
				def orgType = OrganizationType.findByName('others')
				org = new Organization(name:it.homeOrg, displayName:it.homeOrg, lang:it.mainLanguage, url: new UrlURI(uri:it.errorURL ?:'http://aaf.edu.au/support'), primary: orgType)
				org.save()
				
				def adminRole = roleService.createRole("organization-${org.id}-administrators", "Global administrators for the organization ${it.homeOrg}", false)
				LevelPermission permission = new LevelPermission()
			    permission.populate("organization", "${org.id}", "*", null, null, null)
			    permission.managed = false
				permissionService.createPermission(permission, adminRole)
				
				// We'll have to manually give permissions in-tool at some later date.
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
					
				def contact = new Contact(organization:organization, givenName:givenName, surname:surname, email:new MailURI(uri:it.email))
								
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
			if(it.uniqueID && it.email) {
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
		
					if(newUser.validate()){
						user = userService.createUser(newUser)
						if (user.hasErrors()) {
							println "Error creating user account from Shibboleth credentials for ${it.uniqueID}"
						}
		
						def contact = MailURI.findByUri(newUser.profile.email)?.contact
						if(!contact) {
							contact = new Contact(givenName:it.givenName, surname:it.surname, email:new MailURI(uri:it.email), organization:organization )
							if(!contact.save()) {
								println "Unable to create Contact to link with incoming user" 
							}
						}
					
						user.contact = contact
						if(!user.save()) {
							println "Unable to create Contact link with $user" 
						}

						if(!user.save()) {
							println "Unable to create User link with $contact" 
						}
					
					
						println "Imported user $user"
					} else {
						println "Rejected user import for ${it.uniqueID}"
						newUser.errors.each {
							println it
						}
					}
				}
				else
					println "ERROR IMPORTING USER NO ORGANIZATION ${it.homeOrgName}"
			}
		})
	}
	
	def assignOrganizationAdministrators() {
		sql.eachRow("select * from homeOrgs",
		{
			def org = Organization.findByName(it.homeOrgName)
			if(org) {
				def adminRole = Role.findByName("organization-${org.id}-administrators")
				if(!adminRole) {
					adminRole = roleService.createRole("organization-${org.id}-administrators", "Global administrators for the organization ${it.homeOrgName}", false)
					LevelPermission permission = new LevelPermission()
				    permission.populate("organization", "${org.id}", "*", null, null, null)
				    permission.managed = false
					permissionService.createPermission(permission, adminRole)
				}

				sql.eachRow("select users.uniqueID from objectAdmins INNER JOIN users on objectAdmins.userID=users.userID where uniqueID not like '' and objectType='rra' and objectAdmins.objectID=${it.homeOrgID}",
				{ u ->
					def user = User.findWhere(username:u.uniqueID)
					if(user)
						roleService.addMember(user, adminRole)
				})
			}
		})
	}

	def importEntities() {
		println "Creating core EntitiesDescriptor ${grailsApplication.config.fedreg.metadata.federation}" 
		def eds = new EntitiesDescriptor(name: grailsApplication.config.fedreg.metadata.federation)
		if(!eds.save()) {
		  eds.errors.each {println it}
		}
		
		println "Importing entities from upstream resource registry"
		sql.eachRow("select * from homeOrgs",
		{
			def ed = EntityDescriptor.findWhere(entityID: it.entityID) 		// There are actually duplicate entityID's ....
			if(ed == null){			
				def org = Organization.findByName(it.homeOrgName)
				def entity = new EntityDescriptor(entityID:it.entityID, organization:org, active:true, approved:true)
				entity.save()
				if(entity.hasErrors()) {
					entity.errors.each {println it}
					println "Not importing $entity it is error"
				}
				println "Imported entity ${entity.entityID}"
			
				// Create ContactPerson instances and link entities to contacts
				sql.eachRow("select email, contactType from contacts INNER JOIN homeOrgs ON contacts.objectID=homeOrgs.homeOrgID WHERE homeOrgs.entityID=${entity.entityID}",
				{
					linkContact(it, entity)
				})
				entity.save()
			
				def adminRole = roleService.createRole("descriptor-${entity.id}-administrators", "Global administrators for the entity ${it.entityID}", false)
				LevelPermission permission = new LevelPermission()
			    permission.populate("descriptor", "${entity.id}", "*", null, null, null)
			    permission.managed = false
				permissionService.createPermission(permission, adminRole)
				
				sql.eachRow("select users.uniqueID from objectAdmins INNER JOIN users on objectAdmins.userID=users.userID where uniqueID not like '' and objectType='homeOrg' and objectAdmins.objectID=${it.homeOrgID}",
				{ u ->
					def user = User.findWhere(username:u.uniqueID)
					if(user)
						roleService.addMember(user, adminRole)
				})
			}
		})
		
		sql.eachRow("select * from resources",
		{
			def org = Organization.findByName(it.homeOrg)
			def entity = new EntityDescriptor(entityID:it.providerID, organization:org, active:true, approved:true).save()
			
			// Create ContactPerson instances and link entities to contacts
			sql.eachRow("select email, contactType from contacts INNER JOIN resources ON contacts.objectID=resources.resourceID WHERE resources.providerID=${entity.entityID}",
			{
				linkContact(it, entity)
			})
			entity.save()
			
			def adminRole = roleService.createRole("descriptor-${entity.id}-administrators", "Global administrators for the entity ${entity.entityID}", false)
			LevelPermission permission = new LevelPermission()
		    permission.populate("descriptor", "${entity.id}", "*", null, null, null)
		    permission.managed = false
			permissionService.createPermission(permission, adminRole)
			
			sql.eachRow("select users.uniqueID from objectAdmins INNER JOIN users on objectAdmins.userID=users.userID where uniqueID not like '' and objectAdmins.objectID=${it.resourceID}",
			{ u ->
				def user = User.findWhere(username:u.uniqueID)
				
				if(user)
					roleService.addMember(user, adminRole)
			})
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
	
	def determineProtocolSupport(binding, descriptor) {
		def saml2Namespace = SamlURI.findWhere(uri:'urn:oasis:names:tc:SAML:2.0:protocol')
		def saml1Namespace = SamlURI.findWhere(uri:'urn:oasis:names:tc:SAML:1.1:protocol')
		def shibboleth1Namespace = SamlURI.findWhere(uri:'urn:mace:shibboleth:1.0')

		def shibNameID = SamlURI.findByUri('urn:mace:shibboleth:1.0:nameIdentifier')
		def trans = SamlURI.findByUri('urn:oasis:names:tc:SAML:2.0:nameid-format:transient')
		
		if(binding.uri.contains('urn:oasis:names:tc:SAML:2.0') && !descriptor.protocolSupportEnumerations?.contains(saml2Namespace)) {
			descriptor.addToProtocolSupportEnumerations(saml2Namespace)
			if(!descriptor.nameIDFormats?.contains(trans))
				descriptor.addToNameIDFormats(trans)
		}

		if(binding.uri.contains('urn:oasis:names:tc:SAML:1.0') && !descriptor.protocolSupportEnumerations?.contains(saml1Namespace)) {
			descriptor.addToProtocolSupportEnumerations(saml1Namespace)
			if(!descriptor.nameIDFormats?.contains(shibNameID))
				descriptor.addToNameIDFormats(shibNameID)
		}

		if(binding.uri.contains('urn:mace:shibboleth:1.0') && !descriptor.protocolSupportEnumerations?.contains(shibboleth1Namespace)) {
			descriptor.addToProtocolSupportEnumerations(shibboleth1Namespace)
			if(!descriptor.protocolSupportEnumerations.contains(saml1Namespace))
				descriptor.addToProtocolSupportEnumerations(saml1Namespace)
			if(!descriptor.nameIDFormats?.contains(shibNameID))
				descriptor.addToNameIDFormats(shibNameID)
		}
	}

	def importIDPSSODescriptors() {
		sql.eachRow("select * from homeOrgs",
		{			
			def entity = EntityDescriptor.findWhere(entityID:it.entityID)
			def idp = new IDPSSODescriptor(active:true, approved:true, entityDescriptor:entity, organization:entity.organization, scope:it.homeOrgName, wantAuthnRequestsSigned:false)
			
			sql.eachRow("select * from objectDescriptions where objectID=${it.homeOrgID} and objectType='homeOrg'",
			{
				idp.displayName = it.descriptiveName?:"N/A"
				idp.description = it.description?:"N/A"
			})
			
			sql.eachRow("select * from serviceLocations where objectID=${it.homeOrgID} and serviceType='SingleSignOnService'",
			{
				def binding = SamlURI.findByUri(it.serviceBinding)
				if(binding) {
					def location = new UrlURI(uri:it.serviceLocation.trim())
					def ssoService = new SingleSignOnService(binding: binding, location: location, active:true, approved:true)
					idp.addToSingleSignOnServices(ssoService)
					
					determineProtocolSupport(binding, idp)
				}
				else 
					println("No SamlURI binding for uri ${it.serviceBinding} exists, not importing, not importing SingleSignOnService")
			})
			def index = 0
			sql.eachRow("select * from serviceLocations where objectID=${it.homeOrgID} and serviceType='ArtifactResolutionService'",
			{
				def binding = SamlURI.findByUri(it.serviceBinding)
				if(binding) {
					def location = new UrlURI(uri:it.serviceLocation.trim())
					def artServ = new ArtifactResolutionService(binding: binding, location: location, isDefault: it.defaultLocation, active:true, approved:true, endpointIndex:index++)
					idp.addToArtifactResolutionServices(artServ)
					
					determineProtocolSupport(binding, idp)
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
			else {
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
			
				def adminRole = roleService.createRole("descriptor-${idp.id}-administrators", "Global administrators for ${idp}", false)
				LevelPermission permission = new LevelPermission()
			    permission.populate("descriptor", "${idp.id}", "*", null, null, null)
			    permission.managed = false
				permissionService.createPermission(permission, adminRole)
			
				sql.eachRow("select users.uniqueID from objectAdmins INNER JOIN users on objectAdmins.userID=users.userID where uniqueID not like '' and objectType='homeOrg' and objectAdmins.objectID=${it.homeOrgID}",
				{ u ->
					def user = User.findWhere(username:u.uniqueID)
					
					if(user)
						roleService.addMember(user, adminRole)
				})
			}
		})	
	}
	
	def importAttributeAuthorityDescriptors() {		
		sql.eachRow("select * from homeOrgs",
		{	
			def idp, aa		
			def entity = EntityDescriptor.findWhere(entityID:it.entityID)
			if(entity.idpDescriptors.size() > 0) {
				idp = entity.idpDescriptors.toList().get(0)
				aa = new AttributeAuthorityDescriptor(active:true, approved:true, entityDescriptor:entity, organization:entity.organization, scope:it.homeOrgName, displayName:idp.displayName, description:idp.description)
			} else {
				aa = new AttributeAuthorityDescriptor(active:true, approved:true, entityDescriptor:entity, organization:entity.organization)
				
				sql.eachRow("select * from objectDescriptions where objectID=${it.homeOrgID} and objectType='homeOrg'",
				{
					aa.displayName = it.descriptiveName?:"N/A"
					aa.description = it.description?:"N/A"
				})
				println "Imported stand alone attribute authority"
			}
			
			sql.eachRow("select * from serviceLocations where objectID=${it.homeOrgID} and serviceType='AttributeService'",
			{
				def binding = SamlURI.findByUri(it.serviceBinding)
				if(binding) {
					def location = new UrlURI(uri:it.serviceLocation.trim())
					def attrService = new AttributeService(binding: binding, location: location, active:true, approved:true)
					aa.addToAttributeServices(attrService)
					
					determineProtocolSupport(binding, aa)
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
				
			if(idp) {
				aa.collaborator = idp
				aa.save()
			
				idp.collaborator = aa
				idp.save()
			}
			
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
		
		sql.eachRow("select * from resources",
		{
			// RR hardcodes NameIDFormats so we need to figure out which version of SAML is supported(or even both) and manually add NameIDFormats
			boolean saml1 = false
			boolean saml2 = false
			
			def entity = EntityDescriptor.findWhere(entityID:it.providerID)
			def sd = new ServiceDescription()
			def sp = new SPSSODescriptor(active:true, approved:true, entityDescriptor:entity, organization:entity.organization, visible:it.visible, serviceDescription:sd, authnRequestsSigned:false, wantAssertionsSigned:false)
			
			sql.eachRow("select * from objectDescriptions where objectID=${it.resourceID} and objectType='resource'",
			{
				sp.displayName = it.descriptiveName
				sp.description = it.description
			})

			sql.eachRow("select * from serviceLocations where objectID=${it.resourceID} and serviceType='SingleLogoutService'",
			{
				def binding = SamlURI.findByUri(it.serviceBinding)
				if(binding) {
					def location = new UrlURI(uri:it.serviceLocation.trim())
					def sls = new SingleLogoutService(binding: binding, location: location, active:true, approved:true)
					sp.addToSingleLogoutServices(sls)
					
					determineProtocolSupport(binding, sp)
				}
				else 
					println("No SamlURI binding for uri ${it.serviceBinding} exists, not importing, not importing SingleLogoutService")
			})
			def index = 0
			sql.eachRow("select * from serviceLocations where objectID=${it.resourceID} and serviceType='AssertionConsumerService'",
			{
				def binding = SamlURI.findByUri(it.serviceBinding)
				if(binding) {
					def location = new UrlURI(uri:it.serviceLocation.trim())
					def acs = new AssertionConsumerService(binding: binding, location: location, isDefault: it.defaultLocation, active:true, approved:true, endpointIndex:index++)
					sp.addToAssertionConsumerServices(acs)
					
					determineProtocolSupport(binding, sp)
				}
				else 
					println("No SamlURI binding for uri ${it.serviceBinding} exists, not importing, not importing AssertionConsumerService")
			})
			sql.eachRow("select * from serviceLocations where objectID=${it.resourceID} and serviceType='ManageNameIDService'",
			{
				def binding = SamlURI.findByUri(it.serviceBinding)
				if(binding) {
					def location = new UrlURI(uri:it.serviceLocation.trim())
					def mnids = new ManageNameIDService(binding: binding, location: location, active:true, approved:true)
					sp.addToManageNameIDServices(mnids)
					
					determineProtocolSupport(binding, sp)
				}
				else 
					println("No SamlURI binding for uri ${it.serviceBinding} exists, not importing, not importing ManageNameIDService")
			})
			sql.eachRow("select * from serviceLocations where objectID=${it.resourceID} and serviceType='DiscoveryResponse'",
			{
				def binding = SamlURI.findByUri(SamlConstants.drs)
				if(binding) {
					def location = new UrlURI(uri:it.serviceLocation.trim())
					def drs = new DiscoveryResponseService(binding: binding, location: location, active:true, approved:true, isDefault:true)
					sp.addToDiscoveryResponseServices(drs)
				}
				else 
					println("No SamlURI binding for uri ${it.serviceBinding} exists, not importing, not importing DiscoveryResponseService")
			})
			
			// RR has no real concept of multiple AttributeConsumingServices so everything is just index 0 and true for isDefault...
			// Additionally current metadata just grabs data from objectDescription for serviceName and serviceDescription ... fun.
			def acs = new AttributeConsumingService(index:0, isDefault:true)
			sql.eachRow("select * from objectDescriptions where objectID=${it.resourceID} and objectType='resource'",
			{
				acs.lang = it.language
				acs.addToServiceNames(it.descriptiveName)
				if(it.description)
					if(it.description.size() < 254)
						acs.addToServiceDescriptions(it.description)
					else
						acs.addToServiceDescriptions(it.description.substring(0,254))
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
			
			importCrypto(it.resourceID, sp, true)
			
			entity.addToSpDescriptors(sp)
			entity.save()
			if(entity.hasErrors()) {
				println "Failed modifying ${entity.entityID}"
				entity.errors.each {println it}
			}
			else {
				println "Added new SPSSODescriptor to Entity ${entity.entityID}"
				
				sp.save()
				
				def adminRole = roleService.createRole("descriptor-${sp.id}-administrators", "Global administrators for ${sp}", false)
				LevelPermission permission = new LevelPermission()
			    permission.populate("descriptor", "${sp.id}", "*", null, null, null)
			    permission.managed = false
				permissionService.createPermission(permission, adminRole)
				
				sql.eachRow("select users.uniqueID from objectAdmins INNER JOIN users on objectAdmins.userID=users.userID where uniqueID not like '' and objectAdmins.objectID=${it.resourceID}",
				{ u ->
					def user = User.findWhere(username:u.uniqueID)
					
					if(user)
						roleService.addMember(user, adminRole)
				})
			}
		})
	}
	
	def importCrypto(def id, def descriptor, def enc) {
		sql.eachRow("select DISTINCT(certData) from certData where objectID=${id}",
		{
			try {
				def data = "-----BEGIN CERTIFICATE-----\n${it.certData.normalize()}\n-----END CERTIFICATE-----"
				def cert = cryptoService.createCertificate(data)	
				if(cryptoService.validateCertificate(cert)) {
					def keyInfo = new KeyInfo(certificate:cert)
					def keyDescriptor = new KeyDescriptor(keyInfo:keyInfo, keyType:KeyTypes.signing, roleDescriptor:descriptor)
					keyDescriptor.validate()
					if(keyDescriptor.hasErrors()){
						println "Error import crypto for $descriptor and ${it.certData}"
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
			}
			catch(Exception e) {
				println "Error importing crypto for descriptor ${descriptor.displayName}"
				println e.getMessage()
			}
		})
	}
