package aaf.fr.foundation

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import java.security.*
import java.security.cert.*

/**
 * Provides methods for managing Crypto/Certificates.
 *
 * @author Bradley Beddoes
 */
class CryptoService {
  static transactional = true

	def oidMap = ["1.2.840.113549.1.9.1":"E"];

        def grailsApplication

	def associateCertificate(RoleDescriptor descriptor, String data, String name, KeyTypes type) {
		def cert = createCertificate(data)
		if(cert) {
			def keyInfo = new KeyInfo(certificate:cert, keyName:name)
			def keyDescriptor = new KeyDescriptor(keyInfo:keyInfo, keyType:type, encryptionMethod:null)
      keyDescriptor.roleDescriptor = descriptor

			descriptor.addToKeyDescriptors(keyDescriptor)
			descriptor.save()
			keyDescriptor.save()

			if(descriptor.hasErrors() || keyDescriptor.hasErrors()) {
				descriptor.errors?.each {
					log.warn it
				}
				keyDescriptor.errors?.each {
					log.warn it
				}
				throw new ErronousStateException("Unable to associate ${keyDescriptor} with ${descriptor}")
			}
			return true
		}
		false
	}

	def unassociateCertificate(KeyDescriptor key) {
		key.disabled = true
		key.save()
		if(key.hasErrors()) {
			key.errors.each {
				log.warn it
			}
			throw new ErronousStateException("Unable to unassociate ${key}")
		}
		true
	}

	def aaf.fr.foundation.Certificate createCertificate(String _data) {
		def data = _data?.trim()
		if(!data) {
			log.error 'Invalid certificate data, no certificate provided'
			println "no data"
			return null
		}

		if(!(data.startsWith('-----BEGIN CERTIFICATE-----')) || !(data.endsWith('-----END CERTIFICATE-----'))){
			println "no banners"
		  log.error 'Invalid certificate data, no banners present'
		  return null
		}

		if(data.count('-----BEGIN CERTIFICATE-----') > 1 || data.count('-----END CERTIFICATE-----') > 1 ){
			println "multiple banners"
		  log.error 'Invalid certificate data multiple certificates provided'
		  return null
		}

		def cert = new aaf.fr.foundation.Certificate(data: data?.normalize())
		cert.expiryDate = expiryDate(cert)
		cert.issuer = issuer(cert)
		cert.subject = subject(cert)
		return cert
	}

	def Date expiryDate(aaf.fr.foundation.Certificate certificate) {
		try {
			CertificateFactory cf = CertificateFactory.getInstance("X.509")
			def c = cf.generateCertificate(new ByteArrayInputStream(certificate.data.getBytes("ASCII")))
			c.getNotAfter()
		}
		catch (Exception e) {
			log.error e.getLocalizedMessage()
			log.debug e
		}
	}

	def String issuer(aaf.fr.foundation.Certificate certificate) {
		CertificateFactory cf = CertificateFactory.getInstance("X.509")
		def c = cf.generateCertificate(new ByteArrayInputStream(certificate.data.getBytes("ASCII")))
		c.issuerX500Principal.getName("RFC2253", oidMap)
	}

	def String subject(aaf.fr.foundation.Certificate certificate) {
		CertificateFactory cf = CertificateFactory.getInstance("X.509")
		def c = cf.generateCertificate(new ByteArrayInputStream(certificate.data.getBytes("ASCII")))
		c.subjectX500Principal.getName("RFC2253", oidMap)
	}
}
