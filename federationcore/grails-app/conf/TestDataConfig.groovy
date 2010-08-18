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
