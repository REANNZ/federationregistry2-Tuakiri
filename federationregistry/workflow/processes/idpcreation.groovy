
process(name:'idpCreation', description: 'Workflow defining IDP creation approval process') {
	
	task(name: 'orgApproval', description: 'Request approval from owning organization administrators') {
		approver(role: '{IDP_ORG}') {
			reject(name: 'invalidDefinition', description:'Technical or descriptive content about this IdP is invalid') {
				start ('finish')
			}
			reject(name: 'notAccepted', description:'IdP is not valid for this organization and will be deleted') {
				start ('finish')
			}
		}
		outcome(name: 'idpOrgApproved', description:'testoutcome1 description') {
			start ('finish')
		}
    }
	
	task(name: 'finish', description: 'Completes the idpCreate workflow') {
		finish()
	}
	
}