package aaf.fr.admin

import grails.plugins.federatedgrails.*
import aaf.fr.identity.*

class SubjectController {

  def permissionService

  def index() {
    redirect(action: "list", params: params)
  }
  
  def list() {
    [subjects:Subject.list()]
  }

  def show() {
    def subject = Subject.get(params.id)
    if (!subject) {
      log.warn "No subject for $params.id located when attempting to show"
      redirect(action: "list")
      return
    }

    [subject: subject]
  }

  def showpublic() {
    def subject = Subject.get(params.id)
    if (!subject) {
      log.warn "No subject for $params.id located when attempting to show"
      redirect(action: "list")
      return
    }

    [subject: subject]
  }

  def enablesubject() {
    def subject = Subject.get(params.id)
    if (!subject) {
      log.warn "No subject for $params.id located when attempting to disablesubject"
      redirect(action: "list")
      return
    }

    subject.enabled = true
    if(!subject.save(flush:true)) {
      throw new RuntimeException("Error setting $subject to disabled state, object persistance failed") 
    }

    redirect(action: "show", id:  subject.id)
  }

  def disablesubject() {
    def subject = Subject.get(params.id)
    if (!subject) {
      log.warn "No subject for $params.id located when attempting to disablesubject"
      redirect(action: "list")
      return
    }

    subject.enabled = false
    if(!subject.save(flush:true)) {
      throw new RuntimeException("Error setting $subject to disabled state, object persistance failed") 
    }
    redirect(action: "show", id:  subject.id)
  }

  def createpermission() {
    def subject = Subject.get(params.id)
    if (!subject) {
      log.warn "No subject for $params.id located when attempting to createpermission"
      redirect(action: "list")
      return
    }

    def permission = new Permission()
    permission.type = Permission.defaultPerm
    permission.target = params.target
    permission.owner = subject
    
    permissionService.createPermission(permission, subject) 
    redirect(action: "show", id:  subject.id, fragment:"tab-permissions")
  }

  def deletepermission() {
    def subject = Subject.get(params.id)
    if (!subject) {
      log.warn "No subject for $params.id located when attempting to createpermission"
      redirect(action: "list")
      return
    }

    def permission = Permission.get(params.permID)
    if (!permission) {
      log.warn "No permission for $params.permID located when attempting to deletepermission"
      redirect(action: "list")
      return
    }

    permissionService.deletePermission(permission)
    redirect(action: "show", id:  subject.id, fragment:"tab-permissions")
  }
}