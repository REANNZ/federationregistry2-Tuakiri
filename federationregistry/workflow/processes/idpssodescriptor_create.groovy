
process(name:'idpssodescriptor_create', description: 'Workflow defining IDPSSODescription creation approval process') {
	
	task(name: 'organizationapproval', description: 'Requests that a user who is an administrative/executive member of the owning organization approves creation and associated billing by introducing this Identity Provider') {
		approver(role: 'organization-{organization}-administrators') {
			reject(name: 'Not Associated', description:'Not an Identity Provider associated with this organization (Identity Provider details will be discarded)') {
				start ('deleteidpssodescriptor')
			}
			reject(name: 'Not Accepted', description:'The organization will not accept ownership for this Identity Provider (Identity Provider details will be discarded)') {
				start ('deleteidpssodescriptor')
			}
		}
		outcome(name: 'organizationapproved', description:'The organization has accepted responsibility for this Identity Provider') {
			start ('federationapproval')
		}
	}
	
	task(name: 'federationapproval', description: 'Requests that a user who is an administrative/executive member of the federation approves creation and activation for this Identity Provider as a valuable addition for the federation') {
		approver(role: 'federation-administrators') {
			reject(name: 'Not Accepted', description:'The federation will not accept ownership for this Identity Provider (Identity Provider details will be discarded)') {
				start ('finish')
			}
		}
		outcome(name: 'organizationapproved', description:'The organization has accepted responsibility for this Identity Provider') {
			start ('activateidpssodescriptor')
		}
	}
	
	task(name: 'activateidpssodescriptor', description: 'Activates the Identity Provider so it can be rendered into Metadata') {
		execute(script: 'idpssodescriptor_activate')
		outcome(name: 'idpssodescriptoractivated', description:'The Identity Provider is now activate and being populated into Metadata') {
			start ('finish')
		}
	}
	
	task(name: 'deleteidpssodescriptor', description: 'Deletes the Identity Provider after it has been rejected for some reason') {
		execute(script: 'idpssodescriptor_delete')
		outcome(name: 'idpssodescriptordeleted', description:'The Identity Provider definition has been deleted') {
			start ('finish')
		}
	}

	task(name: 'finish', description: 'Completes the idpssodescriptor_create workflow') {
		finish()
	}
	
}