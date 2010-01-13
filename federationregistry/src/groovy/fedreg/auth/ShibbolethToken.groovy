package fedreg.auth

import org.openid4java.discovery.Identifier
import org.apache.shiro.authc.AuthenticationToken

/**
 * Token used with OpenID Shiro realm for authentication purposes
 *
 * @author Bradley Beddoes
 */
public class ShibbolethToken implements AuthenticationToken {

  	def principal, givenName, surname, email

	public Object getPrincipal() {
	    return this.principal
	}
	
  	public Object getCredentials() {
	    return null
	}
}