testDataConfig {
	sampleData {
		'fedreg.core.OrganizationType' {
			def i = 1
			name = {-> "name${i++}" } 
		}
		'fedreg.core.Organization' {
			def i = 1
			name = {-> "name${i++}" } 
		}
		'fedreg.core.EntityDescriptor' {
			def i = 1
			entityID = {-> "http://test.case.com/${i++}" } 
		}
	}
}

environments {
    production {
        testDataConfig {
            enabled = false
        }
    }
	dev {
        testDataConfig {
            enabled = false
        }
    }
}
