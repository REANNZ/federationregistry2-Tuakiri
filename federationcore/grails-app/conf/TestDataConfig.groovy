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
		'fedreg.core.AttributeBase' {
			def i = 1
			name = {-> "name${i++}" } 
		}
		'fedreg.core.EntityDescriptor' {
			def i = 1
			entityID = {-> "http://test.case.com/${i++}" } 
		}
		'grails.plugins.nimble.core.UserBase' {
			def i = 1
			username = {-> "username${i++}" } 
		}
		'fedreg.core.MailURI' {
			def i = 1
			uri = {-> "mail${i++}@test.com" }
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
