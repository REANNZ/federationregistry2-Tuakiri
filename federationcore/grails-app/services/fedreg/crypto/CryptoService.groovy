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
			def c = cf.generateCertificate(new ByteArrayInputStream(cacert.data.bytes))
			def ta = new TrustAnchor(c, null)
			trustAnchors.add(ta)
		}
		PKIXParameters p = new PKIXParameters(trustAnchors)
		p.setRevocationEnabled(false);
		
		def certList = [cf.generateCertificate(new ByteArrayInputStream(certificate.data.bytes))] as List
		
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
		CertificateFactory cf = CertificateFactory.getInstance("X.509")
		def c = cf.generateCertificate(new ByteArrayInputStream(certificate.data.bytes))
		c.getNotAfter()
	}	

}