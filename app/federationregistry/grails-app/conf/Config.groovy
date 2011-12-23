import javax.naming.InitialContext
import javax.naming.Context

// Import externalized configuration for the Federation Registry application
def externalConf = getFromEnvironment("fr_config")
if(externalConf) {
  println( "Including external configuration from: ${externalConf}" )
  grails.config.locations = ["file:${externalConf}/fr-config.groovy"]
} else {
  println "No external configuration location specified as environment variable fr_config, terminating startup"
  throw new RuntimeException("No external configuration location specified as environment variable fr_config")
}

// Extract user details to append to Audit Table
auditLog {
  actorClosure = { request, session ->
    org.apache.shiro.SecurityUtils.getSubject()?.getPrincipal()
  }
}

grails.converters.xml.pretty.print = true

grails.mime.file.extensions = true
grails.mime.use.accept.header = false
grails.mime.types = [ html: ['text/html', 'application/xhtml+xml'],
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
    testDataConfig {
    enabled = true
    }
    grails.mail.port = com.icegreen.greenmail.util.ServerSetupTest.SMTP.port
  }
  development {
    grails.resources.debug = true
    grails.gsp.enable.reload = true
    testDataConfig {
      enabled = false
    }

    grails.mail.port = com.icegreen.greenmail.util.ServerSetupTest.SMTP.port
  }
}

/**
* This is a little hacky but lets us move away from environment variables in production scenarios
* while maintaining flexibility in local development.
*/
public String getFromEnvironment(final String name) {
  if(name == null) return null;
  try {
    final Object object = ((Context)(new InitialContext().lookup("java:comp/env"))).lookup(name);
    if (object != null) return object.toString();
  } catch (final Exception e) {}

  return System.getenv(name);
}
