
// Import externalized configuration for the Federation Registry application
def externalConf = System.getenv("FEDREG_CONFIG")
if(externalConf) {
	println( "Including configuration file: ${externalConf}" )
	grails.config.locations = ["file:${externalConf}/FedRegConfig.groovy",
							   "file:${externalConf}/NimbleConfig.groovy"]
} else {
	println "No external configuration location specified as environment variable FEDREG_CONFIG, terminating startup"
	throw new RuntimeException("No external configuration location specified as environment variable FEDREG_CONFIG, terminating startup")
}

// Standard Grails configuration

grails.gorm.default.mapping = {

}

grails.converters.xml.pretty.print = true

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
grails.views.gsp.sitemesh.preprocess = true

// Environmental configuration
environments {
    test {
        grails.serverURL = "http://localhost:8080/${appName}"
		testDataConfig {
        	enabled = true
      	}
		
		log4j = {
			debug	'fedreg.workflow',
					'grails.app.controller',
					'grails.app.service',
					'grails.app.domain'

			appenders {
				console name:'stdout', layout:pattern(conversionPattern: '%d %-5p: %m%n')
			}
		}
		
		nimble {
			messaging {
				mail {
					host = 'localhost'
					port = com.icegreen.greenmail.util.ServerSetupTest.SMTP.port
				}
			}
		}
		
    }
    development {
		grails.gsp.enable.reload = true
        grails.serverURL = "http://localhost:8080/${appName}"
		testDataConfig {
        	enabled = false
      	}

		log4j = {
			debug	'fedreg.workflow',
					'fedreg.core',
					'fedreg.host',
					'grails.app.controller',
					'grails.app.service',
					'grails.app.domain'

			appenders {
				console name:'stdout', layout:pattern(conversionPattern: '%d %-5p: %m%n')
			}
		}
		
		nimble {
			messaging {
				mail {
					host = 'localhost'
					port = com.icegreen.greenmail.util.ServerSetupTest.SMTP.port
				}
			}
		}
    }
}
