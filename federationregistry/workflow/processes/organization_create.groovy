
process(name:'organization_create', description: 'Workflow defining Organization creation approval process') {
	
	task(name: 'finish', description: 'Completes the idpssodescriptor_create workflow') {
		finish()
	}
	
}