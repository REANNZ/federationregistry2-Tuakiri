package fedreg.crypto

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import java.security.*
import java.security.cert.*

import fedreg.core.*

/**
 * Provides methods for managing Crypto/Certificates.
 *
 * @author Bradley Beddoes
 */
class CryptoService {
	
	def associateCertificate(RoleDescriptor descriptor, String data, String name, KeyTypes type) {
		def cert = createCertificate(data)	
		if(validateCertificate(cert, false)) {		
			def keyInfo = new KeyInfo(certificate:cert, keyName:name)
			def keyDescriptor = new KeyDescriptor(keyInfo:keyInfo, keyType:type, roleDescriptor:descriptor)
		
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
	
	def fedreg.core.Certificate createCertificate(String data) {
		def cert = new fedreg.core.Certificate(data: data.trim().normalize())	
		cert.expiryDate = expiryDate(cert)
		cert.issuer = issuer(cert)
		cert.subject = subject(cert)
		return cert
	}

	def boolean validateCertificate(fedreg.core.Certificate certificate) {
		validateCertificate(certificate, false)
	}
	
	def boolean validateCertificate(fedreg.core.Certificate certificate, boolean requireChain) {
		log.debug "Validating certificate ${certificate.subject} with issuer ${certificate.issuer}"	
		if(!requireChain && certificate.subject == certificate.issuer) {
			log.debug "requireChain is false and cert is self signed, valid."
			return true
		}
		
		Security.addProvider(new BouncyCastleProvider());	
		CertificateFactory cf = CertificateFactory.getInstance("X.509", "BC")
		CertPathValidator cpv = CertPathValidator.getInstance("PKIX", "BC")
	
		def trustAnchors = [] as Set
	
		try{
			CACertificate.list().each { cacert ->
				def c = cf.generateCertificate(new ByteArrayInputStream(cacert.data.getBytes("ASCII")))
				def ta = new TrustAnchor(c, null)
				trustAnchors.add(ta)
			}
			PKIXParameters p = new PKIXParameters(trustAnchors)
			p.setSigProvider("BC")
			p.setRevocationEnabled(false);
	
			def certList = [cf.generateCertificate(new ByteArrayInputStream(certificate.data.getBytes("ASCII")))] as List
	
			cpv.validate(cf.generateCertPath(certList), p)
			true
		}
		catch(Exception e) {
			log.warn "Unable to validate certificate against current trust anchors"
			log.warn "Localized Message: " + e.getLocalizedMessage()
			log.debug "Exception was: ", e
			certificate.errors.rejectValue("data", "fedreg.core.certificate.data.invalid")
		 	false
		}
	}
	
	def Date expiryDate(fedreg.core.Certificate certificate) {
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
	
	def String issuer(fedreg.core.Certificate certificate) {
		CertificateFactory cf = CertificateFactory.getInstance("X.509")
		def c = cf.generateCertificate(new ByteArrayInputStream(certificate.data.getBytes("ASCII")))
		c.issuerX500Principal.name
	}

	def String subject(fedreg.core.Certificate certificate) {
		CertificateFactory cf = CertificateFactory.getInstance("X.509")
		def c = cf.generateCertificate(new ByteArrayInputStream(certificate.data.getBytes("ASCII")))
		c.subjectX500Principal.name
	}
}