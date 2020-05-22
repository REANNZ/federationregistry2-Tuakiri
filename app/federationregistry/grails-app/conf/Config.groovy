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

// Enable console plugin
grails.plugin.console.enabled = true

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

grails.views.default.codec = "html"
grails.views.gsp.encoding = "UTF-8"
grails.converters.encoding = "UTF-8"
grails.enable.native2ascii = true
grails.views.gsp.sitemesh.preprocess = true

/**
* This is allows usage of environment variables in production
* while maintaining flexibility in development.
*/
public String getFromEnvironment(final String name) {
  if(name == null) return null;
  try {
    final Object object = ((Context)(new InitialContext().lookup("java:comp/env"))).lookup(name);
    if (object != null) return object.toString();
  } catch (final Exception e) {}

  return System.getenv(name);
}

// Uncomment and edit the following lines to start using Grails encoding & escaping improvements

/* remove this line 
// GSP settings
grails {
    views {
        gsp {
            encoding = 'UTF-8'
            htmlcodec = 'xml' // use xml escaping instead of HTML4 escaping
            codecs {
                expression = 'html' // escapes values inside null
                scriptlet = 'none' // escapes output from scriptlets in GSPs
                taglib = 'none' // escapes output from taglibs
                staticparts = 'none' // escapes output from static template parts
            }
        }
        // escapes all not-encoded output at final stage of outputting
        filteringCodecForContentType {
            //'text/html' = 'html'
        }
    }
}
remove this line */
