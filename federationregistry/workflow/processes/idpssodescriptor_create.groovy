
process(name:'idpssodescriptor_create', description: 'Workflow defining IDP creation approval process') {
	
	task(name: 'orgApproval', description: 'Request approval from owning organization administrators') {
		approver(role: '{organization}_approvers') {
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
	
	task(name: 'finish', description: 'Completes the idpssodescriptor_create workflow') {
		finish()
	}
	
}