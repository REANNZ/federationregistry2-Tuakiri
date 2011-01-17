dataSource { 
    pooled = false 
    driverClassName = "com.mysql.jdbc.Driver" 
    username = "test" 
    password = "test" 
	dialect = "org.hibernate.dialect.MySQL5InnoDBDialect"
}

hibernate {
    cache.use_second_level_cache=false
    cache.use_query_cache=false
    cache.provider_class='net.sf.ehcache.hibernate.EhCacheProvider'
}

environments {
	test {
		dataSource {
				dbCreate = "update"
				url = "jdbc:mysql://localhost:3306/testdb"
				loggingSql = false
		}
	}
}