
grails.spring.bean.packages = ['fedreg.workflow']

workflow {
	messaging {
		mail {
			from = "workflow@example.com"
		}
	}
}



// Environmental configuration
environments {
	development {	
		nimble.messaging.mail.host = 'localhost'
		nimble.messaging.mail.port = com.icegreen.greenmail.util.ServerSetupTest.SMTP.port
	}
    test {	
		nimble.messaging.mail.host = 'localhost'
		nimble.messaging.mail.port = com.icegreen.greenmail.util.ServerSetupTest.SMTP.port
		log4j = {
			debug	'fedreg.workflow',
					'grails.app.service',
					'grails.app.domain'

			appenders {
				console name:'stdout', layout:pattern(conversionPattern: '%d %-5p: %m%n')
			}
		}
	}
}