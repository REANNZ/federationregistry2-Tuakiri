import fedreg.core.*

/**
Allows FR administrators to import certificates that are otherwise rejected via the GUI due to subject naming incompatabilities that are
the expected norm`.

Vladimir Mencl
31/05/2011
*/

def commit = false
def spID = "<fill in ID here>"
def data = """-----BEGIN CERTIFICATE-----
<PASTE CERTIFICATE HERE (REMOVING THIS TEXT)>
-----END CERTIFICATE-----"""

// ---------------------------
// NO CHANGES BELOW THIS POINT
// ---------------------------

if(!commit)
   println "SCRIPT RUNNING IN NON COMMIT MODE, UPDATES NOT PERMANENT\n"

def cryptoService = ctx.getBean("cryptoService")

def cert = cryptoService.createCertificate(data)
def keyInfo = new KeyInfo(certificate:cert)

println "new cert = " + cert
println "new keyinfo  = " + keyInfo

spDesc = SPSSODescriptor.get(spID)

println spDesc
   
if (commit) {
   cryptoService.associateCertificate(spDesc, data, null, KeyTypes.signing)
   cryptoService.associateCertificate(spDesc, data, null, KeyTypes.encryption)
 }

println "Certificates now associated with spDesc " + spDesc
spDesc.keyDescriptors.each {
  println it.toString() + " disabled="+it.disabled
  println "KeyInfo=" + it.keyInfo
  println "Certificate=" + it.keyInfo.certificate
}

