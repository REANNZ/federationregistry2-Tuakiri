import org.apache.shiro.SecurityUtils
import org.apache.shiro.authc.AuthenticationException
import org.apache.shiro.authc.UnknownAccountException
import org.apache.shiro.authc.DisabledAccountException
import org.apache.shiro.authc.IncorrectCredentialsException
import org.apache.shiro.web.util.SavedRequest
import org.apache.shiro.web.util.WebUtils

import grails.plugins.federatedgrails.*

class AuthController {
  static defaultAction = "login"
  public final String TARGET = 'grails.controllers.AuthController.shiro:TARGET'
    
  def grailsApplication

  def login = {
    // Stores initial content user is attempting to access to redirect when auth complete
    if(params.targetUri)
      session.setAttribute(TARGET, params.targetUri)

    // Integrates with Shibboleth NativeSPSessionCreationParameters as per https://wiki.shibboleth.net/confluence/display/SHIB2/NativeSPSessionCreationParameters
    // This allows us to mix and match publicly available and private content within by making use of security filters in conf/SecurityFilters.groovy
    def localAction = createLink([action: 'federatedlogin', absolute: true])
    def url = "${grailsApplication.config.federation.ssoendpoint}?target=${localAction}"

    // If this a production scenario we defer to shibboleth
    if (grailsApplication.config.federation.automatelogin) {
      redirect (url: url)
      return
    }

    [spsession_url: url]
  }
  
  def logout = {
        log.info("Signing out subject [${subject?.id}]${subject?.principal}")
        SecurityUtils.subject?.logout()
        redirect(uri: '/')
    }

  def echo = {
      def attr = [:]
    if(grailsApplication.config.federation.request.attributes) {
      request.attributeNames.each {
        attr.put(it, (String)request.getAttribute(it))
      }
    } else {
      request.headerNames.each {
        attr.put(it, (String)request.getHeader(it))
      } 
    }
    return [attr: attr]
  }
  
  def federatedlogin = {
    if (!grailsApplication.config.federation.federationactive) {
      log.error("Attempt to do federated login when Apache SP is not marked active in local configuration")
      response.sendError(403)
      return
    }
    
    def incomplete = false
    def errors = []
    
    def principal = federatedAttributeValue(grailsApplication, grailsApplication.config.federation.mapping.principal)
    def credential = federatedAttributeValue(grailsApplication, grailsApplication.config.federation.mapping.credential)
    
    def attributes = [:]  
    
    /*
    Depending on your application requirements you can request as many or as few attributes as necessary,
    the below gives an example of what is possible. Refer to the AAF core attributes for a complete set of possibilities.
    
    attributes.entityID = federatedAttributeValue(grailsApplication, grailsApplication.config.federation.mapping.entityID)
    attributes.displayName =  federatedAttributeValue(grailsApplication, grailsApplication.config.federation.mapping.displayName)
    attributes.email = federatedAttributeValue(grailsApplication, grailsApplication.config.federation.mapping.email)
    attributes.entitlements = federatedAttributeValue(grailsApplication, grailsApplication.config.federation.mapping.entitlement)
    attributes.homeOrganization = federatedAttributeValue(grailsApplication, grailsApplication.config.federation.mapping.homeOrganization)
    attributes.homeOrganizationType = federatedAttributeValue(grailsApplication, grailsApplication.config.federation.mapping.homeOrganizationType)
    */
    
    if (!principal) {
      incomplete = true
      errors.add "Unique subject identifier (principal) was not presented"
    }

    if (!credential) {
      incomplete = true
      errors.add "Internal SAML session identifier (credential) was not presented"
    }
    
    // Add additional checks for any other attribute your application can't live without here. For example displayName or email.
    
    if(incomplete) {
      log.warn "Incomplete federated authentication attempt was aborted"
      errors.each { log.warn it }
      render (view:"federatedincomplete", model:[errors:errors])
      return
    }
    
    log.debug "Attempting Federation invoked based authentication event for subject $principal based on credential provided in $credential"
    
    try {
      def remoteHost = request.getRemoteHost()
      def ua = request.getHeader("User-Agent")
      ua = ua.length() > 254 ? ua.substring(0,254) : ua // Handle stupid user agents that present every detail known to man about corporate environments
      
      def token = new FederatedToken(principal:principal, credential:credential, attributes:attributes, remoteHost:remoteHost, userAgent:ua ) 
      
      SecurityUtils.subject.login(token)
      log.info "Successfully processed federation based authentication event for subject $principal based on credential provided in $credential, redirecting to content"
      
      def targetUri = session.getAttribute(TARGET)
            session.removeAttribute(TARGET)
      targetUri ? redirect(uri: targetUri) : redirect(uri:"/")
      return
    }
    catch (IncorrectCredentialsException e) {
      log.warn "Federated credentials failure for subject $principal, incorrect credentials."
      log.debug e
    }
    catch (DisabledAccountException e) {
      log.warn "Federated credentials failure for subject $principal, account disabled locally"
      log.debug e
    }
    catch (AuthenticationException e) {
      log.warn "Federated credentials failure for subject $principal, generic fault"
      log.debug e
    }

    redirect(action: "federatederror")
  }
  
  def federatedincomplete = { }
  
  def federatederror = { }
  
  def locallogin = {
    if (!grailsApplication.config.federation.developmentactive) {
      log.error "Authentication diverted to local development/testing accounts but this mode is not enabled in configuration"
      response.sendError(403)
      return
    }
    
    def incomplete = false
    def errors = []
    
    // Instead of SAML attributes we setup the session via form posted name/val
    def principal = params.principal
    def credential = params.credential  
    def attributes = params.attributes
    
    if (!principal) {
      incomplete = true
      errors.add "Unique subject identifier (principal) was not presented"
    }

    if (!credential) {
      incomplete = true
      errors.add "Internal SAML session identifier (credential) was not presented"
    }
    
    // Add additional checks for any other attribute your application can't live without here. For example displayName or email.
    
    if(incomplete) {
      log.info "Incomplete federated authentication attempt was aborted"
      errors.each { log.warn it }
      render (view:"federatedincomplete", model:[errors:errors])
      return
    }
    
    log.debug "Attempting local authentication event for subject $principal based on credential provided in $credential"
    
    try {
      def remoteHost = request.getRemoteHost()
      def ua = request.getHeader("User-Agent")
      ua = ua.length() > 254 ? ua.substring(0,254) : ua // Handle stupid user agents that present every detail known to man about corporate environments
      
      def token = new FederatedToken(principal:principal, credential:credential, attributes:attributes, remoteHost:remoteHost, userAgent:ua ) 
      
      
      
      SecurityUtils.subject.login(token)
      log.info "Successfully processed local development/testing authentication event for subject $principal based on credential provided in $credential, redirecting to content"
      
      def targetUri = session.getAttribute(TARGET)
            session.removeAttribute(TARGET)
      targetUri ? redirect(uri: targetUri) : redirect(uri:"/")
      return
    }
        catch (IncorrectCredentialsException e) {
            log.warn "Local credentials failure for subject $principal, incorrect credentials."
            log.debug e
        }
        catch (DisabledAccountException e) {
            log.warn "Local credentials failure for subject $principal, account disabled locally"
            log.debug e
        }
        catch (AuthenticationException e) {
            log.warn "Local credentials failure for subject $principal, generic fault"
            log.debug e
    }
    
    redirect(action: "login")
  }
  
  private String federatedAttributeValue(def grailsApplication, String attr) {
    def value
    if(grailsApplication.config.federation.request.attributes) {
      value = request.getAttribute(attr)
    } else {
      value = request.getHeader(attr)   // Not as secure
    }
    
    value
  }
}
