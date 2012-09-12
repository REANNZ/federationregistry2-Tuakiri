import aaf.fr.foundation.*

testDataConfig {
  sampleData {
    'aaf.fr.foundation.OrganizationType' {
      def i = 1
      name = {-> "ot_name${i++}" } 
      displayName = {-> "ot_displayname${i++}" } 
    }
    'aaf.fr.foundation.Organization' {
      def i = 1
      name = {-> "org_name${i++}" } 
      url = { -> "http://www.org-name${i++}.com"}
    }
    'aaf.fr.foundation.AttributeBase' {
      def i = 1
      name = {-> "attributebase_name${i++}" } 
    }
    'aaf.fr.foundation.EntityDescriptor' {
      def i = 1
      entityID = {-> "http://test.case.com/${i++}" } 
    }
    'aaf.fr.foundation.IDPSSODescriptor' {
      def i = 1
      displayName = {-> "idp_displayname${i++}" } 
      scope = {-> "${i++}-uni.edu.au" }
    }
    'aaf.fr.identity.Subject' {
      def i = 1
      principal = {-> "principal${i++}" } 
    }
    'aaf.fr.foundation.Contact' {
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
