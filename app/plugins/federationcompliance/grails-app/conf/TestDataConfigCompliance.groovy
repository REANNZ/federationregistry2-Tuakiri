testDataConfig {
	sampleData {
		'fedreg.core.AttributeBase' {
			def i = 1
			name = {-> "name${i++}" } 
		}
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