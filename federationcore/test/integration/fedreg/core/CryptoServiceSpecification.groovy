package fedreg.core

import java.security.*
import java.security.cert.*

import grails.plugin.spock.IntegrationSpecification

class CryptoServiceSpecification extends IntegrationSpecification {
	
	def cryptoService
  
	def 'validate self signed cert with trusted local CA chain'() {
		setup:
		def ca = new File('./test/integration/data/demoCA/cacertminimal.pem').text
		def caCert = new CACertificate(data:ca)
		def caKeyInfo = new CAKeyInfo(certificate:caCert)
		caKeyInfo.save()
		
		def pk = new File('./test/integration/data/newcertminimal.pem').text
		def testCert = new Certificate(data:pk)
		
		expect:
		CACertificate.count() == 1
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
		CACertificate.count() == 1
		cryptoService.validateCertificate(testCert) == false
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
		CACertificate.count() == 2
		cryptoService.validateCertificate(cert) == true
		
		where:
		cert << [new Certificate(data:new File('./test/integration/data/managertestaaf.pem').text), new Certificate(data:new File('./test/integration/data/newcertminimal.pem').text)]
	}

}