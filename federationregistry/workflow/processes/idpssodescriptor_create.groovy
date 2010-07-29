
process(name:'idpssodescriptor_create', description: 'Workflow defining IDPSSODescription creation approval process') {
	
	task(name: 'finish', description: 'Completes the idpssodescriptor_create workflow') {
		finish()
	}
	
}