package aaf.fedreg

import org.springframework.beans.factory.InitializingBean
import groovy.sql.Sql

import aaf.fedreg.core.Attribute
import aaf.fedreg.core.AttributeScope
import aaf.fedreg.core.AttributeCategory
import aaf.fedreg.core.IdentityProvider
import aaf.fedreg.core.Organization
import aaf.fedreg.core.OrganizationType


import aaf.fedreg.saml2.metadata.orm.SamlURI
import aaf.fedreg.saml2.metadata.orm.UrlURI
import aaf.fedreg.saml2.metadata.orm.SingleSignOnService


class DataImporterService implements InitializingBean {

    boolean transactional = true

	def grailsApplication
	def sql

	// This entire service is a temporary piece of code to assist with a slow orderly migration from a PHP based resource registry to a new generation Grails version
	// It is anticipated that over time all data being sourced from the RR will be created in this app and this service and all methods will be totally discontinued
	
	void afterPropertiesSet()
    {
		def oldrr = grailsApplication.config.aaf.fedreg.oldrr		
        sql = Sql.newInstance(oldrr.connection, oldrr.user, oldrr.password, oldrr.driver)	
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

	// TODO: Extend to support full range of IDP components, very minimal currently much more to pull in
	def importIdentityProviders() {
		
		sql.eachRow("select * from homeOrgs",
		{			
			
			def org = Organization.findByName(it.homeOrgName)
			def idp = new IdentityProvider(organization: org)
			
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

			idp.save()
		})
		
		
	}
}
