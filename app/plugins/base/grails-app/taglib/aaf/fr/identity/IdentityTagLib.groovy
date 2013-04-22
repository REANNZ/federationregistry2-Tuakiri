package aaf.fr.identity

import org.apache.shiro.SecurityUtils
import org.apache.shiro.authz.permission.WildcardPermission

/**
* Provides authentication identity tags to federation registry
*
* @author Bradley Beddoes
*/
class IdentityTagLib {

  static namespace = "fr"

  static returnObjectForTags = ['subject']

  // This tag only writes its body to the output if the current user is an FR wide administrator
  def isAdministrator = { attrs, body ->
    if(checkPermission('federation:globaladministrator')) {
      out << body()
    }
  }

  // This tag only writes its body to the output if the current user has the given permission.
  def hasPermission = {attrs, body ->
    def target = attrs['target']
    if (target) {
      if (checkPermission(target)) {
        out << body()
      }
    }
    else {
      throwTagError('Tag [hasPermission] must have [in] attribute.')
    }
  }

  // This tag only writes its body to the output if the current user has any of the given permissions.
  def hasAnyPermission = {attrs, body ->
    def inList = attrs['in']
    if (!inList)
    throwTagError('Tag [hasAnyPermission] must have [in] attribute.')

    if(inList.any { checkPermission(it) } ) {
      out << body()
    }
  }

  // This tag only writes its body to the output if the current user does not have the given permission.
  def lacksPermission = {attrs, body ->
    def target = attrs['target']
    if (target) {
      if (!checkPermission(target)) {
        out << body()
      }
    }
    else {
      throwTagError('Tag [lacksPermission] must have [in] attribute.')
    }
  }

  // This tag only writes its body to the output if the current subject is logged in.
  def isLoggedIn = {attrs, body ->
    if (checkAuthenticated()) {
      out << body()
    }
  }

  // This tag only writes its body to the output if the current subject isn't logged in.
  def isNotLoggedIn = {attrs, body ->
    if (!checkAuthenticated()) {
      out << body()
    }
  }

  // Provides markup that renders the username of the logged in user
  def principal = {
    Long id = SecurityUtils.getSubject()?.getPrincipal()
    if (id) {
      def subject = Subject.get(id)
      if (subject)
        out << subject.principal.encodeAsHTML()
    }
  }

  // Provides markup the renders the name of the logged in user
  def principalName = { attrs, body ->
    Long id = SecurityUtils.getSubject()?.getPrincipal()
    if (id) {
      def subject = Subject.get(id)
      out << subject.cn.encodeAsHTML()
    }
  }

  // Provides access to the logged in subject
  def subject = { attrs, body ->
    Long id = SecurityUtils.getSubject()?.getPrincipal()
    if (id) {
      def subject = Subject.get(id)
    }
  }

  private boolean checkPermission(String target) {
    WildcardPermission permission = new WildcardPermission(target, false)
    return SecurityUtils.subject.isPermitted(permission)
  }

  private boolean checkAuthenticated() {
    return SecurityUtils.subject.authenticated
  }

}
