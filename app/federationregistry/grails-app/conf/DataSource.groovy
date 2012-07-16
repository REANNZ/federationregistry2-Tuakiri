
hibernate {
    cache.use_second_level_cache = true
    cache.use_query_cache = true
    cache.region.factory_class = 'net.sf.ehcache.hibernate.EhCacheRegionFactory'
}

// environment specific settings
environments {
	development {
		dataSource {
			dbCreate = "update"
		}
	}
	test {
		dataSource {
      pooled = true
      driverClassName = "com.mysql.jdbc.Driver"
      dialect = org.hibernate.dialect.MySQL5InnoDBDialect
      dbCreate = "create-drop"
      loggingSql = false
      
      url = "jdbc:mysql://localhost/federationregistry_testapp_${System.getenv('fr_testdatabase')}"
      username = "fr"
      password =  "password"
    }
	}
	production {
		dataSource {
			dbCreate = "update"
		}
	}
}