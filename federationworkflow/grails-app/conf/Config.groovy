
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
	}
}