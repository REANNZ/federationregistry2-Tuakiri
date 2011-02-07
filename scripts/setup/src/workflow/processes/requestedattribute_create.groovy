process(name:'requestedattribute_create', description: 'Workflow defining Requested Attribute creation and approval process for SPSSODescriptors') {
	
	task(name: 'Auto approve', description: 'Automatically approves all new attributes for SPSSOdescriptors, useful as default in test environments') {
		execute(script: 'requestedattribute_activate')
		outcome(name: 'requestedattributeactivated', description:'The Requested Attribute is now activate and being populated into Metadata') {
			start ('finish')
		}
	}
	
	task(name: 'finish', description: 'Completes the requestedattribute_create workflow') {
		finish()
	}

}