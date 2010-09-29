
process(name:'spssodescriptor_create', description: 'Workflow defining Service Provider creation and approval process') {
	
	task(name: 'Send confirmation message', description: 'Invokes a script to confirm registration of SP.') {
		execute(script: 'spssodescriptor_confirm')
		outcome(name: 'confirmedspssodescriptor', description:'User registering the SP has been advised of creation') {
			start ('Check for valid approvers')
		}
	}
	
	task(name: 'Check for valid approvers', description: 'Ensures that organization-{organization}-administrators is actually populated with users, if not bypasses directly to federation administrators.') {
		execute(script: 'organization_administrators_populated')
		outcome(name: 'organization_hasadministrators', description:'The Organization has administrators and they will be asked to approve.') {
			start ('Request organization approval')
		}
		outcome(name: 'organization_noadministrators', description:'The Organization has no locally registered administrators. Federation Administrators will undertake an out of band approval process.') {
			start ('Request executive federation approval')
		}
	}
	
	task(name: 'Request organization approval', description: 'Requests that a user who is an administrative member of the owning organization approves creation and associated billing by introducing this Service Provider.') {
		approver(role: 'organization-{organization}-administrators') {
			reject(name: 'Not Associated', description:'Not a Service Provider associated with this organization (Service Provider details will be discarded)') {
				start ('Delete Service Provider')
			}
			reject(name: 'Not Accepted', description:'The organization will not accept ownership for this Service Provider (Service Provider details will be discarded)') {
				start ('Delete Service Provider')
			}
		}
		outcome(name: 'organizationapproved', description:'The organization has accepted responsibility for this Service Provider') {
			start ('Request Federation approval')
		}
	}
	
	task(name: 'Request Federation approval', description: 'Requests that a user who is an administrative member of the federation approves activation for this Service Provider as a valuable addition for the federation.') {
		approver(role: 'federation-administrators') {
			reject(name: 'Not Accepted', description:'The federation will not accept ownership for this Service Provider (Service Provider details will be discarded)') {
				start ('Delete Service Provider')
			}
		}
		outcome(name: 'federationapproved', description:'The organization has accepted responsibility for this Service Provider') {
			start ('Activate Service Provider')
		}
	}
	
	task(name: 'Request executive federation approval', description: 'Requests that a user who is an administrative member of the federation approves activation for this Service Provider for the Federation and on behalf of the owning Organization. External verification should be undertaken.') {
		approver(role: 'federation-administrators') {
			reject(name: 'Not Accepted', description:'The federation will not accept ownership for this Service Provider (Service Provider details will be discarded).') {
				start ('Delete Service Provider')
			}
			reject(name: 'Organization Not Accepted', description:'The organization responsible will not accept ownership for this Service Provider (Service Provider details will be discarded).') {
				start ('Delete Service Provider')
			}
		}
		outcome(name: 'executivelyapproved', description:'The organization and federation combined have accepted responsibility for this Service Provider') {
			start ('Activate Service Provider')
		}
	}
	
	task(name: 'Activate Service Provider', description: 'Activates the Service Provider so it can be rendered into Metadata.') {
		execute(script: 'spssodescriptor_activate')
		outcome(name: 'spssodescriptoractivated', description:'The Service Provider is now activate and being populated into Metadata') {
			start ('finish')
		}
	}
	
	task(name: 'Delete Service Provider', description: 'Deletes the Service Provider after it has been rejected.') {
		execute(script: 'spssodescriptor_delete')
		outcome(name: 'spssodescriptordeleted', description:'The Service Provider definition has been deleted') {
			start ('finish')
		}
	}

	task(name: 'finish', description: 'Completes the spssodescriptor_create workflow') {
		finish()
	}
	
}