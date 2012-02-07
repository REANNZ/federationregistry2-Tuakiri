import fedreg.core.*
import grails.plugins.nimble.core.*

println "SP and administrators generated from FR on ${new Date()}"

SPSSODescriptor.list().each { sp ->
  println "Service: ${sp.displayName}"
  println "Organization: ${sp.organization.displayName}"
  def role = Role.findWhere(name:"descriptor-${sp.id}-administrators")
  if(role?.users) {
    println "Administrators:"
    def uniqueUsers = [] as List
     role.users.each { admin ->
      if(!uniqueUsers.contains(admin.profile.email)){
        println "${admin.profile.fullName} - ${admin.profile.email}"
        uniqueUsers.add(admin.profile.email)
      }
    }
  } else {
    println "No administrators"
  }
  
  println "\n--------\n"
}

true