
process(name:'spssodescriptor_create', description: 'Workflow defining SPSSODescription creation approval process') {
	
	task(name: 'finish', description: 'Completes the spssodescriptor_create workflow') {
		finish()
	}
	
}