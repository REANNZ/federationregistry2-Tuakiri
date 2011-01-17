import fedreg.core.*

testDataConfig {
	sampleData {
		'fedreg.core.OrganizationType' {
			def i = 1
			name = {-> "name${i++}" } 
		}
		'fedreg.core.Organization' {
			def i = 1
			name = {-> "name${i++}" } 
			url = { -> new UrlURI(uri:"http://www.name${i++}.com")}
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
		'grails.plugins.nimble.core.UserBase' {
			def i = 1
			username = {-> "username${i++}" }
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
