import fedreg.core.*

/**
Allows FR administrators to import third party CA for validating signed certificates supplied
by IdP or SP (it is recommended they use self signed but this is not always practical).

Be sure to import all certificates in the chain, intermediatries can be particuarly tricky.
Until the chain is complete FR will continue to fail in verifying certificates

Bradley Beddoes
25/03/2011
*/

def commit = true

def data = """-----BEGIN CERTIFICATE-----
<PASTE CERTIFICATE HERE (REMOVING THIS TEXT)>
-----END CERTIFICATE-----"""

// ---------------------------
// NO CHANGES BELOW THIS POINT
// ---------------------------

if(!commit)
   println "SCRIPT RUNNING IN NON COMMIT MODE, UPDATES NOT PERMANENT\n"

def caCert = new CACertificate(data:data)
def caKeyInfo = new CAKeyInfo(certificate:caCert)

if(commit) {
  caKeyInfo.save()
  if(caKeyInfo.hasErrors()) {
    println "Error importing CA"
    caKeyInfo.errors.each {println it}
  } 
  else 
      println "CA imported successfully"
}

true