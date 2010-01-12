
fedreg {
	shibboleth {
		name = "Australian Access Federation"
        displayname = "Australian Access Federation"
        description = "The Australian Access Federation provides the means of allowing a participating institution and/or a service provider to trust the information it receives from another participating institution."
        url = "http://www.aaf.edu.au"
        alttext = "Australian Access Federation"

        federationprovider {
            enabled = false
            autoprovision = true
        }

		headers {
			uniqueIdentifier = "persistent-id"
			givenName= "giveName"
			surname= "sn"
			email= "mail"
		}
    }
}