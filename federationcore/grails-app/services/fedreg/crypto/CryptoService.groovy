package fedreg.crypto

import java.security.*
import java.security.cert.*

import fedreg.core.*

class CryptoService {

	def boolean validateCertificate(fedreg.core.Certificate certificate) {
		CertificateFactory cf = CertificateFactory.getInstance("X.509")
		CertPathValidator cpv = CertPathValidator.getInstance("PKIX")
		
		def trustAnchors = [] as Set
		CACertificate.list().each { cacert ->
			def c = cf.generateCertificate(new ByteArrayInputStream(cacert.data.getBytes("UTF8")))
			def ta = new TrustAnchor(c, null)
			trustAnchors.add(ta)
		}
		PKIXParameters p = new PKIXParameters(trustAnchors)
		p.setRevocationEnabled(false);
		
		def certList = [cf.generateCertificate(new ByteArrayInputStream(certificate.data.getBytes("UTF8")))] as List
		
		try{
			cpv.validate(cf.generateCertPath(certList), p)
			return true
		}
		catch(Exception e) {
			log.warn "Unable to validate certificate against current trust anchors"
			log.debug e.getLocalizedMessage()
			return false
		}
	}
	
	def Date expiryDate(fedreg.core.Certificate certificate) {
		try {
			CertificateFactory cf = CertificateFactory.getInstance("X.509")
			def c = cf.generateCertificate(new ByteArrayInputStream(certificate.data.getBytes("UTF8")))
			c.getNotAfter()
		}
		catch (Exception e) {
			log.error e.getLocalizedMessage()
			log.debug e
		}
	}
	
	def String issuer(fedreg.core.Certificate certificate) {
		CertificateFactory cf = CertificateFactory.getInstance("X.509")
		def c = cf.generateCertificate(new ByteArrayInputStream(certificate.data.getBytes("UTF8")))  // Wrap in string here to ensure B64 due to TEXT type weirdness
		c.issuerX500Principal.name
	}

	def String subject(fedreg.core.Certificate certificate) {
		CertificateFactory cf = CertificateFactory.getInstance("X.509")
		def c = cf.generateCertificate(new ByteArrayInputStream(certificate.data.getBytes("UTF8")))  // Wrap in string here to ensure B64 due to TEXT type weirdness
		c.subjectX500Principal.name
	}
}