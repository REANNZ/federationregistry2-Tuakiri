package aaf.fr.foundation

import grails.plugin.spock.*
import org.springframework.transaction.interceptor.TransactionAspectSupport
import org.springframework.transaction.annotation.*

import fedreg.core.*
import fedreg.workflow.*
import grails.plugins.nimble.core.*

class OrganizationServiceSpec extends IntegrationSpec {
	
	def savedMetaClasses
	def workflowProcessService
	def organizationService
	def params
	
	def setup () {
		savedMetaClasses = [:]
		
		SpecHelpers.registerMetaClass(WorkflowProcessService, savedMetaClasses)
		workflowProcessService.metaClass = WorkflowProcessService.metaClass
		
		def user = UserBase.build()
		SpecHelpers.setupShiroEnv(user)
		
		params = [:]
	}
	
	def cleanup() {
		SpecHelpers.resetMetaClasses(savedMetaClasses)
		workflowProcessService.metaClass = WorkflowProcessService.metaClass
	}
	
	def "Create succeeds when valid organization data and valid new contact data is provided"() {
		setup:
		def ot = OrganizationType.build().save()
		
		params.active = true
		params.organization = [name:"test org", displayName:"Test Org Pty Ltd", url:"http://test.org", primary:ot.id, lang:'en']
		params.contact = [givenName:"Bradley", surname:"Beddoes", email:"bradleybeddoes@intient.com"]
		
		def wfProcessName, wfDescription, wfPriority, wfParams
		WorkflowProcessService.metaClass.initiate =  { String processName, String instanceDescription, ProcessPriority priority, Map params ->
			wfProcessName = processName
			wfDescription = instanceDescription
			wfPriority = priority
			wfParams = params
			[true, [:]]
		}
		WorkflowProcessService.metaClass.run = { def processInstance -> }
		
		when:
		def (created, organization_, contact_) = organizationService.create(params)
		
		then:
		organization_.name == "test org"
		organization_.displayName ==  "Test Org Pty Ltd"
		organization_.url == "http://test.org"
		organization_.primary == ot
		
		contact_.givenName == "Bradley"
		contact_.surname == "Beddoes"
		contact_.email == "bradleybeddoes@intient.com"
		
		wfProcessName == "organization_create"
		wfDescription == "Approval for creation of Organization Test Org Pty Ltd"
		wfPriority == ProcessPriority.MEDIUM
		wfParams.size() == 3
		wfParams.organization == "${organization_.id}"
		wfParams.creator == "${contact_.id}"
	}
	
	def "Create succeeds when valid organization data and existing contact is provided"() {
		setup:
		def ot = OrganizationType.build().save()
		def contact = Contact.build().save()
		
		params.active = true
		params.organization = [name:"test org2", displayName:"Test Org2 Pty Ltd", url:"http://test.org", primary:ot.id, lang:'en']
		params.contact = [id:contact.id]
		
		def wfProcessName, wfDescription, wfPriority, wfParams
		WorkflowProcessService.metaClass.initiate =  { String processName, String instanceDescription, ProcessPriority priority, Map params ->
			wfProcessName = processName
			wfDescription = instanceDescription
			wfPriority = priority
			wfParams = params
			[true, [:]]
		}
		WorkflowProcessService.metaClass.run = { def processInstance -> }
		
		when:
		def (created, organization_, contact_) = organizationService.create(params)
		
		then:
		created
		
		organization_.name == "test org2"
		organization_.displayName ==  "Test Org2 Pty Ltd"
		organization_.url == "http://test.org"
		organization_.primary == ot
		
		contact_.givenName == contact.givenName
		contact_.surname == contact.surname
		contact_.email == contact.email
		
		wfProcessName == "organization_create"
		wfDescription == "Approval for creation of Organization Test Org2 Pty Ltd"
		wfPriority == ProcessPriority.MEDIUM
		wfParams.size() == 3
		wfParams.organization == "${organization_.id}"
		wfParams.creator == "${contact_.id}"
	}
	
	def "Create fails when invalid organization data and existing contact is provided"() {
		setup:
		def ot = OrganizationType.build().save()
		def contact = Contact.build().save()
		
		params.active = true
		params.organization = [displayName:"Test Org3 Pty Ltd", url:"http://test.org", primary:ot.id, lang:'en']
		params.contact = [id:contact.id]
		
		when:
		def (created, organization_, contact_) = organizationService.create(params)
		
		then:
		!created
		
		organization_.name == null
		organization_.displayName ==  "Test Org3 Pty Ltd"
		organization_.url == "http://test.org"
		organization_.primary == ot
		
		contact_.givenName == contact.givenName
		contact_.surname == contact.surname
		contact_.email == contact.email
	}
	
	def "Create fails when invalid organization data and invalid contact data is provided"() {
		setup:
		def ot = OrganizationType.build().save()
		
		params.active = true
		params.organization = [displayName:"Test Org4 Pty Ltd", url:"http://test.org", primary:ot.id, lang:'en']
		params.contact = [givenName:"Bradley", surname:"Beddoes", email:"zzz"]
		
		when:
		def (created, organization_, contact_) = organizationService.create(params)
		
		then:
		!created
		
		organization_.name == null
		organization_.displayName ==  "Test Org4 Pty Ltd"
		organization_.url == "http://test.org"
		organization_.primary == ot
		
		contact_.givenName == "Bradley"
		contact_.surname == "Beddoes"
		contact_.email == "zzz"
	}
	
	def "Update succeeds when providing valid data"() {
		setup:
		def ot = OrganizationType.build(name:'ot1').save()
		def ot2 = OrganizationType.build(name:'ot2').save()
		def ot3 = OrganizationType.build(name:'ot3').save()
		def ot4 = OrganizationType.build(name:'ot4').save()
		def organization = Organization.build(primary: ot).save();
		
		params.id = organization.id
		params.organization = [name: "Test Org5", displayName:"Test Org5 Pty Ltd", url:"http://test.org", primary:ot2.id, lang:'en', active:'true', types:[(ot3.id):'on', (ot4.id):'on']]
		
		when:
		def (updated, organization_) = organizationService.update(params)
		
		then:
		updated
		organization_.name == "Test Org5"
		organization_.displayName == "Test Org5 Pty Ltd"
		organization_.primary == ot2
		organization_.url == "http://test.org"
		organization_.types.contains(ot3)
		organization_.types.contains(ot4)
	}
	
	def "Update fails when providing invalid data"() {
		setup:
		def ot = OrganizationType.build().save()
		def ot2 = OrganizationType.build().save()
		def ot3 = OrganizationType.build().save()
		def ot4 = OrganizationType.build().save()
		def organization = Organization.build(primary: ot).save();
		
		params.id = organization.id
		params.organization = [name: "", displayName:"Test Org6 Pty Ltd", url:"http://test.org", primary:ot2.id, lang:'en', active:'true', types:[(ot3.id):'on', (ot4.id):'on']]
		
		when:
		def (updated, organization_) = organizationService.update(params)
		
		then:
		!updated
		organization_.name == ""
		organization_.displayName == "Test Org6 Pty Ltd"
		organization_.primary == ot2
		organization_.url == "http://test.org"
		organization_.types.contains(ot3)
		organization_.types.contains(ot4)
	}
	
}