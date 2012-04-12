package aaf.fr.app

import aaf.fr.identity.Subject

class Invitation {

  String inviteCode
  
  Boolean utilized = false
  Subject utilizedBy
    
  Long role
  
  String controller
  String action
  long objID
  
  static constraints = {
    inviteCode(nullable:false, unique:true)
    
    role(nullable:true)
    
    controller(nullable:true)
    action(nullable:true)
    objID(nullable:true)
    
    utilizedBy(nullable:true)
  }
  
  static mapping = {
    group column: "grp__"
    role column: "role__"
  }
  
}
