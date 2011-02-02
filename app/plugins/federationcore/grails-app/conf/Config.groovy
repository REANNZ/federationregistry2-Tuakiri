

// Environmental configuration
environments {

    test {
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
}
// The following properties have been added by the Upgrade process...
grails.views.default.codec="none" // none, html, base64
grails.views.gsp.encoding="UTF-8"

// Codenarc
codenarc.processTestUnit=false
codenarc.processTestIntegration=false


