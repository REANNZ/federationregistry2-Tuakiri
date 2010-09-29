
process(name:'idpssodescriptor_create', description: 'Workflow defining Identity Provider creation and approval process') {
	
	task(name: 'Send confirmation message', description: 'Invokes a script to confirm registration of IDP.') {
		execute(script: 'idpssodescriptor_confirm')
		outcome(name: 'confirmedidpssodescriptor', description:'User registering the IDP has been advised of creation') {
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
	
	task(name: 'Request organization approval', description: 'Requests that a user who is an administrative member of the owning organization approves creation and associated billing by introducing this Identity Provider.') {
		approver(role: 'organization-{organization}-administrators') {
			reject(name: 'Not Associated', description:'Not an Identity Provider associated with this organization (Identity Provider details will be discarded)') {
				start ('Delete Identity Provider')
			}
			reject(name: 'Not Accepted', description:'The organization will not accept ownership for this Identity Provider (Identity Provider details will be discarded)') {
				start ('Delete Identity Provider')
			}
		}
		outcome(name: 'organizationapproved', description:'The organization has accepted responsibility for this Identity Provider') {
			start ('Request Federation approval')
		}
	}
	
	task(name: 'Request Federation approval', description: 'Requests that a user who is an administrative member of the federation approves activation for this Identity Provider as a valuable addition for the federation.') {
		approver(role: 'federation-administrators') {
			reject(name: 'Not Accepted', description:'The federation will not accept ownership for this Identity Provider (Identity Provider details will be discarded)') {
				start ('Delete Identity Provider')
			}
		}
		outcome(name: 'organizationapproved', description:'The organization has accepted responsibility for this Identity Provider') {
			start ('Activate Identity Provider')
		}
	}
	
	task(name: 'Request executive federation approval', description: 'Requests that a user who is an administrative member of the federation approves activation for this Identity Provider for the Federation and on behalf of the owning Organization. External verification should be undertaken.') {
		approver(role: 'federation-administrators') {
			reject(name: 'Not Accepted', description:'The federation will not accept ownership for this Identity Provider (Identity Provider details will be discarded).') {
				start ('Delete Identity Provider')
			}
			reject(name: 'Organization Not Accepted', description:'The organization responsible will not accept ownership for this Identity Provider (Identity Provider details will be discarded).') {
				start ('Delete Identity Provider')
			}
		}
		outcome(name: 'organizationapproved', description:'The organization has accepted responsibility for this Identity Provider') {
			start ('Activate Identity Provider')
		}
	}
	
	task(name: 'Activate Identity Provider', description: 'Activates the Identity Provider so it can be rendered into Metadata.') {
		execute(script: 'idpssodescriptor_activate')
		outcome(name: 'idpssodescriptoractivated', description:'The Identity Provider is now activate and being populated into Metadata') {
			start ('finish')
		}
	}
	
	task(name: 'Delete Identity Provider', description: 'Deletes the Identity Provider after it has been rejected.') {
		execute(script: 'idpssodescriptor_delete')
		outcome(name: 'idpssodescriptordeleted', description:'The Identity Provider definition has been deleted') {
			start ('finish')
		}
	}

	task(name: 'finish', description: 'Completes the idpssodescriptor_create workflow') {
		finish()
	}
	
}