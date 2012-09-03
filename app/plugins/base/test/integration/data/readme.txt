This demo CA and signed ceritifcate was created using OpenSSL as follows:

Create a CA hierarchy:

 CA.pl -newca
Complete certificate creation example: create a CA, create a request, sign the request and finally create a PKCS#12 file containing it.

 CA.pl -newca
 CA.pl -newreq
 CA.pl -signreq
 CA.pl -pkcs12 "My Test Certificate"

All passphrases are 'secure'.
