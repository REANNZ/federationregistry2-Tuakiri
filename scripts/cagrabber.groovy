
def script = "import fedreg.core.* \n\n"
def inf = new File('./testcerts')
def xml = inf.text.replaceAll('ds:', '') //lazyness

def keys = new XmlSlurper().parseText(xml)
keys.KeyInfo.eachWithIndex { it, i ->

    script = script + """
            def data${i} = "\"\"-----BEGIN CERTIFICATE-----\n${it.X509Data.X509Certificate}\n-----END CERTIFICATE-----"\"\"
            if(!CACertificate.findWhere(data:data0)) {
                def caCert${i} = new CACertificate(data:data${i})
                def caKeyInfo${i} = new CAKeyInfo(certificate:caCert${i})
                caKeyInfo${i}.save()
                if(caKeyInfo${i}.hasErrors()) {
                    println "Error importing CA ${i}"
                    caKeyInfo${i}.errors.each {println it}
                } else { println "CA ${i} imported" }
            }
            else { println "CA ${i} existed from seperate source" }
    """

}

def outf = new File('./cagroovyimporter.groovy')
outf.text = script

return true