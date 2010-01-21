package fedreg.host

/**
 * Provides methods for interacting with Shibboleth authentication processes.
 *
 * @author Bradley Beddoes
 */

import grails.plugins.nimble.core.*

class ShibbolethService {
    boolean transactional = true

    public static final federationProviderUid = "shibboleth"
    public static final federationProviderDiscriminator = ""

    def grailsApplication
    
    def shibbolethFederationProvider

    /**
     * Integrates with extended Nimble bootstrap process, sets up Shibboleth Environment
     * once all domain objects etc have dynamic methods available to them.
     */
    public void nimbleInit() {
        if (grailsApplication.config.fedreg.shibboleth.federationprovider.enabled) {
            shibbolethFederationProvider = FederationProvider.findByUid(ShibbolethService.federationProviderUid)
            if (!shibbolethFederationProvider) {

                shibbolethFederationProvider = new FederationProvider()
                shibbolethFederationProvider.uid = ShibbolethService.federationProviderUid
                shibbolethFederationProvider.autoProvision = grailsApplication.config.fedreg.shibboleth.federationprovider.autoprovision

                def details = new Details()
                details.name = grailsApplication.config.fedreg.shibboleth.name
                details.displayName = grailsApplication.config.fedreg.shibboleth.displayname
                details.description = grailsApplication.config.fedreg.shibboleth.description

                def url = new Url()
                url.location = grailsApplication.config.fedreg.shibboleth.url
                url.altText = grailsApplication.config.fedreg.shibboleth.alttext
                def savedUrl = url.save()
                if(url.hasErrors()) {
                    url.errors.each {
                        log.error(it)
                    }
                    throw new RuntimeException("Unable to create valid Shibboleth federation provider (url)")
                }

                details.url = savedUrl

                shibbolethFederationProvider.details = details

				shibbolethFederationProvider.save()
                if (shibbolethFederationProvider.hasErrors()) {
                    shibbolethFederationProvider.errors.each {
                        log.error(it)
                    }
                    throw new RuntimeException("Unable to create valid Shibboleth federation provider")
                }
		 	}
		}
	}
}
	