import org.apache.shiro.subject.Subject
import org.apache.shiro.util.ThreadContext
import org.apache.shiro.SecurityUtils
import grails.plugins.federatedgrails.*
import aaf.fr.app.*
import aaf.fr.workflow.*
import aaf.fr.foundation.*
import aaf.fr.identity.*

/*
This script re-establishes the base FR environment once all database manipulations have been completed. You need to migrate workflow scripts following
the successful execution of this script.

In addition it re-assigns the first global administrator to continue to have access privileges. Once FR is fully operational
the UI should then be used to further assign permissions.
*/

def administratorsTargetedIDs = ["EPTID1", "EPTID2" ]

roleService = ctx.getBean("roleService")
permissionService = ctx.getBean("permissionService")

// Create federation-administrators role, used in workflows etc
// First check if the role already exists
def adminRole = Role.findByName("federation-administrators")
if (!adminRole) {
  adminRole = roleService.createRole("federation-administrators", "Role representing federation level administrators who can make decisions onbehalf of the entire federation, granted global FR access", false)
}

// Grant administrative global access permission
// Check first if the role already has the required permission, otherwise create and grant it.
if (!adminRole.permissions.findAll{it -> it.target == '*'}) {
    Permission adminPermission = new Permission(target:'*')
    adminPermission.managed = true
    adminPermission.type = Permission.defaultPerm
    permissionService.createPermission(adminPermission, adminRole)
}

// Create federation-reporting role, used to grant full reporting access to non fr wide admins
// First check if the role already exists
def reportingRole = Role.findByName("federation-reporting")
if (!reportingRole) {
  reportingRole = roleService.createRole("federation-reporting", "Access to federation wide reports for executive, management etc.", false)
}

// Grant global reports access permission
// Check first if the role already has the required permission, otherwise create and grant it.
if (!reportingRole.permissions.findAll{it -> it.target == 'federation:management:reporting'}) {
  Permission reportingAdminPermission = new Permission(target:'federation:management:reporting')
  reportingAdminPermission.managed = true
  reportingAdminPermission.type = Permission.defaultPerm
  permissionService.createPermission(reportingAdminPermission, reportingRole)
}

// Create contactmanagement-reporting role, used to grant full contact management access to non fr wide admins
// First check if the role already exists
def contactManagementRole = Role.findByName("federation-contactmanagement")
if (!contactManagementRole) {
  contactManagementRole = roleService.createRole("federation-contactmanagement", "Access to manage contacts for all IdP and SP", false)
}

// Grant global reports access permission
// Check first if the role already has the required permission, otherwise create and grant it.
if (!contactManagementRole.permissions.findAll{it -> it.target == 'federation:management:contacts'}) {
  Permission contactAdminPermission = new Permission(target:'federation:management:contacts')
  contactAdminPermission.managed = true
  contactAdminPermission.type = Permission.defaultPerm
  permissionService.createPermission(contactAdminPermission, contactManagementRole)
}

// Populate default administrative account
// First check if the account already exists
def subject = aaf.fr.identity.Subject.findByPrincipal('internaladministrator')
if (!subject) {
  subject = new aaf.fr.identity.Subject(principal:'internaladministrator', cn:'internal administrator', email:'internaladministrator@not.valid')
  subject.save(flush: true)
  if(subject.hasErrors()) {
    subject.errors.each { println it }
    return false
  }
}

def adminSubject = null
administratorsTargetedIDs.each {
  adminSubject = aaf.fr.identity.Subject.findWhere(principal:it)
  if(adminSubject)
    roleService.addMember(adminSubject, adminRole)
  else
    println "Unable to located referenced subject %s to provide administrative access. Manual intervention required." % (it)
}

println "Completed creating base FR environment"
true
