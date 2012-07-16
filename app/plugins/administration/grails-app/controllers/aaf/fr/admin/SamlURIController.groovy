package aaf.fr.admin

import org.springframework.dao.DataIntegrityViolationException
import aaf.fr.foundation.*

class SamlURIController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list() {
        [samlURIInstanceList: SamlURI.list(), samlURIInstanceTotal: SamlURI.count()]
    }

    def create() {
        [samlURIInstance: new SamlURI(params)]
    }

    def save() {
        def samlURIInstance = new SamlURI(params)
        if (!samlURIInstance.save(flush: true)) {
            render(view: "create", model: [samlURIInstance: samlURIInstance])
            return
        }

    flash.message = message(code: 'default.created.message', args: [message(code: 'samlURI.label', default: 'SamlURI'), samlURIInstance.id])
        redirect(action: "show", id: samlURIInstance.id)
    }

    def show() {
        def samlURIInstance = SamlURI.get(params.id)
        if (!samlURIInstance) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'samlURI.label', default: 'SamlURI'), params.id])
            redirect(action: "list")
            return
        }

        [samlURIInstance: samlURIInstance]
    }

    def edit() {
        def samlURIInstance = SamlURI.get(params.id)
        if (!samlURIInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'samlURI.label', default: 'SamlURI'), params.id])
            redirect(action: "list")
            return
        }

        [samlURIInstance: samlURIInstance]
    }

    def update() {
        def samlURIInstance = SamlURI.get(params.id)
        if (!samlURIInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'samlURI.label', default: 'SamlURI'), params.id])
            redirect(action: "list")
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (samlURIInstance.version > version) {
                samlURIInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'samlURI.label', default: 'SamlURI')] as Object[],
                          "Another user has updated this SamlURI while you were editing")
                render(view: "edit", model: [samlURIInstance: samlURIInstance])
                return
            }
        }

        samlURIInstance.properties = params

        if (!samlURIInstance.save(flush: true)) {
            render(view: "edit", model: [samlURIInstance: samlURIInstance])
            return
        }

    flash.message = message(code: 'default.updated.message', args: [message(code: 'samlURI.label', default: 'SamlURI'), samlURIInstance.id])
        redirect(action: "show", id: samlURIInstance.id)
    }

    def delete() {
        def samlURIInstance = SamlURI.get(params.id)
        if (!samlURIInstance) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'samlURI.label', default: 'SamlURI'), params.id])
            redirect(action: "list")
            return
        }

        try {
            samlURIInstance.delete(flush: true)
      flash.message = message(code: 'default.deleted.message', args: [message(code: 'samlURI.label', default: 'SamlURI'), params.id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
      flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'samlURI.label', default: 'SamlURI'), params.id])
            redirect(action: "show", id: params.id)
        }
    }
}
