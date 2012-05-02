import org.apache.shiro.subject.Subject
import org.apache.shiro.util.ThreadContext
import org.apache.shiro.SecurityUtils
import grails.plugins.federatedgrails.*
import aaf.fr.app.*
import aaf.fr.workflow.*
import aaf.fr.foundation.*
import aaf.fr.identity.*

/*
This script re-establishes the base FR environment once all database manipulations have been completed.

In addition it re-assigns the first global administrator to continue to have access privileges. Once FR is fully operational
the UI should then be used to further assign permissions.
*/

def administratorsTargetedID = ""

roleService = ctx.getBean("roleService")
permissionService = ctx.getBean("permissionService")

// Create federation-administrators role, used in workflows etc
def adminRole = roleService.createRole("federation-administrators", "Role representing federation level administrators who can make decisions onbehalf of the entire federation, granted global FR access", false)

// Grant administrative global access permission
Permission adminPermission = new Permission(target:'*')
adminPermission.managed = true
adminPermission.type = Permission.adminPerm

permissionService.createPermission(adminPermission, adminRole)

// Create federation-reporting role, used to grant full reporting access to non fr wide admins
def reportingRole = roleService.createRole("federation-reporting", "Access to federation wide reports for executive, management etc.", false)

// Grant global reports access permission
Permission reportingAdminPermission = new Permission(target:'federation:management:reporting')
reportingAdminPermission.managed = true
reportingAdminPermission.type = Permission.adminPerm

permissionService.createPermission(reportingAdminPermission, reportingRole)

// Create contactmanagement-reporting role, used to grant full contact management access to non fr wide admins
def contactManagementRole = roleService.createRole("federation-contactmanagement", "Access to manage contacts for all IdP and SP", false)

// Grant global reports access permission
Permission contactAdminPermission = new Permission(target:'federation:management:contacts')
contactAdminPermission.managed = true
contactAdminPermission.type = Permission.adminPerm

permissionService.createPermission(contactAdminPermission, contactManagementRole)

// Populate default administrative account
def subject = new aaf.fr.identity.Subject(principal:'internaladministrator', cn:'internal administrator', email:'internaladministrator@not.valid')
subject.save(flush: true)
if(subject.hasErrors()) {
  subject.errors.each { println it }
  return false
}

def adminSubject = aaf.fr.identity.Subject.findWhere(principal:administratorsTargetedID)
if(adminSubject)
  roleService.addMember(adminSubject, adminRole)
else
  println "Unable to located referenced subject to provide administrative access. Manual intervention required."

println "Completed creating base FR environment"
true