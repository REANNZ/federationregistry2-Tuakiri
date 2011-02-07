
process(name:'organization_create', description: 'Workflow defining Organization creation and approval process') {
	
	task(name: 'Send confirmation message', description: 'Invokes a script to confirm registration of an Organization.') {
		execute(script: 'organization_confirm')
		outcome(name: 'confirmedorganization', description:'User registering the Organization has been advised of creation') {
			start ('Request Federation approval')
		}
	}
	
	task(name: 'Request Federation approval', description: 'Requests that a user who is an administrative member of the federation approves activation for this Organization as a valuable addition for the federation.') {
		approver(role: 'federation-administrators') {
			reject(name: 'Not Accepted', description:'The federation will not accept this Organization (Organization details will be discarded)') {
				start ('Delete Organization')
			}
		}
		outcome(name: 'federationapproved', description:'The organization has accepted responsibility for this Identity Provider') {
			start ('Activate Organization')
		}
	}
	
	task(name: 'Activate Organization', description: 'Activates the Organization so it can register components with the federation') {
		execute(script: 'organization_activate')
		outcome(name: 'organizationactivated', description:'The Organization is now activate and being populated into Metadata') {
			start ('finish')
		}
	}
	
	task(name: 'Delete Organization', description: 'Deletes the Organization after it has been rejected.') {
		execute(script: 'organization_delete')
		outcome(name: 'organizationdeleted', description:'The Organization definition has been deleted') {
			start ('finish')
		}
	}

	task(name: 'finish', description: 'Completes the organization_create workflow') {
		finish()
	}
	
}