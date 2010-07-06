package fedreg.crypto

import java.security.*
import java.security.cert.*

import fedreg.core.*

class CryptoService {
	
	def fedreg.core.Certificate createCertificate(String data) {
		def cert = new fedreg.core.Certificate(data: data)	
		if(validateCertificate(cert)) {
			cert.expiryDate = expiryDate(cert)
			cert.issuer = issuer(cert)
			cert.subject = subject(cert)
		}
		return cert
	}

	def boolean validateCertificate(fedreg.core.Certificate certificate) {
		CertificateFactory cf = CertificateFactory.getInstance("X.509")
		CertPathValidator cpv = CertPathValidator.getInstance("PKIX")
		
		def trustAnchors = [] as Set
		
		try{
			CACertificate.list().each { cacert ->
				def c = cf.generateCertificate(new ByteArrayInputStream(cacert.data.getBytes("ASCII")))
				def ta = new TrustAnchor(c, null)
				trustAnchors.add(ta)
			}
			PKIXParameters p = new PKIXParameters(trustAnchors)
			p.setRevocationEnabled(false);
		
			def certList = [cf.generateCertificate(new ByteArrayInputStream(certificate.data.getBytes("ASCII")))] as List
		
			cpv.validate(cf.generateCertPath(certList), p)
			return true
		}
		catch(Exception e) {
			log.warn "Unable to validate certificate against current trust anchors"
			log.debug e.getLocalizedMessage()
			certificate.errors.rejectValue("data", "fedreg.core.certificate.data.invalid")
			return false
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