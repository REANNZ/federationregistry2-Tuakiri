log4j = {
    error  'org.codehaus.groovy.grails.web.servlet',  //  controllers
           'org.codehaus.groovy.grails.web.pages', //  GSP
           'org.codehaus.groovy.grails.web.sitemesh', //  layouts
           'org.codehaus.groovy.grails.web.mapping.filter', // URL mapping
           'org.codehaus.groovy.grails.web.mapping', // URL mapping
           'org.codehaus.groovy.grails.commons', // core / classloading
           'org.codehaus.groovy.grails.plugins', // plugins
           'org.codehaus.groovy.grails.orm.hibernate', // hibernate integration
           'org.springframework',
           'org.hibernate',
           'net.sf.ehcache.hibernate'

    error   'org.mortbay.log'

		debug 'grails.app.service.grails.buildtestdata.BuildTestDataService'
	    debug 'grails.buildtestdata.DomainInstanceBuilder'
	    debug 'grails.buildtestdata.handler'
	
		info	'fedreg.metadata',
				'grails.app.controller',
				'grails.app.service',
				'grails.app.domain'
}

// The following properties have been added by the Upgrade process...
grails.views.default.codec="none" // none, html, base64
grails.views.gsp.encoding="UTF-8"

// Codenarc
codenarc.processTestUnit=false
codenarc.processTestIntegration=false
