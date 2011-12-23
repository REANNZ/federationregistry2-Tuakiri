package aaf.fr.identity

import grails.plugins.federatedgrails.SubjectBase

class Subject extends SubjectBase {

  String displayName
  String email

  static constraints = {
    email email:true
  }

  public String toString() {
      "aaf.fr.identity.Subject [id: $id, principal: $principal]"
  }

  // Crude but necessary
  public String getGivenName() {
    displayName?.split(' ')[0]
  }

  public String getSurname() {
    displayName?.split(' ')[1]
  }
}