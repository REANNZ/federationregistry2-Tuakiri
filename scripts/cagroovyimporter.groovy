import fedreg.core.* 


            def data0 = """-----BEGIN CERTIFICATE-----
 
 
MIICtzCCAiACAQAwDQYJKoZIhvcNAQEEBQAwgaMxCzAJBgNVBAYTAkVTMRIw
EAYDVQQIEwlCQVJDRUxPTkExEjAQBgNVBAcTCUJBUkNFTE9OQTEZMBcGA1UE
ChMQSVBTIFNlZ3VyaWRhZCBDQTEYMBYGA1UECxMPQ2VydGlmaWNhY2lvbmVz
MRcwFQYDVQQDEw5JUFMgU0VSVklET1JFUzEeMBwGCSqGSIb3DQEJARYPaXBz
QG1haWwuaXBzLmVzMB4XDTk4MDEwMTIzMjEwN1oXDTA5MTIyOTIzMjEwN1ow
gaMxCzAJBgNVBAYTAkVTMRIwEAYDVQQIEwlCQVJDRUxPTkExEjAQBgNVBAcT
CUJBUkNFTE9OQTEZMBcGA1UEChMQSVBTIFNlZ3VyaWRhZCBDQTEYMBYGA1UE
CxMPQ2VydGlmaWNhY2lvbmVzMRcwFQYDVQQDEw5JUFMgU0VSVklET1JFUzEe
MBwGCSqGSIb3DQEJARYPaXBzQG1haWwuaXBzLmVzMIGfMA0GCSqGSIb3DQEB
AQUAA4GNADCBiQKBgQCsT1J0nznqjtwlxLyYXZhkJAk8IbPMGbWOlI6H0fg3
PqHILVikgDVboXVsHUUMH2Fjal5vmwpMwci4YSM1gf/+rHhwLWjhOgeYlQJU
3c0jt4BT18g3RXIGJBK6E2Ehim51KODFDzT9NthFf+G4Nu+z4cYgjui0OLzh
PvYR3oydAQIDAQABMA0GCSqGSIb3DQEBBAUAA4GBACzzw3lYJN7GO9HgQmm4
7mSzPWIBubOE3yN93ZjPEKn+ANgilgUTB1RXxafey9m4iEL2mdsUdx+2/iU9
4aI+A6mB0i1sR/WWRowiq8jMDQ6XXotBtDvECgZAHd1G9AHduoIuPD14cJ58
GNCr+Lh3B0Zx8coLY1xq+XKU1QFPoNtC
 
                                
-----END CERTIFICATE-----"""
            if(!CACertificate.findWhere(data:data0)) {
                def caCert0 = new CACertificate(data:data0)
                def caKeyInfo0 = new CAKeyInfo(certificate:caCert0)
                caKeyInfo0.save()
                if(caKeyInfo0.hasErrors()) {
                    println "Error importing CA 0"
                    caKeyInfo0.errors.each {println it}
                } else { println "CA 0 imported" }
            }
            else { println "CA 0 existed from seperate source" }
    
            def data1 = """-----BEGIN CERTIFICATE-----
 
MIIF5jCCBU+gAwIBAgIDAJAYMA0GCSqGSIb3DQEBBQUAMIGjMQswCQYDVQQGEwJF
UzESMBAGA1UECBMJQkFSQ0VMT05BMRIwEAYDVQQHEwlCQVJDRUxPTkExGTAXBgNV
BAoTEElQUyBTZWd1cmlkYWQgQ0ExGDAWBgNVBAsTD0NlcnRpZmljYWNpb25lczEX
MBUGA1UEAxMOSVBTIFNFUlZJRE9SRVMxHjAcBgkqhkiG9w0BCQEWD2lwc0BtYWls
Lmlwcy5lczAeFw0wMTEyMzAxMzM2MTFaFw0yNTEyMjkxMzM2MTFaMIIBEjELMAkG
A1UEBhMCRVMxEjAQBgNVBAgTCUJhcmNlbG9uYTESMBAGA1UEBxMJQmFyY2Vsb25h
MSkwJwYDVQQKEyBJUFMgQ2VydGlmaWNhdGlvbiBBdXRob3JpdHkgcy5sLjEuMCwG
A1UEChQlZ2VuZXJhbEBpcHNjYS5jb20gQy5JLkYuICBCLUI2MjIxMDY5NTEuMCwG
A1UECxMlaXBzQ0EgQ0xBU0VBMSBDZXJ0aWZpY2F0aW9uIEF1dGhvcml0eTEuMCwG
A1UEAxMlaXBzQ0EgQ0xBU0VBMSBDZXJ0aWZpY2F0aW9uIEF1dGhvcml0eTEgMB4G
CSqGSIb3DQEJARYRZ2VuZXJhbEBpcHNjYS5jb20wgZ8wDQYJKoZIhvcNAQEBBQAD
gY0AMIGJAoGBAKb1c2Y2HaMvT60q2O8Mpkvvpxus9/JGFxuyAqs+EYmMaqgP2GMU
mdcfvLInaAJu9DCJ663rQdy0Qgb6SB8TjGTfhy3HFNSng+RyOzLq0015MWUFCTOB
K27mNq0hETM2K2jKvkMsN7c9aRY75Z2+MqfV30qA/NpzcKrZKIIvaLuxAgMBAAGj
ggK0MIICsDAMBgNVHRMEBTADAQH/MBEGCWCGSAGG+EIBAQQEAwIABzAMBgNVHQ8E
BQMDB/+AMGsGA1UdJQRkMGIGCCsGAQUFBwMBBggrBgEFBQcDAgYIKwYBBQUHAwMG
CCsGAQUFBwMEBggrBgEFBQcDCAYKKwYBBAGCNwIBFQYKKwYBBAGCNwIBFgYKKwYB
BAGCNwoDAQYKKwYBBAGCNwoDBDAdBgNVHQ4EFgQUDgdg1DnJG1tdkHsjyNI0nUqa
RjkwgboGA1UdIwSBsjCBr6GBqaSBpjCBozELMAkGA1UEBhMCRVMxEjAQBgNVBAgT
CUJBUkNFTE9OQTESMBAGA1UEBxMJQkFSQ0VMT05BMRkwFwYDVQQKExBJUFMgU2Vn
dXJpZGFkIENBMRgwFgYDVQQLEw9DZXJ0aWZpY2FjaW9uZXMxFzAVBgNVBAMTDklQ
UyBTRVJWSURPUkVTMR4wHAYJKoZIhvcNAQkBFg9pcHNAbWFpbC5pcHMuZXOCAQAw
HAYDVR0RBBUwE4ERZ2VuZXJhbEBpcHNjYS5jb20wCQYDVR0SBAIwADBDBglghkgB
hvhCAQ0ENhY0Q0xBU0VBMSBDQSBDZXJ0aWZpY2F0ZSBpc3N1ZWQgYnkgaHR0cHM6
Ly93d3cuaXBzLmVzLzAiBglghkgBhvhCAQIEFRYTaHR0cHM6Ly93d3cuaXBzLmVz
LzBzBgNVHR8EbDBqMDGgL6AthitodHRwczovL3d3dy5pcHMuZXMvY3JsL2lwc1NF
UlZJRE9SRVNjcmwuY3JsMDWgM6Axhi9odHRwczovL3d3d2JhY2suaXBzLmVzL2Ny
bC9pcHNTRVJWSURPUkVTY3JsLmNybDAvBggrBgEFBQcBAQQjMCEwHwYIKwYBBQUH
MAGGE2h0dHA6Ly9vY3NwLmlwcy5FUy8wDQYJKoZIhvcNAQEFBQADgYEAJwVKDHTJ
FF3Idcje7okMYx4fAYTHAssZ15HI3T7PsMjz9ZK1VIJUYh8y64ChfVbx5OcoWSak
ffDKLTHGyfNwncHPXiak77IS54joGYXtgOQ5AmiHY6kTPGz1RWoizpZWB1+mo+43
Cm6Si55A3hQwGSiTkLfz/LVO8veWDrsQlNQ=
                                
-----END CERTIFICATE-----"""
            if(!CACertificate.findWhere(data:data0)) {
                def caCert1 = new CACertificate(data:data1)
                def caKeyInfo1 = new CAKeyInfo(certificate:caCert1)
                caKeyInfo1.save()
                if(caKeyInfo1.hasErrors()) {
                    println "Error importing CA 1"
                    caKeyInfo1.errors.each {println it}
                } else { println "CA 1 imported" }
            }
            else { println "CA 1 existed from seperate source" }
    
            def data2 = """-----BEGIN CERTIFICATE-----
 
MIIDEzCCAnygAwIBAgIBATANBgkqhkiG9w0BAQQFADCBxDELMAkGA1UEBhMCWkExFTATBgNVBAgT
DFdlc3Rlcm4gQ2FwZTESMBAGA1UEBxMJQ2FwZSBUb3duMR0wGwYDVQQKExRUaGF3dGUgQ29uc3Vs
dGluZyBjYzEoMCYGA1UECxMfQ2VydGlmaWNhdGlvbiBTZXJ2aWNlcyBEaXZpc2lvbjEZMBcGA1UE
AxMQVGhhd3RlIFNlcnZlciBDQTEmMCQGCSqGSIb3DQEJARYXc2VydmVyLWNlcnRzQHRoYXd0ZS5j
b20wHhcNOTYwODAxMDAwMDAwWhcNMjAxMjMxMjM1OTU5WjCBxDELMAkGA1UEBhMCWkExFTATBgNV
BAgTDFdlc3Rlcm4gQ2FwZTESMBAGA1UEBxMJQ2FwZSBUb3duMR0wGwYDVQQKExRUaGF3dGUgQ29u
c3VsdGluZyBjYzEoMCYGA1UECxMfQ2VydGlmaWNhdGlvbiBTZXJ2aWNlcyBEaXZpc2lvbjEZMBcG
A1UEAxMQVGhhd3RlIFNlcnZlciBDQTEmMCQGCSqGSIb3DQEJARYXc2VydmVyLWNlcnRzQHRoYXd0
ZS5jb20wgZ8wDQYJKoZIhvcNAQEBBQADgY0AMIGJAoGBANOkUG7I/1Zr5s9dtuoMaHVHoqrC2oQl
/Kj0R1HahbUgdJSGHg91yekIYfUGbTBuFRkC6VLAYttNmZ7iagxEOM3+vuNkCXDF/rFrKbYvScg7
1CcEJRCXL+eQbcAoQpnXTEPew/UhbVSfXcNY4cDk2VuwuNy0e982OsK1ZiIS1ocNAgMBAAGjEzAR
MA8GA1UdEwEB/wQFMAMBAf8wDQYJKoZIhvcNAQEEBQADgYEAB/pMaVz7lcxG7oWDTSEwjsrZqG9J
GubaUeNgcGyEYRGhGshIPllDfU+VPaGLtwtimHp1it2ITk6eQNuozDJ0uW8NxuOzRAvZim+aKZuZ
GCg70eNAKJpaPNW15yAbi8qkq43pUdniTCxZqdq5snUb9kLy78fyGPmJvKP/iiMucEc=
                                
-----END CERTIFICATE-----"""
            if(!CACertificate.findWhere(data:data0)) {
                def caCert2 = new CACertificate(data:data2)
                def caKeyInfo2 = new CAKeyInfo(certificate:caCert2)
                caKeyInfo2.save()
                if(caKeyInfo2.hasErrors()) {
                    println "Error importing CA 2"
                    caKeyInfo2.errors.each {println it}
                } else { println "CA 2 imported" }
            }
            else { println "CA 2 existed from seperate source" }
    
            def data3 = """-----BEGIN CERTIFICATE-----
 
MIICPDCCAaUCEHC65B0Q2Sk0tjjKewPMur8wDQYJKoZIhvcNAQECBQAwXzELMAkG
A1UEBhMCVVMxFzAVBgNVBAoTDlZlcmlTaWduLCBJbmMuMTcwNQYDVQQLEy5DbGFz
cyAzIFB1YmxpYyBQcmltYXJ5IENlcnRpZmljYXRpb24gQXV0aG9yaXR5MB4XDTk2
MDEyOTAwMDAwMFoXDTI4MDgwMTIzNTk1OVowXzELMAkGA1UEBhMCVVMxFzAVBgNV
BAoTDlZlcmlTaWduLCBJbmMuMTcwNQYDVQQLEy5DbGFzcyAzIFB1YmxpYyBQcmlt
YXJ5IENlcnRpZmljYXRpb24gQXV0aG9yaXR5MIGfMA0GCSqGSIb3DQEBAQUAA4GN
ADCBiQKBgQDJXFme8huKARS0EN8EQNvjV69qRUCPhAwL0TPZ2RHP7gJYHyX3KqhE
BarsAx94f56TuZoAqiN91qyFomNFx3InzPRMxnVx0jnvT0Lwdd8KkMaOIG+YD/is
I19wKTakyYbnsZogy1Olhec9vn2a/iRFM9x2Fe0PonFkTGUugWhFpwIDAQABMA0G
CSqGSIb3DQEBAgUAA4GBALtMEivPLCYATxQT3ab7/AoRhIzzKBxnki98tsX63/Do
lbwdj2wsqFHMc9ikwFPwTtYmwHYBV4GSXiHx0bH/59AhWM1pF+NEHJwZRDmJXNyc
AA9WjQKZ7aKQRUzkuxCkPfAyAw7xzvjoyVGM5mKf5p/AfbdynMk2OmufTqj/ZA1k
                    
-----END CERTIFICATE-----"""
            if(!CACertificate.findWhere(data:data0)) {
                def caCert3 = new CACertificate(data:data3)
                def caKeyInfo3 = new CAKeyInfo(certificate:caCert3)
                caKeyInfo3.save()
                if(caKeyInfo3.hasErrors()) {
                    println "Error importing CA 3"
                    caKeyInfo3.errors.each {println it}
                } else { println "CA 3 imported" }
            }
            else { println "CA 3 existed from seperate source" }
    
            def data4 = """-----BEGIN CERTIFICATE-----
 
MIIEnDCCBAWgAwIBAgIQdTN9mrDhIzuuLX3kRpFi1DANBgkqhkiG9w0BAQUFADBf
MQswCQYDVQQGEwJVUzEXMBUGA1UEChMOVmVyaVNpZ24sIEluYy4xNzA1BgNVBAsT
LkNsYXNzIDMgUHVibGljIFByaW1hcnkgQ2VydGlmaWNhdGlvbiBBdXRob3JpdHkw
HhcNMDUwMTE5MDAwMDAwWhcNMTUwMTE4MjM1OTU5WjCBsDELMAkGA1UEBhMCVVMx
FzAVBgNVBAoTDlZlcmlTaWduLCBJbmMuMR8wHQYDVQQLExZWZXJpU2lnbiBUcnVz
dCBOZXR3b3JrMTswOQYDVQQLEzJUZXJtcyBvZiB1c2UgYXQgaHR0cHM6Ly93d3cu
dmVyaXNpZ24uY29tL3JwYSAoYykwNTEqMCgGA1UEAxMhVmVyaVNpZ24gQ2xhc3Mg
MyBTZWN1cmUgU2VydmVyIENBMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKC
AQEAlcMhEo5AxQ0BX3ZeZpTZcyxYGSK4yfx6OZAqd3J8HT732FXjr0LLhzAC3Fus
cOa4RLQrNeuT0hcFfstG1lxToDJRnXRkWPkMmgDqXkRJZHL0zRDihQr5NO6ziGap
paRa0A6Yf1gNK1K7hql+LvqySHyN2y1fAXWijQY7i7RhB8m+Ipn4G9G1V2YETTX0
kXGWtZkIJZuXyDrzILHdnpgMSmO3ps6wAc74k2rzDG6fsemEe4GYQeaB3D0s57Rr
4578CBbXs9W5ZhKZfG1xyE2+xw/j+zet1XWHIWuG0EQUWlR5OZZpVsm5Mc2JYVjh
2XYFBa33uQKvp/1HkaIiNFox0QIDAQABo4IBgTCCAX0wEgYDVR0TAQH/BAgwBgEB
/wIBADBEBgNVHSAEPTA7MDkGC2CGSAGG+EUBBxcDMCowKAYIKwYBBQUHAgEWHGh0
dHBzOi8vd3d3LnZlcmlzaWduLmNvbS9ycGEwMQYDVR0fBCowKDAmoCSgIoYgaHR0
cDovL2NybC52ZXJpc2lnbi5jb20vcGNhMy5jcmwwDgYDVR0PAQH/BAQDAgEGMBEG
CWCGSAGG+EIBAQQEAwIBBjApBgNVHREEIjAgpB4wHDEaMBgGA1UEAxMRQ2xhc3Mz
Q0EyMDQ4LTEtNDUwHQYDVR0OBBYEFG/sr6DdiqTv9SoQZy0/VYK81+8lMIGABgNV
HSMEeTB3oWOkYTBfMQswCQYDVQQGEwJVUzEXMBUGA1UEChMOVmVyaVNpZ24sIElu
Yy4xNzA1BgNVBAsTLkNsYXNzIDMgUHVibGljIFByaW1hcnkgQ2VydGlmaWNhdGlv
biBBdXRob3JpdHmCEHC65B0Q2Sk0tjjKewPMur8wDQYJKoZIhvcNAQEFBQADgYEA
w34IRl2RNs9n3Nenr6+4IsOLBHTTsWC85v63RBKBWzFzFGNWxnIu0RoDQ1w4ClBK
Tc3athmo9JkNr+P32PF1KGX2av6b9L1S2T/L2hbLpZ4ujmZSeD0m+v6UNohKlV4q
TBnvbvqCPy0D79YoszcYz0KyNCFkR9MgazpM3OYDkAw=
                    
-----END CERTIFICATE-----"""
            if(!CACertificate.findWhere(data:data0)) {
                def caCert4 = new CACertificate(data:data4)
                def caKeyInfo4 = new CAKeyInfo(certificate:caCert4)
                caKeyInfo4.save()
                if(caKeyInfo4.hasErrors()) {
                    println "Error importing CA 4"
                    caKeyInfo4.errors.each {println it}
                } else { println "CA 4 imported" }
            }
            else { println "CA 4 existed from seperate source" }
    
            def data5 = """-----BEGIN CERTIFICATE-----
 
MIIGLDCCBZWgAwIBAgIQbk/6s8XmacTRZ8mSq+hYxDANBgkqhkiG9w0BAQUFADCB
wTELMAkGA1UEBhMCVVMxFzAVBgNVBAoTDlZlcmlTaWduLCBJbmMuMTwwOgYDVQQL
EzNDbGFzcyAzIFB1YmxpYyBQcmltYXJ5IENlcnRpZmljYXRpb24gQXV0aG9yaXR5
IC0gRzIxOjA4BgNVBAsTMShjKSAxOTk4IFZlcmlTaWduLCBJbmMuIC0gRm9yIGF1
dGhvcml6ZWQgdXNlIG9ubHkxHzAdBgNVBAsTFlZlcmlTaWduIFRydXN0IE5ldHdv
cmswHhcNMDkwMzI1MDAwMDAwWhcNMTkwMzI0MjM1OTU5WjCBtTELMAkGA1UEBhMC
VVMxFzAVBgNVBAoTDlZlcmlTaWduLCBJbmMuMR8wHQYDVQQLExZWZXJpU2lnbiBU
cnVzdCBOZXR3b3JrMTswOQYDVQQLEzJUZXJtcyBvZiB1c2UgYXQgaHR0cHM6Ly93
d3cudmVyaXNpZ24uY29tL3JwYSAoYykwOTEvMC0GA1UEAxMmVmVyaVNpZ24gQ2xh
c3MgMyBTZWN1cmUgU2VydmVyIENBIC0gRzIwggEiMA0GCSqGSIb3DQEBAQUAA4IB
DwAwggEKAoIBAQDUVo9XOzcopkBj0pXVBXTatRlqltZxVy/iwDSMoJWzjOE3JPMu
7UNFBY6J1/raSrX4Po1Ox/lJUEU3QJ90qqBRVWHxYISJpZ6AjS+wIapFgsTPtBR/
RxUgKIKwaBLArlwH1/ZZzMtiVlxNSf8miKtUUTovStoOmOKJcrn892g8xB85essX
gfMMrQ/cYWIbEAsEHikYcV5iy0PevjG6cQIZTiapUdqMZGkD3pz9ff17Ybz8hHyI
XLTDe+1fK0YS8f0AAZqLW+mjBS6PLlve8xt4+GaRCMBeztWwNsrUqHugffkwer/4
3RlRKyC6/qfPoU6wZ/WAqiuDLtKOVImOHikLAgMBAAGjggKpMIICpTA0BggrBgEF
BQcBAQQoMCYwJAYIKwYBBQUHMAGGGGh0dHA6Ly9vY3NwLnZlcmlzaWduLmNvbTAS
BgNVHRMBAf8ECDAGAQH/AgEAMHAGA1UdIARpMGcwZQYLYIZIAYb4RQEHFwMwVjAo
BggrBgEFBQcCARYcaHR0cHM6Ly93d3cudmVyaXNpZ24uY29tL2NwczAqBggrBgEF
BQcCAjAeGhxodHRwczovL3d3dy52ZXJpc2lnbi5jb20vcnBhMDQGA1UdHwQtMCsw
KaAnoCWGI2h0dHA6Ly9jcmwudmVyaXNpZ24uY29tL3BjYTMtZzIuY3JsMA4GA1Ud
DwEB/wQEAwIBBjBtBggrBgEFBQcBDARhMF+hXaBbMFkwVzBVFglpbWFnZS9naWYw
ITAfMAcGBSsOAwIaBBSP5dMahqyNjmvDz4Bq1EgYLHsZLjAlFiNodHRwOi8vbG9n
by52ZXJpc2lnbi5jb20vdnNsb2dvLmdpZjApBgNVHREEIjAgpB4wHDEaMBgGA1UE
AxMRQ2xhc3MzQ0EyMDQ4LTEtNTIwHQYDVR0OBBYEFKXvCxHOwEEDo0plkEiyHOBX
LX1HMIHnBgNVHSMEgd8wgdyhgcekgcQwgcExCzAJBgNVBAYTAlVTMRcwFQYDVQQK
Ew5WZXJpU2lnbiwgSW5jLjE8MDoGA1UECxMzQ2xhc3MgMyBQdWJsaWMgUHJpbWFy
eSBDZXJ0aWZpY2F0aW9uIEF1dGhvcml0eSAtIEcyMTowOAYDVQQLEzEoYykgMTk5
OCBWZXJpU2lnbiwgSW5jLiAtIEZvciBhdXRob3JpemVkIHVzZSBvbmx5MR8wHQYD
VQQLExZWZXJpU2lnbiBUcnVzdCBOZXR3b3JrghB92f4Hz6getxB5Z/uniTTGMA0G
CSqGSIb3DQEBBQUAA4GBAGN0Lz1Tqi+X7CYRZhr+8d5BJxnSf9jBHPniOFY6H5Cu
OcUgdav4bC1nHynCIdcUiGNLsJsnY5H48KMBJLb7j+M9AgtvVP7UzNvWhb98lR5e
YhHB2QmcQrmy1KotmDojYMyimvFu6M+O0Ro8XhnF15s1sAIjJOUFuNWI4+D6ufRf
                    
-----END CERTIFICATE-----"""
            if(!CACertificate.findWhere(data:data0)) {
                def caCert5 = new CACertificate(data:data5)
                def caKeyInfo5 = new CAKeyInfo(certificate:caCert5)
                caKeyInfo5.save()
                if(caKeyInfo5.hasErrors()) {
                    println "Error importing CA 5"
                    caKeyInfo5.errors.each {println it}
                } else { println "CA 5 imported" }
            }
            else { println "CA 5 existed from seperate source" }
    
            def data6 = """-----BEGIN CERTIFICATE-----
 
MIIENjCCAx6gAwIBAgIBATANBgkqhkiG9w0BAQUFADBvMQswCQYDVQQGEwJTRTEU
MBIGA1UEChMLQWRkVHJ1c3QgQUIxJjAkBgNVBAsTHUFkZFRydXN0IEV4dGVybmFs
IFRUUCBOZXR3b3JrMSIwIAYDVQQDExlBZGRUcnVzdCBFeHRlcm5hbCBDQSBSb290
MB4XDTAwMDUzMDEwNDgzOFoXDTIwMDUzMDEwNDgzOFowbzELMAkGA1UEBhMCU0Ux
FDASBgNVBAoTC0FkZFRydXN0IEFCMSYwJAYDVQQLEx1BZGRUcnVzdCBFeHRlcm5h
bCBUVFAgTmV0d29yazEiMCAGA1UEAxMZQWRkVHJ1c3QgRXh0ZXJuYWwgQ0EgUm9v
dDCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBALf3GjPm8gAELTngTlvt
H7xsD821+iO2zt6bETOXpClMfZOfvUq8k+0DGuOPz+VtUFrWlymUWoCwSXrbLpX9
uMq/NzgtHj6RQa1wVsfwTz/oMp50ysiQVOnGXw94nZpAPA6sYapeFI+eh6FqUNzX
mk6vBbOmcZSccbNQYArHE504B4YCqOmoaSYYkKtMsE8jqzpPhNjfzp/haW+710LX
a0Tkx63ubUFfclpxCDezeWWkWaCUN/cALw3CknLa0Dhy2xSoRcRdKn23tNbE7qzN
E0S3ySvdQwAl+mG5aWpYIxG3pzOPVnVZ9c0p10a3CitlttNCbxWyuHv77+ldU9U0
WicCAwEAAaOB3DCB2TAdBgNVHQ4EFgQUrb2YejS0Jvf6xCZU7wO94CTLVBowCwYD
VR0PBAQDAgEGMA8GA1UdEwEB/wQFMAMBAf8wgZkGA1UdIwSBkTCBjoAUrb2YejS0
Jvf6xCZU7wO94CTLVBqhc6RxMG8xCzAJBgNVBAYTAlNFMRQwEgYDVQQKEwtBZGRU
cnVzdCBBQjEmMCQGA1UECxMdQWRkVHJ1c3QgRXh0ZXJuYWwgVFRQIE5ldHdvcmsx
IjAgBgNVBAMTGUFkZFRydXN0IEV4dGVybmFsIENBIFJvb3SCAQEwDQYJKoZIhvcN
AQEFBQADggEBALCb4IUlwtYj4g+WBpKdQZic2YR5gdkeWxQHIzZlj7DYd7usQWxH
YINRsPkyPef89iYTx4AWpb9a/IfPeHmJIZriTAcKhjW88t5RxNKWt9x+Tu5w/Rw5
6wwCURQtjr0W4MHfRnXnJK3s9EK0hZNwEGe6nQY1ShjTK3rMUUKhemPR5ruhxSvC
Nr4TDea9Y355e6cJDUCrat2PisP29owaQgVR1EX1n6diIWgVIEM8med8vSTYqZEX
c4g/VhsxOBi0cQ+azcgOno4uG+GMmIPLHzHxREzGBHNJdmAPx/i9F4BrLunMTA5a
mnkPIAou1Z5jJh5VkpTYghdae9C8x49OhgQ=
                    
-----END CERTIFICATE-----"""
            if(!CACertificate.findWhere(data:data0)) {
                def caCert6 = new CACertificate(data:data6)
                def caKeyInfo6 = new CAKeyInfo(certificate:caCert6)
                caKeyInfo6.save()
                if(caKeyInfo6.hasErrors()) {
                    println "Error importing CA 6"
                    caKeyInfo6.errors.each {println it}
                } else { println "CA 6 imported" }
            }
            else { println "CA 6 existed from seperate source" }
    
            def data7 = """-----BEGIN CERTIFICATE-----
 
MIIEvTCCA6WgAwIBAgIQYfhYp7mBV5M0nX4Zu4oQmTANBgkqhkiG9w0BAQUFADCB
lzELMAkGA1UEBhMCVVMxCzAJBgNVBAgTAlVUMRcwFQYDVQQHEw5TYWx0IExha2Ug
Q2l0eTEeMBwGA1UEChMVVGhlIFVTRVJUUlVTVCBOZXR3b3JrMSEwHwYDVQQLExho
dHRwOi8vd3d3LnVzZXJ0cnVzdC5jb20xHzAdBgNVBAMTFlVUTi1VU0VSRmlyc3Qt
SGFyZHdhcmUwHhcNMDkxMDA4MDAwMDAwWhcNMjAwNTMwMTA0ODM4WjBaMQswCQYD
VQQGEwJBVTEQMA4GA1UEChMHQXVzQ0VSVDEdMBsGA1UECxMUQ2VydGlmaWNhdGUg
U2VydmljZXMxGjAYBgNVBAMTEUF1c0NFUlQgU2VydmVyIENBMIIBIjANBgkqhkiG
9w0BAQEFAAOCAQ8AMIIBCgKCAQEAy4rp0zW1SWexYnKXJxPOx1/9tndNo7EoCrr4
nh3qwV33ONMNQVPxS7m5aJ05VnZM85lB65IZWiKvABEuniTKHkySARGU/p/srdiU
NeOnvjSAwY9gXEx6I2Tzx9KRVL+NjfMEeLzMxQI/gnc3uX0HsaJT1x7pLaj8iFVT
Ot7eC75Cladpu1g5Ela3J/2dJlZQBZVCvfnvbIlVeqy6zMubTe/iSovG5U5FhOHN
MHbdq0zclQQmden/Syr4xAfMww/H1oyJqwzO2t5tBPtIcq2G5VroQehX2hSKgj49
51Ops8AJ8L7j0ah6MraBrf32UPdy+Lxpl3LsKxsTjpAjcJm1AQIDAQABo4IBPzCC
ATswHwYDVR0jBBgwFoAUoXJfJhsomEOVXQc31YWWnUvSw0UwHQYDVR0OBBYEFJRr
I7km6oK0U2CdiV33AsTZKV3pMA4GA1UdDwEB/wQEAwIBBjASBgNVHRMBAf8ECDAG
AQH/AgEAMBkGA1UdIAQSMBAwDgYMKwYBBAGBtz0FAQEAMEQGA1UdHwQ9MDswOaA3
oDWGM2h0dHA6Ly9jcmwudXNlcnRydXN0LmNvbS9VVE4tVVNFUkZpcnN0LUhhcmR3
YXJlLmNybDB0BggrBgEFBQcBAQRoMGYwPQYIKwYBBQUHMAKGMWh0dHA6Ly9jcnQu
dXNlcnRydXN0LmNvbS9VVE5BZGRUcnVzdFNlcnZlcl9DQS5jcnQwJQYIKwYBBQUH
MAGGGWh0dHA6Ly9vY3NwLnVzZXJ0cnVzdC5jb20wDQYJKoZIhvcNAQEFBQADggEB
AF6bZutNoHE6MykkmKdsBLjv0YHwEn1tpp4BPPpxeNJ1t9vO2IKpnEdd2uLt9UWZ
iW2Sg70L9l2JB7z2Nsw1+Wwv60TvLk9BAYa4K18EMb9wWEcgl5rh/w2Ls5wqZA/S
VvOIpAXo2Zfs1/X4fZHqpNmOgMzgK+5/MNRwmveYAGa5WqLCMz8aMWQOyoI54QL9
XZgUA0Q9iBMpadyMvxUGjP1KIvXSLF33HBX1SUCyiE+OvqpH5DJ89JgA4slflE5N
qpuZJUe4mQ+fuYgQOGUhjBpF40GmaWncsQqYWsQgPUA4x5OQ9m+56+W0k23LVmHV
o0Fm8z4QEWtHpAe49hlDn0o=
                    
-----END CERTIFICATE-----"""
            if(!CACertificate.findWhere(data:data0)) {
                def caCert7 = new CACertificate(data:data7)
                def caKeyInfo7 = new CAKeyInfo(certificate:caCert7)
                caKeyInfo7.save()
                if(caKeyInfo7.hasErrors()) {
                    println "Error importing CA 7"
                    caKeyInfo7.errors.each {println it}
                } else { println "CA 7 imported" }
            }
            else { println "CA 7 existed from seperate source" }
    
            def data8 = """-----BEGIN CERTIFICATE-----
 
MIIEPDCCAySgAwIBAgIQSEus8arH1xND0aJ0NUmXJTANBgkqhkiG9w0BAQUFADBv
MQswCQYDVQQGEwJTRTEUMBIGA1UEChMLQWRkVHJ1c3QgQUIxJjAkBgNVBAsTHUFk
ZFRydXN0IEV4dGVybmFsIFRUUCBOZXR3b3JrMSIwIAYDVQQDExlBZGRUcnVzdCBF
eHRlcm5hbCBDQSBSb290MB4XDTA1MDYwNzA4MDkxMFoXDTIwMDUzMDEwNDgzOFow
gZcxCzAJBgNVBAYTAlVTMQswCQYDVQQIEwJVVDEXMBUGA1UEBxMOU2FsdCBMYWtl
IENpdHkxHjAcBgNVBAoTFVRoZSBVU0VSVFJVU1QgTmV0d29yazEhMB8GA1UECxMY
aHR0cDovL3d3dy51c2VydHJ1c3QuY29tMR8wHQYDVQQDExZVVE4tVVNFUkZpcnN0
LUhhcmR3YXJlMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAsffDOD+0
qH/POYJRZ9Btn9L/WPPnnyvsDYlUmbk4mRb34CF5SMK7YXQSlh08anLVPBBnOjnt
KxPNZuuVCTOkbJex6MbswXV5nEZejavQav25KlUXEFSzGfCa9vGxXbanbfvgcRdr
ooj7AN/+GjF3DJoBerEy4ysBBzhuw6VeI7xFm3tQwckwj9vlK3rTW/szQB6g1ZgX
vIuHw4nTXaCOsqqq9o5piAbF+okh8widaS4JM5spDUYPjMxJNLBpUb35Bs1orWZM
vD6sYb0KiA7I3z3ufARMnQpea5HW7sftKI2rTYeJc9BupNAeFosU4XZEA39jrOTN
SZzFkvSrMqFIWwIDAQABo4GqMIGnMB8GA1UdIwQYMBaAFK29mHo0tCb3+sQmVO8D
veAky1QaMB0GA1UdDgQWBBShcl8mGyiYQ5VdBzfVhZadS9LDRTAOBgNVHQ8BAf8E
BAMCAQYwDwYDVR0TAQH/BAUwAwEB/zBEBgNVHR8EPTA7MDmgN6A1hjNodHRwOi8v
Y3JsLnVzZXJ0cnVzdC5jb20vQWRkVHJ1c3RFeHRlcm5hbENBUm9vdC5jcmwwDQYJ
KoZIhvcNAQEFBQADggEBADzse+Cuow6WbTDXhcbSaFtFWoKmNA+wyZIjXhFtCBGy
dAkjOjUlc1heyrl8KPpH7PmgA1hQtlPvjNs55Gfp2MooRtSn4PU4dfjny1y/HRE8
akCbLURW0/f/BSgyDBXIZEWT6CEkjy3aeoR7T8/NsiV8dxDTlNEEkaglHAkiD31E
NREU768A/l7qX46w2ZJZuvwTlqAYAVbO2vYoC7Gv3VxPXLLzj1pxz+0YrWOIHY6V
9+qV5x+tkLiECEeFfyIvGh1IMNZMCNg3GWcyK+tc0LL8blefBDVekAB+EcfeEyrN
pG1FJseIVqDwavfY5/wnfmcI0L36tsNhAgFlubgvz1o=
                    
-----END CERTIFICATE-----"""
            if(!CACertificate.findWhere(data:data0)) {
                def caCert8 = new CACertificate(data:data8)
                def caKeyInfo8 = new CAKeyInfo(certificate:caCert8)
                caKeyInfo8.save()
                if(caKeyInfo8.hasErrors()) {
                    println "Error importing CA 8"
                    caKeyInfo8.errors.each {println it}
                } else { println "CA 8 imported" }
            }
            else { println "CA 8 existed from seperate source" }
    
            def data9 = """-----BEGIN CERTIFICATE-----
 
MIIE6TCCA9GgAwIBAgIQD0X0AesgqBmYLaiwRfo0MjANBgkqhkiG9w0BAQUFADCB
kzELMAkGA1UEBhMCVVMxCzAJBgNVBAgTAlVUMRcwFQYDVQQHEw5TYWx0IExha2Ug
Q2l0eTEeMBwGA1UEChMVVGhlIFVTRVJUUlVTVCBOZXR3b3JrMSEwHwYDVQQLExho
dHRwOi8vd3d3LnVzZXJ0cnVzdC5jb20xGzAZBgNVBAMTElVUTiAtIERBVEFDb3Jw
IFNHQzAeFw0wOTEwMDgwMDAwMDBaFw0yMDA1MzAxMDQ4MzhaMF4xCzAJBgNVBAYT
AkFVMRAwDgYDVQQKEwdBdXNDRVJUMR0wGwYDVQQLExRDZXJ0aWZpY2F0ZSBTZXJ2
aWNlczEeMBwGA1UEAxMVQXVzQ0VSVCBTR0MgU2VydmVyIENBMIIBIjANBgkqhkiG
9w0BAQEFAAOCAQ8AMIIBCgKCAQEA2bS92oiPMQXHTRQ/DEwIDHoN0hjT0PCPsojK
M4ew+EIJ0DgvlxmSJMPhyZk8EabhLpz/jjy323+OjvGTvwX0igbAJdqFbebTmYB+
tgeDYImFZjjQKhBsh622T+zIrseiqmWEXOkEq8m0Uuc2aARNIUO5AGuhqwV7ANY6
x6tlAiGb6jkqRDDOvUET7aBFAqywjvvO7leT8DOtiVBgaKwHrfPrNa3SSkGiREIH
L5NQiiMPqBxm7exVhqR4CgdkM6JaMd0+V8xgcVciy3KsNJGkgD1y+rMGMGl244Ky
t8TPDpFAqxOL1X9eN9SUoKTWe1VPM12KkLIeIv8ztzP0Mg8otwIDAQABo4IBazCC
AWcwHwYDVR0jBBgwFoAUUzLRs89/+uDxoF2FTpLSnkUdtE8wHQYDVR0OBBYEFBYP
HJ1Pbkki3jPNMos0CnbbrQY1MA4GA1UdDwEB/wQEAwIBBjASBgNVHRMBAf8ECDAG
AQH/AgEAMDQGA1UdJQQtMCsGCCsGAQUFBwMBBggrBgEFBQcDAgYKKwYBBAGCNwoD
AwYJYIZIAYb4QgQBMBkGA1UdIAQSMBAwDgYMKwYBBAGBtz0FAQEAMD0GA1UdHwQ2
MDQwMqAwoC6GLGh0dHA6Ly9jcmwudXNlcnRydXN0LmNvbS9VVE4tREFUQUNvcnBT
R0MuY3JsMHEGCCsGAQUFBwEBBGUwYzA6BggrBgEFBQcwAoYuaHR0cDovL2NydC51
c2VydHJ1c3QuY29tL1VUTkFkZFRydXN0U0dDX0NBLmNydDAlBggrBgEFBQcwAYYZ
aHR0cDovL29jc3AudXNlcnRydXN0LmNvbTANBgkqhkiG9w0BAQUFAAOCAQEABQPN
bzuVoN6vzsjEJAbAhOX1smQY3lMzco1nNeiK5tVH373DXCYtZb1/MzUTrX0qYOWb
VFixm5/eqUGBaN31TJje3Tk+qbtMrfaaYBFH3W1PzaW5mVqJ+D7OeleyzAmxzWdO
BeuhybZUo3Ut5aOR0oKEzeCPXR7ZY7vw8iy7rXNj3xIYDzhe9NcI1pLEf5U1b9a/
P81Xt5QUFjE2Gja+SSStwNTc4cAXpNAVZwZDFnhl/ybpclQwKC0o2NtcitFln5XJ
lGoDi82m/ytVDnmLLLeAjwoRoy5cRJjvrc6Raodqs1tspauEjWrp0i9uV1MhzKeU
Oa6Ta6YZeFWhGxyJrg==
                    
-----END CERTIFICATE-----"""
            if(!CACertificate.findWhere(data:data0)) {
                def caCert9 = new CACertificate(data:data9)
                def caKeyInfo9 = new CAKeyInfo(certificate:caCert9)
                caKeyInfo9.save()
                if(caKeyInfo9.hasErrors()) {
                    println "Error importing CA 9"
                    caKeyInfo9.errors.each {println it}
                } else { println "CA 9 imported" }
            }
            else { println "CA 9 existed from seperate source" }
    
            def data10 = """-----BEGIN CERTIFICATE-----
 
MIIGVTCCBT2gAwIBAgIQCFH5WYFBRcq94CTiEsnCDjANBgkqhkiG9w0BAQUFADBs
MQswCQYDVQQGEwJVUzEVMBMGA1UEChMMRGlnaUNlcnQgSW5jMRkwFwYDVQQLExB3
d3cuZGlnaWNlcnQuY29tMSswKQYDVQQDEyJEaWdpQ2VydCBIaWdoIEFzc3VyYW5j
ZSBFViBSb290IENBMB4XDTA3MDQwMzAwMDAwMFoXDTIyMDQwMzAwMDAwMFowZjEL
MAkGA1UEBhMCVVMxFTATBgNVBAoTDERpZ2lDZXJ0IEluYzEZMBcGA1UECxMQd3d3
LmRpZ2ljZXJ0LmNvbTElMCMGA1UEAxMcRGlnaUNlcnQgSGlnaCBBc3N1cmFuY2Ug
Q0EtMzCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBAL9hCikQH17+NDdR
CPge+yLtYb4LDXBMUGMmdRW5QYiXtvCgFbsIYOBC6AUpEIc2iihlqO8xB3RtNpcv
KEZmBMcqeSZ6mdWOw21PoF6tvD2Rwll7XjZswFPPAAgyPhBkWBATaccM7pxCUQD5
BUTuJM56H+2MEb0SqPMV9Bx6MWkBG6fmXcCabH4JnudSREoQOiPkm7YDr6ictFuf
1EutkozOtREqqjcYjbTCuNhcBoz4/yO9NV7UfD5+gw6RlgWYw7If48hl66l7XaAs
zPw82W3tzPpLQ4zJ1LilYRyyQLYoEt+5+F/+07LJ7z20Hkt8HEyZNp496+ynaF4d
32duXvsCAwEAAaOCAvcwggLzMA4GA1UdDwEB/wQEAwIBhjCCAcYGA1UdIASCAb0w
ggG5MIIBtQYLYIZIAYb9bAEDAAIwggGkMDoGCCsGAQUFBwIBFi5odHRwOi8vd3d3
LmRpZ2ljZXJ0LmNvbS9zc2wtY3BzLXJlcG9zaXRvcnkuaHRtMIIBZAYIKwYBBQUH
AgIwggFWHoIBUgBBAG4AeQAgAHUAcwBlACAAbwBmACAAdABoAGkAcwAgAEMAZQBy
AHQAaQBmAGkAYwBhAHQAZQAgAGMAbwBuAHMAdABpAHQAdQB0AGUAcwAgAGEAYwBj
AGUAcAB0AGEAbgBjAGUAIABvAGYAIAB0AGgAZQAgAEQAaQBnAGkAQwBlAHIAdAAg
AEMAUAAvAEMAUABTACAAYQBuAGQAIAB0AGgAZQAgAFIAZQBsAHkAaQBuAGcAIABQ
AGEAcgB0AHkAIABBAGcAcgBlAGUAbQBlAG4AdAAgAHcAaABpAGMAaAAgAGwAaQBt
AGkAdAAgAGwAaQBhAGIAaQBsAGkAdAB5ACAAYQBuAGQAIABhAHIAZQAgAGkAbgBj
AG8AcgBwAG8AcgBhAHQAZQBkACAAaABlAHIAZQBpAG4AIABiAHkAIAByAGUAZgBl
AHIAZQBuAGMAZQAuMA8GA1UdEwEB/wQFMAMBAf8wNAYIKwYBBQUHAQEEKDAmMCQG
CCsGAQUFBzABhhhodHRwOi8vb2NzcC5kaWdpY2VydC5jb20wgY8GA1UdHwSBhzCB
hDBAoD6gPIY6aHR0cDovL2NybDMuZGlnaWNlcnQuY29tL0RpZ2lDZXJ0SGlnaEFz
c3VyYW5jZUVWUm9vdENBLmNybDBAoD6gPIY6aHR0cDovL2NybDQuZGlnaWNlcnQu
Y29tL0RpZ2lDZXJ0SGlnaEFzc3VyYW5jZUVWUm9vdENBLmNybDAfBgNVHSMEGDAW
gBSxPsNpA/i/RwHUmCYaCALvY2QrwzAdBgNVHQ4EFgQUUOpzidsp+xCPnuUBINTe
eZlIg/cwDQYJKoZIhvcNAQEFBQADggEBAF1PhPGoiNOjsrycbeUpSXfh59bcqdg1
rslx3OXb3J0kIZCmz7cBHJvUV5eR13UWpRLXuT0uiT05aYrWNTf58SHEW0CtWakv
XzoAKUMncQPkvTAyVab+hA4LmzgZLEN8rEO/dTHlIxxFVbdpCJG1z9fVsV7un5Tk
1nq5GMO41lJjHBC6iy9tXcwFOPRWBW3vnuzoYTYMFEuFFFoMg08iXFnLjIpx2vrF
EIRYzwfu45DC9fkpx1ojcflZtGQriLCnNseaIGHr+k61rmsb5OPs4tk8QUmoIKRU
9ZKNu8BVIASm2LAXFszj0Mi0PeXZhMbT9m5teMl5Q+h6N/9cNUm/ocU=
                    
-----END CERTIFICATE-----"""
            if(!CACertificate.findWhere(data:data0)) {
                def caCert10 = new CACertificate(data:data10)
                def caKeyInfo10 = new CAKeyInfo(certificate:caCert10)
                caKeyInfo10.save()
                if(caKeyInfo10.hasErrors()) {
                    println "Error importing CA 10"
                    caKeyInfo10.errors.each {println it}
                } else { println "CA 10 imported" }
            }
            else { println "CA 10 existed from seperate source" }
    
            def data11 = """-----BEGIN CERTIFICATE-----
 
MIIEQjCCA6ugAwIBAgIEQodApTANBgkqhkiG9w0BAQUFADCBwzELMAkGA1UEBhMC
VVMxFDASBgNVBAoTC0VudHJ1c3QubmV0MTswOQYDVQQLEzJ3d3cuZW50cnVzdC5u
ZXQvQ1BTIGluY29ycC4gYnkgcmVmLiAobGltaXRzIGxpYWIuKTElMCMGA1UECxMc
KGMpIDE5OTkgRW50cnVzdC5uZXQgTGltaXRlZDE6MDgGA1UEAxMxRW50cnVzdC5u
ZXQgU2VjdXJlIFNlcnZlciBDZXJ0aWZpY2F0aW9uIEF1dGhvcml0eTAeFw0wNjEw
MDEwNTAwMDBaFw0xNDA3MjYxODE1MTVaMGwxCzAJBgNVBAYTAlVTMRUwEwYDVQQK
EwxEaWdpQ2VydCBJbmMxGTAXBgNVBAsTEHd3dy5kaWdpY2VydC5jb20xKzApBgNV
BAMTIkRpZ2lDZXJ0IEhpZ2ggQXNzdXJhbmNlIEVWIFJvb3QgQ0EwggEiMA0GCSqG
SIb3DQEBAQUAA4IBDwAwggEKAoIBAQDGzOVz5vvUu+UtLTKm3+WBP8nNJUm2cSrD
1ZQ0Z6IKHLBfaaZAscS3so/QmKSpQVk609yU1jzbdDikSsxNJYL3SqVTEjju80lt
cZF+Y7arpl/DpIT4T2JRvvjF7Ns4kuMG5QiRDMQoQVX7y1qJFX5x6DW/TXIJPb46
OFBbdzEbjbPHJEWap6xtABRaBLe6E+tRCphBQSJOZWGHgUFQpnlcid4ZSlfVLuZd
HFMsfpjNGgYWpGhz0DQEE1yhcdNafFXbXmThN4cwVgTlEbQpgBLxeTmIogIRfCdm
t4i3ePLKCqg4qwpkwr9mXZWEwaElHoddGlALIBLMQbtuC1E4uEvLAgMBAAGjggET
MIIBDzASBgNVHRMBAf8ECDAGAQH/AgEBMCcGA1UdJQQgMB4GCCsGAQUFBwMBBggr
BgEFBQcDAgYIKwYBBQUHAwQwMwYIKwYBBQUHAQEEJzAlMCMGCCsGAQUFBzABhhdo
dHRwOi8vb2NzcC5lbnRydXN0Lm5ldDAzBgNVHR8ELDAqMCigJqAkhiJodHRwOi8v
Y3JsLmVudHJ1c3QubmV0L3NlcnZlcjEuY3JsMB0GA1UdDgQWBBSxPsNpA/i/RwHU
mCYaCALvY2QrwzALBgNVHQ8EBAMCAQYwHwYDVR0jBBgwFoAU8BdiE1U9s/8KAGv7
UISX8+1i0BowGQYJKoZIhvZ9B0EABAwwChsEVjcuMQMCAIEwDQYJKoZIhvcNAQEF
BQADgYEASA4rbyBiTCiToyQ9WKshz4D4mpeQaiLtWnxHNpnneYR1qySPkgrVYQSu
w2pcsszZ5ESHb9uPOGL3RDadurxuB8TUjegf0Qtgo7WczmO+7Wfc+Lrebskly1u1
nXZwC99CcvhPQRFkpdLq/NWvEfQVOGecIKhLd1qRMkIy54Wz3zY=
                    
-----END CERTIFICATE-----"""
            if(!CACertificate.findWhere(data:data0)) {
                def caCert11 = new CACertificate(data:data11)
                def caKeyInfo11 = new CAKeyInfo(certificate:caCert11)
                caKeyInfo11.save()
                if(caKeyInfo11.hasErrors()) {
                    println "Error importing CA 11"
                    caKeyInfo11.errors.each {println it}
                } else { println "CA 11 imported" }
            }
            else { println "CA 11 existed from seperate source" }
    