
// Import externalized configuration for the Federation Registry application
def fedregConf = System.getenv("FEDREG_CONFIG")
if(fedregConf) {
	println( "Including configuration file: ${fedregConf}" )
	grails.config.locations = ["file:${fedregConf}"]
} else {
	println "No external configuration file defined for environment variable FEDREG_CONFIG, terminating startup"
	throw new RuntimeException("No external configuration file defined for environment variable FEDREG_CONFIG, terminating startup")
}

// Standard Grails configuration

grails.gorm.default.mapping = {

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
grails.views.gsp.sitemesh.preprocess = true

// Environmental configuration
environments {
    test {
        grails.serverURL = "http://localhost:8080/${appName}"
		testDataConfig {
        	enabled = true
      	}
    }
    development {
		grails.gsp.enable.reload = true
        grails.serverURL = "http://localhost:8080/${appName}"
		testDataConfig {
        	enabled = false
      	}
    }
}




     
