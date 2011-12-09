import aaf.fr.identity.AdminsService
import aaf.fr.identity.SubjectService

/**
 * Filter that works with shiro security model to protect controllers, actions, views for Federation Registry
 *
 * @author Bradley Beddoes
 */
public class SecurityFilters {

	def grailsApplication

	def filters = {
	
		// Undertake bootstrap
		all(controller: '*') {
			before = {
				if( !['initialBootstrap','console'].contains(controllerName) && grailsApplication.config.fedreg.bootstrap)
					redirect (controller: "initialBootstrap")
			}
		}
		
		// Invitations
		invitations(controller: "invitation") {
			before = {
				accessControl { true }
			}
		}

		// Dashboard
		dashboard(controller: "dashboard") {
			before = {
				accessControl { true }
			}
		}

		// Members
		members(controller: "(organization|entityDescriptor|identityProvider|serviceProvider|contacts)") {
			before = {
				accessControl { true }
			}
		}

		// Members Backend
		membersbackend(controller: "(attributeConsumingSerivce|descriptorAdministration|descriptorAttribute|descriptorContact|descriptorEndpoint|descriptorNameIDFormat|organizationAdministration|organizationContact|roleDescriptorCrypto|roleDescriptorMonitor)") {
			before = {
				accessControl { true }
			}
		}
		
		// Service Categories
		servicecategories(controller: "serviceCategory", action:"(list|add|remove)") {
			before = {
				accessControl { true }
			}
		}
		
		// Reporting
		compliance(controller: "(federationReports|identityProviderReports|serviceProviderReports|identityProviderAttributeCompliance|attributeRelease|certifyingAuthorityUsage)") {
			before = {
				accessControl { true }
			}
		}

		// Workflow
		workflow(controller: "workflow*") {
			before = {
				accessControl { true }
			}
		}

		// Metadata
		metadata(controller: "metadata", action:"(view|viewall)") {
			before = {
				accessControl { true }
			}
		}

		// Monitoring functionality
		monitoring(controller: "monitor") {
			before = {
				accessControl { true }
			}
		}

		// Administrative components
		administration(controller: "(admins|user|role)") {
			before = {
				accessControl { true }
			}
		}

		// Console and initial bootstrap
		console(controller: "(code|console|initialBootstrap)") {
			before = {
				if( ['initialBootstrap'].contains(controllerName) && !grailsApplication.config.fedreg.bootstrap)
					redirect (controller: "dashboard")
				else {	
					if(!grailsApplication.config.fedreg.bootstrap) {
						accessControl {
							role(AdminsService.ADMIN_ROLE)
						}
					}
				}
			}
		}

	}

}
