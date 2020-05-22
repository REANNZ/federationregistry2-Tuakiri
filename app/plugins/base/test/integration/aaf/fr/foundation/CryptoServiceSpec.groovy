package aaf.fr.foundation

import java.security.*
import java.security.cert.*

import grails.test.spock.*

class CryptoServiceSpec extends IntegrationSpec {

	def cryptoService
	def grailsApplication

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
