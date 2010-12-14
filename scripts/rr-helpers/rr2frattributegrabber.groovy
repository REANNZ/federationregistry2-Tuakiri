
    import groovy.sql.Sql
    import fedreg.core.*
    
    sql = Sql.newInstance("jdbc:mysql://localhost:3306/resourceregistry", "rr", "password", "com.mysql.jdbc.Driver")
    
    sql.eachRow("select * from attributes", 
    {
        println "def ${it.alias} =  new AttributeBase(oid:'${it.attributeOID}', name:'${it.attributeURN}', friendlyName:'${it.attributeFullName}', headerName:'${it.headerName}', alias:'${it.alias}', description:'${it.description}', scope:, specificationRequired:false)"
    })