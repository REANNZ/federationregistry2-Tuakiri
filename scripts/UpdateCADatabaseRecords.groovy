import aaf.fr.foundation.*
import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.security.*
import java.security.cert.*

Security.addProvider(new BouncyCastleProvider());
CertificateFactory cf = CertificateFactory.getInstance("X.509", "BC")

/* handle OIDs used in subject DNs by some CAs
 * - these OIDs are not decoded by getSubjectX500Principal().getName()
 * (even though they are coded by c.getSubjectDN()
 * - so instead use 
 *   getSubjectX500Principal().getName("RFC2253", oidMap)
 */
def oidMap = new HashMap();
oidMap.put("1.2.840.113549.1.9.1","E");

CACertificate.getAll().each {
  def c = cf.generateCertificate(new ByteArrayInputStream(it.data.getBytes("ASCII")))

  keyname = c.getSubjectX500Principal().getName("RFC2253", oidMap)
  if(keyname.length() > 254)
    keyname = keyname.substring(0,254)

  /* INTERNAL DETAILS
  println "Issuer: " + c.getIssuerDN()
  println "Subject: " + c.getSubjectDN()
  println "Validity: from " + c.getNotBefore() + " to " + c.getNotAfter()
  println "KeyName: " + keyname
  */

  it.caKeyInfo.keyName = keyname
  it.caKeyInfo.expiryDate = c.getNotAfter()

  it.save()
}

true
