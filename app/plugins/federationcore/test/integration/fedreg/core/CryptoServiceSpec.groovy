package fedreg.core

import java.security.*
import java.security.cert.*

import grails.plugin.spock.*

class CryptoServiceSpec extends IntegrationSpec {
	
	def cryptoService
	
	def 'validate signing certificate association with role descriptor'() {
		setup:
		def ca = new File('./test/integration/data/demoCA/cacertminimal.pem').text
		def caCert = new CACertificate(data:ca)
		def caKeyInfo = new CAKeyInfo(certificate:caCert)
		caKeyInfo.save()
		
		def idp = IDPSSODescriptor.build().save()
		def data = new File('./test/integration/data/newcertminimal.pem').text
		
		when:
		def created = cryptoService.associateCertificate(idp, data, "testcert", KeyTypes.signing)
		
		then:
		created
		idp.keyDescriptors.size() == 1
		def kd = idp.keyDescriptors.toArray()[0]
		kd.keyType == KeyTypes.signing
		kd.keyInfo.keyName == "testcert" 
		kd.keyInfo.certificate.data == data
		!kd.disabled
	}
	
	def 'validate encryption certificate association with role descriptor'() {
		setup:
		def ca = new File('./test/integration/data/demoCA/cacertminimal.pem').text
		def caCert = new CACertificate(data:ca)
		def caKeyInfo = new CAKeyInfo(certificate:caCert)
		caKeyInfo.save()
		
		def idp = IDPSSODescriptor.build().save()
		def data = new File('./test/integration/data/newcertminimal.pem').text
		
		when:
		def created = cryptoService.associateCertificate(idp, data, "testcert", KeyTypes.encryption)
		
		then:
		created
		idp.keyDescriptors.size() == 1
		def kd = idp.keyDescriptors.toArray()[0]
		kd.keyType == KeyTypes.encryption
		kd.keyInfo.keyName == "testcert" 
		kd.keyInfo.certificate.data == data
		!kd.disabled
	}
	
	def 'validate dual certificate association with role descriptor'() {
		setup:
		def ca = new File('./test/integration/data/demoCA/cacertminimal.pem').text
		def caCert = new CACertificate(data:ca)
		def caKeyInfo = new CAKeyInfo(certificate:caCert)
		caKeyInfo.save()
		
		def idp = IDPSSODescriptor.build().save()
		def data = new File('./test/integration/data/newcertminimal.pem').text
		
		when:
		def created = cryptoService.associateCertificate(idp, data, "testcert", KeyTypes.signing)
		created = cryptoService.associateCertificate(idp, data, "testcert", KeyTypes.encryption)
		
		then:
		created
		idp.keyDescriptors.size() == 2
	}
	
	def 'validate invalid certificate association with role descriptor fails'() {
		setup:
		def ca = new File('./test/integration/data/demoCA/cacertminimal.pem').text
		def caCert = new CACertificate(data:ca)
		def caKeyInfo = new CAKeyInfo(certificate:caCert)
		caKeyInfo.save()
		
		def idp = IDPSSODescriptor.build().save()
		def data = new File('./test/integration/data/managertestaaf.pem').text
		
		when:
		def created = cryptoService.associateCertificate(idp, data, "testcert", KeyTypes.signing)
		
		then:
		!created
		idp.keyDescriptors == null
	}
	
	def 'validate certificate unassociation from role descriptor'() {
		setup:
		def ca = new File('./test/integration/data/demoCA/cacertminimal.pem').text
		def caCert = new CACertificate(data:ca)
		def caKeyInfo = new CAKeyInfo(certificate:caCert)
		caKeyInfo.save()
		
		def idp = IDPSSODescriptor.build().save()
		def data = new File('./test/integration/data/newcertminimal.pem').text
		def created = cryptoService.associateCertificate(idp, data, "testcert", KeyTypes.signing)
		
		when:
		cryptoService.unassociateCertificate(idp.keyDescriptors.toArray()[0])
		
		then:
		created
		idp.keyDescriptors.size() == 1
		def kd = idp.keyDescriptors.toArray()[0]
		kd.keyType == KeyTypes.signing
		kd.keyInfo.keyName == "testcert" 
		kd.keyInfo.certificate.data == data
		kd.disabled
	}
  
	def 'validate self signed cert with trusted local CA chain'() {
		setup:
		def ca = new File('./test/integration/data/demoCA/cacertminimal.pem').text
		def caCert = new CACertificate(data:ca)
		def caKeyInfo = new CAKeyInfo(certificate:caCert)
		caKeyInfo.save()
		
		def pk = new File('./test/integration/data/newcertminimal.pem').text
		def testCert = new Certificate(data:pk)
		
		expect:
		cryptoService.validateCertificate(testCert) == true
	}
	
	def 'ensure failure with untrusted self signed CA chain'() {
		setup:
		def ca = new File('./test/integration/data/auscertintermediate.crt').text
		def caCert = new CACertificate(data:ca)
		def caKeyInfo = new CAKeyInfo(certificate:caCert)
		caKeyInfo.save()
		
		def pk = new File('./test/integration/data/newcertminimal.pem').text
		def testCert = new Certificate(data:pk)
		
		expect:
		!cryptoService.validateCertificate(testCert, true)
	}
	
	def 'ensure validation of multiple certs from different CA chains (Local CA and Auscert intermediate to Comondo)'() {
		def testCert, testCert2
		setup:
		def ca = new File('./test/integration/data/demoCA/cacertminimal.pem').text
		def caCert = new CACertificate(data:ca)
		def caKeyInfo = new CAKeyInfo(certificate:caCert)
		caKeyInfo.save()
		
		def ca2 = new File('./test/integration/data/auscertintermediate.crt').text
		def caCert2 = new CACertificate(data:ca2)
		def caKeyInfo2 = new CAKeyInfo(certificate:caCert2)
		caKeyInfo2.save()
		
		expect:
		cryptoService.validateCertificate(cert) == true
		
		where:
		cert << [new Certificate(data:new File('./test/integration/data/managertestaaf.pem').text), new Certificate(data:new File('./test/integration/data/newcertminimal.pem').text)]
	}
	
	def 'ensure valid expiry date calculated'() {
		setup:
        def cert = new Certificate(data:new File('./test/integration/data/managertestaaf.pem').text)
        
        expect:
        // This awful for now but having cross server UTC issues and out of dev time
		cryptoService.expiryDate(cert) instanceof Date
	}
	
	def 'ensure valid issuer'() {
		
		expect:
		cryptoService.issuer(cert) == issuer
		
		where:
		cert << [new Certificate(data:new File('./test/integration/data/managertestaaf.pem').text)]
		issuer = 'CN=AusCERT Server CA,OU=Certificate Services,O=AusCERT,C=AU'	
	}

}