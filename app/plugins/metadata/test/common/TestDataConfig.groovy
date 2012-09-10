testDataConfig {
    sampleData {
        'aaf.fr.foundation.Organization' {
            def i = 1
            name = {-> "name${i++}" }
        }
        'aaf.fr.foundation.OrganizationType' {
            def i = 1
            displayName = {-> "displayName${i++}" }
            name = {-> "name${i++}" }
        }
    }
}
