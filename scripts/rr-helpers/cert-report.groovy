    import groovy.sql.Sql
    
    import fedreg.host.*
    import fedreg.core.*
    
    cryptoService = ctx.getBean("cryptoService")
    userService = ctx.getBean("userService")
    roleService = ctx.getBean("roleService")
    permissionService = ctx.getBean("permissionService")
    grailsApplication = ctx.getBean('grailsApplication')
    
    sql = Sql.newInstance("jdbc:mysql://localhost:3307/resourceregistry", "rr-ro", "aafrrr0", "com.mysql.jdbc.Driver")
    
    sql.eachRow("select * from certData where objectType='issuingca'", {
        def data = "-----BEGIN CERTIFICATE-----\n${it.certData}\n-----END CERTIFICATE-----"
        def caCert = new CACertificate(data:data)
        def caKeyInfo = new CAKeyInfo(certificate:caCert)
        caKeyInfo.save()
        if(caKeyInfo.hasErrors()) {
            println "Error importing CA"
            caKeyInfo.errors.each {println it}
        }
    })

    def invalid = 0
    def tot = 0
    sql.eachRow("select DISTINCT objectID, certData from certData where objectType != 'issuingca'",
    {
        tot++
        try {
            def data = "-----BEGIN CERTIFICATE-----\n${it.certData.normalize()}\n-----END CERTIFICATE-----"
            def cert = cryptoService.createCertificate(data)    
            if(!cryptoService.validateCertificate(cert)) {
                sql.eachRow("select * from objectDescriptions where objectID=${it.objectID}") {
                    println "Invalid certificate for ${it.objectID} - ${it.objectType} - ${it.descriptiveName} \n Issuer: ${cert.issuer} \n Expires: ${cert.expiryDate} \n"
                    invalid++
                }
            }
        }
        catch(Exception e) {
            println e.getMessage()
        }
    })
    
    println "Total of $invalid certificates located from $tot ${(invalid/tot)*100}%"