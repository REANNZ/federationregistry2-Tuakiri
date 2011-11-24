import fedreg.core.*

testDataConfig {
	sampleData {
		'fedreg.core.OrganizationType' {
			def i = 1
			name = {-> "ot_name${i++}" } 
		}
		'fedreg.core.Organization' {
			def i = 1
			name = {-> "org_name${i++}" } 
			url = { -> "http://www.org-name${i++}.com"}
		}
		'fedreg.core.AttributeBase' {
			def i = 1
			name = {-> "attributebase_name${i++}" } 
		}
		'fedreg.core.EntityDescriptor' {
			def i = 1
			entityID = {-> "http://test.case.com/${i++}" } 
		}
		'grails.plugins.nimble.core.UserBase' {
			def i = 1
			username = {-> "username${i++}" } 
		}
		'fedreg.core.Contact' {
			def i = 1
			email = {-> "mailbox_${i++}@test.com" }
		}
	}
}

environments {
    production {
        testDataConfig {
            enabled = false
        }
    }
	test {
        testDataConfig {
            enabled = true
        }
    }
	dev {
        testDataConfig {
            enabled = false
        }
    }
}
