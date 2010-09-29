package fedreg.core

import grails.plugin.spock.*

import fedreg.core.*
import fedreg.workflow.*
import grails.plugins.nimble.core.*

class EntityDescriptorServiceSpec extends IntegrationSpec {
	def cryptoService
	def savedMetaClasses
	def entityDescriptorService
	def params
	
	def setup () {
		savedMetaClasses = [:]
		
		entityDescriptorService = new EntityDescriptorService()
		def user = UserBase.build()
		SpecHelpers.setupShiroEnv(user)
		
		params = [:]
	}
	
	def cleanup() {
		SpecHelpers.resetMetaClasses(savedMetaClasses)

	}
	
	def "Creating valid entity descriptor succeeds (without initial contact)"() {
		setup:
		def organization = Organization.build().save()
		def ct = ContactType.build().save()
		
		params.organization = [id: organization.id]
		params.active = true
		params.entity = [identifier:"http://identityProvider.test.com"]
		params.contact = [givenName:"Bradley", surname:"Beddoes", email:"bradleybeddoes@intient.com", type:ct.name]
		
		when:
		def(created, entityDescriptor_) = entityDescriptorService.create(params)
		
		then:
		created
		EntityDescriptor.count() == 1
		Contact.count() == 1
		entityDescriptor_ != null
		entityDescriptor_.organization == organization
		entityDescriptor_.contacts.size() == 1
	}
	
	def "Creating valid entity descriptor succeeds (with initial contact)"() {
		setup:
		def organization = Organization.build().save()
		def ct = ContactType.build().save()
		def contact = Contact.build(organization: organization).save()
		
		params.organization = [id: organization.id]
		params.active = true
		params.entity = [identifier:"http://ed.test.com"]
		params.contact = [id:contact.id, type:ct.name]
		
		when:
		def(created, entityDescriptor_) = entityDescriptorService.create(params)
		
		then:
		created
		EntityDescriptor.count() == 1
		Contact.count() == 1
		entityDescriptor_ != null
		entityDescriptor_.organization == organization
		entityDescriptor_.contacts.size() == 1
	}
	
	def "Creating entity descriptor with invalid data fails"() {
		setup:
		def organization = Organization.build().save()
		def ct = ContactType.build().save()
		
		params.organization = [id: organization.id]
		params.active = true
		params.entity = [identifier:""]
		params.contact = [givenName:"Bradley", surname:"Beddoes", email:"bradleybeddoes@intient.com", type:ct.name]
		
		when:
		def(created, entityDescriptor_) = entityDescriptorService.create(params)
		
		then:
		!created
		EntityDescriptor.count() == 0
		Contact.count() == 1
		entityDescriptor_ != null
		entityDescriptor_.organization == organization
		entityDescriptor_.contacts.size() == 1
	}
	
	def "Updating entity descriptor with valid data succeeds"() {
		setup:
		def organization = Organization.build().save()
		def entityDescriptor = EntityDescriptor.build(organization:organization).save()
		
		params.id = entityDescriptor.id
		params.entity = [identifier : "http://newed.test.com"]
		
		when:
		def(updated, entityDescriptor_) = entityDescriptorService.update(params)
		
		then:
		updated
		EntityDescriptor.count() == 1
		entityDescriptor_ != null
		entityDescriptor_.organization == organization
		entityDescriptor.entityID == "http://newed.test.com"
	}
	
	def "Updating entity descriptor with invalid data fails"() {
		setup:
		def organization = Organization.build().save()
		def entityDescriptor = EntityDescriptor.build(organization:organization).save()
		
		params.id = entityDescriptor.id
		params.entity = [identifier : ""]
		
		when:
		def(updated, entityDescriptor_) = entityDescriptorService.update(params)
		
		then:
		!updated
		EntityDescriptor.count() == 1
		entityDescriptor_.hasErrors()
		entityDescriptor_ != null
		entityDescriptor_.organization == organization
		entityDescriptor.entityID == ""
	}
	
	def "Updating entity descriptor with duplicate entityID fails"() {
		setup:
		def organization = Organization.build().save()
		def entityDescriptor = EntityDescriptor.build(organization:organization).save()
		def entityDescriptor2 = EntityDescriptor.build(entityID: "http://my.entityid.com").save()
		
		params.id = entityDescriptor.id
		params.entity = [identifier : "http://my.entityid.com"]
		
		when:
		def(updated, entityDescriptor_) = entityDescriptorService.update(params)
		
		then:
		!updated
		EntityDescriptor.count() == 2
		entityDescriptor_.hasErrors()
		entityDescriptor_.errors.getFieldError('entityID').code == 'unique'
		entityDescriptor_ != null
		entityDescriptor_.organization == organization
		entityDescriptor.entityID == "http://my.entityid.com"
	}
}