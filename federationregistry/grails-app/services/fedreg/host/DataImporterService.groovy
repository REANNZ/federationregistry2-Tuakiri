package fedreg.host

import org.springframework.beans.factory.InitializingBean
import groovy.sql.Sql

import fedreg.core.Attribute
import fedreg.core.AttributeScope
import fedreg.core.AttributeCategory

import fedreg.core.EntityDescriptor
import fedreg.core.IDPSSODescriptor

import fedreg.core.Organization
import fedreg.core.OrganizationType
import fedreg.core.Contact
import fedreg.core.ContactPerson
import fedreg.core.ContactType

import fedreg.core.SamlURI
import fedreg.core.UrlURI
import fedreg.core.MailURI
import fedreg.core.SingleSignOnService

class DataImporterService implements InitializingBean {

    boolean transactional = true

	def grailsApplication
	def sql

	// This entire service is a temporary piece of code to assist with a slow orderly migration from a PHP based resource registry to a new generation Grails version
	// It is anticipated that over time all data being sourced from the RR will be created in this app and this service and all methods will be totally discontinued
	
	void afterPropertiesSet()
    {
		def oldrr = grailsApplication.config.fedreg.oldrr		
        sql = Sql.newInstance(oldrr.connection, oldrr.user, oldrr.password, oldrr.driver)	
    }

	def dumpData() {
		log.debug("Executing dump data process")
		User.list().each { it.entityDescriptor = null; it.save(); }
		IDPSSODescriptor.list().each {it.delete();}
		EntityDescriptor.list().each {it.delete();}
		Contact.list().each {it.delete();}
		Attribute.list().each {it.delete();}
		Organization.list().each {it.delete();}
		OrganizationType.list().each {it.delete();}
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
			def org = new Organization(name:it.homeOrgName, displayName:it.homeOrgName, lang:it.mainLanguage, url:it.errorURL ?:'http://aaf.edu.au/support', primary: orgType)
			org.save()
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
			def names = it.contactName.split(" ")
			def givenName = names[0]
			def surname = names[1]
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
		sql.eachRow("select * from homeOrgs",
		{
			def org = Organization.findByName(it.homeOrgName)
			def entity = new EntityDescriptor(entityID:it.entityID, organization:org, blah:'blah')
			entity.save()
			if(entity.hasErrors()) {
				entity.errors.each {log.error it}
			}
		})
		
		// Create ContactPerson instances and link entities to contacts
		EntityDescriptor.list().each { ent ->
			sql.eachRow("select email, contactType from contacts INNER JOIN homeOrgs ON contacts.objectID=homeOrgs.homeOrgID WHERE homeOrgs.entityID=${ent.entityID}",
			{
				def email = it.email
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
				switch(it.contactType) {
					case 'technical': type = ContactType.technical; break;
					case 'support': type = ContactType.support; break;
					case 'administrative': type = ContactType.administrative; break;
					case 'billing': type = ContactType.billing; break;
					default: type = ContactType.other; break;
				}
				def contactPerson = new ContactPerson(contact:contact, entity:ent, type:type)
				ent.addToContacts(contactPerson)
			})
			
			ent.save()
		}
	}

	// TODO: Extend to support full range of IDP components, very minimal currently much more to pull in
	def importIdentityProviders() {
		
		sql.eachRow("select * from homeOrgs",
		{			
			def entity = EntityDescriptor.findWhere(entityID:it.entityID)
			def idp = new IDPSSODescriptor(entityDescriptor:entity, organization:entity.organization)
			
			sql.eachRow("select * from serviceLocations where objectID=${it.homeOrgID} and serviceType='SingleSignOnService'",
			{
				def binding = SamlURI.findByUri(it.serviceBinding)
				if(binding) {
					def location = new UrlURI(uri:it.serviceLocation)
					def ssoService = new SingleSignOnService(binding: binding, location: location)
					idp.addToSingleSignOnServices(ssoService)
				}
			})
			
			sql.eachRow("select attributeOID from attributes INNER JOIN homeOrgAttributes ON attributes.attributeID=homeOrgAttributes.attributeID where homeOrgAttributes.homeOrgID=${it.homeOrgID};",
			{
				def attr = Attribute.findByOid(it.attributeOID)
				if(attr)
					idp.addToAttributes(attr)
			})

			//idp.save()
			//if(idp.hasErrors()) {
			//	idp.errors.each {log.error it}
			//}
			entity.addToIdpDescriptors(idp)
			entity.save()
		})	
	}
}
