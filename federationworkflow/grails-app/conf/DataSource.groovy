
hibernate {
    cache.use_second_level_cache=true
    cache.use_query_cache=true
    cache.provider_class='net.sf.ehcache.hibernate.EhCacheProvider'
}
// environment specific settings
environments {
	development {
		dataSource {
			pooled = true
			driverClassName = "org.hsqldb.jdbcDriver"
			username = "sa"
			password = ""
			
			dbCreate = "create" // one of 'create', 'create-drop','update'
			url = "jdbc:hsqldb:mem:devDB"
		}
	}
	test {
		
		dataSource {
			pooled = true
			driverClassName = "org.hsqldb.jdbcDriver"
			username = "sa"
			password = ""
			
			dbCreate = "create" // one of 'create', 'create-drop','update'
			url = "jdbc:hsqldb:mem:devDB"
		}
	}
	production {
		dataSource {
			dbCreate = "update"
			url = "jdbc:hsqldb:file:prodDb;shutdown=true"
		}
	}
}