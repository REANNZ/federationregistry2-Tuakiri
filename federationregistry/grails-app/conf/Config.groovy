
aaf {
	fedreg {
		oldrr {
			connection = "jdbc:mysql://localhost:3306/resourceregistry"
			user = "rr"
			password = "password"
			driver = "com.mysql.jdbc.Driver"
		}
	}
}

security.shiro.authc.required = false

// set per-environment serverURL stem for creating absolute links
environments {
    production {
        grails.serverURL = "http://www.changeme.com"
		testDataConfig {
        	enabled = false
      	}
    }
    test {
        grails.serverURL = "http://localhost:8080/${appName}"
		testDataConfig {
        	enabled = true
      	}
    }
    development {
        grails.serverURL = "http://localhost:8080/${appName}"
		testDataConfig {
        	enabled = false
      	}
    }
}

// log4j configuration
log4j = {
    error 	'org.codehaus.groovy.grails.web.servlet',
          	'org.codehaus.groovy.grails.web.pages',
          	'org.codehaus.groovy.grails.web.sitemesh',
          	'org.codehaus.groovy.grails.web.mapping.filter',
          	'org.codehaus.groovy.grails.web.mapping',
          	'org.codehaus.groovy.grails.commons',
          	'org.codehaus.groovy.grails.plugins'

    debug 	'intient.nimble',
			'aaf.fedreg'
}

grails.mime.file.extensions = true
grails.mime.use.accept.header = false
grails.mime.types = [html: ['text/html', 'application/xhtml+xml'],
    xml: ['text/xml', 'application/xml'],
    text: 'text/plain',
    js: 'text/javascript',
    rss: 'application/rss+xml',
    atom: 'application/atom+xml',
    css: 'text/css',
    csv: 'text/csv',
    all: '*/*',
    json: ['application/json', 'text/json'],
    form: 'application/x-www-form-urlencoded',
    multipartForm: 'multipart/form-data'
]

grails.views.default.codec = "none"
grails.views.gsp.encoding = "UTF-8"
grails.converters.encoding = "UTF-8"

grails.enable.native2ascii = true


// enable GSP preprocessing: replace head -> g:captureHead, title -> g:captureTitle, meta -> g:captureMeta, body -> g:captureBody
grails.views.gsp.sitemesh.preprocess = true




     