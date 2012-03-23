package aaf.fr.admin

import org.springframework.dao.DataIntegrityViolationException
import aaf.fr.foundation.*

class OrganizationTypeController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list() {
        [organizationTypeInstanceList: OrganizationType.list(), organizationTypeInstanceTotal: OrganizationType.count()]
    }

    def create() {
        [organizationTypeInstance: new OrganizationType(params)]
    }

    def save() {
        def organizationTypeInstance = new OrganizationType(params)
        if (!organizationTypeInstance.save(flush: true)) {
            render(view: "create", model: [organizationTypeInstance: organizationTypeInstance])
            return
        }

    flash.message = message(code: 'default.created.message', args: [message(code: 'organizationType.label', default: 'OrganizationType'), organizationTypeInstance.id])
        redirect(action: "show", id: organizationTypeInstance.id)
    }

    def show() {
        def organizationTypeInstance = OrganizationType.get(params.id)
        if (!organizationTypeInstance) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'organizationType.label', default: 'OrganizationType'), params.id])
            redirect(action: "list")
            return
        }

        [organizationTypeInstance: organizationTypeInstance]
    }

    def edit() {
        def organizationTypeInstance = OrganizationType.get(params.id)
        if (!organizationTypeInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'organizationType.label', default: 'OrganizationType'), params.id])
            redirect(action: "list")
            return
        }

        [organizationTypeInstance: organizationTypeInstance]
    }

    def update() {
        def organizationTypeInstance = OrganizationType.get(params.id)
        if (!organizationTypeInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'organizationType.label', default: 'OrganizationType'), params.id])
            redirect(action: "list")
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (organizationTypeInstance.version > version) {
                organizationTypeInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'organizationType.label', default: 'OrganizationType')] as Object[],
                          "Another user has updated this OrganizationType while you were editing")
                render(view: "edit", model: [organizationTypeInstance: organizationTypeInstance])
                return
            }
        }

        organizationTypeInstance.properties = params

        if (!organizationTypeInstance.save(flush: true)) {
            render(view: "edit", model: [organizationTypeInstance: organizationTypeInstance])
            return
        }

    flash.message = message(code: 'default.updated.message', args: [message(code: 'organizationType.label', default: 'OrganizationType'), organizationTypeInstance.id])
        redirect(action: "show", id: organizationTypeInstance.id)
    }

    def delete() {
        def organizationTypeInstance = OrganizationType.get(params.id)
        if (!organizationTypeInstance) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'organizationType.label', default: 'OrganizationType'), params.id])
            redirect(action: "list")
            return
        }

        try {
            organizationTypeInstance.delete(flush: true)
      flash.message = message(code: 'default.deleted.message', args: [message(code: 'organizationType.label', default: 'OrganizationType'), params.id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
      flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'organizationType.label', default: 'OrganizationType'), params.id])
            redirect(action: "show", id: params.id)
        }
    }
}
