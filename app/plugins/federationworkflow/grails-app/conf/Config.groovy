
grails.spring.bean.packages = ['fedreg.workflow']

// Environmental configuration
environments {
	development {	
		nimble.messaging.mail.host = 'localhost'
		nimble.messaging.mail.port = com.icegreen.greenmail.util.ServerSetupTest.SMTP.port
		nimble.messaging.mail.from = "workflow@example.com"
	}
    test {
		log4j = {
			debug	'fedreg.workflow',
					'grails.app.service',
					'grails.app.domain'

			appenders {
				console name:'stdout', layout:pattern(conversionPattern: '%d %-5p: %m%n')
			}
		}
		
		nimble {
		    tablenames {
		        user =  "_user"
		        role =  "_role"
		        group =  "_group"
		        federationprovider =  "federation_provider"
		        profilebase = "profile_base"
		        loginrecord = "login_record"
		        details =  "details"
		        permission = "permission"
		        levelpermission = "level_permission"
		        url = "url"
		    }
			messaging {
				mail {
					host = 'localhost'
					port = com.icegreen.greenmail.util.ServerSetupTest.SMTP.port
					from = "workflow@test.com"
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
