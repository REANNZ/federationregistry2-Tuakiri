package aaf.fr.identity

import grails.plugins.federatedgrails.SubjectBase

class Subject extends SubjectBase {

  // Extend with your custom values here
  String email

  static constraints = {
    email email:true
  }

  public String toString() {
      "aaf.fr.identity.Subject [id: $id, principal: $principal]"
  }

}